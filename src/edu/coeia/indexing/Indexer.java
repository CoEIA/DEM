/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.main.util.FilesPath;

import org.apache.lucene.index.IndexWriter;

import java.io.File ;

/**
 * Abstract Class for defining Index object
 * @author wajdyessam
 */

public abstract class Indexer {
    
    public Indexer(IndexWriter writer, File file, String mimeType, boolean imageCaching, String caseLocation, ImageExtractor imageExtractor) {
        this.file = file ;
        this.mimeType = mimeType; 
        this.imageCache = imageCaching;
        this.imageExtractor = imageExtractor;
        this.imagesLocation = caseLocation + "\\" + FilesPath.IMAGES_PATH;
        this.tmpLocation = caseLocation + "\\" + FilesPath.CASE_TMP;
        this.caseLocation = caseLocation;
        this.writer = writer ;
        
        createTmpImageFolder(); // used when indexer called by archiveIndexer
    }
    
    private void createTmpImageFolder() {
        File tmpImageFile = new File(this.imagesLocation);
        if ( ! tmpImageFile.exists() )
            tmpImageFile.mkdir();
    }
    
    public abstract boolean doIndexing();
    
    protected File file ;
    protected String mimeType ;
    protected boolean imageCache ;
    protected String imagesLocation ;
    protected String tmpLocation ;
    protected String caseLocation;
    
    protected static int id = 1;
    protected ImageExtractor imageExtractor;
    
    protected IndexWriter writer;
}
