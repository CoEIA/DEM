/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.items;

import edu.coeia.cases.Case;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.searching.LuceneSearcher;
import static edu.coeia.indexing.IndexingConstant.* ;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;

/**
 *
 * @author wajdyessam
 */
public class ItemFactory {
    
    public static Item newInstance (final Document document, final Case aCase) {       
        if ( isFileDocument(document)) {
            return buildFileItem(document, aCase);
        }

        if ( isEmailDocument(document)) {
            return buildOnlineEmailItem(document);
        }

        if ( isOfflineEmailDocument(document)) {
            return buildOfflineEmailItem(document, aCase);
        }
        
        if ( isChatDocument(document)) {
            return buildChatItem(document);
        }
        
        throw new UnsupportedOperationException("There is no item for this type of document");
    }
    
    private static Item buildFileItem(final Document document, final Case aCase) {
        int documentId = Integer.parseInt(document.get(DOCUMENT_ID));
        int documentParentId = Integer.parseInt(document.get(DOCUMENT_PARENT_ID));
        String documentHash = document.get(DOCUMENT_HASH);
        
        String filePath = document.get(FILE_PATH);
        String fileName = document.get(FILE_NAME);
        String fileContent = document.get(FILE_CONTENT);
        String fileDate = document.get(FILE_DATE);
        String fileMime = document.get(FILE_MIME);
        
         String description = "File";
        
        if ( documentParentId != 0 ) {
            LuceneSearcher searcher;
            try {
                searcher = new LuceneSearcher(aCase);
                Document parentDocuemnt = searcher.getLuceneDocumentById(document.get(DOCUMENT_PARENT_ID));
                DOCUMENT_TYPE  type = IndexingConstant.getDocumentType(parentDocuemnt.get(IndexingConstant.DOCUMENT));
                
                if ( type == DOCUMENT_TYPE.OFFLINE_EMAIL ) 
                    description = "Email Attachment";
                else {
                    description = "File Embedded";
                }
                
            } catch (Exception ex) {
                Logger.getLogger(ItemFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        FileItem item = new FileItem(documentId, documentParentId, documentHash, 
            fileName, filePath, fileContent, fileDate, fileMime);
        
        item.setDescription(description);
        return item;
    }
    
    private static Item buildChatItem(final Document document) {
        int documentId = Integer.parseInt(document.get(DOCUMENT_ID));
        int documentParentId = Integer.parseInt(document.get(DOCUMENT_PARENT_ID));
        String documentHash = document.get(DOCUMENT_HASH);

        String chatFrom = document.get(CHAT_FROM);
        String chatTo = document.get(CHAT_TO);
        String chatTime = document.get(CHAT_TIME);
        String chatMessage = document.get(CHAT_MESSAGE);

        String chatAgent = document.get(CHAT_AGENT);
        String chatFile = document.get(CHAT_FILE);
        String chatLength = document.get(CHAT_LENGTH);
        String chatMessagePath = document.get(CHAT_MESSAGE_PATH);
        
        return new ChatItem(documentId, documentParentId, documentHash, 
            chatFrom, chatTo, chatTime, chatMessage,
            chatAgent, chatFile, chatLength, chatMessagePath);
    }
    
    private static Item buildOnlineEmailItem(final Document document) {
        int documentId = Integer.parseInt(document.get(DOCUMENT_ID));
        int documentParentId = Integer.parseInt(document.get(DOCUMENT_PARENT_ID));
        String documentHash = document.get(DOCUMENT_HASH);
        
        String emailSendDate = document.get(ONLINE_EMAIL_SENT_DATE);
        String emailFolderName = document.get(ONLINE_EMAIL_FOLDER_NAME);
        String emailSubject = document.get(ONLINE_EMAIL_SUBJECT);
        String emailFrom = document.get(ONLINE_EMAIL_FROM);
        String emailTo = document.get(ONLINE_EMAIL_TO);
        String emailId = document.get(ONLINE_EMAIL_MESSAGE_ID);
        boolean hasAttachment = Boolean.valueOf(document.get(ONLINE_EMAIL_ATTACHMENT_PATH));
        String user = document.get(ONLINE_EMAIL_USER_NAME);
        
        return new EmailItem(documentId, documentParentId, documentHash,
                emailFrom, emailTo, emailSubject, emailSendDate, emailFolderName, hasAttachment,user);
    }
    
    private static Item buildOfflineEmailItem(final Document document, final Case aCase) {
        int documentId = Integer.parseInt(document.get(DOCUMENT_ID));
        int documentParentId = Integer.parseInt(document.get(DOCUMENT_PARENT_ID));
        String documentHash = document.get(DOCUMENT_HASH);
        
        String emailSendDate = document.get(OFFLINE_EMAIL_CLIENT_SUBMIT_TIME);
        String emailFolderName = document.get(OFFLINE_EMAIL_FOLDER_NAME);
        String emailSubject = document.get(OFFLINE_EMAIL_SUBJECT);
        String emailFrom = document.get(OFFLINE_EMAIL_SENT_REPRESENTING_NAME);
        String emailTo = document.get(OFFLINE_EMAIL_DISPLAY_TO);
        String emailId = document.get(OFFLINE_EMAIL_ID);
        boolean hasAttachment = Boolean.valueOf(document.get(ONLINE_EMAIL_ATTACHMENT_PATH));
        String user = document.get(OFFLINE_EMAIL_PATH);
        
        String description = "Email Message";
        
        if ( documentParentId != 0 ) {
            LuceneSearcher searcher;
            try {
                searcher = new LuceneSearcher(aCase);
                Document parentDocuemnt = searcher.getLuceneDocumentById(document.get(DOCUMENT_PARENT_ID));
                String path = parentDocuemnt.get(OFFLINE_EMAIL_PATH);
                
                if ( path.endsWith(".ost") || path.endsWith(".OST") ||
                      path.endsWith(".pst") || path.endsWith(".PST") ) {
                    description = "Email Message";
                }
                else
                    description = "Email Attachment";
            } catch (Exception ex) {
                Logger.getLogger(ItemFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        EmailItem item = new EmailItem(documentId, documentParentId, documentHash,
                emailFrom, emailTo, emailSubject, emailSendDate, emailFolderName, hasAttachment, user);
        
        item.setDescription(description);
        return item;
    }
}
