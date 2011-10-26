/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.util;

/**
 *
 * @author wajdyessam
 */

import static edu.coeia.util.PreconditionsChecker.checkNull; 
import static edu.coeia.util.PreconditionsChecker.checkNotEmptyString;

import java.io.File ;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader ;
import java.io.InputStreamReader ;

import java.util.List ;
import java.util.ArrayList; 

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
    
    /**
     * execute command line utility and read the output stream from it
     * @param path is the path for command line utility
     * @return the result of output stream as list of string
     * @throws IOException if the utility is not found
     * @throws NullPointerException if the path contain null data or empty string
     */
    public static List<String> readProgramOutputStream (String path) throws IOException {
        path = checkNull("path can't be null", path);
        path = checkNotEmptyString("path must have a value", path);
        
	Process process = Runtime.getRuntime().exec(path);
	BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()) );

	String line = null ;
	List<String> result = new ArrayList<String>();
	while ( (line = input.readLine() ) != null ) {
            result.add( line );
	}

	input.close();
	return ( result );
    }
}
