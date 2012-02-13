/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */


import edu.coeia.constants.IndexingConstant;
import org.junit.Test ;
import static org.junit.Assert.* ;

public class IndexingConstantTest {

    @Test
    public void testDocumentType() {
        assertEquals("FILE", IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_GENERAL_TYPE.FILE));
    }
    
    @Test
    public void testDocumentType2() {
        assertEquals("CHAT", IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_GENERAL_TYPE.CHAT));
    }
    
    @Test
    public void testDocumentTypeFromString() {
        assertEquals(IndexingConstant.DOCUMENT_GENERAL_TYPE.CHAT, IndexingConstant.fromStringToDocumentGeneralType("CHAT"));
    }
}
