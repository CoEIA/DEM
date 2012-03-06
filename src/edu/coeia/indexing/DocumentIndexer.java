/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import static edu.coeia.constants.IndexingConstant.*;
import edu.coeia.constants.IndexingConstant.DOCUMENT_GENERAL_TYPE;
import edu.coeia.extractors.ImageExtractor;
import edu.coeia.util.FileUtil;
import edu.coeia.util.HashCalculator;
import edu.coeia.util.Utilities;

import java.io.File;
import java.io.StringWriter;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.tika.Tika;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author wajdyessam
 */

final class DocumentIndexer extends Indexer {

    /*
     * static factory method to get an instance of DocumentIndexer
     */
    public static DocumentIndexer newInstance (IndexerManager luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor, int parentId) {
        return new DocumentIndexer(luceneIndex, file, mimeType, imageExtractor, parentId);
    }
    
    private DocumentIndexer(IndexerManager luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor,int parentId) {
        super(luceneIndex, file,mimeType, imageExtractor);
        this.setParentId(parentId);
    }
    
    @Override
    public boolean doIndexing() {
        boolean status = false ;
        
        try {
            status = this.indexDocument(getFullTextAndMetadataDocument(this.getFile()));
        }
        catch(Exception e){
            throw new UnsupportedOperationException(e.getMessage());
        }

        return status;
    }
    
    private Document getFullTextAndMetadataDocument(final File file) throws Exception{
        Map<String, String> metadataMap = new HashMap<String, String>();

        ContentHandler contentHandler;
        Document document = new Document();

        final TikaInputStream inputStream =  TikaInputStream.get(file);
        try {
            final Detector detector = new DefaultDetector();
            final Parser parser = new AutoDetectParser(detector);

            final Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
            metadata.set(Metadata.MIME_TYPE_MAGIC, this.getMimeType());

            final ParseContext parseContext = new ParseContext();
            parseContext.set(Parser.class, parser);

            //StringWriter writer = new StringWriter();
            //contentHandler = new BodyContentHandler(writer);
            contentHandler = new DefaultHandler();
            parser.parse(inputStream, contentHandler, metadata, parseContext);
            
            // save metadata
            for(String name: metadata.names()) {
                metadataMap.put(name, metadata.get(name));
            }

            Document doc = new Document();

            DOCUMENT_DESCRIPTION_TYPE type = this.getParentId() != 0 ? DOCUMENT_DESCRIPTION_TYPE.EMBEDDED_FILE: DOCUMENT_DESCRIPTION_TYPE.NORMAL_FILE;

            if ( LuceneDocumentBuilder.insideOfflineEmailAttachmentFolder(this) || LuceneDocumentBuilder.insideOnlineEmailAttachmentFolder(this) )
                type = DOCUMENT_DESCRIPTION_TYPE.EMAIL_ATTACHMENT;

            // generic lucene fileds
            doc.add(getAnalyzedField(DOCUMENT_TYPE, fromDocumentTypeToString(DOCUMENT_GENERAL_TYPE.FILE)));
            doc.add(getNotAnlyzedField(DOCUMENT_ID, String.valueOf(this.getId())));
            doc.add(getNotAnlyzedField(DOCUMENT_PARENT_ID, String.valueOf(this.getParentId())));
            doc.add(getNotAnlyzedField(DOCUMENT_HASH, HashCalculator.calculateFileHash(this.getFile().getAbsolutePath())));
            doc.add(getAnalyzedField(DOCUMENT_DESCRIPTION, fromDocumentTypeToString(type)));

            // specific document fields
            String path = this.getCaseFacade().getRelativePath(this.getFile().getPath());
            if ( path.isEmpty() )
                path = this.getFile().getAbsolutePath(); // this is for file inside TMP, we cannot get relative path for it

            doc.add(getNotAnlyzedField(FILE_PATH, path));
            doc.add(getNotAnlyzedField(FILE_NAME, this.getFile().getName()));
            doc.add(getNotAnlyzedField(FILE_DATE, DateTools.timeToString(this.getFile().lastModified(), DateTools.Resolution.MINUTE)));
            doc.add(getNotAnlyzedField(FILE_MIME, FileUtil.getExtension(this.getFile())));
            //doc.add(getAnalyzedField(FILE_CONTENT, writer.toString()));
            doc.add(new Field(FILE_CONTENT,  new Tika().parse(file)));

            // unkown metadata extracted by Tika
            for(Map.Entry<String, String> entry: metadataMap.entrySet()) {
                String name =  Utilities.getEmptyStringWhenNullString(entry.getKey());
                String value = Utilities.getEmptyStringWhenNullString(entry.getValue());

                doc.add(getAnalyzedField(name, value));
            }
        
        }
        finally {
            inputStream.close();
        }

        return document;
    }
    
    private static Field getNotAnlyzedField(final String field, final String value) {
        return new Field(field, value, Field.Store.YES, Field.Index.NOT_ANALYZED);
    }
    
    private static Field getAnalyzedField(final String field, final String value) {
        return new Field(field, value, Field.Store.YES, Field.Index.ANALYZED);
    }
}
