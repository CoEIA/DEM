/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.detector;

import java.util.List;
import java.util.ArrayList ;

/**
 *
 * @author wajdyessam
 */

public class MSNDetector implements AutoDetection {
    
    @Override
    public List<String> getFilesInPath (String path) {
        List<String> resultPath = new ArrayList<String>();
        
        return resultPath;
    }
    
    @Override
    public List<String> getFilesInCurrentSystem() {
        return null;
    }

    public List<String> getFilesInPathInternet(List<String> path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
