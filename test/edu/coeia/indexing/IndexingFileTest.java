/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.cases.Case;

import org.junit.Before; 
import org.junit.Ignore ;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File ;

/**
 *
 * @author wajdyessam
 */

public class IndexingFileTest extends CaseBaseSource{
    
    private Case fackCaseObject ;
    
    @Before
    public void init() {
        initForIndexFactoryTest();
    }
    
    @Test
    public void indexZipFileTest() {
        try {  
            LuceneIndexer luceneIndexer = LuceneIndexer.getInstance(fackCaseObject, true);
            
            File file = new File(zipFiles.get(0));
            
            boolean state = LuceneIndexer.indexFile(file);
            luceneIndexer.closeIndex();
            
            assertEquals(state, true);
        }
        catch(Exception e) { e.printStackTrace(); }
    }
    
    @Test
    public void indexTxtFileTest() {
        try {
            LuceneIndexer luceneIndexer = LuceneIndexer.getInstance(fackCaseObject, true);
            
            File file = new File(textFiles.get(0));
            
            boolean state = LuceneIndexer.indexFile(file);
            luceneIndexer.closeIndex();
            
            assertEquals(state, true);
            assertEquals(1, luceneIndexer.getIndexNumber());
        }
        catch(Exception e) { e.printStackTrace(); }
    }
    
    @Test
    public void indexDOCXFileTest() {
        try {
            LuceneIndexer luceneIndexer = LuceneIndexer.getInstance(fackCaseObject, true);
            
            File file = new File(docFiles.get(1));
            
            boolean state = LuceneIndexer.indexFile(file);
            luceneIndexer.closeIndex();
            
            assertEquals(state, true);
            assertEquals(67, luceneIndexer.getIndexNumber());
        }
        catch(Exception e) { e.printStackTrace(); }
    }
    
    @Test
    public void indexDOCFileTest1() {
        try {
            LuceneIndexer luceneIndexer = LuceneIndexer.getInstance(fackCaseObject, true);
            
            File file = new File(docFiles.get(0));
            
            boolean state = LuceneIndexer.indexFile(file);
            luceneIndexer.closeIndex();
            
            assertEquals(state, true);
            assertEquals(3, luceneIndexer.getIndexNumber());
        }
        catch(Exception e) { e.printStackTrace(); }
    }
}
