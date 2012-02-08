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
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author wajdyessam
 */
public class DEMLogger {
   /**
     * Return a Logger whose name follows a specific naming convention.
     * 
     * <P>The conventional Logger names are taken as
     * <tt>aClass.getPackage().getName()</tt>
     */
    public static Logger getLogger(Class<?> aClass) {
        return Logger.getLogger(aClass.getPackage().getName());
    }

    
    /**
     * logging
     * open new log or write to existed one
     * Set Application Logging (Console and File)
     */
    public static void logging(final Logger logger) {
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
                     row.append(" ").append(time).append(" Message (").append(message).append(" )\n");
                     
                     return row.toString();
                 }
            }
            
            LogManager.getLogManager().reset();
            
            ConsoleHandler consoleHandler = new ConsoleHandler();    
            
            String fileName = ApplicationConstants.APPLICATION_LOG_FILE + File.separator
                    + String.format("DEM_%s.log", DateUtil.formatDateForLogFileName(new Date()));
            FileHandler fileHandler = new FileHandler(fileName, true);
            
            consoleHandler.setFormatter(new CustomeFormatter());
            fileHandler.setFormatter(new CustomeFormatter());
            
            logger.addHandler(fileHandler);
            logger.addHandler(consoleHandler);
            
            logger.log(Level.INFO, "DEM Main Frame");
        }
        catch (Exception e ) {}
    }
}
