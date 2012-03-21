/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.searching;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.cases.CaseFacade;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.constants.IndexingConstant ;
import edu.coeia.items.Item;
import edu.coeia.items.ItemFactory;
import edu.coeia.searching.SearcherThread.ProgressSearchData;

import java.awt.Color;

import javax.swing.SwingWorker ;
import javax.swing.JOptionPane;

import java.util.Date ;
import java.util.List ;
import java.util.ArrayList ;

import org.apache.lucene.document.Document;

class SearcherThread extends SwingWorker<String,ProgressSearchData> {
    private long time ;
    private int count = 0 ;
    private String queryString ;
    
    private CaseFacade caseFacade;
    private LuceneSearcher searcher ;
    private AdvancedSearchPanel panel ;
    private SearchScope searchScope; 
    private List<Integer> resultIds;
    private List<Item> items = new ArrayList<Item>();
    
    public SearcherThread (AdvancedSearchPanel panel) {
        this.queryString = panel.getQueryText();
        this.panel = panel ;
        this.caseFacade = this.panel.getCaseFacade();
        this.searchScope = panel.getSearchScope() ;
    }
    
    @Override
    public String doInBackground() {
        try {
            this.panel.getSearchTable().setEnabled(false);
            this.panel.getSearchTable().setForeground(Color.GRAY);

            this.searcher = new LuceneSearcher(this.panel.getCaseFacade().getCase());
            
            long start = new Date().getTime();
            this.count = searcher.search(queryString, this.searchScope);
            long end = new Date().getTime();
            this.time = end-start ;
           
            this.resultIds = new ArrayList<Integer>();
            
            for (int i=0 ; i<this.count; i++) {
                try {
                    Document document = this.searcher.getDocHits(i);
                    Item item = ItemFactory.newInstance(document, this.caseFacade);
                    resultIds.add(Integer.parseInt(document.get(IndexingConstant.DOCUMENT_ID)));
                    items.add(item);
                }
                catch(Exception e) { 
                    e.printStackTrace();
                }
            }

            this.searcher.closeSearcher();
            
        } catch (Exception ex) {
           ex.printStackTrace();
        }

        return String.valueOf(this.time);
    }
    
    @Override
    public void done() {
        // render search result
        for(Item item: this.items ) {
            JTableUtil.addRowToJTable(this.panel.getSearchTable(), item.getDisplayData());
        }
        
        this.items.clear();
        
        this.panel.setResultId(this.resultIds);
        this.panel.setResultTableText(this.queryString);
        this.panel.setSearchTableFocusable();
        this.panel.getSearchProgressBar().setIndeterminate(false);
        this.panel.getSearchTable().setEnabled(true);
        this.panel.getSearchTable().setForeground(Color.BLACK);
        this.panel.getResultSaveButton().setEnabled(true);
        
        JOptionPane.showMessageDialog(this.panel, "Searching process is complete");
    }
    
//    @Override
//    protected void process(List<ProgressSearchData> chunks) {
//        if ( isCancelled() )
//            return; 
//                
//        for(ProgressSearchData pd: chunks) {
//            JTableUtil.addRowToJTable(this.panel.getSearchTable(), pd.getItem().getDisplayData());
//        }
//    }
    
    class ProgressSearchData {
        private final int count ;
        private final Item item ;

        public ProgressSearchData (int count, Item item) {
            this.count = count ;
            this.item = item;
        }

        int getCount () { return this.count  ; }
        Item getItem () { return this.item ; }
    }
}
