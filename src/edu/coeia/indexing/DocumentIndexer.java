/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

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

        try {
            TikaExtractor extractor = TikaExtractor.getExtractor(this.file, this.mimeType);
            
            String bodyText = extractor.getContent();
            Map<String, String> metadata = extractor.getMetadata();
            
            Document doc = getDocument(bodyText, metadata); // add parentid and parent metadata here
            int objectId = id;
            
            if (doc != null) {
                this.luceneIndex.getWriter().addDocument(doc);    // index file
                this.id++;                       // increase the id counter if file indexed successfully
                
            } else {
                System.out.println("Fail Parsing: " + file.getAbsolutePath());
                return false;
            }
                    
            // cache images with id as parent id
            if ( imageCache ) {
                imageExtractor.extractImages(this, file, objectId);
            }
            
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
        
    private Document getDocument(String content, Map<String, String> metadata) {
        Document doc = new Document();
        
        doc.add(new Field(IndexingConstant.FILE_NAME, file.getPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_TITLE, file.getName() , Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_DATE, DateTools.timeToString(file.lastModified(), DateTools.Resolution.MINUTE),Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_CONTENT, content, Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_MIME, FileUtil.getExtension(this.file), Field.Store.YES, Field.Index.NOT_ANALYZED) );
        
        doc.add(new Field(IndexingConstant.FILE_ID, String.valueOf(this.id), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_PARENT_ID, String.valueOf(this.parentId), Field.Store.YES, Field.Index.NOT_ANALYZED));
         
        for(Map.Entry<String, String> entry: metadata.entrySet()) {
            String name =  entry.getKey();
            String value = entry.getValue();
            
            if (indexedMetadataFields.contains(name))
                doc.add(new Field(name, value,Field.Store.YES, Field.Index.NOT_ANALYZED));
            else 
                doc.add(new Field(name, value, Field.Store.YES, Field.Index.NOT_ANALYZED)); 
        }
        
        return doc;
    }
}
