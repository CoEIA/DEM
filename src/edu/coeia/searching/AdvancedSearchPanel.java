/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AdvancedSearchPanel.java
 *
 * Created on Oct 22, 2011, 8:17:54 AM
 */
package edu.coeia.searching;

import edu.coeia.cases.Case;
import edu.coeia.gutil.GuiUtil;
import edu.coeia.util.FilesPath ;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.gutil.JTableUtil;

import java.awt.BorderLayout;
import java.awt.event.InputEvent;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JOptionPane ;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFrame;

import java.io.File ;

import java.util.List; 
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;

/**
 *
 * @author wajdyessam
 */
public class AdvancedSearchPanel extends javax.swing.JPanel {

    private JWebBrowser fileBrowser = new JWebBrowser();
    
    private Case caseObj;
    private JFrame parentFrame ;
    private final static Logger logger = Logger.getLogger(FilesPath.LOG_NAMESPACE);
    
    /** Creates new form AdvancedSearchPanel */
    public AdvancedSearchPanel(Case aIndex, JFrame aParentFrame) {
        initComponents();
        
        this.caseObj = aIndex;
        this.parentFrame = aParentFrame;
        
        // add file browser
        fileBrowser.setBarsVisible(false);
        fileBrowser.setStatusBarVisible(false);
        fileRenderPanel.add(fileBrowser, BorderLayout.CENTER);  
        
        JTableUtil.packColumns(searchTable, 0);
        disableNotIndexedComponent();        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LeftPanel = new javax.swing.JPanel();
        searchScopePanel = new javax.swing.JPanel();
        fileSystemCheckBox = new javax.swing.JCheckBox();
        fileSystemMetadataCheckBox = new javax.swing.JCheckBox();
        fileSystemContentCheckBox = new javax.swing.JCheckBox();
        emailCheckBox = new javax.swing.JCheckBox();
        emailHeaderCheckBox = new javax.swing.JCheckBox();
        emailContentCheckBox = new javax.swing.JCheckBox();
        chatCheckBox = new javax.swing.JCheckBox();
        chatContentCheckBox = new javax.swing.JCheckBox();
        headerPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        queryTextField = new javax.swing.JTextField();
        advancedSearchLabelButton = new javax.swing.JLabel();
        startSearchingButton = new javax.swing.JButton();
        clearLabelButton = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        searchProgressBard = new javax.swing.JProgressBar();
        resultSavingButton = new javax.swing.JButton();
        CenterPanel = new javax.swing.JPanel();
        resultPanel = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        searchTable = new javax.swing.JTable();
        viewPanel = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        fileRenderPanel = new javax.swing.JPanel();
        FileMetaDataPanel = new javax.swing.JPanel();
        jScrollPane28 = new javax.swing.JScrollPane();
        metaDataTextArea = new javax.swing.JTextArea();
        imageViewPanel = new javax.swing.JPanel();
        audioViewPanel = new javax.swing.JPanel();
        videoViewPanel = new javax.swing.JPanel();
        documentView = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        searchScopePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Search Scope"));

        fileSystemCheckBox.setSelected(true);
        fileSystemCheckBox.setText("File System:");
        fileSystemCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileSystemCheckBoxActionPerformed(evt);
            }
        });

        fileSystemMetadataCheckBox.setText("Metadata");

        fileSystemContentCheckBox.setSelected(true);
        fileSystemContentCheckBox.setText("Content");

        emailCheckBox.setText("Email:");
        emailCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailCheckBoxActionPerformed(evt);
            }
        });

        emailHeaderCheckBox.setText("Headers");

        emailContentCheckBox.setText("Content");

        chatCheckBox.setText("Instant Messaging:");
        chatCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chatCheckBoxActionPerformed(evt);
            }
        });

        chatContentCheckBox.setText("Content");

        javax.swing.GroupLayout searchScopePanelLayout = new javax.swing.GroupLayout(searchScopePanel);
        searchScopePanel.setLayout(searchScopePanelLayout);
        searchScopePanelLayout.setHorizontalGroup(
            searchScopePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchScopePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchScopePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fileSystemCheckBox)
                    .addComponent(emailCheckBox)
                    .addGroup(searchScopePanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(searchScopePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, searchScopePanelLayout.createSequentialGroup()
                                .addComponent(emailHeaderCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(emailContentCheckBox))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, searchScopePanelLayout.createSequentialGroup()
                                .addComponent(fileSystemMetadataCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fileSystemContentCheckBox))))
                    .addGroup(searchScopePanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(chatContentCheckBox))
                    .addComponent(chatCheckBox))
                .addContainerGap(120, Short.MAX_VALUE))
        );
        searchScopePanelLayout.setVerticalGroup(
            searchScopePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchScopePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileSystemCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(searchScopePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileSystemMetadataCheckBox)
                    .addComponent(fileSystemContentCheckBox))
                .addGap(18, 18, 18)
                .addComponent(emailCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(searchScopePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailHeaderCheckBox)
                    .addComponent(emailContentCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(chatCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chatContentCheckBox)
                .addContainerGap())
        );

        headerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Search Options"));

        jLabel1.setText("Query: ");

        queryTextField.setText(" ");

        advancedSearchLabelButton.setForeground(new java.awt.Color(0, 51, 255));
        advancedSearchLabelButton.setText("Advanced Search");
        advancedSearchLabelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                advancedSearchLabelButtonMouseClicked(evt);
            }
        });

        startSearchingButton.setText("Search");
        startSearchingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startSearchingButtonActionPerformed(evt);
            }
        });

        clearLabelButton.setForeground(new java.awt.Color(0, 0, 255));
        clearLabelButton.setText("Clear");
        clearLabelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clearLabelButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(queryTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(startSearchingButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                        .addComponent(advancedSearchLabelButton)
                        .addGap(18, 18, 18)
                        .addComponent(clearLabelButton)))
                .addContainerGap())
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(queryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startSearchingButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearLabelButton)
                    .addComponent(advancedSearchLabelButton)))
        );

        resultSavingButton.setText("Save Results");
        resultSavingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resultSavingButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(searchProgressBard, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(resultSavingButton)
                .addContainerGap(117, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchProgressBard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(resultSavingButton)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout LeftPanelLayout = new javax.swing.GroupLayout(LeftPanel);
        LeftPanel.setLayout(LeftPanelLayout);
        LeftPanelLayout.setHorizontalGroup(
            LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchScopePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        LeftPanelLayout.setVerticalGroup(
            LeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(searchScopePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(LeftPanel, java.awt.BorderLayout.WEST);

        CenterPanel.setLayout(new java.awt.BorderLayout());

        searchTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "File Name", "File Path", "Last Modification", "File Size", "File Tag"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        searchTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchTableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                searchTableMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(searchTable);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
        );

        jTabbedPane4.addTab("Search Result", jPanel22);

        javax.swing.GroupLayout resultPanelLayout = new javax.swing.GroupLayout(resultPanel);
        resultPanel.setLayout(resultPanelLayout);
        resultPanelLayout.setHorizontalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                .addContainerGap())
        );
        resultPanelLayout.setVerticalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
        );

        CenterPanel.add(resultPanel, java.awt.BorderLayout.CENTER);

        fileRenderPanel.setLayout(new java.awt.BorderLayout());
        jTabbedPane2.addTab("Text Content", fileRenderPanel);

        metaDataTextArea.setColumns(20);
        metaDataTextArea.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        metaDataTextArea.setRows(5);
        jScrollPane28.setViewportView(metaDataTextArea);

        javax.swing.GroupLayout FileMetaDataPanelLayout = new javax.swing.GroupLayout(FileMetaDataPanel);
        FileMetaDataPanel.setLayout(FileMetaDataPanelLayout);
        FileMetaDataPanelLayout.setHorizontalGroup(
            FileMetaDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane28, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
        );
        FileMetaDataPanelLayout.setVerticalGroup(
            FileMetaDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane28, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("MetaData", FileMetaDataPanel);

        javax.swing.GroupLayout imageViewPanelLayout = new javax.swing.GroupLayout(imageViewPanel);
        imageViewPanel.setLayout(imageViewPanelLayout);
        imageViewPanelLayout.setHorizontalGroup(
            imageViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 573, Short.MAX_VALUE)
        );
        imageViewPanelLayout.setVerticalGroup(
            imageViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 161, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Image", imageViewPanel);

        javax.swing.GroupLayout audioViewPanelLayout = new javax.swing.GroupLayout(audioViewPanel);
        audioViewPanel.setLayout(audioViewPanelLayout);
        audioViewPanelLayout.setHorizontalGroup(
            audioViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 573, Short.MAX_VALUE)
        );
        audioViewPanelLayout.setVerticalGroup(
            audioViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 161, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Audio", audioViewPanel);

        javax.swing.GroupLayout videoViewPanelLayout = new javax.swing.GroupLayout(videoViewPanel);
        videoViewPanel.setLayout(videoViewPanelLayout);
        videoViewPanelLayout.setHorizontalGroup(
            videoViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 573, Short.MAX_VALUE)
        );
        videoViewPanelLayout.setVerticalGroup(
            videoViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 161, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Video", videoViewPanel);

        javax.swing.GroupLayout documentViewLayout = new javax.swing.GroupLayout(documentView);
        documentView.setLayout(documentViewLayout);
        documentViewLayout.setHorizontalGroup(
            documentViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 573, Short.MAX_VALUE)
        );
        documentViewLayout.setVerticalGroup(
            documentViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 161, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Document View", documentView);

        javax.swing.GroupLayout viewPanelLayout = new javax.swing.GroupLayout(viewPanel);
        viewPanel.setLayout(viewPanelLayout);
        viewPanelLayout.setHorizontalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                .addContainerGap())
        );
        viewPanelLayout.setVerticalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        CenterPanel.add(viewPanel, java.awt.BorderLayout.SOUTH);

        add(CenterPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void startSearchingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startSearchingButtonActionPerformed
        startSearching();
    }//GEN-LAST:event_startSearchingButtonActionPerformed

    private void clearLabelButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearLabelButtonMouseClicked
        removeSearchField(true,false);
    }//GEN-LAST:event_clearLabelButtonMouseClicked

    private void advancedSearchLabelButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_advancedSearchLabelButtonMouseClicked
        showAdvancedSearch();
    }//GEN-LAST:event_advancedSearchLabelButtonMouseClicked

    private void searchTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchTableMouseClicked
        resultTableClicked(evt);
    }//GEN-LAST:event_searchTableMouseClicked

    private void searchTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchTableMousePressed
        tableMouseEvent(evt);
    }//GEN-LAST:event_searchTableMousePressed

    private void resultSavingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resultSavingButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resultSavingButtonActionPerformed

    private void fileSystemCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSystemCheckBoxActionPerformed
        if ( fileSystemCheckBox.isSelected() ) {
            fileSystemContentCheckBox.setEnabled(true);
            fileSystemMetadataCheckBox.setEnabled(true);
        }
        else {
            fileSystemContentCheckBox.setEnabled(false);
            fileSystemMetadataCheckBox.setEnabled(false);
        }
    }//GEN-LAST:event_fileSystemCheckBoxActionPerformed

    private void emailCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailCheckBoxActionPerformed
        if ( emailCheckBox.isSelected() ) {
            emailContentCheckBox.setEnabled(true);
            emailHeaderCheckBox.setEnabled(true);
        }
        else {
            emailContentCheckBox.setEnabled(false);
            emailHeaderCheckBox.setEnabled(false);
        }
    }//GEN-LAST:event_emailCheckBoxActionPerformed

    private void chatCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chatCheckBoxActionPerformed
        if ( chatCheckBox.isSelected() ) {
            chatContentCheckBox.setEnabled(true);
        }
        else {
            chatContentCheckBox.setEnabled(false);
        }        
    }//GEN-LAST:event_chatCheckBoxActionPerformed

    private void tableMouseEvent(java.awt.event.MouseEvent evt) {
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( searchTable.isEnabled() )
                GuiUtil.showPopup(evt);
        }
    }
    
    private void resultTableClicked(java.awt.event.MouseEvent evt) {
        // set summary panel
        try {
            if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
                GuiUtil.showPopup(evt);
                return ;
            }

            // other click event
            int row = searchTable.getSelectedRow();
            String fileId = String.valueOf(searchTable.getValueAt(row, 0));

            showInformationByID(fileId);
        }
        catch (Exception e ){
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }
    
    private void showAdvancedSearch() {
        AdvancedSearchDialog asd = new AdvancedSearchDialog(null, true);
        asd.setVisible(true);

        String query = asd.getQuery() ;

        if ( query == null || query.isEmpty() )
            return ;

        queryTextField.setText(query);
        startSearching();
    }
    
