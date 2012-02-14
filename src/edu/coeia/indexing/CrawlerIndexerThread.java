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
import edu.coeia.cases.CaseFacade;
import edu.coeia.cases.CaseHistory;
import edu.coeia.indexing.dialogs.CrawlerStatistics;
import edu.coeia.util.FileUtil;
import edu.coeia.util.SizeUtil;
import edu.coeia.util.ApplicationLogging;
import edu.coeia.extractors.OfficeImageExtractor;
import edu.coeia.indexing.dialogs.IndexingDialog;
import edu.coeia.indexing.dialogs.FileSystemCrawlingProgressPanel;

import javax.swing.SwingWorker ;
import javax.swing.JOptionPane;

import java.io.File ;
import java.io.IOException ;

import java.util.ArrayList;
import java.util.Date ;
import java.util.List ;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

public final class CrawlerIndexerThread extends SwingWorker<String,Void> {

    private boolean indexStatus = false;
    
    private final Case aCase ;
    private final LuceneIndex luceneIndex ;
    private final IndexingDialog parentDialog ;
    private final CaseFacade caseFacade ;
    private final CrawlerStatistics crawlerStatistics;
    
    private final static Logger logger = ApplicationLogging.getLogger();

    public CrawlerIndexerThread (final IndexingDialog parentDialog) throws IOException{
        this.caseFacade = parentDialog.getCaseFacade();
        this.aCase = this.caseFacade.getCase();
        this.parentDialog = parentDialog;
        this.luceneIndex = LuceneIndex.newInstance(this.caseFacade);
        this.crawlerStatistics = new CrawlerStatistics();
    }
    
    @Override
    public String doInBackground() {
        this.checkForRemovingOldStatus();
        long start = new Date().getTime();
        this.indexStatus = startCrawling();
        long end = new Date().getTime();
        long totalTimeOfIndexingProcess = end-start ;
        return String.valueOf(totalTimeOfIndexingProcess);
    }
    
    private void checkForRemovingOldStatus() {
        try {
            FileUtil.removeDirectoryContent(this.caseFacade.getCaseIndexFolderLocation());
            FileUtil.removeDirectoryContent(this.caseFacade.getCaseArchiveOutputFolderLocation());
            FileUtil.removeDirectoryContent(this.caseFacade.getCaseImageFolderLocation());
            FileUtil.removeDirectoryContent(this.caseFacade.getCaseOfflineEmailAttachmentLocation());
            this.caseFacade.updateMappingFile();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
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
            if ( !aCase.getEmailConfigurations().isEmpty()) {
               this.doEmailCrawling();
            }
            
            status = true;
        }
        catch(IOException e){
           logger.log(Level.SEVERE, "Cannot Write Evience path in the index", e);
           status = false;
        }
        catch(CancellationException e) {
            logger.log(Level.INFO, "Indexing Stopped By User");
            status = true;
        }
        
        return status;
    }
    
    private void writeEvidenceLocation(final List<String> paths) throws IOException{
        this.luceneIndex.writeEvidenceLocation(paths);
    }
    
    private void doDirectoryCrawling(final File path) throws CancellationException{
        this.checkForThreadCancelling();
        logger.log(Level.INFO, String.format("Crawling Folder: %s", path.getAbsolutePath()));
        
        if ( path.isDirectory() && path.canRead() ) {
            File[] files = path.listFiles();
            
            if ( files != null ) {
                for(File file: files) {
                    this.checkForThreadCancelling();

                    if ( file.isDirectory() && file.canRead()) {
                        doDirectoryCrawling(file);
                    }
                    else if ( file.isFile() && file.canRead()) {
                        this.updateGUIWithFileStatus(file);
                        this.doFileCrawling(file);
                    }
                }
            }
        }
    }
    
    private void updateGUIWithFileStatus(final File path) {
        this.parentDialog.updateStatus(this.crawlerStatistics);
        
        FileSystemCrawlingProgressPanel.FileSystemCrawlerData data = 
                new FileSystemCrawlingProgressPanel.FileSystemCrawlerData(
            path.getPath(),
            SizeUtil.getSize(path.getPath()),
            FileUtil.getExtension(path.getPath()),
            new Date(new File(path.getPath()).lastModified()).toString(),
            new ArrayList<String>()
        );
        
        this.parentDialog.showFileSystemPanel(data);
    }
    
    private boolean doFileCrawling(final File path) {
        boolean status = false;
        this.crawlerStatistics.incraseNumberOfScannedItems();
        this.crawlerStatistics.increaseSizeOfScannedItems(path.getAbsoluteFile().length());
        
        try {
            status = this.luceneIndex.indexFile(path, this.parentDialog);
            this.crawlerStatistics.increaseNumberOfIndexedItems();
        }
        catch (Exception e) {
            this.crawlerStatistics.increaseNumberOfErrorItems();
            logger.log(Level.SEVERE, String.format("File %s cannot be indexed", path.getAbsolutePath()));
        }
        
        return status;
    }
    
    private void doEmailCrawling() throws CancellationException{
        File dbPath = new File(this.caseFacade.getCaseOnlineDatabaseLocation());
        
        logger.log(Level.INFO, String.format("Crawling Email In: %s", dbPath));
        
        OnlineEmailIndexer emailIndexer = new OnlineEmailIndexer(this.luceneIndex, dbPath, "",
                new OfficeImageExtractor(), this.parentDialog);
        
        logger.log(Level.INFO, String.format("Email Indexing Status: %s", emailIndexer.doIndexing()));
    }

    @Override
    public void done() {
        try {
            logger.log(Level.INFO, "Done Indexing Process");
            
            String endTime = this.get();

            // show message box after finish the indexing process if there is problem
            if ( indexStatus ) 
                JOptionPane.showMessageDialog(this.parentDialog,
                    String.format("Indexing Completed Successfully in %s Millisecond(s)", endTime),
                    "Indexing Process Is Completed",
                    JOptionPane.INFORMATION_MESSAGE);
            
            this.closeDialog();
        }
        catch(InterruptedException e) {
            logger.log(Level.SEVERE, null, e);
        }
        catch(CancellationException e) {
            JOptionPane.showMessageDialog(this.parentDialog,
                    "Indexing Process Stopped","Indexing Process Is Not Completed",
                    JOptionPane.ERROR_MESSAGE);
        }
        catch(ExecutionException e) {
             logger.log(Level.SEVERE, null, e);
        }
        finally {
            updateHistory();
        }
    }

    private void updateHistory() {
        new Thread( 
            new Runnable() { 
               @Override
               public void run() {
                try {
                    saveHistory();
                    closeIndex();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
               }
            }
        ).start();
    }
    
    private void closeDialog() {
        this.parentDialog.clearFields();
        this.parentDialog.hideIndexingDialog();
    }
    
    public void stopIndexingThread() throws IOException {
        this.cancel(true);
    }
    
    private void saveHistory() {
        CaseHistory history = CaseHistory.newInstance(
            this.aCase.getCaseName(), new Date().toString(), true, 
            this.crawlerStatistics.getNumberOfIndexedItems(), 
            this.crawlerStatistics.getNumberOfScannedItems());

        this.caseFacade.setCaseHistory(history);
    }

    private void closeIndex () throws IOException {
        this.luceneIndex.closeIndex();
    }
    
    private void checkForThreadCancelling() throws CancellationException{
        if ( this.isCancelled() )
            throw new CancellationException("Cralwer is Cancelled by stop button");
    }
}
