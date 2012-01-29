/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

import edu.coeia.util.FileUtil;
import edu.coeia.util.FilesPath;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wajdyessam
 */

public final class CaseManager {
    private final Case aCase ;
    
    public static CaseManager newInstance (final Case aCase) {
        return new CaseManager(aCase);
    }
    
    /**
     * create the folder structure for the current case object
     * options and return true if their is no problems
     */
    public boolean createNewCase() throws IOException {
        this.createCaseFoldersStructure();
        return true;
    }
    
    /**
     * update case existing information 
     * by removing old file and write the new information file 
     * with the new information contained in the current instance
     * of Case object
     * 
     * @throws IOException 
     */
    public void updateCaseInformation() throws IOException {
        FileUtil.createFile(this.getCaseInformationFileLocation()); 
        FileUtil.writeObject(this.aCase, new File(this.getCaseInformationFileLocation()));
        this.updateCaseToInfoFile();
    }
    
    public void updateCaseToInfoFile() throws IOException {
        List<String> indexPtr = FileUtil.getFileContentInArrayList(new File(FilesPath.INDEXES_INFO) );
        List<String> newIndexPtr = new ArrayList<String>();

        for (String line: indexPtr) {
            String name = line.split("-")[0].trim();
            String path = line.substring(line.indexOf("-") + 1).trim();

            if ( name.equals(aCase.getCaseName()) && path.equals(aCase.getCaseLocation()))
                continue ;

            newIndexPtr.add(line);
        }
        
        String newLine = aCase.getCaseName() + " - " + aCase.getCaseLocation();
        newIndexPtr.add(newLine);

        // write new index information to file
        FileUtil.writeToFile(newIndexPtr, FilesPath.INDEXES_INFO);
    }
    
    // add entry to indexes info file
    public void writeCaseToInfoFile () throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(FilesPath.INDEXES_INFO), true));
        writer.println(aCase.getCaseName() + " - " + aCase.getCaseLocation());
        writer.close();
    }
    
    public boolean isCaseHaveChangedSource() throws IOException {
        return !CasePathHandler.newInstance(aCase.getCaseLocation()).getChangedEntries().isEmpty();
    }
    
    public boolean removeCase() throws Exception {
        boolean status = false; 
        
        File file = new File( aCase.getCaseLocation() );

        if ( FileUtil.removeDirectory(file) ) {
            List<String> indexPtr = FileUtil.getFileContentInArrayList(new File(FilesPath.INDEXES_INFO) );
            List<String> newIndexPtr = new ArrayList<String>();

            for (String line: indexPtr) {
                String name = line.split("-")[0].trim();
                String path = line.substring(line.indexOf("-") + 1).trim();

                if ( name.equals(aCase.getCaseName()) && path.equals(aCase.getCaseLocation()))
                    continue ;

                newIndexPtr.add(line);
            }

            // write new index information to file
            FileUtil.writeToFile(newIndexPtr, FilesPath.INDEXES_INFO);

            // remove case history from preferences
            CaseHistoryHandler.remove(aCase.getCaseName());
            
            status = true;
        }
        
        return status;
    }
    
    public String getCaseFolderLocation() { 
        return this.aCase.getCaseLocation();
    }
    
    public String getIndexFolderLocation() { 
        return aCase.getCaseLocation() + File.separator + FilesPath.INDEX_PATH;
    }
    
    public String getImageFolderLocation() {
        return aCase.getCaseLocation() +  File.separator + FilesPath.IMAGES_PATH;
    }
    
    public String getCaseInformationFileLocation() {
        return aCase.getCaseLocation() + File.separator + aCase.getCaseName() + ".DAT";
    }
    
    public String getCaseLogFileLocation() {
        return aCase.getCaseLocation() + File.separator + aCase.getCaseName() + ".LOG";
    }
    
    public String getCaseArchiveOutputFolderLocation() {
        return aCase.getCaseLocation() + File.separator + FilesPath.CASE_TMP;
    }
    
    public String getCaseOfflineEmailAttachmentLocation() {
        return aCase.getCaseLocation() + File.separator + FilesPath.OFFLINE_EMAIL_ATTACHMENTS;
    }
    
    private void createCaseFoldersStructure () throws IOException {
        FileUtil.createFolder(this.getCaseFolderLocation());    // CASE Parent Folder
        FileUtil.createFolder(this.getIndexFolderLocation());   // INDEX folder
        FileUtil.createFolder(this.getImageFolderLocation());   // IMAGE folder
        FileUtil.createFolder(this.getCaseArchiveOutputFolderLocation());   // ARCHIVE folder
        FileUtil.createFolder(this.getCaseOfflineEmailAttachmentLocation()); // OFFLINE Email Attachments
        
        // Write Case information inside .DAT file and also make LOG file
        FileUtil.createFile(this.getCaseLogFileLocation());
        FileUtil.createFile(this.getCaseInformationFileLocation()); 
        FileUtil.writeObject(aCase, new File(this.getCaseInformationFileLocation()));

        // create case configuration file and write path mapping on it
        CasePathHandler handler = CasePathHandler.newInstance(aCase.getCaseLocation());
        for(String path: aCase.getEvidenceSourceLocation()) {
            handler.add(new File(path));
        }
        handler.saveConfiguration();
    }
    
    private CaseManager (final Case aCase) {
        this.aCase = aCase;
    }
}
