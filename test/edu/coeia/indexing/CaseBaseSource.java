/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import java.util.List ;
import java.util.ArrayList ;

import java.io.File ;

public class CaseBaseSource {
    
    protected void fillDocuments() {
        zips = new ArrayList<String>();
        zips.add(SOURCE_PATH + "data.zip");
        zips.add(SOURCE_PATH + "data2.zip");
        zips.add(SOURCE_PATH + "data3.zip");
        zips.add(SOURCE_PATH + "pass.zip");
    }
    
   protected void initTikaExtractorTest() {
       files.add(new File(SOURCE_PATH + "PlainText.txt"));
       files.add(new File(SOURCE_PATH + "test.docx"));
       files.add(new File(SOURCE_PATH + "RTF.rtf"));
       files.add(new File(SOURCE_PATH + "aa.pdf"));

       mimeType = "text/plain";
   }
   
   protected void fillContainerDocuments() {
        containersList = new ArrayList<String>();
        containersList.add(SOURCE_PATH + "DEMManual.docx");
        containersList.add(SOURCE_PATH + "DEM - Expo.pptx");
        containersList.add(SOURCE_PATH + "a.pdf");
        containersList.add(SOURCE_PATH + "Form for exhibition.docx");
    }
    
   protected void initForIndexFactoryTest() {
        rarFiles = new ArrayList<String>();
        zipFiles = new ArrayList<String>();
        docFiles = new ArrayList<String>();
        pptFiles = new ArrayList<String>();
        imagesFiles = new ArrayList<String>();
        webFiles = new ArrayList<String>();
        xmlFiles = new ArrayList<String>();
        textFiles = new ArrayList<String>();
        
        // add rar files for testing
        rarFiles.add(SOURCE_PATH + "DEM pics.rar");
        rarFiles.add(SOURCE_PATH + "New Text Document.rar");

        // add zip files for testing
        zipFiles.add(SOURCE_PATH + "data.zip");
        zipFiles.add(SOURCE_PATH + "data2.zip");
        zipFiles.add(SOURCE_PATH + "pass.zip");

        // add doc files for testing
        docFiles.add(SOURCE_PATH + "MSWord.doc");
        docFiles.add(SOURCE_PATH + "DEMManual.docx");
        docFiles.add(SOURCE_PATH + "DEM_OnePage.docx");
        docFiles.add(SOURCE_PATH + "test.docx");

        // add ppt files
        pptFiles.add(SOURCE_PATH + "DEMExpoold.pptx");
        pptFiles.add(SOURCE_PATH + "DEM - Expo.pptx");

        // add images files
        imagesFiles.add(SOURCE_PATH + "a.jpeg");
        imagesFiles.add(SOURCE_PATH + "b.jpeg");
        imagesFiles.add(SOURCE_PATH + "c.jpeg");

        // add html pages
        webFiles.add(SOURCE_PATH + "HTML.html");

        // add xml files
        xmlFiles.add(SOURCE_PATH + "addressbook.xml");

        // add text files
        textFiles.add(SOURCE_PATH + "unicodeTxt.txt");
        textFiles.add(SOURCE_PATH + "PlainText.txt");
        textFiles.add(SOURCE_PATH + "New Text Document.txt");
    }
    
    protected List<String> rarFiles;
    protected List<String> zipFiles;
    protected List<String> docFiles;
    protected List<String> pptFiles;
    protected List<String> imagesFiles;
    protected List<String> webFiles;
    protected List<String> xmlFiles;
    protected List<String> textFiles;
    
    protected List<String> containersList ;
    protected List<File> files = new ArrayList<File>();
    protected List<String> zips ;
    
    protected String mimeType; 

    protected static final String OUTPUT_PATH = "C:\\out";
    protected static final String SOURCE_PATH = "C:\\data\\" ;
}
