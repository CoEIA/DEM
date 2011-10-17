/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

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
    public static final String FILE_NAME = "file_name" ;
    public static final String FILE_TITLE = "file_title" ;
    public static final String FILE_CONTENT = "file_content" ;
    public static final String FILE_DATE = "file_date" ;
    public static final String FILE_CONTAINER = "file_container" ;
    public static final String FILE_ID = "file_id" ;
    public static final String FILE_PARENT_ID = "file_parent_id" ;
    public static final String FILE_MIME = "file_mime" ;
    public static final String FILE_HASH = "file_hash" ;
    
    /**
     * constants for chat sessions
     */
    public static final String CHAT_AGENT = "chat_agent" ;
    public static final String CHAT_FILE = "chat_file" ;
    public static final String CHAT_FROM = "chat_from" ;
    public static final String CHAT_TO = "chat_to" ;
    public static final String CHAT_TIME = "chat_time" ;
    public static final String CHAT_MESSAGE = "chat_message" ;
    public static final String CHAT_LENGTH = "chat_length" ;
    public static final String CHAT_MESSAGE_PATH = "chat_message_path" ;
    
    /**
     * constants for email
     */
}
