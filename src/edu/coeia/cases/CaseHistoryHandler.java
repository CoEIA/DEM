/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

/**
 * Retrieve, Put and Remove Case History Information (case name, 
 * last modification time, case status i.e is indexed or not, 
 * the number of items in the case, the size of evidence source file)
 * this information is not stored in case file.DAT because
 * it's will modified each time case is indexed (mutable information)
 * the current implementation use java preference (registry) to store
 * and handle this informations
 * 
 * @author wajdyessam
 */

import java.util.prefs.Preferences ;

import java.util.prefs.BackingStoreException;
import static edu.coeia.util.PreconditionsChecker.* ;

public final class CaseHistoryHandler {
   
    /**
     * constant used for storing variable into registry (the default
     * implementations for java preferences API
     */
    private static final String DEM_CASES_NODES_PATH = "/com/dem/cases/"; 
    
    private static final String CASE_NAME = "CASE_NAME" ;
    private static final String CASE_TIME = "CASE_TIME" ;
    private static final String CASE_STATUS = "CASE_STATUS" ;
    private static final String CASE_ITEMS = "CASE_ITEMS" ;
    private static final String CASE_SIZE = "CASE_SIZE" ;
    
    /**
     * Write Case information into preference locations
     * the case must be indexed before written into storing location
     * or it will not written on registry 
     */
    public static void set (final CaseHistory caseHistory) {
        checkNull("caseHistory cannot be null object", caseHistory);

        if ( caseHistory.getIsCaseIndexed() ) {
            Preferences root = Preferences.userRoot();
            Preferences node = root.node(CaseHistoryHandler.DEM_CASES_NODES_PATH + caseHistory.caseName);
            
            // write history
            node.put(CASE_NAME, caseHistory.caseName);
            node.put(CASE_TIME, caseHistory.lastModified);
            node.putBoolean(CASE_STATUS, caseHistory.isCaseIndexed);
            node.putLong(CASE_ITEMS, caseHistory.numberOfItemsIndexed);
            node.putLong(CASE_SIZE, caseHistory.caseSize);
        }
    }
    
    /**
     * Get case history information
     * @param caseName the name of case we want to extract its history
     * @return CaseHistory object that hold history information for this <tt>caseName</tt>
     * @throws NullPointerException if <tt>caseName</tt> is not exists
     */
    public static CaseHistory get (final String caseName) {
        checkNull("case name must be not null", caseName);
        checkNotEmptyString("case name must have value", caseName);
        
        Preferences root = Preferences.userRoot();
        
        CaseHistory history = null ;

        Preferences node = root.node(CaseHistoryHandler.DEM_CASES_NODES_PATH + caseName) ;

        history = CaseHistory.newInstance(
            node.get(CASE_NAME, caseName),
            node.get(CASE_TIME, "Case is not modified"),
            node.getBoolean(CASE_STATUS, false),
            node.getLong(CASE_ITEMS, 0),
            node.getLong(CASE_SIZE, 0)
        );

        return history;
    }
    
    /**
     * Remove CaseHistory information for locations
     * the path to this must be existed first
     * @param caseName the name of case to be deleted
     * @return status flag if deleted or not
     */
    static boolean remove (final String caseName) {
        checkNull("case name must be not null", caseName);
        checkNotEmptyString("case name must have value", caseName);
        
        boolean status = false; 
        Preferences root = Preferences.userRoot();
        
        try {
            if ( root.nodeExists(CaseHistoryHandler.DEM_CASES_NODES_PATH + caseName)) {
                Preferences node = root.node(CaseHistoryHandler.DEM_CASES_NODES_PATH + caseName) ;
                node.removeNode();
                status = true;
            }
        }
        catch(BackingStoreException e) {
            throw new NullPointerException("Cannot find Histoy for this case: " + caseName);
        }
                
        return status;
    }
    
    /**
     * Container for holding case history information
     */
    public static final class CaseHistory {
        private String caseName ;
        private String lastModified ;
        private boolean isCaseIndexed ;
        private long numberOfItemsIndexed ;
        private long caseSize ;
        
        public static CaseHistory newInstance(String cName, String lm, boolean isIndexed, 
                long itemsCount, long size) {
            
            return new CaseHistory(cName, lm, isIndexed, itemsCount, size);
        }
        
        private CaseHistory(String cName, String lm, boolean isIndexed, long itemsCount, long size) {
            this.lastModified = lm;
            this.caseName = cName ;
            this.isCaseIndexed = isIndexed; 
            this.numberOfItemsIndexed = itemsCount ;
            this.caseSize = size; 
        }
        
        public String getLastModified() { return this.lastModified ;}
        public String getCaseName() { return this.caseName ; }
        public boolean getIsCaseIndexed() { return this.isCaseIndexed ; }
        public long getNumberOfItemsIndexed() { return this.numberOfItemsIndexed ; }
        public long getCaseSize() { return this.caseSize ;}
    }
}
