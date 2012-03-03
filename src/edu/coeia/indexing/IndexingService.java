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
import edu.coeia.indexing.dialogs.EmailCrawlingProgressPanel;
import edu.coeia.indexing.dialogs.IndexingDialog;
import edu.coeia.indexing.dialogs.FileSystemCrawlingProgressPanel;

import java.awt.EventQueue;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class IndexingService {
    private boolean indexStatus = false;
    
    private final Case aCase ;
    private final IndexerManager indexerManager ;
    private final IndexingDialog parentDialog ;
    private final CaseFacade caseFacade ;
    private final CrawlerStatistics crawlerStatistics;
    
    private final static Logger logger = ApplicationLogging.getLogger();

    private final static int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    private final static ExecutorService crawlerService = Executors.newSingleThreadExecutor();
    private final static ExecutorService indexerService = Executors.newFixedThreadPool(NUMBER_OF_THREADS * 2);
    
    public IndexingService (final IndexingDialog parentDialog) throws IOException{
        this.caseFacade = parentDialog.getCaseFacade();
        this.aCase = this.caseFacade.getCase();
        this.parentDialog = parentDialog;
        this.indexerManager = IndexerManager.newInstance(this.caseFacade);
        this.crawlerStatistics = new CrawlerStatistics();
        this.addListeners();
    }
     
    private void saveHistory() {
        CaseHistory history = CaseHistory.newInstance(
            this.aCase.getCaseName(), new Date().toString(), true, 
            this.crawlerStatistics.getNumberOfIndexedItems(), 
            this.crawlerStatistics.getNumberOfScannedItems());

        this.caseFacade.setCaseHistory(history);
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
                // write evidence location information on index
                this.writeEvidenceLocation(this.aCase.getEvidenceSourceLocation());
            
                FilesCrawler crawler = new FilesCrawler(this.aCase.getEvidenceSourceLocation(),
                        indexerService, crawlerService, indexerManager);
                crawlerService.submit(crawler);
            }
            catch(RejectedExecutionException e) {
                logger.severe("Task Submission Rejected");
            }
        }

        crawlerService.shutdown();
        crawlerService.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
        logger.info("End Crawling Service");
    }
    
    private void writeEvidenceLocation(final List<String> paths) throws IOException{
        this.indexerManager.writeEvidenceLocation(paths);
    }
        
    private void addListeners() {
        this.indexerManager.addListener(
            new IndexerListener() {
                @Override
                public void indexerStarted(final IndexerEvent event) {
                    EventQueue.invokeLater(
                        new Runnable() {
                            @Override
                            public void run() {
                            }
                        }
                    );
                }
                
                @Override
                public void indexerCompleted(final IndexerEvent event) {
                    EventQueue.invokeLater(
                        new Runnable() {
                            @Override
                                public void run() {
                                    System.out.println(event.getResult() + " For: " + event.getPath());
                            }
                        }
                    );
                }
            }
        );
    }
}
