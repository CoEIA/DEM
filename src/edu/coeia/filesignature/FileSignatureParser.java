 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.filesignature;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Ahmed
 */

final class FileSignatureParser {
   
    public static List<FileSignature> paserFile() throws IOException {
        BufferedReader br = null;
        List<FileSignature> files = new ArrayList<FileSignature>();
        
        try {
            br = new BufferedReader(new FileReader("tools\\Signatures.dat"));
            String line = null;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",N/A");
                String[] firstPart = values[0].split(",");
                String[] secondPart = values[1].split(",");
                List<String> extensionPart = Arrays.asList(secondPart[1].split("\\|"));

                FileSignature fs = new FileSignature(firstPart[0], firstPart[1].replaceAll("\\ ", ""), extensionPart, secondPart[2]);
                files.add(fs);
            }
        }
        finally {
            if (br != null) {
                br.close();
            }
        }

        return files;
    }
}
