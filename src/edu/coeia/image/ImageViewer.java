/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.image;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.index.IndexerReader ;
import edu.coeia.index.IndexInformation ;
import edu.coeia.utility.FilesPath ;

import java.util.List ;

import java.io.IOException ;

public class ImageViewer {
    private static List<String> imagesPath ;

    public static List<String> getInstance(IndexInformation index) throws IOException {
        if ( imagesPath == null ) {
            IndexerReader ir = new IndexerReader(index.getIndexLocation() + "\\" + FilesPath.INDEX_PATH);
            imagesPath = ir.getImagesPath();
            ir.close();
        }

        return imagesPath ;
    }
}