//    private void clusterPathTreeAction() {
//        try {
//            DefaultMutableTreeNode node = (DefaultMutableTreeNode) clusterPathTree.getLastSelectedPathComponent();
//            if ( node == null || node.isRoot() || ! node.isLeaf()) {
//                return ;
//            }
//            
//            String filePath= node.getUserObject().toString();
//            showInformation(filePath);
//       }
//       catch (Exception e ){
//           logger.log(Level.SEVERE, "Uncaught exception", e);
//       }
//    }
//    
//    private void clusterTypeTreeAction() {
//        try {
//            DefaultMutableTreeNode node = (DefaultMutableTreeNode) clusterTypeTree.getLastSelectedPathComponent();
//            if ( node == null || node.isRoot() || ! node.isLeaf()) {
//                return ;
//            }
//
//            String filePath= node.getUserObject().toString();
//            showInformation(filePath);
//       }
//       catch (Exception e ){
//       }
//    }
    
    private void showInformationByID (String fileId) {        
        try {
            File indexPath = new File(caseObj.getIndexLocation() + "\\" + FilesPath.INDEX_PATH);
            
            // Do Lucene Search
            LuceneSearcher searcher = new LuceneSearcher(indexPath);
            Document document = searcher.searchById(fileId);
            
            //TODO: Getting Object Type
            // Show Object Content, FILE, CHAT, EMAIL
            
            // Show File Content
            String content = document.get(IndexingConstant.FILE_CONTENT);
            String keyword = queryTextField.getText().trim().toLowerCase();
            fileBrowser.setHTMLContent(highlightString(content, keyword));
            
            // show matadata information for File
            List<Fieldable> fields = document.getFields();
            StringBuilder metadataBuilder = new StringBuilder();
            
            for (Fieldable field: fields) {
                if ( ! field.name().startsWith("file_")) // files in IndexingConstant start with prefix file_
                    metadataBuilder.append(field.name()).append(" : " ).append(field.stringValue()).append("\n");
            }
            
            String metadata = metadataBuilder.toString();
            //TODO: replace metadate view to browser or html type to support html rendering
            //metaDataTextArea.setText(highlightString(metadata, keyword));
            metaDataTextArea.setText(metadata);

            fileRenderPanel.validate();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private String highlightString (String content, String keyword) {
        String highlither = "<span style=\"background-color: #FFFF00\">" + keyword +  "</span>" ;
        String highlitedString = content.replace(keyword, highlither);
        
        return highlitedString ;
    }
    
    private void startSearching () {
        removeSearchField(false,false);

        if ( caseObj.getIndexStatus() == false ) {
            JOptionPane.showMessageDialog(this, "please do the indexing operation first before do any operation",
                    "Case is not indexed",JOptionPane.ERROR_MESSAGE );
            return ;
        }

        File indexLocation = new File (caseObj.getIndexLocation() + "\\" + FilesPath.INDEX_PATH);
        String queryString = queryTextField.getText().trim();

        if ( queryString.isEmpty() ) {
            JOptionPane.showMessageDialog(this, "please fill the query string and choose an index location");
            return  ;
        }

        JTableUtil.packColumns(searchTable, 2);
        searchProgressBard.setIndeterminate(true);
        
        // build lucene fileds
        SearchScope searchScope = getSearchScope();
        
         SearcherThread sThread = new SearcherThread(indexLocation,queryString,this, searchScope);
         sThread.execute();
    }
    
    private SearchScope getSearchScope() {
        SearchScope.Builder builder = new SearchScope.Builder();
        
        if ( fileSystemCheckBox.isSelected() ) {
            if ( fileSystemContentCheckBox.isSelected() ) {
                builder = builder.fileSystemContent(true);
            }
            
            if ( fileSystemMetadataCheckBox.isSelected() ) {
                builder = builder.fileSystemMetadata(true);
            }
        }
        
        if ( emailCheckBox.isSelected() ) {
            if ( emailContentCheckBox.isSelected() ) {
                builder = builder.emailContent(true);
            }
            
            if ( emailHeaderCheckBox.isSelected() ) {
                builder = builder.emailHeader(true);
            }
        }
        
        if ( chatCheckBox.isSelected() ) {
            if ( chatContentCheckBox.isSelected() ) {
                builder = builder.chatContent(true);
            }
        }
        
        return builder.build();
    }

    JProgressBar getSearchProgressBar () { return this.searchProgressBard ; }
    JTable getSearchTable() { return this.searchTable ; }
    List<String> getSupportedExtension () { return new ArrayList<String>(); }
    
    private void removeSearchField (boolean all, boolean restCheckBox) {
        searchProgressBard.setIndeterminate(false); 

        ( (DefaultTableModel) searchTable.getModel() ).getDataVector().removeAllElements();
        ( (DefaultTableModel) searchTable.getModel() ).fireTableDataChanged();
        
        fileBrowser.setHTMLContent("");

        if ( all ) {
            queryTextField.setText("");
        }

        if ( restCheckBox ) {
        }
    }
    
    private void disableNotIndexedComponent () {
        if ( caseObj.getDocumentInIndex().isEmpty() ) {
            startSearchingButton.setEnabled(false);
//            clearFieldsButton.setEnabled(false);
//            keywordsListButton.setEnabled(false);
        }
    }
    
    public void setSearchKeyword (String text) {
        queryTextField.setText(text);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CenterPanel;
    private javax.swing.JPanel FileMetaDataPanel;
    private javax.swing.JPanel LeftPanel;
    private javax.swing.JLabel advancedSearchLabelButton;
    private javax.swing.JPanel audioViewPanel;
    private javax.swing.JCheckBox chatCheckBox;
    private javax.swing.JCheckBox chatContentCheckBox;
    private javax.swing.JLabel clearLabelButton;
    private javax.swing.JPanel documentView;
    private javax.swing.JCheckBox emailCheckBox;
    private javax.swing.JCheckBox emailContentCheckBox;
    private javax.swing.JCheckBox emailHeaderCheckBox;
    private javax.swing.JPanel fileRenderPanel;
    private javax.swing.JCheckBox fileSystemCheckBox;
    private javax.swing.JCheckBox fileSystemContentCheckBox;
    private javax.swing.JCheckBox fileSystemMetadataCheckBox;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel imageViewPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTextArea metaDataTextArea;
    private javax.swing.JTextField queryTextField;
    private javax.swing.JPanel resultPanel;
    private javax.swing.JButton resultSavingButton;
    private javax.swing.JProgressBar searchProgressBard;
    private javax.swing.JPanel searchScopePanel;
    private javax.swing.JTable searchTable;
    private javax.swing.JButton startSearchingButton;
    private javax.swing.JPanel videoViewPanel;
    private javax.swing.JPanel viewPanel;
    // End of variables declaration//GEN-END:variables
}