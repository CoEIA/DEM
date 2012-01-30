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
import edu.coeia.chat.MSNMessage;
import edu.coeia.chat.MSNMessageReader.MSNChatSession;
import edu.coeia.extractors.NoneImageExtractor;

import java.io.File;

import org.apache.lucene.document.Document;

final class MSNChatIndexer extends Indexer {
    static final String CHAT_AGENT = "MSN";

    public static MSNChatIndexer newInstance(LuceneIndex luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor) {
        return new MSNChatIndexer(luceneIndex, file, mimeType, imageExtractor, 0);
    }

    private MSNChatIndexer(LuceneIndex luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor, int parentId) {
        super(luceneIndex, file, mimeType, imageExtractor);
        setParentId(parentId);
    }

    @Override
    public boolean doIndexing() {
        boolean status = false;

        try {
            MSNMessageReader reader = new MSNMessageReader();
            MSNChatSession session = reader.processFile(this.getFile());
            
           // this id for the XML file, each message will have this id as parent
            int currentDocumentId = this.getId();
            
            // index the document itslef, without text extraction
            NonDocumentIndexer.newInstance(this.getLuceneIndex(), this.getFile(), this.getMimeType(),
                    new NoneImageExtractor(), this.getParentId()).doIndexing();
            
            for(MSNMessage msg: session.getConversations()) {
                Document doc = LuceneDocumentBuilder.getDocument(this, msg, currentDocumentId, 
                        session.getUserName(), session.getOtherName(), session.getPath(), CHAT_AGENT); // add parentid and parent metadata here
                status = this.indexDocument(doc);
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return status;
    }
}
