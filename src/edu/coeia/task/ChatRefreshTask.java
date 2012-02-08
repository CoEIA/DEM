/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.cases.Case;
import edu.coeia.chat.ChatViewerPanel;
import edu.coeia.gutil.JListUtil;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.constants.ApplicationConstants;

import java.awt.EventQueue;

import java.io.File;
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author wajdyessam
 */

public class ChatRefreshTask implements Task{
    private final TaskThread thread;
    private final Case aCase;
    private final ChatViewerPanel panel;
    
    public ChatRefreshTask(final Case aCase, final ChatViewerPanel panel) {
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
        Set<String> set = this.getChatFilePath();
        for(final String item: set) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() { 
                   JListUtil.addToList(item, panel.getListModel(), panel.getList());
               }
            });
        }
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.thread.isCancelledThread();
    }
    
    private Set<String> getChatFilePath() throws IOException{
        String indexDir = aCase.getCaseLocation() + File.separator + ApplicationConstants.CASE_INDEX_FOLDER;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        Set<String> aList = new HashSet<String>();

        for (int i=0; i<indexReader.maxDoc(); i++) {
            if ( this.isCancelledTask() )
                return aList;
            
            Document document = indexReader.document(i);
            if ( document != null ) {
                Field field = document.getField(IndexingConstant.CHAT_FILE);
                if ( field != null && field.stringValue() != null) {
                   if ( document.getField(IndexingConstant.CHAT_AGENT).stringValue().equals(panel.getAgent())) {
                       String chatFile = field.stringValue();
                       final File path = new File(chatFile);
                       aList.add(path.getName());
                   }
                }
            }
        }
        indexReader.close();

        return aList;
    }
}
