/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.extractors.TikaExtractor;
import edu.coeia.extractors.ImageExtractor;

import java.io.File;

import java.util.Map;

import org.apache.lucene.document.Document;

/**
 *
 * @author wajdyessam
 */

final class DocumentIndexer extends Indexer {

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
        this.setParentId(parentId);
    }
    
    @Override
    public boolean doIndexing() {
        boolean status = false ;
        
        try {
            TikaExtractor extractor = TikaExtractor.getExtractor(this.getFile(), this.getMimeType(),
                    TikaExtractor.EXTRACT_TYPE.TEXT_METADATA);
            
            String bodyText = extractor.getContent();
            Map<String, String> metadata = extractor.getMetadata();
            
            Document doc = LuceneDocumentBuilder.getDocumentForFile(
                    this, bodyText, metadata); // add parentid and parent metadata here
            status = this.indexDocument(doc);
        }
        catch(Exception e){
            throw new UnsupportedOperationException(e.getMessage());
        }

        return status;
    }
}
