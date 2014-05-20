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

import edu.coeia.hashanalysis.HashAnalysisPanel;
import edu.coeia.cases.Case;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.searching.LuceneSearcher;
import edu.coeia.hashanalysis.HashAnalysisPanel.MatchingResult;
import edu.coeia.hashanalysis.HashCategory;
import edu.coeia.hashanalysis.HashItem;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.lucene.document.Document ;

/**
 *
 * @author wajdyessam
 */
public class HashLibraryDuplicationTask implements Task{
    private final BackgroundProgressDialog dialog ;
    private final Case aCase;
    private final HashAnalysisPanel panel;
    
    public HashLibraryDuplicationTask(final Case aCase, final HashAnalysisPanel panel) {
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
        this.doHashLibraryDuplicationAnalysis();
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.dialog.isCancelledThread();
    }
    
    private void doHashLibraryDuplicationAnalysis() {
        Object[] values = this.panel.getHashLibraryList().getSelectedValues();
        for(Object value: values) {
            String hashCategoryName = String.valueOf(value);
            List<MatchingResult> results = this.startHashAnalysis(hashCategoryName);
            this.panel.getHashLibraryDuplicationResult().addAll(results);
        }
        
        if ( this.panel.getHashLibraryDuplicationResult().isEmpty() ) {
            JOptionPane.showMessageDialog(null, "There is no duplication with selected hash set(s)",
                    "cannot find any matched files in this case",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private List<MatchingResult> startHashAnalysis(final String hashSetName) {
        HashCategory hashCategory = this.getHashCategory(hashSetName);
        List<MatchingResult> matchedDuplications = new ArrayList<MatchingResult>();
        
        for(HashItem item: hashCategory.getItems()) {
            String hashValue = item.getHashValue();
            List<Document> documents = searchFor(hashValue);
            
            if ( !documents.isEmpty() ) {
                Object[] data = {
                    item.getFileName(), item.getFilePath(), 
                    hashSetName, item.getCaseName(), item.getCasePath(), 
                    item.getHashValue(), item.getInvestigatorName(), item.getTime()
                };
                
                JTableUtil.addRowToJTable(this.panel.getAnalysisResultTable(), data);
                
                MatchingResult result = new MatchingResult(hashCategory, item, documents);
                matchedDuplications.add(result);
            }
        }
        
        return matchedDuplications;
    }
    
    private List<Document> searchFor(final String hashValue) {
        List<Document> documents = new ArrayList<Document>();
        LuceneSearcher luceneSearcher = null;
        
        try {
            luceneSearcher = new LuceneSearcher(this.aCase);
            int hits = luceneSearcher.searchForHash(hashValue);
            
            for(int i=0; i<hits; i++) {
                Document document = luceneSearcher.getDocHits(i);
                documents.add(document);
            }
            
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
             try {
                 if ( luceneSearcher != null )
                    luceneSearcher.closeSearcher();
             }
             catch(Exception e) {
                 e.printStackTrace();
             }
        }
        
        return documents;
    }
    
    private HashCategory getHashCategory(final String hashSetName) {
        HashCategory hashCategory = null;
        
        for(HashCategory tmp: this.panel.getHashCateogries() ) {
            if ( tmp.getName().equals(hashSetName) ) {
                hashCategory = tmp;
                break;
            }
        }
        
        return hashCategory;
    }
}
