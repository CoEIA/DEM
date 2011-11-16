/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.util;


import java.io.ByteArrayInputStream;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.InputStream ;
import java.io.File ;

/**
 *
 * @author wajdyessam
 */
public class FileUtilTest {
    
    @Test(expected=NullPointerException.class)
    public void saveNullStreamTest() {
        FileUtil.saveObject(null, "file", "C:\\");
    }
    
    @Test(expected=NullPointerException.class)
    public void saveNullLocation() {
        InputStream stream = new ByteArrayInputStream(new byte[] {123, 43, 53, 34} );
        FileUtil.saveObject(stream, "test.txt", null);
    }
    
    @Test
    public void saveStreamTest() {
        
        InputStream stream = new ByteArrayInputStream(new byte[] {123, 43, 53, 34} );
        FileUtil.saveObject(stream, "test.txt", "C:\\");
        
        try {
            assertEquals(123, FileUtil.getFileContent(new File("C:\\test.txt")).charAt(0));
        }
        catch(Exception e) {
            
        }
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void saveStreamEmptyPathTest() {
        
        InputStream stream = new ByteArrayInputStream(new byte[] {123, 43, 53, 34} );
        FileUtil.saveObject(stream, "", "C:\\");
        
        try {
            assertEquals(123, FileUtil.getFileContent(new File("C:\\test.txt")).charAt(0));
        }
        catch(Exception e) {
            
        }
    }
    
    @Test
    public void createFolderTest () {
        FileUtil.createFolder("C:\\MyFile");
        assertEquals(true, new File("C:\\MyFile").exists());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void createEmptyFolderStringTest() {
        FileUtil.createFolder("");
        assertEquals(true, new File("C:\\XX").exists());
    }
    
    @Test(expected=NullPointerException.class)
    public void crateNullFolderFileTest() {
        File file = null;
        FileUtil.createFolder(file);
        assertEquals(true, new File("C:\\XX").exists());
    }
    
}
