package edu.coeia.onlinemail;

import edu.coeia.util.FileUtil;
import static edu.coeia.util.PreconditionsChecker.* ;

import java.io.IOException;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.text.Normalizer;
import java.util.Arrays;
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
import javax.mail.internet.MimeUtility;

/**
 * Email Reader - Read a Gmail Email using Java Mail API
 * and Store all the message in List for processing
 * @auther Wajdy Essam
 * @version 1.0 18-7-2011
 */
public final class OnlineEmailReader {

    /**
     * static factory that read all message from user account and store it in list
     * @param username the name of the account
     * @param password the password of the account
     * @return EmailReader object that contain all the messages in list
     */
    public static OnlineEmailReader newInstance(final String userName, final String password,
            String attachmentsPath, String dbPath) throws SQLException {

        // check null values
        checkNull("username must be not null", userName);
        checkNull("password must be not null", password);
        checkNull("attachmentsPath must be not null", attachmentsPath);
        checkNull("dbPath must be not null", dbPath);
        
        // check empty string
        checkNotEmptyString("username must be not empty string", userName);
        checkNotEmptyString("password must be not empty string", password);
        checkNotEmptyString("attachmentsPath must be not empty string", attachmentsPath);
        checkNotEmptyString("dbPath must be not empty string", dbPath);

        return new OnlineEmailReader(userName, password, attachmentsPath, dbPath);
    }

    /**
     * private constructor to build EmailReader
     */
    private OnlineEmailReader(final String userName, final String password, String attachmentsPath, String dbPath) throws SQLException {
        this.userName = userName;
        this.password = password;
        this.attachmentsPath = attachmentsPath;

        FileUtil.createFolder(attachmentsPath);

        boolean create = OnlineEmailDBHandler.isDBExists(dbPath);

        if (create) {
            System.out.println("Open Exisiting DB");
        } else {
            System.out.println("Create new DB");
        }
        
        
        try {
            db = new OnlineEmailDBHandler(!create, dbPath);
            
        } catch (ClassNotFoundException ex) {
        } catch (InstantiationException ex) {
        } catch (SQLException ex) {
        } catch (IllegalAccessException ex) {
        }

    }

    /**
     * return Iterator to get EmailMesaages
     * throw NoSuchProviderException, MessagingException, IOException
     */
    public EmailIterator createIterator() throws NoSuchProviderException, MessagingException, IOException, SQLException {
        readMessages();
        return new EmailIterator(this);
    }

    class EmailIterator {

        public EmailIterator(OnlineEmailReader reader) {
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
        private OnlineEmailReader reader;
        private Iterator<OnlineEmailMessage> iterator;
        private OnlineEmailMessage current;
    }

    public void Connect() throws NoSuchProviderException, MessagingException {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imaps.partialfetch", "false");

        Session session = Session.getDefaultInstance(props, null);
        store = session.getStore("imaps");
        store.connect(PROTOCOL, userName, password);
    }

