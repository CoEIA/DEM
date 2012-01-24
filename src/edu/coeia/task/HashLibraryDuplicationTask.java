/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

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
    private final TaskThread thread;
    private final Case aCase;
    private final HashAnalysisPanel panel;
    
    public HashLibraryDuplicationTask(final Case aCase, final HashAnalysisPanel panel) {
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
        this.doHashLibraryDuplicationAnalysis();
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.thread.isCancelledThread();
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
