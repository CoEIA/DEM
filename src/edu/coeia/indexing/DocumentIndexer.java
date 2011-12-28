/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.extractors.TikaExtractor;
import edu.coeia.extractors.ImageExtractor;
import edu.coeia.hash.HashCalculator;
import edu.coeia.util.FileUtil;

import java.io.File;

import java.util.Map;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 *
 * @author wajdyessam
 */

final class DocumentIndexer extends Indexer {

    private int parentId ;
    
    /*
     * static factory method to get an instance of DocumentIndexer
     */
    public static DocumentIndexer newInstance (LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor) {
        return new DocumentIndexer(luceneIndex, file, mimeType, imageExtractor, 0);
    }
     
    /*
     * static factory method to get an instance of DocumentIndexer
     */
    public static DocumentIndexer newInstance (LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor, int parentId) {
        return new DocumentIndexer(luceneIndex, file, mimeType, imageExtractor, parentId);
    }
    
    private DocumentIndexer(LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor,int parentId) {
        super(luceneIndex, file,mimeType, imageExtractor);
        this.parentId = parentId ;
    }
    
    @Override
    public boolean doIndexing() {
        boolean status = false ;
        
        try {
            TikaExtractor extractor = TikaExtractor.getExtractor(this.getFile(), this.getMimeType());
            
            String bodyText = extractor.getContent();
            Map<String, String> metadata = extractor.getMetadata();
            
            Document doc = getDocument(bodyText, metadata); // add parentid and parent metadata here
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
        }
        catch(Exception e){
            throw new UnsupportedOperationException(e.getMessage());
        }

        return status;
    }
        
    private Document getDocument(String content, Map<String, String> metadata) {
        Document doc = new Document();
        
        // generic document fields
        doc.add(new Field(IndexingConstant.DOCUMENT_ID, String.valueOf(this.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT, IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.FILE), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_PARENT_ID, String.valueOf(this.parentId), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_HASH, HashCalculator.calculateFileHash(this.getFile().getAbsolutePath()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        // specific document fields
        doc.add(new Field(IndexingConstant.FILE_PATH, this.getFile().getPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_NAME, this.getFile().getName() , Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_DATE, DateTools.timeToString(this.getFile().lastModified(), DateTools.Resolution.MINUTE),Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_CONTENT, content, Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_MIME, FileUtil.getExtension(this.getFile()), Field.Store.YES, Field.Index.NOT_ANALYZED) );
        
        // unkown metadata extracted by Tika
        for(Map.Entry<String, String> entry: metadata.entrySet()) {
            String name =  entry.getKey();
            String value = entry.getValue();

            doc.add(new Field(name, value, Field.Store.YES, Field.Index.ANALYZED)); 
        }
        
        return doc;
    }
}
