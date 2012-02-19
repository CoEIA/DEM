/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tasks;

import edu.coeia.cases.Case;
import edu.coeia.managers.ApplicationManager;
import edu.coeia.cases.CaseFacade;
import edu.coeia.main.CaseManagerFrame;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wajdyessam
 */
public class CaseRemoverTask implements Task{
    private final BackgroundProgressDialog dialog ;
    private final CaseManagerFrame frame;
    private final String caseName ;
    
    public CaseRemoverTask(final CaseManagerFrame frame, final String caseName) {
        this.dialog = new BackgroundProgressDialog(null, true, this);
        this.frame = frame;
        this.caseName = caseName;
    }
    
    @Override
    public void startTask() {
        this.dialog.startThread();
    }
    
    @Override
    public void doTask()  {
        try {
            removeCaseAction();
        } catch (Exception ex) {
            Logger.getLogger(CaseRemoverTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.dialog.isCancelledThread();
    }
  
    private void removeCaseAction() throws Exception{
        Case aCase = ApplicationManager.Manager.getCaseFromCaseName(caseName);
        CaseFacade caseFacade = CaseFacade.newInstance(aCase);
        
        boolean status = caseFacade.removeCase();
        this.frame.readCases(); // update view table
    }
}
