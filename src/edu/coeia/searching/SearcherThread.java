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
package edu.coeia.searching;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.cases.CaseFacade;
import edu.coeia.constants.IndexingConstant;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.items.Item;
import edu.coeia.items.ItemFactory;
import edu.coeia.searching.SearcherThread.ProgressSearchData;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
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
            List<Document> documents = searcher.search(queryString, this.searchScope);
            long end = new Date().getTime();
            this.time = end-start ;
           
            this.resultIds = new ArrayList<Integer>();
            
            for (Document document: documents) {
                try {
                    Item item = ItemFactory.newInstance(document, this.caseFacade, false);
                    publish(new ProgressSearchData(item));
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
    
    @Override
    protected void process(List<ProgressSearchData> chunks) {
        if ( isCancelled() )
            return; 
                
        for(ProgressSearchData pd: chunks) {
            JTableUtil.addRowToJTable(this.panel.getSearchTable(), pd.getItem().getDisplayData());
        }
    }
    
    class ProgressSearchData {
        private final Item item ;

        public ProgressSearchData (Item item) {
            this.item = item;
        }

        Item getItem () { return this.item ; }
    }
}
