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

public final class OfficeImageExtractor implements ImageExtractor{
    
    @Override
    public void extractImages(Indexer indexer, File file,int parentId) {

        if ( file.isDirectory() )
            return ;
        
        // extracting images
        TikaObjectExtractor extractor = TikaObjectExtractor.newInstance(indexer, 
                file.getAbsolutePath(), indexer.getTmpLocation(),
            TikaObjectExtractor.OBJECT_TYPE.CONTAINER);
        
        TikaObjectExtractor.EmbeddedObjectCollections handler  = null;
        try {
            handler = extractor.extract();
                    // index the images using ImageIndexer
            for(TikaObjectExtractor.ExtractedObjectInfo location: handler.getLocations()) {
                try {
                    indexer.getLuceneIndex().indexFile(new File(location.getFileNewPath()), parentId, indexer.getDialog());
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
