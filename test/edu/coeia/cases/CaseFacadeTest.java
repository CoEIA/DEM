/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author wajdyessam
 */
public class CaseFacadeTest {
    
    public CaseFacadeTest() {
    }

    @Test
    public void createCaseTest() throws Exception {
        List<String> list = new ArrayList<String>();
        list.add("C:\\Documents and Settings\\wajdyessam\\Desktop\\DEM\\DEM_2");
        
        Case case1 = new Case.CaseBuilder("wajdyTest", "C:\\te", "wajdy", "this is test", list, new Date())
                .indexArchiveFiles(true)
                .indexEmbeddedDocuments(true)
                .computeHashForEveryItem(true)
                .detectDuplicationWithHashLibrary(true)
                .detectDuplicationWithinCase(true)
                .getCacheImages(true)
                .getCheckEmbedded(true)
                .getCheckCompressed(true)
                .build();
        
        CaseFacade caseFacade = CaseFacade.newCase(case1);
        caseFacade.removeCase();
    }
}
