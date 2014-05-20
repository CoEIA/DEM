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
package edu.coeia.constants;

import org.apache.lucene.document.Document;

/**
 *
 * @author wajdyessam
 */

/*
 * Lucene Constants 
 * using as field in any indexed document
 * work for: SIMPLE FILE, CONTAINER FILE, ARCHIVE FILE, IMAGE FILE, UNKNOWN FILE
 */
public class IndexingConstant {
  
    /*
     * Non-instabiable class
     */
    private IndexingConstant() {
        throw new AssertionError();
    }
    
    public static enum DOCUMENT_GENERAL_TYPE {
        FILE,
        CHAT,
        ONLINE_EMAIL,
        OFFLINE_EMAIL,
        IMAGE
    };
    
    public static enum DOCUMENT_DESCRIPTION_TYPE {
        NORMAL_FILE,
        EMBEDDED_FILE,
        NORMAL_CHAT,
        EMAIL_MESSAGE,
        EMAIL_ATTACHMENT,
        IMAGE,
        EMBEDDED_IMAGE
    };
    
    /**
     * return string representation for this enum
     */
    public static <E extends Enum<E>> String fromDocumentTypeToString(final E type) {
        return type.toString();
    }
    
    /**
     * return enum representation for this value
     * @param value
     * @return 
     */
    public static DOCUMENT_GENERAL_TYPE fromStringToDocumentGeneralType(final String value) {
        return DOCUMENT_GENERAL_TYPE.valueOf(value);
    }
    
    public static DOCUMENT_DESCRIPTION_TYPE fromStringToDocumentDescription(final String value) {
        return DOCUMENT_DESCRIPTION_TYPE.valueOf(value);
    }
    
    /**
     * constant for evidence path document
     * we used it to know if the evidence is changed or not
     */
    public static final String EVIDENCE_PATH = "EVIDENCE_PATH";
    
    /**
     * Constant for any Document in Lucene index
     */
    public static final String DOCUMENT_TYPE = "DOCUMENT" ;          // the general type of this document
    public static final String DOCUMENT_ID = "DOCUMENT_ID" ;    // id of the document in the index
    public static final String DOCUMENT_PARENT_ID = "DOCUMENT_PARENT_ID"; // id of the parent of this document
    public static final String DOCUMENT_HASH = "DOCUMENT_HASH" ; // md5 hash for any document in index
    public static final String DOCUMENT_DESCRIPTION = "DOCUMENT_DESCRIPTION";   // describe the specific type of this object
    
    /**
     * constants for all files and images
     */
    public static final String FILE_PATH = "FILE_PATH" ;
    public static final String FILE_NAME = "FILE_NAME" ;
    public static final String FILE_CONTENT = "FILE_CONTENT" ;
    public static final String FILE_DATE = "FILE_DATE" ;
    public static final String FILE_MIME = "FILE_MIME" ;
    
    /**
     * constants for chat sessions
     */
    public static final String CHAT_AGENT = "CHAT_AGENT" ;
    public static final String CHAT_FILE = "CHAT_FILE" ;
    public static final String CHAT_FROM = "CHAT_FROM" ;
    public static final String CHAT_TO = "CHAT_TO" ;
    public static final String CHAT_TIME = "CHAT_TIME" ;
    public static final String CHAT_MESSAGE = "CHAT_MESSAGE" ;
    public static final String CHAT_LENGTH = "CHAT_LENGTH" ;
    public static final String CHAT_MESSAGE_PATH = "CHAT_MESSAGE_PATH" ;
    
    /**
     * constants for online email
     */
    public static final String ONLINE_EMAIL_USER_NAME = "ONLINE_EMAIL_USER_NAME";
    public static final String ONLINE_EMAIL_FOLDER_NAME = "ONLINE_EMAIL_FOLDER_NAME";
    public static final String ONLINE_EMAIL_FROM = "ONLINE_EMAIL_FROM";
    public static final String ONLINE_EMAIL_TO = "ONLINE_EMAIL_TO";
    public static final String ONLINE_EMAIL_SUBJECT = "ONLINE_EMAIL_SUBJECT";
    public static final String ONLINE_EMAIL_BODY = "ONLINE_EMAIL_BODY";
    public static final String ONLINE_EMAIL_CC  = "ONLINE_EMAIL_CC";
    public static final String ONLINE_EMAIL_BCC = "ONLINE_EMAIL_BCC";
    public static final String ONLINE_EMAIL_RECIEVED_DATE = "ONLINE_EMAIL_RECIEVED_DATE";
    public static final String ONLINE_EMAIL_SENT_DATE = "ONLINE_EMAIL_SENT_DATE";
    public static final String ONLINE_EMAIL_ATTACHMENT_PATH = "ONLINE_EMAIL_ATTACHMENT_PATH";
    public static final String ONLINE_EMAIL_MESSAGE_ID = "ONLINE_EMAIL_ID";
    
