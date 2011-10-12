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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.index.IndexWriter ;
import org.apache.lucene.document.Document ;
import org.apache.lucene.document.Field ;
import org.apache.tika.metadata.Metadata;

public class ImageIndexer extends Indexer{
    
    private int parentId ;
    
    public ImageIndexer(IndexWriter writer, File file, String mimeType, boolean imageCaching, String location, ImageExtractor imageExtractor) {
        this(writer, file,mimeType, imageCaching, location, imageExtractor, 0);
    }
        
    public ImageIndexer(IndexWriter writer, File file, String mimeType, boolean imageCaching, String location, ImageExtractor imageExtractor, int parentId) {
        super(writer, file,mimeType, imageCaching, location, imageExtractor);
        this.parentId = parentId ;
    }
        
    @Override
    public boolean doIndexing() {
        
        try{
            TikaExtractor extractor = TikaExtractor.getExtractor(this.file, this.mimeType);
            Map<String, String> metadata = extractor.getMetadata();
            
            Document doc = getDocument(file, metadata);
            writer.addDocument(doc);
            
            int objectId = this.id ;
            
            if ( doc != null) {
                writer.addDocument(doc);    // index file
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
            
            return true;
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
        
        return false;
    }
    
    // provide lucene document for images format (JPEG, PNG.. etc)
    private Document getDocument(File file, Map<String, String> metadata) {
        Document doc = new Document();
        
        doc.add(new Field(IndexingConstant.FILE_MIME, file.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED) );
        
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
    
    private static final Set<String> indexedMetadataFields = new HashSet<String>();
    static {
        indexedMetadataFields.add(Metadata.TITLE);
        indexedMetadataFields.add(Metadata.AUTHOR);
        indexedMetadataFields.add(Metadata.COMMENTS);
        indexedMetadataFields.add(Metadata.KEYWORDS);
        indexedMetadataFields.add(Metadata.DESCRIPTION);
        indexedMetadataFields.add(Metadata.SUBJECT);
    }
    
}
