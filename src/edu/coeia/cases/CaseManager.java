package edu.coeia.cases;

import edu.coeia.util.FileUtil;
import edu.coeia.util.FilesPath ;

import java.io.File ;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException ;

import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList ;

/**
 * Case Manager is singleton class that hold functionality related to other cases management
 * 
 * it prevent the case to be opened again, by holding list of all opening case
 * and update this list as the case opening/closing
 * 
 * 
 * @author wajdyessam
 */
enum CaseManager {
    
    /*
     * Single manager for all cases running in application
     */
    Manager;
    
    private List<String> listOfOpeningCase ;
        
    private CaseManager() {
        this.listOfOpeningCase = new ArrayList<String>();  
        
        if ( !isCaseFolderExist() || isCaseHaveMissingFiles()) {
            try {
                createApplicationFolders();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
        
    // add entry to indexes info file
    public static void writeCaseToInfoFile (Case index) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(FilesPath.INDEXES_INFO), true));
        writer.println(index.getCaseName() + " - " + index.getCaseLocation());
        writer.close();
    }
    
    public static Case getCase(String line) throws IOException, ClassNotFoundException {
        String name = line.split("-")[0].trim();
        String path = line.split("-")[1].trim();

        Case aIndex = FileUtil.readObject(new File(path + "\\" + name + ".DAT"));
        return aIndex;
    }
    
    /*
     * Get index path from index name 
     * @return IndexInformation 
     */
    public static Case getCaseFromCaseName (String indexName) throws FileNotFoundException, IOException, ClassNotFoundException {
        File indexesInfo = new File(FilesPath.INDEXES_INFO);
        List<String> indexesInfoContent  = FileUtil.getFileContentInArrayList(indexesInfo);

        for(String path: indexesInfoContent) {
            Case index = CaseManager.getCase(path);

            if ( index.getCaseName().equals(indexName))
                return index ;
        }

        return null ;
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

                if (aCase.getCaseName().equalsIgnoreCase(caseName)) {
                    return true;
                }
            }
        }
        catch (IOException e) {}
        catch (ClassNotFoundException e) { }

        return false;
    }
    
    public static boolean removeCase(final Case aCase) throws Exception {
        boolean status = false; 
        
        File file = new File( aCase.getCaseLocation() );

        if ( FileUtil.removeDirectory(file) ) {
            List<String> indexPtr = FileUtil.getFileContentInArrayList(new File(FilesPath.INDEXES_INFO) );
            List<String> newIndexPtr = new ArrayList<String>();

            for (String line: indexPtr) {
                String name = line.split("-")[0].trim();
                String path = line.split("-")[1].trim();

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
    
    /*
     * CaseOperation
     * Read And Write Case to File
     */
    public static class CaseOperation {
        
        public static void writeCase (Case caseObject) throws IOException {
            // create main folder
            File dir = new File( caseObject.getCaseLocation());
            dir.mkdir();

            // create THE_INDEX that hold index data used by lucene engine
            File dir2 = new File( caseObject.getCaseLocation() + "\\" + FilesPath.INDEX_PATH );
            dir2.mkdir();

            // create IMAGES that hold case images
            File imgDir = new File( caseObject.getCaseLocation() + "\\" + FilesPath.IMAGES_PATH);
            imgDir.mkdir();
            
            // create index information file & write the index on it
            String info = caseObject.getCaseLocation() + "\\" + caseObject.getCaseName() + ".DAT" ;
            File infoFile = new File(info);
            infoFile.createNewFile();
            FileUtil.writeObject(caseObject, infoFile);
            
            // create log file
            String log = caseObject.getCaseLocation() + "\\" + caseObject.getCaseName() + ".LOG" ;
            new File(log).createNewFile();
            
            // create tmp files for archives extractions
            File tmpFile = new File(caseObject.getCaseLocation() + "\\" + FilesPath.CASE_TMP);
            tmpFile.mkdir();
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
     * @return true if their is missing files, false when all essential files exists
     */
    private boolean isCaseHaveMissingFiles() {
        boolean status = true;
        
        File root = new File(FilesPath.APPLICATION_PATH);
        File cases = new File(FilesPath.CASES_PATH);
        File indexesInfo = new File(FilesPath.INDEXES_INFO);
        File hashLibraryFile = new File(FilesPath.HASH_LIBRARY_PATH);
        
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
     * Check if Case Main Folder Exists
     * @return 
     */
    private boolean isCaseFolderExist() {
        File root = new File(FilesPath.APPLICATION_PATH);
        return ( root.exists() );
    }

    public String getCasesPath() {
        return FilesPath.CASES_PATH;
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
        File hashLibraryFile = new File(FilesPath.HASH_LIBRARY_PATH);
        
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
        new File(FilesPath.HIS_TMP).createNewFile();
        new File(FilesPath.PASS_TMP).createNewFile();
        new File(FilesPath.CORRE_FILE).createNewFile();
    }
}
