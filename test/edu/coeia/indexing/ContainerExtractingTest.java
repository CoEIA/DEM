/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import org.junit.Before ;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author wajdyessam
 */
public class ContainerExtractingTest extends CaseBaseSource {
       
    @Before
    public void init() {
        fillContainerDocuments();
    }
    
   @Test
   public void extractImagesFromDOCX() {
      TikaObjectExtractor.EmbeddedObjectHandler handler = TikaObjectExtractor.getExtractor(containersList.get(0), OUTPUT_PATH,
       TikaObjectExtractor.OBJECT_TYPE.CONTAINER).extract();
      
      assertEquals(33, handler.getLocations().size());
   }
   
   @Test
   public void extractImagesFromPPTX() {
       TikaObjectExtractor.EmbeddedObjectHandler handler = TikaObjectExtractor.getExtractor(containersList.get(1), OUTPUT_PATH,
       TikaObjectExtractor.OBJECT_TYPE.CONTAINER).extract();
      
      assertEquals(53, handler.getLocations().size());
   }
   
   @Test
   public void extractImagesFromPDF() {
      TikaObjectExtractor.EmbeddedObjectHandler handler = TikaObjectExtractor.getExtractor(containersList.get(2), OUTPUT_PATH,
       TikaObjectExtractor.OBJECT_TYPE.CONTAINER).extract();
      
      assertEquals(0, handler.getLocations().size());
   }
   
   @Test
   public void extractImageFromDOCX2() {
      TikaObjectExtractor.EmbeddedObjectHandler handler = TikaObjectExtractor.getExtractor(containersList.get(3), OUTPUT_PATH,
       TikaObjectExtractor.OBJECT_TYPE.CONTAINER).extract();
      
      assertEquals(7, handler.getLocations().size());
   }
}
