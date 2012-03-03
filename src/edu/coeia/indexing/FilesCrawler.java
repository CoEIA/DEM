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

/**
 *
 * @author wajdyessam
 */

final class FilesCrawler implements Runnable{
    private final List<String> tasks;
    private final ExecutorService consumerService;
    private final ExecutorService producerService;
    private final IndexerManager indexerManager;

    private long numberOfFiles; 
    private long sizeOfFiles;

    private final FileFilter fileFilter = new FileFilter() { 
        @Override
        public boolean accept(File file) { return true; }
    };

    public FilesCrawler(final List<String> tasks, final ExecutorService consumerService, 
            final ExecutorService producerService, final IndexerManager luceneInexer) {
        this.tasks = tasks;
        this.consumerService = consumerService;
        this.producerService = producerService;
        this.indexerManager = luceneInexer;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        for (String task: tasks) 
            crawl(new File(task));

        try {
            consumerService.shutdown();
            boolean isTerm = consumerService.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
            long end = System.currentTimeMillis();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            try {
                indexerManager.closeIndex();
            }
            catch(Exception e) { 
                e.printStackTrace();
            }
        }
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
                        this.numberOfFiles++;
                        this.sizeOfFiles += entry.length();

                        FileIndexer indexer = new FileIndexer(entry, indexerManager);
                        consumerService.submit(indexer);
                    }
                }
            }
        }
    }
}
