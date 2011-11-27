/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import com.pff.PSTException;
import edu.coeia.onlinemail.OnlineEmailDBHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import edu.coeia.onlinemail.OnlineEmailMessage;
import edu.coeia.util.FilesPath;
import org.apache.tika.exception.TikaException;

/**
 *
 * @author Ahmed
 */
final class EmailIndexer extends Indexer {
    
    public EmailIndexer (LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor) {
        super(luceneIndex, file, mimeType, imageExtractor);
    }

    @Override
    public boolean doIndexing() {
        boolean status = false;
        
        try {
            OnlineEmailDBHandler db = new OnlineEmailDBHandler(file.getAbsolutePath());
            List<OnlineEmailMessage> AllMsgs = db.getAllMessages();
            
            for (OnlineEmailMessage msg : AllMsgs) {
                System.out.println("Read Message, Subject: " + msg.getSubject() + " , Attachments No: " + msg.getAttachments().size());

                Document doc = getDocument(msg);
                
                if (doc != null) {
                    this.luceneIndex.getWriter().addDocument(doc);    // index file
                    id++;
                    
                    for (String sAttachments : msg.getAttachments()) {
                        System.out.println("Attachment: " + sAttachments);
                        
                        File attachmentPath = new File(this.caseLocation+"\\"+FilesPath.ATTACHMENTS+"\\"+sAttachments);
                        luceneIndex.indexFile(attachmentPath , id);
                    }
                } 
                else {
                    System.out.println("Fail Parsing: " + msg.getSubject());
                }
            }
            
            status = true;
        } 
        catch (Exception ex) {
            throw new UnsupportedOperationException("Error in Email Reading");
        }
        
        return status;
    }

    public Document getDocument(OnlineEmailMessage msg) throws CorruptIndexException, IOException, FileNotFoundException, TikaException, PSTException {

        Document doc = new Document();

        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_ID, String.valueOf(id), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_FOLDER_NAME, msg.getFolderName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_FROM, msg.getFrom(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_BODY, msg.getBody(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_SUBJECT, msg.getSubject(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_SENT_DATE, msg.getSentDate(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.ONLINE_EMAIL_RECIEVED_DATE, msg.getReceiveDate(), Field.Store.YES, Field.Index.NOT_ANALYZED));

        for (String sBcc : msg.getBCC()) {
            doc.add(new Field(IndexingConstant.ONLINE_EMAIL_BCC, sBcc, Field.Store.YES, Field.Index.NOT_ANALYZED));
        }

        for (String sCC : msg.getCC()) {
            doc.add(new Field(IndexingConstant.ONLINE_EMAIL_CC, sCC, Field.Store.YES, Field.Index.NOT_ANALYZED));
        }

        for (String sAttachments : msg.getAttachments()) {
            doc.add(new Field(IndexingConstant.ONLINE_EMAIL_ATTACHMENT_PATH, sAttachments, Field.Store.YES, Field.Index.NOT_ANALYZED));
      
        }
        return doc;
    }
}
