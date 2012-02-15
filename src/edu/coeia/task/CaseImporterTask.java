/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.cases.Case;
import edu.coeia.managers.ApplicationManager;
import edu.coeia.cases.CaseFacade;
import edu.coeia.main.CaseManagerFrame;
import edu.coeia.constants.ApplicationConstants;
import edu.coeia.util.GUIFileFilter;
import edu.coeia.util.ZipUtil;

import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author wajdyessam
 */
public class CaseImporterTask implements Task{
    private final ProgressDialog dialog ;
    private final CaseManagerFrame frame;
    private File file;
    
    public CaseImporterTask(final CaseManagerFrame frame) {
        this.dialog = new ProgressDialog(null, true, this);
        this.frame = frame;
    }
    
    @Override
    public void startTask() {
        final GUIFileFilter SWING_DEM_FILTER = new GUIFileFilter("DEM CASE", 
            ApplicationConstants.CASE_EXPORT_EXTENSION);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(SWING_DEM_FILTER);

        int result = fileChooser.showOpenDialog(null);
        if ( result == JFileChooser.APPROVE_OPTION ) {
            file = fileChooser.getSelectedFile();
            this.dialog.startThread();
        }
    }
    
    @Override
    public void doTask() {
        try {
            importCaseAction();
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(CaseImporterTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.dialog.isCancelledThread();
    }
    
    private void importCaseAction() throws Exception{
        ZipUtil zipper = new ZipUtil(this);
        String fileNameWithOutExt = file.getName().toString().replaceFirst("[.][^.]+$", "");
        String destPath = ApplicationManager.Manager.getCasesPath() + File.separator + fileNameWithOutExt;
        zipper.decompress(file.getAbsolutePath(), destPath);

        File filePath = new File(destPath).listFiles()[0];
        String path = filePath.listFiles()[0].getAbsolutePath();

        String line = fileNameWithOutExt + " - " + path;
        Case aCase = ApplicationManager.Manager.getCase(line);
        aCase.setCaseLocation(path);
        
        String prefLocation = aCase.getCaseLocation() + File.separator +  ApplicationConstants.CASE_PREFERENCE_EXTENSION;
        CaseFacade caseManger = CaseFacade.newInstance(aCase);
        caseManger.updateCase(aCase.getCaseName(), path);
        caseManger.importHistory(prefLocation);
        
        this.frame.readCases();
    }
}
