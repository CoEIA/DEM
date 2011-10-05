///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package edu.coeia.library;
//
///**
// *
// * @author wajdyessam
// */
//
//import org.junit.Test ;
//import static org.junit.Assert.*;
//
//import java.util.List ;
//import java.util.ArrayList ;
//
//import java.io.File ;
//import java.io.IOException; 
//
//import org.apache.tika.exception.TikaException;
//
//public class TikaTesting {
//     /**
//     * For office files which don't have anything embedded in them
//     */
//    @Test
//    public void testWithoutEmbedded() throws Exception {
//       ContainerExtractor extractor = new ParserContainerExtractor();
//        
//       String[] files = new String[] {
//             "testEXCEL.xls", "testWORD.doc", "testPPT.ppt",
//             "testVISIO.vsd", "test-outlook.msg"
//       };
//       for(String file : files) {
//          // Process it without recursing
//          TrackingHandler handler = process(file, extractor, false);
//           
//          // Won't have fired
//          assertEquals(0, handler.filenames.size());
//          assertEquals(0, handler.mediaTypes.size());
//           
//          // Ditto with recursing
//          handler = process(file, extractor, true);
//          assertEquals(0, handler.filenames.size());
//          assertEquals(0, handler.mediaTypes.size());
//       }
//    }
//     
//    /**
//     * Office files with embedded images, but no other
//     *  office files in them
//     */
//    @Test
//    public void testEmbeddedImages() throws Exception {
//       ContainerExtractor extractor = new ParserContainerExtractor();
//       TrackingHandler handler;
//        
//       // Excel with 1 image
//       handler = process("testEXCEL_1img.xls", extractor, false);
//       assertEquals(1, handler.filenames.size());
//       assertEquals(1, handler.mediaTypes.size());
//        
//       assertEquals(null, handler.filenames.get(0));
//       assertEquals(TYPE_PNG, handler.mediaTypes.get(0));
// 
//        
//       // PowerPoint with 2 images + sound
//       // TODO
//        
//        
//       // Word with 1 image
//       handler = process("testWORD_1img.doc", extractor, false);
//       assertEquals(1, handler.filenames.size());
//       assertEquals(1, handler.mediaTypes.size());
//        
//       assertEquals("image1.png", handler.filenames.get(0));
//       assertEquals(TYPE_PNG, handler.mediaTypes.get(0));
// 
//        
//       // Word with 3 images
//       handler = process("testWORD_3imgs.doc", extractor, false);
//       assertEquals(3, handler.filenames.size());
//       assertEquals(3, handler.mediaTypes.size());
//        
//       assertEquals("image1.png", handler.filenames.get(0));
//       assertEquals("image2.jpg", handler.filenames.get(1));
//       assertEquals("image3.png", handler.filenames.get(2));
//       assertEquals(TYPE_PNG, handler.mediaTypes.get(0));
//       assertEquals(TYPE_JPG, handler.mediaTypes.get(1));
//       assertEquals(TYPE_PNG, handler.mediaTypes.get(2));
//    }
//}
