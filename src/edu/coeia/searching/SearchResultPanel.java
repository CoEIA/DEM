/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SearchResultPanel.java
 *
 * Created on Dec 21, 2011, 11:38:17 AM
 */
package edu.coeia.searching;

import edu.coeia.cases.Case;
import edu.coeia.util.FilesPath ;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.hash.HashCalculator;
import edu.coeia.hashanalysis.HashItem;
import edu.coeia.hashanalysis.HashSetDialog;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.main.CaseFrame;
import edu.coeia.viewer.SearchViewer;
import edu.coeia.viewer.SourceViewerDialog;

import java.awt.event.InputEvent;

import javax.swing.JTable;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

import java.util.List; 
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.apache.lucene.document.Document;

/**
 *
 * @author wajdyessam
 */
public class SearchResultPanel extends javax.swing.JPanel {

    private final static Logger logger = Logger.getLogger(FilesPath.LOG_NAMESPACE);
    
    private JFrame parentFrame; 
    private Case caseObj;
    
    private String keyword ;
    private List<Integer> documentIds = new ArrayList<Integer>();
    
    /** Creates new form SearchResultPanel */
    public SearchResultPanel(JFrame parentFrame) {
        initComponents();

        this.parentFrame = parentFrame;
        this.caseObj  = ((CaseFrame) this.parentFrame).getCase();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        searchTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel22.setLayout(new java.awt.BorderLayout());

        searchTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Last Modification", "Type", "Path/Title"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        searchTable.setFillsViewportHeight(true);
        searchTable.setGridColor(new java.awt.Color(255, 255, 255));
        searchTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        searchTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchTableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                searchTableMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(searchTable);

        jPanel22.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane4.addTab("Search Result", jPanel22);

        add(jTabbedPane4, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void searchTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchTableMouseClicked
        this.resultTableClicked(evt);
    }//GEN-LAST:event_searchTableMouseClicked

    private void searchTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchTableMousePressed
        this.resultTableRightClicked(evt);
    }//GEN-LAST:event_searchTableMousePressed

    void setSearchTableFocusable() {
        this.searchTable.requestFocusInWindow();
    }
    
    void clearSearchTable() {
        JTableUtil.removeAllRows(this.searchTable);
    }
     
    public JTable getSearchTable() {
        return this.searchTable ;
    }
    
    private void resultTableRightClicked(java.awt.event.MouseEvent evt) {
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( this.searchTable.isEnabled() )
                this.showHashSetPopup(evt);
        }
    }
    
    private void resultTableClicked(java.awt.event.MouseEvent evt) {
        // set summary panel
        try {
            if ( evt.getClickCount() == 2 ) { // Double Click
                // other click event
                int row = searchTable.getSelectedRow();
                if ( row < 0 ) return ; // if not select row
                
                String fileId = String.valueOf(searchTable.getValueAt(row, 0));
                int currentId = Integer.parseInt(fileId);
                //this.parentPanel.setCurrentId(currentId);
                
                SearchViewer searchViewer = new SearchViewer(this.keyword,
                        currentId, this.documentIds);
                
                SourceViewerDialog panel = new SourceViewerDialog(this.parentFrame, true, searchViewer);
                panel.setVisible(true);
            }
        }
        catch (Exception e ){
            logger.log(Level.SEVERE, "Uncaught exception", e);
            e.printStackTrace();
        }
    }
    
    private void showHashSetPopup (java.awt.event.MouseEvent event) {

        final JTable table = (JTable) event.getSource();
        JPopupMenu popup = new JPopupMenu();
        JButton btn = new JButton("Adding to Hash Set");
        
        btn.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed (java.awt.event.ActionEvent event) {
                try {
                    LuceneSearcher searcher = new LuceneSearcher(caseObj);
                    
                    int[] indexes = table.getSelectedRows();
                    List<HashItem> hashItems = new ArrayList<HashItem>();
                    
                    for(int i: indexes) {
                        int id = Integer.valueOf(String.valueOf(table.getValueAt(table.convertRowIndexToView(i), 0)));
                        Document currentDocument = searcher.getDocument(String.valueOf(id));
                        hashItems.add(getHashItemFromDocument(currentDocument));
                    }
                    
                    // display hash set dialog
                    HashSetDialog hashSetDialog = new HashSetDialog(parentFrame, true, hashItems);
                    hashSetDialog.setVisible(true);
                    
                    searcher.closeSearcher();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        popup.add(btn);
        table.setComponentPopupMenu(popup);
    }
        
    private HashItem getHashItemFromDocument(final Document document) {
        HashItem item = null;
        
        if ( IndexingConstant.isFileDocument(document) ) {
            String fileName = document.get(IndexingConstant.FILE_TITLE);
            String filePath = document.get(IndexingConstant.FILE_NAME);

            String hashValue = HashCalculator.calculateFileHash(filePath);
            
            item = HashItem.newInstance(fileName, filePath, this.caseObj.getCaseName(),
                    this.caseObj.getCaseLocation(), this.caseObj.getInvestigatorName(), 
                    new Date(), hashValue);
        }
                
        return item;
    }
    
    void setQueryText(final String query) {
        this.keyword = query;
    }
    
    void setResultIds(final List<Integer> ids) {
        this.documentIds.clear();
        this.documentIds.addAll(Collections.unmodifiableList(ids));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel22;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTable searchTable;
    // End of variables declaration//GEN-END:variables
}
