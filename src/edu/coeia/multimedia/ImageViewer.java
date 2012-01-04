/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.multimedia;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.cases.Case ;
import edu.coeia.util.FilesPath ;

import java.util.List ;

import java.io.IOException ;

public class ImageViewer {
    private static List<String> imagesPath ;

    public static List<String> getInstance(Case index) throws IOException, Exception {
        if ( imagesPath == null ) {
            MultimediaReader ir = new MultimediaReader(index.getCaseLocation() + "\\" + FilesPath.INDEX_PATH);
            imagesPath = ir.getListPathsFromIndex(MultimediaReader.Operations.Images);
            ir.close();
        }

        return imagesPath ;
    }
}
