/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

/**
 *
 * @author wajdyessam
 */

public final class CaseHistory {
    private final String caseName ;
    private final String lastModified ;
    private final boolean isCaseIndexed ;
    private final long numberOfItemsIndexed ;
    private final long caseSize ;

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
