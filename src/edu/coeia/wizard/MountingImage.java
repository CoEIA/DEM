/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.wizard;

import edu.coeia.constants.ApplicationConstants;
import edu.coeia.util.FileUtil;

import java.util.List;

/**
 * Mount Image (iso, encase and some other binary images type)
 * by calling OFSmount command line tool to do the mounting operation
 * 
 * @author wajdyessam
 */

public class MountingImage {
    private final String fileName;

    public MountingImage(final String fileName) { 
        this.fileName = fileName;
    }

    /**
     * mounting the image in drive ( the new of the drive will be the next available one)
     * 
     * @return the path of new drive that contain the files inside the image
     * @throws Exception if there is some error in mounting the image file
     */
    public String mount() throws Exception{
        String applicationParameter = String.format(ApplicationConstants.mountLuncher, this.fileName);
        List<String> output = FileUtil.readProgramOutputStream(applicationParameter);

        if ( haveError(output) ) 
            throw new Exception("Cannot mount this image file: " + this.fileName);

        return getDriveLetter(output);
    }

    /**
     * Check to see if the output have error then throws exception
     * to the user know about this problem
     */
    private boolean haveError(final List<String> result) throws Exception{
        for(String line: result) {
            if ( line.contains("Done.") ) {
                return false;
            }
        }

        return true;
    }

    /**
     * get the new drive letter from the result
     * because its auto generated and we don't know the name of the 
     * new generated drive, so you parsing until find the letter
     *
     * the format of the line that contain the drive letter is:
     * 				Created device 0: G: -> XPHash.E01
     * in the above example the drive letter is G:\\
     */
    private String getDriveLetter(final List<String> result) {
        for(String line: result) {
            if ( line.contains("Created device")) {
                String letter = line.split(":")[1];
                String fullDrivePath = letter + ":\\";	// letter + : + \
                return fullDrivePath.trim();
            }
        }

        return "";
    }
}
