/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import org.apache.lucene.index.IndexWriter;

/**
 * Abstract Class for defining Index object
 * @author wajdyessam
 */

public abstract class Index {
    public abstract boolean doIndexing (IndexWriter writer);
}
