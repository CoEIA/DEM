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

public class YahooDetector implements AutoDetection{
    
    @Override
    public List<String> getFilesInPath (String path) {
        List<String> resultPath = new ArrayList<String>();
        
        return resultPath;
    }
    
    @Override
    public List<String> getFilesInCurrentSystem() {
        List<String> yahooPaths = new ArrayList<String>();
        File[] roots = File.listRoots();

        if (ApplicationConstants.getOSType() == ApplicationConstants.OS_TYPE.XP) {
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

    public List<String> getFilesInPathInternet(List<String> path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
