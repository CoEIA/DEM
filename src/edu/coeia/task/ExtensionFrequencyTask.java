/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.cases.Case;
import edu.coeia.cases.CasePathHandler;
import edu.coeia.charts.PieChartPanel;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.investigation.ExtensionFrequencyPanel;
import edu.coeia.util.FileUtil;
import edu.coeia.util.FilesPath;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author wajdyessam
 */
public class ExtensionFrequencyTask implements Task{
    private final TaskThread thread;
    private final Case aCase;
    private final ExtensionFrequencyPanel panel;
    
    public ExtensionFrequencyTask(final Case aCase, final ExtensionFrequencyPanel panel) {
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
        try {
            Map<String, Double> extensionFrequncyMap = this.getExtensionFreq();
            JPanel chartPanel = PieChartPanel.newInstance(extensionFrequncyMap, "Extension Frequency for: " + this.aCase.getCaseName());
            this.panel.setIndexVisualizationPanel(chartPanel);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.thread.isCancelledThread();
    }
    
    public Map<String,Double> getExtensionFreq () throws IOException {
        Map<String,Double> map = new HashMap<String,Double>();
        
        String indexDir = this.aCase.getCaseLocation() + File.separator + FilesPath.INDEX_PATH;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        
        CasePathHandler handler = CasePathHandler.newInstance(aCase.getCaseLocation());
        handler.readConfiguration();
        
        int max = indexReader.maxDoc();
        
        for (int i=0; i<max; i++) {
            if ( this.isCancelledTask() )
                return map;;
                        
            Document document = indexReader.document(i);
            if ( document != null ) {
                Field field = document.getField(IndexingConstant.FILE_PATH);
                if ( field != null && field.stringValue() != null) {
                    String path = field.stringValue();
                    String fullPath = handler.getFullPath(path);
                    String ext = FileUtil.getExtension(fullPath);

                    if ( ext == null || ext.length() > 6) // no more extension than 5 character!
                        continue;

                    ext = ext.toLowerCase();

                    if ( map.get(ext) == null ){
                        map.put(ext, 1.0);
                    }
                    else
                        map.put(ext, map.get(ext) + 1);
                }
            }
            
            document = null;
        }
        
        indexReader.close();
        
//        TermEnum te = indexReader.terms(new Term(IndexingConstant.FILE_PATH,"") );
//        while ( te.next() ) {
//            Term currentTerm = te.term();
//
//            if ( ! currentTerm.field().equals(IndexingConstant.FILE_PATH))
//                continue ;
//
//            String file = currentTerm.text();
//            String fullPath = handler.getFullPath(file);
//            String ext = FileUtil.getExtension(fullPath);
//            
//            if ( ext == null || ext.length() > 6) // no more extension than 5 character!
//                continue;
//
//            ext = ext.toLowerCase();
//
//            if ( map.get(ext) == null ){
//                map.put(ext, 1.0);
//            }
//            else
//                map.put(ext, map.get(ext) + 1);
//        }
//
//        te.close();
        
        return map ;
    }
}
