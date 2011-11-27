/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import java.util.ArrayList;
import java.util.List;

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
    
    /**
     * constants for all files and images
     */
    public static final String FILE_NAME = "FILE_NAME" ;
    public static final String FILE_TITLE = "FILE_TITLE" ;
    public static final String FILE_CONTENT = "FILE_CONTENT" ;
    public static final String FILE_DATE = "FILE_DATE" ;
    public static final String FILE_CONTAINER = "FILE_CONTAINER" ;
    public static final String FILE_ID = "FILE_ID" ;
    public static final String FILE_PARENT_ID = "FILE_PARENT_ID" ;
    public static final String FILE_MIME = "FILE_MIME" ;
    public static final String FILE_HASH = "FILE_HASH" ;
    
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
    public static final String ONLINE_EMAIL_ID = "ONLINE_EMAIL_ID";
    
}
