/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tasks;

import edu.coeia.cases.Case;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.constants.IndexingConstant;
import edu.coeia.items.ChatItem;
import edu.coeia.items.EmailItem;
import edu.coeia.items.ItemFactory;
import edu.coeia.offlinemail.EmailBrowsingPanel;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author wajdyessam
 */
public class EmailLoadingTask  implements Task{
    private final BackgroundProgressDialog dialog ;
    private final Case aCase;
    private final EmailBrowsingPanel panel;
    
    public EmailLoadingTask(final Case aCase, final EmailBrowsingPanel panel) {
        this.dialog = new BackgroundProgressDialog(null, true, this);
        this.aCase = aCase;
        this.panel = panel;
    }
    
    @Override
    public void startTask() {
        this.dialog.startThread();
    }
    
    @Override
    public void doTask() throws Exception {
        this.loadEmail();
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.dialog.isCancelledThread();
    }
    
    private void loadEmail() throws Exception {
        String type = "";
        String path = "";
        
        if ( this.panel.isOfflineEmailSelected() ) {
            path = String.valueOf(this.panel.getList().getSelectedValue());
            type = IndexingConstant.OFFLINE_EMAIL_PATH;
            
        }
        else if ( this.panel.isOnlineEmailSelected() ) {
            path = String.valueOf(this.panel.getList().getSelectedValue());
            type = IndexingConstant.ONLINE_EMAIL_USER_NAME;
        }
        
        long start = System.currentTimeMillis();
        this.getAllEmailMessages(path, type);
        long end = System.currentTimeMillis();
        
        System.out.println("finish task1 in: " + (end-start));
        
        start = System.currentTimeMillis();
        this.getAllEmailMessagesFast(path, type);
        end = System.currentTimeMillis();
        System.out.println("finish task2 in:  " + (end-start));
    }
    
    private void getAllEmailMessages(final String path, final String constant) throws IOException, ParseException {
        List<Integer> ids = new ArrayList<Integer>();
        
        String indexDir = this.panel.getCaseFacade().getCaseIndexFolderLocation();
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        
        for (int i=0; i<indexReader.maxDoc(); i++) {
            if ( this.isCancelledTask() )
                return ;
                     
            Document document = indexReader.document(i);
            if ( document != null ) {
                Field field = document.getField(constant);
                if ( field != null && field.stringValue() != null) {
                   String tmp = field.stringValue();
                   
                   if ( tmp.endsWith(path)) {
                        EmailItem item = (EmailItem) ItemFactory.newInstance(document, panel.getCaseFacade());
                        JTableUtil.addRowToJTable(panel.getTable(), item.getFullDisplayData());
                        ids.add(Integer.valueOf(item.getDocumentId()));
                   }
                }
            }
        }
        
        System.out.println("task 1 size: " + ids.size());
        this.panel.setResultIds(ids);
        indexReader.close();
    }
    
    private void getAllEmailMessagesFast(final String path, final String constant) throws IOException {
        List<Integer> ids = new ArrayList<Integer>();
        
        try {
            Directory directory = FSDirectory.open(new File(
                    this.panel.getCaseFacade().getCaseIndexFolderLocation()
                    ));
            
            IndexSearcher searcher = new IndexSearcher(directory);
            QueryParser parser = new QueryParser(Version.LUCENE_30, 
                    IndexingConstant.DOCUMENT_DESCRIPTION, new StopAnalyzer(Version.LUCENE_30));
            //parser.setAllowLeadingWildcard(true);
            Query query = parser.parse("EMAIL_MESSAGE");
            
            TopDocs topDocs = searcher.search(query, 5000);

            for(int i=0; i<topDocs.totalHits; i++) {
                Document document = searcher.doc(i);
                String emailPath = document.get(constant);
                
                if ( emailPath != null && !emailPath.trim().isEmpty()) {
                    
                    if ( emailPath.endsWith(path) ) {
                        EmailItem item = (EmailItem) ItemFactory.newInstance(document, panel.getCaseFacade());
                        JTableUtil.addRowToJTable(panel.getTable(), item.getFullDisplayData());
                        ids.add(Integer.valueOf(item.getDocumentId()));
                    }
                }
            }
            
            searcher.close();
        } catch (ParseException ex) {
            Logger.getLogger(ChatRefreshTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("task 2 size: " + ids.size());
        this.panel.setResultIds(ids);
    }
}
