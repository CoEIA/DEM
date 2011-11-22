/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.cases.Case;
import edu.coeia.cases.CaseHistoryHandler;
import edu.coeia.cases.EmailConfiguration;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.indexing.CrawlerIndexerThread.ProgressIndexData;
import edu.coeia.util.DateUtil;
import edu.coeia.util.FileUtil;
import edu.coeia.util.FilesPath;
import edu.coeia.util.SizeUtil;

import javax.swing.SwingWorker ;
import javax.swing.table.DefaultTableModel ;
import javax.swing.JOptionPane;

import java.io.File ;
import java.io.IOException ;

import java.util.Date ;
import java.util.List ;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

final class CrawlerIndexerThread extends SwingWorker<String,ProgressIndexData> {

    private long totalTimeOfIndexingProcess = 0 ;
    private long itemsCount, caseSize ;
    
    private int noOfFilesEnumerated = 0, noOfFilesIndexed=0;
    private int noOfFilesCannotIndexing = 0;

    private boolean indexStatus = false;
    
    private LuceneIndex luceneIndex ;
    
    private final Case aCase ;
    private final IndexingDialog parentDialog ;
    
    private static final Logger logger = Logger.getLogger(edu.coeia.util.FilesPath.LOG_NAMESPACE);

    public CrawlerIndexerThread (IndexingDialog parentDialog) {
        this.aCase = parentDialog.getCase();
        this.parentDialog = parentDialog;
     
        try {
            luceneIndex = LuceneIndex.newInstance(this.aCase);
            logger.log(Level.INFO, "Create Lucene Indexer Instance");
        } catch (IOException ex) {
           logger.log(Level.SEVERE, "Uncaught exception", ex);
        }
        
        this.parentDialog.setNumberOfFilesError("0");
        this.parentDialog.setProgressIndetermined(true);
    }
    
    @Override
    public String doInBackground() {
        long start = new Date().getTime();
         
        startCrawling();
        
        long end = new Date().getTime();
        totalTimeOfIndexingProcess = end-start ;

        indexStatus = true ;  
        itemsCount = this.noOfFilesIndexed ;
        
        return String.valueOf(totalTimeOfIndexingProcess);
    }
    
    private void startCrawling () {
        // crawle and index source directories
        for ( String dirName : aCase.getEvidenceSourceLocation() ) {
            if ( this.isCancelled() )
                throw new CancellationException("Cralwer is Cancelled by stop button");
                  
            logger.log(Level.INFO, "Start Index File: " + dirName);
            
            File directory = new File(dirName);
            doDirectoryCrawling(directory);
        } 
        
        // crawl and index emails
        if ( aCase.getEmailConfig().size() > 0 ) {
            File dbPath = new File(this.aCase.getCaseLocation() + "\\" + FilesPath.EMAIL_DB );
            EmailIndexer emailIndexer = new EmailIndexer(luceneIndex, dbPath, "", new OfficeImageExtractor());
            System.out.println("Status: " + emailIndexer.doIndexing());
        }
    }
    
    private void doDirectoryCrawling(File path) {
        if ( this.isCancelled() )
            throw new CancellationException("Cralwer is Cancelled by stop button");
                    
        if ( path.isDirectory() ) {
            File[] files = path.listFiles();
               
            for(File file: files) {
                if ( this.isCancelled() )
                    throw new CancellationException("Cralwer is Cancelled by stop button");
                            
                noOfFilesEnumerated++;
                doDirectoryCrawling(file);
            }
            
            if ( this.luceneIndex.indexDir(path) )
                noOfFilesIndexed++;
        }
        else {
            long size = path.length();
            caseSize += size; 
            
            // if file size more than 3 MB then show size message to indicate that indexing will take some time
            String msg = size > 3145728 ? "This file will take some minutes to index, please wait..." : " " ;
            
            // publish file progress (update labels)
            publish(new ProgressIndexData( noOfFilesEnumerated,noOfFilesIndexed, 
                    path.getAbsolutePath(), "" , ProgressIndexData.TYPE.LABEL , msg));

            boolean status = this.luceneIndex.indexFile(path);
            if ( ! status ) // update error table
                publish(new ProgressIndexData( noOfFilesEnumerated,noOfFilesIndexed,
                        path.getAbsolutePath(), "Cannot Index This File", ProgressIndexData.TYPE.TABEL , msg));
            else
                noOfFilesIndexed++;

            ++noOfFilesEnumerated ;
        }
    }
    
