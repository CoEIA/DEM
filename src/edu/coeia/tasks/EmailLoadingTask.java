/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tasks;

import edu.coeia.cases.Case;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.constants.IndexingConstant;
import edu.coeia.items.EmailItem;
import edu.coeia.items.ItemFactory;
import edu.coeia.offlinemail.EmailBrowsingPanel;
import java.awt.EventQueue;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

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
        String user = "";
        String path = "";
        String desc = "";
        
        if ( this.panel.isOfflineEmailSelected() ) {
            path = String.valueOf(this.panel.getList().getSelectedValue());
            user = IndexingConstant.OFFLINE_EMAIL_PATH;
            desc = IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_GENERAL_TYPE.OFFLINE_EMAIL);
            
        }
        else if ( this.panel.isOnlineEmailSelected() ) {
            path = String.valueOf(this.panel.getList().getSelectedValue());
            user = IndexingConstant.ONLINE_EMAIL_USER_NAME;
            desc = IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_GENERAL_TYPE.ONLINE_EMAIL);
        }
        
        this.getAllEmailMessagesFast(path, user, desc);
    }
    
    private void getAllEmailMessagesFast(final String path, final String constant, final String type) throws IOException {
        List<Integer> ids = new ArrayList<Integer>();
        
        try {
            Directory directory = FSDirectory.open(new File(
                    this.panel.getCaseFacade().getCaseIndexFolderLocation()
                    ));
            
            IndexSearcher searcher = new IndexSearcher(directory);
            QueryParser parser = new QueryParser(Version.LUCENE_30, 
                    IndexingConstant.DOCUMENT_TYPE, new StopAnalyzer(Version.LUCENE_30));
            parser.setAllowLeadingWildcard(true);
            Query query = parser.parse("email");
            
            TopDocs topDocs = searcher.search(query, 100000);

            for(ScoreDoc scoreDocs: topDocs.scoreDocs) {
                Document document = searcher.doc(scoreDocs.doc);
                String emailPath = document.get(constant);
                
                if ( emailPath != null && !emailPath.trim().isEmpty()) {
                    
                    if ( emailPath.endsWith(path) ) {
                        final EmailItem item = (EmailItem) ItemFactory.newInstance(document, panel.getCaseFacade());
                        
                        EventQueue.invokeLater(new Runnable() { 
                            @Override
                            public void run() {
                                JTableUtil.addRowToJTable(panel.getTable(), item.getFullDisplayData());
                            }
                        });
                        
                        ids.add(Integer.valueOf(item.getDocumentId()));
                    }
                }
            }
            
            searcher.close();
        } catch (ParseException ex) {
            Logger.getLogger(ChatRefreshTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.panel.setResultIds(ids);
    }
}
