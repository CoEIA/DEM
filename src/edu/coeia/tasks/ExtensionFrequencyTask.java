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
import edu.coeia.cases.CaseFacade;
import edu.coeia.charts.PieChartPanel;
import edu.coeia.constants.IndexingConstant;
import edu.coeia.investigation.ExtensionFrequencyPanel;
import edu.coeia.util.FileUtil;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
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
public class ExtensionFrequencyTask implements Task{
    private final BackgroundProgressDialog dialog ;
    private final Case aCase;
    private final ExtensionFrequencyPanel panel;
    private final CaseFacade caseFacade ;
    
    public ExtensionFrequencyTask(final CaseFacade caseFacade, final ExtensionFrequencyPanel panel) {
        this.dialog = new BackgroundProgressDialog(null, true, this);
        this.caseFacade = caseFacade;
        this.aCase = this.caseFacade.getCase();
        this.panel = panel;
    }
    
    @Override
    public void startTask() {
        this.dialog.startThread();
    }
    
    @Override
    public void doTask() throws Exception {
        Map<String, Double> result = this.getExtensionFreqFast();
        JPanel chartPanel = PieChartPanel.newInstance(result,
                "Extension Frequency for: " + this.aCase.getCaseName(), getFactor());
        this.panel.setIndexVisualizationPanel(chartPanel);
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.dialog.isCancelledThread();
    }
    
    private int getFactor() throws IOException {
        Directory directory = FSDirectory.open(new File(
            this.caseFacade.getCaseIndexFolderLocation()
        ));
        
        IndexReader indexReader = IndexReader.open(directory);
        
        int factor = indexReader.maxDoc() / 200; 
        indexReader.close();
        
        return factor;
    }
    
    private Map<String,Double> getExtensionFreqFast() throws IOException {
        Map<String,Double> map = new HashMap<String,Double>();
        
        try {
            Directory directory = FSDirectory.open(new File(
                    this.caseFacade.getCaseIndexFolderLocation()
                    ));
            
            IndexSearcher searcher = new IndexSearcher(directory);
            QueryParser parser = new QueryParser(Version.LUCENE_30, 
                    IndexingConstant.DOCUMENT_TYPE, new StopAnalyzer(Version.LUCENE_30));
            parser.setAllowLeadingWildcard(true);
            Query query = parser.parse("file");
            
            TopDocs topDocs = searcher.search(query, 100000);

            for(ScoreDoc scoreDoc: topDocs.scoreDocs) {
                Document document = searcher.doc(scoreDoc.doc);
                String filePath = document.get(IndexingConstant.FILE_PATH);
                
                if ( filePath != null && !filePath.trim().isEmpty()) {
                    final File path = new File(filePath);
                    String ext = FileUtil.getExtension(path);

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
            
            searcher.close();
        } catch (ParseException ex) {
            Logger.getLogger(ChatRefreshTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return map ;
    }
}
