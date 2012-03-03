/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import java.io.File;

import java.util.concurrent.Callable;

/**
 *
 * @author wajdyessam
 */

final class FileIndexer implements Callable<Void>{
    private final File task;
    private final IndexerManager indexerManager;

    public FileIndexer(final File task, final IndexerManager indexer) {
        this.task = task;
        this.indexerManager = indexer;
    }

    @Override
    public Void call() {
        if ( !Thread.currentThread().isInterrupted()  ) {
            indexFile(this.task);
        }

        return null;
    }

    private void indexFile(final File file) {
        try {
            this.indexerManager.indexFile(file, null);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
