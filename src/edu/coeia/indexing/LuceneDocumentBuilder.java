/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import static edu.coeia.indexing.IndexingConstant.*;
import edu.coeia.chat.MSNMessage;
import edu.coeia.chat.SkypeMessage;
import edu.coeia.chat.YahooMessage;
import edu.coeia.chat.YahooMessageDecoder;
import edu.coeia.util.HashCalculator;
import edu.coeia.indexing.IndexingConstant.DOCUMENT_GENERAL_TYPE;
import edu.coeia.onlinemail.OnlineEmailMessage;
import edu.coeia.util.DateUtil;
import edu.coeia.util.FileUtil;
import edu.coeia.util.Utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pff.PSTException;
import com.pff.PSTMessage;
import com.pff.PSTRecipient;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 *
 * @author wajdyessam
 */
final class LuceneDocumentBuilder {
    
    private static boolean insideOfflineEmailAttachmentFolder(final Indexer indexer) {
        return indexer.getFile().getAbsolutePath().contains(indexer.getCaseFacade().getCaseOfflineEmailAttachmentLocation());
    }
    
    private static boolean insideOnlineEmailAttachmentFolder(final Indexer indexer) {
        return indexer.getFile().getAbsolutePath().contains(indexer.getCaseFacade().getCaseOnlineEmailAttachmentLocation());
    }
        
    // provide lucene document for images format (JPEG, PNG.. etc)
    public static Document getDocumentForImage(final Indexer indexer, Map<String, String> metadata) {
        
        Document doc = new Document();
        
        DOCUMENT_DESCRIPTION_TYPE type = indexer.getParentId() != 0 ? DOCUMENT_DESCRIPTION_TYPE.EMBEDDED_IMAGE: DOCUMENT_DESCRIPTION_TYPE.IMAGE;
        
        if ( insideOfflineEmailAttachmentFolder(indexer) || insideOnlineEmailAttachmentFolder(indexer) )
            type = DOCUMENT_DESCRIPTION_TYPE.EMAIL_ATTACHMENT;
        
        // generic lucene fileds
        doc.add(getNotAnlyzedField(DOCUMENT_TYPE, fromDocumentTypeToString(DOCUMENT_GENERAL_TYPE.IMAGE)));
        doc.add(getNotAnlyzedField(DOCUMENT_ID, String.valueOf(indexer.getId())));
        doc.add(getNotAnlyzedField(DOCUMENT_PARENT_ID, String.valueOf(indexer.getParentId())));
        doc.add(getNotAnlyzedField(DOCUMENT_HASH, HashCalculator.calculateFileHash(indexer.getFile().getAbsolutePath())));
        doc.add(getNotAnlyzedField(DOCUMENT_DESCRIPTION, fromDocumentTypeToString(type)));
        
        // specific document fields
        String path = indexer.getCaseFacade().getRelativePath(indexer.getFile().getPath());
        if ( path.isEmpty() )
            path = indexer.getFile().getAbsolutePath(); // this is for file inside TMP, we cannot get relative path for it

        doc.add(getNotAnlyzedField(FILE_PATH, path));
        doc.add(getNotAnlyzedField(FILE_NAME, indexer.getFile().getName()));
        doc.add(getNotAnlyzedField(FILE_DATE, DateTools.timeToString(indexer.getFile().lastModified(), DateTools.Resolution.MINUTE)));
        doc.add(getNotAnlyzedField(FILE_MIME, FileUtil.getExtension(indexer.getFile())));

        // unknown image metadata extracted by Tika
        for(Map.Entry<String, String> entry: metadata.entrySet()) {
            String name =  entry.getKey();
            String value = entry.getValue();
            
            doc.add(getAnalyzedField(name, value));
        }
        
        return doc;
    }
    
