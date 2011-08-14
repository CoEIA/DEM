/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.filesystem.parse;

/**
 *
 * @author wajdyessam
 */

import java.io.InputStream ;

import org.apache.lucene.document.Document ;

public interface TypeParser {
    public Document parseDocument ( InputStream filePath) throws Exception ;
}
