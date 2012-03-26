/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tasks;

import edu.coeia.cases.Case;
import edu.coeia.managers.ApplicationManager;
import edu.coeia.cases.CaseFacade;
import edu.coeia.main.CaseManagerFrame;
import edu.coeia.constants.ApplicationConstants;
import edu.coeia.util.GUIFileFilter;
import edu.coeia.util.ZipUtil;

import java.awt.EventQueue;
import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author wajdyessam
 */
public class CaseImporterTask implements Task{
    private final BackgroundProgressDialog dialog ;
    private final CaseManagerFrame frame;
    private File file;
    
    public CaseImporterTask(final CaseManagerFrame frame) {
        this.dialog = new BackgroundProgressDialog(null, true, this);
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
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(frame, "Cannot Importing Case File",
                            "Case file is currept", JOptionPane.ERROR_MESSAGE);
                }
            } );
            
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
        zipper.decompress(file.getAbsolutePath(), ApplicationManager.Manager.getCasesPath() );

        String destPath = ApplicationManager.Manager.getCasesPath() + File.separator + fileNameWithOutExt;
        String line = fileNameWithOutExt + " - " + destPath;
        Case aCase = ApplicationManager.Manager.getCase(line);
        aCase.setCaseLocation(destPath);
        
        String prefLocation = aCase.getCaseLocation() + File.separator +  ApplicationConstants.CASE_PREFERENCE_EXTENSION;
        
        CaseFacade caseFacade = CaseFacade.openCase(aCase);
        
        caseFacade.closeCaseAuditing();
        caseFacade.closeCaseTags();
        
        // updating case pointer after closing db
        // so we know here if db is not found, this is file is currpted so 
        // ignore it and not added in casees list
        caseFacade.updateCase(aCase.getCaseName(), destPath);
        caseFacade.importHistory(prefLocation);
        
        this.frame.readCases();
    }
}
