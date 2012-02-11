/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.constants;

import java.io.File;

import static edu.coeia.constants.ApplicationConstants.*;

/**
 *
 * @author wajdyessam
 */
public class OpreatingSystemConstants {
    public final static String WINDOWS_APP_FOLDER = "Application Data";
    
    //***************************************** need to remove
    public static final String RECENT = "C:\\Documents and Settings\\" + SystemConstant.USER_NAME + "\\Recent";
    //*****************************************
    
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
