/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wajdyessam
 */

final class FilesCrawler implements Runnable{
    private final List<String> tasks;
    private final ExecutorService indexerService;
    private final IndexerManager indexerManager;
    private final IndexingService indexingService;
    
    private final FileFilter fileFilter = new FileFilter() { 
        @Override
        public boolean accept(File file) { return true; }
    };

    public FilesCrawler(final List<String> tasks, final ExecutorService consumerService
            , final IndexerManager luceneInexer, final IndexingService indexingService) {
        this.tasks = tasks;
        this.indexerService = consumerService;
        this.indexerManager = luceneInexer;
        this.indexingService = indexingService;
    }

    @Override
    public void run() {
        for (String task: tasks) 
            crawl(new File(task));
    }

    private void crawl(final File root) {
        File[] entries = root.listFiles(this.fileFilter);

        if ( entries != null ) {
            if ( !Thread.currentThread().isInterrupted() ) {
                for(File entry: entries) {
                    if ( entry.isDirectory() && entry.canRead() )  {
                        this.crawl(entry);
                    }
                    else if ( entry.isFile() && entry.canRead()) {
                        indexerManager.fireScanningStarted(entry.getAbsolutePath(), entry.length());
                        
                        FileIndexer indexer = new FileIndexer(entry, indexerManager);
                        indexerService.submit(indexer);
                    }
                }
            }
        }
    }
}
