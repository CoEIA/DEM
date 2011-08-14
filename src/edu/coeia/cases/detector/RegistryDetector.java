/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases.detector;

import edu.coeia.utility.FilesPath;

import java.util.List;
import java.util.ArrayList ;

import java.io.File ;

/**
 *
 * @author wajdyessam
 */
public class RegistryDetector implements AutoDetection {
    
    @Override
    public List<String> getFiles() {
        ArrayList<String> msnPath = new ArrayList<String>();
        File[] roots = File.listRoots();

        if (FilesPath.getOSType() == FilesPath.OS_TYPE.XP) {
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
}
