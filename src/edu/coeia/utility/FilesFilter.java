/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.utility;

/**
 *
 * @author wajdyessam
 */

import java.io.File ;
import javax.swing.filechooser.FileFilter;

public class FilesFilter extends FileFilter {

    private String[] ext;
    private String desc ;

    public FilesFilter (String desc, String ... ext ) {
        this.ext = ext ;
        this.desc = desc ;
    }

    public boolean accept(File file) {
        for (String ex: ext)
            if ( file.getName().endsWith(ex) )
                return true;

        return false ;
    }

    public String getDescription() {
        return desc ;
    }
}
