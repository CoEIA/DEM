/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.email;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.main.utilties.InfiniteProgressPanel ;

import javax.swing.SwingWorker ;

import com.pff.PSTFile ;
import javax.swing.JOptionPane;

public class EmailReaderThread extends SwingWorker<Integer,Void>{

    private String path ;
    private PSTFile pstFile ;
    private InfiniteProgressPanel panel ;
    private boolean status;

    public EmailReaderThread (String path, PSTFile pstFile, InfiniteProgressPanel i) {
        this.path = path ;
        this.pstFile = pstFile ;
        this.panel = i ;
    }
    
    @Override
    protected Integer doInBackground() throws Exception {
        if ( EmailReader.getInstance(pstFile, path, panel) == null )
            status = false;
        else
            status = true;
        
        return 0;
    }

    @Override
    public void done() {
        if ( status == false) {
            JOptionPane.showMessageDialog(null, "Cannot handling this email", "there is problem with this email type", JOptionPane.ERROR_MESSAGE);
        }
        
        panel.interrupt();
        panel.stop();
    }
}
