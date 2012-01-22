/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.chat.SkypeMessage;
import edu.coeia.chat.SkypeMessageReader;
import edu.coeia.extractors.ImageExtractor;
import edu.coeia.extractors.NoneImageExtractor;
import edu.coeia.util.Tuple;

import java.io.File;

import java.util.List;

import org.apache.lucene.document.Document;

/**
 *
 * @author wajdyessam
 */
public class SkypeChatIndexer extends Indexer{
    private static final String CHAT_AGENT = "SKYPE" ;
    
    public static SkypeChatIndexer newInstance(LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor) {
        return new SkypeChatIndexer(luceneIndex, file, mimeType, imageExtractor, 0);
    }
        
    private SkypeChatIndexer(LuceneIndex luceneIndex, File file, String mimeType, ImageExtractor imageExtractor,
            int parentId) {
        super(luceneIndex, file, mimeType, imageExtractor);
        this.setParentId(parentId);
    }
        
    @Override
    public boolean doIndexing() {
        boolean status = false;
        
        try {     
            // this id for the .db file, each message will have this id as parent
            int currentDocumentId = this.getId();
            
            NonDocumentIndexer.newInstance(this.getLuceneIndex(), this.getFile(), this.getMimeType(),
                new NoneImageExtractor(), this.getParentId()).doIndexing();            
            
            SkypeMessageReader parser = new SkypeMessageReader();
            List<Tuple<String, List<SkypeMessage>>> msgs = parser.parseSkypeFile(this.getFile().getParent());

            for (Tuple<String, List<SkypeMessage>> user: msgs) {
                for (SkypeMessage msg: user.getB()) {
                    Document document = LuceneDocumentBuilder.getDocument(this, msg, currentDocumentId, CHAT_AGENT);
                    status = this.indexDocument(document);
                }
            }
        }
        catch(Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
        
        return status;
    }
}
