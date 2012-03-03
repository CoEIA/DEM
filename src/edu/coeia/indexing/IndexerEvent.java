/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

public final class IndexerEvent {
    private final String path;
    private final boolean result;

    public IndexerEvent(final String path) {
        this.path = path;
        this.result = false;
    }

    public IndexerEvent(final String path, final boolean result) {
        this.path = path;
        this.result = result;
    }

    public String getPath() {
        return this.path;
    }

    public boolean getResult() {
        return this.result;
    }
}
