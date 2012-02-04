/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.extractors.TikaExtractor;
import edu.coeia.extractors.ImageExtractor;

import java.io.File ;

import java.util.Map;

import org.apache.lucene.document.Document ;


final class ImageIndexer extends Indexer{
    
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
        this.setParentId(parentId);
    }
        
    @Override
    public boolean doIndexing() {
        boolean status = false; 
        
        try{            
            TikaExtractor extractor = TikaExtractor.getExtractor(this.getFile(), this.getMimeType(),
                    TikaExtractor.EXTRACT_TYPE.METADATA);
            Map<String, String> metadata = extractor.getMetadata();
            
            Document doc = LuceneDocumentBuilder.getDocumentForImage(this, metadata);
            status = this.indexDocument(doc);
        }
        catch(Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
        
        return status;
    }
}
