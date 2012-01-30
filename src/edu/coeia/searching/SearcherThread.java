/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.searching;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.cases.Case;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.indexing.IndexingConstant ;
import edu.coeia.items.Item;
import edu.coeia.items.ItemFactory;
import edu.coeia.searching.SearcherThread.ProgressSearchData;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker ;

import java.util.Date ;
import java.util.List ;
import java.util.ArrayList ;

import org.apache.lucene.document.Document;

class SearcherThread extends SwingWorker<String,ProgressSearchData> {
    private long time ;
    private int count = 0 ;
    private String queryString ;
    
    private Case aCase;
    private LuceneSearcher searcher ;
    private AdvancedSearchPanel panel ;
    private SearchScope searchScope; 
    private List<Integer> resultIds;
    
    public SearcherThread (AdvancedSearchPanel panel) {
        this.queryString = panel.getQueryText();
        this.panel = panel ;
        this.searchScope = panel.getSearchScope() ;
    }
    
    @Override
    public String doInBackground() {
        try {
            this.aCase = this.panel.getCaseFacade().getCase();
            this.searcher = new LuceneSearcher(this.aCase);
            
            long start = new Date().getTime();
            this.count = searcher.search(queryString, this.searchScope);
            long end = new Date().getTime();
            this.time = end-start ;
           
            this.resultIds = new ArrayList<Integer>();

            for (int i=0 ; i<this.count; i++) {
                try {
                    Document document = this.searcher.getDocHits(i);
                    resultIds.add(Integer.parseInt(document.get(IndexingConstant.DOCUMENT_ID)));
                    publish(new ProgressSearchData(i, document));
                }
                catch(Exception e) { 
                    e.printStackTrace();
                }
            }

            try {
                this.searcher.closeSearcher();
            } catch (Exception ex) {
                Logger.getLogger(SearcherThread.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        } catch (Exception ex) {
           ex.printStackTrace();
        }

        return String.valueOf(this.time);
    }
    
    @Override
    public void done() {
        this.panel.setResultId(this.resultIds);
        this.panel.setResultTableText(this.queryString);
        this.panel.setSearchTableFocusable();
        this.panel.getSearchProgressBar().setIndeterminate(false);
    }
    
    @Override
    protected void process(List<ProgressSearchData> chunks) {
        if ( isCancelled() )
            return; 
                
        for(ProgressSearchData pd: chunks) {
            Item item = ItemFactory.newInstance(pd.getDocument(), this.aCase);
            JTableUtil.addRowToJTable(this.panel.getSearchTable(), item.getDisplayData());
        }
    }
    
    class ProgressSearchData {
        private final int count ;
        private final Document document ;

        public ProgressSearchData (int count, Document doc) {
            this.count = count ;
            this.document = doc;
        }

        int getCount () { return this.count  ; }
        Document getDocument () { return this.document ; }
    }
}
