/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import java.io.File;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

import org.apache.tika.metadata.Metadata;

/**
 *
 * @author wajdyessam
 */

public class DocumentIndexer extends Indexer {

    public DocumentIndexer(File file, String mimeType, boolean imageCaching, String location, ImageExtractor imageExtractor) {
        super(file, mimeType, imageCaching, location, imageExtractor);
    }

    @Override
    public boolean doIndexing(IndexWriter writer) {

        try {
           TikaExtractor extractor = TikaExtractor.getExtractor(this.file, this.mimeType);
            
            String bodyText = extractor.getContent();
            Map<String, String> metadata = extractor.getMetadata();
            
            Document doc = getDocument(bodyText, metadata);
            if (doc != null) {
                writer.addDocument(doc);    // index file
            } else {
                System.out.println("Fail Parsing: " + file.getAbsolutePath());
                return false;
            }
                    
            // cache images
            if ( imageCache ) {
                imageExtractor.extractImages(file, this.location);
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
        
        for(Map.Entry<String, String> entry: metadata.entrySet()) {
            String name =  entry.getKey();
            String value = entry.getValue();
            
            if (indexedMetadataFields.contains(name))
                doc.add(new Field(name, value,Field.Store.NO, Field.Index.ANALYZED));
            else 
                doc.add(new Field(name, value, Field.Store.YES, Field.Index.NO)); 
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
