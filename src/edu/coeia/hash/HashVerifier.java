/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.hash;

import java.io.File;
import javax.swing.SwingWorker;
import javax.swing.JFrame; 

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;

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
    private final String sourceHashValue ;
    private final String sourcePath ;
    private final JFrame parent ;
    
    private HashVerifierDialog progressDialog ;
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
        
        this.progressDialog = new HashVerifierDialog(this.parent, true, this);
        this.progressDialog.getProgressBar().setIndeterminate(true);
        this.progressDialog.setVisible(true);
    }
    
    private HashVerifier (final JFrame parent, final String hash, final String path) {  
        this.sourceHashValue = hash;
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
                    HashVerifier.this.progressDialog.setCurrentFile(item.getName());
                    HashVerifier.this.progressDialog.setCurrentHash(item.getHash());
                }
            }
        }
        
        @Override
        public void done() {
            try {
                String result = get();
                
                // clean dialog
                progressDialog.setCurrentFile("");
                progressDialog.setCurrentHash("");
                progressDialog.getProgressBar().setIndeterminate(false);
                
                if ( result.equalsIgnoreCase(sourceHashValue)) {
                    progressDialog.setResultHash("Hash Are Equal: " + result);
                    JOptionPane.showMessageDialog(parent, "Hash Are Equal: " + result);
                }
                else {
                    progressDialog.setResultHash("Hash Are Not Equal: " + result);
                    JOptionPane.showMessageDialog(parent, "Hash Are Not Equal: " + result);
                }
            }
            catch(InterruptedException e) {} 
            catch(CancellationException e) {

            }
            catch(ExecutionException e) {
                e.printStackTrace();
            }
            finally {
                progressDialog.dispose();
                return;
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
    
    public String getSourceHash()   { return this.sourceHashValue ; }
    public void stopThread()        { hashVerifierThread.cancel(true); }
}
