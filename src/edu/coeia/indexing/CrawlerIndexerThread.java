/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.extractors.OfficeImageExtractor;
import edu.coeia.cases.Case;
import edu.coeia.cases.CaseHistoryHandler;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.indexing.CrawlerIndexerThread.ProgressIndexData;
import edu.coeia.util.DateUtil;
import edu.coeia.util.FileUtil;
import edu.coeia.util.FilesPath;
import edu.coeia.util.SizeUtil;

import javax.swing.SwingWorker ;
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
    private long sizeOfFilesInEvidenceFolder ;
    private long numberOfFilesInEvidenceFolder;
    
    private long numberOfFilesIndexed;
    private long numberOfFilesCannotIndexed;

    private boolean indexStatus = false;
    
    private final Case aCase ;
    private final LuceneIndex luceneIndex ;
    private final IndexingDialog parentDialog ;
    private static final Logger logger = Logger.getLogger(edu.coeia.util.FilesPath.LOG_NAMESPACE);

    public CrawlerIndexerThread (IndexingDialog parentDialog) throws IOException{
        this.aCase = parentDialog.getCase();
        this.parentDialog = parentDialog;
        this.luceneIndex = LuceneIndex.newInstance(this.aCase);
        this.parentDialog.setNumberOfFilesError("0");
        this.parentDialog.setProgressIndetermined(true);
        
        logger.log(Level.INFO, "Create Lucene Indexer Instance");
    }
    
    @Override
    public String doInBackground() {
        long start = new Date().getTime();
        this.indexStatus = startCrawling();
        long end = new Date().getTime();
        long totalTimeOfIndexingProcess = end-start ;
        return String.valueOf(totalTimeOfIndexingProcess);
    }
    
    /**
     * crawling case sources and index each of them
     * 
     * @return ture if indexing without problem
     */
    private boolean startCrawling () {
        boolean status = false; 
        
        try {
            // write evidence location information on index
            this.writeEvidenceLocation(this.aCase.getEvidenceSourceLocation());
            
            // crawle and index source directories
            for ( String dirName : this.aCase.getEvidenceSourceLocation() ) {
                this.checkForThreadCancelling();
                this.doDirectoryCrawling(new File(dirName));
            }

            // crawl and index emails
            if ( !aCase.getEmailConfig().isEmpty()) {
               this.doEmailCrawling();
            }
            
            status = true;
        }
        catch(Exception e){
           logger.log(Level.SEVERE, "Stopping Indexing Process", e);
        }
        
        return status;
    }
    
    private void writeEvidenceLocation(final List<String> paths) throws IOException{
        this.luceneIndex.writeEvidenceLocation(paths);
    }
    
    private void doDirectoryCrawling(File path) {
        this.checkForThreadCancelling();
        logger.log(Level.INFO, "Indexing Folder: " + path.getAbsolutePath());
        
        if ( path.isDirectory() && path.canRead() ) {
            File[] files = path.listFiles();
               
            try {
                for(File file: files) {
                    this.checkForThreadCancelling();

                    if ( file.isDirectory() && file.canRead()) {
                        if ( this.luceneIndex.indexDir(path) )
                            numberOfFilesIndexed++;
                        
                        doDirectoryCrawling(file);
                    }
                    else if ( file.isFile() && file.canRead()) {
                        boolean status = doFileCrawling(file);
                        
                        if (status) {
                            numberOfFilesInEvidenceFolder++;
                            logger.log(Level.INFO, "File Indexing Successfully: " + file.getAbsolutePath());
                        }
                    }
                }
            }
            // to prevent NullPointerException casued by accessing
            // folder we have no permission to acess (files array will contain null value)
            catch(Exception e) { 
                //TODO: show in logging table that the current folder
                // cannot be indexed becuase of permission problem
                //e.printStackTrace();
                Logger.getLogger(CrawlerIndexerThread.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
    
    private boolean doFileCrawling(final File path) {
        long size = path.length();
        
        // if file size more than 3 MB then show size message to indicate that indexing will take some time
        String msg = size > 3145728 ? "This file will take some minutes to index, please wait..." : " " ;

        this.sizeOfFilesInEvidenceFolder += size; 
        
        // publish file progress (update labels)
        publish(new ProgressIndexData( numberOfFilesInEvidenceFolder,numberOfFilesIndexed, 
                path.getAbsolutePath(), "" , ProgressIndexData.TYPE.LABEL , msg));

        boolean status = false;

        try {
            status = this.luceneIndex.indexFile(path, this.parentDialog);
            this.numberOfFilesIndexed++;
        }
        catch (Exception e) {
          publish(new ProgressIndexData( numberOfFilesInEvidenceFolder,numberOfFilesIndexed,
                path.getAbsolutePath(), e.getMessage() , ProgressIndexData.TYPE.TABEL , msg));
          Logger.getLogger(CrawlerIndexerThread.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return status;
    }
    
    private void doEmailCrawling() {
        File dbPath = new File(this.aCase.getCaseLocation() + "\\" + FilesPath.EMAIL_DB );
        logger.log(Level.INFO, "Email Indexing in Folder: " +  dbPath);
        EmailIndexer emailIndexer = new EmailIndexer(this.luceneIndex, dbPath, "", new OfficeImageExtractor());
        logger.log(Level.INFO, "Email Indexing Status: " +  emailIndexer.doIndexing());
    }
    
    @Override
    protected void process(List<ProgressIndexData> chunks) {
        if ( isCancelled() )
            return; 
                
        for (ProgressIndexData pd : chunks) {
            if ( pd.getType() == ProgressIndexData.TYPE.TABEL ) {
                Object[] data = { FileUtil.getExtension(pd.getPath()), pd.getPath(), pd.getStatus()};
                JTableUtil.addRowToJTable(this.parentDialog.getLoggingTable(), data);
                this.numberOfFilesCannotIndexed++;
                this.parentDialog.setNumberOfFilesError(String.valueOf(this.numberOfFilesCannotIndexed));
            }
            else {
                // set gui panel to reflect that this is file object
                FileSystemCrawlingProgressPanel panel = new FileSystemCrawlingProgressPanel();
                panel.setCurrentFile(pd.getPath());
                panel.setFileSize(SizeUtil.getSize(pd.getPath()));
                panel.setFileExtension(FileUtil.getExtension(pd.getPath()));
                
                this.parentDialog.changeProgressPanel(panel);
                this.parentDialog.setNumberOfFiles(String.valueOf(pd.getIndexCount()));
                this.parentDialog.setBigSizeLabel(pd.getSizeMsg());
            }
        }
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
                        this.aCase.getCaseName(), new Date().toString(), true, this.numberOfFilesIndexed, 
                        this.sizeOfFilesInEvidenceFolder);

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
             Logger.getLogger(CrawlerIndexerThread.class.getName()).log(Level.SEVERE, null, e);
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
        this.parentDialog.clearFields();
    }
    
    public void stopIndexingThread() throws IOException {
        this.cancel(true);
    }
    
    private void closeIndex () throws IOException {
        this.luceneIndex.closeIndex();
    }
    
    private void checkForThreadCancelling() throws CancellationException{
        if ( this.isCancelled() )
            throw new CancellationException("Cralwer is Cancelled by stop button");
    }
    
    static final class ProgressIndexData {
        enum TYPE {LABEL, TABEL}
        
        final String path;
        final long progressCount ;
        final long indexCount ;
        final String status ;
        final TYPE type ;
        final String sizeMsg ;

        public ProgressIndexData (long progressCount, long indexCount, String p,
                String status, TYPE type, String sm) {
            this.progressCount = progressCount;
            this.path = p ;
            this.indexCount = indexCount ;
            this.status = status ;
            this.type = type;
            this.sizeMsg = sm;
        }

        public String getPath ()        { return this.path   ; }
        public long getProgressCount ()  { return this.progressCount  ; }
        public String getStatus()       { return this.status ; }
        public TYPE getType ()           { return this.type   ; }
        public long getIndexCount()      { return this.indexCount ;}
        public String getSizeMsg()      { return this.sizeMsg; }
    }
}
