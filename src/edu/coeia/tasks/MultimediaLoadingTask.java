/* 
 * Copyright (C) 2014 Center of Excellence in Information Assurance
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.coeia.tasks;

import edu.coeia.multimedia.MultimediaViewerPanel;
import edu.coeia.cases.Case;
import edu.coeia.cases.CaseFacade;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.constants.IndexingConstant;
import edu.coeia.util.FileUtil;

import java.awt.EventQueue;

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
    private final BackgroundProgressDialog dialog ;
    private final Case aCase;
    private final MultimediaViewerPanel panel;
    private final MultimediaViewerPanel.TYPE type;
    private final CaseFacade caseFacade ;
    
    private final static String[] imageExtensions = {"jpg", "jpeg", "bmp", "gif", "tif", "png","psd"};
    private final static String[] audioExtensions = {"mp3", "rm", "ra", "wav", "3gp","amr","ogg","wma","raw","m4p","flac"};
    private final static String[] archiveExtensions = {"rar", "zip", "7z"};
    private final static String[] videoExtensions = {"avi", "mp4", "asf","mpeg","3gp"};
    
    public MultimediaLoadingTask(final CaseFacade caseFacade, final MultimediaViewerPanel panel,
            final MultimediaViewerPanel.TYPE type) {
        this.dialog = new BackgroundProgressDialog(null, true, this);
        this.caseFacade = caseFacade ;
        this.aCase = this.caseFacade.getCase();
        this.panel = panel;
        this.type = type;
    }
    
    @Override
    public void startTask() {
        this.dialog.startThread();
    }
    
    @Override
    public void doTask() throws Exception {
        this.loadItems();
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.dialog.isCancelledThread();
    }
    
    private void loadItems() throws IOException {
        String indexDir = this.caseFacade.getCaseIndexFolderLocation();
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
                            
        for (int i=0; i<indexReader.maxDoc(); i++) {
            if ( this.isCancelledTask() )
                break;
            
            Document document = indexReader.document(i);
            
            if (document != null) {
                Field field = document.getField(IndexingConstant.FILE_MIME);
                
                if (field != null && field.stringValue() != null) {
                    String documentExtension = field.stringValue();
                    final StringBuilder fullpath = new StringBuilder();
                    
                    if (type == MultimediaViewerPanel.TYPE.IMAGE && isImage(documentExtension) ) {
                        fullpath.append(this.caseFacade.getFullPath(document.get(IndexingConstant.FILE_PATH)));
                    }
                    else if (type == MultimediaViewerPanel.TYPE.AUDIO && isAudio(documentExtension)) {
                        fullpath.append(this.caseFacade.getFullPath(document.get(IndexingConstant.FILE_PATH)));
                    }
                    else if (type == MultimediaViewerPanel.TYPE.ARCHIVE && isArchieve(documentExtension)) {
                        fullpath.append(this.caseFacade.getFullPath(document.get(IndexingConstant.FILE_PATH)));
                    }
                    else if (type == MultimediaViewerPanel.TYPE.VIDEO && isVideo(documentExtension)) {
                        fullpath.append(this.caseFacade.getFullPath(document.get(IndexingConstant.FILE_PATH)));
                    }
                    
                    if ( ! fullpath.toString().isEmpty() ) {
                        EventQueue.invokeLater(new Runnable() { 
                            @Override
                            public void run() {
                                File file = new File(fullpath.toString());
                                Object[] data = {file.getAbsolutePath(), FileUtil.getExtension(file),
                                        file.lastModified(), file.isHidden(), file.length()};
                                JTableUtil.addRowToJTable(panel.getTable(), data);
                            }
                        });
                    }
                }
            }
        }
        
        indexReader.close();
    }
    
    private boolean isImage(String extension) {
        return Arrays.asList(imageExtensions).contains(extension);
    }
    
    private boolean isAudio(String extension) {
        return Arrays.asList(audioExtensions).contains(extension);
    }
    
    private boolean isArchieve(String extension) {
        return Arrays.asList(archiveExtensions).contains(extension);
    }
    
    private boolean isVideo(String extension) {
        return Arrays.asList(videoExtensions).contains(extension);
    }
}
