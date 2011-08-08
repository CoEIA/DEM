/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.internet;

import java.io.* ;

import java.io.IOException ;

import java.util.Scanner ;
import java.util.ArrayList ;

/**
 *
 * @author wajdyessam
 *
 */

public class IEHandler {

    public ArrayList<String> readProgramOutputFile (String path, String fileName) throws IOException,
    InterruptedException {

        Process process = Runtime.getRuntime().exec(path);
        int ret = process.waitFor();

        if ( ret == 0 ) {
            File file  = new File(fileName);
            File file2 = new File(fileName+".tmp");

            if ( ! file.exists() ) {
                System.out.println("file not found");
                return null ;
            }

            FileInputStream fis = new FileInputStream(file);
            byte[] contents = new byte[fis.available()];
            fis.read(contents, 0, contents.length);

            String asString = new String(contents, "Cp1256");
            byte[] newBytes = asString.getBytes("UTF8");
            FileOutputStream fos = new FileOutputStream(file2);
            fos.write(newBytes);
            fos.close();

            Scanner input = new Scanner( file2 , "UTF8" ) ;
            ArrayList<String> result = new ArrayList<String>();

            while ( input.hasNext() ) {
                result.add( input.nextLine() ) ;
            }

            input.close();
            return (result);
        }

        return null;
    }
    
 

    public static void getFiles (File file, ArrayList<String> data) {
	if ( file.isDirectory() ) {
            File[] files = file.listFiles();

            for (int i=0 ; i<files.length ; i++) {
                getFiles(files[i], data);
            }
	}
	else {
            data.add( file.getAbsolutePath());
	}
    }


}