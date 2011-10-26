/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.searching;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.util.Utilities;
import edu.coeia.gutil.GUIComponent;
import edu.coeia.util.Tuple;
import edu.coeia.indexing.IndexingConstant ;

import javax.swing.SwingWorker ;
import javax.swing.table.DefaultTableModel ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.DefaultTreeModel ;
import javax.swing.JTree;

import java.io.File ;

import java.util.Date ;
import java.util.ArrayList ;
import java.util.List ;

import edu.coeia.clustering.ClusteringData ;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;

class ProgressSearchData {
    private String path;
    private int count ;
    private int numberOfPatterns ;
    private Document document ;
    
    public ProgressSearchData (int c, Document doc) {
        count = c ;
        this.document = doc;
    }

    public String getPath ()    { return path   ; }
    public int getCount ()      { return count  ; }
    public int getNumberOfPatterns () { return numberOfPatterns ; }
    public Document getDocument () { return this.document ; }
}

class SearcherThread extends SwingWorker<String,ProgressSearchData> {
    private long time ;
    private File indexLocation  ;
    private String queryString ;
    private LuceneSearcher searcher ;
    private int count = 0 ;
    private List<String> extensionsAllowed, filePath;
    private AdvancedSearchPanel panel ;
    private SearchScope searchScope; 
    
    public SearcherThread (File indexLocation, String queryString, AdvancedSearchPanel panel, SearchScope fields) {
        this.indexLocation = indexLocation ;
        this.queryString = queryString;
        this.panel = panel ;
        this.searchScope = fields ;
        
        extensionsAllowed = panel.getSupportedExtension();
        filePath = new ArrayList<String>();
    }
    
    public String doInBackground() {
        try {
            long start = new Date().getTime();
            
            searcher = new LuceneSearcher(indexLocation);
            count = searcher.search(queryString, this.searchScope);

            fillTable();
            
            long end = new Date().getTime();
            time = end-start ;

            searcher.closeSearcher();

           
        } catch (Exception ex) {
           ex.printStackTrace();
        }

        return "" + time ;
    }

    private void fillTable () throws Exception {
        for (int i=0 ; i<count ; i++) {
            Document document = searcher.getDocHits(i);
            
            //if ( Utilities.isExtentionAllowed(extensionsAllowed, Utilities.getExtension(file))) {
                publish(new ProgressSearchData(i, document));

//                if ( document != null )
//                    filePath.add(file);
            //}
        }
    }
    
    @Override
    protected void process(java.util.List<ProgressSearchData> chunks) {
        for (ProgressSearchData pd : chunks) {
            
            String fileId = pd.getDocument().get(IndexingConstant.FILE_ID);
            String fileDate = pd.getDocument().get(IndexingConstant.FILE_DATE);
            String fileTitle = pd.getDocument().get(IndexingConstant.FILE_TITLE);
            String fileName = pd.getDocument().get(IndexingConstant.FILE_NAME);
            
            File file = new File(fileName);
            //String filename = file.getName();
            int size = (int) file.length();
            
            Date date = new Date(file.lastModified());
            
            ((DefaultTableModel)panel.getSearchTable().getModel()).addRow(new Object[] {
                fileId, fileName, fileTitle, Utilities.formatDateTime(date), size, 0
            });
        }

       int index = chunks.size()-1 ;
       //Utilities.scrollToVisible(searchGUI.table,(chunks.get(index)).getCount(),0);
       Utilities.packColumns(panel.getSearchTable(), index);
    }
    
    @Override
    public void done() {
        panel.getSearchProgressBar().setIndeterminate(false);
        //searchGUI.timeLbl.setText("" + Utilities.getTimeFromMilliseconds(time));
        //searchGUI.dateLbl.setText(Utilities.formatDateTime(new Date()));
        //searchGUI.indexLocationLbl.setText(indexLocation.getAbsoluteFile().getName());
        //searchGUI.dataIndexedLocation.setText(queryString);


        try {
            // show clustering in cluster tree
            ArrayList<Tuple<String, ArrayList<String>>> result = ClusteringData.clustetringData(
                    indexLocation, queryString, IndexingConstant.FILE_NAME, IndexingConstant.FILE_CONTENT);        
            buildTree("Result OF Clustering Path", result, panel.getClusterPathTree());

            // show clustering in type cluster tree
            result = getTypeClustering();
            buildTree("Result OF Clustering Type", result, this.panel.getClusterTypeTree());
            
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void buildTree (String header, ArrayList<Tuple<String, ArrayList<String>>> data, JTree tree) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(header);

        for (Tuple<String,ArrayList<String>> cluster: data) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(cluster.getA());

            for (String file: cluster.getB() ){
                DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(file);
                node.add(fileNode);
            }
            root.add(node);
        }

        tree.setModel(new DefaultTreeModel(root));
    }

    private ArrayList<Tuple<String, ArrayList<String>>> getTypeClustering () {
        ArrayList<Tuple<String, ArrayList<String>>> result = new ArrayList<Tuple<String, ArrayList<String>>>();

        for (String ext: extensionsAllowed) {
            Tuple<String, ArrayList<String>> tple = new Tuple<String, ArrayList<String>>();
            tple.setA(ext);
            ArrayList<String> files = getFilesPath(ext);
            tple.setB(files);

            if (files.size() > 0 )
                result.add(tple);
        }

        return result;
    }

    private ArrayList<String> getFilesPath (String ext) {
        ArrayList<String> result = new ArrayList<String>();

        for (String file: filePath) {
            if ( Utilities.getExtension(file).equalsIgnoreCase(ext))
                result.add(file);
        }

        return result;
    }
}
