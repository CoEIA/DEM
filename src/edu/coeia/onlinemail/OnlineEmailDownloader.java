package edu.coeia.onlinemail;

import edu.coeia.cases.Case;
import edu.coeia.util.ApplicationLogging;
import edu.coeia.wizard.EmailConfiguration;
import edu.coeia.wizard.EmailConfiguration.ONLINE_EMAIL_AGENT;
import edu.coeia.util.FileUtil;
import edu.coeia.util.Utilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static edu.coeia.util.PreconditionsChecker.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.text.Normalizer;
import java.util.Collections;
import java.util.Date;
import javax.mail.Session;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;
import javax.mail.Part;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.MessagingException;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.FolderClosedException;
import javax.mail.UIDFolder;
import javax.mail.internet.MimeUtility;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.text.JTextComponent;

class ProgressData {
    
    OnlineEmailMessage mEmail;
    
    public ProgressData(OnlineEmailMessage msg) {
        
        this.mEmail = msg;
    }
}

/**
 * Email Reader - Read a Gmail Email using Java Mail API
 * and Store all the message in List for processing
 * @version 1.0 18-7-2011
 */
public class OnlineEmailDownloader extends SwingWorker<Void, ProgressData> {
    
    private EmailDownloaderDialog emaildialogue;
    private boolean emailFinished, m_bPause;
    private String dbPath, m_strTmpPath;
    private String Username;
    private String Password;
    private ResumeState objResume;
    
    /**
     * Logger Object
     */
    private final static Logger logger = ApplicationLogging.getLogger();

    /**
     * static factory that read all message from user account and store it in list
     * @param username the name of the account
     * @param password the password of the account
     * @return EmailReader object that contain all the messages in list
     */
    public static OnlineEmailDownloader newInstance(EmailDownloaderDialog dialogue,
            String attachmentsPath, String dbPath, String strTmpPath) throws SQLException, NoSuchProviderException, MessagingException, IOException {
        // check null values
        checkNull("attachmentsPath must be not null", attachmentsPath);
        checkNull("dbPath must be not null", dbPath);
        checkNotEmptyString("attachmentsPath must be not empty string", attachmentsPath);
        checkNotEmptyString("dbPath must be not empty string", dbPath);
        
        return new OnlineEmailDownloader(dialogue, attachmentsPath, dbPath, strTmpPath);
    }
    
    public OnlineEmailDownloader(EmailDownloaderDialog dialogue,
            String attachmentsPath, String dbPath, String strTmpPath) throws SQLException, NoSuchProviderException, MessagingException, IOException {
        
        this.attachmentsPath = attachmentsPath;
        this.emaildialogue = dialogue;
        this.dbPath = dbPath;
        this.m_bPause = false;
        this.m_strTmpPath = strTmpPath;
        
        objResume = new ResumeState();

        // if(true) objResume.Activate();
        
        System.setProperty("mail.mime.address.strict", "false");
        System.setProperty("mail.mimi.decodefilename", "true");
        System.setProperty("mail.mimi.base64.ignoreerros", "true");
        System.setProperty("mail.mime.multipart.ignorewhitespaceline", "true");
        System.setProperty("mail.mime.parameters.strict", "false");
        System.setProperty("mail.mime.setdefaulttextcharse", "true");
        
        FileUtil.createFolder(attachmentsPath);
        
        this.emaildialogue.addWindowListener(new WindowAdapter() {
            
            public void windowClosed(WindowEvent e) {
                cancel(true);
            }
        });
        emaildialogue.getCancelButton().setEnabled(false);
    }
    
