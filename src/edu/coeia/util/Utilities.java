/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.util;

import edu.coeia.offlinemail.Message;

import javax.swing.filechooser.FileSystemView;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import java.io.File;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Static convenience methods for common tasks, which eliminate code duplication
 * @author wajdyessam
 */
public class Utilities {

    // from http://www.devx.com/tips/Tip/34353
    // convert little endian to big endain
    public static int convertFromLEToBE (int i) {
        return((i&0xff)<<24)+((i&0xff00)<<8)+((i&0xff0000)>>8)+((i>>24)&0xff);
    }
    
    /**
     * return hexadecimal representation of arrays as uppercase string
     * @param bytes
     * @return 
     */
    public static String toHex(final byte[] bytes) {
        assert bytes != null;

        StringBuilder hex = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            int byte1 = bytes[i] & 0xFF;

            if (byte1 < 0xF) {
                hex.append("0");
            }

            hex.append(Integer.toHexString(byte1).toUpperCase());
        }

        return hex.toString();
    }

    public static String getEmptyStringWhenNullString(String input) {
        String result = "";
        if (input != null) {
            result = input;
        }
        return result;
    }
    
    public static String getEmptyStringWhenNullDate(final Date date) {
        StringBuilder result = new StringBuilder();
        
        if ( date == null)
            return result.toString();
        
        result.append(date.toString());
        return result.toString();
    }

    public static InputStream convertListToInputStream(List<String> input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (String element : input) {
            out.writeUTF(element);
        }
        InputStream toStream = new ByteArrayInputStream(baos.toByteArray());
        return toStream;
    }

    public static List<String> getStringListFromCommaSeparatedString(String input) {
        List<String> list = new ArrayList<String>();

        if (!input.isEmpty()) {
            String[] toArray = input.split(",");
            list.addAll(Arrays.asList(toArray));
        }
        
        return list;
    }
    
    public static String getCommaSeparatedStringFromCollection(Collection<String> collections) {
        StringBuilder result = new StringBuilder();
        
        if ( collections != null) {
           Object[] array = collections.toArray();
           
           for (int i = 0; i < array.length; i++) {
                result.append(array[i]);
                
                if (i < array.length - 1) {
                    result.append(',');
                }
            }
        }
        
        return result.toString();
    }

    /**
     * Highlight the content with yellow color 
     * the content that will be highlighted is the keyword
     * @param content original content
     * @param keyword the keyword to be highlighted
     * @return highlighted string 
     */
    public static String highlightString(final String content, final String keyword) {
        String highlither = "<span style=\"background-color: #FFFF00\">" + keyword + "</span>";
        String highlitedString = content.replace(keyword, highlither);

        return highlitedString;
    }

    public static void selectObjectInExplorer(String path) throws Exception {
        Runtime rt = Runtime.getRuntime();
        rt.exec("explorer /select," + path);
    }

    public static boolean isALocalDirve(String path) {
        boolean result = false;

        File file = new File(path);
        String desc = FileSystemView.getFileSystemView().getSystemTypeDescription(file);

        if (desc.startsWith("Local Disk")) {
            result = true;
        }

        return (result);
    }

    public static String filterLine(String line) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            if (i % 60 == 0) {
                result.append("<br></br>");
            }

            result.append(line.charAt(i));
        }

        return result.toString();
    }

    public static String reverseHost(String host) {
        StringBuilder tmp = new StringBuilder();

        for (int i = host.length() - 2; i >= 0; i--) {
            tmp.append(host.charAt(i));
        }

        return tmp.toString();
    }

    public static String getFireFoxUserName(String path) {
        int sIndex = path.indexOf("Settings\\") + "Settings".length() + 1;
        int eIndex = path.indexOf("Application Data") - 1;

        String user = path.substring(sIndex, eIndex);
        return user;
    }

    public static void setToClipBoard(String content) {
        StringSelection str = new StringSelection(content);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);
    }

    public static boolean isFound(List<String> aList, String userName) {
        for (String str : aList) {
            if (userName.equals(str.trim())) {
                return (true);
            }
        }

        return false;
    }

    public static boolean isMessageFound(List<Message> aList, String userName) {
        for (Message str : aList) {
            if (userName.equals(str.getSenderName().trim())) {
                return (true);
            }
        }

        return false;
    }

    public static int getMessageIndex(List<Message> list, Message msg) {
        for (int i = 0; i < list.size(); i++) {
            if (msg.getSenderName().equals(list.get(i).getSenderName())) {
                return i;
            }
        }

        return -1;
    }

    public static boolean isExtentionAllowed(List<String> extensionsAllowed, String ext) {
        for (String str : extensionsAllowed) {
            if (str.equalsIgnoreCase(ext)) {
                return true;
            }
        }

        return (false);
    }


    /**
     * Return <tt>true</tt> only if <tt>aText</tt> is not null,
     * and is not empty after tramming.
     * 
     * Trimming removes both leading/trailing white-spaces and ASCII 
     * control characters
     * 
     * @param aText string of text
     */
    public static boolean textHasContent(String aText) {
        return aText != null && !aText.isEmpty();
    }

    public static Date checkDate(Date date) {
        if (date == null) {
            return new Date();
        }
        return date;
    }
}