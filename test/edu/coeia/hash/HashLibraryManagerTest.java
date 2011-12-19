/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.hash;

import edu.coeia.hashanalysis.HashLibraryManager;
import edu.coeia.hashanalysis.HashItem;
import edu.coeia.hashanalysis.HashCategory;
import java.util.ArrayList;
import org.junit.Test; 
import static org.junit.Assert.*; 
import java.util.Date; 
import java.util.List;

/**
 *
 * @author wajdyessam
 */
public class HashLibraryManagerTest {
    @Test
    public void testCrateNewHashSet() throws Exception{
        HashItem item1 = HashItem.newInstance("a.doc","C:\\a.doc","test","F:\\test", 
            "Wajdy", new Date(), "DFWERWEWER");
        
        HashCategory hashCategory = new HashCategory("name", "notes");
        hashCategory.addItem(item1);
        
        assertFalse(HashLibraryManager.isHashLibraryContain(hashCategory));
        assertTrue(HashLibraryManager.addHashCategory(hashCategory));
    }
    
    @Test
    public void testUpdateExistingHashSet() throws Exception {
        HashItem item1 = HashItem.newInstance("a.doc","C:\\a.doc","test","F:\\test", 
            "Wajdy", new Date(), "DFWERWEWER");
        HashItem item2 = HashItem.newInstance("b.pdf","", "casename", "casepath", 
                "ahmed", new Date(), "asdfdsa");
        
        HashCategory hashCategory = new HashCategory("name", "another notes");
        hashCategory.addItem(item1);
        
        HashLibraryManager.addHashCategory(hashCategory);
        
        List<HashItem> items = new ArrayList<HashItem>();
        items.add(item2);
        
        HashLibraryManager.update(items, "name");
    }
    
    @Test
    public void testImportHashSet() {
    }
    
    @Test
    public void testExportHashSet() {
    }
}
