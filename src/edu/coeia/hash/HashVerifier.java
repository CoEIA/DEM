/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.hash;

import edu.coeia.hash.HashCalculator;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.SwingWorker;
import javax.swing.JDialog ;
import javax.swing.JFrame; 

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Hash Verifier will check if the current hash value submitted by user is
 * equal to the value of the folder <tt>path</tt> that also submitted to constructor
 * its then will run background thread to do the hash value calculation
 * and in the same time it will display progress bar dialog that show the 
 * current file under processing
 * 
 * @author wajdyessam
 */
public final class HashVerifier {
    private final String hashValue ;
    private final String sourcePath ;
    private final JFrame parent ;
    
    private ProgressDialog progressDialog ;
    private SwingWorker<String, HashProgress> hashVerifierThread; 
    
    /**
     * Create new instance of hash verifier that can execute thread for hash computing
     * it will compute the hash of path folder and compare it with hash value that caller want to verify
     * @param parent parent for the dialog that will show progress bar
     * @param hash the hash value that we want to verify
     * @param path the path of evidence that we will compute its has
     * @return 
     */
    public static HashVerifier newInstance (final JFrame parent, final String hash, final String path) {
        return new HashVerifier(parent, hash, path);
    }
    
    /**
     * Start Hash Verifier thread to verify the evidence path hash
     */
    public void start() {
        hashVerifierThread = new HashVeriferThread();
        hashVerifierThread.execute();
        
        this.progressDialog = new ProgressDialog(this.parent, true);
        this.progressDialog.setVisible(true);
    }
    
    private HashVerifier (final JFrame parent, final String hash, final String path) {  
        this.hashValue = hash;
        this.sourcePath = path;
        this.parent = parent ;
    }
    
    /**
     * Swing Worker Thread
     * used to update progress dialog when calculate directory hash value
     */
    private class HashVeriferThread extends SwingWorker<String, HashProgress> {
        private HashCalculator.DirectoryHash dirHash = HashCalculator.DirectoryHash.newInstance();
        
        @Override
        public String doInBackground() {
            calculateDirectoryHash(HashVerifier.this.sourcePath);
            return dirHash.done();
        }
        
        @Override
        public void process(List<HashProgress> data) {
            if ( isCancelled() ) {
                return; 
            }
            else {
                for(HashProgress item: data) {
                    HashVerifier.this.progressDialog.setFileLabel(item.getName());
                    HashVerifier.this.progressDialog.setHashLabel(item.getHash());
                }
            }
        }
        
        @Override
        public void done() {
            try {
                String result = get();
                System.out.println("Result: " + result);
                
                if ( result.equalsIgnoreCase(hashValue)) {
                    System.out.println("equal hash value");
                }
                else
                    System.out.println("not equal hash value");
                
                // clean dialog
                progressDialog.fileLabel.setText("");
                progressDialog.hashLabel.setText("");
                progressDialog.progressBar.setIndeterminate(false);
            }
            catch(InterruptedException e) {} 
            catch(CancellationException e) {
                progressDialog.setVisible(false);
                return;
            }
            catch(ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        private void calculateDirectoryHash(final String path) {
            if ( this.isCancelled() )
                throw new CancellationException("SwingWorker Cancelled by cancel button");
                            
            if ( new File(path).isDirectory()) {
                File[] files = new File(path).listFiles();
                
                for(File file: files) {
                    if ( this.isCancelled() )
                        throw new CancellationException("SwingWorker Cancelled by cancel button");
                                
                    if ( file.isDirectory()) {
                        calculateDirectoryHash(file.getAbsolutePath());
                    }
                    else if ( file.isFile() ) {
                        String filePath = file.getAbsolutePath();
                        
                        // compute hash for file
                        String hash = HashCalculator.calculateFileHash(filePath);
                        
                        // add hash to dir hash value
                        dirHash.addFile(filePath);
                        
                        // update gui elements
                        HashProgress chunks = new HashProgress(filePath, hash);
                        this.publish(chunks);
                    }
                }
            }
        }
    }
    
    /**
     * contain data that is passed from SwingWorker thread to process method
     */
    private final class HashProgress {
        private final String name, hash;
        
        public HashProgress(final String name, final String hash) {
            this.name = name;
            this.hash = hash;
        }
        
        public String getName() { return this.name ; }
        public String getHash() { return this.hash; }
    }
    
    /**
     * Progress GUI Dialog
     */
    private final class ProgressDialog extends JDialog {
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
                    hashVerifierThread.cancel(true);
                }
            });
            
            southPanel.add(button);
            this.add(southPanel, BorderLayout.SOUTH);
        }
        
        void setFileLabel(final String text) {
            this.fileLabel.setText(text);
        }
        
        void setHashLabel(final String text) {
            this.hashLabel.setText(text);
        }
    }
}
