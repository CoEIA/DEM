/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.constants;

import java.io.File ;

/**
 * Containing Constant for Folders and Files Name
 * that will be used throughout the application
 * 
 * also contain the paths for the tools and command lines
 * that is used throughout the application
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
    public final static String CASE_PREFERENCE_FILE = "CASE.pref";
    public final static String CASE_AUDITING_EXTENSION = ".LOG";
    public final static String CASE_SERIALIZED_INFORMATION_EXTENSION = ".DAT";
    public final static String CASE_IMAGES_FOLDER = "IMAGES" ;
    public final static String CASE_ARCHIVE_FOLDER = "CASE_ARCHIVE_EXTRACTION" ;
    public final static String CASE_AUDITING_FOLDER = "CASE_AUDITING";
    public final static String CASE_CONFIG_FILE = "config.dat";
    public final static String CASE_TAGS_FOLDER = "CASE_TAGS" ;
    public final static String CASE_TAG_DATABASE_FILE = "CASE_DB.db";
    public final static String CASE_ONLINE_EMAIL_ATTACHMENTS_FOLDER = "ONLINE_EMAIL_ATTACHMENTS" ;
    public final static String CASE_OFFLINE_EMAIL_ATTACHMENTS_FOLDER = "OFFLINE_EMAIL_ATTACHMENTS";
    public final static String CASE_EMAIL_DB_FOLDER = "ONELINE_EMAILS_DATABASE" ;
    public final static String CASE_INDEX_FOLDER = "THE_INDEX" ;
    public final static String CASE_ROW_REPORT_FOLDER = "RAW";
    public final static String CASE_REPORTS_FOLDER = "Reports";
    public final static String TEMPLATES_FOLDER = "templates";
    
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
    
    /** Reports constants **/
    public static final String FILE_SYSTEM_JASPER_FILE = "filesystem_report.jasper";
    public static final String FILE_SYSTEM_X_PATH = "/dem/detail/effectivefiles/file";
    public static final String FILE_SYSTEM_REPORT_NAME = "filesystem";
    public static final String FILE_SYSTEM_XML_FILE = "filesystem.xml";
    
    public static final String FILE_EXTENSION_JASPER_FILE = "fileextension_report.jasper";
    public static final String FILE_EXTENSION_X_PATH = "/dem/detail/effectiveextensions/ext";
    public static final String FILE_EXTENSION_REPORT_NAME = "fileextension";
    public static final String FILE_EXTENSION_XML_FILE = "fileextension.xml";
    
    public static final String CASES_JASPER_FILE = "cases_report.jasper";
    public static final String CASES_X_PATH = "/dem/cases/case";
    public static final String CASES_REPORT_NAME = "cases";
    public static final String CASES_XML_FILE = "cases.xml";
    
    public static final String TAGS_JASPER_FILE = "taggeditems_report.jasper";
    public static final String TAGS_X_PATH = "/dem/filetags/tag";
    public static final String TAGS_REPORT_NAME = "taggeditems";
    public static final String TAGS_XML_FILE = "taggeditems.xml";
    
    public static final String SIGNATURE_RESULT_JASPER_FILE = "filesignature_report.jasper";
    public static final String SIGNATURE_RESULT_X_PATH = "/filesign/sign";
    public static final String SIGNATURE_RESULT_REPORT_NAME = "filesignature";
    public static final String SIGNATURE_RESULT_XML_FILE = "filesignature.xml";
}