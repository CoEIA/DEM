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
    
    private final FileFilter fileFilter = new FileFilter() { 
        @Override
        public boolean accept(File file) { return true; }
    };

    public FilesCrawler(final List<String> tasks, final ExecutorService consumerService
            , final IndexerManager luceneInexer) {
        this.tasks = tasks;
        this.indexerService = consumerService;
        this.indexerManager = luceneInexer;
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
            else {
                System.out.println("interrupted now");
            }
        }
    }
}
