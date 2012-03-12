/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing.dialogs;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author wajdyessam
 */
public class CrawlerStatistics {
    private AtomicLong sizeOfScannedItems  = new AtomicLong(0);
    private AtomicLong numberOfScannedItems = new AtomicLong(0);
    private AtomicLong numberOfItemsIndexed = new AtomicLong(0);
    private AtomicLong numberOfItemsCannotIndexed = new AtomicLong(0);

    public void increaseSizeOfScannedItems(final long value) { this.sizeOfScannedItems.getAndAdd(value) ; }
    public void incraseNumberOfScannedItems() { this.numberOfScannedItems.incrementAndGet(); }
    public void increaseNumberOfIndexedItems() { this.numberOfItemsIndexed.incrementAndGet(); }
    public void increaseNumberOfErrorItems() { this.numberOfItemsCannotIndexed.incrementAndGet(); }

    public long getSizeOfScannedItems() { return this.sizeOfScannedItems.get(); }
    public long getNumberOfScannedItems() { return this.numberOfScannedItems.get();}
    public long getNumberOfIndexedItems() { return this.numberOfItemsIndexed.get();}
    public long getNumberOfErrorItems() { return this.numberOfItemsCannotIndexed.get();}  
}