/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

public interface IndexerListener {
    public void indexerStarted(IndexerEvent event);
    public void indexerCompleted(IndexerEvent event);
}
