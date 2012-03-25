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
 * Application Manager is singleton class that hold functionality related to cases
 * 
 * it also prevent the case to be opened again, by holding list of all opening case
 * and update this list when new case opening or closing existing one
 * 
 * 
 * @author wajdyessam
 */

public enum ApplicationManager {
    
    /*
     * Single manager for all cases running in application
     */
    Manager;
    
    private List<String> listOfOpeningCase ;
        
    private ApplicationManager() {
        this.listOfOpeningCase = new ArrayList<String>();  
        
        if ( !isApplicationFolderExists() || isApplicationHaveMissingFiles()) {
            try {
                createApplicationFoldersStructure();
            } catch (IOException ex) {
                Logger.getLogger(ApplicationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public List<Case> getCases() throws FileNotFoundException, IOException, ClassNotFoundException{
        List<Case> cases = new ArrayList<Case>();
        
        File indexesInfo = new File(ApplicationConstants.APPLICATION_CASES_FILE);
        List<String> casesPath  = FileUtil.getFileContentInList(indexesInfo);

        for(String path: casesPath) {
            try {
                Case aCase = this.getCase(path);
                cases.add(aCase);
            }
            catch(Exception e) {
                // this case cannot be reading becuase it was removing
                // the the pointer still exist to it
            }
        }
        
        return cases;
    }

    /*
     * Get index path from index name 
     * @return IndexInformation 
     */
    public Case getCaseFromCaseName (String indexName) throws FileNotFoundException, IOException, ClassNotFoundException {
        File indexesInfo = new File(ApplicationConstants.APPLICATION_CASES_FILE);
        List<String> indexesInfoContent  = FileUtil.getFileContentInList(indexesInfo);

        for(String path: indexesInfoContent) {
            try {
                Case index = this.getCase(path);

                if ( index.getCaseName().equals(indexName))
                    return index ;
            }
            catch(Exception e) {
                // this case cannot be reading becuase it was removing
                // the the pointer still exist to it
            }
            
        }

        return null ;
    }
    
    public Case getCase(String line) throws IOException, ClassNotFoundException {
        String name = line.split("-")[0].trim();
        String path = line.substring(line.indexOf("-") + 1).trim();

        Case aIndex = FileUtil.readObject(new File(path + "\\" + name + ".DAT"));
        return aIndex;
    }
    
    /*
     * Check if Case Exists
     * @return true if there is case with this name
     */
    public boolean caseExists(String caseName) throws FileNotFoundException, IOException, 
            ClassNotFoundException  {
        
        File casesInfo = new File(ApplicationConstants.APPLICATION_CASES_FILE);
        List<String> casesInfoContent = FileUtil.getFileContentInList(casesInfo);

        for (String path : casesInfoContent) {
            try {
              Case aCase = this.getCase(path);

                if (aCase.getCaseName().equalsIgnoreCase(caseName)) {
                    return true;
                }
            }
            catch(Exception e) {
                // this case cannot be reading becuase it was removing
                // the the pointer still exist to it
            }
        }

        return false;
    }
    
    public void removeCaseFromOpeningCase(final String caseName) {
        this.listOfOpeningCase.remove(caseName);
    }
    
    public void addCase (String caseName) {
        this.listOfOpeningCase.add(caseName);
    }
    
    public boolean isRunningCase (String caseName) {
        return this.listOfOpeningCase.contains(caseName);
    }

    public String getCasesPath() {
        return ApplicationConstants.APPLICATION_CASES_PATH;
    }
    
    
    /**
     * Check if their is missing files in the case
     * @return true if their is missing files, false when all essential files exists
     */
    private boolean isApplicationHaveMissingFiles() {
        boolean status = true;
        
        File root = new File(ApplicationConstants.APPLICATION_PATH);
        File cases = new File(ApplicationConstants.APPLICATION_CASES_PATH);
        File indexesInfo = new File(ApplicationConstants.APPLICATION_CASES_FILE);
        File hashLibraryFile = new File(ApplicationConstants.APPLICATION_HASH_LIBRARY_PATH);
        
        if (root.exists())
            status = false;
        
        if (cases.exists())
            status = false;

        if (indexesInfo.exists())
            status = false;
         
        if (hashLibraryFile.exists())
            status = false;
         
         return status;
    }
    
    /**
     * Check if DEM Main Folder that contain anythings related to cases Exists
     * @return 
     */
    private boolean isApplicationFolderExists() {
        File root = new File(ApplicationConstants.APPLICATION_PATH);
        return ( root.exists() );
    }
    
    /**
     * Create All the folder required to store cases
     * @throws IOException 
     */
    private void createApplicationFoldersStructure () throws IOException{
        File root = new File(ApplicationConstants.APPLICATION_PATH);
        File cases = new File(ApplicationConstants.APPLICATION_CASES_PATH);
        File indexesInfo = new File(ApplicationConstants.APPLICATION_CASES_FILE);
        File tmpFile = new File(ApplicationConstants.APPLICATION_TMP_PATH);
        File logFile = new File(ApplicationConstants.APPLICATION_LOG_PATH);
        File hashLibraryFile = new File(ApplicationConstants.APPLICATION_HASH_LIBRARY_PATH);
        
        if  ( ! root.exists() )
            root.mkdir();   // make offline folder in applicationData
        
        if  ( ! cases.exists() )
            cases.mkdir();  // make offlinemining\cases

        if  ( ! indexesInfo.exists() )
            indexesInfo.createNewFile();  // make offlinemining\indexes.txt

        if  ( ! tmpFile.exists() )
            tmpFile.mkdir();

        if ( !logFile.exists() ) 
            logFile.mkdir();
        
        if ( !hashLibraryFile.exists() )
            hashLibraryFile.mkdir();
         
        // create tmp files
        new File(ApplicationConstants.HIS_TMP).createNewFile();
        new File(ApplicationConstants.PASS_TMP).createNewFile();
        new File(ApplicationConstants.CORRE_FILE).createNewFile();
    }
}
