/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.items;

import edu.coeia.indexing.IndexingConstant;
import edu.coeia.cases.Case;
import edu.coeia.cases.CaseFacade;
import edu.coeia.searching.LuceneSearcher;
import static edu.coeia.indexing.IndexingConstant.* ;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;

/**
 *
 * @author wajdyessam
 */
public class ItemFactory {
    
    public static Item newInstance (final Document document, final CaseFacade caseFacade) {       
        if ( isFileDocument(document)) {
            return buildFileItem(document, caseFacade);
        }

        if ( isEmailDocument(document)) {
            return buildOnlineEmailItem(document);
        }

        if ( isOfflineEmailDocument(document)) {
            return buildOfflineEmailItem(document);
        }
        
        if ( isChatDocument(document)) {
            return buildChatItem(document);
        }
        
        if ( isImageDocument(document) ) {
            return buildImageItem(document, caseFacade);
        }
        
        throw new UnsupportedOperationException("There is no item for this type of document");
    }
    
    private static Item buildImageItem(final Document document, final CaseFacade caseFacade) {
        int documentId = Integer.parseInt(document.get(DOCUMENT_ID));
        int documentParentId = Integer.parseInt(document.get(DOCUMENT_PARENT_ID));
        String documentHash = document.get(DOCUMENT_HASH);
        
        String filePath = document.get(FILE_PATH);
        String fileName = document.get(FILE_NAME);
        String fileContent = document.get(FILE_CONTENT);
        String fileDate = document.get(FILE_DATE);
        String fileMime = document.get(FILE_MIME);
        String description = document.get(DOCUMENT_DESCRIPTION);
        String metadata = getMetadataForFile(documentId, caseFacade.getCase());
        
        filePath = caseFacade.getFullPath(filePath);
        
        FileItem item = new FileItem(documentId, documentParentId, documentHash, description,
            fileName, filePath, fileContent, fileDate, fileMime, metadata);
        
        return item;
    }
    
    private static Item buildFileItem(final Document document, final CaseFacade caseFacade) {
        int documentId = Integer.parseInt(document.get(DOCUMENT_ID));
        int documentParentId = Integer.parseInt(document.get(DOCUMENT_PARENT_ID));
        String documentHash = document.get(DOCUMENT_HASH);
        
        String filePath = document.get(FILE_PATH);
        String fileName = document.get(FILE_NAME);
        String fileContent = document.get(FILE_CONTENT);
        String fileDate = document.get(FILE_DATE);
        String fileMime = document.get(FILE_MIME);
        String description = document.get(DOCUMENT_DESCRIPTION);
        String metadata = getMetadataForFile(documentId, caseFacade.getCase());
        
        filePath = caseFacade.getFullPath(filePath);
        
        FileItem item = new FileItem(documentId, documentParentId, documentHash, description,
            fileName, filePath, fileContent, fileDate, fileMime, metadata);
        
        return item;
    }
    
    private static Item buildChatItem(final Document document) {
        int documentId = Integer.parseInt(document.get(DOCUMENT_ID));
        int documentParentId = Integer.parseInt(document.get(DOCUMENT_PARENT_ID));
        String documentHash = document.get(DOCUMENT_HASH);
        String description = document.get(DOCUMENT_DESCRIPTION);
        
        String chatFrom = document.get(CHAT_FROM);
        String chatTo = document.get(CHAT_TO);
        String chatTime = document.get(CHAT_TIME);
        String chatMessage = document.get(CHAT_MESSAGE);

        String chatAgent = document.get(CHAT_AGENT);
        String chatFile = document.get(CHAT_FILE);
        String chatLength = document.get(CHAT_LENGTH);
        String chatMessagePath = document.get(CHAT_MESSAGE_PATH);
        
        return new ChatItem(documentId, documentParentId, documentHash,description, 
            chatFrom, chatTo, chatTime, chatMessage,
            chatAgent, chatFile, chatLength, chatMessagePath);
    }
    
