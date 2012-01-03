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
import edu.coeia.util.FilesPath;
import org.junit.Test;
import static org.junit.Assert.* ;
import org.junit.Before ;

import java.io.File;
import java.util.List;

public class CasePathHandlerTest {
    @Test
    public void testCasePathWhenCreatingCaseWithOneSource() throws Exception{
        String casePath = "C:\\out";
        CasePathHandler pathHandler = CasePathHandler.newInstance(casePath);
        pathHandler.add(new File("C:\\data"));
        pathHandler.saveConfiguration();
        assertEquals(1, pathHandler.readConfiguration().size());
        FileUtil.removeFile(casePath + File.separator + FilesPath.CASE_CONFIG);
    }
    
    @Test
    public void testCasePathWhenCreatingCaseWithMultiSource() throws Exception{
        String casePath = "C:\\out";
        CasePathHandler pathHandler = CasePathHandler.newInstance(casePath);
        pathHandler.add(new File("C:\\data"));
        pathHandler.add(new File("C:\\docs"));
        pathHandler.saveConfiguration();
        assertEquals(2, pathHandler.readConfiguration().size());
        FileUtil.removeFile(casePath + File.separator + FilesPath.CASE_CONFIG);
    }
    
    @Test
    public void testDetectionOneSourceIsNotExisting() throws Exception{
        String casePath = "C:\\out";
        CasePathHandler pathHandler = CasePathHandler.newInstance(casePath);
        pathHandler.add(new File("C:\\notexistingfolder"));
        pathHandler.saveConfiguration();
        assertEquals(1, pathHandler.getChangedEntries().size());
        FileUtil.removeFile(casePath + File.separator + FilesPath.CASE_CONFIG);
    }
    
    @Test
    public void testDetectionMultiSourceIsNotExisting() throws Exception{
        String casePath = "C:\\out";
        CasePathHandler pathHandler = CasePathHandler.newInstance(casePath);
        pathHandler.add(new File("C:\\notexistingfolder"));
        pathHandler.add(new File("C:\\data"));
        pathHandler.add(new File("C:\\notexistingfolder2"));
        pathHandler.add(new File("C:\\notexistingfolder3"));
        pathHandler.add(new File("C:\\docs"));
        pathHandler.saveConfiguration();
        assertEquals(3, pathHandler.getChangedEntries().size());
        FileUtil.removeFile(casePath + File.separator + FilesPath.CASE_CONFIG);
    }
        
    @Test
    public void testUpdatingCaseConfigurationFile() {
        
    }
}
