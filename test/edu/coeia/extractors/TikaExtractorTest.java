/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.extractors;

import edu.coeia.indexing.CaseBaseSource;
import java.io.File;
import org.apache.tika.metadata.Metadata ;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map ;

/**
 *
 * @author wajdyessam
 */
public class TikaExtractorTest extends CaseBaseSource{
    
   @Test
   public void extractTextFileContent()  throws Exception{
       TikaExtractor extractor = TikaExtractor.getExtractor(files.get(0), mimeType);
       String content = extractor.getContent().trim();
       System.out.println("content: " + content);
       assertEquals("This is the content of the Plain Text document", content);
   }
   
   @Test
   public void extractTextFileMetadata()  throws Exception{
        TikaExtractor extractor = TikaExtractor.getExtractor(files.get(0), mimeType);
        Map<String, String> metadata = extractor.getMetadata();
        
        assertEquals("PlainText.txt", metadata.get(Metadata.RESOURCE_NAME_KEY));
   }
   
   // test another file DOCX
   @Test
   public void extractDOCXFileContent()  throws Exception{
       TikaExtractor extractor = TikaExtractor.getExtractor(files.get(1), "");
       String content = extractor.getContent();
       
       assertTrue(content.contains("Wajdy Action"));
   }
   
   // test another file RTF
   @Test
   public void extractRTFFileContent()  throws Exception{
       TikaExtractor extractor = TikaExtractor.getExtractor(files.get(2), "");
       String content = extractor.getContent();
       
       assertTrue(content.contains("document"));
   }
   
   // test PDF file
   @Test
   public void extractPDFFileContent()  throws Exception{
       TikaExtractor extractor = TikaExtractor.getExtractor(files.get(3), "");
       assertTrue(extractor.getContent().contains("Hashes sometimes show up in unusual circumstances"));
   }
   
   @Test
   public void extractExeFileContent() throws Exception {
       File file  = new File("C:\\data\\assistant.exe");
       TikaExtractor extractor = TikaExtractor.getExtractor(file, "");
       System.out.println("content: " + extractor.getContent());
       
       assertTrue(extractor.getContent().contains(""));
       
       for(Map.Entry<String, String> entry: extractor.getMetadata().entrySet()) {
            String name =  entry.getKey();
            String value = entry.getValue();
            System.out.println("name: " + name);
            System.out.println("value: " + value);
        }
   }
   
    @Test
   public void extractExeFileContent2() throws Exception {
       File file  = new File("C:\\data\\a.exe");
       TikaExtractor extractor = TikaExtractor.getExtractor(file, "");
       System.out.println("content: " + extractor.getContent());
       
       assertTrue(extractor.getContent().contains(""));
       
       for(Map.Entry<String, String> entry: extractor.getMetadata().entrySet()) {
            String name =  entry.getKey();
            String value = entry.getValue();
            System.out.println("name: " + name);
            System.out.println("value: " + value);
        }
   }
      
   @Before
   public void init() {
       initForTikaExtractorTest();
   }
}
