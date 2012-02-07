/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.multimedia.MultimediaViewerPanel;
import edu.coeia.cases.Case;
import edu.coeia.cases.CaseFacade;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.util.FileUtil;
import edu.coeia.util.ApplicationConstants;

import java.io.File;
import java.io.IOException;

import java.util.Arrays;

import org.apache.lucene.index.IndexReader ;
import org.apache.lucene.store.Directory ;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 *
 * @author wajdyessam
 */
public class MultimediaLoadingTask implements Task{
    private final TaskThread thread;
    private final Case aCase;
    private final MultimediaViewerPanel panel;
    private final MultimediaViewerPanel.TYPE type;
    private final CaseFacade caseFacade ;
    
    public MultimediaLoadingTask(final CaseFacade caseFacade, final MultimediaViewerPanel panel,
            final MultimediaViewerPanel.TYPE type) {
        this.thread = new TaskThread(this);
        this.caseFacade = caseFacade ;
        this.aCase = this.caseFacade.getCase();
        this.panel = panel;
        this.type = type;
    }
    
    @Override
    public void startTask() {
        this.thread.execute();
    }
    
    @Override
    public void doTask() throws Exception {
        this.loadItems();
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.thread.isCancelledThread();
    }
    
    private void loadItems() throws IOException {
        String indexDir = this.aCase.getCaseLocation() + File.separator + ApplicationConstants.CASE_INDEX_FOLDER;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
                            
        for (int i = 0; i < indexReader.maxDoc(); i++) {
            if ( this.isCancelledTask() )
                return;
            
            Document document = indexReader.document(i);
            
            if (document != null) {
                Field field = document.getField(IndexingConstant.FILE_MIME);
                
                if (field != null && field.stringValue() != null) {
                    String documentExtension = field.stringValue();
                    String fullpath = "";
                    
                    if (type == MultimediaViewerPanel.TYPE.IMAGE && isImage(documentExtension) ) {
                        fullpath = this.caseFacade.getFullPath(document.get(IndexingConstant.FILE_PATH));
                    }
                    else if (type == MultimediaViewerPanel.TYPE.AUDIO && isAudio(documentExtension)) {
                        fullpath = this.caseFacade.getFullPath(document.get(IndexingConstant.FILE_PATH));
                    }
                    else if (type == MultimediaViewerPanel.TYPE.ARCHIVE && isArchieve(documentExtension)) {
                        fullpath = this.caseFacade.getFullPath(document.get(IndexingConstant.FILE_PATH));
                    }
                    else if (type == MultimediaViewerPanel.TYPE.VIDEO && isVideo(documentExtension)) {
                        fullpath = this.caseFacade.getFullPath(document.get(IndexingConstant.FILE_PATH));
                    }
                    
                    if ( ! fullpath.isEmpty() ) {
                        File file = new File(fullpath);
                        Object[] data = {file.getAbsolutePath(), FileUtil.getExtension(file),
                                        file.lastModified(), file.isHidden(), file.length()};
                        JTableUtil.addRowToJTable(panel.getTable(), data);
                    }
                }
            }
        }
        
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
