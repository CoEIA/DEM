/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.cases.Case;
import edu.coeia.cases.CaseHistoryHandler;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.indexing.CrawlerIndexerThread.ProgressIndexData;
import edu.coeia.util.DateUtil;
import edu.coeia.util.FileUtil;
import edu.coeia.util.SizeUtil;

import javax.swing.SwingWorker ;
import javax.swing.table.DefaultTableModel ;
import javax.swing.JOptionPane;

import java.io.File ;
import java.io.IOException ;

import java.util.Date ;
import java.util.List ;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pff.PSTException ;

import org.apache.tika.exception.TikaException;

final class CrawlerIndexerThread extends SwingWorker<String,ProgressIndexData> {

    private long time = 0 ;
    private int progressCount = 0, indexCount=0;
    
    private long itemsCount, caseSize ;
    
    private int totalNumberOfError = 0;
    private boolean indexStatus = false;
    
    private LuceneIndex luceneIndex ;
    private final Case aCase ;
    private final IndexingDialog parentDialog ;
    
    private static final Logger logger = Logger.getLogger(edu.coeia.util.FilesPath.LOG_NAMESPACE);

    public CrawlerIndexerThread (IndexingDialog parentDialog) {
        this.aCase = parentDialog.getCase();
        this.parentDialog = parentDialog;
     
        try {
            luceneIndex = LuceneIndex.getInstance(this.aCase, true);
            
            logger.log(Level.INFO, "this is first line in indexing");
        } catch (IOException ex) {
           logger.log(Level.SEVERE, "Uncaught exception", ex);
        }
        
        this.parentDialog.setNumberOfFilesError("0");
        this.parentDialog.setProgressIndetermined(true);
    }
    
    public String doInBackground() {
        long start = new Date().getTime();
         
        for ( String dirName : aCase.getEvidenceSourceLocation() ) {
            logger.log(Level.INFO, "Start Index File: {0}", dirName);
            File file = new File(dirName);
            boolean ignoreEmail = true;
            dirTraversal(file, ignoreEmail);
            logger.log(Level.INFO, "End of indexing: {0}", dirName);
        }       
        
        long end = new Date().getTime();
        time = end-start ;

        indexStatus = true ;      
        return "" + time ;
    }
    
    private void dirTraversal(File dir, boolean ignoreEmail) {
        if ( dir.isDirectory() ) {
            File[] files = dir.listFiles();

            if ( files != null ) {
                for (int i=0 ; i<files.length ; i++) {
                    progressCount++;
                    dirTraversal(files[i], ignoreEmail);
                }
            }
            
            /**
             * is end with Program Files\Yahoo!\Messenger\Profiles
             */
            if ( isValidYahooPath(dir.getAbsolutePath()) ) {
                System.out.println("found yahoo at: " + dir.getAbsolutePath());
                LuceneIndex.indexYahooDir(dir);
            }
            
            /**
             * is ending userXX\My Documents\My Received Files
             */
            if (isValidMSNPath(dir.getAbsolutePath())) {
                System.out.println("found msn at: " + dir.getAbsolutePath());
                LuceneIndex.indexHotmailDir(dir);
            }
            
            /**
             * is path ending with Application Data\SKYPE
             */
            if ( isValidSkypePath(dir.getAbsolutePath())) {
                System.out.println("found skype at: " + dir.getAbsolutePath());
            }
        }
        else {
            long size = dir.length();
            
            // if file size more than 3 MB then show size message to indicate that indexing will take some time
            String msg = size > 3145728 ? "This file will take some minutes to index, please wait..." : " " ;

            publish(new ProgressIndexData( progressCount,indexCount, dir.getAbsolutePath(), "" , 1 , msg));
            logger.log(Level.INFO, "Index File: {0} , size: {1}", new Object[]{dir.getAbsolutePath(), SizeUtil.getSize(dir.getAbsolutePath())});
            try {
                if ( isEmailFile(dir.getAbsolutePath()) &&  ignoreEmail ) {
                    indexCount++;
                }
                else {
                    boolean status = LuceneIndex.indexFile(dir);

                    if ( ! status )
                        publish(new ProgressIndexData( progressCount,indexCount, dir.getAbsolutePath(), "Cannot Index This File", 0 , msg));
                    else
                        indexCount++;
                }
            }
            catch (IOException e) {
                publish(new ProgressIndexData( progressCount,indexCount, dir.getAbsolutePath(), e.getMessage() , 0 , msg));
                logger.log(Level.SEVERE, "Uncaught exception", e);
            }
            catch (PSTException e) {
                publish(new ProgressIndexData( progressCount,indexCount, dir.getAbsolutePath(), e.getMessage() , 0 , msg));
                logger.log(Level.SEVERE, "Uncaught exception", e);
            }
            catch (TikaException e) {
                publish(new ProgressIndexData( progressCount,indexCount, dir.getAbsolutePath(), e.getMessage() , 0 , msg));
                logger.log(Level.SEVERE, "Uncaught exception", e);
            }

            ++progressCount ;
        }
    }

