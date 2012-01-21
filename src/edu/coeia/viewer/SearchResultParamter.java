/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.viewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author wajdyessam
 */
public class SearchResultParamter {
    
    public SearchResultParamter(final String keyword,final int documentId, final List<Integer> ids) {
        this.keyword = keyword;
        this.currentDocumentId = documentId;
        this.documentsIdNumbers = new ArrayList<Integer>();
        this.documentsIdNumbers.addAll(Collections.unmodifiableList(ids));
    }
    
    public String getKeyword() { return this.keyword ; }
    public int getCurrentDocumentId() { return this.currentDocumentId; }
    public List<Integer> getDocumentIds() { return Collections.unmodifiableList(this.documentsIdNumbers); }
    
    private final String keyword;
    private final int currentDocumentId; 
    private final List<Integer> documentsIdNumbers;
}
