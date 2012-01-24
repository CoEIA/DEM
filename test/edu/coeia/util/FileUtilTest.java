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
    public void saveNullStreamTest() throws Exception{
        FileUtil.saveObject(null, "file", "C:\\");
    }
    
    @Test(expected=NullPointerException.class)
    public void saveNullLocation()  throws Exception{
        InputStream stream = new ByteArrayInputStream(new byte[] {123, 43, 53, 34} );
        FileUtil.saveObject(stream, "test.txt", null);
    }
    
    @Test
    public void saveStreamTest()  throws Exception{
        
        InputStream stream = new ByteArrayInputStream(new byte[] {123, 43, 53, 34} );
        FileUtil.saveObject(stream, "test.txt", "C:\\");
        
        try {
            assertEquals(123, FileUtil.getFileContent(new File("C:\\test.txt")).charAt(0));
        }
        catch(Exception e) {
            
        }
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void saveStreamEmptyPathTest()  throws Exception{
        
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
    
    @Test
    public void getNormalFilExtenstion() {
        String fileName = "C:\\code.txt";
        assertEquals("txt", FileUtil.getExtension(fileName));
        
        fileName = "C:\\a\\a.b\\d.3\\file.pdf";
        assertEquals("pdf", FileUtil.getExtension(fileName));
        
        fileName = "C:\\a\\asd\\";
        assertEquals("", FileUtil.getExtension(fileName));
        
        fileName = "C:\\Documents and Settings\\wajdyessam\\Desktop\\DEM\\DEM1\\DEM_Source_Code\\OfflineMining\\.git\\objects\\0e\\a75c8762079b23f296fb5954c8a44f007147af";
        assertEquals("", FileUtil.getExtension(fileName));
    }
}
