/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.hash;

import org.junit.Test; 
import org.junit.Before;
import static org.junit.Assert.*; 
import java.util.Date; 

/**
 *
 * @author wajdyessam
 */
public class HashLibraryManagerTest {
    @Test
    public void testCrateNewHashSet() {
        HashItem item1 = HashItem.newInstance("a.doc","C:\\a.doc","test","F:\\test", 
            "Wajdy", new Date(), "DFWERWEWER");
        
        HashCategory hashCategory = new HashCategory("name");
        hashCategory.addItem(item1);
        
        HashLibraryManager hashManager = new HashLibraryManager();
        
        assertFalse(hashManager.isContain(hashCategory));
        assertTrue(hashManager.add(hashCategory));
    }
    
    @Test
    public void testUpdateExistingHashSet() {
        HashItem item1 = HashItem.newInstance("a.doc","C:\\a.doc","test","F:\\test", 
            "Wajdy", new Date(), "DFWERWEWER");
        HashItem item2 = HashItem.newInstance("b.pdf","", "casename", "casepath", 
                "ahmed", new Date(), "asdfdsa");
        
        HashCategory hashCategory = new HashCategory("name");
        hashCategory.addItem(item1);
        
        HashCategory hashCategory2= new HashCategory("name");
        hashCategory2.addItem(item2);
        
        HashLibraryManager hashManager = new HashLibraryManager();
        hashManager.add(hashCategory);
        hashManager.update(hashCategory2);
    }
    
    @Test
    public void testImportHashSet() {
    }
    
    @Test
    public void testExportHashSet() {
    }
}
