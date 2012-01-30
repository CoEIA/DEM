/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.cases.Case;
import edu.coeia.managers.ApplicationManager;
import edu.coeia.cases.CaseFacade;
import edu.coeia.main.CaseManagerFrame;
import edu.coeia.main.UpdatingCaseEvidenceSourceDialog;
import edu.coeia.main.CaseFrame;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
 *
 * @author wajdyessam
 */
public class CaseLoaderTask implements Task {
    private final TaskThread thread;
    private final CaseManagerFrame frame;
    private final String caseName ;
    private final boolean startIndex;
    private final CaseFacade caseFacade ;
    
    public CaseLoaderTask(final CaseManagerFrame frame,
            final CaseFacade caseFacade, final boolean indexNow) {
        
        this.thread = new TaskThread(this);
        this.frame = frame;
        this.caseFacade = caseFacade;
        this.caseName = caseFacade.getCase().getCaseName();
        this.startIndex = indexNow;
    }
    
    @Override
    public void startTask() {
        this.thread.execute();
    }
    
    @Override
    public void doTask() throws Exception {
        this.loadCase();
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.thread.isCancelledThread();
    }
    
    private void loadCase () throws FileNotFoundException, IOException, ClassNotFoundException, Exception{
        if ( caseName != null ) {
            if ( !ApplicationManager.Manager.isRunningCase(caseName)) {
                Case aCase = ApplicationManager.Manager.getCaseFromCaseName(caseName);

                // check here for case evience chnaging
                // and update the file before opening the case
                boolean caseSourceIsUptoDate = true;
                
                CaseFacade caseFacade = CaseFacade.newInstance(aCase);
        
                if ( caseFacade.isCaseHaveChangedSource() )  {
                    caseSourceIsUptoDate = askAndUpdateNewCaseSource(caseFacade);
                }
                
                if ( caseSourceIsUptoDate ) {                    
                    ApplicationManager.Manager.addCase(caseName);

                    CaseFrame mainFrame = new CaseFrame(this.caseFacade, ApplicationManager.Manager.getList());
                    mainFrame.setLocationRelativeTo(this.frame);
                    mainFrame.setVisible(true);
                    
                    if ( ! caseFacade.getCaseHistory().getIsCaseIndexed() ) {
                        mainFrame.showIndexDialog(startIndex);
                    }
                }
                else {
                    System.out.println("case folder in changed, and you should provide the correct path");
                }
            }
            else {
                JOptionPane.showMessageDialog(this.frame, "This case is already opening",
                        "Already Openining Case", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private boolean askAndUpdateNewCaseSource(final CaseFacade caseFacade) throws IOException {
        UpdatingCaseEvidenceSourceDialog dialog = new UpdatingCaseEvidenceSourceDialog(this.frame, true, caseFacade);
        dialog.setVisible(true);
        
        return dialog.getResult();
    }
}
