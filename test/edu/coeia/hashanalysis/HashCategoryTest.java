/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.hashanalysis;

import org.junit.Test ;
import static org.junit.Assert.*;

import java.util.Date ;

/**
 *
 * @author wajdyessam
 */

public class HashCategoryTest {
    @Test
    public void addCategoryTest() {
        HashItem item1 = HashItem.newInstance("a.doc","C:\\a.doc","test","F:\\test", 
                "Wajdy", new Date(), "DFWERWEWER");
        
        HashCategory hashCategory = new HashCategory("name","this is simple testing");
        
        assertFalse(hashCategory.isContain(item1));
        assertTrue(hashCategory.addItem(item1));
        assertTrue(hashCategory.isContain(item1));
    }
}
