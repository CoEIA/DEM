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
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader ;
import java.io.FileNotFoundException ;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.util.List ;
import java.util.ArrayList ;
import java.util.Scanner ;
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
    public static void saveObject(InputStream stream, String filename, String destination) throws IOException {
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
    
    public static boolean removeDirectory (String path) {
        return removeDirectory(new File(path));
    }
    
    public static boolean removeDirectory (File dirPath) {
        if ( dirPath.isDirectory() ) {
            File[] files = dirPath.listFiles() ;

            for(File file: files ) {
                if ( file.isDirectory() )
                    removeDirectory(file);
                else {
                    boolean status = file.delete() ;

                    if ( ! status )
                        return false ;
                }
            }
        }

        return dirPath.delete() ;
    }

    public static void writeToFile (List<String> data, String fileName) 
        throws FileNotFoundException, UnsupportedEncodingException {
        
        File file = new File(fileName);
        PrintWriter writer = new PrintWriter(file,"UTF-8");

        for(String line: data) {
            writer.println(line );
        }

        writer.close();
    }
    
    public static String getFileContent (File file) throws FileNotFoundException {
        Scanner input = new Scanner(file);
        StringBuilder content = new StringBuilder("");
        while ( input.hasNext() )
            content.append( input.nextLine() );

        return content.toString();
    }

    public static String getFileContentWithSpace (File file) throws FileNotFoundException {
        Scanner input = new Scanner(file);
        StringBuilder content = new StringBuilder("");
        while ( input.hasNext() )
            content.append(input.nextLine()).append( " ");

        return content.toString();
    }

    public static List<String> getFileContentInArrayList (File file) throws FileNotFoundException {
        List<String> aList = new ArrayList<String>();
        Scanner input = new Scanner(file);

        while ( input.hasNext() ) {
            aList.add(input.nextLine());
        }

        return (aList);
    }    
    
    public static String getExtension (File f) {
        if ( !f.exists() || f.isDirectory() ) {
            return null ;
        }
        
        int index = f.getAbsolutePath().lastIndexOf(".");
        
        if ((index < 0) && (index >= f.toString().length()))
            return null ;

        String ext = f.toString().substring(index+1);

        return (ext);
    }

    public static String getExtension (String f){
        return getExtension(new File(f));
    }    
    
    public static boolean isDirectoryExists(String Path) {
        File file = new File(Path);
        boolean isExist = false;

        if (file.exists()) {
            if (file.isDirectory()) {
                isExist = true;
            }
        }

        return isExist;
    }
    
    /**
     * check if the file in the path is exist in the system
     * and its a valid file object
     * @param path
     * @return 
     */
    public static boolean isFileFound(final String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }
    
    /**
     * read bytes from file and get it in array of bytes
     * @param path the file path
     * @return an array with the bytes in the file
     */
    public static byte[] getFileBytes(final String path) {
        assert path != null;
        
        File file = new File(path);
        InputStream in = null; 
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int ch;
        
        try {
            in = new FileInputStream(file);
            
            while ( (ch=in.read()) != -1 )
                buffer.write(ch);
        }
        catch(IOException e) {
        }
        finally {
            try {
                in.close();
            }
            catch(IOException e) {
                
            }
        }
        
        return buffer.toByteArray();
    }
}
