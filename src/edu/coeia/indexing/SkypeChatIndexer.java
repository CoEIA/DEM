/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.chat.SkypeMessage;
import edu.coeia.chat.SkypeMessageReader;
import edu.coeia.extractors.ImageExtractor;
import edu.coeia.extractors.NoneImageExtractor;
import edu.coeia.hash.HashCalculator;
import edu.coeia.util.FileUtil;
import edu.coeia.util.Tuple;

import edu.coeia.util.Utilities;
import java.io.File;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;

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
                    Document document = getDocument(msg, currentDocumentId);
                    status = this.indexDocument(document);
                }
            }
        }
        catch(Exception e) {
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
        
    private Document getDocument(SkypeMessage msg, int parentId) {
        Document doc = new Document();
        
        // genric lucene fileds
        doc.add(new Field(IndexingConstant.DOCUMENT, IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.CHAT), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_ID, String.valueOf(this.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_PARENT_ID, String.valueOf(parentId), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        // specific lucene fileds
        doc.add(new Field(IndexingConstant.CHAT_AGENT, CHAT_AGENT, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_FILE, this.getPathHandler().getRelativePath(this.getFile()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_FROM, msg.getAuther(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_TO, Utilities.getEmptyStringWhenNullString(msg.getPartner()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_TIME, msg.getDate() , Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_MESSAGE, Utilities.getEmptyStringWhenNullString(msg.getMessageText()), Field.Store.YES, Field.Index.ANALYZED));
        
        return doc;
    }
}
