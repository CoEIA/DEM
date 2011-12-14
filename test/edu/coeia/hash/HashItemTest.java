/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.hash;

/**
 *
 * @author wajdyessam
 */

import org.junit.Test ;
import static org.junit.Assert.* ;
import org.junit.Before; 

import java.util.Date ;

public class HashItemTest {
    
    HashItem item1, item1Copy; 
    HashItem item2;
    
    @Before
    public void instaite() {
        item1 = HashItem.newInstance("a.doc","C:\\a.doc","test","F:\\test", 
                "Wajdy", new Date(), "DFWERWEWER");
        
        item1Copy = HashItem.newInstance("a.doc","C:\\a.doc","test","F:\\test", 
                "Wajdy", new Date(), "DFWERWEWER");
        
        item2 = HashItem.newInstance("b.pdf","", "casename", "casepath", "ahmed", new Date(), "asdfdsa");
    }
    
    @Test
    public void testCreatingHashItem() {        
        assertEquals("a.doc", item1.getFileName());
        assertEquals("asdfdsa", item2.getHashValue());
    }
    
    @Test
    public void testHashItemEquality() {
        assertEquals(item1, item1Copy);
    }
}
