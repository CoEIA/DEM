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
    public void extractImages(File file, String distenationFolder) {
        TikaObjectExtractor extractor = TikaObjectExtractor.getExtractor(file.getAbsolutePath(), distenationFolder,
                TikaObjectExtractor.OBJECT_TYPE.CONTAINER);
        
        TikaObjectExtractor.EmbeddedObjectHandler handler = extractor.extract();
    }
}
