/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.filesystem;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.filesystem.index.Indexer;
import edu.coeia.utility.Utilities;
import edu.coeia.main.utilties.IndexGUIComponent ;
import edu.coeia.cases.Case;

import javax.swing.SwingWorker ;
import javax.swing.table.DefaultTableModel ;

import java.io.File ;
import java.io.IOException ;

import java.util.Date ;
import java.util.List ;

import com.pff.PSTException ;

import edu.coeia.cases.CaseManager;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
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

public class IndexerThread extends SwingWorker<String,ProgressIndexData> {

    private long time = 0 ;
    private int progressCount = 0, indexCount=0;
    private Indexer indexer ;
    private IndexGUIComponent indexGUI ;
    private Case index ;
    private int totalNumberOfFiles ;
    private int totalNumberOfError = 0;
    private boolean indexStatus = false;

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private FileHandler handler ;

    public IndexerThread (File indexLocation, IndexGUIComponent indexGUI, Case index,
            List<String> imagesPath) {
        this.indexGUI = indexGUI ;
        this.index = index ;
        this.totalNumberOfFiles = index.getDataIndexedCount();
       
        try {
            handler = new FileHandler("indexing.log");
            logger.addHandler(handler);
            indexer = new Indexer(indexLocation, imagesPath , true);
            logger.log(Level.INFO, "this is first line in indexing");
        } catch (IOException ex) {
           logger.log(Level.SEVERE, "Uncaught exception", ex);
        }

        this.indexGUI.progressBar.setStringPainted(true);
        this.indexGUI.progressBar.setMaximum(index.getDataIndexedCount() + index.getPstPath().size());
        this.indexGUI.numberOfErrorFilesLbl.setText("0");
    }
    
    public String doInBackground() {
        long start = new Date().getTime();
         
        // index selected files
        logger.log(Level.INFO, "Start Index Files");
        for ( String dirName : index.getDocumentInIndex() ) {
            File file = new File(dirName);
            System.out.println("File: " + file.getAbsolutePath());
            boolean ignoreEmail = true;
            dirTraversal(file, ignoreEmail);
        }

        logger.log(Level.INFO, "Start Index Email if contain");
        
        // index outlooks pst files
        for (String path: index.getPstPath() ) {
            logger.log(Level.INFO, "Index Mail: " + path);
            File file = new File(path);
            boolean ignoreEmail = false;
            dirTraversal(file, ignoreEmail);
        }

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
        }
        else {
            long size = dir.length();
            
            // if file size more than 3 MB then show size message to indicate that indexing will take some time
            String msg = size > 3145728 ? "This file will take some minutes to index, please wait..." : " " ;

            if ( Utilities.isExtentionAllowed(index.getExtensionAllowed(), Utilities.getExtension(dir))) {
                publish(new ProgressIndexData( progressCount,indexCount, dir.getAbsolutePath(), "" , 1 , msg));
                logger.log(Level.INFO, "Index File: " + dir.getAbsolutePath() + " , size: " + getSize(dir.getAbsolutePath()));
                try {
                    if ( isEmailFile(dir.getAbsolutePath()) &&  ignoreEmail ) {
                        indexCount++;
                    }
                    else {
                        boolean status = indexer.indexFile(dir);

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
            else {
                //System.out.println( dir.getName() +  " is not allowed");
            }
        }
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
                ((DefaultTableModel)indexGUI.table.getModel()).addRow(new Object[] { Utilities.getExtension(pd.getPath())
                    , pd.getPath(), "cannot index this file (password protected or internal error in file format)"});

                totalNumberOfError++;
                indexGUI.numberOfErrorFilesLbl.setText("" + totalNumberOfError);
                
                indexGUI.progressBar.setValue( pd.getProgressCount() );
//                indexGUI.progressBar.setValue( (pd.getProgressCount()/totalNumberOfFiles)*100 );
            }
            else {
                indexGUI.currentFileLbl.setText(pd.getPath());
                //indexGUI.sizeOfFileLbl.setText(new File(pd.getPath()).length() + " Bytes");
                indexGUI.sizeOfFileLbl.setText(getSize(pd.getPath()));
                indexGUI.numberOfFileLbl.setText("(" + pd.getIndexCount() + "/" + totalNumberOfFiles + ")");
                indexGUI.fileExtensionLbl.setText(Utilities.getExtension(pd.getPath()));
                indexGUI.bigSizeMsgLbl.setText(pd.getSizeMsg());
                
                indexGUI.progressBar.setValue( pd.getProgressCount() );
//                indexGUI.progressBar.setValue( (pd.getProgressCount()/totalNumberOfFiles)*100 );
                indexGUI.progressBar.setValue( pd.getIndexCount() );
            }
        }

       int indexNum = chunks.size()-1 ;
       Utilities.scrollToVisible(indexGUI.table,(chunks.get(indexNum)).getProgressCount(),0);
    }

    private String getSize (String path) {
        long fileSize = new File(path).length() ;
        String resultString = "";

        if ( fileSize > 1073741824L) {
            long tmp = (long) Utilities.toGB(fileSize);
            resultString = tmp + " GB";
        }
        else if ( fileSize > 1048576 ) {
            long tmp = (long) Utilities.toMB(fileSize);
            resultString = tmp + " MB";
        }
        else if ( fileSize > 1024 ) {
            long tmp = (long) Utilities.toKB(fileSize);
            resultString = tmp + " KB";
        }
        else {
             resultString = fileSize + " Bytes" ;
        }
        
        return (resultString);
    }

    @Override
    public void done() {
        logger.log(Level.INFO, "Done Indexing Process");
        
        indexGUI.progressBar.setValue(indexGUI.progressBar.getMaximum());
        indexGUI.progressBar.setStringPainted(false);
        String indexingTime = "" + Utilities.getTimeFromMilliseconds(time) ;
        String lastIndexDate = Utilities.formatDateTime(new Date()) ;

        indexGUI.timeLbl.setText(indexingTime);
        indexGUI.dateLbl.setText(lastIndexDate);

        indexGUI.startIndexingButton.setEnabled(true);
        indexGUI.stopIndexingButton.setEnabled(false);
        
        // save log files
        try {
            if ( indexStatus ) {
                index.setIndexStatus(true);
                index.setLastIndexDate(lastIndexDate);
                index.setIndexingTime(indexingTime);
                CaseManager.CaseOperation.writeCase(index);
                //System.out.println("write status information after indexing");
            }
            closeIndex();
        }
        catch (IOException e){
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }

        // show message box after finish the indexing process
        if ( indexStatus )
            JOptionPane.showMessageDialog(null, "Indexing Process Finished","Indexing Process Finished",
                    JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "Indexing Process Stopped","There is an Error In Indexing Process",
                    JOptionPane.INFORMATION_MESSAGE);

        clearFields();
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
        indexer.closeIndex();
    }
}
