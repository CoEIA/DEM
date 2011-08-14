/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases.detector;

import edu.coeia.main.util.FilesPath;

import java.util.List;
import java.util.ArrayList ;

import java.io.File ;

/**
 *
 * @author wajdyessam
 */

public class YahooDetector implements AutoDetection{
    
    @Override
    public List<String> getFiles() {
        ArrayList<String> yahooPaths = new ArrayList<String>();
        File[] roots = File.listRoots();

        if (FilesPath.getOSType() == FilesPath.OS_TYPE.XP) {
            for (File file : roots) {
                String filePath = file.getAbsolutePath() + "\\" + "Program Files\\Yahoo!\\Messenger\\Profiles";
                File yahooDir = new File(filePath);

                if (yahooDir.exists()) {
                    File[] yahooFiles = yahooDir.listFiles();

                    for (File yahooFile : yahooFiles) {
                        String yahooFilePatn = yahooFile.getAbsolutePath();
                        yahooPaths.add(yahooFilePatn);
                    }
                }
            }
        } else { // C:\Users\<username>\AppData\Local\VirtualStore\Program Files\Yahoo!\Messenger\Profiles\<userid>\Archive\Messages\<buddyid>
            for (File file : roots) {
                String filePath = file.getAbsolutePath() + "\\" + "Users";
                File yahooDir = new File(filePath);

                if (yahooDir.exists()) {
                    File[] yahooFiles = yahooDir.listFiles();

                    for (File yahooFile : yahooFiles) {
                        String yahooFolderPath = yahooFile.getAbsolutePath() + "\\AppData\\Local\\VirtualStore\\Program Files\\Yahoo!\\Messenger\\Profiles";

                        File yahooFolderFile = new File(yahooFolderPath);
                        if (yahooFolderFile.exists()) {
                            String ieFilePath = yahooFolderFile.getAbsolutePath();
                            yahooPaths.add(ieFilePath);
                        }
                    }
                }
            }
        }

        return yahooPaths;
    }
}
