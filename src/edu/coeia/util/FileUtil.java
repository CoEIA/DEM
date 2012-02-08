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
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStreamReader ;
import java.io.FileNotFoundException ;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import java.util.List ;
import java.util.ArrayList ;
import java.util.Arrays;
import java.util.Scanner ;
import java.util.logging.Logger;

/*
 * Noninstantiable utility class
 * contain a collection of methods for maniulation of text files
 */

public final class FileUtil {
    /*
     * Suppress default constructor for noninstantiability
     */
    private FileUtil() {
        throw new AssertionError();
    }

    /**
     * Return the full content of <tt>stream</tt> as <tt>String</tt>
     * 
     * <p>If <tt>stream</tt> has no content or null stream, then return 
     * an empty <tt>String</tt>
     * 
     * @param stream the input stream that hold the data 
     * @return string hold the content of the <tt>stream</tt>
     */
    public static String convertStreamToString(final InputStream stream) {
        StringBuilder result = new StringBuilder();
        
        if ( stream == null )
            return result.toString();
        
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(stream));
            try {
                String line = null;
                while ( (line = input.readLine()) != null ) {
                    result.append(line);
                    result.append("\n");
                }
            }
            finally {
                input.close();
            }
        }
        catch(IOException e) {
            logger.severe(String.format("Cannot Read Input Stream: %s", stream));
        }
        
        return result.toString();
    }
    
    /**
     * Generic Method to write any serializable object to file
     * @param <T> object type, must be implement Serializable interface
     * @param object the object to be written to the file
     * @param file the file path
     * @throws IOException if there are errors in the written process
     */
    public static <T extends Serializable> void  writeObject (T object, File file) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(object);
        out.close();
    }

    /**
     * Generic Method to read any serializable object from file
     * @param <T> the type of object, must be implement serializable interface
     * @param file the file path
     * @return the object to be written
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static <T extends Serializable> T readObject (File file) throws IOException,ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        T object = (T) in.readObject();
        in.close();

        return object; 
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
    
    public static boolean createFile(String filePath) throws IOException {
        filePath = checkNull("filepath can't be null ", filePath);
        filePath = checkNotEmptyString("file path must be not empty", filePath);
        
        File file = new File(filePath);
        return file.createNewFile();
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
    public static void saveObject(InputStream stream, String filename, String destination) 
            throws FileNotFoundException, IOException {
        filename = checkNull("filename can't be null", filename);
        destination = checkNull("destination string can't be null", destination);
        
        filename = checkNotEmptyString("filename must have value", filename);
        destination = checkNotEmptyString("destination must have a value", destination);
        
        String filePath = destination + "\\" + filename;
        saveObject(stream, filePath);
    }
    
    /**
     * Save the bytes in the stream to the specified location
     * 
     * This method will save the stream content to destination path
     * 
     * @param stream contain the stream for the file to be written
     * @param destination the target destination
     */
    public static void saveObject(InputStream stream, String destination) throws FileNotFoundException,
            IOException {
        destination = checkNull("destination string can't be null", destination);
        destination = checkNotEmptyString("destination must have a value", destination);
        
        OutputStream outputStream = null;
        try {
            File file = new File(destination);
            outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length = 0;

            while ( (length = stream.read(buffer)) > 0 ) {
                outputStream.write(buffer, 0, length);
            }
        }
        finally {
            if ( outputStream != null )
                outputStream.close();
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
    
    public static void removeDirectoryContent(final String directoryName) {
        File directory = new File(directoryName);
        
        if ( directory.isDirectory() ) {
            for(File file: directory.listFiles()) {
                if ( file.isDirectory() )
                    removeDirectoryContent(file.getAbsolutePath());
                else {
                    file.delete();
                }
            }
        }
    }

    /**
     * remove file from system
     * @param filePath the path to the selected file
     * @return true if removed successfully
     */
    public static boolean removeFile(final String filePath) {
        return new File(filePath).delete();
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

        input.close();
        return content.toString();
    }

    public static String getFileContentWithSpace (File file) throws FileNotFoundException {
        Scanner input = new Scanner(file);
        StringBuilder content = new StringBuilder("");
        while ( input.hasNext() )
            content.append(input.nextLine()).append( " ");

        input.close();
        return content.toString();
    }

    public static List<String> getFileContentInList (File file) throws FileNotFoundException {
        List<String> aList = new ArrayList<String>();
        Scanner input = new Scanner(file);

        while ( input.hasNext() ) {
            aList.add(input.nextLine());
        }

        input.close();
        return (aList);
    }
    
    public static String getExtension (String f){
        return getExtension(new File(f));
    }   
    
    public static String getExtension (final File file) {        
        int index = file.getName().lastIndexOf(".");
        if ( index < 0 )
            return "";

        String ext = file.getName().substring(index+1);
        return (ext);
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
     * get all the files in directory after applying the file filter
     * @param directory the directory we want all the files inside it
     * @param fileFilter the filter to be applied
     * @return list of all Files inside directory
     */
    public static List<File> getFilesInDirectory(final String directory, final FileFilter fileFilter) {
        List<File> files = new ArrayList<File>();
        
        File file = new File(directory);
        files.addAll(Arrays.asList(file.listFiles(fileFilter)));
        
        return files;
    }
    
    private final static Logger logger = ApplicationLogging.getLogger();
}
