/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */
import edu.coeia.hash.HashCalculator;
import edu.coeia.chat.MSNMessageReader;
import edu.coeia.extractors.ImageExtractor;
import static edu.coeia.chat.MSNMessageReader.*;
import edu.coeia.extractors.NoneImageExtractor;
import edu.coeia.util.FileUtil;

import java.io.File;

import java.io.IOException;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;

final class MSNChatIndexer extends Indexer {

    private static final String CHAT_AGENT = "MSN";

    /**
     * static factory method to get an instance of MSNIndexer
     */
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
                Document doc = getDocument(msg, currentDocumentId, 
                        session.getUserName(), session.getOtherName(), session.getPath()); // add parentid and parent metadata here
                status = this.indexDocument(doc);
            }
        } catch (Exception e) {
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
        
    private Document getDocument(MSNMessage msg, int parentId, String profileName, String destinationName, String path) {
        Document doc = new Document();

       // genric lucene fileds
        doc.add(new Field(IndexingConstant.DOCUMENT, IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.CHAT), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_ID, String.valueOf(this.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_PARENT_ID, String.valueOf(parentId), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        // specific lucene fileds
        doc.add(new Field(IndexingConstant.CHAT_AGENT, CHAT_AGENT, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_FILE, this.getPathHandler().getRelativePath(path), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_FROM, msg.getFrom(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_TO, msg.getTo(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_TIME, msg.getDateTime() , Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_MESSAGE, msg.getMessage(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_LENGTH, String.valueOf(msg.getMessage().length()), Field.Store.YES, Field.Index.NOT_ANALYZED));

        return doc;
    }
}
