/* 
 * Copyright (C) 2014 Center of Excellence in Information Assurance
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.coeia.items;

import edu.coeia.constants.IndexingConstant;
import edu.coeia.cases.Case;
import edu.coeia.cases.CaseFacade;
import edu.coeia.searching.LuceneSearcher;
import static edu.coeia.constants.IndexingConstant.* ;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author wajdyessam
 */
public class ItemFactory {
    
    public static Item newInstance (final Document document, final CaseFacade caseFacade
            , boolean readContent) {       
        if ( isFileDocument(document)) {
            return buildFileItem(document, caseFacade, readContent);
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
    
    private static Item buildFileItem(final Document document, final CaseFacade caseFacade,
            final boolean readContent) {
        int documentId = Integer.parseInt(document.get(DOCUMENT_ID));
        int documentParentId = Integer.parseInt(document.get(DOCUMENT_PARENT_ID));
        String documentHash = document.get(DOCUMENT_HASH);
        
        String filePath = document.get(FILE_PATH);
        String fileName = document.get(FILE_NAME);
        //String fileContent = document.get(FILE_CONTENT);
        
        String fileDate = document.get(FILE_DATE);
        String fileMime = document.get(FILE_MIME);
        String description = document.get(DOCUMENT_DESCRIPTION);
        String metadata = getMetadataForFile(documentId, caseFacade.getCase());
        
        filePath = caseFacade.getFullPath(filePath);
        
        String fileContent = "";
        
        if ( readContent ) {
            try {
                fileContent = getFullText(filePath);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        
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
    
    private static String getFullText(final String filepath) throws IOException, SAXException, TikaException {
        StringWriter writer = new StringWriter();
        
        final TikaInputStream inputStream =  TikaInputStream.get(new File(filepath));
        try {
            final Detector detector = new DefaultDetector();
            final Parser parser = new AutoDetectParser(detector);

            final Metadata metadata = new Metadata();
            final ParseContext parseContext = new ParseContext();
            parseContext.set(Parser.class, parser);

            
            ContentHandler contentHandler = new BodyContentHandler(writer);
            parser.parse(inputStream, contentHandler, metadata, parseContext);
        }
        finally {
            inputStream.close();
        }
        
        return writer.toString();
    }
}
