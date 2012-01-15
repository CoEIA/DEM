/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.chat.MSNMessageReader;
import edu.coeia.extractors.ImageExtractor;
import static edu.coeia.chat.MSNMessageReader.* ;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

final class MSNChatIndexer extends Indexer{
    
    private static final String CHAT_AGENT = "MSN" ;
    private int parentId ;
    
    /**
     * static factory method to get an instance of MSNIndexer
     */
    public static MSNChatIndexer newInstance(LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor) {
            
        return new MSNChatIndexer(luceneIndex, file, mimeType, imageExtractor, 0);
    }
    
    private MSNChatIndexer (LuceneIndex luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor,int parentId) {
        
        super(luceneIndex, file, mimeType, imageExtractor);
        this.parentId = parentId ;
    }
    
    @Override
    public boolean doIndexing() {
        boolean status = false;
        
        try {
           MSNChatSession session = MSNMessageReader.getMSNChatSession(this.getFile());

//            for(MSNChatSession session: sessions) {
//                for(MSNConversation conversation: session.conversations) {
//                    for(MSNMessage msg: conversation.messages) {
//                        
//                        Document doc = getDocument(msg,  session.userName, session.otherName , conversation.path); // add parentid and parent metadata here
//                        
//                        //int objectId = id;
//
//                        if (doc != null) {
//                            //this.getLuceneIndex().getWriter().addDocument(doc);    // index file
//                            //this.id++;                       // increase the id counter if file indexed successfully
//
//                        } else {
//                            System.out.println("Fail Parsing: " + this.getFile().getAbsolutePath());
//                            return false;
//                        }
//            
//                    }
//                }
//            }
        
            status = true;
        }
        catch(Exception e){
            throw new UnsupportedOperationException(e.getMessage());
        }

        return status;
    }
    
    private Document getDocument(MSNMessage msg, String profileName, String destinationName, String path) {
        Document doc = new Document();
        
        String from = profileName ;
        String to   = destinationName ;
        
        doc.add(new Field(IndexingConstant.CHAT_AGENT, CHAT_AGENT, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_FILE, path, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_FROM, from, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_TO, to, Field.Store.YES, Field.Index.NOT_ANALYZED));
        //doc.add(new Field(IndexingConstant.CHAT_TIME, Utilities.formatDateTime(msg.getTimeStamp()) , Field.Store.YES, Field.Index.NOT_ANALYZED));
        //doc.add(new Field(IndexingConstant.CHAT_MESSAGE, result.toString(), Field.Store.YES, Field.Index.ANALYZED));
        //doc.add(new Field(IndexingConstant.CHAT_LENGTH, String.valueOf(msg.getMessageLength()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        //doc.add(new Field(IndexingConstant.CHAT_MESSAGE_PATH, msg.getMessagePath().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        return doc;
    }
    
}
