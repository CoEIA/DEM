/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.extractors.ImageExtractor;
import edu.coeia.chat.YahooMessage;
import edu.coeia.chat.YahooMessageDecoder;
import edu.coeia.chat.YahooMessageReader;
import edu.coeia.chat.YahooMessageReader.YahooConversation;
import edu.coeia.chat.YahooMessageReader.YahooChatSession;
import edu.coeia.util.DateUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;

import java.util.List ;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public final class YahooChatIndexer extends Indexer{
    
    /**
     *  chat type
     */
    private static final String CHAT_AGENT = "YAHOO" ;
    private int parentId ;
    
    /**
     * static factory method to get an instance of YahooChatIndexer
     * 
     * @param writer
     * @param file
     * @param mimeType
     * @param imageCaching
     * @param caseLocation
     * @param imageExtractor
     * @return YahooChatIndexer
     */
    public static YahooChatIndexer newInstance(LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor) {
            
        return new YahooChatIndexer(luceneIndex, file, mimeType, imageExtractor, 0);
    }
    
    /**
     * index file object with parentid
     * 
     * @param writer
     * @param file
     * @param mimeType
     * @param imageCaching
     * @param caseLocation
     * @param imageExtractor
     * @param parentId 
     */
    private YahooChatIndexer(LuceneIndex luceneIndex, File file, String mimeType, ImageExtractor imageExtractor,
            int parentId) {
        
        super(luceneIndex, file, mimeType, imageExtractor);
        this.parentId = parentId ;
    }
    
    @Override
    public boolean doIndexing() {
        boolean status = false ;
        
        try {
            YahooMessageReader reader = new YahooMessageReader();
            List<YahooChatSession> sessions = reader.getAllYahooChatSession(this.getFile().getAbsolutePath());
            
            this.increaseId();
            
            // then index the chat seesions in this file
            for(YahooChatSession session: sessions) {
                for(YahooConversation conversation: session.conversations) {
                    // index the .DAT file first
                    String datPath = conversation.path ;
                    this.parentId = this.getId();
                    this.getLuceneIndex().indexFile(new File(datPath), parentId);
                
                    for(YahooMessage msg: conversation.messages) {
                        Document doc = getDocument(msg,  session.userName, session.otherName , conversation.path); // add parentid and parent metadata here
                       
                        if (doc != null) {
                            this.getLuceneIndex().getWriter().addDocument(doc);    // index file
                            this.increaseId();                       // increase the id counter if file indexed successfully

                        } else {
                            System.out.println("Fail Parsing: " + this.getFile().getAbsolutePath());
                            return false;
                        }
            
                    }
                }
            }
        
            status = true;
        }
        catch(Exception e){
            throw new UnsupportedOperationException(e.getMessage());
        }

        return status;
    }
    
    private Document getDocument(YahooMessage msg, String profileName, String destinationName, String path) {
        Document doc = new Document();
        
        String from = profileName ;
        String to   = destinationName ;
        
        if ( msg.getMessagePath() == YahooMessage.MESSAGE_PATH.SOURCE_TO_DEST) {
            from = profileName;
            to = destinationName ;
        }
        else {
            from = destinationName ;
            to = profileName ;
        }
        
        StringBuilder result = new StringBuilder();
        try {
            byte[] plainText  = YahooMessageDecoder.decode(msg.getCipherText(),profileName);
            String plain = new String(plainText, "UTF-8");
            result.append(plain);
        }
        catch(UnsupportedEncodingException e) {
        }
        
        // genric lucene fileds
        doc.add(new Field(IndexingConstant.DOCUMENT, IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.CHAT), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_ID, String.valueOf(this.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_PARENT_ID, String.valueOf(this.parentId), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        // specific lucene fileds
        doc.add(new Field(IndexingConstant.CHAT_AGENT, CHAT_AGENT, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_FILE, path, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_FROM, from, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_TO, to, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_TIME, DateUtil.formatDateTime(msg.getTimeStamp()) , Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_MESSAGE, result.toString(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_LENGTH, String.valueOf(msg.getMessageLength()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_MESSAGE_PATH, msg.getMessagePath().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        
        
        return doc;
    }
}
