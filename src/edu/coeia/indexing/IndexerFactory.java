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
     * Return Indexer depend on the type of the file
     * and make this document without parent (have 0 value in parentId)
     * so we can know later if this document embedded in other document
     * or not
     */
    public static Indexer getIndexer (LuceneIndex luceneIndex, File file){
        return getIndexer(luceneIndex, file, 0);
    }
        
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
            
            if ( isChatPath(file.getAbsolutePath()) ) {
                indexer = getChatIndexer(luceneIndex, file);
            }
            
            // if its outlook file, then call offline email indexer
            else if ( isOutlookFile(mime, file.getAbsolutePath()) ) {
                indexer = OutlookIndexer.newInstance(luceneIndex, file, mime, new NoneImageExtractor());
            }
            
            // if found office file and user select extract images, then do it
            // else it will indexed without images extractors
            else if ( isOfficeFile(mime) && luceneIndex.getCaseFacade().getCase().getCacheImages())
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
            else if (isArchiveFile(mime) && luceneIndex.getCaseFacade().getCase().getCheckCompressed())
                indexer = ArchiveIndexer.newInstance(luceneIndex, file, mime, new OfficeImageExtractor(), parentId);
             
            // images type
            else if ( isImage(mime) )
                indexer = ImageIndexer.newInstance(luceneIndex, file, mime, new ExternalImageExtractor(), parentId); 
            
            // Unkown file Format
            else
                indexer = NonDocumentIndexer.newInstance(luceneIndex, file, mime, new NoneImageExtractor(), parentId);
        }
        catch(IOException e){
            e.printStackTrace();
        }        
        
        return indexer;
    }
    
    public static boolean isChatPath(String path) {
        return isValidMSNChatFile(path) 
                || isValidSkypeChatFile(path) 
                || isValidYahooChatFile(path);
    }
        
    /**
     * Get Indexer for chat sessions
     * @param luceneIndex
     * @param chatFile
     * @return 
     */
    public static Indexer getChatIndexer (LuceneIndex luceneIndex, File chatFile) {        
        String path = chatFile.getAbsolutePath();
        Indexer indexer = null;
        
        if ( isValidMSNChatFile(path)) {
            indexer = ChatIndexer.newInstance(luceneIndex, chatFile, 
                    FileUtil.getExtension(path), new NoneImageExtractor(), ChatIndexer.CHAT_TYPE.MSN);
        }
        
        else if ( isValidYahooChatFile(chatFile.getAbsolutePath()) ) {
            indexer = ChatIndexer.newInstance(luceneIndex, chatFile,
                    FileUtil.getExtension(path), new NoneImageExtractor(), ChatIndexer.CHAT_TYPE.YAHOO);  
        }
        
        else if ( isValidSkypeChatFile(chatFile.getAbsolutePath()) )  {
            indexer = ChatIndexer.newInstance(luceneIndex, chatFile,
                    FileUtil.getExtension(path), new NoneImageExtractor(), ChatIndexer.CHAT_TYPE.SKYPE);
        }
        
        return indexer;
    }
    
    /**
     * Test if the path is valid Yahoo path
     * @param path to chat profile
     * @return true if path is correct and false if not
     */
    private static boolean isValidYahooChatFile(String path) {
        if ( path.contains("Program Files\\Yahoo!\\Messenger\\Profiles") &&
              path.endsWith(".dat"))
            return true;
        
        return false;
    }
    
    /**
     * Test if the path is valid MSN path
     * @param path to chat profile
     * @return true if path is correct and false if not
     */
    private static boolean isValidMSNChatFile(String path) {
        if ( path.contains("My Documents\\My Received Files") &&
             path.contains("History") && path.endsWith(".xml") )
            return true;
        
        return false;
    }
    
    /**
     * Test if the path is valid SKYPE path
     * @param path to chat profile
     * @return true if path is correct and false if not
     */
    private static boolean isValidSkypeChatFile(String path) {
        if ( path.contains("Application Data\\Skype") &&
              path.endsWith("main.db"))
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
                 mime.equalsIgnoreCase("text/html") ||
                 mime.startsWith("text");
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
                 mime.equalsIgnoreCase("application/msword") ||
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
