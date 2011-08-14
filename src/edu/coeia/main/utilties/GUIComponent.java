/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.main.utilties;

/**
 *
 * @author wajdyessam
 */

import javax.swing.* ;

import java.util.List ;

public class GUIComponent {
    public JProgressBar progressBar;
    public JTable table ;
    public JLabel dateLbl, indexLocationLbl, dataIndexedLocation, timeLbl ;
    public List<String> exts;
    public JTree clusterTree, clusterTypeTree;
    
    public GUIComponent (JProgressBar pb, JTable tbl , JLabel date , JLabel index, JLabel data, JLabel time,
            List<String> exts, 
            JTree tree, JTree typeTree ){

        this.exts = exts ;
        progressBar = pb ;
        table = tbl ;
        dateLbl = date ;
        indexLocationLbl = index ;
        dataIndexedLocation = data ;
        timeLbl = time ;
        clusterTree = tree ;
        clusterTypeTree = typeTree; 
    }

    public List<String> getExtension () {
        return (exts);
    }
}
