///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package edu.coeia.indexing;
//
///**
// *
// * @author wajdyessam
// */
//
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//import java.util.List;
//import java.util.ArrayList;
//
//import java.io.File;
//
//public class IndexFactoryTest {
//
//    @Test
//    public void rarFilesMimeTest() {
//        for (String rarFile : rarFiles) {
//            assertEquals(ArchiveIndexer.class.getName(), IndexerFactory.getIndexer(new File(rarFile), true, OUTPUT_PATH).getClass().getName());
//        }
//    }
//
//    @Test
//    public void zipFilesMimeTest() {
//        for (String zipFile : zipFiles) {
//            assertEquals(ArchiveIndexer.class.getName(), IndexerFactory.getIndexer(new File(zipFile), true, OUTPUT_PATH).getClass().getName());
//        }
//    }
//
//    @Test
//    public void docFilesMimeTest() {
//        for (String docFile : docFiles) {
//            assertEquals(DocumentIndexer.class.getName(), IndexerFactory.getIndexer(new File(docFile), true, OUTPUT_PATH).getClass().getName());
//        }
//    }
//
//    @Test
//    public void ImageFilesMimeTest() {
//        for (String imageFile : imagesFiles) {
//            assertEquals(ImageIndexer.class.getName(), IndexerFactory.getIndexer(new File(imageFile), true, OUTPUT_PATH).getClass().getName());
//        }
//    }
//
//    @Test
//    public void xmlFilesMimeTest() {
//        for (String xmlFile : xmlFiles) {
//            assertEquals(DocumentIndexer.class.getName(), IndexerFactory.getIndexer(new File(xmlFile), true, OUTPUT_PATH).getClass().getName());
//        }
//    }
//
//    @Test
//    public void textFilesMimeTest() {
//        for (String textFile : textFiles) {
//            assertEquals(DocumentIndexer.class.getName(), IndexerFactory.getIndexer(new File(textFile), true, OUTPUT_PATH).getClass().getName());
//        }
//    }
//
//    @Test
//    public void webFilesMimeTest() {
//        for (String webFile : webFiles) {
//            assertEquals(DocumentIndexer.class.getName(), IndexerFactory.getIndexer(new File(webFile), true, OUTPUT_PATH).getClass().getName());
//        }
//    }
//
//    @Before
//    public void initDocuments() {
//        rarFiles = new ArrayList<String>();
//        zipFiles = new ArrayList<String>();
//        docFiles = new ArrayList<String>();
//        docxFiles = new ArrayList<String>();
//        pptFiles = new ArrayList<String>();
//        imagesFiles = new ArrayList<String>();
//        webFiles = new ArrayList<String>();
//        xmlFiles = new ArrayList<String>();
//        textFiles = new ArrayList<String>();
//
//        fillDocuments();
//    }
//
//    private void fillDocuments() {
//        // add rar files for testing
//        rarFiles.add(CASE_PATH + "134.rar");
//        rarFiles.add(CASE_PATH + "test.rar");
//
//        // add zip files for testing
//        zipFiles.add(CASE_PATH + "2.zip");
//        zipFiles.add(CASE_PATH + "a.zip");
//        zipFiles.add(CASE_PATH + "dasfas.zip");
//
//        // add doc files for testing
//        docFiles.add(CASE_PATH + "41.doc");
//        docFiles.add(CASE_PATH + "Dr.Alzahrani Khtab.doc");
//        docFiles.add(CASE_PATH + "fdeha_book52.doc");
//        docFiles.add(CASE_PATH + "بحث الارهاب.doc");
//
//        // add docx files
//
//        // add ppt files
//
//        // add images files
//        imagesFiles.add(CASE_PATH + "1254550999.jpg");
//        imagesFiles.add(CASE_PATH + "images (9).jpg");
//        imagesFiles.add(CASE_PATH + "1.jpg");
//        imagesFiles.add(CASE_PATH + "a\\mwa2.gif");
//        imagesFiles.add(CASE_PATH + "search.png");
//
//        // add html pages
//        webFiles.add(CASE_PATH + "imgres.htm");
//        webFiles.add(CASE_PATH + "Copy.htm");
//        webFiles.add(CASE_PATH + "اخبار مظاهرات الرياض 2011.htm");
//
//        // add xml files
//        xmlFiles.add(CASE_PATH + "related6604.xml");
//
//        // add text files
//        textFiles.add(CASE_PATH + "4318.txt");
//        textFiles.add(CASE_PATH + "vdcivvaw.t1aqu2csct.txt");
//        textFiles.add(CASE_PATH + "vdcex78p.jh8wwibdbj.txt");
//    }
//    private List<String> rarFiles;
//    private List<String> zipFiles;
//    private List<String> docFiles;
//    private List<String> docxFiles;
//    private List<String> pptFiles;
//    private List<String> imagesFiles;
//    private List<String> webFiles;
//    private List<String> xmlFiles;
//    private List<String> textFiles;
//    private static final String CASE_PATH = "C:\\DEM_CASE\\";
//    private static final String OUTPUT_PATH = "C:\\out";
//}
