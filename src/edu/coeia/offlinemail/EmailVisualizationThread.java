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
package edu.coeia.offlinemail;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.visualization.MessageFrequencyDialog;
import edu.coeia.visualization.CorrelationDialog;
import com.pff.PSTFile;
import edu.coeia.gutil.InfiniteProgressPanel;
import edu.coeia.charts.LineChartPanel;
import edu.coeia.util.ApplicationLogging;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.SwingWorker ;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class ProgressEmailVisualizationData {
    public String from, to, date, numberOfMessages;
}

public class EmailVisualizationThread extends SwingWorker<String, ProgressEmailVisualizationData>{

    public enum FolderType { INBOX, SENT, ESP, LOCATION, FREQUENCY, COMMUNICATION };
    
    InfiniteProgressPanel i ;
    JFrame frame ;
    MessageFrequencyDialog dialog;
    String folderName, pstPath, from, to, userName, fromUser, toUser;
    boolean status;
    PSTFile pst ;
    FolderType type;

    HashMap<String, Integer> data;
    ArrayList<Message> inboxDate, outboxDate, persons;
    
    private final static Logger logger = ApplicationLogging.getLogger();

    public EmailVisualizationThread (JFrame frame, InfiniteProgressPanel i, String folderName, PSTFile testPST,  String path,
            String from, String to, FolderType type) {
        this.frame = frame;
        this.i = i;
        this.status = true;
        this.folderName = folderName;
        this.pst = testPST;
        this.pstPath = path;
        this.from = from;
        this.to = to;
        this.type = type;
        
        logger.info("EmailVisualizationThread Constructor");
    }

    public EmailVisualizationThread (MessageFrequencyDialog frame, InfiniteProgressPanel i, String folderName, PSTFile testPST,  String path,
            String from, String to, FolderType type, String fromUser, String toUser) {
        this.dialog = frame;
        this.i = i;
        this.status = true;
        this.folderName = folderName;
        this.pst = testPST;
        this.pstPath = path;
        this.from = from;
        this.to = to;
        this.type = type;
        this.fromUser = fromUser;
        this.toUser = toUser;
        
        logger.info("EmailVisualizationThread Constructor");
    }

    public String doInBackground () {
        getFolderData();
        userName = getUserName();
        return "" ;
    }

    private void getFolderData () {
        EmailHandler emailHandler = new EmailHandler(pst, pstPath);

        try {
            switch (this.type ) {
                case INBOX:
                    data = emailHandler.getMessagesCountInFolder(folderName, from, to);
                    break;
                case SENT:
                    data = emailHandler.getMessagesCountInFolder(folderName, from, to);
                    break;
                case ESP:
                    data = emailHandler.getEspName(from,to);
                    break;
                case LOCATION:
                    data = emailHandler.getLocations(from,to);
                    break;
                case FREQUENCY:
                    persons = emailHandler.getSenderName(from, to);
                    break;
                case COMMUNICATION:
                    inboxDate  = emailHandler.getInboxMessagesDate(from, to);
                    outboxDate = emailHandler.getOutboxMessagesDate(from, to);
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }

    private String getUserName () {
        EmailHandler emailHandler = new EmailHandler(pst, pstPath);
        return emailHandler.getUserName();
    }

    public void stop () {
        status = false;
        i.interrupt();
        i.stop();
        
        System.out.println("done");
        logger.log(Level.INFO, "Finish EmailReaderThread, Done!");

        switch ( this.type ) {
            case INBOX:
            case SENT:
            case ESP:
            case LOCATION:
                if ( data == null) {
                    JOptionPane.showMessageDialog(null, "Cannot handling this email", "there is problem with this email type", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    try {
                        CorrelationDialog cd = new CorrelationDialog(frame, true, data,userName,folderName);
                        cd.setVisible(true);
                        cd.releaseMemory();
                    } catch (Exception ex) {
                        Logger.getLogger(EmailVisualizationThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

            case FREQUENCY:
                if ( persons == null )
                    JOptionPane.showMessageDialog(null, "Cannot handling this email", "there is problem with this email type", JOptionPane.ERROR_MESSAGE);
                else {
                    MessageFrequencyDialog mfd = new MessageFrequencyDialog(frame,true,persons,
                             folderName, pst, pstPath, from, to, userName);

                    mfd.setVisible(true);
                }
                break;

            case COMMUNICATION:
                try {
                    if ( inboxDate == null || outboxDate == null )
                        JOptionPane.showMessageDialog(null, "Cannot handling this email", "there is problem with this email type", JOptionPane.ERROR_MESSAGE);
                    else {
                        JPanel panel = LineChartPanel.getLineChartPanel(fromUser, toUser, inboxDate, outboxDate);
                        dialog.setPanel(panel);
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(EmailVisualizationThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }
    }
            
    @Override
    public void done () {
        stop();
    }
}
