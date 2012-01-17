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
import edu.coeia.chat.YahooMessageReader;
import edu.coeia.chat.YahooMessageReader.YahooChatSession;
import edu.coeia.extractors.NoneImageExtractor;

import java.io.File;

import org.apache.lucene.document.Document;

final class YahooChatIndexer extends Indexer{
    private static final String CHAT_AGENT = "YAHOO" ;
    
    /**
     * static factory method to get an instance of YahooChatIndexer
     */
    public static YahooChatIndexer newInstance(LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor) {
        return new YahooChatIndexer(luceneIndex, file, mimeType, imageExtractor, 0);
    }
    
    private YahooChatIndexer(LuceneIndex luceneIndex, File file, String mimeType, ImageExtractor imageExtractor,
            int parentId) {
        super(luceneIndex, file, mimeType, imageExtractor);
        this.setParentId(parentId);
    }
    
    @Override
    public boolean doIndexing() {
        boolean status = false ;
        
        try {
            YahooMessageReader reader = new YahooMessageReader();
            YahooChatSession session = reader.processFile(this.getFile());
            
            // this id for the .dat file, each message will have this id as parent
            int currentDocumentId = this.getId();
            
            NonDocumentIndexer.newInstance(this.getLuceneIndex(), this.getFile(), this.getMimeType(),
                new NoneImageExtractor(), this.getParentId()).doIndexing();
            
            // then index the chat seesions in this file
            for(YahooMessage msg: session.messages) {
                Document document = LuceneDocumentBuilder.getDocument(this, msg, currentDocumentId, 
                        session.userName, session.otherName , session.path, CHAT_AGENT); // add parentid and parent metadata here
                status = this.indexDocument(document);
            }
        }
        catch(Exception e){
            throw new UnsupportedOperationException(e.getMessage());
        }

        return status;
    }
}
