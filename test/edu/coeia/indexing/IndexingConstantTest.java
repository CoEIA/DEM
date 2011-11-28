/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */


import org.junit.Test ;
import static org.junit.Assert.* ;

public class IndexingConstantTest {

    @Test
    public void testDocumentType() {
        assertEquals("FILE", IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.FILE));
    }
    
    @Test
    public void testDocumentType2() {
        assertEquals("CHAT", IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.CHAT));
    }
    
    @Test
    public void testDocumentTypeFromString() {
        assertEquals(IndexingConstant.DOCUMENT_TYPE.CHAT, IndexingConstant.getDocumentType("CHAT"));
    }
}
