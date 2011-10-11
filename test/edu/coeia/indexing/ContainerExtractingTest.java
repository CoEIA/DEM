/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import org.junit.Before ;
import org.junit.Test;
import org.junit.Ignore ;
import static org.junit.Assert.*;

import java.util.List ;
import java.util.ArrayList ;

/**
 *
 * @author wajdyessam
 */
public class ContainerExtractingTest {
       
    @Before
    public void init() {
        fillDocuments();
    }
    
   @Test
   public void extractImagesFromDOCX() {
      TikaObjectExtractor.EmbeddedObjectHandler handler = TikaObjectExtractor.getExtractor(files.get(0), OUTPUT_PATH,
       TikaObjectExtractor.OBJECT_TYPE.CONTAINER).extract();
      
      assertEquals(33, handler.getLocations().size());
   }
   
   @Test
   public void extractImagesFromPPTX() {
       TikaObjectExtractor.EmbeddedObjectHandler handler = TikaObjectExtractor.getExtractor(files.get(1), OUTPUT_PATH,
       TikaObjectExtractor.OBJECT_TYPE.CONTAINER).extract();
      
      assertEquals(53, handler.getLocations().size());
   }
   
   @Test
   public void extractImagesFromPDF() {
      TikaObjectExtractor.EmbeddedObjectHandler handler = TikaObjectExtractor.getExtractor(files.get(2), OUTPUT_PATH,
       TikaObjectExtractor.OBJECT_TYPE.CONTAINER).extract();
      
      assertEquals(0, handler.getLocations().size());
   }
   
   @Test
   public void extractImageFromDOCX2() {
      TikaObjectExtractor.EmbeddedObjectHandler handler = TikaObjectExtractor.getExtractor(files.get(3), OUTPUT_PATH,
       TikaObjectExtractor.OBJECT_TYPE.CONTAINER).extract();
      
      assertEquals(7, handler.getLocations().size());
   }
    
    private void fillDocuments() {
        files = new ArrayList<String>();
        files.add(SOURCE_PATH + "DEMManual.docx");
        files.add(SOURCE_PATH + "DEM - Expo.pptx");
        files.add(SOURCE_PATH + "a.pdf");
        files.add(SOURCE_PATH + "Form for exhibition.docx");
    }
    
    private List<String> files ;
    
    private static final String SOURCE_PATH = "C:\\data\\";
    private static final String OUTPUT_PATH = "C:\\out";
}