    public static Document getDocumentForFile(Indexer indexer, String content, Map<String, String> metadata) {
        Document doc = new Document();
        
        DOCUMENT_DESCRIPTION_TYPE type = indexer.getParentId() != 0 ? DOCUMENT_DESCRIPTION_TYPE.EMBEDDED_FILE: DOCUMENT_DESCRIPTION_TYPE.NORMAL_FILE;
        
        if ( insideOfflineEmailAttachmentFolder(indexer) || insideOnlineEmailAttachmentFolder(indexer) )
            type = DOCUMENT_DESCRIPTION_TYPE.EMAIL_ATTACHMENT;
                
        // generic lucene fileds
        doc.add(getNotAnlyzedField(DOCUMENT_TYPE, fromDocumentTypeToString(DOCUMENT_GENERAL_TYPE.FILE)));
        doc.add(getNotAnlyzedField(DOCUMENT_ID, String.valueOf(indexer.getId())));
        doc.add(getNotAnlyzedField(DOCUMENT_PARENT_ID, String.valueOf(indexer.getParentId())));
        doc.add(getNotAnlyzedField(DOCUMENT_HASH, HashCalculator.calculateFileHash(indexer.getFile().getAbsolutePath())));
        doc.add(getNotAnlyzedField(DOCUMENT_DESCRIPTION, fromDocumentTypeToString(type)));
        
        // specific document fields
        String path = indexer.getCaseFacade().getRelativePath(indexer.getFile().getPath());
        if ( path.isEmpty() )
            path = indexer.getFile().getAbsolutePath(); // this is for file inside TMP, we cannot get relative path for it

        doc.add(getNotAnlyzedField(FILE_PATH, path));
        doc.add(getNotAnlyzedField(FILE_NAME, indexer.getFile().getName()));
        doc.add(getNotAnlyzedField(FILE_DATE, DateTools.timeToString(indexer.getFile().lastModified(), DateTools.Resolution.MINUTE)));
        doc.add(getNotAnlyzedField(FILE_MIME, FileUtil.getExtension(indexer.getFile())));
        doc.add(getAnalyzedField(FILE_CONTENT, content));
        content = null;
        
        // unkown metadata extracted by Tika
        for(Map.Entry<String, String> entry: metadata.entrySet()) {
            String name =  Utilities.getEmptyStringWhenNullString(entry.getKey());
            String value = Utilities.getEmptyStringWhenNullString(entry.getValue());

            doc.add(getAnalyzedField(name, value));
        }
        
        return doc;
    }
    
    public static Document getDocumentForMSNMessage(
            Indexer indexer, MSNMessage msg, int parentId, 
            String profileName, String destinationName, String path, final String agent) {
            
        Document doc = new Document();

        // generic lucene fileds
        doc.add(getNotAnlyzedField(DOCUMENT_TYPE, fromDocumentTypeToString(DOCUMENT_GENERAL_TYPE.CHAT)));
        doc.add(getNotAnlyzedField(DOCUMENT_ID, String.valueOf(indexer.getId())));
        doc.add(getNotAnlyzedField(DOCUMENT_PARENT_ID, String.valueOf(parentId)));
        doc.add(getNotAnlyzedField(DOCUMENT_HASH, HashCalculator.calculateStringHash(msg.getMessage())));
        doc.add(getNotAnlyzedField(DOCUMENT_DESCRIPTION, fromDocumentTypeToString(DOCUMENT_DESCRIPTION_TYPE.NORMAL_CHAT)));
        
        // specific lucene fileds
        doc.add(getNotAnlyzedField(CHAT_AGENT, agent));
        doc.add(getNotAnlyzedField(CHAT_FILE, indexer.getCaseFacade().getRelativePath(path)));
        doc.add(getNotAnlyzedField(CHAT_FROM, msg.getFrom()));
        doc.add(getNotAnlyzedField(CHAT_TO, msg.getTo()));
        doc.add(getNotAnlyzedField(CHAT_TIME, msg.getDate()));
        doc.add(getNotAnlyzedField(CHAT_LENGTH, String.valueOf(msg.getMessage().length())));
        doc.add(getAnalyzedField(CHAT_MESSAGE, msg.getMessage()));
        
        return doc;
    }
    
