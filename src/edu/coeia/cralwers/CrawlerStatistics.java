/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cralwers;

/**
 *
 * @author wajdyessam
 */
public class CrawlerStatistics {
    private long sizeOfScannedItems ;
    private long numberOfScannedItems;
    private long numberOfItemsIndexed;
    private long numberOfItemsCannotIndexed;

    public void increaseSizeOfScannedItems(final long value) { this.sizeOfScannedItems += value ; }
    public void incraseNumberOfScannedItems() { this.numberOfScannedItems++; }
    public void increaseNumberOfIndexedItems() { this.numberOfItemsIndexed++; }
    public void increaseNumberOfErrorItems() { this.numberOfItemsCannotIndexed++; }

    public long getSizeOfScannedItems() { return this.sizeOfScannedItems; }
    public long getNumberOfScannedItems() { return this.numberOfScannedItems;}
    public long getNumberOfIndexedItems() { return this.numberOfItemsIndexed;}
    public long getNumberOfErrorItems() { return this.numberOfItemsCannotIndexed;}  
}