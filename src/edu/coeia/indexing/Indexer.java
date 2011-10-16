/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.util.FilesPath;

import org.apache.lucene.index.IndexWriter;

import org.apache.tika.metadata.Metadata;

import java.util.HashSet;
import java.util.Set;
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
        
        createCaseDataFolders(); // used when indexer called by archiveIndexer
    }
    
    private void createCaseDataFolders() {
        File tmpImageFile = new File(this.imagesLocation);
        if ( ! tmpImageFile.exists() )
            tmpImageFile.mkdir();
        
        File tmpFile = new File(this.tmpLocation);
        if ( !tmpFile.exists() )
            tmpFile.mkdir();
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
    
    protected static final Set<String> indexedMetadataFields = new HashSet<String>();
    static {
        indexedMetadataFields.add(Metadata.TITLE);
        indexedMetadataFields.add(Metadata.AUTHOR);
        indexedMetadataFields.add(Metadata.COMMENTS);
        indexedMetadataFields.add(Metadata.KEYWORDS);
        indexedMetadataFields.add(Metadata.DESCRIPTION);
        indexedMetadataFields.add(Metadata.SUBJECT);
    }
}
