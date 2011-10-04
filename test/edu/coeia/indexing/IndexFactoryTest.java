/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import org.junit.Before;
import org.junit.Test ;
import static org.junit.Assert.*;

import java.util.List ;
import java.util.ArrayList ;

import java.io.File ;
import java.io.IOException; 
import org.apache.tika.exception.TikaException;

public class IndexFactoryTest {

    @Test
    public void rarFilesMimeTest() {
        for(String rarFile: rarFiles) {
            try {
                assertEquals(ArchiveIndexer.class.getName(), IndexerFactory.getIndexer(new File(rarFile), true).getClass().getName());
            }
            catch(IOException e){
                System.out.println("File: " + rarFile + " is not existing in CASE_PATH");
            }
            catch(TikaException e) {
            }
        }
    }
    
    @Test
    public void zipFilesMimeTest() {
        for(String zipFile: zipFiles) {
            try {
                assertEquals(ArchiveIndexer.class.getName(), IndexerFactory.getIndexer(new File(zipFile), true).getClass().getName());
            }
            catch(IOException e){
                System.out.println("File: " + zipFile + " is not existing in CASE_PATH");
            }
            catch(TikaException e) {
            }
        }
    }
      
    @Test
    public void docFilesMimeTest() {
        for(String docFile: docFiles) {
            try {
                assertEquals(DocumentIndexer.class.getName(), IndexerFactory.getIndexer(new File(docFile), true).getClass().getName());
            }
            catch(IOException e){
                System.out.println("File: " + docFile + " is not existing in CASE_PATH");
            }
            catch(TikaException e) {
            }
        }
    }
    
    @Test
    public void ImageFilesMimeTest() {
        for(String imageFile: imagesFiles) {
            try {
                assertEquals(ImageIndexer.class.getName(), IndexerFactory.getIndexer(new File(imageFile), true).getClass().getName());
            }
            catch(IOException e){
                System.out.println("File: " + imageFile + " is not existing in CASE_PATH");
            }
            catch(TikaException e) {
            }
        }
    }
        
    @Test
    public void xmlFilesMimeTest() {
        for(String xmlFile: xmlFiles) {
            try {
                assertEquals(DocumentIndexer.class.getName(), IndexerFactory.getIndexer(new File(xmlFile), true).getClass().getName());
            }
            catch(IOException e){
                System.out.println("File: " + xmlFile + " is not existing in CASE_PATH");
            }
            catch(TikaException e) {
            }
        }
    }
        
    @Test
    public void textFilesMimeTest() {
        for(String textFile: textFiles) {
            try {
                assertEquals(DocumentIndexer.class.getName(), IndexerFactory.getIndexer(new File(textFile), true).getClass().getName());
            }
            catch(IOException e){
                System.out.println("File: " + textFile + " is not existing in CASE_PATH");
            }
            catch(TikaException e) {
            }
        }
    }
            
    @Test
    public void webFilesMimeTest() {
        for(String webFile: webFiles) {
            try {
                assertEquals(DocumentIndexer.class.getName(), IndexerFactory.getIndexer(new File(webFile), true).getClass().getName());
            }
            catch(IOException e){
                System.out.println("File: " + webFile + " is not existing in CASE_PATH");
            }
            catch(TikaException e) {
            }
        }
    }
    
    @Before
    public void initDocuments() {
        rarFiles = new ArrayList<String>();
        zipFiles = new ArrayList<String>();
        docFiles = new ArrayList<String>();
        docxFiles = new ArrayList<String>();
        pptFiles = new ArrayList<String>();
        imagesFiles = new ArrayList<String>();
        webFiles = new ArrayList<String>();
        xmlFiles = new ArrayList<String>();
        textFiles = new ArrayList<String>();
        
        fillDocuments();
    }
    
    private void fillDocuments() {
        // add rar files for testing
        rarFiles.add(CASE_PATH + "134.rar"); 
        rarFiles.add(CASE_PATH + "test.rar");
        
        // add zip files for testing
        zipFiles.add(CASE_PATH + "2.zip");
        zipFiles.add(CASE_PATH + "a.zip");  
        zipFiles.add(CASE_PATH + "dasfas.zip"); 
        
        // add doc files for testing
        docFiles.add(CASE_PATH + "41.doc");
        docFiles.add(CASE_PATH + "Dr.Alzahrani Khtab.doc");
        docFiles.add(CASE_PATH + "fdeha_book52.doc");
        docFiles.add(CASE_PATH + "بحث الارهاب.doc");
        
        // add docx files
        
        // add ppt files
        
        // add images files
        imagesFiles.add(CASE_PATH + "1254550999.jpg");
        imagesFiles.add(CASE_PATH + "images (9).jpg");
        imagesFiles.add(CASE_PATH + "1.jpg");
        imagesFiles.add(CASE_PATH + "a\\mwa2.gif");
        imagesFiles.add(CASE_PATH + "search.png");
        
        // add html pages
        webFiles.add(CASE_PATH + "imgres.htm");
        webFiles.add(CASE_PATH + "Copy.htm");
        webFiles.add(CASE_PATH + "اخبار مظاهرات الرياض 2011.htm");
        
        // add xml files
        xmlFiles.add(CASE_PATH + "related6604.xml");
        
        // add text files
        textFiles.add(CASE_PATH + "4318.txt");
        textFiles.add(CASE_PATH + "vdcivvaw.t1aqu2csct.txt");
        textFiles.add(CASE_PATH + "vdcex78p.jh8wwibdbj.txt");
    }
    
    private List<String> rarFiles ;
    private List<String> zipFiles ;
    private List<String> docFiles ;
    private List<String> docxFiles;
    private List<String> pptFiles;
    private List<String> imagesFiles ;
    private List<String> webFiles ;
    private List<String> xmlFiles ;
    private List<String> textFiles ;
    
    private static final String CASE_PATH = "C:\\DEM_CASE\\" ;
}
