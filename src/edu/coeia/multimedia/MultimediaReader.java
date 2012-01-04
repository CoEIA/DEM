/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.multimedia;

/**
 *
 * @author wajdyessam
 *
 */

import edu.coeia.cases.Case;
import edu.coeia.cases.CasePathHandler;
import edu.coeia.indexing.IndexingConstant;
import org.apache.lucene.index.IndexReader ;
import org.apache.lucene.store.Directory ;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.TermEnum ;
import org.apache.lucene.index.Term ;

import java.io.IOException ;
import java.io.File ;

import java.util.List ;
import java.util.ArrayList ;
import java.util.Arrays;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class MultimediaReader {
    private String indexDir ;
    private Directory dir ;
    private IndexReader indexReader ;
    enum Operations { Images, Audio, Archieve,Video };
    
    private static final Logger logger = Logger.getLogger(edu.coeia.util.FilesPath.LOG_NAMESPACE);

    public MultimediaReader (String location) throws IOException{
        indexDir = location ;
        dir = FSDirectory.open(new File(indexDir));
        indexReader = IndexReader.open(dir);
        
        logger.info("ImageReader Constructor");
    }
    public List<String> getListPathsFromIndex(Operations or, Case aCase) throws Exception {
        List<String> aList = new ArrayList<String>();

        for (int i = 0; i < indexReader.maxDoc(); i++) {
            Document document = indexReader.document(i);
            if (document != null) {
                Field field = document.getField(IndexingConstant.FILE_MIME);
                if (field != null && field.stringValue() != null) {
                    String documentExtension = field.stringValue();
                    if (or == Operations.Images) {
                        if (isImage(documentExtension)) {
                            String path = document.get(IndexingConstant.FILE_PATH);
                            CasePathHandler handler = CasePathHandler.newInstance(aCase.getCaseLocation());
                            handler.readConfiguration();
                            String fullpath = handler.getFullPath(path);
                            aList.add(fullpath);
                        }
                    } else if (or == Operations.Audio) {
                        if (isAudio(documentExtension)) {
                            String path = document.get(IndexingConstant.FILE_PATH);
                            CasePathHandler handler = CasePathHandler.newInstance(aCase.getCaseLocation());
                            handler.readConfiguration();
                            String fullpath = handler.getFullPath(path);
                            aList.add(fullpath);

                        }
                    }
                     else if (or == Operations.Archieve) {
                        if (isArchieve(documentExtension)) {
                            String path = document.get(IndexingConstant.FILE_PATH);
                            CasePathHandler handler = CasePathHandler.newInstance(aCase.getCaseLocation());
                            handler.readConfiguration();
                            String fullpath = handler.getFullPath(path);
                            aList.add(fullpath);

                        }
                    }
                    
                     else if (or == Operations.Video) {
                        if (isVideo(documentExtension)) {
                            String path = document.get(IndexingConstant.FILE_PATH);
                            CasePathHandler handler = CasePathHandler.newInstance(aCase.getCaseLocation());
                            handler.readConfiguration();
                            String fullpath = handler.getFullPath(path);
                            aList.add(fullpath);

                        }
                    }

                }
            }
        }
         return (aList);  
    }

    private boolean isImage(String extension) {
        String[] extensions = {"jpg", "bmp", "gif", "tif", "png","psd"};
        boolean b  = false;
        for (int i = 0; i<extensions.length; i++)
        {
            if(extension.equals(extensions[i]))
            {
                b = true;
                break;
            }
           
        }
        return b;
    }
    
    private boolean isAudio(String extension)
    {
    
        String[] extensions = {"mp3", "rm", "ra", "wav", "3gp","amr","ogg","wma","raw","m4p","flac"};
        
        boolean b = false;
        for (int i = 0; i < extensions.length; i++) {
            if (extension.equals(extensions[i])) {
                b = true;
                break;
            }

        }
        return b;

    }
    private boolean isArchieve(String extension)
    {
    
        String[] extensions = {"rar", "zip", "7z"};
        
        boolean b = false;
        for (int i = 0; i < extensions.length; i++) {
            if (extension.equals(extensions[i])) {
                b = true;
                break;
            }

        }
        return b;

    }
     private boolean isVideo(String extension)
    {
    
        String[] extensions = {"avi", "mp4", "asf","mpeg","3gp"};
        
        boolean b = false;
        for (int i = 0; i < extensions.length; i++) {
            if (extension.equals(extensions[i])) {
                b = true;
                break;
            }

        }
        return b;

    }
  
    
    public void close () throws IOException {
        indexReader.close();
    }
}
