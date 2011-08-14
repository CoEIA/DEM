/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.chat;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.main.util.Utilities;

import java.io.IOException ;
import java.io.File ;
import java.io.FilenameFilter ;

import java.util.ArrayList ;
import java.util.HashMap ;

import java.util.regex.Pattern ;
import java.util.regex.Matcher ;

public class MSNParser {
    private static final String MSN_SETTINGS = "HKEY_CURRENT_USER\\Software\\Microsoft\\MSNMessenger\\PerPassportSettings";

    private static final String Valid_LOGGING_KEY = " /v MessageLoggingEnabled" ;
    private static final String USER_NAME_KEY = " /v UTL" ;
    private static final String MESSAGE_PATH = " /v MessageLogPath" ;

    private ArrayList<String> allUserLoggingToMSN ;
    private HashMap<String,String> allUserLoggingPath ;

    private String path ;
    public MSNParser (String path) {
        allUserLoggingToMSN = new ArrayList<String>();
        allUserLoggingPath  = new HashMap<String,String>();
        this.path = path ;
    }

    public void parse () throws IOException {
        ArrayList<String> msnPathes = getMSNHistoryPath();

        for (String line: msnPathes) {
            int value = getLoggingValue(line);

            if ( value == 0 ) {
                String name = getUserNameFromKey(line);
                allUserLoggingToMSN.add(name);
            }
            else if ( value == 1 ) {
                String name = getUserNameFromKey(line);
                String p = getMessagePathFromKey(line);
                allUserLoggingPath.put(name,p);
            }
        }
    }

    public ArrayList<String> getAllUserLoggingToMsn () {
        return allUserLoggingToMSN ;
    }

    public HashMap<String,String> getAllUserLoggingPath () {
        return allUserLoggingPath ;
    }

    private String getMessagePathFromKey (String path) throws IOException {
        ArrayList<String> result  = queryRegistryForSubKeys(path + MESSAGE_PATH);
        ArrayList<String> newPath = parseKey(result);
        String line = newPath.get(0).trim() ;

        String msgPath = getPath(line);

        return msgPath ;
    }

    private String getPath (String line) {
        String lines = line.split("REG_SZ")[1].trim();
        return lines ;
    }

    private String getUserNameFromKey (String path) throws IOException {
        ArrayList<String> result  = queryRegistryForSubKeys(path + USER_NAME_KEY);
        ArrayList<String> newPath = parseKey(result);
        String line = newPath.get(0).trim() ;
        String userName = getEmail(line);

        return userName ;
    }

    private String getEmail (String line) {
        String lines = line.split(" ")[1];
        String email = lines.split("\"")[1];

        return email ;
    }

    private ArrayList<String> parseKey (ArrayList<String> oldList) {
        ArrayList<String> newList = new ArrayList<String>();

        for (String line: oldList ) {
            if ( ! line.isEmpty()  && ! line.startsWith("! REG.EXE VERSION 3.0") && !line.startsWith("HKEY_CURRENT_USER") ) {
                newList.add(line);
            }
        }

        return newList ;
    }

    /*
        0 means valid logging without storing history
        1 means valid logging with storing history
        -1 means not valid logging
    */
    private int getLoggingValue (String path) throws IOException {
        ArrayList<String> result  = queryRegistryForSubKeys(path + Valid_LOGGING_KEY);
        ArrayList<String> newPath = parseValues(result);

        for (String line: newPath) {
            if ( line.startsWith("Error:") )
                return -1 ;
            else if ( line.contains("0x0") )
                return 0 ;
            else
                return 1 ;
        }

        return -1 ;
    }

    private ArrayList<String> parseValues (ArrayList<String> oldList) {
        ArrayList<String> newList = new ArrayList<String>();

        for (String line: oldList ) {
            if ( ! line.isEmpty()  && ! line.startsWith("! REG.EXE VERSION 3.0") && line.trim().startsWith("MessageLoggingEnabled") ) {
                newList.add(line);
            }
        }

        return newList ;
    }

    public ArrayList<String> getMSNHistoryPath () throws IOException {
        ArrayList<String> subkeys = queryRegistryForSubKeys();
        ArrayList<String> result  = new ArrayList<String>();

        ArrayList<String> newSubKeys = parseSubKeys(subkeys);

        for (String subkey: newSubKeys) {
            result.add( subkey );
        }

        return (result);
    }

    private ArrayList<String> parseSubKeys (ArrayList<String> oldList) {
        ArrayList<String> newList = new ArrayList<String>();

        for (String line: oldList ) {
            if ( ! line.isEmpty()  && line.startsWith("HKEY_CURRENT_USER") && ! line.equals(MSN_SETTINGS) ) {
                newList.add(line);
            }
        }

        return newList ;
    }

    private ArrayList<String> queryRegistryForSubKeys () throws IOException  {
        return queryRegistryForSubKeys(MSN_SETTINGS);
    }

    private ArrayList<String> queryRegistryForSubKeys (String command) throws IOException  {
        String query = path + " QUERY " + command ;
        return ( Utilities.readProgramOutputStream( query)  );
    }

    // msn raeder
    public String getFileName (String name) {
        Pattern p = Pattern.compile("[a-zA-Z_.]*");
        Matcher m = p.matcher(name);

        if (m.find()) {
                return m.group(0);
        }

        return "" ;
    }

    public File[] getAllFiles (String path) {
        File dir = new File(path);

        File[] files = dir.listFiles( new FilenameFilter() {
                public boolean accept (File dir, String name) {
                    return name.toLowerCase().endsWith(".xml");
                }
            }
        );

        return (files);
    }

    public String getFileFromPath (String path, String childName) {
        File[] files = getAllFiles(path);

        for (File f: files) {
            if ( f.getName().startsWith(childName))
                return f.getAbsolutePath();
        }

        return "" ;
    }
    
}

//REG query HKEY_CURRENT_USER\Software\Microsoft\MSNMessenger\PerPassportSettings
