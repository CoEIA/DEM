/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.chat.SkypeMessage;
import edu.coeia.chat.SkypeParser;
import edu.coeia.extractors.ImageExtractor;
import edu.coeia.hash.HashCalculator;
import edu.coeia.util.FileUtil;
import edu.coeia.util.Tuple;

import edu.coeia.util.Utilities;
import java.io.File;

import java.util.List;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 *
 * @author wajdyessam
 */
public class SkypeChatIndexer extends Indexer{
    
    /**
     *  chat type
     */
    private static final String CHAT_AGENT = "SKYPE" ;
    private int parentId ;
    
    public static SkypeChatIndexer newInstance(LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor) {
            
        return new SkypeChatIndexer(luceneIndex, file, mimeType, imageExtractor, 0);
    }
        
    private SkypeChatIndexer(LuceneIndex luceneIndex, File file, String mimeType, ImageExtractor imageExtractor,
            int parentId) {
        
        super(luceneIndex, file, mimeType, imageExtractor);
        this.parentId = parentId ;
    }
        
    @Override
    public boolean doIndexing() {
        boolean status = false;
        
        try {     
            // this id for the .db file, each message will have this id as parent
            this.increaseId();
            this.getLuceneIndex().getWriter().addDocument(getDocument());
            this.parentId = this.getId();
            
            SkypeParser parser = new SkypeParser();
            List<Tuple<String, List<SkypeMessage>>> msgs = parser.parseSkypeFile(this.getFile().getParent());

            for (Tuple<String, List<SkypeMessage>> user: msgs) {
                for (SkypeMessage msg: user.getB()) {
                    Document document = getDocument(msg);
                    
                    if (document != null) {
                        this.getLuceneIndex().getWriter().addDocument(document);    // index file
                        this.increaseId();                       // increase the id counter if file indexed successfully

                    } else {
                        System.out.println("Fail Parsing: " + this.getFile().getAbsolutePath());
                        return false;
                    }
                }
            }
            
            status = true;
        }
        catch(Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
        
        return status;
    }
    
    private Document getDocument(SkypeMessage msg) {
        Document doc = new Document();
        
        // genric lucene fileds
        doc.add(new Field(IndexingConstant.DOCUMENT, IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.CHAT), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_ID, String.valueOf(this.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_PARENT_ID, String.valueOf(this.parentId), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        // specific lucene fileds
        doc.add(new Field(IndexingConstant.CHAT_AGENT, CHAT_AGENT, Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_FILE, this.getPathHandler().getRelativePath(this.getFile()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_FROM, msg.getAuther(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_TO, Utilities.getEmptyStringWhenNullString(msg.getPartner()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_TIME, msg.getDate() , Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.CHAT_MESSAGE, Utilities.getEmptyStringWhenNullString(msg.getMessageText()), Field.Store.YES, Field.Index.ANALYZED));
        
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
