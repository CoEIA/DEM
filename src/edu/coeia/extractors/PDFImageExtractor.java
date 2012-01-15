/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.extractors;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.indexing.Indexer;
import java.io.File ;

public final class PDFImageExtractor implements ImageExtractor {
    
    @Override
    public void extractImages(Indexer indexer, File file,int parentId) {
        
        // extracting images
        TikaObjectExtractor extractor = TikaObjectExtractor.getExtractor(file.getAbsolutePath(), indexer.getTmpLocation(),  
             TikaObjectExtractor.OBJECT_TYPE.CONTAINER);
        
        TikaObjectExtractor.EmbeddedObjectHandler handler = extractor.extract();
        
        // index the images using ImageIndexer
        for(TikaObjectExtractor.ObjectLocation location: handler.getLocations()) {
            try {
                indexer.getLuceneIndex().indexFile(new File(location.newFilePath), parentId, indexer.getDialog());
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
