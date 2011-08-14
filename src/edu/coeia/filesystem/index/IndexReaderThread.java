/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.filesystem.index;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.main.gui.util.InfiniteProgressPanel;
import edu.coeia.main.chart.PieChartPanel;
import edu.coeia.main.util.Utilities;

import org.apache.lucene.index.IndexReader ;
import org.apache.lucene.store.Directory ;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.TermEnum ;
import org.apache.lucene.index.Term ;
import org.apache.lucene.document.Document ;

import java.io.IOException ;
import java.io.File ;

import java.util.HashMap ;
import java.util.List ;
import java.util.ArrayList ;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingWorker ;
import javax.swing.JPanel;

class IndexReaderThread extends SwingWorker<String, Integer> {
    public enum IndexItem { TAGS, EXT, IMAGES};

    InfiniteProgressPanel panel ;
    IndexFileSystemPanel fileSystemPanel;
    
    boolean status;
    IndexItem type;
    
    private String indexDir, indexName;
    private Directory dir ;
    private IndexReader indexReader ;

    HashMap<String, Integer> tags;
    HashMap<String, Double> exts;
    List<String> images;

    private static Logger logger = Logger.getLogger("IndexReaderThread");
    private static FileHandler handler ;

    public IndexReaderThread (InfiniteProgressPanel i, String location, String name, IndexItem type, IndexFileSystemPanel frame) throws IOException {
        this.panel = i;
        this.status = true;
        this.type = type;
        this.fileSystemPanel = frame;
        
        indexDir = location ;
        indexName = name;
        dir = FSDirectory.open(new File(indexDir));
        indexReader = IndexReader.open(dir);

        try {
            handler = new FileHandler("IndexReaderThread.log");
            logger.addHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Uncaught exception", ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, "Uncaught exception", ex);
        }
    }

    public String doInBackground () {
        switch (type ) {
            case TAGS:
                try {
                    tags = getAllTermFreqFromBody();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Uncaught exception", ex);
                }
                break;

            case EXT:
                exts = getExtensionFreq();
                break;

            case IMAGES:
                try {
                    images = getImagesPath();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Uncaught exception", ex);
                }
                break;
        }
        
        return "" ;
    }

    // get terms and frequncy for all terms in docuemnts
    public HashMap<String,Integer> getAllTermFreqFromBody ()  throws IOException {
        HashMap<String,Integer> map = new HashMap<String,Integer>();
        TermEnum te = indexReader.terms(new Term("body","") );

        while ( te.next() ) {
            Term currentTerm = te.term();

            if ( ! currentTerm.field().equals("body"))
                continue ;

            String termText = currentTerm.text();
            int frequency   = indexReader.docFreq(currentTerm);

            map.put(termText,frequency);
        }

        te.close();

        logger.log(Level.INFO, "Number of tags: " + map.size());
        return map ;
    }

    public List<String> getImagesPath () throws IOException {
        List<String> aList = new ArrayList<String>();

        TermEnum te = indexReader.terms(new Term("mime","") );
        while ( te.next() ) {
            Term currentTerm = te.term();

            if ( ! currentTerm.field().equals("mime"))
                continue ;

            String termText = currentTerm.text();
            aList.add(termText);
        }

        te.close();

        logger.log(Level.INFO, "Number of images: " + aList.size());
        return (aList);
    }

    public HashMap<String,Double> getExtensionFreq () {
        HashMap<String,Double> map = new HashMap<String,Double>();
        logger.log(Level.INFO, "Number of Docs: " + indexReader.numDocs());

        try {
            for (int i=0 ; i<indexReader.numDocs(); i++) {
                Document doc = indexReader.document(i);
                String file = doc.get("filename");

                if ( file == null )
                    continue ;

                String ext = Utilities.getExtension(file);

                if ( ext == null )
                    continue;

                ext = ext.toLowerCase();
                
                if ( map.get(ext) == null ){
                    map.put(ext, 1.0);
                }
                else
                    map.put(ext, map.get(ext) + 1);
            }
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }

        logger.log(Level.INFO, "Exts Size: " + map.size());
        return map ;
    }

    public void close () throws IOException {
        indexReader.close();
    }
    
    public void stop () {
        status = false;
        panel.interrupt();
        panel.stop();
        
        try {
            close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Uncaught exception", ex);
        }
        
        logger.log(Level.INFO, "Finish IndexReaderThread, Done!");
    }

    @Override
    public void done () {
        stop();

        // render result
        if ( this.type == IndexItem.TAGS) {
            //TODO: set in panel 
            this.fileSystemPanel.setTags(tags);
        }
        else if ( this.type == IndexItem.EXT) {
            try {
                JPanel chartPanel = PieChartPanel.getPieChartPanel(exts, "Extension Frequency for: " + indexName);
                //TODO:set in panel
                this.fileSystemPanel.setIndexVisualizationPanel(chartPanel);
            } catch (IOException ex) {
                Logger.getLogger(IndexReaderThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if ( this.type == IndexItem.IMAGES) {
            
        }
    }
}
