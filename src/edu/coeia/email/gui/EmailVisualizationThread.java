/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.email.gui;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.email.correlation.CorrelationDialog;
import com.pff.PSTFile;
import edu.coeia.email.EmailHandler;
import edu.coeia.main.gui.util.InfiniteProgressPanel;
import edu.coeia.main.chart.LineChartPanel;
import edu.coeia.email.util.Message;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.SwingWorker ;

import java.util.logging.FileHandler;
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
    
    private static Logger logger = Logger.getLogger("EmailVisualizationThread");
    private static FileHandler handler ;

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

        try {
            handler = new FileHandler("EmailVisualizationThread.log");
            logger.addHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Uncaught exception", ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, "Uncaught exception", ex);
        }
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
        
        try {
            handler = new FileHandler("EmailVisualizationThread.log");
            logger.addHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Uncaught exception", ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, "Uncaught exception", ex);
        }
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
                    CorrelationDialog cd = new CorrelationDialog(frame, true, data,userName,folderName);
                    cd.setVisible(true);
                    cd.releaseMemory();
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
