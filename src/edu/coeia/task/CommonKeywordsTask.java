/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.cases.Case;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.investigation.CommonKeywordsPanel;
import edu.coeia.constants.ApplicationConstants;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author wajdyessam
 */
public class CommonKeywordsTask implements Task{
    private final TaskThread thread;
    private final Case aCase;
    private final CommonKeywordsPanel panel;
    
    public CommonKeywordsTask(final Case aCase, final CommonKeywordsPanel panel) {
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
        Map<String, Integer> commonKeywordsMap = this.getAllTermFreqFromBody();
        this.panel.setTags(commonKeywordsMap);
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.thread.isCancelledThread();
    }
    
    // get terms and frequncy for all terms in docuemnts
    public Map<String,Integer> getAllTermFreqFromBody ()  throws IOException {
        String indexDir = this.aCase.getCaseLocation() + File.separator + ApplicationConstants.CASE_INDEX_FOLDER;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        
        Map<String,Integer> map = new HashMap<String,Integer>();
        TermEnum te = indexReader.terms(new Term(IndexingConstant.FILE_CONTENT,"") );

        while ( te.next() ) {
            if ( isCancelledTask() )
                return map;
            
            Term currentTerm = te.term();

            if ( ! currentTerm.field().equals(IndexingConstant.FILE_CONTENT))
                continue ;

            String termText = currentTerm.text();
            int frequency   = indexReader.docFreq(currentTerm);

            map.put(termText,frequency);
        }

        te.close();
        return map ;
    }
}
