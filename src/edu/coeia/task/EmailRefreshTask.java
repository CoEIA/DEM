/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.cases.Case;
import edu.coeia.wizard.EmailConfiguration;
import edu.coeia.gutil.JListUtil;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.offlinemail.EmailBrowsingPanel;
import edu.coeia.util.FilesPath;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;

import java.util.HashSet;
import java.util.List;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author wajdyessam
 */
public class EmailRefreshTask implements Task{
    private final TaskThread thread;
    private final Case aCase;
    private final EmailBrowsingPanel panel;
    
    public EmailRefreshTask(final Case aCase, final EmailBrowsingPanel panel) {
        this.thread = new TaskThread(this);
        this.aCase = aCase;
        this.panel = panel;
    }
    
    @Override
    public void startTask() {
        this.thread.execute();
    }
    
    @Override
    public void doTask() throws Exception {
        this.fillTable();
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.thread.isCancelledThread();
    }
    
    private void fillTable() throws IOException {
        final Set<String> paths = getOfflineEmailsPaths();
        
        EventQueue.invokeLater(new Runnable() { 
            @Override
            public void run() {
                for(String path: paths) {
                    JListUtil.addToList(path, panel.getModel(), panel.getList());
                }

                for(EmailConfiguration config: aCase.getEmailConfigurations()) {
                    JListUtil.addToList(config.getUserName(), panel.getModel(), panel.getList());
                }
            }
        });
    }
    
    private Set<String> getOfflineEmailsPaths() throws IOException {
        Set<String> offlineEmailPaths = new HashSet<String>();
        
        String indexDir = this.aCase.getCaseLocation() + File.separator + FilesPath.INDEX_PATH;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        int max = indexReader.maxDoc();
        
        for (int i=0; i<max ; i++) {
            if ( this.isCancelledTask() )
                return offlineEmailPaths;
                        
            Document document = indexReader.document(i);
            if ( document != null ) {
                Field field = document.getField(IndexingConstant.OFFLINE_EMAIL_PATH);
                if ( field != null && field.stringValue() != null) {
                   String path = field.stringValue();
                   offlineEmailPaths.add(path);
                }
            }
            
            document = null;
        }
        
        indexReader.close();
        
        return offlineEmailPaths;
    }
}
