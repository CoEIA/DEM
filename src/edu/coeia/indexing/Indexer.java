/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.cases.CaseFacade;
import edu.coeia.extractors.ImageExtractor;

import java.io.File ;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;

/**
 * Abstract Class for defining Index object
 * @author wajdyessam
 */

public abstract class Indexer {
    
    public Indexer(IndexerManager luceneIndex, File file, String mimeType, ImageExtractor imageExtractor) {
        this.file = file ;
        this.mimeType = mimeType; 
        this.caseFacade = luceneIndex.getCaseFacade();
        this.imageCache = this.caseFacade.getCase().getCacheImages();
        this.imageExtractor = imageExtractor;
        this.caseLocation = this.caseFacade.getCase().getCaseLocation();
        this.imagesLocation = this.caseFacade.getCaseImageFolderLocation();
        this.tmpLocation = this.caseFacade.getCaseArchiveOutputFolderLocation();
        this.luceneIndex = luceneIndex ;
    }
    
    public abstract boolean doIndexing();
    
    protected boolean indexDocument(final Document document) throws CorruptIndexException, IOException {
        boolean status  = false;
        
        int objectId = this.getId();

        if (document != null) {
            this.luceneIndex.addDocument(document);    // index file
            this.increaseId();      // increase the id counter if file indexed successfully

            // cache images with id as parent id
            if ( this.isImageCache() ) {
                this.getImageExtractor().extractImages(this, this.getFile(), objectId);
            }

            status = true;
        }
        
        return status;
    }
    
    public File getFile() { return this.file ; }
    public String getMimeType() { return this.mimeType ;}
    public boolean isImageCache() { return this.imageCache; }
    public String getImagesLocation() { return this.imagesLocation ;}
    public String getTmpLocation() { return this.tmpLocation ; }
    public String getCaseLocation() { return this.caseLocation; }
    public IndexerManager getLuceneIndex() { return this.luceneIndex; }
    public ImageExtractor getImageExtractor() { return this.imageExtractor; }
    public CaseFacade getCaseFacade() { return this.caseFacade ; }
    
    public int getId() { return id ; }
    public void increaseId() { id++; }
    
    public int getParentId() { return parentId; }
    public void setParentId(int aParentId) { parentId = aParentId; }
    
    public void setCrawler(final CrawlerIndexerThread crawler) { this.crawler = crawler; }
    public CrawlerIndexerThread getCrawler() { return this.crawler; }
    
    private static int id = 1;
    private static int parentId = 0;
    
    private final File file ;
    private final String mimeType ;
    private final boolean imageCache ;
    private final String imagesLocation ;
    private final String tmpLocation ;
    private final String caseLocation;
    
    private final ImageExtractor imageExtractor;
    private final IndexerManager luceneIndex ;
    private final CaseFacade caseFacade ;
    
    private CrawlerIndexerThread crawler;
}
