/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import java.io.File ;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExternalImageExtractor implements ImageExtractor{
    
    @Override
    public void extractImages(File file, String distenationFolder) {
        try {
            extractEmbbeddedImage(new FileInputStream(file), file.getName(), distenationFolder);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExternalImageExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void extractEmbbeddedImage(InputStream stream, String filename, String destination) {
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
