/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.main;

import edu.coeia.util.ApplicationLogging;
import edu.coeia.constants.SystemConstant;

import java.util.logging.Logger;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;

/**
 *
 * @author wajdyessam
 */
public class ApplicationLauncher {
    /**
     * Main Entry Point
     * Launch the application and display this main window
    */
    public static void main(String args[]) {
        NativeInterface.open(); // used for swing DJ Library
        
        ApplicationLauncher.logSystemInformation();
        ApplicationLauncher.showMainWindow();
    }
    
    private static void logSystemInformation() {
        logger.info("Luanching DEM Application....");
        logger.info(String.format("Operating System: %s %s", SystemConstant.OS_NAME, SystemConstant.OS_VERSION));
        logger.info(String.format("JRE: %s", SystemConstant.JRE_VERSION));
        logger.info(String.format("Java Launched From: %s", SystemConstant.JRE_HOME));
        logger.info(String.format("Application Name: %s", SystemConstant.APPLICATION_NAME_VERSION));
        logger.info(String.format("User Name: %s", SystemConstant.USER_NAME));
        logger.info(String.format("User Home: %s", SystemConstant.USER_HOME));
        logger.info(String.format("User Working Directory: %s", SystemConstant.USER_DIRECTORY));
        logger.info("End of System Basic Information");
    }
    
    private static void showMainWindow() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CaseManagerFrame().setVisible(true);
            }
        });
    }
    
    private final static Logger logger = ApplicationLogging.getLogger();
}
