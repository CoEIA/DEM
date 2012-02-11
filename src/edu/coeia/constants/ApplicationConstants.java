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
    
    /** Application folders/Files name Constants */
    private final static String APPLICATION_FOLDER = "CoEIA_Forensics";
    private final static String APPLICATION_CASEES_FOLDER = "CASES";
    private final static String APPLICATION_TMP_FOLDER = "TMP";
    private final static String APPLICATION_HASH_LIBRARY_FOLDER = "HASH_LIBRARY";
    private final static String APPLICATION_LOG_FOLDER = "LOG";
    private final static String APPLICATION_CASES_INFORMATION_FILE = "INDEXES.txt";
    
    
    /** Case Folders/Files name and extension */
    public final static String CASE_HASH_SET_EXTENSION = ".HASH_SET";
    public final static String CASE_EXPORT_EXTENSION = ".DEM_CASE";
    public final static String CASE_PREFERENCE_EXTENSION = "CASE.pref";
    public final static String CASE_AUDITING_EXTENSION = ".LOG";
    public final static String CASE_SERIALIZED_INFORMATION_EXTENSION = ".DAT";
    public final static String CASE_IMAGES_FOLDER = "IMAGES" ;
    public final static String CASE_ARCHIVE_FOLDER = "CASE_ARCHIVE_EXTRACTION" ;
    public final static String CASE_AUDITING_FOLDER = "CASE_AUDITING";
    public final static String CASE_CONFIG_FILE = "config.dat";
    public final static String CASE_TAGS_FOLDER = "CASE_TAGS" ;
    public final static String CASE_ONLINE_EMAIL_ATTACHMENTS_FOLDER = "ONLINE_EMAIL_ATTACHMENTS" ;
    public final static String CASE_OFFLINE_EMAIL_ATTACHMENTS_FOLDER = "OFFLINE_EMAIL_ATTACHMENTS";
    public final static String CASE_EMAIL_DB_FOLDER = "ONELINE_EMAILS_DATABASE" ;
    public final static String CASE_INDEX_FOLDER = "THE_INDEX" ;
    
    // DEM path in user system
    public final static String APPLICATION_PATH = 
            SystemConstant.USER_HOME 
            + File.separator 
            + OpreatingSystemConstants.WINDOWS_APP_FOLDER 
            + File.separator
            + APPLICATION_FOLDER;

    // cases folder
    public final static String APPLICATION_CASES_PATH = APPLICATION_PATH 
            + File.separator 
            + APPLICATION_CASEES_FOLDER ;

    // tmp path 
    public final static String APPLICATION_TMP_PATH = APPLICATION_PATH 
            + File.separator 
            + APPLICATION_TMP_FOLDER ;
    
    // the path for hash library 
    public final static String APPLICATION_HASH_LIBRARY_PATH = APPLICATION_PATH 
            + File.separator 
            + APPLICATION_HASH_LIBRARY_FOLDER;
    
    // application logging path
    public final static String APPLICATION_LOG_PATH = APPLICATION_PATH
            + File.separator 
            + APPLICATION_LOG_FOLDER;
    
    // APPLICATION_CASES_FILE contain name and path of all creating indexs
    public static final String APPLICATION_CASES_FILE = APPLICATION_PATH 
            + File.separator
            + APPLICATION_CASES_INFORMATION_FILE;
    
    // location of temporary files used by the application
    public final static String HIS_TMP = APPLICATION_TMP_PATH + "\\history.txt";
    public final static String PASS_TMP = APPLICATION_TMP_PATH + "\\password.txt";
    public final static String CORRE_FILE = APPLICATION_TMP_PATH + "\\corr_file.xml" ;
    public final static String FF_REPORT = APPLICATION_TMP_PATH + "\\ff_report.html" ;
    
    // tools that was used by application
    public static final String IP_DB = "tools\\countries.db" ;
    public static final String STOP_WORD_FILE = "tools\\stopwords.txt";
    public static final String SMART_CARD_PROGRAM = "tools\\om.exe";
    public static final String IE_HISTORY_PROGRAM = "tools\\iehv.exe";
    public static final String IE_CACHE_PROGRAM = "tools\\IECacheList.exe";
    public static final String IE_PASSWORD_PROGRAM = "tools\\iepv.exe" ;
    public static final String TEMPLATES = "templates\\";
}