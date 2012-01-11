/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory ;
import org.apache.lucene.util.Version ;

import edu.coeia.cases.Case;
import edu.coeia.util.FilesPath;

import java.io.FileNotFoundException ;
import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.StopAnalyzer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Test;

/**
 *
 * @author Ahmed
 */
public class EmailIndexerTest {
    
     @Test
     public void TestEmails() throws CorruptIndexException, LockObtainFailedException, IOException
      
    {
//         Directory dir = null;
//         IndexWriter writer_ = null;
//         
//         try {
//            dir = FSDirectory.open(new File("C:\\Indexer"));
//        } catch (IOException ex) {
//            Logger.getLogger(EmailIndexer.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//            writer_ = new IndexWriter(dir,
//                new StandardAnalyzer(
//                Version.LUCENE_21),
//                true,
//                IndexWriter.MaxFieldLength.UNLIMITED);
//      
//            EmailIndexer ei = new EmailIndexer(writer_, new File("C:\\Secure_DB"), null, true, null, null);
//        
//        ei.doIndexing();
       
        

    }
}
