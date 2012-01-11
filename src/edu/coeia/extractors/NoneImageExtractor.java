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

public class NoneImageExtractor implements ImageExtractor{
    @Override
    public void extractImages(Indexer indexer, File file, int parentId) {
        //System.out.println("None Image Extractor");
    }
}
