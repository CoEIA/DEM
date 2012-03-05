/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */
public class ScanningEvent {
    private final String path;
    private final long size;

    public ScanningEvent(final String path, final long size) {
        this.path = path;
        this.size = size;
    }

    public String getPath() {
        return this.path;
    }

    public long getSize() {
        return this.size;
    }
}
