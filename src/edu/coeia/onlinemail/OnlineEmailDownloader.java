package edu.coeia.onlinemail;

import edu.coeia.util.FileUtil;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import static edu.coeia.util.PreconditionsChecker.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;


import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.NoSuchElementException;
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
import javax.mail.Message.RecipientType;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

class ProgressData {

    private String From;
    private String SentDate;
    private String CreationDate;
    private List<String> CC;
    private List<String> BCC;
    private String Attachments;
    private String Subject;
    private List<String> to;

    public ProgressData() {
    }

    public ProgressData(String From, List<String> To, String Subject, String SentDate,
            String CreationDate, List<String> CC, List<String> BCC, String Attachments) {
        this.From = From;
        this.SentDate = SentDate;
        this.CreationDate = CreationDate;
        this.CC = CC;
        this.BCC = BCC;
        this.Attachments = Attachments;
        this.Subject = Subject;
        this.to = To;

    }

    public String getSubject() {
        return this.Subject;
    }

    public String getTo() {

        String to = new String();
        for (String s : this.to) {
            to = s;
        }
        return to;
    }

    public String getFrom() {
        return this.From;
    }

    public String getSentDate() {
        return this.SentDate;
    }

    public String getCreationDate() {
        return this.CreationDate;
    }

    public List<String> getCC() {
        return this.CC;
    }

    public List<String> getBCC() {
        return this.BCC;
    }

    public String getAttachments() {
        return this.Attachments;
    }
}

/**
 * Email Reader - Read a Gmail Email using Java Mail API
 * and Store all the message in List for processing
 * @auther Wajdy Essam
 * @version 1.0 18-7-2011
 */
public class OnlineEmailDownloader extends SwingWorker<Void, ProgressData> {

