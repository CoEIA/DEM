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

import org.apache.lucene.index.IndexWriter;

public class IndexerFactory {
    
    public static Indexer getIndexer (IndexWriter writer, File file, boolean supportCaching, String caseLocation, int parentId) {
        try {
            Tika tika = new Tika();

            String mime = tika.detect(file);
            
            if ( mime.equalsIgnoreCase("application/msword") ||
                 mime.equalsIgnoreCase("application/vnd.ms-excel.sheet.binary.macroenabled.12") ||
                 mime.equalsIgnoreCase("application/x-mspublisher") ||
                 mime.equalsIgnoreCase("application/x-tika-msoffice") ||
                 mime.equalsIgnoreCase("application/vnd.ms-outlook") ||
                 mime.equalsIgnoreCase("application/vnd.ms-excel") ||
                 mime.equalsIgnoreCase("application/vnd.ms-powerpoint") ||
                 mime.equalsIgnoreCase("application/vnd.visio") ||     
                 mime.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.presentationml.slideshow") ||
                 mime.equalsIgnoreCase("application/vnd.ms-word.document.macroenabled.12") ||
                 mime.equalsIgnoreCase("application/vnd.ms-excel") ||
                 mime.equalsIgnoreCase("application/vnd.ms-powerpoint") ||
                 mime.equalsIgnoreCase("application/vnd.visio") || 
                 mime.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document") || 
                 mime.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.presentationml.presentation") ||
                 mime.equals("application/rtf") )
                
                 return new DocumentIndexer(writer, file, mime, supportCaching, caseLocation, new OfficeImageExtractor(), parentId);

            if ( mime.equalsIgnoreCase("text/plain") ||
                 mime.equalsIgnoreCase("application/xml") ||
                 mime.equalsIgnoreCase("application/xhtml+xml") ||
                 mime.equalsIgnoreCase("text/html") )
                return new DocumentIndexer(writer, file, mime, supportCaching, caseLocation, new NoneImageExtractor(), parentId);


            if ( mime.equalsIgnoreCase("application/pdf") )
                return new DocumentIndexer(writer, file, mime, supportCaching, caseLocation, new PDFImageExtractor(), parentId);

             if ( mime.equalsIgnoreCase("application/zip") ||
                  mime.equalsIgnoreCase("application/x-rar-compressed"))
                return new ArchiveIndexer(writer, file, mime, supportCaching, caseLocation, new OfficeImageExtractor(), parentId);

            else if (mime.startsWith("image/"))
                return new ImageIndexer(writer, file, mime, supportCaching, caseLocation, new ExternalImageExtractor(), parentId);
             
             System.out.println("mime: " + mime);
        }
        catch(IOException e){
            e.printStackTrace();
        }        
        
        throw new UnsupportedOperationException("This file have no handler to handle it");
    }
    
    public static Indexer getIndexer (IndexWriter writer, File file, boolean supportCaching, String caseLocation){
        return getIndexer(writer, file, supportCaching, caseLocation, 0);
    }
}
