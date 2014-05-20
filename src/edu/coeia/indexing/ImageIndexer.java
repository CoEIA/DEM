/* 
 * Copyright (C) 2014 Center of Excellence in Information Assurance
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import static edu.coeia.constants.IndexingConstant.*;
import edu.coeia.constants.IndexingConstant.DOCUMENT_GENERAL_TYPE;
import edu.coeia.extractors.ImageExtractor;
import edu.coeia.util.FileUtil;
import edu.coeia.util.HashCalculator;
import edu.coeia.util.Utilities;

import java.io.File;

import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;

import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;


final class ImageIndexer extends Indexer{
    
    public static ImageIndexer newInstance(IndexerManager luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor) {
        return new ImageIndexer(luceneIndex, file,mimeType, imageExtractor, 0);
    }
        
    public static ImageIndexer newInstance(IndexerManager luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor, int parentId) {
        return new ImageIndexer(luceneIndex, file,mimeType, imageExtractor, parentId);
    }
    
    private ImageIndexer(IndexerManager luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor, int parentId) {
        super(luceneIndex, file,mimeType, imageExtractor);
        this.setParentId(parentId);
    }
        
    @Override
    public boolean doIndexing() {
        boolean status = false; 
        
        try{            
            status = this.indexDocument(getMetadataWithEmptyTextDocument(this.getFile()));
        }
        catch(Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
        
        return status;
    }
    
    // provide lucene document for images format (JPEG, PNG.. etc)
    private Document getMetadataWithEmptyTextDocument(final File file) throws Exception{
        Map<String, String> metadataMap = new HashMap<String, String>();

        ContentHandler contentHandler;
        Document doc = new Document();

        final TikaInputStream inputStream =  TikaInputStream.get(file);
        try {
            final Detector detector = new DefaultDetector();
            final Parser parser = new AutoDetectParser(detector);

            final Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
            metadata.set(Metadata.MIME_TYPE_MAGIC, this.getMimeType());

            final ParseContext parseContext = new ParseContext();
            parseContext.set(Parser.class, parser);

            contentHandler = new DefaultHandler();

            parser.parse(inputStream, contentHandler, metadata, parseContext);
            
            // save metadata
            for(String name: metadata.names()) {
                metadataMap.put(name, metadata.get(name));
            }
             
            DOCUMENT_DESCRIPTION_TYPE type = this.getParentId() != 0 ? DOCUMENT_DESCRIPTION_TYPE.EMBEDDED_IMAGE: DOCUMENT_DESCRIPTION_TYPE.IMAGE;

            if ( LuceneDocumentBuilder.insideOfflineEmailAttachmentFolder(this) || LuceneDocumentBuilder.insideOnlineEmailAttachmentFolder(this) )
                type = DOCUMENT_DESCRIPTION_TYPE.EMAIL_ATTACHMENT;

            // generic lucene fileds
            doc.add(getAnalyzedField(DOCUMENT_TYPE, fromDocumentTypeToString(DOCUMENT_GENERAL_TYPE.IMAGE)));
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

        return doc;
    }
    
    private static Field getNotAnlyzedField(final String field, final String value) {
        return new Field(field, value, Field.Store.YES, Field.Index.NOT_ANALYZED);
    }
    
    private static Field getAnalyzedField(final String field, final String value) {
        return new Field(field, value, Field.Store.YES, Field.Index.ANALYZED);
    }
}
