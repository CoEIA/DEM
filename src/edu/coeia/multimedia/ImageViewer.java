///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package edu.coeia.multimedia;
//
///**
// *
// * @author wajdyessam
// */
//
//import edu.coeia.cases.Case ;
//import edu.coeia.util.FilesPath ;
//
//import java.util.List ;
//import java.util.ArrayList;
//
//public class ImageViewer {
//
//    public static List<String> getInstance(Case index) throws Exception {
//        List<String> imagesPath = new ArrayList<String>();
//
//        MultimediaReader ir = new MultimediaReader(index.getCaseLocation() + "\\" + FilesPath.INDEX_PATH);
//        imagesPath.addAll(ir.getListPathsFromIndex(MultimediaReader.TYPE.IMAGE,index));
//        ir.close();
//
//        return imagesPath ;
//    }
//}
