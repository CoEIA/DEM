/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.chat;

import javax.swing.SwingWorker;

import edu.coeia.cases.Case;
import edu.coeia.gutil.JListUtil;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.items.ChatItem;
import edu.coeia.items.ItemFactory;
import edu.coeia.util.FilesPath;

import java.awt.Cursor;
import java.io.File;
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author wajdyessam
 */
//public class Task extends SwingWorker<Void, Void> {
//    private final Case aCase; 
//    private final String agent;
//    
//    public Task (final Case aCase, final String agent) {
//        this.aCase = aCase; 
//        this.agent = agent;
//    }
//    
//    @Override
//    public Void doInBackground() {
//        try {
//            getChatFilePath();
//        } catch (IOException ex) {
//            Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//    
//    private Set<String> getChatFilePath() throws IOException{
//        String indexDir = this.aCase.getCaseLocation() + "\\" + FilesPath.INDEX_PATH;
//        Directory dir = FSDirectory.open(new File(indexDir));
//        IndexReader indexReader = IndexReader.open(dir);
//        Set<String> aList = new HashSet<String>();
//        
//        int prog = 0;
//        this.setProgress(prog);
//        
//        for (int i=0; i<indexReader.maxDoc(); i++) {
//            this.setProgress(prog);
//            Document document = indexReader.document(i);
//            if ( document != null ) {
//                Field field = document.getField(IndexingConstant.CHAT_FILE);
//                if ( field != null && field.stringValue() != null) {
//                    
//                   if ( document.getField(IndexingConstant.CHAT_AGENT).stringValue().equals(agent)) {
//                       String chatFile = field.stringValue();
//                       aList.add(chatFile);
//                   }
//                }
//            }
//            
//            prog += 1;
//        }
//        indexReader.close();
//        
//        return aList;
//    }
//        
//    @Override
//    public void done() {
//        System.out.println("end");
//    }
//}
