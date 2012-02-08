/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

import edu.coeia.constants.SystemConstant;

import java.io.IOException;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * This class represent the set of actions that the user can make on case
 * it will log each of these action in log file
 * 
 * @author wajdyessam
 */

final class CaseAuditing {
    private final static Logger logger = Logger.getLogger("DEM_AUDITING");
    private final Case aCase ;
    private final String logPath;
    
    public CaseAuditing(final Case aCase, final String path) {
        this.aCase = aCase;
        this.logPath = path;
    }
    
    public void init() throws IOException {
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        
        FileHandler fileHandler = new FileHandler(this.logPath, true);
        fileHandler.setFormatter(new AuditingFormatter());
        logger.addHandler(fileHandler);
    }
    
    public void auditing(final String message) {
        logger.info(message);
    }
    
    private class AuditingFormatter extends Formatter{
        @Override
        public synchronized String format (final LogRecord logRecord) {
            StringBuilder buffer = new StringBuilder();

            String investigatorName = aCase.getInvestigatorName();
            String userName = SystemConstant.USER_NAME;
            String timeStamp = new Date(logRecord.getMillis()).toString();
            String message = logRecord.getMessage();
            String caseName = aCase.getCaseName();
            
            buffer.append(investigatorName);
            buffer.append(",");
            buffer.append(userName);
            buffer.append(",");
            buffer.append(timeStamp);
            buffer.append(",");
            buffer.append(caseName);
            buffer.append(",");
            buffer.append(message);
            buffer.append("\n");

            return buffer.toString();
        }
    }
}
