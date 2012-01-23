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

import java.io.IOException ;
import java.io.File ;

import java.util.List ;
import java.util.ArrayList ;
import java.util.Arrays;
import java.util.logging.Logger;

import org.apache.lucene.index.IndexReader ;
import org.apache.lucene.store.Directory ;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class MultimediaReader {
    private final Directory directory ;
    private final IndexReader indexReader ;
    
    public enum TYPE { IMAGE, AUDIO, ARCHIVE, VIDEO };
    private static final Logger logger = Logger.getLogger(edu.coeia.util.FilesPath.LOG_NAMESPACE);

    public MultimediaReader (final String location) throws IOException{
        this.directory = FSDirectory.open(new File(location));
        this.indexReader = IndexReader.open(directory);
        logger.info("MultimediaReader Constructor");
    }
    
    public List<String> getListPathsFromIndex(final TYPE type, final Case aCase) throws Exception {
        List<String> aList = new ArrayList<String>();

        CasePathHandler handler = CasePathHandler.newInstance(aCase.getCaseLocation());
        handler.readConfiguration();
                            
        for (int i = 0; i < indexReader.maxDoc(); i++) {
            Document document = indexReader.document(i);
            
            if (document != null) {
                Field field = document.getField(IndexingConstant.FILE_MIME);
                
                if (field != null && field.stringValue() != null) {
                    String documentExtension = field.stringValue();
                    
                    if (type == TYPE.IMAGE && isImage(documentExtension) ) {
                        String fullpath = handler.getFullPath(document.get(IndexingConstant.FILE_PATH));
                        aList.add(fullpath);
                    }
                    else if (type == TYPE.AUDIO && isAudio(documentExtension)) {
                        String fullpath = handler.getFullPath(document.get(IndexingConstant.FILE_PATH));
                        aList.add(fullpath);
                    }
                    else if (type == TYPE.ARCHIVE && isArchieve(documentExtension)) {
                        String fullpath = handler.getFullPath(document.get(IndexingConstant.FILE_PATH));
                        aList.add(fullpath);
                    }
                    else if (type == TYPE.VIDEO && isVideo(documentExtension)) {
                        String fullpath = handler.getFullPath(document.get(IndexingConstant.FILE_PATH));
                        aList.add(fullpath);
                    }
                }
            }
        }
        
        return (aList);  
    }

    public void close () throws IOException {
        indexReader.close();
    }
        
    private boolean isImage(String extension) {
        String[] extensions = {"jpg", "bmp", "gif", "tif", "png","psd"};
        return Arrays.asList(extensions).contains(extension);
    }
    
    private boolean isAudio(String extension) {
        String[] extensions = {"mp3", "rm", "ra", "wav", "3gp","amr","ogg","wma","raw","m4p","flac"};
        return Arrays.asList(extensions).contains(extension);
    }
    
    private boolean isArchieve(String extension) {
        String[] extensions = {"rar", "zip", "7z"};
        return Arrays.asList(extensions).contains(extension);
    }
    
    private boolean isVideo(String extension) {
        String[] extensions = {"avi", "mp4", "asf","mpeg","3gp"};
        return Arrays.asList(extensions).contains(extension);
    }
}
