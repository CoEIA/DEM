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
import edu.coeia.util.ApplicationConstants;
import org.junit.Test;
import static org.junit.Assert.* ;
import org.junit.Before ;

import java.io.File;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class CasePathHandlerTest {
    @Test
    public void testCasePathWhenCreatingCaseWithOneSource() throws Exception{
        String casePath = "C:\\out";
        CasePathMappingHandler pathHandler = CasePathMappingHandler.newInstance(casePath);
        pathHandler.add(new File("C:\\data"));
        pathHandler.saveConfiguration();
        assertEquals(1, pathHandler.reloadFileMapping().size());
        FileUtil.removeFile(casePath + File.separator + ApplicationConstants.CASE_CONFIG);
    }
    
    @Test
    public void testCasePathWhenCreatingCaseWithMultiSource() throws Exception{
        String casePath = "C:\\out";
        CasePathMappingHandler pathHandler = CasePathMappingHandler.newInstance(casePath);
        pathHandler.add(new File("C:\\data"));
        pathHandler.add(new File("C:\\docs"));
        pathHandler.saveConfiguration();
        assertEquals(2, pathHandler.reloadFileMapping().size());
        FileUtil.removeFile(casePath + File.separator + ApplicationConstants.CASE_CONFIG);
    }
    
    @Test
    public void testDetectionOneSourceIsNotExisting() throws Exception{
        String casePath = "C:\\out";
        CasePathMappingHandler pathHandler = CasePathMappingHandler.newInstance(casePath);
        pathHandler.add(new File("C:\\notexistingfolder"));
        pathHandler.saveConfiguration();
        assertEquals(1, pathHandler.getChangedEntries().size());
        FileUtil.removeFile(casePath + File.separator + ApplicationConstants.CASE_CONFIG);
    }
    
    @Test
    public void testDetectionMultiSourceIsNotExisting() throws Exception{
        String casePath = "C:\\out";
        CasePathMappingHandler pathHandler = CasePathMappingHandler.newInstance(casePath);
        pathHandler.add(new File("C:\\notexistingfolder"));
        pathHandler.add(new File("C:\\data"));
        pathHandler.add(new File("C:\\notexistingfolder2"));
        pathHandler.add(new File("C:\\notexistingfolder3"));
        pathHandler.add(new File("C:\\docs"));
        pathHandler.saveConfiguration();
        assertEquals(3, pathHandler.getChangedEntries().size());
        FileUtil.removeFile(casePath + File.separator + ApplicationConstants.CASE_CONFIG);
    }
    
    @Test
    public void testConvertigFromFullPathToRelativePath() {
        String casePath = "C:\\out";
        CasePathMappingHandler pathHandler = CasePathMappingHandler.newInstance(casePath);
        pathHandler.add(new File(casePath));
        String path = "C:\\out\\4318.txt";
        assertEquals("@PATH_0@\\4318.txt", pathHandler.getRelativePath(path));
    }
    
    @Test
    public void testConvertigFromFullRootPathToRelativePath() {
        String casePath = "C:\\";
        CasePathMappingHandler pathHandler = CasePathMappingHandler.newInstance(casePath);
        pathHandler.add(new File(casePath));
        String path = "C:\\git_shell_ext_debug.txt";
        assertEquals("@PATH_0@\\git_shell_ext_debug.txt", pathHandler.getRelativePath(path));
    }
    
    @Test
    public void testConvertingFromNestedFullPathToRelativePath() {
        String casePath = "C:\\out";
        CasePathMappingHandler pathHandler = CasePathMappingHandler.newInstance(casePath);
        pathHandler.add(new File(casePath));
        String path = "C:\\out\\2 Factor Authentication Task\\Finished_100120_Wajdy_Authentications in Online Banking_R.docx";
        assertEquals("@PATH_0@\\2 Factor Authentication Task\\Finished_100120_Wajdy_Authentications in Online Banking_R.docx", pathHandler.getRelativePath(path));
    }
    
    @Test
    public void testConvertingFromNestedRelativePathToFullPath() {
                String casePath = "C:\\out";
        CasePathMappingHandler pathHandler = CasePathMappingHandler.newInstance(casePath);
        pathHandler.add(new File(casePath));
        String expected = "C:\\out\\2 Factor Authentication Task\\Finished_100120_Wajdy_Authentications in Online Banking_R.docx";
        String relative = "@PATH_0@\\2 Factor Authentication Task\\Finished_100120_Wajdy_Authentications in Online Banking_R.docx";
        
        assertEquals(expected, pathHandler.getFullPath(relative));
    }
}
