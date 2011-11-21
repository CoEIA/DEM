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

final class IndexerFactory {
    
    public static Indexer getIndexer (LuceneIndex luceneIndex, File file, int parentId) {
        String mime = null;
        
        try {
            Tika tika = new Tika();

            mime = tika.detect(file);
            
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
                
                 return DocumentIndexer.newInstance(luceneIndex, file, mime, new OfficeImageExtractor(), parentId);

            if ( mime.equalsIgnoreCase("text/plain") ||
                 mime.equalsIgnoreCase("application/xml") ||
                 mime.equalsIgnoreCase("application/xhtml+xml") ||
                 mime.equalsIgnoreCase("text/html") )
                return DocumentIndexer.newInstance(luceneIndex, file, mime, new NoneImageExtractor(), parentId);


            if ( mime.equalsIgnoreCase("application/pdf") )
                return DocumentIndexer.newInstance(luceneIndex, file, mime, new PDFImageExtractor(), parentId);

             if ( mime.equalsIgnoreCase("application/zip") ||
                  mime.equalsIgnoreCase("application/x-rar-compressed"))
                return ArchiveIndexer.newInstance(luceneIndex, file, mime, new OfficeImageExtractor(), parentId);

            else if (mime.startsWith("image/"))
                return DocumentIndexer.newInstance(luceneIndex, file, mime, new ExternalImageExtractor(), parentId);
        }
        catch(IOException e){
            e.printStackTrace();
        }        
        
        // Unkown file Format
        return DocumentIndexer.newInstance(luceneIndex, file, mime, new NoneImageExtractor(), parentId);
        //throw new UnsupportedOperationException("This file have no handler to handle it");
    }
    
    public static Indexer getIndexer (LuceneIndex luceneIndex, File file){
        return getIndexer(luceneIndex, file, 0);
    }
    
    public static Indexer getFolderIndexer (LuceneIndex luceneIndex, File dir) {
        if ( isValidMSNPath(dir.getAbsolutePath()) )
            return indexHotmailDir(luceneIndex, dir);
        
        else if ( isValidYahooPath(dir.getAbsolutePath()) )
            return indexYahooDir(luceneIndex, dir);
        
        throw new UnsupportedOperationException("Normal Folder");
    }
    
    private static Indexer indexYahooDir(LuceneIndex luceneIndex, File path) {
        return YahooChatIndexer.newInstance(luceneIndex, path, "", new NoneImageExtractor());  
    }
    
    private static Indexer indexHotmailDir(LuceneIndex luceneIndex, File path) {
        return MSNIndexer.newInstance(luceneIndex, path, "", new NoneImageExtractor());
    }
    
    /**
     * Test if the path is valid Yahoo path
     * @param path to chat profile
     * @return true if path is correct and false if not
     */
    private static boolean isValidYahooPath(String path) {
        if ( path.endsWith("Program Files\\Yahoo!\\Messenger\\Profiles") )
            return true;
        
        return false;
    }
    
    /**
     * Test if the path is valid MSN path
     * @param path to chat profile
     * @return true if path is correct and false if not
     */
    private static boolean isValidMSNPath(String path) {
        if ( path.endsWith("My Documents\\My Received Files") )
            return true;
        
        return false;
    }
    
    /**
     * Test if the path is valid SKYPE path
     * @param path to chat profile
     * @return true if path is correct and false if not
     */
    private static boolean isValidSkypePath(String path) {
        if ( path.endsWith("Application Data\\Skype") )
            return true;
        
        return false;
    }
}
