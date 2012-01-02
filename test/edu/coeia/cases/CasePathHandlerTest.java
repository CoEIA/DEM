/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.util.FileUtil;
import org.junit.Test;
import static org.junit.Assert.* ;
import org.junit.Before ;

import java.io.File;
import java.util.List;

public class CasePathHandlerTest {
    private File path1, path2;
    
    @Before 
    public void init() {
        path1 = new File("C:\\data");
        path2 = new File("C:\\docs");
    }
    
    @Test
    public void testAddingPath() {
        CasePathHandler pathHandler = new CasePathHandler();
        pathHandler.add(path1);
        pathHandler.add(path2);
        
        assertEquals(2, pathHandler.getEntries().size());
    }
    
    @Test
    public void testSavingCasePath() throws Exception{
        CasePathHandler pathHandler = new CasePathHandler();
        pathHandler.add(path1);
        pathHandler.add(path2);
        pathHandler.save("C:\\file.txt");
        FileUtil.removeFile("C:\\file.txt");
    }
    
    @Test
    public void testReadCasePath() throws Exception {
        CasePathHandler pathHandler = new CasePathHandler();
        pathHandler.add(path1);
        pathHandler.add(path2);
        pathHandler.save("C:\\file.txt");
        
        pathHandler.read("C:\\file.txt");
        List<CasePathHandler.PathEntry> entries = pathHandler.getEntries();
        assertEquals(2, entries.size());
        
        assertEquals(path1.getAbsolutePath(), entries.get(0).path);
        assertEquals(path2.getAbsolutePath(), entries.get(1).path);
        
        FileUtil.removeFile("C:\\file.txt");
    }
    
    @Test
    public void testConvertingFullPathToRelativePath() {
        CasePathHandler pathHandler = new CasePathHandler();
        pathHandler.add(path1);
        pathHandler.add(path2);
        
        File filePath = new File("C:\\data\\eng_case\\daily_read.txt");
        String expected =  String.format(CasePathHandler.prefix, 0) + File.separator + filePath.getName();
        assertEquals(expected, pathHandler.getRelativePath(filePath));
    }
        
    @Test
    public void testConvertingFullPathToRelativePath2() {
        CasePathHandler pathHandler = new CasePathHandler();
        pathHandler.add(path1);
        pathHandler.add(path2);
        
        File filePath = new File("C:\\docs\\index.html");
        String expected =  String.format(CasePathHandler.prefix, 1) + File.separator + filePath.getName();
        assertEquals(expected, pathHandler.getRelativePath(filePath));
    }
    
    @Test
    public void testConvertRelativePathToFullPath() {
        CasePathHandler pathHandler = new CasePathHandler();
        pathHandler.add(path1);
        pathHandler.add(path2);
        
        String path = "@PATH_0@\\eng_case\\daily_read.txt";
        String expected = "C:\\data\\eng_case\\daily_read.txt";
        
        assertEquals(expected, pathHandler.getFullPath(path));
    }
    
    @Test
    public void testIfSourceIsChanged() {
        
    }
    
    @Test
    public void testUpdateCaseSource() {
        
    }
}
