/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 * Image Extractor
 * Interface for images extractor from different type of files
 * @author wajdyessam
 */

import java.io.File ;

public interface ImageExtractor {
    public void extractImages (File file, String distenationFolder);
}
