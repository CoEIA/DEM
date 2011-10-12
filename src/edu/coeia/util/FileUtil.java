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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
    
    private FileUtil() {
        throw new AssertionError();
    }
    
    /*
     * Save Object
     * @param stream contain input data
     * @param filename contain the name of the file and extension
     * @parem destination contain the full path of the file
     */
    public static void saveObject(InputStream stream, String filename, String destination) {
        try {
            String filePath = destination + "\\" + filename;
            File file = new File(filePath);

            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length = 0;

            while ( (length = stream.read(buffer)) > 0 ) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
        }
        catch (IOException e) {
        }
    }
}