    private void ListAllFolders() throws MessagingException {
        folder = store.getDefaultFolder().list("*");
        for (Folder fd : folder) {
            if ((fd.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
                System.out.println(fd.getFullName() + ": " + fd.getMessageCount());
            }
        }
    }

    public static List<OnlineEmailMessage> getAllMessages() throws SQLException {
        String select = "SELECT * FROM emails ";
        Statement statement = OnlineEmailDBHandler.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(select);
        
        List<OnlineEmailMessage> mEmails = new ArrayList<OnlineEmailMessage>();
        
        OnlineEmailMessage message = null;

        while (resultSet.next()) {

            String paths = resultSet.getString("PATH");
            String[] arrPaths = paths.split(",");
            List<String> listPaths = Arrays.asList(arrPaths);

            String bcc = resultSet.getString("BCC");
            String[] bccArray = bcc.split(",");
            List<String> bccList = Arrays.asList(bccArray);

            String cc = resultSet.getString("CC");
            String[] ccArray = bcc.split(",");
            List<String> ccList = Arrays.asList(ccArray);

            message = OnlineEmailMessage.newInstance(resultSet.getInt("EMAILID"), resultSet.getString("FROM_ADDRESS"), bccList, ccList, resultSet.getString("SUBJECT"),
                    resultSet.getString("BODY_MESSAGE"), resultSet.getString("SENT_DATE"), resultSet.getString("CREATED_DATE"), listPaths);

            mEmails.add(message);
        }
        
        resultSet.close();
        statement.close();

        return mEmails;
    }

    public void readMessages() throws NoSuchProviderException, MessagingException, IOException, SQLException {

        int count = 0;

        javax.mail.Folder[] folders = store.getDefaultFolder().list("*");
        for (javax.mail.Folder folder : folders) {

            if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {

                System.out.println(folder.getFullName() + ": " + folder.getMessageCount());
                folder.open(Folder.READ_WRITE);

            }

            Message[] messages = folder.getMessages();

            for (Message message : messages) {

                // Get cc
                List<String> cclist = getAddress(message, Message.RecipientType.BCC);
                String ccBuilder = getFormattedString(cclist);

                // Get Bcc
                List<String> bcclist = getAddress(message, Message.RecipientType.CC);
                String bccBuilder = getFormattedString(bcclist);

                int messageId = messages.length - count++;
                String from = getFromAddress(message.getFrom()[0].toString());
                String subject = message.getSubject();
                String body = getMessageContent(message);
                Date sentDate = message.getSentDate();
                Date receiveDate = message.getReceivedDate();

                System.out.println("SentDate : " + sentDate);
                System.out.println("ReceiveDate : " + receiveDate);
                System.out.println("------------From------\n" + from);
                System.out.println();
                for (String d : cclist) {

                    System.out.println("------------CC------\n" + d);

                }
                for (String s : bcclist) {
                    System.out.println("------------BCC------\n" + s);

                }
                System.out.println("------------Subject------\n" + subject);
                System.out.println("------------Body------\n" + body);

                // Save Attachment
                List<String> Paths = getAttachments(message);
                String pathBuilder = getFormattedString(Paths);

                // Save Message in DB 
                db.inserteEmail(messageId, from, subject, body, sentDate.toString(), receiveDate.toString(), ccBuilder, bccBuilder, pathBuilder.toString());

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
        Address[] recipients = message.getRecipients(type);

        if (recipients != null) {
            for (Address address : recipients) {
                addressList.add(address.toString());
            }
        }

        return addressList;
    }

    private List<String> getAttachments(Message temp) throws IOException, MessagingException {

        attachments = new ArrayList<String>();

        Object objRef = temp.getContent();
        if (!(objRef instanceof Multipart)) {
            return Collections.emptyList();
        }

        Multipart multipart = (Multipart) temp.getContent();
        System.out.println("Number of Attachments: " + multipart.getCount());

        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                continue; // dealing with attachments only
            }

            String decoded = MimeUtility.decodeText(bodyPart.getFileName());

            String filename = Normalizer.normalize(decoded, Normalizer.Form.NFC);

            System.out.println("file name" + filename);
            FileUtil.saveObject(bodyPart.getInputStream(), filename, attachmentsPath);


            attachments.add(filename);

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

    private String getMessageContent(Message message) throws IOException, MessagingException {
        Object content = message.getContent();

        if (content instanceof Multipart) {
//            StringBuilder messageContent = new StringBuilder();
//            Multipart multipart = (Multipart) content;
//
//            for (int i = 0; i < multipart.getCount(); i++) {
//                Part part = (Part) multipart.getBodyPart(i);
//
//                messageContent.append(part.getContent().toString());




            return " ";

        } else {
            return content.toString();
        }
    }
    
    
    private final String userName;
    private final String password;
    private static final String PROTOCOL = "imap.gmail.com";
    private Store store;
    private Folder[] folder;
    private List<OnlineEmailMessage> messageList = new ArrayList<OnlineEmailMessage>();
    private List<String> attachments;
    private String attachmentsPath;
    private String dbPath;
    private OnlineEmailDBHandler db;
}
