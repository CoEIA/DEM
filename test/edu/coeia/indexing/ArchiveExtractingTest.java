/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import org.junit.Test ;
import org.junit.Before; 

import static org.junit.Assert.* ;

import java.util.List ;
import java.util.ArrayList; 

public class ArchiveExtractingTest {
    
    @Before
    public void init() {
        fillDocuments();
    }
    
    @Test
    public void extractObjectFromZipTest1() {
       TikaObjectExtractor.EmbeddedObjectHandler handler = TikaObjectExtractor.getExtractor(zips.get(0), OUTPUT_PATH,
               TikaObjectExtractor.OBJECT_TYPE.ARCHIVE).extract();
       
       assertEquals(10, handler.getLocations().size());
    }
    
    @Test
    public void extractObjectFromZipTest2() {
       TikaObjectExtractor.EmbeddedObjectHandler handler = TikaObjectExtractor.getExtractor(zips.get(1), OUTPUT_PATH,
               TikaObjectExtractor.OBJECT_TYPE.ARCHIVE).extract();
       
       assertEquals(0, handler.getLocations().size());
    }
      
    @Test
    public void extractObjectFromZipTest3() {
       TikaObjectExtractor.EmbeddedObjectHandler handler = TikaObjectExtractor.getExtractor(zips.get(2), OUTPUT_PATH,
               TikaObjectExtractor.OBJECT_TYPE.ARCHIVE).extract();
       
       assertEquals(2, handler.getLocations().size());
       assertEquals("a.jpeg", handler.getLocations().get(0).fileName);
       assertEquals("data2.zip", handler.getLocations().get(1).fileName);
    }
    
   @Test
   public void zipProtectedTest() {
       TikaObjectExtractor.EmbeddedObjectHandler handler = TikaObjectExtractor.getExtractor(zips.get(3), OUTPUT_PATH,
               TikaObjectExtractor.OBJECT_TYPE.ARCHIVE).extract();
       
       assertNull(handler);
   }
        
    private void fillDocuments() {
        zips = new ArrayList<String>();
        zips.add(SOURCE_PATH + "data.zip");
        zips.add(SOURCE_PATH + "data2.zip");
        zips.add(SOURCE_PATH + "data3.zip");
        zips.add(SOURCE_PATH + "pass.zip");
    }
    
    private List<String> zips ;
    
    private static final String SOURCE_PATH = "C:\\data\\";
    private static final String OUTPUT_PATH = "C:\\out";
}
