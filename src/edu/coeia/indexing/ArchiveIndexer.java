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
    
    private int parentId ;
    
    public static ArchiveIndexer newInstance (IndexWriter writer, File file, String mimeType, boolean imageCaching, String caseLocation, ImageExtractor imageExtractor,
            int parentId) {
        return new ArchiveIndexer(writer, file, mimeType, imageCaching, caseLocation, imageExtractor, parentId);
    }
        
    public static ArchiveIndexer newInstance (IndexWriter writer, File file, String mimeType, 
            boolean imageCaching, String caseLocation, ImageExtractor imageExtractor) {
        return new ArchiveIndexer(writer, file, mimeType, imageCaching, caseLocation, imageExtractor, 0);
    }
        
    private ArchiveIndexer(IndexWriter writer, File file, String mimeType, 
            boolean imageCaching, String caseLocation, ImageExtractor imageExtractor,int parentId) {
        super(writer, file,mimeType, imageCaching, caseLocation, imageExtractor);
        this.parentId = parentId ;
    }
        
    @Override
    public boolean doIndexing() {
        String folderName = this.tmpLocation;
        
        // extract all the archive content in temp folder
        TikaObjectExtractor.EmbeddedObjectHandler handler = TikaObjectExtractor.getExtractor(
                this.file.getAbsolutePath(), folderName,
                TikaObjectExtractor.OBJECT_TYPE.ARCHIVE).extract();
        
        if ( handler != null ) {
            for(TikaObjectExtractor.ObjectLocation location: handler.getLocations()) {
                System.out.println("object: " + location.oldFilePath + " , " + location.newFilePath);
                try {
                    LuceneIndex.indexFile(new File(location.newFilePath), parentId);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        return true;
    }
}
