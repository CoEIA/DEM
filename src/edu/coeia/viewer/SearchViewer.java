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
public class SearchViewer {
    
    public SearchViewer(final String keyword,final int documentId, final List<Integer> ids) {
        this.keyword = keyword;
        this.documentId = documentId;
        this.documentsIdNumbers = new ArrayList<Integer>();
        this.documentsIdNumbers.addAll(Collections.unmodifiableList(ids));
    }
    
    public String getKeyword() { return this.keyword ; }
    public int getDocumentId() { return this.documentId; }
    public List<Integer> getDocumentIds() { return Collections.unmodifiableList(this.documentsIdNumbers); }
    
    private final String keyword;
    private final int documentId; 
    private final List<Integer> documentsIdNumbers;
}
