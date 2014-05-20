/* 
 * Copyright (C) 2014 Center of Excellence in Information Assurance
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.coeia.constants;

/**
 * Containing folder name related to the current JRE
 * implementation also hold some look and feel names
 * 
 * also hold the application name and version
 * 
 * @author wajdyessam
 */

public final class SystemConstant {
    
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
    
    /** look and feel constant */
    public final static String DUSK_LOOK_AND_FEEL = "org.jvnet.substance.skin.SubstanceDustLookAndFeel";
    public final static String RAVEN_LOOK_AND_FEEL = "org.jvnet.substance.skin.SubstanceRavenGraphiteLookAndFeel";
    public final static String BUSINESS_LOOK_AND_FEEL = "org.jvnet.substance.skin.SubstanceBusinessLookAndFeel";
    public final static String WINDOWS_LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    
}
