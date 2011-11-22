/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import com.pff.PSTException;
import edu.coeia.cases.Case;
import edu.coeia.email.EmailReader;
import edu.coeia.onlinemail.OnlineEmailDBHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import edu.coeia.onlinemail.OnlineEmailMessage;
import edu.coeia.onlinemail.OnlineEmailReader;
import edu.coeia.util.FilesPath;
import java.util.Collections;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
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

        List<OnlineEmailMessage> AllMsgs = Collections.EMPTY_LIST;
        OnlineEmailDBHandler db = null;
        try {
            db = new OnlineEmailDBHandler(file.getAbsolutePath());
            AllMsgs = db.getAllMessages();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EmailIndexer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(EmailIndexer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EmailIndexer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(EmailIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        for (OnlineEmailMessage msg : AllMsgs) {
            Document doc = null;
            try {
                System.out.println("msg from: " + msg.toString());
                
                doc = getDocument(msg);
                if (doc != null) {
                
                this.luceneIndex.getWriter().addDocument(doc);    // index file
                this.id++;
                
               
                String attachmentPath = this.luceneIndex.getCase().getCaseLocation() + "\\" + FilesPath.ATTACHMENTS;
                for (String sAttachments : msg.getAttachments()) {
                    System.out.println("attachments: " + sAttachments);
                luceneIndex.indexFile(new File(attachmentPath + "\\"+sAttachments), msg.getId());
                }
             
                
            } else {
                System.out.println("Fail Parsing");
                return false;
            }
                
          
            } catch (CorruptIndexException ex) {
                Logger.getLogger(EmailIndexer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(EmailIndexer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TikaException ex) {
                Logger.getLogger(EmailIndexer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PSTException ex) {
                Logger.getLogger(EmailIndexer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
          
        }
        
        return true;
    
    }

    public Document getDocument(OnlineEmailMessage msg) throws CorruptIndexException, IOException, FileNotFoundException, TikaException, PSTException {

        Document doc = new Document();

        doc.add(new Field(IndexingConstant.OnlineEmail_Id, String.valueOf(this.id), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.OnlineEmail_FolderName, msg.getFolderName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.OnlineEmail_From, msg.getFrom(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.OnlineEmail_Body, msg.getBody(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.OnlineEmail_Subject, msg.getSubject(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.OnlineEmail_SentDate, msg.getSentDate(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.OnlineEmail_ReceivedDate, msg.getReceiveDate(), Field.Store.YES, Field.Index.NOT_ANALYZED));

        for (String sBcc : msg.getBCC()) {
            doc.add(new Field(IndexingConstant.OnlineEmail_BCC, sBcc, Field.Store.YES, Field.Index.NOT_ANALYZED));
        }

        for (String sCC : msg.getCC()) {
            doc.add(new Field(IndexingConstant.OnlineEmail_CC, sCC, Field.Store.YES, Field.Index.NOT_ANALYZED));
        }

        for (String sAttachments : msg.getAttachments()) {
            doc.add(new Field(IndexingConstant.OnlineEmail_AttachmentsPath, sAttachments, Field.Store.YES, Field.Index.NOT_ANALYZED));
      
        }
        return doc;
    }
}
