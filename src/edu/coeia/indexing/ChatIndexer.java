/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.chat.ChatMessage;
import edu.coeia.chat.ChatReader;
import edu.coeia.chat.ChatSession;
import edu.coeia.chat.MSNChatReader;
import edu.coeia.chat.MSNMessage;
import edu.coeia.chat.SkypeMessage;
import edu.coeia.chat.SkypeChatReader;
import edu.coeia.chat.YahooChatReader;
import edu.coeia.chat.YahooMessage;
import edu.coeia.extractors.ImageExtractor;
import edu.coeia.extractors.NoneImageExtractor;

import java.io.File;

import org.apache.lucene.document.Document;

final class ChatIndexer extends Indexer{
    private final CHAT_TYPE type;
    
    public static enum CHAT_TYPE { MSN, YAHOO, SKYPE };
    
    public static ChatIndexer newInstance(IndexerManager luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor, CHAT_TYPE type) {
        return new ChatIndexer(luceneIndex, file, mimeType, imageExtractor, type, 0);
    }
        
    private ChatIndexer(IndexerManager luceneIndex, File file, String mimeType, ImageExtractor imageExtractor,
            CHAT_TYPE type, int parentId) {
        super(luceneIndex, file, mimeType, imageExtractor);
        this.type = type;
        this.setParentId(parentId);
    }
        
    @Override
    public boolean doIndexing() {
        boolean status = false;
        
        try {     
            int currentDocumentId = this.getId();
            
            NonDocumentIndexer.newInstance(this.getLuceneIndex(), this.getFile(), this.getMimeType(),
                new NoneImageExtractor(), this.getParentId()).doIndexing();            
            
            ChatReader reader = getChatReader();
            ChatSession session = reader.processFile(this.getFile());
            
            for(ChatMessage chatMessage: session.getConversations()) {
                Document document = getDocument(session, chatMessage, currentDocumentId);
                status = this.indexDocument(document);
            }
                        
        }
        catch(Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
        
        return status;
    }
    
    private Document getDocument(final ChatSession session,
            final ChatMessage chatMessage, int currentDocumentId)  {
        
        if ( this.type == CHAT_TYPE.MSN ) {
            MSNMessage msg = (MSNMessage) chatMessage;
            Document document = LuceneDocumentBuilder.getDocumentForMSNMessage(this, msg, currentDocumentId, 
                session.getUserName(), session.getOtherName(), session.getPath(), type.name()); // add parentid and parent metadata here
            
            return document;
        }
        else if ( this.type == CHAT_TYPE.YAHOO ) {
            YahooMessage msg = (YahooMessage) chatMessage;
            Document document = LuceneDocumentBuilder.getDocumentForYahooMessage(this, msg, currentDocumentId, 
                session.getUserName(), session.getOtherName() , session.getPath(), type.name()); // add parentid and parent metadata here
            
            return document;
        }

        SkypeMessage msg = (SkypeMessage) chatMessage;
        Document document = LuceneDocumentBuilder.getDocumentForSkypeMessage(this, msg, currentDocumentId, type.name());
        return document;
    }
    
    private ChatReader getChatReader() {
        if ( this.type == CHAT_TYPE.MSN ) 
            return new MSNChatReader();
        else if ( this.type == CHAT_TYPE.YAHOO )
            return new YahooChatReader();
        else 
            return new SkypeChatReader();
    }
}
