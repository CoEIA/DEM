/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.util;

/**
 *
 * @author wajdyessam
 */

import java.io.File ;
import java.text.DecimalFormat;

public class SizeUtil {
    
    /*
     * Suppress default constructor for noninstantiability
     */
    private SizeUtil() {
        throw new AssertionError();
    }
    
    public static String getSize (String path) {
        long fileSize = new File(path).length() ;
        String resultString = "";

        if ( fileSize > 1073741824L) {
            long tmp = (long) toGB(fileSize);
            resultString = tmp + " GB";
        }
        else if ( fileSize > 1048576 ) {
            long tmp = (long) toMB(fileSize);
            resultString = tmp + " MB";
        }
        else if ( fileSize > 1024 ) {
            long tmp = (long) toKB(fileSize);
            resultString = tmp + " KB";
        }
        else {
             resultString = fileSize + " Bytes" ;
        }
        
        return (resultString);
    }
        
    public static double toKB (double n) {
        return n/1024 ;
    }

    public static double toMB (double n) {
        return toKB(n)/ 1024 ;
    }

    public static double toGB (double n) {
        return toMB(n) / 1024 ;
    }

    public static double fromMBtoByte(double mb) {
        return mb * 1024 * 1024 ;
    }
    
    public static String formatSize (double number) {
        return new DecimalFormat("##.##").format(number) ;
    }
}
