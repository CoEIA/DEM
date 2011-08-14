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

public class OutlookDetector implements AutoDetection {
    
    
    @Override
    public List<String> getFiles() {
        List<String> pstPaths = new ArrayList<String>();
        
        File[] roots = File.listRoots();

        if (FilesPath.getOSType() == FilesPath.OS_TYPE.XP) {
            for (File file : roots) {
                String filePath = file.getAbsolutePath() + "\\" + "Documents and Settings";
                File osFile = new File(filePath);

                if (osFile.exists()) {
                    File[] files = osFile.listFiles();

                    for (File userFile : files) {
                        String outlookFolderPath = userFile.getAbsolutePath() + "\\Local Settings\\Application Data\\Microsoft\\Outlook";

                        File outlookFolderFile = new File(outlookFolderPath);
                        if (outlookFolderFile.exists()) {
                            File[] outlookFiles = outlookFolderFile.listFiles();

                            for (File outlookFile : outlookFiles) {
                                String outlookFilePath = outlookFile.getAbsolutePath();
                                if (outlookFile.isFile() && /*( outlookFilePath.endsWith("pst") ||*/ outlookFilePath.endsWith("ost")) {
                                    if (!outlookFile.getName().contains("archive")) {
                                        pstPaths.add(outlookFilePath);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else { // C:\Users\xx\AppData\Local\Microsoft\Outlook\Outlook.pst \
            for (File file : roots) {
                String filePath = file.getAbsolutePath() + "\\" + "Users";
                File pstFile = new File(filePath);

                if (pstFile.exists()) {
                    File[] pstFiles = pstFile.listFiles();

                    for (File userFile : pstFiles) {
                        String pstFolderPath = userFile.getAbsolutePath() + "\\AppData\\Local\\Microsoft\\Outlook";

                        File pstFolderFile = new File(pstFolderPath);
                        if (pstFolderFile.exists()) {
                            File[] outlookFiles = pstFolderFile.listFiles();

                            for (File outlookFile : outlookFiles) {
                                String outlookFilePath = outlookFile.getAbsolutePath();
                                if (outlookFile.isFile() && /*( outlookFilePath.endsWith("pst") ||*/ outlookFilePath.endsWith("ost")) {
                                    if (!outlookFile.getName().contains("archive")) {
                                        pstPaths.add(outlookFilePath);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return pstPaths;
    }
}
