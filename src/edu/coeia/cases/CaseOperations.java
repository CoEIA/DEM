/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

import edu.coeia.main.CaseManagerFrame;
import edu.coeia.managers.ApplicationManager;

import edu.coeia.util.FileUtil;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author wajdyessam
 */
public class CaseOperations {
    
    private final String caseName ;
    private final JFrame parentFrame;
    
    private CaseOperationDialog caseOperationDialog;
    private SwingWorker<Boolean, CaseOperationProgress> caseOperationThread;
    
    public CaseOperations(final JFrame frame, final String caseName) {
        this.caseName = caseName;
        this.parentFrame = frame;
    }
    
    public void start() {
        this.caseOperationThread = new CaseOperationThread();
        this.caseOperationThread.execute();
        
        this.caseOperationDialog = new CaseOperationDialog(parentFrame, true, this);
        this.caseOperationDialog.getProgressBar().setIndeterminate(true);
        this.caseOperationDialog.setVisible(true);
    }
    
    void stop() {
        this.caseOperationThread.cancel(true);
    }
    
    private final class CaseOperationThread extends SwingWorker<Boolean, CaseOperationProgress> {
        @Override
        public Boolean doInBackground() {
            boolean removeCase = false;
            
            try {
                CaseFacade caseFacade = ApplicationManager.Manager.openCase(caseName);
                caseFacade.closeCase();
                
                // remove all file
                removeCase = removeDirectory(new File(caseFacade.getCaseFolderLocation()));
            } catch (Exception ex) {
                Logger.getLogger(CaseOperations.class.getName()).log(Level.SEVERE, null, ex);
            }

            return removeCase;
        }
        
        private boolean removeDirectory (File dirPath) {
            if ( dirPath.isDirectory() ) {
                File[] files = dirPath.listFiles() ;

                for(File file: files ) {
                    publish(new CaseOperationProgress(file.getAbsolutePath()));
                    
                    if ( file.isDirectory() )
                        removeDirectory(file);
                    else {
                       file.delete() ;
                    }
                }
            }

            return dirPath.delete() ;
        }
        
        @Override
        public void process(final List<CaseOperationProgress> items) {
            if ( isCancelled() )
                return;
            
            for(CaseOperationProgress item: items) {
                CaseOperations.this.caseOperationDialog.addLine(item.fileName);
            }
        }
        
        @Override
        public void done() {
            try {
                Boolean result = get();
                
                if ( result ) {
                    JOptionPane.showMessageDialog(parentFrame, "Finishing Task Seccesfully");
                }
                else {
                    JOptionPane.showMessageDialog(parentFrame, "The task is canclled by the user");
                }
            } 
            catch (InterruptedException ex) {
                Logger.getLogger(CaseOperations.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (ExecutionException ex) {
                Logger.getLogger(CaseOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally {
                caseOperationDialog.dispose();
                ((CaseManagerFrame)parentFrame).readCases();
                
                return;
            }
            
        }
    }
    
    private final class CaseOperationProgress {
        String fileName;
        
        public CaseOperationProgress(final String name) {
            this.fileName = name;
        }
    }
}
