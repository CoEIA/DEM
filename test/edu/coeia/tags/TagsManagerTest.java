/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tags;

import edu.coeia.util.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date; 

/**
 *
 * @author wajdyessam
 */
public class TagsManagerTest {

    private TagsManager tm;
    private String location = "F:\\tags" ;
    
    @Before
    public void setUp() throws Exception {
        tm = TagsManager.getTagsManager(location);
    }

    @After
    public void tearDown() throws Exception {
        tm.closeManager();
        FileUtil.removeDirectory(location);
    }
    
    @Test
    public void createTagsManagerTest() {
        assertEquals(0, tm.getTags().size());
    }
    
    @Test
    public void createTagsManagerTest2() {
        assertEquals(0, tm.getTags().size());
        
        tm.addTag(Tag.newInstance("Wajdy Essam", new Date(), "This is my first Comment"));
        tm.addTag(Tag.newInstance("Ahmed Ali", new Date(2154545), "this is ahmed ali comments"));
        
        assertEquals(2, tm.getTags().size());
    }
    
    @Test
    public void createTagsManagerTest3() {
        tm.addTag(Tag.newInstance("name", new Date(), "this testing comments"));
        tm.addTag(Tag.newInstance("testing", new Date(), "this is testing"));
        
        tm.removeTag(1);
        assertEquals(1, tm.getTags().size());
    }
    
    @Test
    public void createTagsManagerTest4() {
        tm.addTag(Tag.newInstance("name", new Date(), "this testing comments"));
        tm.addTag(Tag.newInstance("testing", new Date(), "this is testing"));
        
        // remove one
        tm.removeTag(1);
        assertEquals(1, tm.getTags().size());
        
        // close without saving value
        tm.closeManager();
        tm = TagsManager.getTagsManager(location);
        assertEquals(0, tm.getTags().size());
        
        // writes tags
        tm.addTag(Tag.newInstance("testing", new Date(), "this is testing"));
        tm.addTag(Tag.newInstance("another testing", new Date(), "this is another testing"));
        tm.saveTags();
        assertEquals(2, tm.getTags().size());
        tm.closeManager();
        
        // open again
        tm = TagsManager.getTagsManager(location);
        assertEquals(2, tm.getTags().size());
    }
    
    @Test(expected=NullPointerException.class)
    public void testNullLocation() {
        tm = TagsManager.getTagsManager(null);
    }
    
    @Test(expected=IllegalArgumentException.class) 
    public void testEmptyLocation() {
        tm = TagsManager.getTagsManager("");
    }
    
    @Test
    public void testTagEquality() {
        Date date = new Date();
        Tag tag1 = Tag.newInstance("ahmed", date, "test");
        Tag tag2 = Tag.newInstance("ahmed", date, "test");
        
        assertEquals(tag1, tag2);
    }
    
    @Test
    public void testTagNonEquality() {
        Date date = new Date();
        Tag tag1 = Tag.newInstance("wajdy", date, "test");
        Tag tag2 = Tag.newInstance("ahmed", date, "test");
        
        assertTrue(! tag1.equals(tag2));
    }
    
    
    @Test
    public void testNotChangedTags() {
        assertEquals(false, tm.isTagsDbModified());
    }
    
    @Test
    public void testChangedTags() {
        tm.addTag(Tag.newInstance("Testing", new Date(), "this is simple message"));
        assertEquals(true, tm.isTagsDbModified());
    }
}
