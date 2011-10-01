/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import org.apache.lucene.index.IndexWriter;

/**
 *
 * @author wajdyessam
 */
public class DocumentIndex extends Index{

    @Override
    public boolean doIndexing(IndexWriter writer) {
        return false;
    }
    
}
