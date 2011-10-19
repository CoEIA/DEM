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

import static edu.coeia.util.PreconditionsChecker.checkNull; 
import static edu.coeia.util.PreconditionsChecker.checkNotEmptyString;


/*
 * Noninstantiable utility class
 */
public class FileUtil {
    
    /*
     * Suppress default constructor for noninstantiability
     */
    private FileUtil() {
        throw new AssertionError();
    }
    

    /**
     * create new folder if folderPath is not exists
     * @param folderPath 
     */
    public static void createFolder(String folderPath) {
        folderPath = checkNull("filepath can't be null ", folderPath);
        folderPath = checkNotEmptyString("file path must be not empty", folderPath);
        
        createFolder(new File(folderPath));
    }
    
    /**
     * create new folder if folder is not exists
     * @param folder is path to folder
     */
    public static void createFolder(File folder) {
        folder = checkNull("folder must be not null", folder);
        
        if ( ! folder.exists())
            folder.mkdir();
    }
    
    /*
     * Save the bytes in the stream to the specified location with the file name
     * 
     * This method will save the stream content to some location in hardisk
     * 
     * @param stream the InputStream that hold the bytes; must be not null
     * @param filename the file name of the saved file
     * @param destination the location of the file to be saved
     * @throws NullPointerException if the stream, filename and destination contain null data
     */
    public static void saveObject(InputStream stream, String filename, String destination) {
          filename = checkNull("filename can't be null", filename);
        destination = checkNull("destination string can't be null", destination);
        
        filename = checkNotEmptyString("filename must have value", filename);
        destination = checkNotEmptyString("destination must have a value", destination);
        
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