    private void createDB() throws SQLException {
        
        try {
            this.db = new OnlineEmailDBHandler(this.dbPath);
        } catch (Exception ex) {
            Logger.getLogger(OnlineEmailDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void pauseDownloading() {
        this.m_bPause = true;
        logger.info("paused has been pressed");
        
        try {
            ResumeState resTmp = readResumeStatus();
            if (resTmp != null) {
                objResume = resTmp;
                if (objResume.isActive()) {
                    String strIsResume = emaildialogue.getPauseButton().getText();
                    if (strIsResume.compareTo("Resume") == 0) {
                        emaildialogue.getPauseButton().setText("Pause");
                        emaildialogue.getDownloadProgressBar().setIndeterminate(true);
                        List<EmailConfiguration> emailInfos = emaildialogue.getCase().getEmailConfigurations();
                        for (EmailConfiguration s : emailInfos) {
                            downloadEmail(emaildialogue.getCase(), s, emaildialogue);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void downloadEmail(Case currentCase, EmailConfiguration config, EmailDownloaderDialog dialogue) throws Exception {

        // if hotmail
        if (config.getSource() == ONLINE_EMAIL_AGENT.HOTMAIL) {
            
            dialogue.onlineEmailDownloader = new OnlineEmailDownloader(dialogue, attachmentsPath, dbPath, m_strTmpPath);
            dialogue.onlineEmailDownloader.ConnectPop3Hotmail(Username, Password);
            dialogue.onlineEmailDownloader.execute();
            dialogue.setVisible(true);
            
        }
        // if YAHOO
        if (config.getSource() == ONLINE_EMAIL_AGENT.YAHOO) {
            
            dialogue.onlineEmailDownloader = new OnlineEmailDownloader(dialogue, attachmentsPath, dbPath, m_strTmpPath);
            dialogue.onlineEmailDownloader.ConnectPop3Yahoo(Username, Password);
            dialogue.onlineEmailDownloader.execute();
            dialogue.setVisible(true);
            
        }
        
        if (config.getSource() == ONLINE_EMAIL_AGENT.GMAIL) {
            dialogue.onlineEmailDownloader = new OnlineEmailDownloader(dialogue, attachmentsPath, dbPath, m_strTmpPath);
            dialogue.onlineEmailDownloader.ConnectIMAP(Username, Password);
            dialogue.onlineEmailDownloader.execute();
            dialogue.setVisible(true);
            
        }
        
    }

    /**
     * write resume state into file 
     * @param ResumeState object
     * @throws Exception 
     */
    private void writeResumeStatus(ResumeState resumeState) throws Exception {        
        
        String strFilePath = m_strTmpPath + "resume.dat";        
        File file = new File(strFilePath);
        FileUtil.writeObject(resumeState, file);
        
    }

    /**
     * delete resume state into file 
     * @param 
     * @throws Exception 
     */
    private void deleteResumeStatus() {        
        
        String strFilePath = m_strTmpPath + "resume.dat";        
        FileUtil.removeFile(strFilePath);
    }

    /**
     * read the file and retrieve the object
     * @param file the file path to be read
     * @return ResumeState object
     * @throws Exception 
     */
    public ResumeState readResumeStatus() throws Exception {
        String strFilePath = m_strTmpPath + "resume.dat";        
        
        if (!FileUtil.isFileFound(strFilePath)) {
            return null;
        }
        
        File file = new File(strFilePath);
        ResumeState resumeState = FileUtil.readObject(file);
        return resumeState;
    }
    
    protected void threadInitialization() throws IOException, Exception {
        ResumeState resTmp = readResumeStatus();
        
        if (resTmp != null) {
            objResume = resTmp;
            emaildialogue.getPauseButton().setText("Resume");
            //JOptionPane.showMessageDialog(emaildialogue, "Do you want to resume your current download?", "Resume Option", JOptionPane.);
            int result = JOptionPane.showConfirmDialog(emaildialogue, "Do you want to resume your current download?", "Resume Option", JOptionPane.YES_NO_OPTION);
            if (result != JOptionPane.YES_OPTION) {
                this.deleteResumeStatus();
                objResume.resetState();
            }
            
            emaildialogue.getPauseButton().setText("Pause");
        } else {
            emaildialogue.getPauseButton().setText("Pause");
        }

        // 1).  Create Data base 
        createDB();
        emaildialogue.getCancelButton().setEnabled(true);
        emaildialogue.getPauseButton().setEnabled(true);
    }
    
    @Override
    protected Void doInBackground() throws Exception, MessagingException, IOException, SQLException {
        
        int count = 0;
        
        if (isCancelled()) {
            emailFinished = false;
            return null;
        }
        
        this.threadInitialization();

        // 2).  Crawel For Each Folder 
        javax.mail.Folder[] folders = store.getDefaultFolder().list("*");
        
        for (javax.mail.Folder folder : folders) {
            Message[] messages = null;
            int iMessageCounter = 0;
            
            if (!isFolderHoldMessages(folder)) {
                continue;
            }
            
            if (objResume.isActive() && (objResume.getFoundState() == false)) {
                if (objResume.getFolderName().compareToIgnoreCase(folder.getFullName()) != 0) {
                    continue;
                } else {
                    objResume.setFoundState(true);
                    
                    Message[] mCurrentMessages = folder.getMessages();
                    
                    if (folder instanceof com.sun.mail.pop3.POP3Folder) {
                        int lLastMsgId = mCurrentMessages.length;
                        int iMsgId = iMessageCounter = Integer.parseInt(objResume.getMessageId());
                        messages = ((com.sun.mail.pop3.POP3Folder) folder).getMessages((int) iMsgId, (int) lLastMsgId);
                    } else {
                        long lLastMsgId = (((UIDFolder) folder).getUID(mCurrentMessages[mCurrentMessages.length - 1]));                        
                        long iMsgId = (long) Integer.parseInt(objResume.getMessageId());
                        messages = ((com.sun.mail.imap.IMAPFolder) folder).getMessagesByUID(iMsgId, lLastMsgId);
                    }

                    /* since resume status has been found we need to start processing again. 
                     * so resume deactivation is happening here On next pause resume will active again.
                     */
                    objResume.Deactivate();
                }
            } else {
                messages = folder.getMessages();
            }
            
            if (messages != null) {

                // 4). For Each Message dump the message and download attachment
                for (Message message : messages) {
                    
                    
                    if (this.m_bPause) {
                        
                        
                        String strMsgId;
                        if (folder instanceof com.sun.mail.pop3.POP3Folder) {
                            strMsgId = String.valueOf(iMessageCounter);
                        } else {
                            strMsgId = String.valueOf(((UIDFolder) folder).getUID(message));
                        }
                        
                        objResume.setFolderName(folder.getFullName());
                        objResume.setMessageId(strMsgId);
                        objResume.setFoundState(false);
                        objResume.Activate();
                        
                        this.writeResumeStatus(objResume);
                        this.cancel(true);
                        folder.close(true);
                        
                    }
                    
                    if (isCancelled()) {
                        emailFinished = false;
                        return null;
                    }
                    
                    try {
                        int messageId = messages.length - count++;
                        // sent and receive date
                        Date sentDate = Utilities.checkDate(message.getSentDate());
                        Date receiveDate = Utilities.checkDate(message.getReceivedDate());
                        // Get cc, bcc, to list
                        List<String> cclist = getAddress(message, Message.RecipientType.CC);
                        List<String> bcclist = getAddress(message, Message.RecipientType.BCC);
                        List<String> to = getAddress(message, Message.RecipientType.TO);
                        // Get from, subject, and body
                        String from = getFrom(message);
                        String subject = Utilities.getEmptyStringWhenNullString(message.getSubject());
                        String body = Utilities.getEmptyStringWhenNullString(getText(message));
                        
                        // Save Attachment
                        List<String> Paths = getAttachments(message, messageId);
                        // Save In DBint id, String Username, String from, List<String> to, 
                        OnlineEmailMessage msg = OnlineEmailMessage.newInstance(messageId, this.Username,
                                from, to, bcclist, cclist, subject, body, sentDate.toString(), receiveDate.toString(), Paths, folder.getFullName());
                        db.inserteEmail(msg);
                        db.getConnection().commit();

                        // Publish Data To Thread
                        ProgressData PData = new ProgressData(msg);
                        publish(PData);
                        
                    } // Continue Crawling after Folder Closed Exception ** POP3 Only
                    catch (FolderClosedException ex) {
                        ex.printStackTrace();
                        ConnectPop3Hotmail(Username, Password);
                        if (!folder.isOpen()) {
                            folder.open(Folder.READ_ONLY);
                            messages = folder.getMessages();
                        }
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                        if (!folder.isOpen()) {
                            ConnectPop3Hotmail(Username, Password);
                            folder.open(Folder.READ_ONLY);
                            messages = folder.getMessages();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    
                    iMessageCounter++;
                }
            }
        }
        emailFinished = true;
        return null;
    }
    
    private boolean isFolderHoldMessages(Folder folder) throws MessagingException {
        
        boolean b = false;
        try {
            if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
                
                System.out.println(folder.getFullName() + ": " + folder.getMessageCount());
                folder.open(Folder.READ_ONLY);
                b = true;
            } else if (javax.mail.Folder.HOLDS_FOLDERS != 0) {
                b = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return b;
        
    }

    private String getFrom(Message message) throws MessagingException {
        String result = "";
        Address[] addresses = message.getFrom();
        for (Address add : addresses) {
            result = getFromAddress(add.toString());
            break;
        }
        return result;
        
    }
    
    @Override
    protected void done() {
        
        if (emailFinished) {
            this.deleteResumeStatus();
            JOptionPane.showMessageDialog(emaildialogue, "Finished Downloading Emails", "Done", JOptionPane.INFORMATION_MESSAGE);
            emaildialogue.setVisible(false);
            emaildialogue.getDownloadProgressBar().setIndeterminate(false);
        } else {
            if (objResume.isActive()) {
                JOptionPane.showMessageDialog(emaildialogue, "Email downloading has been paused", "Paused", JOptionPane.INFORMATION_MESSAGE);
                emaildialogue.getPauseButton().setText("Resume");
            } else {
                //delete resume file 
                this.deleteResumeStatus();
                JOptionPane.showMessageDialog(emaildialogue, "Cancelled Email Downloading", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                emaildialogue.setVisible(false);
            }
            
            
        }
        
        emaildialogue.getDownloadProgressBar().setIndeterminate(false);
        
        try {
            store.close();
            try {
                this.db.closeDB();
            } catch (SQLException e) {
                if (e.getErrorCode() == 50000 && ("XJ015").equals(e.getSQLState())) {
                    System.out.println("Derby Shutdown normally");
                } else {
                    System.out.println("Derby Did not shutdown normally");
                    e.printStackTrace();
                }
            }
        } catch (MessagingException ex) {
            Logger.getLogger(OnlineEmailDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    protected void process(List<ProgressData> chunks) {
        if (isCancelled()) {
            emailFinished = false;
            return;
        }
        
        for (ProgressData pd : chunks) {
            emaildialogue.getFrom().setText(Utilities.getEmptyStringWhenNullString(pd.mEmail.getFrom()));
            emaildialogue.getSubject().setText(Utilities.getEmptyStringWhenNullString(pd.mEmail.getSubject()));
            emaildialogue.getSentDate().setText(pd.mEmail.getSentDate());
            this.setEmailDialogElementsText(emaildialogue.getTo(), pd.mEmail.getTo());
            this.setEmailDialogElementsText(emaildialogue.getCC(), pd.mEmail.getCC());
            this.setEmailDialogElementsText(emaildialogue.getBCC(), pd.mEmail.getBCC());
            this.setEmailDialogElementsText(emaildialogue.getAttachments(), pd.mEmail.getAttachments());
        }
    }
    
    private void setEmailDialogElementsText(JTextComponent field, List<String> texts) {
        StringBuilder buffer = new StringBuilder();
        
        if (!texts.isEmpty()) {
            for (String text : texts) {
                buffer.append(Utilities.getEmptyStringWhenNullString(text)).append("\n");
            }
        }
        
        field.setText(buffer.toString());
    }
    
    public boolean ConnectPop3Hotmail(String UserName, String Password) {
        
        this.Username = UserName;
        this.Password = Password;
        
        Properties props = System.getProperties();
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.pop3.port", "995");
        props.setProperty("mail.pop3.socketFactory.port", "995");
        props.setProperty("mail.pop3.connectiontimeout", "10000");
        props.setProperty("mail.pop3.timeout", "10000");
        
        Session session = Session.getDefaultInstance(props, null);
        try {
            store = session.getStore("pop3");
        } catch (NoSuchProviderException ex) {
            
            JOptionPane.showMessageDialog(this.emaildialogue, "Error Connecting to Hotmail", "Bad Such Provider", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        }
        try {
            store.connect("pop3.live.com", 995, UserName, Password);
        } catch (MessagingException ex1) {
            Logger.getLogger(OnlineEmailDownloader.class.getName()).log(Level.SEVERE, null, ex1);
            JOptionPane.showMessageDialog(this.emaildialogue, "Check Username or Password or Connection is lost", "Error in Connecting", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        /****************************/
        //  session.setDebug(true);
        return true;
    }
    
    public boolean ConnectPop3Yahoo(String UserName, String Password) {
        
        this.Username = UserName;
        this.Password = Password;
        
        Properties props = System.getProperties();
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.pop3.port", "995");
        props.setProperty("mail.pop3.socketFactory.port", "995");
        props.setProperty("mail.pop3.connectiontimeout", "10000");
        props.setProperty("mail.pop3.timeout", "10000");
        
        Session session = Session.getDefaultInstance(props, null);
        try {
            store = session.getStore("pop3");
        } catch (NoSuchProviderException ex) {
            
            JOptionPane.showMessageDialog(this.emaildialogue, "Error Connecting to Hotmail", "Bad Such Provider", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        }
        try {
            store.connect("plus.pop.mail.yahoo.com", 995, UserName, Password);
        } catch (MessagingException ex1) {
            Logger.getLogger(OnlineEmailDownloader.class.getName()).log(Level.SEVERE, null, ex1);
            JOptionPane.showMessageDialog(this.emaildialogue, "Check Username or Password or Connection is lost", "Error in Connecting", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        /****************************/
        //  session.setDebug(true);
        return true;
    }
    
    public boolean ConnectIMAP(String UserName, String Password) {
        
        this.Username = UserName;
        this.Password = Password;
        
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.partialfetch", "false");
        props.setProperty("mail.pop3.connectiontimeout", "10000");
        props.setProperty("mail.pop3.timeout", "10000");
        
        Session session = Session.getDefaultInstance(props, null);
        try {
            store = session.getStore("imaps");
        } catch (NoSuchProviderException ex) {
            JOptionPane.showMessageDialog(this.emaildialogue, "Error Connecting to Gmail", "Bad Such Provider", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        }
        
        try {
            store.connect("imap.gmail.com", UserName, Password);
        } catch (MessagingException ex1) {
            Logger.getLogger(OnlineEmailDownloader.class.getName()).log(Level.SEVERE, null, ex1);
            JOptionPane.showMessageDialog(this.emaildialogue, "Check Username or Password or Connection is lost", "Error in Connecting", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        /****************************/
        //  session.setDebug(true);
        return true;
    }
    
    private String getFromAddress(String fromAdd) {
        Pattern p = Pattern.compile("[A-Z0-9\\._%\\+\\-]+@[A-Z0-9\\.\\-]+\\.[A-Z]{2,4}", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(fromAdd);
        
        while (m.find()) {
            fromAdd = (m.group());
        }
        
        return fromAdd;
    }
    
    private List<String> getAddress(Message message, Message.RecipientType type)
            throws MessagingException {
        
        List<String> addressList = new ArrayList<String>();
        Address[] recipients = null;
        try {
            recipients = message.getRecipients(type);
        } catch (MessagingException ex) {
            ex.printStackTrace();
            String d = ex.getMessage();
        }
        
        if (recipients != null) {
            for (Address address : recipients) {
                addressList.add(address.toString());
            }
        }
        
        return addressList;
    }
    
    private List<String> getAttachments(Message temp, int id) throws IOException, MessagingException {
        
        attachments = new ArrayList<String>();
        Object objRef = null;
        try {
            objRef = temp.getContent();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            if (!(objRef instanceof Multipart)) {
                return Collections.emptyList();
            }
            
            Multipart multipart = (Multipart) temp.getContent();
            
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                    continue; // dealing with attachments only
                }
                
                String decoded = MimeUtility.decodeText(bodyPart.getFileName());
                String filename = Normalizer.normalize(decoded, Normalizer.Form.NFC);
                InputStream is = bodyPart.getInputStream();
                if (is == null) {
                    continue;
                }
                
                filename = id + "-" + filename;
                System.out.println("file name" + filename);
                FileUtil.saveObject(bodyPart.getInputStream(), filename, attachmentsPath);
                attachments.add(filename);
            }
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return attachments;
    }
    
    private String getText(Part p) throws
            MessagingException, IOException, Exception {
        if (p.isMimeType("text/*")) {
            String s = null;
            try {
                s = (String) p.getContent();
            } catch (UnsupportedEncodingException ex) {
                InputStream is = p.getInputStream();
                /*
                 * Read the input stream into a byte array.
                 * Choose a charset in some heuristic manner, use
                 * that charset in the java.lang.String constructor
                 * to convert the byte array into a String.
                 */
                s = FileUtil.convertStreamToString(is);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            textIsHtml = p.isMimeType("text/html");
            return s;
        }
        
        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null) {
                        text = getText(bp);
                    }
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null) {
                        return s;
                    }
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null) {
                    return s;
                }
            }
        }
        
        return new String();
    }
    private Store store;
    private List<String> attachments;
    private String attachmentsPath;
    public OnlineEmailDBHandler db;
    private boolean textIsHtml = false;
}
