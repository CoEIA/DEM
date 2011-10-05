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
import java.io.IOException; 

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException ;

public class IndexerFactory {
    
    public static Indexer getIndexer (File file, boolean supportCaching) 
            throws TikaException, IOException { // pass file, writer, isCaching to index object
        
        Tika tika = new Tika();
        
        String mime = tika.detect(file);
        if ( mime.equalsIgnoreCase("application/msword") )
             return new DocumentIndexer(file, mime, supportCaching, new DocImageExtractor());
        
        if ( mime.equalsIgnoreCase("text/plain") ||
             mime.equalsIgnoreCase("application/xml") ||
             mime.equalsIgnoreCase("application/xhtml+xml") ||
             mime.equalsIgnoreCase("text/html") )
            return new DocumentIndexer(file, mime, supportCaching, new NoneImageExtractor());
        
             
        if ( mime.equalsIgnoreCase("application/pdf") )
            return new DocumentIndexer(file, mime, supportCaching, new PDFImageExtractor());
        
         if ( mime.equalsIgnoreCase("application/zip") ||
                  mime.equalsIgnoreCase("application/x-rar-compressed"))
            return new ArchiveIndexer(file, mime, supportCaching, new NoneImageExtractor());
        
        else if (mime.startsWith("image/"))
            return new ImageIndexer(file, mime, supportCaching, new NoneImageExtractor());
        
        
        throw new UnsupportedOperationException("This file have no handler to handle it");
    }
    
    // main method for testing
    public static void main(String[] args) throws TikaException, IOException {
        
        File file = new File("F:\\a\\test.rar");
        Indexer indexObject = IndexerFactory.getIndexer(file, true);
    }
}
