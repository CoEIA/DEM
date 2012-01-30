/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.cases.Case;
import edu.coeia.cases.CaseFacade;
import edu.coeia.util.FilesPath;
import edu.coeia.util.ZipUtil;

import java.io.File;

import javax.swing.JFileChooser;

/**
 *
 * @author wajdyessam
 */
public class CaseExporterTask implements Task{
    private final Case aCase;
    private final TaskThread thread;
    private File file ;
    
    public CaseExporterTask(final Case aCase) {
        this.aCase = aCase;
        this.thread = new TaskThread(this);
    }
    
    @Override
    public void startTask() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(aCase.getCaseName() + FilesPath.DEM_CASE_EXTENSION) );

        int result = fileChooser.showSaveDialog(null);
        if ( result == JFileChooser.APPROVE_OPTION ) {
            this.file = fileChooser.getSelectedFile();
            this.thread.execute();
        }
    }
    
    @Override
    public void doTask() throws Exception {
        exportCaseAction();
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.thread.isCancelledThread();
    }
        
    private void exportCaseAction() throws Exception { 
        String caseName = this.file.getAbsolutePath();

        String prefLocation = this.aCase.getCaseLocation() + File.separator +  FilesPath.DEM_CASE_PREFERENCE;
        CaseFacade caseManager = CaseFacade.newInstance(aCase);
        caseManager.exportHistory(this.aCase.getCaseName(), prefLocation);

        ZipUtil zipper = new ZipUtil(this);
        zipper.compress(this.aCase.getCaseLocation(), caseName);
    }
}
