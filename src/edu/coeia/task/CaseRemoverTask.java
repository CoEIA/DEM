/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.cases.Case;
import edu.coeia.managers.ApplicationManager;
import edu.coeia.cases.CaseFacade;
import edu.coeia.main.CaseManagerFrame;

/**
 *
 * @author wajdyessam
 */
public class CaseRemoverTask implements Task{
    private final TaskThread thread;
    private final CaseManagerFrame frame;
    private final String caseName ;
    
    public CaseRemoverTask(final CaseManagerFrame frame, final String caseName) {
        this.thread = new TaskThread(this);
        this.frame = frame;
        this.caseName = caseName;
    }
    
    @Override
    public void startTask() {
        this.thread.execute();
    }
    
    @Override
    public void doTask() throws Exception {
        removeCaseAction();
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.thread.isCancelledThread();
    }
  
    private void removeCaseAction() throws Exception{
        Case aCase = ApplicationManager.Manager.getCaseFromCaseName(caseName);
        CaseFacade caseManager = CaseFacade.newInstance(aCase);
        boolean status = caseManager.removeCase();
        this.frame.readCases(); // update view table
    }
}
