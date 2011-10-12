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
import java.io.FileNotFoundException ;
import java.io.IOException;

import org.apache.lucene.index.IndexWriter ;
import org.apache.lucene.document.Document ;

import edu.coeia.filesystem.index.FileDocument ;

public class ImageIndexer extends Indexer{
    
    public ImageIndexer(File file, String mimeType, boolean imageCaching, String location, ImageExtractor imageExtractor) {
        super(file,mimeType, imageCaching, location, imageExtractor);
    }
        
    @Override
    public boolean doIndexing(IndexWriter writer) {
        
        try{
            Document doc = FileDocument.documentImage(file);
            writer.addDocument(doc);
        
            if ( doc != null) {
                writer.addDocument(doc);    // index file
            }
            else {
                System.out.println("Fail Parsing: " + file.getAbsolutePath());
                return false;
            }
            
            // cache images
            if ( imageCache ) {
                imageExtractor.extractImages(file, this.imagesLocation);
            }
            
            return true;
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
        
        return false;
    }
    
}
