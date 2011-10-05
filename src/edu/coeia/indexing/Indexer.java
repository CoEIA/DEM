/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import org.apache.lucene.index.IndexWriter;
import java.io.File ;

/**
 * Abstract Class for defining Index object
 * @author wajdyessam
 */

public abstract class Indexer {
    
    public Indexer(File file, String mimeType, boolean imageCaching, ImageExtractor imageExtractor) {
        this.file = file ;
        this.mimeType = mimeType; 
        this.imageCache = imageCaching;
        this.imageExtractor = imageExtractor;
    }
    
    public abstract boolean doIndexing (IndexWriter writer);
    
    protected File file ;
    protected String mimeType ;
    protected boolean imageCache ;
    
    protected ImageExtractor imageExtractor;
}
