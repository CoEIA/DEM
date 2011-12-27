/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.extractors;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.indexing.CaseBaseSource;
import org.junit.Test ;
import org.junit.Before; 
import static org.junit.Assert.* ;

public class ArchiveExtractingTest extends CaseBaseSource{
    
    @Before
    public void init() {
        fillZipArchive();
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
}
