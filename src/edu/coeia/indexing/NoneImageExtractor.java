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

public class NoneImageExtractor implements ImageExtractor{
    @Override
    public void extractImages(IndexWriter writer, File file, String distenationFolder, int parentId) {
        //System.out.println("None Image Extractor");
    }
}
