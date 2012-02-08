/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.util;

import edu.coeia.constants.ApplicationConstants;

import java.io.File;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author wajdyessam
 */
public final class ApplicationLogging {
    
    private static final Logger DEM_LOGGER = Logger.getLogger("DEM_LOGGER");
    
    static {
        setupLogger();
    }
    
   /**
     * Return a Logger whose name follows a specific naming convention.
     * 
     * <P>The conventional Logger names are taken as
     * <tt>aClass.getPackage().getName()</tt>
     */
    public static Logger getLogger() {
        return DEM_LOGGER;
    }
    
    /**
     * logging
     * open new log or write to existed one
     * Set Application Logging (Console and File)
     */
    private static void setupLogger() {
        try {
            class CustomeFormatter extends Formatter{
                 @Override
                 public synchronized String format(LogRecord record){
                     StringBuilder row = new StringBuilder();
                     String level = record.getLevel().getName();
                     String className = record.getSourceClassName();
                     String methodName = record.getSourceMethodName();
                     String message = record.getMessage();
                     String time = DateUtil.formatDateTime(new Date(record.getMillis()));
                     
                     row.append("[");
                     row.append(level);
                     row.append("] ");
                     row.append(className).append(".").append(methodName);
                     row.append(" ").append(time).append(" (").append(message).append(" )\n");
                     
                     return row.toString();
                 }
            }
            
            DEM_LOGGER.setUseParentHandlers(false);
            
            String pattern = "log%g.out";
            String fileName = ApplicationConstants.APPLICATION_LOG_FILE + File.separator + pattern;
            
            FileHandler fileHandler = new FileHandler(fileName, 100000, 10);
            ConsoleHandler consoleHandler = new ConsoleHandler();   
            
            consoleHandler.setFormatter(new CustomeFormatter());
            fileHandler.setFormatter(new CustomeFormatter());
            
            DEM_LOGGER.addHandler(fileHandler);
            DEM_LOGGER.addHandler(consoleHandler);
        }
        catch (Exception e ) {
            DEM_LOGGER.severe(String.format("Cannot be initializing logger with custome handler", e.getMessage()));
        }
    }
}
