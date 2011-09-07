package edu.coeia.cases;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author wajdyessam
 */

import javax.swing.SwingWorker ;

import java.io.File ;
import java.io.IOException ;

import java.util.ArrayList ;
import java.util.List ;
import java.util.Date ;

import edu.coeia.main.util.Tuple ;
import edu.coeia.main.gui.util.InfiniteProgressPanel;
import edu.coeia.main.util.FilesPath;

import org.apache.log4j.Logger;

class CaseCreatorThread extends SwingWorker<Tuple<Integer,Long>,Integer> {

    private ArrayList<String> paths; 
    private InfiniteProgressPanel panel ;
    //private Tuple<Integer,Long> indexedDataCountAndSize ;
    private ArrayList<String> pst, ie, ff, msn, yahoo, skype;
    private List<String> ext ;
    private boolean cache, check;
    private String caseName, caseLocation, investigator, description;
    private CaseWizardDialog caseWizardDlg;
    private Case caseObj ;
    private long caseSize ;
    
    private static final Logger logger = Logger.getLogger(FilesPath.LOG_NAMESPACE);
    
    public CaseCreatorThread ( ArrayList<String> path, InfiniteProgressPanel panel,
            CaseWizardDialog iw, String in, String il, String inv, String des,
            List<String> ext, ArrayList<String> pst, ArrayList<String> ie, ArrayList<String> ff,
            ArrayList<String> msn, ArrayList<String> yahoo, boolean cache, boolean check , Case index,
            ArrayList<String> skype) {
        
        logger.info("CaseCreaterThread Constructor");
        
        this.paths = path ;
        this.panel = panel ;
        this.ext = ext ;
        this.pst = pst ;
        this.ie = ie ;
        this.ff = ff ;
        this.msn = msn;
        this.yahoo = yahoo ;
        this.cache = cache;
        this.check = check;
        this.caseName = in ;
        this.caseLocation = il ;
        this.investigator = inv ;
        this.description = des;
        this.caseWizardDlg = iw ;
        this.caseObj = index ;
        this.skype = skype ;
        this.caseSize = new File(caseLocation).length();
    }

    @Override
    public Tuple<Integer,Long> doInBackground() {
        //indexedDataCountAndSize = getDirectoriesCountAndSize(paths);
        //return indexedDataCountAndSize ;
        return null;
    }

//    public Tuple<Integer,Long> getSizeAndCount () {
//        return indexedDataCountAndSize ;
//    }

//    public Tuple<Integer,Long> getDirectoriesCountAndSize (List<String> docs) {
//        Tuple<Integer,Long> countSize  = new Tuple<Integer,Long>();
//        int count = 0 ;
//        long sum = 0l ;
//
//        for (String name: docs){
//            File file = new File(name);
//
//            if ( ! file.isFile() ) {
//                FilesCounter counter = new FilesCounter(ext);
//                file.listFiles(counter);
//                count += counter.getNumberOfFiles();
//                sum += counter.getSize();
//            }
//            else if ( Utilities.isExtentionAllowed(ext, Utilities.getExtension(file)) ){
//                count++;
//                logger.info("Compute: " + file.getAbsolutePath());
//                sum += file.length();
//            }
//        }
//
//        countSize.setA(count);
//        countSize.setB(sum);
//
//        return countSize ;
//    }
    
    @Override
    public void done() {
        try {
            panel.stop();
            createIndex();
            this.caseWizardDlg.setVisible(false);
        }
        catch (IOException e) {
        }
    }

    private void createIndex () throws IOException{
        this.caseObj = new Case(caseName, caseLocation, investigator, description,
                paths, ext, pst, ie, ff, msn, yahoo, skype, new Date(), caseSize,
                cache, check, false,"","");

        caseWizardDlg.setCurrentCase(caseObj);

        // make new index folders
        CaseManager.CaseOperation.writeCase(caseObj);
    }
}
