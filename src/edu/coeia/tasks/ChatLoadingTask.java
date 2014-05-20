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
                                ChatItem item = (ChatItem) ItemFactory.newInstance(document, panel.getCaseFacade()
                                        , false);
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
