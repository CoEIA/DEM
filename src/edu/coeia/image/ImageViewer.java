/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.image;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.cases.Case ;
import edu.coeia.main.util.FilesPath ;

import java.util.List ;

import java.io.IOException ;
import java.util.Collections;

public class ImageViewer {
    private List<String> imagesPath = Collections.emptyList();

    public List<String> getInstance(Case index) throws IOException {
        ImageReader ir = new ImageReader(index.getIndexLocation() + "\\" + FilesPath.INDEX_PATH);
        imagesPath = ir.getImagesPath();
        ir.close();
        
        return imagesPath ;
    }
}
