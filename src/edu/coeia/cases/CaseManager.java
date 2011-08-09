package edu.coeia.cases;

import edu.coeia.utility.FilesPath ;

import java.io.IOException ;
import java.io.File ;

import java.util.List;
import java.util.ArrayList ;

/**
 *
 * @author wajdyessam
 */
public enum CaseManager {
    
    Manager;
    
    private CaseManager() {
        this.listOfOpeningCase = new ArrayList<String>();  
        
        if ( ! isCaseFolderExist() || isCaseHaveMissingFiles()) {
            try {
                createApplicationFolders();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    // TODO: remove this, it break encapsulation!!!
    public List<String> getList () {
        return this.listOfOpeningCase ;
    }
    
    public void addCase (String caseName) {
        this.listOfOpeningCase.add(caseName);
    }
    
    public boolean isContain (String caseName) {
        return this.listOfOpeningCase.contains(caseName);
    }
    
    /**
     * Check if their is missing files in the case
     * @return true if their is missing files, false otherwise
     */
    private boolean isCaseHaveMissingFiles() {
        File root = new File(FilesPath.APPLICATION_PATH);
        File cases = new File(FilesPath.CASES_PATH);
        File indexesInfo = new File(FilesPath.INDEXES_INFO);
        
        if  ( ! root.exists() )
            return true;
        
         if  ( ! cases.exists() )
              return true;

         if  ( ! indexesInfo.exists() )
              return true;
         
         return false;
    }
    
    /**
     * Check if Case Main Folder Exists
     * @return 
     */
    private boolean isCaseFolderExist() {
        File root = new File(FilesPath.APPLICATION_PATH);
        return ( root.exists() );
    }

    /**
     * Create All the folder required to store cases
     * @throws IOException 
     */
    private void createApplicationFolders () throws IOException{
        File root = new File(FilesPath.APPLICATION_PATH);
        File cases = new File(FilesPath.CASES_PATH);
        File indexesInfo = new File(FilesPath.INDEXES_INFO);
        File tmpFile = new File(FilesPath.TMP_PATH);
        
        if  ( ! root.exists() )
            root.mkdir();   // make offline folder in applicationData
        
         if  ( ! cases.exists() )
             cases.mkdir();  // make offlinemining\cases

         if  ( ! indexesInfo.exists() )
             indexesInfo.createNewFile();  // make offlinemining\indexes.txt

         if  ( ! tmpFile.exists() )
             tmpFile.mkdir();

        // craete tmp files
        new File(FilesPath.HIS_TMP).createNewFile();
        new File(FilesPath.PASS_TMP).createNewFile();
        new File(FilesPath.CORRE_FILE).createNewFile();
    }
    
    private List<String> listOfOpeningCase ;
}
