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

public class OfficeImageExtractor implements ImageExtractor{
    @Override
    public void extractImages(IndexWriter writer, File file, String distenationFolder, int parentId) {
        // extract images in tmp location
        String tmpLocation = "C:\\out" ;
        
        // extracting images
        TikaObjectExtractor extractor = TikaObjectExtractor.getExtractor(file.getAbsolutePath(), tmpLocation,
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