    private static Item buildOnlineEmailItem(final Document document) {
        int documentId = Integer.parseInt(document.get(DOCUMENT_ID));
        int documentParentId = Integer.parseInt(document.get(DOCUMENT_PARENT_ID));
        String documentHash = document.get(DOCUMENT_HASH);
        String description = document.get(DOCUMENT_DESCRIPTION);
        
        String emailSendDate = document.get(ONLINE_EMAIL_SENT_DATE);
        String emailFolderName = document.get(ONLINE_EMAIL_FOLDER_NAME);
        String emailSubject = document.get(ONLINE_EMAIL_SUBJECT);
        String emailFrom = document.get(ONLINE_EMAIL_FROM);
        String emailTo = document.get(ONLINE_EMAIL_TO);
        String emailId = document.get(ONLINE_EMAIL_MESSAGE_ID);
        boolean hasAttachment = Boolean.valueOf(document.get(ONLINE_EMAIL_ATTACHMENT_PATH));
        String user = document.get(ONLINE_EMAIL_USER_NAME);
        
        String content = document.get(ONLINE_EMAIL_BODY);
        String header = "";
        String cc = document.get(ONLINE_EMAIL_CC);
        String bcc = document.get(ONLINE_EMAIL_BCC);
        String plainContent = document.get(ONLINE_EMAIL_BODY);
        
        return new EmailItem(documentId, documentParentId, documentHash,description,
                emailFrom, emailTo, emailSubject, emailSendDate, emailFolderName, hasAttachment,user,
                content, header, cc, bcc, plainContent);
    }
    
    private static Item buildOfflineEmailItem(final Document document) {
        int documentId = Integer.parseInt(document.get(DOCUMENT_ID));
        int documentParentId = Integer.parseInt(document.get(DOCUMENT_PARENT_ID));
        String documentHash = document.get(DOCUMENT_HASH);
        String description = document.get(DOCUMENT_DESCRIPTION);
        
        String emailAgent = document.get(OFFLINE_EMAIL_FOLDER_NAME);
        String emailFolderName = document.get(OFFLINE_EMAIL_FOLDER_NAME);
        String emailSource = document.get(OFFLINE_EMAIL_NAME);
        String user = document.get(OFFLINE_EMAIL_PATH);
        
        String emailSendDate = document.get(OFFLINE_EMAIL_CLIENT_SUBMIT_TIME);
        String emailSubject = document.get(OFFLINE_EMAIL_SUBJECT);
        String emailFrom = document.get(OFFLINE_EMAIL_SENT_REPRESENTING_NAME);
        String emailTo = document.get(OFFLINE_EMAIL_DISPLAY_TO);
        boolean hasAttachment = Boolean.valueOf(document.get(ONLINE_EMAIL_ATTACHMENT_PATH));
        
        String content = document.get(OFFLINE_EMAIL_HTML_CONTENT);
        String plainContent = document.get(OFFLINE_EMAIL_PLAIN_CONTENT);
        String header = document.get(OFFLINE_EMAIL_HEADER);
        String cc = document.get(OFFLINE_EMAIL_DISPLAY_CC);
        String bcc = document.get(OFFLINE_EMAIL_DISPLAY_BCC);
        
        if ( user.trim().isEmpty() )
            user = emailSource;
        
        EmailItem item = new EmailItem(documentId, documentParentId, documentHash, description,
                emailFrom, emailTo, emailSubject, emailSendDate, emailFolderName, hasAttachment, user,
                content, header, cc, bcc, plainContent);
        
        return item;
    }
    
    private static String getMetadataForFile(int documentId, final Case aCase) {
        StringBuilder metadataBuilder = new StringBuilder();
        LuceneSearcher searcher = null;
        
        try {
            searcher = new LuceneSearcher(aCase);
            List<Fieldable> fields = searcher.getLuceneDocumentById(String.valueOf(documentId)).getFields();
            

            for (Fieldable field: fields) {
                if ( !field.name().startsWith("file_") &&
                     !field.name().equalsIgnoreCase(IndexingConstant.FILE_CONTENT)) // files in IndexingConstant start with prefix file_

                    metadataBuilder.append(field.name()).append(" : " ).append(field.stringValue()).append("\n");
            }
            
        } catch (Exception ex) {
            Logger.getLogger(ItemFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                searcher.closeSearcher();
            } catch (Exception ex) {
                Logger.getLogger(ItemFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return metadataBuilder.toString();
    }
}
