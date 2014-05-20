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
        else {
            System.out.println("interrupted");
        }

        return null;
    }

    private void indexFile(final File file) {
        try {
            //System.out.println("index: " + file.getAbsolutePath());
            this.indexerManager.indexFile(file);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