    public static Document getDocumentForSkypeMessage(Indexer indexer, SkypeMessage msg, int parentId, final String agent) {
        Document doc = new Document();
        
        // generic lucene fileds
        doc.add(getNotAnlyzedField(DOCUMENT_TYPE, fromDocumentTypeToString(DOCUMENT_GENERAL_TYPE.CHAT)));
        doc.add(getNotAnlyzedField(DOCUMENT_ID, String.valueOf(indexer.getId())));
        doc.add(getNotAnlyzedField(DOCUMENT_PARENT_ID, String.valueOf(parentId)));
        doc.add(getNotAnlyzedField(DOCUMENT_HASH, HashCalculator.calculateStringHash(Utilities.getEmptyStringWhenNullString(msg.getMessage()))));
        doc.add(getNotAnlyzedField(DOCUMENT_DESCRIPTION, fromDocumentTypeToString(DOCUMENT_DESCRIPTION_TYPE.NORMAL_CHAT)));
        
        // specific lucene fileds
        doc.add(getNotAnlyzedField(CHAT_AGENT, agent));
        doc.add(getNotAnlyzedField(CHAT_FILE, indexer.getCaseFacade().getRelativePath(indexer.getFile().getAbsolutePath())));
        doc.add(getNotAnlyzedField(CHAT_FROM, msg.getFrom()));
        doc.add(getNotAnlyzedField(CHAT_TO, Utilities.getEmptyStringWhenNullString(msg.getTo())));
        doc.add(getNotAnlyzedField(CHAT_TIME, msg.getDate()));
        doc.add(getNotAnlyzedField(CHAT_LENGTH, String.valueOf(msg.getMessage().length())));
        doc.add(getAnalyzedField(CHAT_MESSAGE, Utilities.getEmptyStringWhenNullString(msg.getMessage())));
        
        return doc;
    }
    
    public static Document getDocumentForYahooMessage(Indexer indexer, YahooMessage msg, int parentId,
            String profileName, String destinationName, String path, final String agent) {
        Document doc = new Document();
        
        String from = profileName ;
        String to   = destinationName ;
        
        if ( msg.getMessagePath() == YahooMessage.MESSAGE_PATH.SOURCE_TO_DEST) {
            from = profileName;
            to = destinationName ;
        }
        else {
            from = destinationName ;
            to = profileName ;
        }
        
        StringBuilder result = new StringBuilder();
        try {
            byte[] plainText  = YahooMessageDecoder.decode(msg.getCipherText(),profileName);
            String plain = new String(plainText, "UTF-8");
            result.append(plain);
        }
        catch(UnsupportedEncodingException e) {
        }
        
        // generic lucene fileds
        doc.add(getNotAnlyzedField(DOCUMENT_TYPE, fromDocumentTypeToString(DOCUMENT_GENERAL_TYPE.CHAT)));
        doc.add(getNotAnlyzedField(DOCUMENT_ID, String.valueOf(indexer.getId())));
        doc.add(getNotAnlyzedField(DOCUMENT_PARENT_ID, String.valueOf(parentId)));
        doc.add(getNotAnlyzedField(DOCUMENT_HASH, HashCalculator.calculateStringHash(result.toString())));
        doc.add(getNotAnlyzedField(DOCUMENT_DESCRIPTION, fromDocumentTypeToString(DOCUMENT_DESCRIPTION_TYPE.NORMAL_CHAT)));
        
        // specific lucene fileds
        doc.add(getNotAnlyzedField(CHAT_AGENT, agent));
        doc.add(getNotAnlyzedField(CHAT_FILE, indexer.getCaseFacade().getRelativePath(path)));
        doc.add(getNotAnlyzedField(CHAT_FROM, from));
        doc.add(getNotAnlyzedField(CHAT_TO, to));
        doc.add(getNotAnlyzedField(CHAT_TIME, DateUtil.formatDateTime(msg.getTimeStamp())));
        doc.add(getNotAnlyzedField(CHAT_LENGTH, String.valueOf(msg.getMessageLength())));
        doc.add(getAnalyzedField(CHAT_MESSAGE, result.toString()));
        doc.add(getNotAnlyzedField(CHAT_MESSAGE_PATH,  msg.getMessagePath().toString()));
        
        return doc;
    }
    
