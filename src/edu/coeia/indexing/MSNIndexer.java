/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.util.Utilities;
import edu.coeia.chat.MSNMessageReader;
import static edu.coeia.chat.MSNMessageReader.* ;

import java.io.File;
import java.io.UnsupportedEncodingException;

import java.util.List ;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

public class MSNIndexer extends Indexer{
    
    /**
     *  chat type
     */
    private static final String CHAT_AGENT = "MSN" ;
    
    /**
     * static factory method to get an instance of MSNIndexer
     * 
     * @param writer
     * @param file
     * @param mimeType
     * @param imageCaching
     * @param caseLocation
     * @param imageExtractor
     * @return YahooChatIndexer
     */
    public static MSNIndexer newInstance(IndexWriter writer, File file, String mimeType, 
            boolean imageCaching, String caseLocation, ImageExtractor imageExtractor) {
            
        return new MSNIndexer(writer, file, mimeType, imageCaching, caseLocation, imageExtractor, 0);
    }
    
    
    private MSNIndexer (IndexWriter writer, File file, String mimeType, boolean imageCaching,
            String caseLocation, ImageExtractor imageExtractor,int parentId) {
        
        super(writer, file, mimeType, imageCaching, caseLocation, imageExtractor);
    }
    
    @Override
    public boolean doIndexing() {
        
        try {
           List<MSNChatSession> sessions = MSNMessageReader.getAllMSNChatSession(this.file.getAbsolutePath());

            for(MSNChatSession session: sessions) {
                for(MSNConversation conversation: session.conversations) {
                    for(MSNMessage msg: conversation.messages) {
                        
                        Document doc = getDocument(msg,  session.userName, session.otherName , conversation.path); // add parentid and parent metadata here
                        
                        //int objectId = id;

                        if (doc != null) {
                            this.writer.addDocument(doc);    // index file
                            //this.id++;                       // increase the id counter if file indexed successfully

                        } else {
                            System.out.println("Fail Parsing: " + file.getAbsolutePath());
                            return false;
                        }
            
                    }
                }
            }
        
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
    
    private Document getDocument(MSNMessage msg, String profileName, String destinationName, String path) {
        Document doc = new Document();
        
        String from = profileName ;
        String to   = destinationName ;
        
//        doc.add(new Field(IndexingConstant.CHAT_AGENT, CHAT_AGENT, Field.Store.YES, Field.Index.NOT_ANALYZED));
//        doc.add(new Field(IndexingConstant.CHAT_FILE, path, Field.Store.YES, Field.Index.NOT_ANALYZED));
//        doc.add(new Field(IndexingConstant.CHAT_FROM, from, Field.Store.YES, Field.Index.NOT_ANALYZED));
//        doc.add(new Field(IndexingConstant.CHAT_TO, to, Field.Store.YES, Field.Index.NOT_ANALYZED));
//        doc.add(new Field(IndexingConstant.CHAT_TIME, Utilities.formatDateTime(msg.getTimeStamp()) , Field.Store.YES, Field.Index.NOT_ANALYZED));
//        doc.add(new Field(IndexingConstant.CHAT_MESSAGE, result.toString(), Field.Store.YES, Field.Index.ANALYZED));
//        doc.add(new Field(IndexingConstant.CHAT_LENGTH, String.valueOf(msg.getMessageLength()), Field.Store.YES, Field.Index.NOT_ANALYZED));
//        doc.add(new Field(IndexingConstant.CHAT_MESSAGE_PATH, msg.getMessagePath().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        return doc;
    }
    
}