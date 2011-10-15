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
    
    public static final String FILE_NAME = "file_name" ;
    public static final String FILE_TITLE = "file_title" ;
    public static final String FILE_CONTENT = "file_content" ;
    public static final String FILE_DATE = "file_date" ;
    public static final String FILE_CONTAINER = "file_container" ;
    public static final String FILE_ID = "file_id" ;
    public static final String FILE_PARENT_ID = "file_parent_id" ;
    public static final String FILE_MIME = "file_mime" ;
    public static final String FILE_HASH = "file_hash" ;
    
}
