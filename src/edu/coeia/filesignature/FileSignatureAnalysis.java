/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.filesignature;

import edu.coeia.util.FileUtil;
import edu.coeia.util.Utilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ahmed
 */
public class FileSignatureAnalysis {

    private static final int MAX_SIGNATURE_SIZE = 8;

    public static String getFileSignature(File file) throws FileNotFoundException, IOException {
        byte[] buffer = new byte[15];
        InputStream in = new FileInputStream(file);
        int n = in.read(buffer, 0, 15);
        String hex = Utilities.toHex(buffer);
        return hex;
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
        String extension = FileUtil.getExtension(file);
        boolean b = false;
        for (String ext : db.getExtension()) {

            if (extension.equalsIgnoreCase(ext)) {
               
                b = true;
                break;
            }
        }

        return b;
    }

    // Get the extension of the file and search for matached signature based on
    // on the extension
    public static boolean isMatchedSignature(File file, FileSignature fs) throws FileNotFoundException, IOException {
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
    
    // Get the extension of the file and search for matached signature based on
    // on the extension
    public static boolean isBadSignature(File file, FileSignature fs) throws FileNotFoundException, IOException {
        // First Case, Extension in DB Table, but Signature is different
        // ext  == sign in database 
        boolean res = false;
        res = verifyExtenstion(file, fs);
        boolean b = true;

        boolean result = matchesSignature(fs.getSignature().getBytes(), file);
        if (result == false && res == true) {
            System.out.println("Bad Signature");
            b = true;

        } else {
            b = false;
        }
       return b;
    }
    public static boolean isUnknown(File file, FileSignature fs) throws FileNotFoundException, IOException {
        // First Case, Extension in DB Table, but Signature is different
        // ext  == sign in database 
        String extension = FileUtil.getExtension(file);
        boolean result = isknownFile(file, fs);
        boolean b = true;
        for (String ext : fs.getExtension()) {
            if ((!extension.equalsIgnoreCase(ext)) && result == false) {
                b = false;
            }
            else {
                b = true;
            }
        }
  
        
       return b;
    }

    public static boolean matchAliasSignature(File file, FileSignature db) throws FileNotFoundException, IOException {

        String extension = FileUtil.getExtension(file);
        boolean result = isMatchedSignature(file, db);
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
}
