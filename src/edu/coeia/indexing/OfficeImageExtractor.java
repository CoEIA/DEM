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

public class OfficeImageExtractor implements ImageExtractor{
    
    @Override
    public void extractImages(Indexer indexer, File file,int parentId) {

        // extracting images
        TikaObjectExtractor extractor = TikaObjectExtractor.getExtractor(file.getAbsolutePath(), indexer.tmpLocation,
            TikaObjectExtractor.OBJECT_TYPE.CONTAINER);
        
        TikaObjectExtractor.EmbeddedObjectHandler handler = extractor.extract();
        
        // index the images using ImageIndexer
        for(TikaObjectExtractor.ObjectLocation location: handler.getLocations()) {
            try {
                LuceneIndexer.indexFile(new File(location.newFilePath), parentId);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
