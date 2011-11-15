/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.util;

/**
 *
 * @author wajdyessam
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingWorker;
import javax.swing.JDialog ;
import javax.swing.JFrame; 

import java.util.List;
import java.util.ArrayList ;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public final class HashVerifier {
    private final String hashValue ;
    private final String sourcePath ;
    private final JFrame parent ;
    
    private ProgressDialog progressDialog ;
    
    /**
     * Create new instance of hash verifier that can execute thread for hash computing
     * @param parent
     * @param hash
     * @param path
     * @return 
     */
    public static HashVerifier newInstance (final JFrame parent, final String hash, final String path) {
        return new HashVerifier(parent, hash, path);
    }
    
    /**
     * Start Hash Verifier thread to verify the evidence path hash
     */
    public void start() {
        new HashVeriferThread().execute();
        
        this.progressDialog = new ProgressDialog(this.parent, true);
        this.progressDialog.setVisible(true);
    }
    
    private HashVerifier (final JFrame parent, final String hash, final String path) {  
        this.hashValue = hash;
        this.sourcePath = path;
        this.parent = parent ;
    }
    
    private class HashVeriferThread extends SwingWorker<String, HashProgress> {
        @Override
        public String doInBackground() {
            System.out.println("start hash thread");
            
            return "";
        }
        
        
        @Override
        public void process(List<HashProgress> data) {
            
        }
        
        @Override
        public void done() {
            System.out.println("done hash thread");
            progressDialog.setVisible(false);
        }
    }
    
    private class HashProgress {
    }
    
    private class ProgressDialog extends JDialog {
        private final JLabel fileLabel, hashLabel;
        private final JProgressBar progressBar ;
        
        public ProgressDialog(JFrame owner, boolean model) {
            super(owner, model);
            configDialog(owner);
            
            fileLabel = new JLabel("");
            hashLabel = new JLabel("");
            
            progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            
            setComponents();
            this.pack();
        }
        
        private void configDialog(JFrame owner) {
            this.setTitle("Hash Verifier Window");
            this.setSize(400, 200);
            this.setLocationRelativeTo(owner);
            this.setLayout(new BorderLayout());
        }
        
        private void setComponents() {
            JPanel northPanel = new JPanel();
            northPanel.setLayout(new GridLayout(2, 2));
            northPanel.add(new JLabel("File:"));
            northPanel.add(fileLabel);
            northPanel.add(new JLabel("Hash:"));
            northPanel.add(hashLabel);
            this.add(northPanel, BorderLayout.NORTH);
            
            JPanel centerPanel = new JPanel();
            centerPanel.add(progressBar);
            this.add(centerPanel, BorderLayout.CENTER);
            
            JPanel southPanel = new JPanel();
            JButton button = new JButton("Stop");
            button.addActionListener(new ActionListener() {
                public void actionPerformed (ActionEvent event){ 
                    System.out.println("Stopped");
                }
            });
            
            southPanel.add(button);
            this.add(southPanel, BorderLayout.SOUTH);
        }
    }
}
