/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.cases.Case;
import edu.coeia.cases.ApplicationManager;
import edu.coeia.cases.CaseManager;
import edu.coeia.cases.CaseManagerFrame;
import edu.coeia.cases.UpdatingCaseEvidenceSourceDialog;
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
    
    public CaseLoaderTask(final CaseManagerFrame frame, final String caseName, final boolean indexNow) {
        this.thread = new TaskThread(this);
        this.frame = frame;
        this.caseName = caseName;
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
                
                CaseManager caseManager = CaseManager.newInstance(aCase);
        
                if ( caseManager.isCaseHaveChangedSource() )  {
                    caseSourceIsUptoDate = askAndUpdateNewCaseSource(caseManager);
                }
                
                if ( caseSourceIsUptoDate ) {                    
                    ApplicationManager.Manager.addCase(caseName);

                    CaseFrame mainFrame = new CaseFrame(aCase, ApplicationManager.Manager.getList());
                    mainFrame.setLocationRelativeTo(this.frame);
                    mainFrame.setVisible(true);
                    
                    if ( ! caseManager.getCaseHistory().getIsCaseIndexed() ) {
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
    
    private boolean askAndUpdateNewCaseSource(final CaseManager caseManager) throws IOException {
        UpdatingCaseEvidenceSourceDialog dialog = new UpdatingCaseEvidenceSourceDialog(this.frame, true, caseManager);
        dialog.setVisible(true);
        
        return dialog.getResult();
    }
}
