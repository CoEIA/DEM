package edu.coeia.cases;

import edu.coeia.util.FileUtil;
import edu.coeia.util.FilesPath ;

import java.io.File ;
import java.io.IOException ;


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
            }
        }
    }
    
    public static Case getCase(String line) throws IOException, ClassNotFoundException {
        String name = line.split("-")[0].trim();
        String path = line.split("-")[1].trim();

        Case aIndex = FileUtil.readObject(new File(path + "\\" + name + ".DAT"));
        return aIndex;
    }
    
    /*
     * Check if Case Exists
     * @return true if there is case with this name
     */
    public static boolean caseExists(String caseName)  {
        try {
            File casesInfo = new File(FilesPath.INDEXES_INFO);
            List<String> casesInfoContent = FileUtil.getFileContentInArrayList(casesInfo);

            for (String path : casesInfoContent) {
                Case aCase = CaseManager.getCase(path);

                if (aCase.getIndexName().equalsIgnoreCase(caseName)) {
                    return true;
                }
            }
        }
        catch (IOException e) {}
        catch (ClassNotFoundException e) { }

        return false;
    }
    
    /*
     * CaseOperation
     * Read And Write Case to File
     */
    public static class CaseOperation {
        public static void writeCase (Case caseObject) throws IOException {
            // create index folder
            File dir = new File( caseObject.getCaseLocation());
            dir.mkdir();

            // create THE_INDEX that hold index data used by lucene engine
            File dir2 = new File( caseObject.getCaseLocation() + "\\" + FilesPath.INDEX_PATH );
            dir2.mkdir();

            // create IMAGES that hold case images
            File imgDir = new File( caseObject.getCaseLocation() + "\\" + FilesPath.IMAGES_PATH);
            imgDir.mkdir();
            
            // create index information file & write the index on it
            String info = caseObject.getCaseLocation() + "\\" + caseObject.getIndexName() + ".DAT" ;
            File infoFile = new File(info);
            infoFile.createNewFile();
            FileUtil.writeObject(caseObject, infoFile);
            
            // create log file
            String log = caseObject.getCaseLocation() + "\\" + caseObject.getIndexName() + ".LOG" ;
            new File(log).createNewFile();
        }
    }
    
    
    // TODO: remove this, it break encapsulation!!!
    public List<String> getList () {
        return this.listOfOpeningCase ;
    }
    
    public void addCase (String caseName) {
        this.listOfOpeningCase.add(caseName);
    }
    
    public boolean isRunningCase (String caseName) {
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
        File logFile = new File(FilesPath.APPLICATION_LOG_PATH);
        
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
         
        // craete tmp files
        new File(FilesPath.HIS_TMP).createNewFile();
        new File(FilesPath.PASS_TMP).createNewFile();
        new File(FilesPath.CORRE_FILE).createNewFile();
    }
    
    private List<String> listOfOpeningCase ;
}
