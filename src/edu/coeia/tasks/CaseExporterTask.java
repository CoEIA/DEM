/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tasks;

import edu.coeia.cases.Case;
import edu.coeia.cases.CaseFacade;
import edu.coeia.constants.ApplicationConstants;
import edu.coeia.util.ZipUtil;

import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

/**
 *
 * @author wajdyessam
 */
public class CaseExporterTask implements Task{
    private final Case aCase;
    private final BackgroundProgressDialog dialog ;
    private File file ;
    
    public CaseExporterTask(final Case aCase) {
        this.aCase = aCase;
        this.dialog = new BackgroundProgressDialog(null, true, this);
    }
    
    @Override
    public void startTask() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(aCase.getCaseName() + ApplicationConstants.CASE_EXPORT_EXTENSION) );

        int result = fileChooser.showSaveDialog(null);
        if ( result == JFileChooser.APPROVE_OPTION ) {
            this.file = fileChooser.getSelectedFile();
            this.dialog.startThread();
        }
    }
    
    @Override
    public void doTask() {
        try {
            exportCaseAction();
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(CaseExporterTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.dialog.isCancelledThread();
    }
        
    private void exportCaseAction() throws Exception { 
        String caseName = this.file.getAbsolutePath();

        CaseFacade caseFacade = CaseFacade.newInstance(aCase);
        caseFacade.closeCaseAuditing();
        caseFacade.closeCaseTags();
        
        caseFacade.exportHistory(this.aCase.getCaseName(), caseFacade.getCasePreferenceFileLocation());

        ZipUtil zipper = new ZipUtil(this);
        zipper.compress(this.aCase.getCaseLocation(), caseName);
    }
}
