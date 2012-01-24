/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;


import edu.coeia.cases.Case;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.hashanalysis.HashAnalysisPanel;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.util.FilesPath;

import java.io.File;

import java.util.Collection;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.lucene.index.IndexReader ;
import org.apache.lucene.store.Directory ;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document ;
import org.apache.lucene.document.Field;


/**
 *
 * @author wajdyessam
 */
public class CaseDuplicationTask implements Task{
        private final TaskThread thread;
    private final Case aCase;
    private final HashAnalysisPanel panel;
    
    public CaseDuplicationTask(final Case aCase, final HashAnalysisPanel panel) {
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
        this.doCaseDuplicationAnalysis();
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.thread.isCancelledThread();
    }
    
    private void doCaseDuplicationAnalysis() {
        try {
            boolean isFoundDuplication = this.findCaseDuplication();
            
            if ( !isFoundDuplication ) {
                JOptionPane.showMessageDialog(null, "There is no duplication in this case");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public boolean findCaseDuplication() throws Exception {
        this.fillCaseDuplicationMap();  // read from index and fill the duplication map
        
        boolean isFoundDuplication = false; 
        Map<String, Collection<String>> m = this.panel.getCaseDuplicationMap().asMap();
        
        for(Map.Entry<String, Collection<String>> mapEntry: m.entrySet()){
            String key = mapEntry.getKey();
            Collection<String> documents = mapEntry.getValue();
            
            if ( documents.size() > 1 ) { // find duplication  
                isFoundDuplication = true;
                Object[] data = {key, documents.size()};
                JTableUtil.addRowToJTable(this.panel.getCaseDuplicationTable(), data);
            }
            
            documents = null;
        }
        
        return isFoundDuplication;
    }
        
    private void fillCaseDuplicationMap() throws Exception {
        String indexDir = this.aCase.getCaseLocation() + File.separator + FilesPath.INDEX_PATH;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        
        for (int i=0; i<indexReader.maxDoc(); i++) {
            Document document = indexReader.document(i);
            if ( document != null ) {
                Field field = document.getField(IndexingConstant.DOCUMENT_HASH);
                if ( field != null && field.stringValue() != null) {
                   String documentHash = field.stringValue();
                   this.panel.getCaseDuplicationMap().put(documentHash, document.get(IndexingConstant.DOCUMENT_ID));
                }
            }
        }
        indexReader.close();
    }
    
}
