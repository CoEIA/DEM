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
            assertEquals(123, Utilities.getFileContent(new File("C:\\test.txt")).charAt(0));
        }
        catch(Exception e) {
            
        }
    }
}