    /**
     * Test if the path is valid Yahoo path
     * @param path to chat profile
     * @return true if path is correct and false if not
     */
    public static boolean isValidYahooPath(String path) {
        if ( path.endsWith("Program Files\\Yahoo!\\Messenger\\Profiles") )
            return true;
        
        return false;
    }
    
    /**
     * Test if the path is valid MSN path
     * @param path to chat profile
     * @return true if path is correct and false if not
     */
    public static boolean isValidMSNPath(String path) {
        if ( path.endsWith("My Documents\\My Received Files") )
            return true;
        
        return false;
    }
    
    /**
     * Test if the path is valid SKYPE path
     * @param path to chat profile
     * @return true if path is correct and false if not
     */
    public static boolean isValidSkypePath(String path) {
        if ( path.endsWith("Application Data\\Skype") )
            return true;
        
        return false;
    }
    
    private boolean isEmailFile (String path) {
        if ( path.endsWith("ost") || path.endsWith("OST") ||
                path.endsWith("pst") || path.endsWith("PST"))
            return true;
        
        else
            return false;
    }

    @Override
    protected void process(List<ProgressIndexData> chunks) {
        for (ProgressIndexData pd : chunks) {
            if ( pd.getType() == 0 ) {
                ((DefaultTableModel)this.parentDialog.getLoggingTable().getModel()).addRow(new Object[] { FileUtil.getExtension(pd.getPath())
                    , pd.getPath(), "cannot index this file (password protected or internal error in file format)"});

                totalNumberOfError++;
                this.parentDialog.setNumberOfFilesError(String.valueOf(this.totalNumberOfError));
                this.parentDialog.setprogressBar(pd.getProgressCount());
            }
            else {
                this.parentDialog.setCurrentFile(pd.getPath());
                this.parentDialog.setFileSize(SizeUtil.getSize(pd.getPath()));
                this.parentDialog.setNumberOfFiles(String.valueOf(pd.getIndexCount()));
                this.parentDialog.setFileExtension(FileUtil.getExtension(pd.getPath()));
                this.parentDialog.setBigSizeLabel(pd.getSizeMsg());
                this.parentDialog.setprogressBar(pd.getProgressCount());
                this.parentDialog.setprogressBar(pd.getIndexCount());
            }
        }

       int indexNum = chunks.size()-1 ;
       JTableUtil.scrollToVisible(this.parentDialog.getLoggingTable(),(chunks.get(indexNum)).getProgressCount(),0);
    }

    @Override
    public void done() {
        logger.log(Level.INFO, "Done Indexing Process");

        this.parentDialog.setProgressIndetermined(false);
        
        String indexingTime = "" + DateUtil.getTimeFromMilliseconds(time) ;
        String lastIndexDate = DateUtil.formatDateTime(new Date()) ;

        this.parentDialog.setTimeLabel(indexingTime);
        this.parentDialog.setLastIndexTime(lastIndexDate);
        this.parentDialog.setStartButtonStatus(true);
        this.parentDialog.setStopButtonStatus(false);
        
        // save log files
        try {
            if ( indexStatus ) {
                CaseHistoryHandler.CaseHistory history = CaseHistoryHandler.CaseHistory.newInstance(
                        this.aCase.getIndexName(), new Date().toString(), true, this.itemsCount, 
                        this.caseSize);
                
                CaseHistoryHandler.set(history);
            }
            closeIndex();
        }
        catch (IOException e){
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }

        // show message box after finish the indexing process if there is problem
        if ( ! indexStatus )
            JOptionPane.showMessageDialog(this.parentDialog, "Indexing Process Stopped","Indexing Process Is Not Completed",
                    JOptionPane.ERROR_MESSAGE);
        else 
            JOptionPane.showMessageDialog(this.parentDialog, "Indexing Process Completed Successfully","Indexing Process Is Completed",
                JOptionPane.INFORMATION_MESSAGE);

        clearFields();
        
        this.parentDialog.closeDialog();
    }

    public void clearFields() {
        this.parentDialog.setBigSizeLabel("");
        this.parentDialog.setFileExtension("");
        this.parentDialog.setCurrentFile("");
        this.parentDialog.setFileSize("");
        this.parentDialog.setprogressBar(0);

        //indexGUI.progressBar.setStringPainted(false);
    }
    
    public void closeIndex () throws IOException {
        luceneIndex.closeIndex();
    }
    
    final class ProgressIndexData {
        private final String path;
        private final int progressCount ;
        private final int indexCount ;
        private final String status ;
        private final int type ; // 0 for set data in table , 1 for change label
        private final String sizeMsg ;

        public ProgressIndexData (int progressCount, int indexCount, String p,
                String status, int type, String sm) {
            this.progressCount = progressCount;
            this.path = p ;
            this.indexCount = indexCount ;
            this.status = status ;
            this.type = type;
            this.sizeMsg = sm;
        }

        public String getPath ()        { return this.path   ; }
        public int getProgressCount ()  { return this.progressCount  ; }
        public String getStatus()       { return this.status ; }
        public int getType ()           { return this.type   ; }
        public int getIndexCount()      { return this.indexCount ;}
        public String getSizeMsg()      { return this.sizeMsg; }
    }
}
