/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

import edu.coeia.util.FileUtil;
import edu.coeia.constants.ApplicationConstants;
import edu.coeia.constants.AuditingMessages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Case Manger Faced will create, update and remove cases and their histories 
 * from application
 * 
 * also hide the complexity of path mapping conversion
 * between full and relative path
 * 
 * @author wajdyessam
 */

public final class CaseFacade {
    private final Case aCase ;
    private final CaseHistoryHandler caseHistoryHandler;
    private final CasePathMappingHandler casePathHandler; 
    private final CaseAuditing caseAuditing;
    
    public static CaseFacade newInstance (final Case aCase) throws IOException {
        return new CaseFacade(aCase);
    }
    
    /**
     * create the folder structure for the current case object
     * options and return true if their is no problems
     */
    public boolean createCase() throws IOException {
        this.createCaseFoldersStructure();
        this.saveCaseInformation();
        this.updateCasesInformationFile();
        this.addCaseMappingInformation();
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
        this.caseAuditing.close();
        
        boolean status = FileUtil.removeDirectory(this.getCaseFolderLocation());
        
        List<String> otherCasesGroup = this.getOtherCases(this.aCase.getCaseName(), this.getCaseFolderLocation());
        FileUtil.writeToFile(otherCasesGroup, getCasesInformationFileLocation());

        this.caseHistoryHandler.removeHistory(aCase.getCaseName());
        return status;
    }
    
    // Case History Simplified Methods
    public CaseHistory getCaseHistory() {
        return this.caseHistoryHandler.getHistory(this.aCase.getCaseName());
    }
    
    public void setCaseHistory(final CaseHistory history) {
        this.caseHistoryHandler.setHistory(history);
    }
    
    public void importHistory(final String fileName) throws Exception {
        this.caseHistoryHandler.importHistory(fileName);
    }
    
    public void exportHistory(final String caseName, final String filePath) throws Exception {
        this.caseHistoryHandler.exportHistory(caseName, filePath);
    }
    
    // Case File path mapping simplified methods
    public void updateMappingFile() throws IOException { 
        this.casePathHandler.reloadFileMapping();
    }
    
    public String getFullPath(final String relativePath) {
        return this.casePathHandler.getFullPath(relativePath);
    }
    
    public String getRelativePath(final String fullPath) {
        return this.casePathHandler.getRelativePath(fullPath);
    }
    
    public boolean isCaseHaveChangedSource() throws IOException {
        return !this.casePathHandler.getChangedEntries().isEmpty();
    }
    
    public void updateMapping(final String oldFullPath, final String newFullPath) throws IOException {
        String oldRelativePath = getRelativeMarkForPath(oldFullPath);
        CasePathMappingHandler.PathMapping entry = new CasePathMappingHandler.PathMapping(oldRelativePath, newFullPath);
        this.casePathHandler.updateFullPath(entry);
    }
    
    public List<String> getChangedEntries() throws IOException {
        List<CasePathMappingHandler.PathMapping> mapping = this.casePathHandler.getChangedEntries();
        List<String> fullPaths = new ArrayList<String>();
        
        for(CasePathMappingHandler.PathMapping pathMapping: mapping) {
            fullPaths.add(pathMapping.absolutePath);
        }
        
        return fullPaths;
    }
    
    private String getRelativeMarkForPath(final String fullPath) throws IOException{
        for(CasePathMappingHandler.PathMapping entry: this.casePathHandler.getChangedEntries()) {
             String data = entry.absolutePath;
             String relative = entry.relativePath;
             
             if ( fullPath.equals(data) ) 
                 return relative;
        }
        
        return "";
    }
        
    public String getCasesInformationFileLocation() {
        return ApplicationConstants.APPLICATION_CASES_FILE;
    }
    
    public String getCaseFolderLocation() { 
        return this.aCase.getCaseLocation();
    }
    
    public String getCaseIndexFolderLocation() { 
        return aCase.getCaseLocation() 
                + File.separator 
                + ApplicationConstants.CASE_INDEX_FOLDER;
    }
    
    public String getCaseImageFolderLocation() {
        return aCase.getCaseLocation() 
                +  File.separator 
                + ApplicationConstants.CASE_IMAGES_FOLDER;
    }
    
    public String getCaseInformationFileLocation() {
        return aCase.getCaseLocation() 
                + File.separator 
                + aCase.getCaseName() 
                + ApplicationConstants.CASE_SERIALIZED_INFORMATION_EXTENSION;
    }
    
