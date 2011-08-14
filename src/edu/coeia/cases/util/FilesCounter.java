/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.cases.util;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.main.util.Utilities;
import java.util.List ;

import java.io.File ;
import java.io.FileFilter ;

public class FilesCounter implements FileFilter {
    private int numberOfFiles ;
    private long size ;
    private List<String> ext ;

    public FilesCounter (List<String> ext) {
        this.ext = ext ;
    }
    
    public boolean accept (File path) {
        if ( path.isFile() && Utilities.isExtentionAllowed(ext, Utilities.getExtension(path))) {
            numberOfFiles++;
            size += path.length();
        }
        else if ( path.isDirectory() )
            path.listFiles(this);

        return false;
    }

    public int getNumberOfFiles () {
        return this.numberOfFiles;
    }

    public long getSize () {
        return this.size ;
    }
}
