/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

/**
 * Retrieve, Put and Remove Case History Information
 * this information is not stored in case file.DAT because
 * it's will modified each time case is indexed (mutable information)
 * the current implementation use java preference (registry) to store
 * and handle this informations
 * 
 * @author wajdyessam
 */

import java.util.prefs.Preferences ;
import java.util.Date ;

import java.util.prefs.BackingStoreException;
import static edu.coeia.util.PreconditionsChecker.* ;

final class CaseHistoryHandler {
   
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
     * @param aCase the case to be written its history
     * @param itemsCount the number of items indexed in the case
     * @param caseSize the size of evidence source
     */
    static void set (final Case aCase, final long itemsCount, final long caseSize) {
        checkNull("case cannot be null object", aCase);
        
        String caseName = aCase.getIndexName() ;
        
        if ( aCase.getIndexStatus() ) {
            Preferences root = Preferences.userRoot();
            Preferences node = root.node(CaseHistoryHandler.DEM_CASES_NODES_PATH + caseName);
            
            // write history
            node.put(CASE_NAME, caseName);
            node.put(CASE_TIME, new Date().toString());
            node.putBoolean(CASE_STATUS, true);
            node.putLong(CASE_ITEMS, itemsCount);
            node.putLong(CASE_SIZE, caseSize);
        }
    }
    
    /**
     * Get case history information
     * @param caseName the name of case we want to extract its history
     * @return CaseHistory object that hold history information for this <tt>caseName</tt>
     */
    static CaseHistory get (final String caseName) {
        checkNull("case name must be not null", caseName);
        checkNotEmptyString("case name must have value", caseName);
        
        Preferences root = Preferences.userRoot();
        
        CaseHistory history = null ;
        
        try {
            if ( root.nodeExists(CaseHistoryHandler.DEM_CASES_NODES_PATH + caseName)) {
                Preferences node = root.node(CaseHistoryHandler.DEM_CASES_NODES_PATH + caseName) ;
                
                history = CaseHistory.newInstance(
                    node.get(CASE_NAME, caseName),
                    node.get(CASE_TIME, new Date().toString()),
                    node.getBoolean(CASE_STATUS, false),
                    node.getLong(CASE_ITEMS, 0),
                    node.getLong(CASE_SIZE, 0)
                );
            }
        }
        catch(BackingStoreException e) {
            throw new NullPointerException("Cannot find Histoy for this case: " + caseName);
        }
        
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
    static final class CaseHistory {
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
