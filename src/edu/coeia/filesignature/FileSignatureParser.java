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
import java.util.List;

/**
 *
 * @author Ahmed
 */
public class FileSignatureParser {
    

    public static List<FileSignature> ParseFile() throws IOException {

        BufferedReader br = null;
        List<FileSignature> files = new ArrayList<FileSignature>();
        try {

            br = new BufferedReader(new FileReader("tools\\Signatures.dat"));
            String line = null;

            while ((line = br.readLine()) != null) {

                String[] values = line.split(",N/A");
                String[] firstPart = values[0].split(",");
                String[] secondPart = values[1].split(",");
                String[] extensionPart = null;
                extensionPart = secondPart[1].split("\\|");

                FileSignature fs = new FileSignature(firstPart[0], firstPart[1].replaceAll("\\ ", ""), extensionPart, secondPart[2]);
                files.add(fs);

                // System.out.println(fs.toString());
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return files;
    }
}
