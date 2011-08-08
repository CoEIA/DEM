/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.utility;

/**
 *
 * @author wajdyessam
 */

import java.util.ArrayList ;

import java.io.File ;
import java.io.FileFilter ;

public class FilesCounter implements FileFilter {
    private int numberOfFiles ;
    private long size ;
    private ArrayList<String> ext ;

    public FilesCounter (ArrayList<String> ext) {
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
