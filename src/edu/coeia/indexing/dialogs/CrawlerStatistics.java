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