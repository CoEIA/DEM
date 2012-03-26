package edu.coeia.managers;

import edu.coeia.cases.Case;
import edu.coeia.util.FileUtil;
import edu.coeia.constants.ApplicationConstants ;

import java.io.File ;
import java.io.FileNotFoundException;
import java.io.IOException ;

import java.util.List;
import java.util.ArrayList ;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application Manager is singleton class that hold functionality related to 
 * application folders structure by creating the empty structure when running
 * the application first time, and recreating folders if its removed by mistake
 * by the end user
 * 
 * it also prevent the case to be opened again, by holding list of all opening case
 * and update this list when new case opening or closing existing one
 * 
 * the user of this class can ask to get all cases in this system
 * or get any case by specifying its name
 * 
 * @author wajdyessam
 */

public enum ApplicationManager {
    
    /*
     * this is the single object can be made from this class only 
     * becuase it will create the application folders directory
     * if not found, so we will not let user to make this.
     */
    Manager;
    
    private List<String> listOfOpeningCase ;
        
    /**
     * create application folders if its not found in the system
     * this happen when the application running first time
     * on this system
     */
    private ApplicationManager() {
        this.listOfOpeningCase = new ArrayList<String>();  
        
        if ( !isApplicationFoldersExists() ) {
            try {
                createApplicationFoldersStructure();
            } catch (IOException ex) {
                Logger.getLogger(ApplicationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * get list of all good cases in the system that founds it name and path
     * in the cases database file
     * 
     * if the case name and path exists in case database file but the all the 
     * path is not found in the system, then the application will ignore this 
     * case and will not bring it
     * 
     * @return
     * @throws FileNotFoundException
     */
    public List<Case> getCases() throws FileNotFoundException{
        List<Case> cases = new ArrayList<Case>();
        
        File casesDatabaseFile = new File(ApplicationConstants.APPLICATION_CASES_FILE);
        List<String> casesPath  = FileUtil.getFileContentInList(casesDatabaseFile);

        for(String path: casesPath) {
            try {
                cases.add(this.getCase(path));
            }
            catch(Exception e) {
                // this case cannot be reading becuase the folder 
                // or the .DAT file is not found
                // but the pointer still exist to it
                // so we ignore this case
            }
        }
        
        return cases;
    }

    /*
     * Get index path from index name 
     * @return IndexInformation 
     */
    public Case getCaseFromCaseName (final String caseName) throws FileNotFoundException {
        List<Case> cases = this.getCases();
        
        for(Case caseObject: cases) {
            if (caseObject.getCaseName().equals(caseName)) {
                return caseObject;
            }
        }
        
        return null ;
    }
    
    /*
     * Check if the caseName is exist in the system and
     * the its folder and data also exists (.DAT file)
     * 
     * the implementation is by reading all the cases
     * then check the name of the case with ignoring any capital or
     * small letters
     * 
     * @return true if there is case with this name
     */
    public boolean caseExists(final String caseName) throws FileNotFoundException{
        List<Case> cases = this.getCases();
        
        for(Case caseObject: cases) {
            if (caseObject.getCaseName().equalsIgnoreCase(caseName)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * build the case information from the serialized information 
     * that stored in .DAT file when the case is created
     * 
     * any case have name and path (contain .DAT file) can be build 
     * from this method, even if the case (name and path) is not exists
     * in the case database information file. this is used when importing
     * new case that existed in other location , so the first step 
     * is to build case object for these external case then do import 
     * process
     * 
     * @param line is the line that found in case database file
     *        it have the format: CASE_NAME - CASE_PATH
     *        and followed by new line separator.
     * 
     *        CASE_NAME cannot contain - mark in the name
     *        and this controlled by the case created classes
     *        that will do this checking.
     * 
     * @return the case if its found
     * 
     * @throws IOException
     * @throws ClassNotFoundException 
     *         this exception occur when reading from not existed DAT file
     *         or if their are some error during IO operation
     */
    public Case getCase(final String line) throws IOException, ClassNotFoundException {
        String caseName = line.split("-")[0].trim();
        String casePath = line.substring(line.indexOf("-") + 1).trim();

        File caseInfoDatFile = new File(casePath + File.separator + caseName + ".DAT");
        Case caseObject = FileUtil.readObject(caseInfoDatFile);
        
        return caseObject;
    }
    
    /**
     * the user can ask this class to remove the case name
     * from the current working cases when the user has finished 
     * the work with this case
     * 
     * @param caseName the name of the case to be closed
     */
    public void removeCaseFromOpeningCase(final String caseName) {
        this.listOfOpeningCase.remove(caseName);
    }
    
    /**
     * the user can add this caseName to the currently 
     * working cases so that the application manager
     * can prevent the re-opening the case if its already
     * running now 
     * 
     * @param caseName the name of case to be added to opening list
     */
    public void addCase (String caseName) {
        this.listOfOpeningCase.add(caseName);
    }
    
    /**
     * check if the caseName is currently working or not
     * @param caseName
     * 
     * @return return ture if the cases is in opening list
     */
    public boolean isRunningCase (String caseName) {
        return this.listOfOpeningCase.contains(caseName);
    }

    /**
     * get cases folder than contain all cases
     * 
     * @return  path of cases folder than contain all cases
     */
    public String getCasesPath() {
        return ApplicationConstants.APPLICATION_CASES_PATH;
    }
       
    /**
     * Check that application folder structure is valid
     * and no missing files or folder found
     * 
     * @return true if their is missing files, false when all essential files exists
     */
    private boolean isApplicationFoldersExists() {
        File rootFolder = new File(ApplicationConstants.APPLICATION_PATH);
        File casesFolder = new File(ApplicationConstants.APPLICATION_CASES_PATH);
        File casesDatabaseFile = new File(ApplicationConstants.APPLICATION_CASES_FILE);
        File hashLibraryFolder = new File(ApplicationConstants.APPLICATION_HASH_LIBRARY_PATH);
        File tmpFolder = new File(ApplicationConstants.APPLICATION_TMP_PATH);
        File logFolder = new File(ApplicationConstants.APPLICATION_LOG_PATH);
        
        if (!rootFolder.exists()) return false;
        if (!casesFolder.exists()) return false;
        if (!casesDatabaseFile.exists()) return false;
        if (!hashLibraryFolder.exists()) return false;
        if (!tmpFolder.exists()) return false;
        if (!logFolder.exists()) return false;
        
        return true;
    }  
    
    /**
     * Create Application folder structures 
     * the folder will not created if its exists, 
     * so this methods will not override already existed folders
     * 
     * @throws IOException
     *         if an I/O error occurred
     */
    private void createApplicationFoldersStructure() throws IOException{
        File rootFolder = new File(ApplicationConstants.APPLICATION_PATH);
        File casesFolder = new File(ApplicationConstants.APPLICATION_CASES_PATH);
        File casesDatabaseFile = new File(ApplicationConstants.APPLICATION_CASES_FILE);
        File tmpFolder = new File(ApplicationConstants.APPLICATION_TMP_PATH);
        File logFolder = new File(ApplicationConstants.APPLICATION_LOG_PATH);
        File hashLibraryFolder = new File(ApplicationConstants.APPLICATION_HASH_LIBRARY_PATH);
        
        if (!rootFolder.exists())
            rootFolder.mkdir();                 // CoEIA_Forensics
        
        if (!casesFolder.exists())
            casesFolder.mkdir();                // CoEIA_Forensics\CASES

        if (!casesDatabaseFile.exists())
            casesDatabaseFile.createNewFile();  // CoEIA_Forensics\INDEXES.txt

        if (!tmpFolder.exists())
            tmpFolder.mkdir();                  // CoEIA_Forensics\TMP

        if (!logFolder.exists()) 
            logFolder.mkdir();                  // CoEIA_Forensics\LOG
        
        if (!hashLibraryFolder.exists())
            hashLibraryFolder.mkdir();          // CoEIA_Forensics\HASH_LIBRARY
         
        // create tmp files inside TMP folder
        new File(ApplicationConstants.HIS_TMP).createNewFile();
        new File(ApplicationConstants.PASS_TMP).createNewFile();
        new File(ApplicationConstants.CORRE_FILE).createNewFile();
    }
}