    @Override
    protected void process(List<ProgressIndexData> chunks) {
        if ( isCancelled() )
            return; 
                
        for (ProgressIndexData pd : chunks) {
            if ( pd.getType() == ProgressIndexData.TYPE.TABEL ) {
                ((DefaultTableModel)this.parentDialog.getLoggingTable().getModel()).addRow(new Object[] { FileUtil.getExtension(pd.getPath())
                    , pd.getPath(), "cannot index this file (password protected or internal error in file format)"});

                noOfFilesCannotIndexing++;
                this.parentDialog.setNumberOfFilesError(String.valueOf(this.noOfFilesCannotIndexing));
                this.parentDialog.setprogressBar(pd.getProgressCount());
            }
            else {
                this.parentDialog.setCurrentFile(pd.getPath());
                this.parentDialog.setFileSize(SizeUtil.getSize(pd.getPath()));
                this.parentDialog.setNumberOfFiles(String.valueOf(pd.getIndexCount()));
                this.parentDialog.setFileExtension(FileUtil.getExtension(pd.getPath()));
                this.parentDialog.setBigSizeLabel(pd.getSizeMsg());
                this.parentDialog.setprogressBar(pd.getProgressCount());
                this.parentDialog.setprogressBar(pd.getIndexCount());
            }
        }

       int indexNum = chunks.size()-1 ;
       JTableUtil.scrollToVisible(this.parentDialog.getLoggingTable(),(chunks.get(indexNum)).getProgressCount(),0);
    }

    @Override
    public void done() {
        try {
            logger.log(Level.INFO, "Done Indexing Process");
            
            String endTime = this.get();
            String lastIndexDate = DateUtil.formatDateTime(new Date()) ;

            // show message box after finish the indexing process if there is problem
            if ( indexStatus )
                JOptionPane.showMessageDialog(this.parentDialog, "Indexing Process Completed Successfully","Indexing Process Is Completed",
                    JOptionPane.INFORMATION_MESSAGE);
            
            // set dialog label
            this.parentDialog.setProgressIndetermined(false);
            this.parentDialog.setTimeLabel(endTime);
            this.parentDialog.setLastIndexTime(lastIndexDate);
            this.parentDialog.setStartButtonStatus(true);
            this.parentDialog.setStopButtonStatus(false);

            // save case history & close the index
            if ( indexStatus ) {
                CaseHistoryHandler.CaseHistory history = CaseHistoryHandler.CaseHistory.newInstance(
                        this.aCase.getIndexName(), new Date().toString(), true, this.itemsCount, 
                        this.caseSize);

                CaseHistoryHandler.set(history);
            }
            this.parentDialog.hideIndexingDialog();
        }
        catch(InterruptedException e) {}
        catch(CancellationException e) {
            JOptionPane.showMessageDialog(this.parentDialog, "Indexing Process Stopped","Indexing Process Is Not Completed",
                    JOptionPane.ERROR_MESSAGE);
        }
        catch(ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            try {
                clearFields();
                closeIndex();
            } catch (IOException ex) {
                Logger.getLogger(CrawlerIndexerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void clearFields() {
        this.parentDialog.setBigSizeLabel("");
        this.parentDialog.setFileExtension("");
        this.parentDialog.setCurrentFile("");
        this.parentDialog.setFileSize("");
        this.parentDialog.setprogressBar(0);
        this.parentDialog.getProgressBar().setStringPainted(false);
        this.parentDialog.setProgressIndetermined(false);
        this.parentDialog.setStartButtonStatus(true);
        this.parentDialog.setStopButtonStatus(false);
    }
    
    public void stopIndexingThread() throws IOException {
        this.cancel(true);
    }
    
    private void closeIndex () throws IOException {
        luceneIndex.closeIndex();
    }
    
    static final class ProgressIndexData {
        private enum TYPE {LABEL, TABEL}
        
        private final String path;
        private final int progressCount ;
        private final int indexCount ;
        private final String status ;
        private final TYPE type ;
        private final String sizeMsg ;

        public ProgressIndexData (int progressCount, int indexCount, String p,
                String status, TYPE type, String sm) {
            this.progressCount = progressCount;
            this.path = p ;
            this.indexCount = indexCount ;
            this.status = status ;
            this.type = type;
            this.sizeMsg = sm;
        }

        public String getPath ()        { return this.path   ; }
        public int getProgressCount ()  { return this.progressCount  ; }
        public String getStatus()       { return this.status ; }
        public TYPE getType ()           { return this.type   ; }
        public int getIndexCount()      { return this.indexCount ;}
        public String getSizeMsg()      { return this.sizeMsg; }
    }
}
