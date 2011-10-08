/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.main.util;

import java.io.File ;

/**
 *
 * @author wajdyessam
 */

public class FilesPath {
    
    // logging general namespace
    public final static String LOG_NAMESPACE = "DEM_NAMESPACE";
    
    // get current user name
    private final static String USER_NAME = System.getProperty("user.name");

    // get document and settings path for current user
    private final static String USER_HOME = System.getProperty("user.home");

    // the path for offline mining project
    public final static String APPLICATION_PATH = USER_HOME + "\\" + "Application Data\\" + "CoEIA_Forensics";

    // the path for cases (indexed folders) in offline mining project
    public final static String CASES_PATH = APPLICATION_PATH + "\\" + "CASES" ;

    // the path for tmp files used in project
    public final static String TMP_PATH = APPLICATION_PATH + "\\" + "TMP" ;
    
    // path for storing case images
    public final static String IMAGES_PATH = "IMAGES" ;
    
    // log path
    public final static String APPLICATION_LOG_PATH = APPLICATION_PATH + "\\" + "LOG";
    
    // INDEXES_INFO contain name and path of all creating indexs
    public static final String INDEXES_INFO = APPLICATION_PATH + "\\INDEXES.txt" ;

    // index folder for any case have this name
    public static final String INDEX_PATH = "THE_INDEX" ;

    // operating system version
    private static final String OS_VERSION = System.getProperty("os.name");
   
    //***************************************** need to remove
    public static final String RECENT = "C:\\Documents and Settings\\" + USER_NAME + "\\Recent";
    //*****************************************

    public static final String IP_DB = "tools\\countries.db" ;
    public static final String STOP_WORD_FILE = "tools\\stopwords.txt";
    private static final String IE_HISTORY_PROGRAM = "tools\\iehv.exe";
    private static final String IE_CACHE_PROGRAM = "tools\\IECacheList.exe";
    private static final String IE_PASSWORD_PROGRAM = "tools\\iepv.exe" ;
    public static final String SMART_CARD_PROGRAM = "tools\\om.exe";
    
    // location of temporary files used by the application
    public static final String HIS_TMP = TMP_PATH + "\\history.txt";
    public static final String PASS_TMP = TMP_PATH + "\\password.txt";
    public final static String CORRE_FILE = TMP_PATH + "\\corr_file.xml" ;
    public static final String FF_REPORT = TMP_PATH + "\\ff_report.html" ;
    
    // XP path location postfix 
    public static final String IE_HISTORY = "\\Local Settings\\History" ;
    public static final String IE_CACHE = "\\Local Settings\\Temporary Internet Files" ;
    public static final String IE_FAVORITE = "\\Favorites" ;
    public static final String IE_RECENT = "\\Recent" ;
    public static final String IE_COOKIES = "\\Cookies" ;

    // XP Skype Path & postfix
    public static final String SKYPE_PATH_XP = "\\Application Data\\Skype" ;
    public static final String SKYPE_PATH_7 = "\\AppData\\Roaming\\Skype" ;
            
    public static final String SKYPE_DB = "main.db" ;
    
    // check existing of folder
    public static boolean isValidPath (String path, String postfix) {
        return new File(path + postfix).exists();
    }

    // get skype path in Windows XP
    public static String getSkypePath (String path) {
        if ( getOSType() == OS_TYPE.XP)
            return path + SKYPE_PATH_XP ;
        else
            return path + SKYPE_PATH_7;
    }

    // for current user
    public static String getIEPassword () {
        return IE_PASSWORD_PROGRAM + " /scomma " + PASS_TMP ;
    }

    public static String getIECookiesPath (String folder) {
        folder += IE_COOKIES ;
        String path = IE_CACHE_PROGRAM + " -cookies" + " -folder " + "\"" + folder + "\"" ;
        return path ;
    }
    
    public static String getIEHistoryPath (String folder) {
        folder += IE_HISTORY ;
        String p = new File(IE_HISTORY_PROGRAM).getAbsolutePath();
        String path = "\"" + p + "\""  + " /stab \"" + HIS_TMP + "\" -folder " + "\"" + folder + "\"" ;
        return path;
    }

    public static String getIECachePath (String folder) {
        folder += IE_CACHE ;
        String path = IE_CACHE_PROGRAM  + " -cache" + " -folder " + "\"" + folder + "\"";
        return path;
    }

    public static String getIERecentPath(String folder) {
         return folder + IE_RECENT ;
    }

    public static String getIEFavoritePath (String folder) {
        return folder + IE_FAVORITE ;
    }

    public static enum OS_TYPE { XP , SEVEN } ;
    
    public static OS_TYPE getOSType () {
        if ( OS_VERSION.startsWith("Windows 7"))
            return OS_TYPE.SEVEN ;
        else
            return OS_TYPE.XP ;
    }
}