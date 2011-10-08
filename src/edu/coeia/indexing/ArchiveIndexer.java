/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import java.io.File ;

import org.apache.lucene.index.IndexWriter ;

public class ArchiveIndexer extends Indexer {
    
    public ArchiveIndexer(File file, String mimeType, boolean imageCaching, String location, ImageExtractor imageExtractor) {
        super(file,mimeType, imageCaching, location, imageExtractor);
    }
        
    @Override
    public boolean doIndexing(IndexWriter writer) {
        return false;
    }
    
}
