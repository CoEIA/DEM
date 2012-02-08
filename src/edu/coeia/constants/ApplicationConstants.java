/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.constants;

import java.io.File ;

/**
 *
 * @author wajdyessam
 */

public final class ApplicationConstants {
    
    // logging general namespace
    public final static String LOG_NAMESPACE = "DEM_NAMESPACE";
    
    // Application Constants
    private final static String APPLICATION_FOLDER = "CoEIA_Forensics";
    private final static String WINDOWS_APP_FOLDER = "Application Data";
    private final static String CASEES_FOLDER = "CASES";
    private final static String APPLICATION_TMP_FOLDER = "TMP";
    private final static String HASH_LIBRARY_FOLDER = "HASH_LIBRARY";
    
    // the path for offline mining project
    public final static String APPLICATION_PATH = 
            SystemConstant.USER_HOME 
            + File.separator 
            + WINDOWS_APP_FOLDER 
            + File.separator
            + APPLICATION_FOLDER;

    // the path for cases (indexed folders) in offline mining project
    public final static String CASES_PATH = APPLICATION_PATH 
            + File.separator 
            + CASEES_FOLDER ;

    // the path for tmp files used in project
    public final static String TMP_PATH = APPLICATION_PATH 
            + File.separator 
            + APPLICATION_TMP_FOLDER ;
    
    // the path for hash library 
    public final static String HASH_LIBRARY_PATH = APPLICATION_PATH 
            + File.separator 
            + HASH_LIBRARY_FOLDER;
    
    // log path
    public final static String APPLICATION_LOG_FILE = APPLICATION_PATH
            + File.separator 
            + "LOG";
    
    // APPLICATION_CASES_FILE contain name and path of all creating indexs
    public static final String APPLICATION_CASES_FILE = 
            APPLICATION_PATH 
            + File.separator
            + "INDEXES.txt" ;
    
    // hash set extension
    public final static String CASE_HASH_SET_EXTENSION = ".HASH_SET";
    
    // extension for DEM case
    public final static String CASE_EXPORT_EXTENSION = ".DEM_CASE";
    
    // extension for DEM case preference file
    public final static String CASE_PREFERENCE_EXTENSION = "CASE.pref";
    
    // extension for case LOG file
    public final static String CASE_LOG_EXTENSION = ".LOG";
    
    // extension for case information file .DAT
    public final static String CASE_SERIALIZED_INFORMATION_EXTENSION = ".DAT";
    
    // path for storing case images
    public final static String CASE_IMAGES_FOLDER = "IMAGES" ;
    
    // path for storing case tmp folder
    public final static String CASE_ARCHIVE_FOLDER = "CASE_ARCHIVE_EXTRACTION" ;
    
    // path for storing case configuaration
    public final static String CASE_CONFIG_FILE = "config.dat";
    
    // path for storing case Tags
    public final static String CASE_TAGS_FOLDER = "CASE_TAGS" ;
    
    // path for storing emails in database and attachments in separate folder
    public final static String ONLINE_EMAIL_ATTACHMENTS_FOLDER = "ONLINE_EMAIL_ATTACHMENTS" ;
    public final static String OFFLINE_EMAIL_ATTACHMENTS_FOLDER = "OFFLINE_EMAIL_ATTACHMENTS";
    public final static String EMAIL_DB_FOLDER = "ONELINE_EMAILS_DATABASE" ;

    // index folder for any case have this name
    public static final String CASE_INDEX_FOLDER = "THE_INDEX" ;


   
    //***************************************** need to remove
    public static final String RECENT = "C:\\Documents and Settings\\" + SystemConstant.USER_NAME + "\\Recent";
    //*****************************************

    public static final String IP_DB = "tools\\countries.db" ;
    public static final String STOP_WORD_FILE = "tools\\stopwords.txt";
    private static final String IE_HISTORY_PROGRAM = "tools\\iehv.exe";
    private static final String IE_CACHE_PROGRAM = "tools\\IECacheList.exe";
    private static final String IE_PASSWORD_PROGRAM = "tools\\iepv.exe" ;
    public static final String SMART_CARD_PROGRAM = "tools\\om.exe";
    public static final String TEMPLATES = "templates\\";
    
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
        if ( SystemConstant.OS_NAME.startsWith("Windows 7"))
            return OS_TYPE.SEVEN ;
        else
            return OS_TYPE.XP ;
    }
}