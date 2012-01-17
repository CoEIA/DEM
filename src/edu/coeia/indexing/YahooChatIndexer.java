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
import edu.coeia.chat.YahooMessageReader.YahooChatSession;
import edu.coeia.extractors.NoneImageExtractor;
import edu.coeia.hash.HashCalculator;
import edu.coeia.util.DateUtil;
import edu.coeia.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;

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
                Document document = getDocument(msg, currentDocumentId, 
                        session.userName, session.otherName , session.path); // add parentid and parent metadata here
                status = this.indexDocument(document);
            }
        }
        catch(Exception e){
            throw new UnsupportedOperationException(e.getMessage());
        }

        return status;
    }
    
    private boolean indexDocument(final Document doc) throws CorruptIndexException, IOException {
        boolean status  = false;
        
        int objectId = this.getId();

        if (doc != null) {
            this.getLuceneIndex().getWriter().addDocument(doc);    // index file
            this.increaseId();      // increase the id counter if file indexed successfully

            // cache images with id as parent id
            if ( this.isImageCache() ) {
                this.getImageExtractor().extractImages(this, this.getFile(), objectId);
            }

            status = true;
        }
        
        return status;
    }
        
    private Document getDocument(YahooMessage msg, int parentId, String profileName, String destinationName, String path) {
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
        doc.add(new Field(IndexingConstant.DOCUMENT_PARENT_ID, String.valueOf(parentId), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        // specific lucene fileds
        doc.add(new Field(IndexingConstant.CHAT_AGENT, CHAT_AGENT, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_FILE, this.getPathHandler().getRelativePath(path), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_FROM, from, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_TO, to, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_TIME, DateUtil.formatDateTime(msg.getTimeStamp()) , Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_MESSAGE, result.toString(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_LENGTH, String.valueOf(msg.getMessageLength()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_MESSAGE_PATH, msg.getMessagePath().toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        return doc;
    }
}
