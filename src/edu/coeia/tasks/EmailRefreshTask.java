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

import edu.coeia.cases.Case;
import edu.coeia.wizard.EmailConfiguration;
import edu.coeia.gutil.JListUtil;
import edu.coeia.constants.IndexingConstant;
import edu.coeia.offlinemail.EmailBrowsingPanel;

import java.awt.EventQueue;

import java.io.File;
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author wajdyessam
 */
public class EmailRefreshTask implements Task{
    private final BackgroundProgressDialog dialog ;
    private final Case aCase;
    private final EmailBrowsingPanel panel;
    
    public EmailRefreshTask(final Case aCase, final EmailBrowsingPanel panel) {
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
        this.fillTable();
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.dialog.isCancelledThread();
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
        Set<String> result = new HashSet<String>();
  
         Directory directory = FSDirectory.open(new File(
            this.panel.getCaseFacade().getCaseIndexFolderLocation()
            ));

        IndexSearcher searcher = new IndexSearcher(directory);

        Query query = new TermQuery(new Term(IndexingConstant.DOCUMENT_TYPE, "email"));
        TopDocs topDocs = searcher.search(query, 5000);

        for(ScoreDoc scoreDoc: topDocs.scoreDocs) {
            Document document = searcher.doc(scoreDoc.doc);
            String offlineEmailPath = document.get(IndexingConstant.OFFLINE_EMAIL_PATH);

            if ( offlineEmailPath != null && !offlineEmailPath.trim().isEmpty()) {
                result.add(offlineEmailPath);
            }
        }
        
        searcher.close();
        
        return result;
    }
}
