/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.extractors.ImageExtractor;
import edu.coeia.onlinemail.OnlineEmailMessage;
import edu.coeia.util.FilesPath;
import edu.coeia.onlinemail.OnlineEmailDBHandler;

import edu.coeia.util.Utilities;
import java.awt.EventQueue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;

import org.apache.tika.exception.TikaException;

/**
 *
 * @author Ahmed
 */
final class OnlineEmailIndexer extends Indexer {

    public OnlineEmailIndexer(LuceneIndex luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor) {
        super(luceneIndex, file, mimeType, imageExtractor);
        this.setParentId(0);
    }

    @Override
    public boolean doIndexing() {
        boolean status = false;

        try {
            OnlineEmailDBHandler db = new OnlineEmailDBHandler(this.getFile().getAbsolutePath());
            List<OnlineEmailMessage> AllMsgs = db.getAllMessages();

            for (OnlineEmailMessage msg : AllMsgs) {
                try {
                    Document doc = getDocument(msg);

                    if (doc != null) {
                        this.updateGUI(msg);
                        
                        int currentId = this.getId();
                        this.indexDocument(doc);

                        for (String sAttachments : msg.getAttachments()) {
                            File attachmentPath = new File(this.getCaseLocation() + "\\" + FilesPath.ATTACHMENTS + "\\" + sAttachments);
                            this.getLuceneIndex().indexFile(attachmentPath, currentId , this.getDialog());
                        }
                    }
                } 
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            
            status = true;
        } 
        catch (Exception ex) {
            throw new UnsupportedOperationException("Error in Email Reading");
        }

        return status;
    }

    private boolean indexDocument(final Document doc) throws CorruptIndexException, IOException {
        boolean status  = false;
        
        int objectId = this.getId();

        if (doc != null) {
            this.getLuceneIndex().getWriter().addDocument(doc);    // index file
            this.increaseId();      // increase the id counter if file indexed successfully

            // cache images with id as parent id
            if ( this.isImageCache() ) {
                this.getImageExtractor().extractImages(this, this.getFile(), objectId);
            }

            status = true;
        }
        
        return status;
    }
        
    private void updateGUI(final OnlineEmailMessage email) {
        EventQueue.invokeLater(new Runnable() { 
            @Override
            public void run() {
                String subject = email.getSubject();
                String date = email.getSentDate();
                boolean hasAttachment = !email.getAttachments().isEmpty();
                String folderName = email.getFolderName();
                String agent = email.getUsername();
                
                EmailCrawlingProgressPanel panel = new EmailCrawlingProgressPanel();
                panel.setAgentType(agent);
                panel.setEmailPath("");
                panel.setCurrentFolder(folderName);
                panel.setCurrentMessageSubject(subject);
                panel.setMessageDate(date);
                panel.setFrom(email.getFrom());
                panel.setTo(Utilities.getCommaSeparatedStringFromCollection(email.getTo()));
                panel.setHasAttachment(String.valueOf(hasAttachment));
                panel.setAttachment(email.getAttachments());
                getDialog().changeProgressPanel(panel);
            }
        });
    }
        
    public Document getDocument(OnlineEmailMessage msg) throws CorruptIndexException, IOException, FileNotFoundException, TikaException {
        Document doc = new Document();

        // generic document fields
        //TODO: make hash of message
        doc.add(new Field(IndexingConstant.DOCUMENT_PARENT_ID, String.valueOf(this.getParentId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_ID, String.valueOf(this.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT, IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.ONLINE_EMAIL), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_HASH, "", Field.Store.YES, Field.Index.NOT_ANALYZED));
        
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
}
