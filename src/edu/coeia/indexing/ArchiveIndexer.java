/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import com.pff.PSTException;
import java.io.File ;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.lucene.index.IndexWriter ;
import org.apache.tika.exception.TikaException;

public class ArchiveIndexer extends Indexer {
    
    public ArchiveIndexer(File file, String mimeType, boolean imageCaching, String caseLocation, ImageExtractor imageExtractor) {
        super(file,mimeType, imageCaching, caseLocation, imageExtractor);
    }
        
    @Override
    public boolean doIndexing(IndexWriter writer) {
        // create temporary folder
        String folderName = this.tmpLocation + "\\" + "TMP_" + this.file.getName();
        File folder = new File(folderName);
        folder.mkdir();
        
        // extract all the archive content in temp folder
        TikaObjectExtractor.EmbeddedObjectHandler handler = TikaObjectExtractor.getExtractor(this.file.getAbsolutePath(), folderName, TikaObjectExtractor.OBJECT_TYPE.ARCHIVE).extract();
        
        if ( handler != null ) {
            for(TikaObjectExtractor.ObjectLocation location: handler.getLocations()) {
                System.out.println("object: " + location.oldFilePath + " , " + location.newFilePath);
                try {
                    indexFile(new File(location.newFilePath) , writer, folderName);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        // try indexing the content
        // remove the folder
        
        
        return false;
    }
    
    public boolean indexFile(File file, IndexWriter writer, String location)  
            throws IOException, FileNotFoundException, PSTException, TikaException {
   
        try {
            Indexer indexType = IndexerFactory.getIndexer(file, this.imageCache, location);
            return indexType.doIndexing(writer);
        }
        catch(UnsupportedOperationException e){
            System.out.println(e.getMessage());
        }
        
        return false;
    }
    
}
