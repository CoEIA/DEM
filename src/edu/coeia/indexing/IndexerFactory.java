/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.extractors.PDFImageExtractor;
import edu.coeia.extractors.OfficeImageExtractor;
import edu.coeia.extractors.NoneImageExtractor;
import edu.coeia.extractors.ExternalImageExtractor;
import edu.coeia.util.FileUtil;

import java.io.File ;
import java.io.IOException; 

import org.apache.tika.Tika;

final class IndexerFactory {
    
    /**
     * Get Indexer for Simple, Container, Images Document Files
     * @param luceneIndex
     * @param file
     * @param parentId
     * @return 
     */
    public static Indexer getIndexer (LuceneIndex luceneIndex, File file, int parentId) {
        Indexer indexer  = null;
        
        try {
            Tika tika = new Tika();

            String mime = tika.detect(file);
            
            // if its outlook file, then call offline email indexer
            if ( isOutlookFile(mime, file.getAbsolutePath()) ) {
                indexer = OutlookIndexer.newInstance(luceneIndex, file, mime, new NoneImageExtractor());
            }
            
            // if found office file and user select extract images, then do it
            // else it will indexed without images extractors
            else if ( isOfficeFile(mime) && luceneIndex.getCase().getCacheImages())
                indexer = DocumentIndexer.newInstance(luceneIndex, file, mime, new OfficeImageExtractor(), parentId);
                         
            // simple file (html, txt, xml) is file that have not images
            // inside it
            else if ( isSimpleFile(mime))
                indexer = DocumentIndexer.newInstance(luceneIndex, file, mime, new NoneImageExtractor(), parentId);
                        
            // pdf files
            else if ( isPDFFile(mime))
                indexer = DocumentIndexer.newInstance(luceneIndex, file, mime, new PDFImageExtractor(), parentId);
                        
            // if found archive files and user select to index archive files
            // else consider them as normal file
            else if (isArchiveFile(mime) && luceneIndex.getCase().getCheckCompressed())
                return null;
                //indexer = ArchiveIndexer.newInstance(luceneIndex, file, mime, new OfficeImageExtractor(), parentId);
             
            // images type
            else if ( isImage(mime) )
                indexer = DocumentIndexer.newInstance(luceneIndex, file, mime, new ExternalImageExtractor(), parentId); 
            
            // Unkown file Format
            //else
                //indexer = DocumentIndexer.newInstance(luceneIndex, file, mime, new NoneImageExtractor(), parentId);
        }
        catch(IOException e){
            e.printStackTrace();
        }        
        
        return indexer;
    }
    
    public static Indexer getIndexer (LuceneIndex luceneIndex, File file){
        return getIndexer(luceneIndex, file, 0);
    }
    
    /**
     * Get Indexer for chat sessions
     * @param luceneIndex
     * @param dir
     * @return 
     */
    public static Indexer getFolderIndexer (LuceneIndex luceneIndex, File dir) {
        // TODO:
        // if chat session suppport, then continue to detec it
        // else return from this method
        
        if ( isValidMSNPath(dir.getAbsolutePath()))
            return indexHotmailDir(luceneIndex, dir);
        
        else if ( isValidYahooPath(dir.getAbsolutePath()) )
            return indexYahooDir(luceneIndex, dir);
        
        throw new UnsupportedOperationException("Normal Folder");
    }
    
    private static Indexer indexYahooDir(LuceneIndex luceneIndex, File path) {
        return YahooChatIndexer.newInstance(luceneIndex, path, FileUtil.getExtension(path), new NoneImageExtractor());  
    }
    
    private static Indexer indexHotmailDir(LuceneIndex luceneIndex, File path) {
        return MSNChatIndexer.newInstance(luceneIndex, path, FileUtil.getExtension(path), new NoneImageExtractor());
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
    
    private static boolean isArchiveFile(final String mime) {
        return mime.equalsIgnoreCase("application/zip") ||
          mime.equalsIgnoreCase("application/x-rar-compressed") ||
          mime.equalsIgnoreCase("application/x-bzip2");
    }
    
    private static boolean isImage(final String mime) {
        return mime.startsWith("image/") ;
    }
    
    private static boolean isPDFFile(final String mime) {
        return mime.equalsIgnoreCase("application/pdf") ;
    }
    
    private static boolean isSimpleFile(final String mime) {
        return mime.equalsIgnoreCase("text/plain") ||
                 mime.equalsIgnoreCase("application/xml") ||
                 mime.equalsIgnoreCase("application/xhtml+xml") ||
                 mime.equalsIgnoreCase("text/html")  ;
    }
    
    private static boolean isOfficeFile(final String mime) {
        return mime.equalsIgnoreCase("application/msword") ||
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
                 mime.equals("application/rtf"); 
    }
    
    private static boolean isOutlookFile(final String mime, final String path) {
        return mime.equalsIgnoreCase("application/vnd.ms-outlook") ||
                FileUtil.getExtension(path).equalsIgnoreCase("pst") ||
                FileUtil.getExtension(path).equalsIgnoreCase("ost");
    }
}
