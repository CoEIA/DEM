/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.searching;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.gutil.JTableUtil;
import edu.coeia.indexing.IndexingConstant ;
import edu.coeia.util.DateUtil;

import javax.swing.SwingWorker ;
import javax.swing.table.DefaultTableModel ;

import java.io.File ;

import java.util.Date ;

import org.apache.lucene.document.Document;

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

class SearcherThread extends SwingWorker<String,ProgressSearchData> {
    private long time ;
    private File indexLocation  ;
    private String queryString ;
    private LuceneSearcher searcher ;
    private int count = 0 ;
    private AdvancedSearchPanel panel ;
    private SearchScope searchScope; 
    
    public SearcherThread (File indexLocation, String queryString, AdvancedSearchPanel panel, SearchScope fields) {
        this.indexLocation = indexLocation ;
        this.queryString = queryString;
        this.panel = panel ;
        this.searchScope = fields ;
    }
    
    public String doInBackground() {
        try {
            long start = new Date().getTime();
            
            searcher = new LuceneSearcher(indexLocation);
            count = searcher.search(queryString, this.searchScope);

            fillTable();
            
            long end = new Date().getTime();
            time = end-start ;

            searcher.closeSearcher();

           
        } catch (Exception ex) {
           ex.printStackTrace();
        }

        return "" + time ;
    }

    private void fillTable () throws Exception {
        for (int i=0 ; i<count ; i++) {
            Document document = searcher.getDocHits(i);
            publish(new ProgressSearchData(i, document));
        }
    }
    
    @Override
    protected void process(java.util.List<ProgressSearchData> chunks) {
        for (ProgressSearchData pd : chunks) {
            
            String type = pd.getDocument().get(IndexingConstant.DOCUMENT);
            
            if ( type.equals(IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.FILE))) {
                String fileId = pd.getDocument().get(IndexingConstant.DOCUMENT_ID);
                String fileDate = pd.getDocument().get(IndexingConstant.FILE_DATE);
                String fileTitle = pd.getDocument().get(IndexingConstant.FILE_TITLE);
                String fileName = pd.getDocument().get(IndexingConstant.FILE_NAME);

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

       int index = chunks.size()-1 ;
       JTableUtil.packColumns(panel.getSearchTable(), index);
    }
    
    @Override
    public void done() {
        panel.getSearchProgressBar().setIndeterminate(false);
    }
}