    private EmailDownloaderDialog emaildialogue;
    private boolean emailFinished;
    private String dbPath;
    private String Username;
    private String Password;

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



    }

    /**
     * return Iterator to get EmailMesaages
     * throw NoSuchProviderException, MessagingException, IOException
     */
    public EmailIterator createIterator() throws NoSuchProviderException, MessagingException, IOException, SQLException {

        return new EmailIterator(this);
    }

    public void createDB() throws SQLException {

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

    @Override
    protected Void doInBackground() throws Exception, MessagingException, IOException, SQLException {

        int count = 0;
        if (isCancelled()) {
            emailFinished = false;
            return null;
        }
        // 1).  Create Data base 
        createDB();
        // 2).  Crawel For Each Folder 
        javax.mail.Folder[] folders = store.getDefaultFolder().list("*");

        for (javax.mail.Folder folder : folders) {

            if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
                try {
                    //System.out.println(folder.getFullName() + ": " + folder.getMessageCount());
                    folder.open(Folder.READ_ONLY);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else if (javax.mail.Folder.HOLDS_FOLDERS != 0) {
                continue;
            }
            // 3).  Get All Messages For Each Folder
            Message[] messages = folder.getMessages();

            if (messages != null) {

                // 4). For Each Message dump the message and download attachment
                for (Message message : messages) {
                    if (isCancelled()) {
                        emailFinished = false;
                        return null;
                    }

                    try {
                        // message id
                        int messageId = messages.length - count++;
                        
                        // sent and receive date
                        Date sentDate = checkDate(message.getSentDate());
                        Date receiveDate = checkDate(message.getReceivedDate());
                        
                        // Get cc, bcc, to list
                        List<String> cclist = getAddress(message, Message.RecipientType.CC);
                        List<String> bcclist = getAddress(message, Message.RecipientType.BCC);
                        List<String> to =   getAddress(message, Message.RecipientType.BCC);
                        
                        String ccBuilder = getFormattedString(cclist);
                        String bccBuilder = getFormattedString(bcclist);
                        String toBuilder = getFormattedString(to);
                       
                         
                        String  from = getFrom(message); 
                        String subject = formatInputString(message.getSubject());
                        String body = formatInputString(getText(message));
                        
                        // Print Debug Messages 
                        PrintDebugMessages(sentDate, receiveDate, from, cclist, bcclist, body, subject);
                        // Save Attachment
                        List<String> Paths = getAttachments(message);
                        String pathBuilder = getFormattedString(Paths);
                        // Save In DB
                        db.inserteEmail(messageId, from, toBuilder, this.Username, subject, body, sentDate.toString(), receiveDate.toString(), ccBuilder, bccBuilder, pathBuilder.toString(), folder.getFullName());
                        // Publish Data To Thread
                        ProgressData PData = new ProgressData(from, to, subject, sentDate.toString(), receiveDate.toString(),
                                cclist, bcclist, pathBuilder);
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
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }


            }

        }
        emailFinished = true;
        return null;
    }

    private Date checkDate(Date date) {
        if (date == null) {
            return new Date();
        }
        return date;
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

    private void PrintDebugMessages(Date sentDate, Date receiveDate, String from,
            List<String> ccList, List<String> bccList, String body, String Subject) {

        System.out.println("SentDate : " + sentDate);
        System.out.println("ReceiveDate : " + receiveDate);
        System.out.println("------------From------\n" + from);
        System.out.println();
        for (String d : ccList) {
            System.out.println("------------CC------\n" + d);
        }
        for (String s : bccList) {
            System.out.println("------------BCC------\n" + s);
        }
        System.out.println("------------Subject------\n" + Subject);
        System.out.println("------------Body------\n" + body);


    }

    @Override
    protected void done() {

        if (emailFinished) {

            JOptionPane.showMessageDialog(emaildialogue, "Finished Downloading Emails", "Done", JOptionPane.INFORMATION_MESSAGE);

            emaildialogue.setVisible(false);
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

        } else {
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
            JOptionPane.showMessageDialog(emaildialogue, "Cancelled Email Downloading", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
            emaildialogue.setVisible(false);
        }


    }

    @Override
    protected void process(List<ProgressData> chunks) {

        if (isCancelled()) {
            emailFinished = false;
            return;
        }

        for (ProgressData pd : chunks) {

            emaildialogue.getFrom().setText(formatInputString(pd.getFrom()));
            for (String s1 : pd.getCC()) {
                emaildialogue.getCC().setText(formatInputString(s1) + "\n");
            }
            for (String s2 : pd.getCC()) {
                emaildialogue.getBCC().setText(formatInputString(s2) + "\n");
            }
            emaildialogue.getSubject().setText(formatInputString(pd.getSubject()));
            emaildialogue.getTo().setText(pd.getTo());
            emaildialogue.getAttachments().setText(pd.getAttachments());
            emaildialogue.getSentDate().setText(pd.getSentDate());
            emaildialogue.getCreationDate().setText(pd.getCreationDate());
        }
    }

    private String formatInputString(String input) {
        String result = "";
        if (input != null) {
            result = input;
        }

        return result;
    }

    class EmailIterator {

        public EmailIterator(OnlineEmailDownloader reader) {
            this.reader = reader;
        }

        public void first() {
            this.iterator = this.reader.messageList.iterator();
            next();
        }

        public void next() {
            try {
                this.current = this.iterator.next();
            } catch (NoSuchElementException e) {
                this.current = null;
            }
        }

        public boolean hasNext() {
            return this.current == null;
        }

        public OnlineEmailMessage currentItem() {
            return this.current;
        }
        private OnlineEmailDownloader reader;
        private Iterator<OnlineEmailMessage> iterator;
        private OnlineEmailMessage current;
    }

    public void ConnectPop3(String UserName, String Password) throws NoSuchProviderException, MessagingException {
        Properties props = System.getProperties();
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.pop3.socketFactory.fallback", "false");
        props.setProperty("mail.pop3.port", "995");
        props.setProperty("mail.pop3.socketFactory.port", "995");

        Session session = Session.getDefaultInstance(props, null);
        store = session.getStore("pop3");
        store.connect("pop3.live.com", 995, UserName, Password);
        this.Username = UserName;
        this.Password = Password;
        /****************************/
        session.setDebug(true);
    }

    public void ConnectIMAP(String UserName, String Password) throws NoSuchProviderException, MessagingException {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.partialfetch", "false");
        Session session = Session.getDefaultInstance(props, null);
        store = session.getStore("imaps");
        store.connect("imap.gmail.com", UserName, Password);
        this.Username = UserName;
        this.Password = Password;

        /****************************/
        //session.setDebug(true);
    }

    private void ListAllFolders() throws MessagingException {
        Folder[] folder = store.getDefaultFolder().list("*");
        for (Folder fd : folder) {
            if ((fd.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
                System.out.println(fd.getFullName() + ": " + fd.getMessageCount());
            }
        }
    }

    String getFormattedString(List<String> list) {
        StringBuilder result = new StringBuilder();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                result.append(list.get(i));
                if (i < list.size() - 1) {
                    result.append(',');
                }
            }
        } else {

            return "";
        }
       return result.toString();
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

    private String getFromAddress(String fromAdd) {
        Pattern p = Pattern.compile("[A-Z0-9\\._%\\+\\-]+@[A-Z0-9\\.\\-]+\\.[A-Z]{2,4}", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(fromAdd);

        while (m.find()) {
            fromAdd = (m.group());
        }

        return fromAdd;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
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
                s = convertStreamToString(is);
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
//
//    private String getMessageContent(Message message) throws IOException, MessagingException {
//        Object content = null;
//        try {
//            content = message.getContent();
//        } catch (MessagingException ex) {
//            ex.printStackTrace();
//        }
//
//        StringBuilder messageContent = new StringBuilder();
//
//        if (content instanceof InputStream) {
//
//            InputStream is = (InputStream) content;
//            
//            FileUtil.saveObject(is, message.getFrom()[0].toString(), attachmentsPath);
//
//        } else if (content instanceof String) {
//            return content.toString();
//        } else if (content instanceof Multipart) {
//
//            Multipart multipart = (Multipart) content;
//
//            for (int i = 0; i < multipart.getCount(); i++) {
//                Part part = (Part) multipart.getBodyPart(i);
//
//                if (part.isMimeType("text/plain")) {
//                    messageContent.append(part.getContent().toString());
//                } else if (part.isMimeType("message/rfc822")) {
//                    messageContent.append(part.getContent().toString());
//                    messageContent.append('\n');
//                }
//            }
//        } else if (content instanceof InputStream) {
//            pr("This is just an input stream");
//            pr("---------------------------");
//            String filename = message.getFileName();
//            if (filename != null) {
//                InputStream is = (InputStream) content;
//                FileUtil.saveObject(is, filename, attachmentsPath);
//            }
//
//        }
//
//        return messageContent.toString();
//
//    }

  
    private Store store;
    private List<OnlineEmailMessage> messageList = new ArrayList<OnlineEmailMessage>();
    private List<String> attachments;
    private String attachmentsPath;
    public OnlineEmailDBHandler db;
    static String indentStr = "                                               ";
    static int level = 0;
    private boolean textIsHtml = false;
    static boolean showStructure = false;
    static boolean saveAttachments = false;
    static int attnum = 1;
}
