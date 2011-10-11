/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import org.apache.tika.metadata.Metadata ;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File ;

import java.util.Map ;
import java.util.List ;
import java.util.ArrayList; 

/**
 *
 * @author wajdyessam
 */
public class TikaExtractorTest {
    
   @Test
   public void extractTextFileContent() {
       TikaExtractor extractor = TikaExtractor.getExtractor(files.get(0), mimeType);
       String content = extractor.getContent().trim();
       System.out.println("content: " + content);
       assertEquals("This is the content of the Plain Text document", content);
   }
   
   @Test
   public void extractTextFileMetadata() {
        TikaExtractor extractor = TikaExtractor.getExtractor(files.get(0), mimeType);
        Map<String, String> metadata = extractor.getMetadata();
        
        assertEquals("PlainText.txt", metadata.get(Metadata.RESOURCE_NAME_KEY));
   }
   
   // test another file DOCX
   @Test
   public void extractDOCXFileContent() {
       TikaExtractor extractor = TikaExtractor.getExtractor(files.get(1), "");
       String content = extractor.getContent();
       
       assertTrue(content.contains("Wajdy Action"));
   }
   
   // test another file RTF
   @Test
   public void extractRTFFileContent() {
       TikaExtractor extractor = TikaExtractor.getExtractor(files.get(2), "");
       String content = extractor.getContent();
       
       assertTrue(content.contains("document"));
   }
   
   // test PDF file
   @Test
   public void extractPDFFileContent() {
       TikaExtractor extractor = TikaExtractor.getExtractor(files.get(3), "");
       assertTrue(extractor.getContent().contains("Hashes sometimes show up in unusual circumstances"));
   }
   
   @Before
   public void init() {
       files.add(new File(SOURCE_PATH + "PlainText.txt"));
       files.add(new File(SOURCE_PATH + "test.docx"));
       files.add(new File(SOURCE_PATH + "RTF.rtf"));
       files.add(new File(SOURCE_PATH + "aa.pdf"));
       
       mimeType = "text/plain";
   }
   
   private static final String SOURCE_PATH = "C:\\data\\" ;
   
   private List<File> files = new ArrayList<File>();
   private String mimeType; 
   
}
