/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.detector;

import edu.coeia.util.FilesPath;

import java.util.List;
import java.util.ArrayList ;

import java.io.File ;

/**
 *
 * @author wajdyessam
 */

public class FirefoxDetector implements AutoDetection{
 
    
    @Override
    public List<String> getFiles() {
        ArrayList<String> ffPaths = new ArrayList<String>();
        File[] roots = File.listRoots();

        if (FilesPath.getOSType() == FilesPath.OS_TYPE.XP) {
            for (File file : roots) {
                String filePath = file.getAbsolutePath() + "\\" + "Documents and Settings";
                File osFile = new File(filePath);

                if (osFile.exists()) {
                    File[] files = osFile.listFiles();

                    for (File userFile : files) {
                        String outlookFolderPath = userFile.getAbsolutePath() + "\\Application Data\\Mozilla\\Firefox\\Profiles";

                        File outlookFolderFile = new File(outlookFolderPath);
                        if (outlookFolderFile.exists()) {
                            File[] outlookFiles = outlookFolderFile.listFiles();

                            for (File outlookFile : outlookFiles) {
                                if (outlookFile.isDirectory()) {
                                    String outlookFilePath = outlookFile.getAbsolutePath();
                                    ffPaths.add(outlookFilePath);
                                }
                            }
                        }
                    }
                }
            }
        } else { //C:\Users\Wajdy Essam\AppData\Roaming\Mozilla\Firefox\Profiles\9bh2w0j2.default
            for (File file : roots) {
                String filePath = file.getAbsolutePath() + "\\" + "Users";
                File osFile = new File(filePath);

                if (osFile.exists()) {
                    File[] files = osFile.listFiles();

                    for (File userFile : files) {
                        String ieFolderPath = userFile.getAbsolutePath() + "\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles";

                        File ieFolderFile = new File(ieFolderPath);
                        if (ieFolderFile.exists()) {
                            File[] outlookFiles = ieFolderFile.listFiles();

                            for (File outlookFile : outlookFiles) {
                                if (outlookFile.isDirectory()) {
                                    String outlookFilePath = outlookFile.getAbsolutePath();
                                    ffPaths.add(outlookFilePath);
                                }
                            }
                        }
                    }
                }
            }
        }

        return ffPaths;
    }
}
