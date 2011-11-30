/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SourceViewerDialog.java
 *
 * Created on Nov 28, 2011, 2:20:54 PM
 */
package edu.coeia.main;

import edu.coeia.searching.AdvancedSearchPanel;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.searching.LuceneSearcher ;

import java.awt.BorderLayout;

import java.util.List; 

import org.apache.lucene.document.Document;

/**
 *
 * @author wajdyessam
 */
public class SourceViewerDialog extends javax.swing.JDialog {
    private String keyword ;
    private AdvancedSearchPanel advancedSearchPanel ;
    private int currentId ;
    private LuceneSearcher searcher ;
    private List<Integer> ids;
    
    /** Creates new form SourceViewerDialog */
    public SourceViewerDialog(java.awt.Frame parent, boolean modal, AdvancedSearchPanel panel) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(parent);
        
        this.advancedSearchPanel = panel;
        this.keyword = this.advancedSearchPanel.getQueryText();
        this.currentId = this.advancedSearchPanel.getCurrentId() ;
        this.searcher = this.advancedSearchPanel.getLuceneSearcher();
        this.ids = this.advancedSearchPanel.getIds();
        
        try {
            Document document = this.searcher.getDocument(String.valueOf(this.currentId));
            showDocument(document);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        controlPanel = new javax.swing.JPanel();
        movePanel = new javax.swing.JPanel();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        itemsPanel = new javax.swing.JPanel();
        tagButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        viewerPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Previewer Items Dialog");

        movePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Navigation"));

        previousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/main/resources/Previous48.png"))); // NOI18N
        previousButton.setText("Previous");

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/main/resources/next48.png"))); // NOI18N
        nextButton.setText("Next");

        javax.swing.GroupLayout movePanelLayout = new javax.swing.GroupLayout(movePanel);
        movePanel.setLayout(movePanelLayout);
        movePanelLayout.setHorizontalGroup(
            movePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(movePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(previousButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nextButton)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        movePanelLayout.setVerticalGroup(
            movePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, movePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(movePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nextButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(previousButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                .addContainerGap())
        );

        controlPanel.add(movePanel);

        itemsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Tag and Export"));

        tagButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/main/resources/tag64.png"))); // NOI18N
        tagButton.setText("Tag Item");

        exportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/main/resources/export48.png"))); // NOI18N
        exportButton.setText("Export Item");

        javax.swing.GroupLayout itemsPanelLayout = new javax.swing.GroupLayout(itemsPanel);
        itemsPanel.setLayout(itemsPanelLayout);
        itemsPanelLayout.setHorizontalGroup(
            itemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tagButton, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(exportButton)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        itemsPanelLayout.setVerticalGroup(
            itemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, itemsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(itemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(exportButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tagButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, Short.MAX_VALUE))
                .addContainerGap())
        );

        controlPanel.add(itemsPanel);

        getContentPane().add(controlPanel, java.awt.BorderLayout.NORTH);

        statusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Status"));

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 710, Short.MAX_VALUE)
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        getContentPane().add(statusPanel, java.awt.BorderLayout.SOUTH);

        viewerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Viewer"));

        javax.swing.GroupLayout viewerPanelLayout = new javax.swing.GroupLayout(viewerPanel);
        viewerPanel.setLayout(viewerPanelLayout);
        viewerPanelLayout.setHorizontalGroup(
            viewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 710, Short.MAX_VALUE)
        );
        viewerPanelLayout.setVerticalGroup(
            viewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 438, Short.MAX_VALUE)
        );

        getContentPane().add(viewerPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void showDocument (Document document) {
        if ( document.get(IndexingConstant.DOCUMENT).equals(IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.FILE))) {
            FileSourceViewerPanel panel = new FileSourceViewerPanel(this);
            
            this.viewerPanel.setLayout(new BorderLayout());
            this.viewerPanel.add(panel, BorderLayout.CENTER);
            this.viewerPanel.revalidate();
        }
        else if ( document.get(IndexingConstant.DOCUMENT).equals(IndexingConstant.getDocumentType(IndexingConstant.DOCUMENT_TYPE.CHAT))) {
            ChatSourceViewerPanel panel = new ChatSourceViewerPanel(this);
            
            this.viewerPanel.setLayout(new BorderLayout());
            this.viewerPanel.add(panel, BorderLayout.CENTER);
            this.viewerPanel.revalidate();
        }
        else
            System.out.println("document: " + document.get(IndexingConstant.DOCUMENT));
    }
    
    LuceneSearcher getLuceneSearch() { return this.searcher ; }
    String getQueryString() { return this.keyword ; }
    String getCurrentId() { return String.valueOf(this.currentId);  }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel controlPanel;
    private javax.swing.JButton exportButton;
    private javax.swing.JPanel itemsPanel;
    private javax.swing.JPanel movePanel;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton previousButton;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JButton tagButton;
    private javax.swing.JPanel viewerPanel;
    // End of variables declaration//GEN-END:variables
}
