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
import edu.coeia.cases.CaseManager;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.gutil.IndexGUIComponent ;
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

class ProgressIndexData {
    private String path;
    private int progressCount ;
    private int indexCount ;
    private String status ;
    private int type ; // 0 for set data in table , 1 for change label
    private String sizeMsg ;
    
    public ProgressIndexData (int progressCount, int indexCount, String p, String status, int type, String sm) {
        this.progressCount = progressCount;
        path = p ;
        this.indexCount = indexCount ;
        this.status = status ;
        this.type = type;
        this.sizeMsg = sm;
    }

    public String getPath ()        { return path   ; }
    public int getProgressCount ()  { return progressCount  ; }
    public String getStatus()       { return status ; }
    public int getType ()           { return type   ; }
    public int getIndexCount()      { return indexCount ;}
    public String getSizeMsg()      { return sizeMsg; }
}

public class CrawlerThread extends SwingWorker<String,ProgressIndexData> {

    private long time = 0 ;
    private int progressCount = 0, indexCount=0;
    private LuceneIndexer luceneIndexer ;
    private IndexGUIComponent indexGUI ;
    private Case caseObject ;
    //private int totalNumberOfFiles ;
    private int totalNumberOfError = 0;
    private boolean indexStatus = false;
    
    private IndexingDialog parentDialog ;
    
    private static final Logger logger = Logger.getLogger(edu.coeia.util.FilesPath.LOG_NAMESPACE);

    public CrawlerThread (File indexLocation, IndexGUIComponent indexGUI, Case index,
            List<String> imagesPath, IndexingDialog parentDialog) {
        
        this.indexGUI = indexGUI ;
        this.caseObject = index ;
        this.parentDialog = parentDialog;
     
        try {
            luceneIndexer = LuceneIndexer.getInstance(this.caseObject, true);
            
            logger.log(Level.INFO, "this is first line in indexing");
        } catch (IOException ex) {
           logger.log(Level.SEVERE, "Uncaught exception", ex);
        }

        //this.indexGUI.progressBar.setStringPainted(true);
        //this.indexGUI.progressBar.setMaximum(100 + index.getPstPath().size());
        this.indexGUI.numberOfErrorFilesLbl.setText("0");
        this.indexGUI.progressBar.setIndeterminate(true);
    }
    
    public String doInBackground() {
        long start = new Date().getTime();
         
        // index selected files
        logger.log(Level.INFO, "Start Index Files");
        for ( String dirName : caseObject.getDocumentInIndex() ) {
            File file = new File(dirName);
            System.out.println("File: " + file.getAbsolutePath());
            boolean ignoreEmail = true;
            dirTraversal(file, ignoreEmail);
        }

        logger.log(Level.INFO, "Start Index Email if contain");
        
        // index outlooks pst files
//        for (String path: caseObject.getPstPath() ) {
//            logger.log(Level.INFO, "Index Mail: " + path);
//            File file = new File(path);
//            boolean ignoreEmail = false;
//            dirTraversal(file, ignoreEmail);
//        }

        logger.log(Level.INFO, "End of indexing");
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
                LuceneIndexer.indexYahooDir(dir);
            }
            
            /**
             * is ending userXX\My Documents\My Received Files
             */
            if (isValidMSNPath(dir.getAbsolutePath())) {
                System.out.println("found msn at: " + dir.getAbsolutePath());
                LuceneIndexer.indexHotmailDir(dir);
            }
            
            /**
             * is path ending with Application Data\Skype
             */
            if ( isValidSkypePath(dir.getAbsolutePath())) {
                System.out.println("found skype at: " + dir.getAbsolutePath());
            }
        }
        else {
            long size = dir.length();
            
            // if file size more than 3 MB then show size message to indicate that indexing will take some time
            String msg = size > 3145728 ? "This file will take some minutes to index, please wait..." : " " ;

            //if ( Utilities.isExtentionAllowed(caseObject.getExtensionAllowed(), Utilities.getExtension(dir))) {
                publish(new ProgressIndexData( progressCount,indexCount, dir.getAbsolutePath(), "" , 1 , msg));
                logger.log(Level.INFO, "Index File: " + dir.getAbsolutePath() + " , size: " + SizeUtil.getSize(dir.getAbsolutePath()));
                try {
                    if ( isEmailFile(dir.getAbsolutePath()) &&  ignoreEmail ) {
                        indexCount++;
                    }
                    else {
                        boolean status = LuceneIndexer.indexFile(dir);

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
            //}
            //else {
            //    //System.out.println( dir.getName() +  " is not allowed");
            //}
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
                ((DefaultTableModel)indexGUI.table.getModel()).addRow(new Object[] { FileUtil.getExtension(pd.getPath())
                    , pd.getPath(), "cannot index this file (password protected or internal error in file format)"});

                totalNumberOfError++;
                indexGUI.numberOfErrorFilesLbl.setText("" + totalNumberOfError);
                
                indexGUI.progressBar.setValue( pd.getProgressCount() );
//                indexGUI.progressBar.setValue( (pd.getProgressCount()/totalNumberOfFiles)*100 );
            }
            else {
                indexGUI.currentFileLbl.setText(pd.getPath());
                //indexGUI.sizeOfFileLbl.setText(new File(pd.getPath()).length() + " Bytes");
                indexGUI.sizeOfFileLbl.setText(SizeUtil.getSize(pd.getPath()));
                indexGUI.numberOfFileLbl.setText("(" + pd.getIndexCount()  + ")");
                indexGUI.fileExtensionLbl.setText(FileUtil.getExtension(pd.getPath()));
                indexGUI.bigSizeMsgLbl.setText(pd.getSizeMsg());
                
                indexGUI.progressBar.setValue( pd.getProgressCount() );
//                indexGUI.progressBar.setValue( (pd.getProgressCount()/totalNumberOfFiles)*100 );
                indexGUI.progressBar.setValue( pd.getIndexCount() );
            }
        }

       int indexNum = chunks.size()-1 ;
       JTableUtil.scrollToVisible(indexGUI.table,(chunks.get(indexNum)).getProgressCount(),0);
    }

    @Override
    public void done() {
        logger.log(Level.INFO, "Done Indexing Process");

        this.indexGUI.progressBar.setIndeterminate(false);
        
        String indexingTime = "" + DateUtil.getTimeFromMilliseconds(time) ;
        String lastIndexDate = DateUtil.formatDateTime(new Date()) ;

        indexGUI.timeLbl.setText(indexingTime);
        indexGUI.dateLbl.setText(lastIndexDate);

        indexGUI.startIndexingButton.setEnabled(true);
        indexGUI.stopIndexingButton.setEnabled(false);
        
        // save log files
        try {
            if ( indexStatus ) {
                caseObject.setIndexStatus(true);
                caseObject.setLastIndexDate(lastIndexDate);
                caseObject.setIndexingTime(indexingTime);
                CaseManager.CaseOperation.writeCase(caseObject);
                //System.out.println("write status information after indexing");
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
        indexGUI.bigSizeMsgLbl.setText("");
        indexGUI.fileExtensionLbl.setText("");
        indexGUI.currentFileLbl.setText("");
        indexGUI.sizeOfFileLbl.setText("");
        indexGUI.progressBar.setValue(indexGUI.progressBar.getMaximum());
        indexGUI.progressBar.setStringPainted(false);
    }
    
    public void closeIndex () throws IOException {
        luceneIndexer.closeIndex();
    }
}
