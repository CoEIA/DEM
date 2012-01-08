/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.extractors.ImageExtractor;
import edu.coeia.onlinemail.OnlineEmailMessage;
import edu.coeia.util.FilesPath;
import edu.coeia.onlinemail.OnlineEmailDBHandler;
import edu.coeia.hash.HashCalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.List;

import com.pff.PSTException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;

import org.apache.tika.exception.TikaException;

final class OutlookIndexer extends Indexer{
    
   public OutlookIndexer(LuceneIndex luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor) {
        super(luceneIndex, file, mimeType, imageExtractor);
    }
   
   @Override
   public boolean doIndexing() {
       return false;
   }
}
