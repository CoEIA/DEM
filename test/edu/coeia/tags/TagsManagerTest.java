/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tags;

import edu.coeia.util.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore ;
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
        assertEquals(0, tm.readTags().size());
    }
    
    @Test
    public void createTagsManagerTest2() {
        assertEquals(0, tm.readTags().size());
        
        tm.addTag(Tag.newInstance("Wajdy Essam", new Date(), "This is my first Comment"));
        tm.addTag(Tag.newInstance("Ahmed Ali", new Date(2154545), "this is ahmed ali comments"));
        
        assertEquals(2, tm.readTags().size());
    }
    
    @Test
    public void createTagsManagerTest3() {
        tm.addTag(Tag.newInstance("name", new Date(), "this testing comments"));
        tm.addTag(Tag.newInstance("testing", new Date(), "this is testing"));
        
        tm.removeTag(1);
        assertEquals(1, tm.readTags().size());
    }
    
//    @Test
//    public void createTagsManagerTest34() {
//        tm.addTag(Tag.newInstance("name", new Date(), "this testing comments"));
//        tm.addTag(Tag.newInstance("testing", new Date(), "this is testing"));
//        
//        tm.removeTag(1);
//        assertEquals(1, tm.readTags().size());
//        
//        tm.closeManager();
//        tm = TagsManager.getTagsManager(location);
//        assertEquals(1, tm.readTags().size());
//    }
}
