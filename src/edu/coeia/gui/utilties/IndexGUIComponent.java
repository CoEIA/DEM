/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.gui.utilties;

/**
 *
 * @author wajdyessam
 *
 */

import javax.swing.* ;

public class IndexGUIComponent {

    public IndexGUIComponent (JProgressBar pb, JTable tbl , JLabel date , JLabel time,
        JLabel currentFile, JLabel sizeOfFile, JLabel numberOfFile, JLabel fileExt, JLabel errorLbl , JLabel msgLbl, JButton btn, JButton sBtn ) {

        progressBar = pb ;
        table = tbl ;
        dateLbl = date ;
        timeLbl = time ;
        currentFileLbl = currentFile ;
        sizeOfFileLbl  = sizeOfFile ;
        numberOfFileLbl = numberOfFile ;
        fileExtensionLbl = fileExt ;
        startIndexingButton = btn ;
        stopIndexingButton = sBtn;
        numberOfErrorFilesLbl = errorLbl ;
        bigSizeMsgLbl = msgLbl ;
    }

    public JLabel currentFileLbl, sizeOfFileLbl, numberOfFileLbl, fileExtensionLbl, numberOfErrorFilesLbl, bigSizeMsgLbl;
    public JProgressBar progressBar;
    public JTable table ;
    public JLabel dateLbl, timeLbl ;
    public JButton startIndexingButton, stopIndexingButton;
}
