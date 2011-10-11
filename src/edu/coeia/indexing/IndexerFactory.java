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

public class IndexerFactory {
    
    public static Indexer getIndexer (File file, boolean supportCaching, String location){
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
                
                 return new DocumentIndexer(file, mime, supportCaching, location, new OfficeImageExtractor());

            if ( mime.equalsIgnoreCase("text/plain") ||
                 mime.equalsIgnoreCase("application/xml") ||
                 mime.equalsIgnoreCase("application/xhtml+xml") ||
                 mime.equalsIgnoreCase("text/html") )
                return new DocumentIndexer(file, mime, supportCaching, location, new NoneImageExtractor());


            if ( mime.equalsIgnoreCase("application/pdf") )
                return new DocumentIndexer(file, mime, supportCaching, location, new PDFImageExtractor());

             if ( mime.equalsIgnoreCase("application/zip") ||
                  mime.equalsIgnoreCase("application/x-rar-compressed"))
                return new ArchiveIndexer(file, mime, supportCaching, location, new OfficeImageExtractor());

            else if (mime.startsWith("image/"))
                return new ImageIndexer(file, mime, supportCaching, location, new ExternalImageExtractor());
             
             System.out.println("mime: " + mime);
        }
        catch(IOException e){
            e.printStackTrace();
        }        
        
        throw new UnsupportedOperationException("This file have no handler to handle it");
    }
    
    // main method for testing
    public static void main(String[] args) throws Exception{
        
//        File file = new File("C:\\DEM_CASE\\1.doc");
//        Indexer indexObject = IndexerFactory.getIndexer(file, true);
//        System.out.println("end");
        

        int[] data = { 100, 200, 300, 400 };

        java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocate(data.length * 4);        
        java.nio.IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(data);

        byte[] array = byteBuffer.array();

        for (int i=0; i < array.length; i++)
        {
            System.out.println(i + ": " + array[i]);
        }
    }
}
