/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.extractors.ImageExtractor;
import edu.coeia.extractors.TikaExtractor;
import edu.coeia.hash.HashCalculator;
import edu.coeia.util.FileUtil;
import edu.coeia.util.Utilities;

import java.io.File;

import java.util.Map;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 *
 * @author wajdyessam
 */
final class NonDocumentIndexer extends Indexer{
    
    /*
     * static factory method to get an instance of DocumentIndexer
     */
    public static NonDocumentIndexer newInstance (LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor) {
        return new NonDocumentIndexer(luceneIndex, file, mimeType, imageExtractor, 0);
    }
     
    /*
     * static factory method to get an instance of DocumentIndexer
     */
    public static NonDocumentIndexer newInstance (LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor, int parentId) {
        return new NonDocumentIndexer(luceneIndex, file, mimeType, imageExtractor, parentId);
    }
    
    private NonDocumentIndexer(LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor,int parentId) {
        super(luceneIndex, file,mimeType, imageExtractor);
        this.setParentId(parentId);
    }
    
    @Override
    public boolean doIndexing() {
        boolean status = false ;
        
        try {
            TikaExtractor extractor = TikaExtractor.getExtractor(this.getFile(), this.getMimeType(),
                    TikaExtractor.EXTRACT_TYPE.METADATA);
            
            Map<String, String> metadata = extractor.getMetadata();
            Document doc = LuceneDocumentBuilder.getDocument(this, "", metadata); // add parentid and parent metadata here
            status = this.indexDocument(doc);
        }
        catch(Exception e){
            throw new UnsupportedOperationException(e.getMessage());
        }

        return status;
    }
    
}
