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

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

final class MSNChatIndexer extends Indexer {

    private static final String CHAT_AGENT = "MSN";
    private int parentId;

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
        this.parentId = parentId;
    }

    @Override
    public boolean doIndexing() {
        boolean status = false;

        try {
            MSNMessageReader reader = new MSNMessageReader();
            MSNChatSession session = reader.processFile(this.getFile());
            
           // this id for the XML file, each message will have this id as parent
            this.increaseId();
            //this.getLuceneIndex().getWriter().addDocument(getDocument());
            NonDocumentIndexer.newInstance(this.getLuceneIndex(), this.getFile(), this.getMimeType(),
                    new NoneImageExtractor()).doIndexing();
            this.parentId = this.getId();
            
            for(MSNMessage msg: session.getConversations()) {
                Document doc = getDocument(msg, 
                        session.getUserName(), session.getOtherName(), session.getPath()); // add parentid and parent metadata here

                if (doc != null) {
                    this.getLuceneIndex().getWriter().addDocument(doc);    // index file
                    this.increaseId();                       // increase the id counter if file indexed successfully

                } else {
                    System.out.println("Fail Parsing: " + this.getFile().getAbsolutePath());
                    return false;
                }
            }
            
            status = true;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return status;
    }

    private Document getDocument(MSNMessage msg, String profileName, String destinationName, String path) {
        Document doc = new Document();

       // genric lucene fileds
        doc.add(new Field(IndexingConstant.DOCUMENT, IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.CHAT), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_ID, String.valueOf(this.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_PARENT_ID, String.valueOf(this.parentId), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
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
    
    private Document getDocument() {
        Document doc = new Document();
        
        // generic document fields
        doc.add(new Field(IndexingConstant.DOCUMENT_ID, String.valueOf(this.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT, IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.FILE), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_PARENT_ID, String.valueOf(this.parentId), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_HASH, HashCalculator.calculateFileHash(this.getFile().getAbsolutePath()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        // specific document fields
        doc.add(new Field(IndexingConstant.FILE_PATH, this.getPathHandler().getRelativePath(this.getFile().getPath()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_NAME, this.getFile().getName() , Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_DATE, DateTools.timeToString(this.getFile().lastModified(), DateTools.Resolution.MINUTE),Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_CONTENT, "", Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_MIME, FileUtil.getExtension(this.getFile()), Field.Store.YES, Field.Index.NOT_ANALYZED) );
        
        return doc;
    }
}
