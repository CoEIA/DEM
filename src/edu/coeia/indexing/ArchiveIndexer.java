/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.indexing.dialogs.FileSystemCrawlingProgressPanel;
import edu.coeia.extractors.ImageExtractor;
import edu.coeia.extractors.NoneImageExtractor;
import edu.coeia.extractors.TikaObjectExtractor;
import edu.coeia.extractors.TikaObjectExtractor.EmbeddedObjectCollections;
import edu.coeia.extractors.TikaObjectExtractor.ExtractedObjectInfo;
import edu.coeia.util.FileUtil;
import edu.coeia.util.SizeUtil;

import java.io.File ;

import java.util.ArrayList;
import java.util.List;

final class ArchiveIndexer extends Indexer {
    
    public static ArchiveIndexer newInstance (LuceneIndex luceneIndex, File file, String mimeType, 
           ImageExtractor imageExtractor, int parentId) {
        return new ArchiveIndexer(luceneIndex, file, mimeType, imageExtractor, parentId);
    }
        
    public static ArchiveIndexer newInstance (LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor) {
        return new ArchiveIndexer(luceneIndex, file, mimeType, imageExtractor, 0);
    }
        
    private ArchiveIndexer(LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor,int parentId) {
        super(luceneIndex, file,mimeType, imageExtractor);
        this.setParentId(parentId);
    }
        
    @Override
    public boolean doIndexing() {
        boolean status = false ;
        
        try {
            // index the zip folder with metadata only
            int currentDocumentId = this.getId();
            
            // index the document itslef, without text extraction
            NonDocumentIndexer.newInstance(this.getLuceneIndex(), this.getFile(), this.getMimeType(),
                    new NoneImageExtractor(), this.getParentId()).doIndexing();
            
            // then extract its content with parent id to zip id
            String destinationOfExtractionFiles = this.getTmpLocation();

            // extract and save all the archive content in destination folder
            EmbeddedObjectCollections handler = TikaObjectExtractor.newInstance(
                    this,
                    this.getFile().getAbsolutePath(), destinationOfExtractionFiles,
                    TikaObjectExtractor.OBJECT_TYPE.ARCHIVE).extract();

            // update gui with content
            this.updateGUI(handler);
            
            // then index each file inside archive file
            if ( handler != null ) {
                for(ExtractedObjectInfo location: handler.getLocations()) {
                    this.getLuceneIndex().indexFile(
                            new File(location.getFileNewPath()), currentDocumentId, this.getDialog());
                }
            }

            status = true;
        }
        catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
        
        return status; 
    }
    
    private void updateGUI(final EmbeddedObjectCollections handler) {
        FileSystemCrawlingProgressPanel panel = new FileSystemCrawlingProgressPanel();
        panel.setCurrentFile(getFile().getPath());
        panel.setFileSize(SizeUtil.getSize(getFile().getPath()));
        panel.setFileExtension(FileUtil.getExtension(getFile().getPath()));
        
        List<String> files = new ArrayList<String>();
        for(ExtractedObjectInfo info: handler.getLocations()) {
            files.add(info.getFileName());
        }
        
        panel.setEmbeddedDocuments(files);
        getDialog().changeProgressPanel(panel);
    }
}