    /**
     * constants for offline email
     */
    public static final String OFFLINE_EMAIL_PATH = "OFFLINE_EMAIL_PATH";
    public static final String OFFLINE_EMAIL_NAME = "OFFLINE_EMIAL_NAME";
    public static final String OFFLINE_EMAIL_FOLDER_NAME = "OFFLINE_EMAIL_FOLDER_NAME";
    public static final String OFFLINE_EMAIL_HTML_CONTENT = "OFFLINE_EMAIL_HTML_CONTENT";
    public static final String OFFLINE_EMAIL_PLAIN_CONTENT = "OFFLINE_EMAIL_PLAIN_CONTENT";
    public static final String OFFLINE_EMAIL_ID = "OFFLINE_EMAIL_ID";
    public static final String OFFLINE_EMAIL_INTERNET_ID = "OFFLINE_EMAIL_INTERNET_ID";
    public static final String OFFLINE_EMAIL_SUBJECT = "OFFLINE_EMAIL_SUBJECT";
    public static final String OFFLINE_EMAIL_ACTION_DATE = "OFFLINE_EMAIL_ACTION_DATE";
    public static final String OFFLINE_EMAIL_CLIENT_SUBMIT_TIME = "OFFLINE_EMAIL_CLIENT_SUBMIT_TIME";
    public static final String OFFLINE_EMAIL_DELIVERY_TIME = "OFFLINE_EMAIL_DELIVERY_TIME";
    public static final String OFFLINE_EMAIL_ADDRESS = "OFFLINE_EMAIL_ADDRESS";
    public static final String OFFLINE_EMAIL_HEADER = "OFFLINE_EMAIL_HEADER";
    public static final String OFFLINE_EMAIL_SIZE = "OFFLINE_EMAIL_SIZE";
    public static final String OFFLINE_EMAIL_NUMBER_OF_ATTACHMENT = "OFFLINE_EMAIL_NUMBER_OF_ATTACHMENT";
    public static final String OFFLINE_EMAIL_NUMBER_OF_RECIPENT = "OFFLINE_EMAIL_NUMBER_OF_RECIPENT";
    public static final String OFFLINE_EMAIL_SENT_REPRESENTING_NAME = "OFFLINE_EMAIL_SENT_REPRESENTING_NAME";
    public static final String OFFLINE_EMAIL_SENDER_ADDRESS_TYPE = "OFFLINE_EMAIL_SENDER_ADDRESS_TYPE";
    public static final String OFFLINE_EMAIL_SENDER_EMAIL_ADDRESS = "OFFLINE_EMAIL_SENDER_EMAIL_ADDRESS";
    public static final String OFFLINE_EMAIL_SENDER_NAME = "OFFLINE_EMAIL_SENDER_NAME";
    public static final String OFFLINE_EMAIL_RECIEVED_BY_ADDRESS = "OFFLINE_EMAIL_RECIEVED_BY_ADDRESS";
    public static final String OFFLINE_EMAIL_RECIEVED_BY_ADDRESS_TYPE = "OFFLINE_EMAIL_RECIEVED_BY_ADDRESS_TYPE";
    public static final String OFFLINE_EMAIL_RECIEVED_BY_NAME = "OFFLINE_EMAIL_RECIEVED_BY_NAME";
    public static final String OFFLINE_EMAIL_DISPLAY_TO = "OFFLINE_EMAIL_DISPLAY_TO";
    public static final String OFFLINE_EMAIL_DISPLAY_CC = "OFFLINE_EMAIL_DISPLAY_CC";
    public static final String OFFLINE_EMAIL_DISPLAY_BCC = "OFFLINE_EMAIL_DISPLAY_BCC";
    public static final String OFFLINE_EMAIL_HAS_REPLIED = "OFFLINE_EMAIL_HAS_REPLIED";
    public static final String OFFLINE_EMAIL_HAS_FORWARDED = "OFFLINE_EMAIL_HAS_FORWARDED";
    public static final String OFFLINE_EMAIL_HAS_ATTACHMENT = "OFFLINE_EMAIL_HAS_ATTACHMENT";
    public static final String OFFLINE_EMAIL_RECIPENT_NAME = "OFFLINE_EMAIL_RECIPENT_NAME";
    public static final String OFFLINE_EMAIL_RECIPENT_ADDRESS = "OFFLINE_EMAIL_RECIPENT_ADDRESS";
    public static final String OFFLINE_EMAIL_RECIPENT_TYPE = "OFFLINE_EMAIL_RECIPENT_TYPE";
    public static final String OFFLINE_EMAIL_RECIPENT_SMPT = "OFFLINE_EMAIL_RECIPENT_SMPT";
    public static final String OFFLINE_EMAIL_ATTACHMENT_PATH = "OFFLINE_EMAIL_ATTACHMENT_PATH";
       
    
    public static boolean isFileDocument(final Document document) {
        return document.get(IndexingConstant.DOCUMENT_TYPE)
                .equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_GENERAL_TYPE.FILE));
    }
    
    public static boolean isChatDocument(final Document document) {
        return document.get(IndexingConstant.DOCUMENT_TYPE)
                .equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_GENERAL_TYPE.CHAT));
    }
   
    public static boolean isEmailDocument(final Document document) {
        return document.get(IndexingConstant.DOCUMENT_TYPE)
                .equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_GENERAL_TYPE.ONLINE_EMAIL));
    }
    
    public static boolean isOfflineEmailDocument(final Document document) {
        return document.get(IndexingConstant.DOCUMENT_TYPE)
            .equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_GENERAL_TYPE.OFFLINE_EMAIL));
    }
    
    public static boolean isImageDocument(final Document document) {
        return document.get(IndexingConstant.DOCUMENT_TYPE)
                .equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_GENERAL_TYPE.IMAGE));
    }
}
