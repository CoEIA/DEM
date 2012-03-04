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
import java.util.Date;
import java.util.List;

final class ArchiveIndexer extends Indexer {
    
    public static ArchiveIndexer newInstance (IndexerManager luceneIndex, File file, String mimeType, 
           ImageExtractor imageExtractor, int parentId) {
        return new ArchiveIndexer(luceneIndex, file, mimeType, imageExtractor, parentId);
    }
        
    public static ArchiveIndexer newInstance (IndexerManager luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor) {
        return new ArchiveIndexer(luceneIndex, file, mimeType, imageExtractor, 0);
    }
        
    private ArchiveIndexer(IndexerManager luceneIndex, File file, String mimeType, 
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
            NonDocumentIndexer.newInstance(this.getIndexerManager(), this.getFile(), this.getMimeType(),
                    new NoneImageExtractor(), this.getParentId()).doIndexing();
            
            // then extract its content with parent id to zip id
            String destinationOfExtractionFiles = this.getTmpLocation();

            // extract and save all the archive content in destination folder
            EmbeddedObjectCollections handler = TikaObjectExtractor.newInstance(
                    this,
                    this.getFile().getAbsolutePath(), destinationOfExtractionFiles,
                    TikaObjectExtractor.OBJECT_TYPE.ARCHIVE).extract();

            // update gui with content
            //this.updateGUI(handler);
            
            // then index each file inside archive file
            if ( handler != null ) {
                for(ExtractedObjectInfo location: handler.getLocations()) {
                    this.getIndexerManager().indexFile(
                                new File(location.getFileNewPath()), 
                                currentDocumentId
                            );
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
        List<String> embeddedDocs = new ArrayList<String>();
        for(ExtractedObjectInfo info: handler.getLocations()) {
            embeddedDocs.add(info.getFileName());
        }
        
        FileSystemCrawlingProgressPanel.FileSystemCrawlerData data = new FileSystemCrawlingProgressPanel.FileSystemCrawlerData(
                this.getFile().getAbsolutePath(),
                SizeUtil.getSize(getFile().getPath()),
                FileUtil.getExtension(getFile().getPath()),
                new Date(getFile().lastModified()).toString(),
                embeddedDocs
        );
    }
}
