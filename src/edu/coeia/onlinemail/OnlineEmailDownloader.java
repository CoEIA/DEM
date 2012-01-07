package edu.coeia.onlinemail;

import com.sun.mail.pop3.POP3Folder;
import edu.coeia.util.FileUtil;
import edu.coeia.util.Utilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static edu.coeia.util.PreconditionsChecker.*;
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
    private boolean emailFinished,m_bPause;
    private String dbPath;
    private String Username;
    private String Password;
    private ResumeState objResume;
   
    
    
    /**
     * Logger Object
     */
    private static final Logger logger = Logger.getLogger(edu.coeia.util.FilesPath.LOG_NAMESPACE);
    

    /**
     * static factory that read all message from user account and store it in list
     * @param username the name of the account
     * @param password the password of the account
     * @return EmailReader object that contain all the messages in list
     */
    public static OnlineEmailDownloader newInstance(EmailDownloaderDialog dialogue,
            String attachmentsPath, String dbPath) throws SQLException, NoSuchProviderException, MessagingException, IOException {
        // check null values
        checkNull("attachmentsPath must be not null", attachmentsPath);
        checkNull("dbPath must be not null", dbPath);
        checkNotEmptyString("attachmentsPath must be not empty string", attachmentsPath);
        checkNotEmptyString("dbPath must be not empty string", dbPath);
        return new OnlineEmailDownloader(dialogue, attachmentsPath, dbPath);
    }

    public OnlineEmailDownloader(EmailDownloaderDialog dialogue, String attachmentsPath, String dbPath) throws SQLException, NoSuchProviderException, MessagingException, IOException {

        this.attachmentsPath = attachmentsPath;
        this.emaildialogue = dialogue;
        this.dbPath = dbPath;
        this.m_bPause = false;
        
        objResume = new ResumeState();
        if(true) objResume.Activate();
        
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
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OnlineEmailDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(OnlineEmailDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(OnlineEmailDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void pauseDownloading()
    {
        this.m_bPause = true;
        logger.info("paused has been pressed");
    }

    
            
    @Override
    protected Void doInBackground() throws Exception, MessagingException, IOException, SQLException {

        int count = 0;
        String UID = "";
        if (isCancelled()) {
            emailFinished = false;
            return null;
        }

        // 1).  Create Data base 
        createDB();
        emaildialogue.getCancelButton().setEnabled(true);
        emaildialogue.getPauseButton().setEnabled(true);

        // 2).  Crawel For Each Folder 
        javax.mail.Folder[] folders = store.getDefaultFolder().list("*");
                
        for (javax.mail.Folder folder : folders) 
        {
            Message[] messages;
            
            if(!isFolderHoldMessages(folder)) continue;
            
            if(objResume.isActive() && (objResume.getFoundState()==false))
            {
                if(objResume.getFolderName().compareToIgnoreCase(folder.getFullName())!=0) continue;
                else 
                {
                    objResume.setFoundState(true);
                    
                    long iMsgId = (long)Integer.parseInt(objResume.getMessageId());
                    Message[] mCurrentMessages = folder.getMessages();
                    long lLastMsgId = (((UIDFolder) folder).getUID(mCurrentMessages[mCurrentMessages.length-1]));                 
                    
                    if (folder instanceof com.sun.mail.pop3.POP3Folder)
                        messages=((com.sun.mail.pop3.POP3Folder)folder).getMessages((int)iMsgId,(int)lLastMsgId);
                    else    
                        messages= ((com.sun.mail.imap.IMAPFolder)folder).getMessagesByUID(iMsgId,lLastMsgId);
                }
            }
            else                       
                messages = folder.getMessages();
          
            // 3).  Get All Messages For Each Folder

            if (messages != null)
            {

                // 4). For Each Message dump the message and download attachment
                for (Message message : messages) 
                {
                    
                    String str = String.valueOf(((UIDFolder) folder).getUID(message));
                    
                    com.sun.mail.imap.IMAPFolder pf =  (com.sun.mail.imap.IMAPFolder) folder;
               
                    long strUid = (pf.getUID(message));
                    logger.info(""+strUid+" "+folder.getFullName());    
                    //messages.toString();
                    if(this.m_bPause) {
                        
                    }
                        
                    if (isCancelled()) {
                        emailFinished = false;
                        return null;
                    }
                    try {
                        
                        // if pop3
                        if (folder instanceof com.sun.mail.pop3.POP3Folder) {
                            com.sun.mail.pop3.POP3Folder pf2 =
                                    (com.sun.mail.pop3.POP3Folder) folder;
                            UID = (pf2.getUID(message));
                        } else {
                            UID = String.valueOf(((UIDFolder) folder).getUID(message));

                        }
                       
                        // message id
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
                        // Print Debug Messages 
                        Utilities.PrintDebugMessages(sentDate, receiveDate, from, cclist, bcclist, body, subject);
                        // Save Attachment
                        List<String> Paths = getAttachments(message);
                        // Save In DBint id, String Username, String from, List<String> to, 
                        OnlineEmailMessage msg = OnlineEmailMessage.newInstance(messageId, this.Username,
                                from, to, bcclist, cclist, subject, body, sentDate.toString(), receiveDate.toString(), Paths, folder.getFullName());
                        db.inserteEmail(msg);
                        // Publish Data To Thread
                        ProgressData PData = new ProgressData(msg);
                        publish(PData);

                    } // Continue Crawling after Folder Closed Exception ** POP3 Only
                    catch (FolderClosedException ex) {
                        ex.printStackTrace();
                        ConnectPop3(Username, Password);
                        if (!folder.isOpen()) {
                            folder.open(Folder.READ_ONLY);
                            messages = folder.getMessages();
                        }
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                        if (!folder.isOpen()) {
                            ConnectPop3(Username, Password);
                            folder.open(Folder.READ_ONLY);
                            messages = folder.getMessages();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
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
            JOptionPane.showMessageDialog(emaildialogue, "Finished Downloading Emails", "Done", JOptionPane.INFORMATION_MESSAGE);
            emaildialogue.setVisible(false);
            emaildialogue.getDownloadBar().setIndeterminate(false);
        } else {
            JOptionPane.showMessageDialog(emaildialogue, "Cancelled Email Downloading", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            emaildialogue.setVisible(false);
        }

        emaildialogue.getDownloadBar().setIndeterminate(false);

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
            this.setEmailDialogElementsText(emaildialogue.getTo(),pd.mEmail.getTo());
            this.setEmailDialogElementsText(emaildialogue.getCC(), pd.mEmail.getCC());
            this.setEmailDialogElementsText(emaildialogue.getBCC(), pd.mEmail.getBCC());
            this.setEmailDialogElementsText(emaildialogue.getAttachments(), pd.mEmail.getAttachments());
        }
    }

    private void setEmailDialogElementsText(JTextComponent field, List<String> texts) {
        StringBuilder buffer = new StringBuilder();
        
        if ( !texts.isEmpty() ) {
            for (String text : texts) {
                buffer.append(Utilities.getEmptyStringWhenNullString(text)).append("\n");
            }
        }
        
        field.setText(buffer.toString());
    }
    
    public void pauseDownloading(boolean vbtrue)
    {
        
    }
    
    public boolean ConnectPop3(String UserName, String Password) {

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

    private List<String> getAttachments(Message temp) throws IOException, MessagingException {

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
            System.out.println("Number of Parts: " + multipart.getCount());

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
                System.out.println("file name" + filename);
                try {
                    FileUtil.saveObject(bodyPart.getInputStream(), filename, attachmentsPath);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
                s = Utilities.convertStreamToString(is);
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
