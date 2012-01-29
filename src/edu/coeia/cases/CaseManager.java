/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

import edu.coeia.util.FileUtil;
import edu.coeia.util.FilesPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Case Manger will create, update and remove cases from application
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
    public boolean createCase() throws IOException {
        this.createCaseFoldersStructure();
        this.saveCaseInformation();
        this.updateCasesInformationFile();
        return true;
    }
    
    /**
     * update case existing information 
     * by removing old file and write the new information file 
     * with the new information contained in the current instance
     * of Case object
     * 
     * and then update information file by replacing the case that
     * have the old path with the current information
     * 
     * @throws IOException 
     */
    public void updateCase(final String oldCaseName, final String oldCaseLocation) throws IOException {
        FileUtil.createFile(this.getCaseInformationFileLocation()); 
        this.saveCaseInformation();
        this.replaceCaseLocation(oldCaseName, oldCaseLocation);
    }
    
    /**
     *  Remove the case folder then
     *  update information file and also history
     *  the update will occur even if their is problems when deleting some files
     *  inside the case, so we can know that we read completed case
     *  and no exception occur during reading not-removed correctly case
     * 
     * @return the status of removing case folder
     * @throws Exception 
     */
    public boolean removeCase() throws Exception {
        boolean status = FileUtil.removeDirectory(this.getCaseFolderLocation());
        
        List<String> otherCasesGroup = this.getOtherCases(this.aCase.getCaseName(), this.getCaseFolderLocation());
        FileUtil.writeToFile(otherCasesGroup, getCasesInformationFileLocation());

        // remove case history from preferences
        CaseHistoryHandler.remove(aCase.getCaseName());
        
        return status;
    }
    
    public boolean isCaseHaveChangedSource() throws IOException {
        return !CasePathHandler.newInstance(this.getCaseFolderLocation()).getChangedEntries().isEmpty();
    }
        
    public String getCasesInformationFileLocation() {
        return FilesPath.INDEXES_INFO;
    }
    
    public String getCaseFolderLocation() { 
        return this.aCase.getCaseLocation();
    }
    
    public String getIndexFolderLocation() { 
        return aCase.getCaseLocation() 
                + File.separator 
                + FilesPath.INDEX_PATH;
    }
    
    public String getImageFolderLocation() {
        return aCase.getCaseLocation() 
                +  File.separator 
                + FilesPath.IMAGES_PATH;
    }
    
    public String getCaseInformationFileLocation() {
        return aCase.getCaseLocation() 
                + File.separator 
                + aCase.getCaseName() 
                + FilesPath.DEM_CASE_INFO_EXTENSION;
    }
    
    public String getCaseLogFileLocation() {
        return aCase.getCaseLocation() 
                + File.separator 
                + aCase.getCaseName() 
                + FilesPath.DEM_CASE_LOG_EXTENSION;
    }
    
    public String getCaseArchiveOutputFolderLocation() {
        return aCase.getCaseLocation() 
                + File.separator 
                + FilesPath.CASE_TMP;
    }
    
    public String getCaseOfflineEmailAttachmentLocation() {
        return aCase.getCaseLocation() 
                + File.separator 
                + FilesPath.OFFLINE_EMAIL_ATTACHMENTS;
    }
    
    private List<String> getOtherCases(final String name, final String path) throws FileNotFoundException {
        List<String> originalCases = FileUtil.getFileContentInList(new File(getCasesInformationFileLocation()));
        List<String> otherCases = new ArrayList<String>();

        for (String line: originalCases) {
            String otherCaseName = line.split("-")[0].trim();
            String otherCasePath = line.substring(line.indexOf("-") + 1).trim();

            if ( otherCaseName.equals(name) && otherCasePath.equals(path))
                continue ;

            otherCases.add(line);
        }
        
        return otherCases;
    }
        
    private void saveCaseInformation() throws IOException {
        FileUtil.writeObject(aCase, new File(this.getCaseInformationFileLocation()));
    }
    
    private void updateCasesInformationFile () throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(this.getCasesInformationFileLocation()), true));
        writer.println(aCase.getCaseName() + " - " + aCase.getCaseLocation());
        writer.close();
    }
        
    private void createCaseFoldersStructure () throws IOException {
        FileUtil.createFolder(this.getCaseFolderLocation());    // CASE Parent Folder
        FileUtil.createFolder(this.getIndexFolderLocation());   // INDEX folder
        FileUtil.createFolder(this.getImageFolderLocation());   // IMAGE folder
        FileUtil.createFolder(this.getCaseArchiveOutputFolderLocation());   // ARCHIVE folder
        FileUtil.createFolder(this.getCaseOfflineEmailAttachmentLocation()); // OFFLINE Email Attachments
        
        // create LOG and information (.DAT) file
        FileUtil.createFile(this.getCaseLogFileLocation());
        FileUtil.createFile(this.getCaseInformationFileLocation()); 
        
        // create case configuration file and write path mapping on it
        CasePathHandler handler = CasePathHandler.newInstance(aCase.getCaseLocation());
        for(String path: aCase.getEvidenceSourceLocation()) {
            handler.add(new File(path));
        }
        handler.saveConfiguration();
    }
    
    private void replaceCaseLocation(final String oldCaseName, final String oldCaseLocation) throws IOException {
        List<String> otherCasesGroup = this.getOtherCases(oldCaseName, oldCaseLocation);
        String newLine = aCase.getCaseName() + " - " + aCase.getCaseLocation();
        otherCasesGroup.add(newLine);
        
        FileUtil.writeToFile(otherCasesGroup, this.getCasesInformationFileLocation());
    }
        
    private CaseManager (final Case aCase) {
        this.aCase = aCase;
    }
}
