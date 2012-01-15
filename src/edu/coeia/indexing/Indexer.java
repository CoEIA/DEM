/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.cases.CasePathHandler;
import edu.coeia.extractors.ImageExtractor;
import edu.coeia.util.FilesPath;

import java.io.File ;

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
        this.imagesLocation = this.caseLocation + "\\" + FilesPath.IMAGES_PATH;
        this.tmpLocation = this.caseLocation + "\\" + FilesPath.CASE_TMP;
        this.luceneIndex = luceneIndex ;
        this.pathHandler = CasePathHandler.newInstance(this.caseLocation);
        
        // read the mapping from file
        try {
            this.pathHandler.readConfiguration();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public abstract boolean doIndexing();
    
    /** to update the current GUI dialog, to display what type of file
     * the indexer work on it
     * @param dialog the GUI that have the progress bar and error table
     */
    public void setGUIDialog(final IndexingDialog dialog) { this.indexingDialog = dialog; }
    
    public File getFile() { return this.file ; }
    public String getMimeType() { return this.mimeType ;}
    public boolean isImageCache() { return this.imageCache; }
    public String getImagesLocation() { return this.imagesLocation ;}
    public String getTmpLocation() { return this.tmpLocation ; }
    public String getCaseLocation() { return this.caseLocation; }
    public LuceneIndex getLuceneIndex() { return this.luceneIndex; }
    public ImageExtractor getImageExtractor() { return this.imageExtractor; }
    public CasePathHandler getPathHandler() { return this.pathHandler; }
    public IndexingDialog getDialog() { return this.indexingDialog ; }
    
    public int getId() { return id ; }
    public void increaseId() { id++; }
    
    private static int id = 1;
    
    private final File file ;
    private final String mimeType ;
    private final boolean imageCache ;
    private final String imagesLocation ;
    private final String tmpLocation ;
    private final String caseLocation;
    private final ImageExtractor imageExtractor;
    private final LuceneIndex luceneIndex ;
    private final CasePathHandler pathHandler;
    private IndexingDialog indexingDialog;
}
