/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.filesystem.util;

/**
 *
 * @author wajdyessam
 */

import java.io.File ;
import java.io.FileInputStream;
import java.io.FileNotFoundException ;
import java.io.IOException ;

import org.apache.tika.metadata.Metadata ;
import org.apache.tika.parser.AutoDetectParser ;
import org.apache.tika.parser.Parser ;
import org.apache.tika.sax.BodyContentHandler ;
import org.apache.tika.exception.TikaException;

import org.xml.sax.ContentHandler ;
import org.xml.sax.SAXException ;

public class MetaDataExtraction {
    public static String getMetaData (String path) throws FileNotFoundException, IOException, SAXException,
    TikaException {
        StringBuilder result = new StringBuilder();

        File f = new File(path);
        FileInputStream in = new FileInputStream(f);
        ContentHandler contenthandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
        
        Parser parser = new AutoDetectParser();
        parser.parse(in, contenthandler, metadata);
        
        result.append("\nMime: " + metadata.get(Metadata.CONTENT_TYPE));
        result.append("\nTitle: " + metadata.get(Metadata.TITLE));
        result.append("\nAuthor: " + metadata.get(Metadata.AUTHOR));
        //result.append("\ncontent: " + contenthandler.toString());

        return result.toString();
    }
}
