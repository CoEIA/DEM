/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.viewer;

import edu.coeia.searching.LuceneSearcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author wajdyessam
 */
public class SearchViewer {
    
    public SearchViewer(final String keyword, final LuceneSearcher searcher, 
            final int document, final List<Integer> ids) {
        
        this.keyword = keyword;
        this.luceneSearcher = searcher;
        this.currentDocument = document;
        this.documentsIdNumbers = new ArrayList<Integer>();
        this.documentsIdNumbers.addAll(Collections.unmodifiableList(ids));
    }
    
    public String getKeyword() { return this.keyword ; }
    public LuceneSearcher getLuceneSearcher() { return this.luceneSearcher; }
    public int getCurrentDocument() { return this.currentDocument; }
    public List<Integer> getDocumentIds() { return Collections.unmodifiableList(this.documentsIdNumbers); }
    
    private final String keyword;
    private final LuceneSearcher luceneSearcher ;
    private final int currentDocument; 
    private final List<Integer> documentsIdNumbers;
}
