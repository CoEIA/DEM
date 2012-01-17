/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.extractors.ImageExtractor;
import edu.coeia.util.FilesPath;
import edu.coeia.util.FileUtil;
import edu.coeia.util.Utilities;
import static edu.coeia.indexing.IndexingConstant.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import com.pff.PSTAttachment;
import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;
import com.pff.PSTRecipient;

import java.awt.EventQueue;

import java.util.ArrayList;
import java.util.List;

final class OutlookIndexer extends Indexer{
    
    private int depth = -1;
    
    /*
     * static factory method to get an instance of outlook indexer
     */
    public static OutlookIndexer newInstance (LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor) {
        return new OutlookIndexer(luceneIndex, file, mimeType, imageExtractor, 0);
    }

    public static OutlookIndexer newInstance (LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor, int parentId) {
        return new OutlookIndexer(luceneIndex, file, mimeType, imageExtractor, parentId);
    }
    
    private OutlookIndexer(LuceneIndex luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor, int parentId) {
        super(luceneIndex, file, mimeType, imageExtractor);
        this.setParentId(parentId);
    }
   
    @Override
    public boolean doIndexing() {
       boolean result = false;
       
        try {
            PSTFile pstFile = new PSTFile(this.getFile().getAbsolutePath());
            processOutlookFolder(pstFile.getRootFolder());
            result = true;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OutlookIndexer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PSTException ex) {
            Logger.getLogger(OutlookIndexer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OutlookIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
   
    private void processOutlookFolder(final PSTFolder folder) throws PSTException, IOException {
        depth++;
        String folderName = ""; // root folder has no name
        
        if (depth > 0) {
            folderName = folder.getDisplayName();
        }

        // get folders
        if (folder.hasSubfolders()) {
            Vector<PSTFolder> childFolders = folder.getSubFolders();
            for (PSTFolder childFolder : childFolders) {
                processOutlookFolder(childFolder);
            }
        }

        // get email inside folders
        if (folder.getContentCount() > 0) {
            depth++;
            PSTMessage email = (PSTMessage) folder.getNextChild();
            while (email != null) {
                Document document = this.getDocument(email, folderName);
                this.indexDocument(document);
                email = (PSTMessage) folder.getNextChild();
            }
            depth--;
        }
        depth--;
    }
    
    private void updateGUI(final PSTMessage email, final String folderName, final List<String> attachmentsName) {
        EventQueue.invokeLater(new Runnable() { 
            @Override
            public void run() {
                String subject = email.getSubject();
                String date  = Utilities.getEmptyStringWhenNullDate(email.getClientSubmitTime());
                boolean hasAttachment = email.hasAttachments();
                String sentRepresentingName = email.getSentRepresentingName();   
                String displayTo = email.getDisplayTo();
                
                EmailCrawlingProgressPanel panel = new EmailCrawlingProgressPanel();
                panel.setAgentType("Outlook");
                panel.setEmailPath(getFile().getAbsolutePath());
                panel.setCurrentFolder(folderName);
                panel.setCurrentMessageSubject(subject);
                panel.setMessageDate(date);
                panel.setHasAttachment(String.valueOf(hasAttachment));
                panel.setFrom(sentRepresentingName);
                panel.setTo(displayTo);
                panel.setAttachment(attachmentsName);
                getDialog().changeProgressPanel(panel);
            }
        });
    }
        
    private Document getDocument(final PSTMessage email, final String folderName) throws PSTException, IOException {
        int id = email.getDescriptorNode().descriptorIdentifier;
        String contentHTML = email.getBodyHTML();
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
        
        // generic document fields
        doc.add(new Field(DOCUMENT_ID, String.valueOf(this.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(DOCUMENT, getDocumentType(DOCUMENT_TYPE.OFFLINE_EMAIL), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(DOCUMENT_PARENT_ID, String.valueOf(this.getParentId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        //doc.add(new Field(DOCUMENT_HASH, HashCalculator.calculateFileHash(this.getFile().getAbsolutePath()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        // specific document fields
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_PATH, this.getPathHandler().getRelativePath(this.getFile().getAbsolutePath())));
        doc.add(getNotAnlyzedField(OFFLINE_EMAIL_NAME, this.getFile().getName()));
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
        
        // get the name of attchments
        List<String> attachmentsName = new ArrayList<String>();
        for(int i=0; i<numberOfAttachments; i++) {
            PSTAttachment attchment = email.getAttachment(i);
            String fileName = this.getId() + "-" + id + "-" + attchment.getLongFilename();
            
            if ( fileName.isEmpty() ) {
                String name = attchment.getFilename();
                if ( name.isEmpty()) continue;
                
                fileName =  this.getId() + "-" + id + "-" + name;
            }
            attachmentsName.add(fileName);
        }
        
        updateGUI(email, folderName, attachmentsName);
        
//        System.out.println("html content: " + contentHTML);
//        System.out.println("content plain: " + contentASCII);
//        System.out.println("id: " + id);
//        System.out.println("interned ID: " + internetId);
//        System.out.println("subject: " + subject);
//        System.out.println("action date: " + actionDate);
//        System.out.println("client submit time: " + clientSubmitTime);
//        System.out.println("delievery time: " + deliveryTime);
//        System.out.println("email address: " + emailAddress);
//        System.out.println("message header: " + messageHeader);
//        System.out.println("number of attachments: " + numberOfAttachments);
//        System.out.println("number of recipent: " + numberOfRecipent);
        
        for(int i=0; i<numberOfRecipent; i++) {
            PSTRecipient r = email.getRecipient(i);
            String name = r.getDisplayName();
            String address = r.getEmailAddress();
            String type = r.getEmailAddressType();
            String smptAddress = r.getSmtpAddress();
            
//            System.out.println("Recipent Name: " + name);
//            System.out.println("Recipent address: " + address);
//            System.out.println("Recipent type: " + type);
//            System.out.println("Recipent smpt: " + smptAddress);
            
            doc.add(getNotAnlyzedField(OFFLINE_EMAIL_RECIPENT_NAME, name));
            doc.add(getNotAnlyzedField(OFFLINE_EMAIL_RECIPENT_ADDRESS, address));
            doc.add(getNotAnlyzedField(OFFLINE_EMAIL_RECIPENT_TYPE, type));
            doc.add(getNotAnlyzedField(OFFLINE_EMAIL_RECIPENT_SMPT, smptAddress));
        }
        
        File storingPath = new File(this.getCaseLocation() + "\\" + FilesPath.OFFLINE_EMAIL_ATTACHMENTS);
        
        for(int i=0; i<numberOfAttachments; i++) {
            PSTAttachment attchment = email.getAttachment(i);
            String fileName = this.getId() + "-" + id + "-" + attchment.getLongFilename();
            
            if ( fileName.isEmpty() ) {
                String name = attchment.getFilename();
                if ( name.isEmpty()) continue;
                
                fileName =  this.getId() + "-" + id + "-" + name;
            }
            
            File attachmentPath = new File(storingPath, fileName);
            InputStream stream = attchment.getFileInputStream();
            FileUtil.saveObject(stream,attachmentPath.getAbsolutePath());
            stream.close();
            
            doc.add(getNotAnlyzedField(OFFLINE_EMAIL_ATTACHMENT_PATH, attachmentPath.getAbsolutePath()));
            
            // index attachment
            try {
                this.getLuceneIndex().indexFile(attachmentPath, this.getId(), this.getDialog());
                System.out.println("Index attchment: " + attachmentPath.getAbsolutePath());
            }
            catch(Exception e ) {
                System.out.println("cannot index attchment: " + attachmentPath);
            }
        }
                
//        System.out.println("message size: " + messageSize);
//        System.out.println("sent representing name: "+ sentRepresentingName);
//        System.out.println("sender address type: " + senderAddressType);
//        System.out.println("sender Email Address: " + senderEmailAddress );
//        System.out.println("sender name: " + senderName);
//        System.out.println("recieved by address: " +  recievedByAddress);
//        System.out.println("recieved by address type: " + recievedByAddressType);
//        System.out.println("recieved by name: " + recievedByName);
//        System.out.println("display to: " + displayTo);
//        System.out.println("display cc: " + displayCC);
//        System.out.println("display bcc: " + displayBCC);
//        System.out.println("has replied: " + hasReplied);
//        System.out.println("has forwared: " + hasForwarded);
//        System.out.println("has attachment: " + hasAttachment);
//        System.out.println("******************************************\n");
        
        return doc;
    }
    
    private Field getNotAnlyzedField(final String field, final String value) {
        return new Field(field,value, Field.Store.YES, Field.Index.NOT_ANALYZED);
    }
    
    private Field getAnalyzedField(final String field, final String value) {
        return new Field(field, value, Field.Store.YES, Field.Index.ANALYZED);
    }
}
