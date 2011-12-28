/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.extractors.ImageExtractor;
import java.io.File;

/**
 *
 * @author wajdyessam
 */
final class NonDocumentIndexer extends Indexer{
    
    private int parentId ;
    
    /*
     * static factory method to get an instance of DocumentIndexer
     */
    public static NonDocumentIndexer newInstance (LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor) {
        return new NonDocumentIndexer(luceneIndex, file, mimeType, imageExtractor, 0);
    }
     
    /*
     * static factory method to get an instance of DocumentIndexer
     */
    public static NonDocumentIndexer newInstance (LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor, int parentId) {
        return new NonDocumentIndexer(luceneIndex, file, mimeType, imageExtractor, parentId);
    }
    
    private NonDocumentIndexer(LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor,int parentId) {
        super(luceneIndex, file,mimeType, imageExtractor);
        this.parentId = parentId ;
    }
    
    @Override
    public boolean doIndexing() {
        return false;
    }
    
}
