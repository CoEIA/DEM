/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

/**
 *
 * @author wajdyessam
 */

import org.junit.Test ;
import static org.junit.Assert.* ;

import java.util.Date ;

public class CaseHistoryHandlerTest {
    
    @Test
    public void handleCaseHistoryTest() {
        CaseHistoryHandler.CaseHistory history = CaseHistoryHandler.CaseHistory.newInstance(
                "test1", new Date().toString(), true, 100, 9000);
        
        CaseHistoryHandler.set(history);
        
        CaseHistoryHandler.CaseHistory history2 = CaseHistoryHandler.CaseHistory.newInstance(
                "test2", new Date().toString(), true, 100, 9000);
        
        CaseHistoryHandler.set(history2);
        
        CaseHistoryHandler.CaseHistory history3 = CaseHistoryHandler.get(history.getCaseName());
        assertEquals(history.getCaseName(), history3.getCaseName());
        
        assertEquals(true, CaseHistoryHandler.remove(history2.getCaseName()));
        assertEquals(true, CaseHistoryHandler.remove(history.getCaseName()));
    }
    
    
    @Test(expected=NullPointerException.class)
    public void handleNullCaseHistoryNameTest() {
        CaseHistoryHandler.get(null);
    }
    
    @Test
    public void handleRemoveNotExistsCaseHistoryNameTest() {
        assertEquals(false, CaseHistoryHandler.remove("this is invalid case history name"));
    }
 
}
