/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.util;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.email.Message;

import java.io.File ;

import javax.swing.filechooser.FileSystemView;

import java.awt.Toolkit ;
import java.awt.datatransfer.StringSelection ;

import java.util.List ;

public class Utilities {

    public static void selectObjectInExplorer (String path) throws Exception{
        Runtime rt = Runtime.getRuntime();
        rt.exec("explorer /select," + path);
    }
        
    public static boolean isALocalDirve (String path) {
        boolean result = false;

        File file = new File(path);
        String desc = FileSystemView.getFileSystemView().getSystemTypeDescription(file);

        if ( desc.startsWith("Local Disk") )
                result = true;

        return (result);
    }

    public static String filterLine (String line) {
        StringBuilder result = new StringBuilder() ;

        for (int i=0; i<line.length(); i++) {
            if ( i % 60 == 0)
                result.append("<br></br>");

            result.append(line.charAt(i));
        }

        return result.toString() ;
    }
    
    public static String reverseHost ( String host ) {
        StringBuilder tmp = new StringBuilder();

        for (int i=host.length()-2; i>=0; i--)
                tmp.append( host.charAt(i) );

        return tmp.toString();
    }

    public static String getFireFoxUserName (String path) {
        int sIndex = path.indexOf("Settings\\") + "Settings".length() + 1 ;
        int eIndex = path.indexOf("Application Data") - 1 ;

        String user = path.substring(sIndex, eIndex);
        return user ;
    }

    public static void setToClipBoard (String content) {
        StringSelection str = new StringSelection(content);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);
    }

    public static boolean isFound (List<String> aList , String userName) {
        for (String str: aList) {
            if ( userName.equals(str.trim()))
                return (true);
        }

        return false;
    }

    public static boolean isMessageFound (List<Message> aList , String userName) {
        for (Message str: aList) {
            if ( userName.equals(str.getSenderName().trim()))
                return (true);
        }

        return false;
    }

    public static int getMessageIndex (List<Message> list, Message msg) {
        for (int i=0 ; i<list.size() ; i++) {
            if ( msg.getSenderName().equals(list.get(i).getSenderName()))
                return i ;
        }

        return -1 ;
    }

    public static boolean isExtentionAllowed (List<String> extensionsAllowed, String ext) {
        for (String str: extensionsAllowed)
            if ( str.equalsIgnoreCase(ext))
                return true ;

        return (false);
    }
}