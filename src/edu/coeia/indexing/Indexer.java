/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.extractors.ImageExtractor;
import edu.coeia.util.FilesPath;

import java.util.HashSet;
import java.util.Set;

import java.io.File ;

import org.apache.tika.metadata.Metadata;

/**
 * Abstract Class for defining Index object
 * @author wajdyessam
 */

public abstract class Indexer {
    
    public Indexer(LuceneIndex luceneIndex, File file, String mimeType, ImageExtractor imageExtractor) {
        this.file = file ;
        this.mimeType = mimeType; 
        this.imageCache = luceneIndex.getCase().getCacheImages();
        this.imageExtractor = imageExtractor;
        this.caseLocation = luceneIndex.getCase().getCaseLocation();
        this.imagesLocation = caseLocation + "\\" + FilesPath.IMAGES_PATH;
        this.tmpLocation = caseLocation + "\\" + FilesPath.CASE_TMP;
        this.luceneIndex = luceneIndex ;
        
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
    
    public File getFile() { return this.file ; }
    public String getMimeType() { return this.mimeType ;}
    public boolean isImageCache() { return this.imageCache; }
    public String getImagesLocation() { return this.imagesLocation ;}
    public String getTmpLocation() { return this.tmpLocation ; }
    public String getCaseLocation() { return this.caseLocation; }
    public LuceneIndex getLuceneIndex() { return this.luceneIndex; }
    public ImageExtractor getImageExtractor() { return this.imageExtractor; }
    
    public int getId() { return id ; }
    public void increaseId() { id++; }
    
    protected static final Set<String> indexedMetadataFields = new HashSet<String>();
    static {
        indexedMetadataFields.add(Metadata.TITLE);
        indexedMetadataFields.add(Metadata.AUTHOR);
        indexedMetadataFields.add(Metadata.COMMENTS);
        indexedMetadataFields.add(Metadata.KEYWORDS);
        indexedMetadataFields.add(Metadata.DESCRIPTION);
        indexedMetadataFields.add(Metadata.SUBJECT);
    }
    
    private static int id = 1;
    
    private final File file ;
    private final String mimeType ;
    private final boolean imageCache ;
    private final String imagesLocation ;
    private final String tmpLocation ;
    private final String caseLocation;
    private final ImageExtractor imageExtractor;
    private final LuceneIndex luceneIndex ;
}
