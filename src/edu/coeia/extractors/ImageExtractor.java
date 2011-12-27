/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.extractors;

/**
 * Image Extractor
 * Interface for images extractor from different type of files
 * @author wajdyessam
 */

import edu.coeia.indexing.Indexer;
import java.io.File ;

public interface ImageExtractor {
    public void extractImages (Indexer indexer, File file,int parentId);
}
