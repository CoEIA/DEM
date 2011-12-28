/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.extractors;

/**
 *
 * @author wajdyessam
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import java.util.HashMap;
import java.util.Map;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class TikaExtractor {
    
    /*
     * Extract Text Content and Metadata From Any Documents
     */
    public static TikaExtractor getExtractor(File file, String mimeType) throws FileNotFoundException, IOException, SAXException, 
            TikaException{
        return new TikaExtractor(file, mimeType);
    }
    
    private TikaExtractor (File file, String mimeType) throws FileNotFoundException, IOException, SAXException, 
            TikaException {
        this.file = file ;
        this.mimeType = mimeType;

        processObject();
    }
    
    public String getContent() {
        return this.content;
    }
    
    public Map<String, String> getMetadata() {
        return this.metadataMap;
    }
    
    private void processObject() throws FileNotFoundException, IOException, SAXException, 
            TikaException{
        
        final TikaInputStream inputStream =  TikaInputStream.get(this.file);
        try {
            final Detector detector = new DefaultDetector();
            final Parser parser = new AutoDetectParser(detector);

            final Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, this.file.getName());
            metadata.set(Metadata.MIME_TYPE_MAGIC, this.mimeType);

            final ParseContext parseContext = new ParseContext();
            parseContext.set(Parser.class, parser);

            final StringWriter stringWriter = new StringWriter();
            ContentHandler contentHandler = new BodyContentHandler(stringWriter);

            parser.parse(inputStream, contentHandler, metadata, parseContext);
            

            // save content
            this.content = stringWriter.toString();

            stringWriter.close();

            // save metadata
            for(String name: metadata.names()) {
                metadataMap.put(name, metadata.get(name));
            }
        }
        finally {
            inputStream.close();
        }
    }
    
    private File file ;
    private String mimeType ;
    
    private String content ;
    private Map<String, String> metadataMap = new HashMap<String, String>();
}
