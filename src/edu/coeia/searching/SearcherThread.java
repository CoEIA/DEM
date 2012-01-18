/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.searching;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.indexing.IndexingConstant ;

import edu.coeia.searching.SearcherThread.ProgressSearchData;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker ;
import javax.swing.table.DefaultTableModel ;

import java.util.Date ;
import java.util.List ;
import java.util.ArrayList ;

import org.apache.lucene.document.Document;

class SearcherThread extends SwingWorker<String,ProgressSearchData> {
    private long time ;
    private int count = 0 ;
    private String queryString ;
    private LuceneSearcher searcher ;
    private AdvancedSearchPanel panel ;
    private SearchScope searchScope; 
    
    public SearcherThread (AdvancedSearchPanel panel) {
        this.queryString = panel.getQueryText();
        this.panel = panel ;
        this.searchScope = panel.getSearchScope() ;
    }
    
    @Override
    public String doInBackground() {
        try {
            this.searcher = new LuceneSearcher(this.panel.getCase());
            
            long start = new Date().getTime();
            count = searcher.search(queryString, this.searchScope);
            long end = new Date().getTime();
            time = end-start ;
           
        } catch (Exception ex) {
           ex.printStackTrace();
        }

        return "" + time ;
    }
    
    @Override
    public void done() {
        panel.getSearchProgressBar().setIndeterminate(false);
        
        List<Integer> ids = new ArrayList<Integer>();
        
        for (int i=0 ; i<count ; i++) {
            try {
                Document document = searcher.getDocHits(i);
                ids.add(Integer.parseInt(document.get(IndexingConstant.DOCUMENT_ID)));
                showData(new ProgressSearchData(i, document));
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
        panel.setResultId(ids);
        panel.setResultTableText(queryString);
        panel.setSearchTableFocusable();
    }
    
    
    private void showData(ProgressSearchData pd) {
        String type = pd.getDocument().get(IndexingConstant.DOCUMENT);

        if ( type.equals(IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.FILE))) {
            String fileId = pd.getDocument().get(IndexingConstant.DOCUMENT_ID);
            String fileDate = pd.getDocument().get(IndexingConstant.FILE_DATE);
            String fileTitle = pd.getDocument().get(IndexingConstant.FILE_NAME);
            String fileName = pd.getDocument().get(IndexingConstant.FILE_PATH);

            ((DefaultTableModel)panel.getSearchTable().getModel()).addRow(new Object[] {
                fileId, fileTitle, new Date(Long.valueOf(fileDate)) , type, fileName
            });
        }

        if ( type.equals(IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.ONLINE_EMAIL))) {
            String fileId = pd.getDocument().get(IndexingConstant.DOCUMENT_ID);
            String fileDate = pd.getDocument().get(IndexingConstant.ONLINE_EMAIL_SENT_DATE);
            String fileTitle = pd.getDocument().get(IndexingConstant.ONLINE_EMAIL_FOLDER_NAME);
            String fileName = pd.getDocument().get(IndexingConstant.ONLINE_EMAIL_SUBJECT);

            ((DefaultTableModel)panel.getSearchTable().getModel()).addRow(new Object[] {
                fileId, fileTitle, fileDate, type, fileName
            });
        }

        if ( type.equals(IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.OFFLINE_EMAIL))) {
            String fileId = pd.getDocument().get(IndexingConstant.DOCUMENT_ID);
            String fileDate = pd.getDocument().get(IndexingConstant.OFFLINE_EMAIL_CLIENT_SUBMIT_TIME);
            String fileTitle = pd.getDocument().get(IndexingConstant.OFFLINE_EMAIL_FOLDER_NAME);
            String fileName = pd.getDocument().get(IndexingConstant.OFFLINE_EMAIL_SUBJECT);

            ((DefaultTableModel)panel.getSearchTable().getModel()).addRow(new Object[] {
                fileId, fileTitle, fileDate, type, fileName
            });
        }
        
        if ( type.equals(IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.CHAT))) {
            String fileId = pd.getDocument().get(IndexingConstant.DOCUMENT_ID);
            String fileDate = pd.getDocument().get(IndexingConstant.CHAT_TIME);
            String fileTitle = pd.getDocument().get(IndexingConstant.CHAT_AGENT);
            String fileName = pd.getDocument().get(IndexingConstant.CHAT_FILE);

            ((DefaultTableModel)panel.getSearchTable().getModel()).addRow(new Object[] {
                fileId, fileTitle, fileDate , type, fileName
            }); 
        }
    }
    
    
    class ProgressSearchData {
        private String path;
        private int count ;
        private int numberOfPatterns ;
        private Document document ;

        public ProgressSearchData (int c, Document doc) {
            count = c ;
            this.document = doc;
        }

        public String getPath ()    { return path   ; }
        public int getCount ()      { return count  ; }
        public int getNumberOfPatterns () { return numberOfPatterns ; }
        public Document getDocument () { return this.document ; }
    }
}
