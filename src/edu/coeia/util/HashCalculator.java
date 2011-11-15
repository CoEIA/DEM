/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.util;

/**
 *
 * @author wajdyessam
 */

import static edu.coeia.util.PreconditionsChecker.* ;

import java.security.MessageDigest ;
import java.security.NoSuchAlgorithmException ;

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
     * Calculate MD5 Hash for entire directory content
     * it work by appending hash value for each files inside directory
     * to a some location then compute this data as hash value for directory
     * @param path
     * @return 
     */
    public static String calculateDirectoryHash(final String path) {
        checkNull("path must be not null", path);
        checkNotEmptyString("path must be not empty", path);
        
        if ( !FileUtil.isDirectoryExists(path) )
            throw new IllegalArgumentException("path should point to valid directory");
        
        return "";
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
            md.update(FileUtil.getFileBytes(path));
            digestString.append(toHex(md.digest()));
        }
        catch(NoSuchAlgorithmException e) {
            
        }
        
        return digestString.toString();
    }
    
    /**
     * return hexadecimal representation of arrays as uppercase string
     * @param bytes
     * @return 
     */
    private static String toHex (final byte[] bytes) {
        assert bytes != null ;
        
        StringBuilder hex = new StringBuilder();
        
        for (int i=0; i<bytes.length; i++) {
            int byte1 = bytes[i] & 0xFF;
            
            if ( byte1 < 0xF )
                hex.append("0");
            
            hex.append(Integer.toHexString(byte1).toUpperCase());
        }
        
        return hex.toString();
    }
}
