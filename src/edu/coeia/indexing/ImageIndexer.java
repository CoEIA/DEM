/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import java.io.File ;
import java.io.FileNotFoundException ;
import java.io.IOException;

import java.util.Map;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document ;
import org.apache.lucene.document.Field ;


final class ImageIndexer extends Indexer{
    
    private int parentId ;
    
    public static ImageIndexer newInstance(LuceneIndex luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor) {
        return new ImageIndexer(luceneIndex, file,mimeType, imageExtractor, 0);
    }
        
    public static ImageIndexer newInstance(LuceneIndex luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor, int parentId) {
        return new ImageIndexer(luceneIndex, file,mimeType, imageExtractor, parentId);
    }
    
    private ImageIndexer(LuceneIndex luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor, int parentId) {
        super(luceneIndex, file,mimeType, imageExtractor);
        this.parentId = parentId ;
    }
        
    @Override
    public boolean doIndexing() {
        boolean status = false; 
        
        try{            
            TikaExtractor extractor = TikaExtractor.getExtractor(this.file, this.mimeType);
            Map<String, String> metadata = extractor.getMetadata();
            
            Document doc = getDocument(file, metadata);
            this.luceneIndex.getWriter().addDocument(doc);
            
            int objectId = this.id ;
            
            if ( doc != null) {
                this.luceneIndex.getWriter().addDocument(doc);    // index file
                this.id++;
            }
            else {
                System.out.println("Fail Parsing: " + file.getAbsolutePath());
                return false;
            }
            
            // cache images i.e move the image to images location , id will ignored 
            if ( imageCache ) {
                imageExtractor.extractImages(this, file, objectId);
            }
            
            status = true;
        }
        catch(Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
        
        
        return status;
    }
    
    // provide lucene document for images format (JPEG, PNG.. etc)
    private Document getDocument(File file, Map<String, String> metadata) {
        Document doc = new Document();
        
        doc.add(new Field(IndexingConstant.FILE_NAME, file.getPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_TITLE, file.getName() , Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_DATE, DateTools.timeToString(file.lastModified(), DateTools.Resolution.MINUTE),Field.Store.YES, Field.Index.NOT_ANALYZED));
               
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
