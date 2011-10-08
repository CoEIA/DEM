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

public class PDFImageExtractor implements ImageExtractor {
    @Override
    public void extractImages(File file, String distenationFolder) {
        TikaImageExtractor extractor = TikaImageExtractor.getExtractor(file.getAbsolutePath(), distenationFolder);
        TikaImageExtractor.EmbeddedObjectHandler handler = extractor.extract();
    }
}
