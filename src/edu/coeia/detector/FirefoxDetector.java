/* 
 * Copyright (C) 2014 Center of Excellence in Information Assurance
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.coeia.detector;

import edu.coeia.constants.OpreatingSystemConstants;

import java.util.List;
import java.util.ArrayList ;

import java.io.File ;

/**
 *
 * @author wajdyessam
 */

public class FirefoxDetector implements AutoDetection{
 
    @Override
    public List<String> getFilesInPathInternet (List<String> path) {
        List<String> resultPath = new ArrayList<String>();
         File[] roots = File.listRoots();

        if (OpreatingSystemConstants.getOSType() == OpreatingSystemConstants.OS_TYPE.XP) {
            for (String s : path) {
                String filePath = s + "\\" + "Documents and Settings";
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
                                    resultPath.add(outlookFilePath);
                                }
                            }
                        }
                    }
                }
            }
        } else { //C:\Users\Wajdy Essam\AppData\Roaming\Mozilla\Firefox\Profiles\9bh2w0j2.default
            for (String s : path) {
                String filePath = s + "\\" + "Users";
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
                                    resultPath.add(outlookFilePath);
                                }
                            }
                        }
                    }
                }
            }
        }
 
        return resultPath;
    }
}
