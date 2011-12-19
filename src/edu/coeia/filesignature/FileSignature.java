/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.filesignature;

import edu.coeia.util.FileUtil;
import edu.coeia.util.Utilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ahmed
 */
public class FileSignature {

    private static final int BUFFER_SIZE = 4096;
    private static final int MAX_SIGNATURE_SIZE = 8;
    private String Id;
    private String Signature;
    private String[] Extension;
    private String Type;
    public String KnownSignature;

    public FileSignature() {
    }

    public FileSignature(String Id, String Signature, String[] Extension, String Type) {

        this.Id = Id;
        this.Signature = Signature;
        this.Extension = Extension;
        this.Type = Type;
    }

    public String getID() {
        return this.Id;
    }

    public String getSignature() {
        return this.Signature;
    }

    public String[] getExtension() {
        return this.Extension;
    }

    public String getType() {
        return this.Type;
    }

   

    public String toString() {

        return Id + "\n" + Signature + "\n" + Extension + "\n" + Type + "\n";
    }


    public static boolean matchesSignature(byte[] signature, File file) throws FileNotFoundException, IOException {

        byte[] buffer = new byte[signature.length];
        InputStream in = new FileInputStream(file);
        int n = in.read(buffer, 0, signature.length);

        if (n < signature.length) {
            return false;
        }
        String hex = Utilities.toHex(buffer);

        boolean b = false;
        String signstring = new String(signature);
        if (hex.startsWith(signstring)) {
            b = true;
        } else {
            b = false;
        }

        return b;
    }

    public static boolean verifyExtenstion(File file, FileSignature db) {
        String extension =  FileUtil.getExtension(file);

        for (String ext : db.getExtension()) {

            if (extension.equalsIgnoreCase(ext)) {
                return true;
            }
        }

        return false;
    }

    // Get the extension of the file and search for matached signature based on
    // on the extension
    public static boolean matchBadSignature(File file, FileSignature fs) throws FileNotFoundException, IOException {

        // First Case, Extension in DB Table, but Signature is different
        // ext  == sign in database 
        boolean res = false;
        res = verifyExtenstion(file, fs);
        boolean b = true;

        boolean result = matchesSignature(fs.getSignature().getBytes(), file);
        if (result == true && res == true) {
            System.out.println("Match File");
            b = true;

        } else {
            b = false;
        }


        return b;
    }

    public static boolean matchAliasSignature(File file, FileSignature db) throws FileNotFoundException, IOException {

        String extension = FileUtil.getExtension(file);
        

        boolean result = matchBadSignature(file, db);

        for (String ext : db.getExtension()) {

            if ((!extension.equalsIgnoreCase(ext)) && result == true) {
                return false;
            }
        }

        return true;
    }

    public static boolean isknownFile(File file, FileSignature db) throws FileNotFoundException, IOException {

        boolean b = true;
        if (searchSignature(file, db).isEmpty()) {
            b = false;
        } else {
            b = true;
        }

        return b;
    }

    // Get Signature of the file and search for it in the database
    public static List<String> searchSignature(File file, FileSignature db) throws FileNotFoundException, IOException {

        byte[] buffer = new byte[MAX_SIGNATURE_SIZE];
        InputStream in = new FileInputStream(file);
        int n = in.read(buffer, 0, MAX_SIGNATURE_SIZE);
        String hex = Utilities.toHex(buffer);
        StringBuilder sb = new StringBuilder();
        List<String> knownSignatures = new ArrayList<String>();
        boolean b = true;

        if (hex.startsWith(db.getSignature())) {

            sb.append(db.getType());
            sb.append("  ");
            knownSignatures.add(sb.toString());

        }

        return knownSignatures;
    }

    

    public List<FileSignature> ParseFile() throws IOException {

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