    public static Document getDocumentForOnlineEmailMessage(Indexer indexer, OnlineEmailMessage msg) {
        Document doc = new Document();

        // generic document fields
        doc.add(getNotAnlyzedField(DOCUMENT_TYPE, fromDocumentTypeToString(DOCUMENT_GENERAL_TYPE.ONLINE_EMAIL)));
        doc.add(getNotAnlyzedField(DOCUMENT_ID, String.valueOf(indexer.getId())));
        doc.add(getNotAnlyzedField(DOCUMENT_PARENT_ID, String.valueOf(indexer.getParentId())));
        doc.add(getNotAnlyzedField(DOCUMENT_HASH, HashCalculator.calculateStringHash(msg.getBody())));
        doc.add(getNotAnlyzedField(DOCUMENT_DESCRIPTION, fromDocumentTypeToString(DOCUMENT_DESCRIPTION_TYPE.EMAIL_MESSAGE)));
        
        // specific document fields
        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_USER_NAME, msg.getUsername(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_MESSAGE_ID, String.valueOf(msg.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_FOLDER_NAME, msg.getFolderName(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_FROM, msg.getFrom(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_BODY, msg.getBody(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_SUBJECT, msg.getSubject(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_SENT_DATE, msg.getSentDate(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_RECIEVED_DATE, msg.getReceiveDate(), Field.Store.YES, Field.Index.NOT_ANALYZED));


        for (String item : msg.getTo()) {
            doc.add(new Field(IndexingConstant.ONLINE_EMAIL_TO, item, Field.Store.YES, Field.Index.ANALYZED));
        }

        for (String sBcc : msg.getBCC()) {
            doc.add(new Field(IndexingConstant.ONLINE_EMAIL_BCC, sBcc, Field.Store.YES, Field.Index.ANALYZED));
        }

        for (String sCC : msg.getCC()) {
            doc.add(new Field(IndexingConstant.ONLINE_EMAIL_CC, sCC, Field.Store.YES, Field.Index.ANALYZED));
        }

        for (String sAttachments : msg.getAttachments()) {
            doc.add(new Field(IndexingConstant.ONLINE_EMAIL_ATTACHMENT_PATH, sAttachments, Field.Store.YES, Field.Index.NOT_ANALYZED));
        }

        return doc;
    }
    
    public static Document getDocumentForOfflineEmailMessage(Indexer indexer, final PSTMessage email, 
            final String folderName, int parentId, final List<String> attachmentPaths) throws PSTException, IOException {
        int id = email.getDescriptorNode().descriptorIdentifier;
        String contentHTML = Utilities.getEmptyStringWhenNullString(email.getBodyHTML());
        String contentASCII = email.getBody();
        String internetId = email.getInternetMessageId();
        String subject = email.getSubject();
        
        Date actionDate = email.getActionDate();
        Date clientSubmitTime = email.getClientSubmitTime();
        Date deliveryTime = email.getMessageDeliveryTime();
        
        String emailAddress = email.getEmailAddress();
        String messageHeader = email.getTransportMessageHeaders();
        long messageSize = email.getMessageSize();
        int numberOfAttachments = email.getNumberOfAttachments();
        int numberOfRecipent = email.getNumberOfRecipients();
         
        String sentRepresentingName = email.getSentRepresentingName();       
        String senderAddressType = email.getSenderAddrtype();
        String senderEmailAddress = email.getSenderEmailAddress();
        String senderName = email.getSenderName();
        
        String recievedByAddress = email.getReceivedByAddress();
        String recievedByAddressType = email.getReceivedByAddressType();
        String recievedByName = email.getReceivedByName();
        
        String displayTo = email.getDisplayTo();
        String displayCC = email.getDisplayCC();
        String displayBCC = email.getDisplayBCC();
        
        boolean hasReplied = email.hasReplied();
        boolean hasForwarded = email.hasForwarded();
        boolean hasAttachment = email.hasAttachments();
        
        Document doc = new Document();
        
        String emailHash = contentHTML.trim().isEmpty() ? contentASCII: contentHTML;
        
        // generic document fields
        doc.add(getNotAnlyzedField(DOCUMENT_TYPE, fromDocumentTypeToString(DOCUMENT_GENERAL_TYPE.OFFLINE_EMAIL)));
        doc.add(getNotAnlyzedField(DOCUMENT_ID, String.valueOf(indexer.getId())));
        doc.add(getNotAnlyzedField(DOCUMENT_PARENT_ID, String.valueOf(indexer.getParentId())));
        doc.add(getNotAnlyzedField(DOCUMENT_HASH, HashCalculator.calculateStringHash(emailHash)));
        doc.add(getNotAnlyzedField(DOCUMENT_DESCRIPTION, fromDocumentTypeToString(DOCUMENT_DESCRIPTION_TYPE.EMAIL_MESSAGE)));
        
        // specific document fields
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_PATH, indexer.getCaseFacade().getRelativePath(indexer.getFile().getAbsolutePath())));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_NAME, indexer.getFile().getName()));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_FOLDER_NAME, folderName));
        
