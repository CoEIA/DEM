/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.filesystem.index.FileDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MediaTypeRegistry;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

/**
 *
 * @author wajdyessam
 */

public class DocumentIndexer extends Indexer {

    public DocumentIndexer(File file, String mimeType, boolean imageCaching, ImageExtractor imageExtractor) {
        super(file, mimeType, imageCaching, imageExtractor);
    }

    @Override
    public boolean doIndexing(IndexWriter writer) {

        try {
            Document doc = new Document();

            Tika tika = new Tika();
            String bodyText = tika.parseToString(file);
            doc = FileDocument.documentFile(file, bodyText);

            //doc = getDocument(file);
            
            if (doc != null) {
                writer.addDocument(doc);    // index file
            } else {
                System.out.println("Fail Parsing: " + file.getAbsolutePath());
                return false;
            }

            // cache images
            if ( imageCache ) {
                imageExtractor.extractImages(file, "C:\\IMGS");
            }
            
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }


        return false;
    }
        
    protected Document getDocument(File f) throws Exception {

        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());   // 4

        // If you know content type (eg because this document
        // was loaded from an HTTP server), then you should also
        // set Metadata.CONTENT_TYPE

        // If you know content encoding (eg because this
        // document was loaded from an HTTP server), then you
        // should also set Metadata.CONTENT_ENCODING

        InputStream is = new FileInputStream(f);      // 5
        Parser parser = new AutoDetectParser();       // 6
        ContentHandler handler = new BodyContentHandler(); // 7
        ParseContext context = new ParseContext();   // 8
        context.set(Parser.class, parser);           // 8

        try {
            parser.parse(is, handler, metadata, // 9
                    new ParseContext());        // 9
        } finally {
            is.close();
        }

        Document doc = new Document();

        doc.add(new Field("contents", handler.toString(), // 10
                Field.Store.NO, Field.Index.ANALYZED));   // 10

        if (DEBUG) {
            System.out.println("  all text: " + handler.toString());
        }

        for (String name : metadata.names()) {         //11
            String value = metadata.get(name);

            if (textualMetadataFields.contains(name)) {
                doc.add(new Field("contents", value, //12
                        Field.Store.NO, Field.Index.ANALYZED));
            }

            doc.add(new Field(name, value, Field.Store.YES, Field.Index.NO)); //13

            if (DEBUG) {
                System.out.println("  " + name + ": " + value);
            }
        }

        if (DEBUG) {
            System.out.println();
        }

        doc.add(new Field("filename", f.getCanonicalPath(), //14
                Field.Store.YES, Field.Index.NOT_ANALYZED));

        return doc;
    }
    
    private static final Set<String> textualMetadataFields = new HashSet<String>();
    private boolean DEBUG = false;
    
    static {
        textualMetadataFields.add(Metadata.TITLE);
        textualMetadataFields.add(Metadata.AUTHOR);
        textualMetadataFields.add(Metadata.COMMENTS);
        textualMetadataFields.add(Metadata.KEYWORDS);
        textualMetadataFields.add(Metadata.DESCRIPTION);
        textualMetadataFields.add(Metadata.SUBJECT);
    }
}
