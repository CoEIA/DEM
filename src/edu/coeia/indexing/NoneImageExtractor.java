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

public class NoneImageExtractor implements ImageExtractor{
    @Override
    public void extractImages(File file, String distenationFolder) {
        System.out.println("None Image Extractor");
    }
}
