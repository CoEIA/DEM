/* 
 * Copyright (C) 2014 Center of Excellence in Information Assurance
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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