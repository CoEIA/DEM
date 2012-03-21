/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tasks;

import edu.coeia.chat.ChatViewerPanel;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.constants.IndexingConstant;
import edu.coeia.items.ChatItem;
import edu.coeia.items.ItemFactory;

import java.awt.EventQueue;

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author wajdyessam
 */
public class ChatLoadingTask implements Task{
    private final ChatViewerPanel panel;
    private final String fileName;
    private final BackgroundProgressDialog dialog ;
    
    public ChatLoadingTask(final ChatViewerPanel panel, final String fileName) {
        this.panel = panel;
        this.fileName = fileName;
        this.dialog = new BackgroundProgressDialog(null, true, this);
    }
    
    @Override
    public void startTask() {
        this.dialog.startThread();
    }
    
    @Override
    public void doTask() throws Exception {
        this.displayChatSessionFast();
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.dialog.isCancelledThread();
    }
    
    private void displayChatSessionFast() throws IOException {
        try {
            Directory directory = FSDirectory.open(new File(
                    this.panel.getCaseFacade().getCaseIndexFolderLocation()
                    ));
            
            IndexSearcher searcher = new IndexSearcher(directory);
            QueryParser parser = new QueryParser(Version.LUCENE_30, 
                    IndexingConstant.CHAT_AGENT, new StopAnalyzer(Version.LUCENE_30));
            parser.setAllowLeadingWildcard(true);
            Query query = parser.parse(panel.getAgent());
            
            TopDocs topDocs = searcher.search(query, 5000);

            for(ScoreDoc scoreDoc: topDocs.scoreDocs) {
                final Document document = searcher.doc(scoreDoc.doc);
                String chatFile = document.get(IndexingConstant.CHAT_FILE);
                
                if ( chatFile != null && !chatFile.trim().isEmpty()) {
                    
                    if ( chatFile.endsWith(this.fileName) ) {
                        
                        EventQueue.invokeLater(new Runnable() { 
                            @Override
                            public void run() {
                                ChatItem item = (ChatItem) ItemFactory.newInstance(document, panel.getCaseFacade());
                                Object[] data = new Object[] {item.getFrom(), item.getTo(), item.getMessageText(),
                                        item.getDate()};
                                JTableUtil.addRowToJTable(panel.getTable(), data);
                            }
                        });
                       
                    }
                }
            }
            
            searcher.close();
        } catch (ParseException ex) {
            Logger.getLogger(ChatRefreshTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
