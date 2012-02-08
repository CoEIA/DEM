/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.detector;

import edu.coeia.constants.ApplicationConstants;

import java.util.List;
import java.util.ArrayList ;

import java.io.File ;

/**
 *
 * @author wajdyessam
 */
public class RegistryDetector implements AutoDetection {
    
    @Override
    public List<String> getFilesInPath (String path) {
        List<String> resultPath = new ArrayList<String>();
        
        return resultPath;
    }
    
    @Override
    public List<String> getFilesInCurrentSystem() {
        ArrayList<String> msnPath = new ArrayList<String>();
        File[] roots = File.listRoots();

        if (ApplicationConstants.getOSType() == ApplicationConstants.OS_TYPE.XP) {
            for (File file : roots) {
                String filePath = file.getAbsolutePath() + "\\" + "WINDOWS\\system32";
                File osFile = new File(filePath);

                if (osFile.exists()) {
                    File[] files = osFile.listFiles();

                    for (File userFile : files) {
                        if (userFile.getName().equalsIgnoreCase("reg.exe")) {
                            msnPath.add(userFile.getAbsolutePath());
                        }
                    }
                }
            }
        } else {
            for (File file : roots) {
                String filePath = file.getAbsolutePath() + "\\" + "WINDOWS\\system32";
                File osFile = new File(filePath);

                if (osFile.exists()) {
                    File[] files = osFile.listFiles();

                    for (File userFile : files) {
                        if (userFile.getName().equalsIgnoreCase("reg.exe")) {
                            msnPath.add(userFile.getAbsolutePath());
                        }
                    }
                }
            }
        }

        return msnPath;
    }

    public List<String> getFilesInPathInternet(List<String> path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