    public String getCaseAuditingFolderLocation() {
        return aCase.getCaseLocation() 
            + File.separator 
            + ApplicationConstants.CASE_AUDITING_FOLDER;
    }
    
    public String getCaseAuditingFileLocation() {
        return this.getCaseAuditingFolderLocation()
                + File.separator
                + aCase.getCaseName() 
                + ApplicationConstants.CASE_AUDITING_EXTENSION;
    }
    
    public String getCaseArchiveOutputFolderLocation() {
        return aCase.getCaseLocation() 
                + File.separator 
                + ApplicationConstants.CASE_ARCHIVE_FOLDER;
    }
    
    public String getCaseOfflineEmailAttachmentLocation() {
        return aCase.getCaseLocation() 
                + File.separator 
                + ApplicationConstants.CASE_OFFLINE_EMAIL_ATTACHMENTS_FOLDER;
    }
    
    public String getCaseOnlineEmailAttachmentLocation() {
        return aCase.getCaseLocation()
                + File.separator
                + ApplicationConstants.CASE_ONLINE_EMAIL_ATTACHMENTS_FOLDER;
    }
    
    public String getCaseRawReportFolderLocation() {
        return aCase.getCaseLocation()
                + File.separator
                + ApplicationConstants.CASE_ROW_REPORT_FOLDER;
    }
    
    public String getCaseReportFolderLocation() {
        return aCase.getCaseLocation() 
                + File.separator 
                + ApplicationConstants.CASE_REPORTS_FOLDER;
    }
    
    public String getTagDatabaseLocation() {
        return this.aCase.getCaseLocation() 
                + File.separator 
                + ApplicationConstants.CASE_TAGS_FOLDER;
    }
    
    public String getCaseConfigurationFileLocation() {
        return this.aCase.getCaseLocation()
                + File.separator
                + ApplicationConstants.CASE_CONFIG_FILE;
    }
    
    public Case getCase() { return this.aCase; }
    
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
        FileUtil.createFolder(this.getCaseIndexFolderLocation());   // INDEX folder
        FileUtil.createFolder(this.getCaseImageFolderLocation());   // IMAGE folder
        FileUtil.createFolder(this.getCaseArchiveOutputFolderLocation());   // ARCHIVE folder
        FileUtil.createFolder(this.getCaseOfflineEmailAttachmentLocation()); // OFFLINE Email Attachments
        FileUtil.createFolder(this.getCaseAuditingFolderLocation());
        FileUtil.createFolder(this.getCaseRawReportFolderLocation());
        FileUtil.createFolder(this.getCaseReportFolderLocation());
        
        // create LOG and information (.DAT) file and Configuration File (mapping file)
        FileUtil.createFile(this.getCaseAuditingFileLocation());
        FileUtil.createFile(this.getCaseInformationFileLocation()); 
        FileUtil.createFile(this.getCaseConfigurationFileLocation());
        
        // initialize case auditing
        this.caseAuditing.init();
    }
    
    private void addCaseMappingInformation() throws IOException {
        for(String path: aCase.getEvidenceSourceLocation()) {
            this.casePathHandler.add(new File(path));
        }
        
        this.casePathHandler.saveConfiguration();
    }
    
    private void replaceCaseLocation(final String oldCaseName, final String oldCaseLocation) throws IOException {
        List<String> otherCasesGroup = this.getOtherCases(oldCaseName, oldCaseLocation);
        String newLine = aCase.getCaseName() + " - " + aCase.getCaseLocation();
        otherCasesGroup.add(newLine);
        
        FileUtil.writeToFile(otherCasesGroup, this.getCasesInformationFileLocation());
    }
        
    public void audit(final AuditingMessages message) {
        this.caseAuditing.auditing(message.toString());
    }
    
    private CaseFacade (final Case aCase) throws IOException {
        this.aCase = aCase;
        this.caseHistoryHandler = new CaseHistoryHandler();
        this.casePathHandler = CasePathMappingHandler.newInstance(this.getCaseConfigurationFileLocation());
        this.caseAuditing = new CaseAuditing(this.aCase, this.getCaseAuditingFileLocation());
        
        if ( FileUtil.isFileFound(this.getCaseConfigurationFileLocation()))
            this.updateMappingFile();
        
        if ( FileUtil.isFileFound(this.getCaseAuditingFileLocation()))
            this.caseAuditing.init();
    }
}