        doc.add(getAnalyzedField(OFFLINE_EMAIL_HTML_CONTENT, contentHTML));
        doc.add(getAnalyzedField(OFFLINE_EMAIL_PLAIN_CONTENT, contentASCII));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_ID, String.valueOf(id)));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_INTERNET_ID, internetId));
        doc.add(getAnalyzedField(OFFLINE_EMAIL_SUBJECT, subject));
        
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_ACTION_DATE, Utilities.getEmptyStringWhenNullDate(actionDate)));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_CLIENT_SUBMIT_TIME, Utilities.getEmptyStringWhenNullDate(clientSubmitTime)));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_DELIVERY_TIME, Utilities.getEmptyStringWhenNullDate(deliveryTime)));
        
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_ADDRESS, emailAddress));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_HEADER, messageHeader));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_SIZE, String.valueOf(messageSize)));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_NUMBER_OF_ATTACHMENT, String.valueOf(numberOfAttachments)));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_NUMBER_OF_RECIPENT, String.valueOf(numberOfRecipent)));
        
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_SENT_REPRESENTING_NAME, sentRepresentingName));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_SENDER_ADDRESS_TYPE, senderAddressType));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_SENDER_EMAIL_ADDRESS, senderEmailAddress));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_SENDER_NAME, senderName));
        
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_RECIEVED_BY_ADDRESS, recievedByAddress));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_RECIEVED_BY_ADDRESS_TYPE, recievedByAddressType));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_RECIEVED_BY_NAME, recievedByName));
        
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_DISPLAY_TO, displayTo));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_DISPLAY_CC, displayCC));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_DISPLAY_BCC, displayBCC));
        
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_HAS_REPLIED, String.valueOf(hasReplied)));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_HAS_FORWARDED, String.valueOf(hasForwarded)));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_HAS_ATTACHMENT, String.valueOf(hasAttachment)));
        
        for(int i=0; i<numberOfRecipent; i++) {
            PSTRecipient r = email.getRecipient(i);
            String name = r.getDisplayName();
            String address = r.getEmailAddress();
            String type = r.getEmailAddressType();
            String smptAddress = r.getSmtpAddress();

            doc.add(getNotAnlyzedField(OFFLINE_EMAIL_RECIPENT_NAME, name));
            doc.add(getNotAnlyzedField(OFFLINE_EMAIL_RECIPENT_ADDRESS, address));
            doc.add(getNotAnlyzedField(OFFLINE_EMAIL_RECIPENT_TYPE, type));
            doc.add(getNotAnlyzedField(OFFLINE_EMAIL_RECIPENT_SMPT, smptAddress));
        }
        
        for(String path: attachmentPaths)
            doc.add(getNotAnlyzedField(OFFLINE_EMAIL_ATTACHMENT_PATH, path));
        
        return doc;
    }
    
    private static Field getNotAnlyzedField(final String field, final String value) {
        return new Field(field, value, Field.Store.YES, Field.Index.NOT_ANALYZED);
    }
    
    private static Field getAnalyzedField(final String field, final String value) {
        return new Field(field, value, Field.Store.YES, Field.Index.ANALYZED);
    }
}
