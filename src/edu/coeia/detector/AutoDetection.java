/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.detector;

import java.util.List ;

/**
 *
 * To Detect Some Files/Folders on The Current File System
 * and detect files relative to path
 * 
 * @author wajdyessam
 * @version 10/8/2011
 * 
 */

public interface AutoDetection {
    public List<String> getFilesInCurrentSystem();
    public List<String> getFilesInPathInternet(List<String> path);
}
