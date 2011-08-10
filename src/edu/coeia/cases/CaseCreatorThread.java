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

import edu.coeia.utility.Tuple ;
import edu.coeia.utility.FilesCounter ;
import edu.coeia.gui.CaseWizardDialog;
import edu.coeia.gui.InfiniteProgressPanel;

public class CaseCreatorThread extends SwingWorker<Tuple<Integer,Long>,Integer> {

    private ArrayList<String> paths; 
    private InfiniteProgressPanel panel ;
    private Tuple<Integer,Long> indexedDataCountAndSize ;
    private ArrayList<String> ext, pst, ie, ff, msn, yahoo, skype;
    private boolean cache, check;
    private String indexName, indexLocation, investigator, description;
    private CaseWizardDialog indexWizard;
    private Case index ;
    private long indexSize ;
    
    public CaseCreatorThread ( ArrayList<String> path, InfiniteProgressPanel panel,
            CaseWizardDialog iw, String in, String il, String inv, String des,
            ArrayList<String> ext, ArrayList<String> pst, ArrayList<String> ie, ArrayList<String> ff,
            ArrayList<String> msn, ArrayList<String> yahoo, boolean cache, boolean check , Case index,
            ArrayList<String> skype) {
        
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
        this.indexName = in ;
        this.indexLocation = il ;
        this.investigator = inv ;
        this.description = des;
        this.indexWizard = iw ;
        this.index = index ;
        this.skype = skype ;
        this.indexSize = new File(indexLocation).length();
    }

    public Tuple<Integer,Long> doInBackground() {
        indexedDataCountAndSize = getDirectoriesCountAndSize(paths);
        return indexedDataCountAndSize ;
    }

    public Tuple<Integer,Long> getSizeAndCount () {
        return indexedDataCountAndSize ;
    }

    public Tuple<Integer,Long> getDirectoriesCountAndSize (List<String> docs) {
        Tuple<Integer,Long> countSize  = new Tuple<Integer,Long>();
        int count = 0 ;
        long sum = 0l ;

        for (String name: docs){
            File file = new File(name);

            if ( ! file.isFile() ) {
                FilesCounter counter = new FilesCounter(ext);
                file.listFiles(counter);
                count += counter.getNumberOfFiles();
                sum += counter.getSize();
            }
            else {
                count++;
                sum += file.length();
            }
        }

        countSize.setA(count);
        countSize.setB(sum);

        return countSize ;
    }
    
    @Override
    public void done() {
        try {
            panel.stop();
            createIndex();
            this.indexWizard.setVisible(false);
        }
        catch (IOException e) {
        }
    }

    private void createIndex () throws IOException{
        this.index = new Case(indexName, indexLocation, investigator, description,
                paths, ext, pst, ie, ff, msn, yahoo, skype, new Date(), indexSize,
                indexedDataCountAndSize.getB(), indexedDataCountAndSize.getA(), cache, check, false,"","");

        indexWizard.setCurrentCase(index);

        // make new index folders
        CaseManager.CaseOperation.writeCase(index);
    }
}
