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
import edu.coeia.hash.HashCalculator;
import edu.coeia.util.FileUtil;
import java.io.File ;

import java.io.IOException;
import java.util.Map;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document ;
import org.apache.lucene.document.Field ;
import org.apache.lucene.index.CorruptIndexException;


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
            
            Document doc = getDocument(this.getFile(), metadata);
            status = this.indexDocument(doc);
        }
        catch(Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
        
        return status;
    }
    
    // provide lucene document for images format (JPEG, PNG.. etc)
    private Document getDocument(File file, Map<String, String> metadata) {
        Document doc = new Document();
        
        // generic document fileds
        doc.add(new Field(IndexingConstant.DOCUMENT_ID, String.valueOf(this.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT, IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.FILE), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_PARENT_ID, String.valueOf(this.getParentId()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.DOCUMENT_HASH, HashCalculator.calculateFileHash(this.getFile().getAbsolutePath()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        // specfic document fields
        doc.add(new Field(IndexingConstant.FILE_PATH, this.getCaseFacade().getRelativePath(this.getFile().getPath()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_NAME, file.getName() , Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_DATE, DateTools.timeToString(file.lastModified(), DateTools.Resolution.MINUTE),Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field(IndexingConstant.FILE_MIME, FileUtil.getExtension(file), Field.Store.YES, Field.Index.NOT_ANALYZED) );

        // unknown image metadata extracted by Tika
        for(Map.Entry<String, String> entry: metadata.entrySet()) {
            String name =  entry.getKey();
            String value = entry.getValue();
            doc.add(new Field(name, value, Field.Store.YES, Field.Index.ANALYZED)); 
        }
        
        return doc;
    }
}
