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
import edu.coeia.util.ApplicationLogging;
import edu.coeia.extractors.OfficeImageExtractor;
import edu.coeia.indexing.dialogs.FileSystemCrawlingProgressPanel;
import edu.coeia.indexing.dialogs.IndexingDialog;
import edu.coeia.util.DateUtil;

import javax.swing.JOptionPane;

import java.io.File ;
import java.io.IOException ;

import java.util.Date ;
import java.util.List ;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class IndexingService {
    
    private final Case aCase ;
    private final IndexerManager indexerManager ;
    private final IndexingDialog parentDialog ;
    private final CaseFacade caseFacade ;
    private final CrawlerStatistics crawlerStatistics;
    
    private final static Logger logger = ApplicationLogging.getLogger();

    private final static int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    
    private final ExecutorService crawlerService = Executors.newSingleThreadExecutor();
    private final ExecutorService indexerService = Executors.newFixedThreadPool(NUMBER_OF_THREADS * 2);
    
    public IndexingService (final IndexingDialog parentDialog) throws IOException{
        this.caseFacade = parentDialog.getCaseFacade();
        this.aCase = this.caseFacade.getCase();
        this.parentDialog = parentDialog;
        this.indexerManager = IndexerManager.newInstance(this.caseFacade);
        this.crawlerStatistics = new CrawlerStatistics();
        this.addListeners();
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
        
    public void startService() throws Exception{
        if ( !crawlerService.isShutdown() ) {
            try {
                checkForRemovingOldStatus();
                
                // write evidence location information on index
                this.writeEvidenceLocation(this.aCase.getEvidenceSourceLocation());
            
                FilesCrawler crawler = new FilesCrawler(this.aCase.getEvidenceSourceLocation(),
                        indexerService, indexerManager);
                crawlerService.submit(crawler);
                
                // email crawler
                if ( !aCase.getEmailConfigurations().isEmpty()) {
                    this.doEmailCrawling();
                }
            }
            catch(RejectedExecutionException e) {
                logger.severe("Task Submission Rejected");
            }
        }

        crawlerService.shutdown();
        crawlerService.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
        logger.info("End Crawling Service");
        
        indexerService.shutdown();
        indexerService.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
        logger.info("End Indexing Service Service");
        
        this.updateHistory(true, "time");
    }
    
    
    private void doEmailCrawling() throws CancellationException{
        File dbPath = new File(this.caseFacade.getCaseOnlineDatabaseLocation());
        
        logger.log(Level.INFO, String.format("Crawling Email In: %s", dbPath));
        
        OnlineEmailIndexer emailIndexer = new OnlineEmailIndexer(this.indexerManager, dbPath, "",
                new OfficeImageExtractor());
        
        logger.log(Level.INFO, String.format("Email Indexing Status: %s", emailIndexer.doIndexing()));
    }
    
    public void stopService() throws Exception {
        crawlerService.shutdownNow();
        indexerService.shutdownNow();
    }
    
    private void writeEvidenceLocation(final List<String> paths) throws IOException{
        this.indexerManager.writeEvidenceLocation(paths);
    }
        
    private void addListeners() {
        this.indexerManager.addIndexerListener(
            new IndexerListener() {
                @Override
                public void indexerStarted(final IndexerEvent event) {
                    //System.out.println("index: " + event.getPath());
                }
                
                @Override
                public void indexerCompleted(final IndexerEvent event) {
                    boolean result = event.getResult();
                    if ( result )
                        crawlerStatistics.increaseNumberOfIndexedItems();
                    else
                        crawlerStatistics.increaseNumberOfErrorItems();

                    //System.out.println("[" + crawlerStatistics.getNumberOfIndexedItems() + "] index: " + event.getPath());
                    updateGUI();
                    updateIndexGUI(event.getPath());
                }
            }
        );
        
        this.indexerManager.addScanningListener(
            new ScanningListener() {
                @Override
                public void scanningStarted(ScanningEvent event) {
                    crawlerStatistics.incraseNumberOfScannedItems();
                    crawlerStatistics.increaseSizeOfScannedItems(event.getSize());
                    //System.out.println("[" + crawlerStatistics.getNumberOfScannedItems() + "] scanning: " + event.getPath());
                    updateGUI();
                }
            }
        );
    }
    
    public void updateHistory(final boolean flag, final String time) {
        new Thread( 
            new Runnable() { 
               @Override
               public void run() {
                try {
                    saveHistory();
                    closeIndex();
                    showMessage(flag, time);
                    closeDialog();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
               }
            }
        ).start();
    }
    
    private void showMessage(boolean flag, final String endTime) {
        if ( flag ) 
            JOptionPane.showMessageDialog(this.parentDialog,
                String.format("Indexing Completed Successfully"),
                "Indexing Process Is Completed",
                JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(this.parentDialog,
                "Indexing Process Stopped","Indexing Process Is Not Completed",
                JOptionPane.ERROR_MESSAGE);
    }
    
    private void saveHistory() {
        CaseHistory history = CaseHistory.newInstance(
            this.aCase.getCaseName(), new Date().toString(), true, 
            this.crawlerStatistics.getNumberOfIndexedItems(), 
            this.crawlerStatistics.getNumberOfScannedItems());

        this.caseFacade.setCaseHistory(history);
    }
     
    private void updateGUI() {
        parentDialog.updateStatus(crawlerStatistics);
    }
    
    private void updateIndexGUI(final String path) {
        File file = new File(path);
        FileSystemCrawlingProgressPanel.FileSystemCrawlerData data = new FileSystemCrawlingProgressPanel.FileSystemCrawlerData(
                path,
                String.valueOf(file.length()),
                FileUtil.getExtension(path),
                DateUtil.formatedDateWithTime(new Date(file.lastModified())),
                null);
        
        parentDialog.showFileSystemPanel(data);
    }
    
    public void closeIndex () throws IOException {
        this.indexerManager.closeIndex();
    }
        
    public void closeDialog() {
        this.parentDialog.clearFields();
        this.parentDialog.hideIndexingDialog();
    }
}
