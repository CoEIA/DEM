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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Case Manager is singleton class that hold functionality related to cases
 * 
 * it also prevent the case to be opened again, by holding list of all opening case
 * and update this list when new case opening or closing existing one
 * 
 * 
 * @author wajdyessam
 */

public enum CaseManager {
    
    /*
     * Single manager for all cases running in application
     */
    Manager;
    
    private List<String> listOfOpeningCase ;
        
    private CaseManager() {
        this.listOfOpeningCase = new ArrayList<String>();  
        
        if ( !isApplicationFolderExists() || isApplicationHaveMissingFiles()) {
            try {
                createApplicationFoldersStructure();
            } catch (IOException ex) {
                Logger.getLogger(CaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
        
    public List<Case> getCases() throws FileNotFoundException, IOException, ClassNotFoundException{
        List<Case> cases = new ArrayList<Case>();
        
        File indexesInfo = new File(FilesPath.INDEXES_INFO);
        List<String> casesPath  = FileUtil.getFileContentInArrayList(indexesInfo);

        for(String path: casesPath) {
            Case aCase = this.getCase(path);
            cases.add(aCase);
        }
        
        return cases;
    }
    
    public void updateCase(final Case aCase) throws IOException {
       // create index information file & write the index on it
        String info = aCase.getCaseLocation() + "\\" + aCase.getCaseName() + ".DAT" ;
        File infoFile = new File(info);
        infoFile.createNewFile();
        FileUtil.writeObject(aCase, infoFile);
        
        this.updateCaseToInfoFile(aCase);
    }
    
    public void updateCaseToInfoFile(final Case aCase) throws IOException {
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
    public void writeCaseToInfoFile (Case index) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(FilesPath.INDEXES_INFO), true));
        writer.println(index.getCaseName() + " - " + index.getCaseLocation());
        writer.close();
    }
    
    public Case getCase(String line) throws IOException, ClassNotFoundException {
        String name = line.split("-")[0].trim();
        String path = line.substring(line.indexOf("-") + 1).trim();

        Case aIndex = FileUtil.readObject(new File(path + "\\" + name + ".DAT"));
        return aIndex;
    }
    
    /*
     * Get index path from index name 
     * @return IndexInformation 
     */
    public Case getCaseFromCaseName (String indexName) throws FileNotFoundException, IOException, ClassNotFoundException {
        File indexesInfo = new File(FilesPath.INDEXES_INFO);
        List<String> indexesInfoContent  = FileUtil.getFileContentInArrayList(indexesInfo);

        for(String path: indexesInfoContent) {
            Case index = this.getCase(path);

            if ( index.getCaseName().equals(indexName))
                return index ;
        }

        return null ;
    }
    
    /*
     * Check if Case Exists
     * @return true if there is case with this name
     */
    public boolean caseExists(String caseName) throws FileNotFoundException, IOException, 
            ClassNotFoundException  {
        
        File casesInfo = new File(FilesPath.INDEXES_INFO);
        List<String> casesInfoContent = FileUtil.getFileContentInArrayList(casesInfo);

        for (String path : casesInfoContent) {
            Case aCase = this.getCase(path);

            if (aCase.getCaseName().equalsIgnoreCase(caseName)) {
                return true;
            }
        }

        return false;
    }
    
    public boolean removeCase(final Case aCase) throws Exception {
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
    
    public void createCaseFoldersStructure (Case caseObject) throws IOException {
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

        // create case configuration file and write path mapping on it
        CasePathHandler handler = CasePathHandler.newInstance(caseObject.getCaseLocation());
        for(String path: caseObject.getEvidenceSourceLocation()) {
            handler.add(new File(path));
        }
        handler.saveConfiguration();

        // create tmp files for archives extractions
        File tmpFile = new File(caseObject.getCaseLocation() + "\\" + FilesPath.CASE_TMP);
        tmpFile.mkdir();

        // create folder for offline email attachment
        File outlookAttachment = new File(caseObject.getCaseLocation() + File.separator + FilesPath.OFFLINE_EMAIL_ATTACHMENTS);
        outlookAttachment.mkdir();
    }
    
    public boolean isCaseHaveChangedSource(final Case aCase) throws IOException {
        return !CasePathHandler.newInstance(aCase.getCaseLocation()).getChangedEntries().isEmpty();
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

    public String getCasesPath() {
        return FilesPath.CASES_PATH;
    }
    
    
    /**
     * Check if their is missing files in the case
     * @return true if their is missing files, false when all essential files exists
     */
    private boolean isApplicationHaveMissingFiles() {
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
     * Check if DEM Main Folder that contain anythings related to cases Exists
     * @return 
     */
    private boolean isApplicationFolderExists() {
        File root = new File(FilesPath.APPLICATION_PATH);
        return ( root.exists() );
    }
    
    /**
     * Create All the folder required to store cases
     * @throws IOException 
     */
    private void createApplicationFoldersStructure () throws IOException{
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
