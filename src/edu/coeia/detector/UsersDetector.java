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
public class UsersDetector implements AutoDetection {
   
    @Override
    public List<String> getFilesInPathInternet (List<String> path) {
        List<String> resultPath = new ArrayList<String>();
         File[] roots = File.listRoots();

        if (ApplicationConstants.getOSType() == ApplicationConstants.OS_TYPE.XP) {
            for (String s : path) {
                String filePath = s + "\\" + "Documents and Settings";
                File osFile = new File(filePath);

                if (osFile.exists()) {
                    File[] files = osFile.listFiles();

                    for (File userFile : files) {
                        if (!userFile.isHidden() && userFile.canRead()) {
                            resultPath.add(userFile.getAbsolutePath());
                        }
                    }
                }
            }
        } else {
            for (String s : path) {
                String filePath = s + "\\" + "Users";
                File osFile = new File(filePath);

                if (osFile.exists()) {
                    File[] files = osFile.listFiles();

                    for (File userFile : files) {
                        if (!userFile.isHidden() && userFile.canRead()) {
                            resultPath.add(userFile.getAbsolutePath());
                        }
                    }
                }
            }
        }

        return resultPath;
    }
    
    @Override
    public List<String> getFilesInCurrentSystem() {
        ArrayList<String> iePaths = new ArrayList<String>();
        File[] roots = File.listRoots();

        if (ApplicationConstants.getOSType() == ApplicationConstants.OS_TYPE.XP) {
            for (File file : roots) {
                String filePath = file.getAbsolutePath() + "\\" + "Documents and Settings";
                File osFile = new File(filePath);

                if (osFile.exists()) {
                    File[] files = osFile.listFiles();

                    for (File userFile : files) {
                        if (!userFile.isHidden() && userFile.canRead()) {
                            iePaths.add(userFile.getAbsolutePath());
                        }
                    }
                }
            }
        } else {
            for (File file : roots) {
                String filePath = file.getAbsolutePath() + "\\" + "Users";
                File osFile = new File(filePath);

                if (osFile.exists()) {
                    File[] files = osFile.listFiles();

                    for (File userFile : files) {
                        if (!userFile.isHidden() && userFile.canRead()) {
                            iePaths.add(userFile.getAbsolutePath());
                        }
                    }
                }
            }
        }

        return iePaths;
    }

    public List<String> getFilesInPath(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
