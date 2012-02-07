/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.util;

/**
 *
 * @author wajdyessam
 */

public class SystemConstant {
    
    // application name
    public final static String APPLICATION_NAME = "Digital Evidence Miner: ";
    
    // application version
    public final static String APPLICATION_VERSION = "2";
    
    // application and version
    public final static String APPLICATION_NAME_VERSION = APPLICATION_NAME 
            + APPLICATION_VERSION;
    
    // auther name
    public final static String AUTHER = "COEIA";
    
    // current user name
    public final static String USER_NAME = System.getProperty("user.name");

    // user home directory
    public final static String USER_HOME = System.getProperty("user.home");
    
    // user working directory
    public final static String USER_DIRECTORY = System.getProperty("user.dir");
    
    // operating system version
    public final static String OS_NAME = System.getProperty("os.name");
    
    // operating system version
    public final static String OS_VERSION = System.getProperty("os.version");
    
    // java run time version
    public final static String JRE_VERSION = System.getProperty("java.version");
    
    // java home path
    public final static String JRE_HOME = System.getProperty("java.home");
    
    public final static String DUSK_LOOK_AND_FEEL = "org.jvnet.substance.skin.SubstanceDustLookAndFeel";
    public final static String RAVEN_LOOK_AND_FEEL = "org.jvnet.substance.skin.SubstanceRavenGraphiteLookAndFeel";
    public final static String BUSINESS_LOOK_AND_FEEL = "org.jvnet.substance.skin.SubstanceBusinessLookAndFeel";
    public final static String WINDOWS_LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    
}
