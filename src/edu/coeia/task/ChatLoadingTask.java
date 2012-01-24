/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.cases.Case;
import edu.coeia.chat.ChatViewerPanel;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.items.ChatItem;
import edu.coeia.items.ItemFactory;
import edu.coeia.util.FilesPath;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author wajdyessam
 */
public class ChatLoadingTask implements Task{
    private final TaskThread thread;
    private final Case aCase;
    private final ChatViewerPanel panel;
    private final String fileName;
    
    public ChatLoadingTask(final Case aCase, final ChatViewerPanel panel, final String fileName) {
        this.thread = new TaskThread(this);
        this.aCase = aCase;
        this.panel = panel;
        this.fileName = fileName;
    }
    
    @Override
    public void startTask() {
        this.thread.execute();
    }
    
    @Override
    public void doTask() throws Exception {
        this.displayChatSessions();
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.thread.isCancelledThread();
    }
    
    private void displayChatSessions() throws IOException{
        String indexDir = this.aCase.getCaseLocation() + File.separator + FilesPath.INDEX_PATH;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);

        for (int i=0; i<indexReader.maxDoc(); i++) {
            if ( this.isCancelledTask() )
                return;
                        
            Document document = indexReader.document(i);
            if ( document != null ) {
                Field field = document.getField(IndexingConstant.CHAT_FILE);
                if ( field != null && field.stringValue() != null) {
                    
                   if ( field.stringValue().endsWith(fileName)) {
                       ChatItem item = (ChatItem) ItemFactory.newInstance(document, aCase);
                       Object[] data = new Object[] {item.getFrom(), item.getTo(), item.getMessageText(),
                            item.getDate()};
                       JTableUtil.addRowToJTable(panel.getTable(), data);
                       
                   }
                }
            }
        }
        indexReader.close();
    }
}
