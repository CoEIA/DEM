/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.hash;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.util.FileUtil;
import edu.coeia.util.Utilities;
import static edu.coeia.util.PreconditionsChecker.* ;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.security.MessageDigest ;
import java.security.NoSuchAlgorithmException ;
import java.security.DigestInputStream;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MD5 Hash Calculator for files and directories
 * return the hash representation as uppercase hex string
 * @author wajdyessam
 */
public class HashCalculator {
    
    /**
     * Calculate MD5 hash for <tt>path</tt> file
     * @param path should contain valid file 
     * @return hash value for the path
     */
    public static String calculateFileHash (final String path){
        checkNull("path must be not null", path);
        checkNotEmptyString("path must be not empty", path);
        
        if ( !FileUtil.isFileFound(path) )
            throw new IllegalArgumentException("Path pointer to not valid file");
        
        return MD5HashCalculator(path);
    }

    /**
     * Calculate Hash Value for directory
     */
    final static class DirectoryHash {
        private final MessageDigest digest ;
        
        public static DirectoryHash newInstance() {
            try {
                return new DirectoryHash();
            }
            catch(NoSuchAlgorithmException e ) { }
            
            throw new NullPointerException("cannot make md5 algorithm");
        }
        
        private DirectoryHash() throws NoSuchAlgorithmException{
            digest = MessageDigest.getInstance("MD5");
        }
        
        public void addFile(final String path) {
            //digest.update(FileUtil.getFileBytes(path));
            DigestInputStream digestInputStream = null;
            try {
                digestInputStream = new DigestInputStream(new FileInputStream(path), digest);
                byte[] buffer = new byte[8192];
                while( digestInputStream.read(buffer) != -1 );
            } catch (Exception ex) {
                Logger.getLogger(HashCalculator.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    digestInputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(HashCalculator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        public String done() {
            return (Utilities.toHex(digest.digest()));
        }
    }
    
    /**
     * get MD5 hash value for path file
     * @param path the path to the file
     * @return hex uppercase string hash value
     */
    private static String MD5HashCalculator(final String path) {
        assert path != null;
        
        StringBuilder digestString = new StringBuilder();
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            //md.update(FileUtil.getFileBytes(path));
            DigestInputStream digestInputStream = new DigestInputStream(new FileInputStream(path), md);
            byte[] buffer = new byte[8192];
            while( digestInputStream.read(buffer) != -1 );
            digestString.append(Utilities.toHex(md.digest()));
            digestInputStream.close();
        }
        catch(FileNotFoundException e) {
            
        }
        catch(NoSuchAlgorithmException e) {
            
        }
        catch(IOException e) {
            
        }
        
        return digestString.toString();
    }
}
