/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CaseInformationPanel.java
 *
 * Created on Oct 5, 2011, 11:07:42 AM
 */
package edu.coeia.filesystem;

import edu.coeia.cases.CaseHistory;
import edu.coeia.cases.CaseFacade;
import edu.coeia.tags.TagsDialog;
import edu.coeia.main.CaseFrame;
import edu.coeia.tags.TagsManager;
import edu.coeia.tags.Tag ;
import edu.coeia.hash.HashVerifier;
import edu.coeia.util.DateUtil;

import java.awt.Toolkit;

import java.util.List ;

import javax.swing.JOptionPane;

/**
 *
 * @author wajdyessam
 */
public final class CaseInformationPanel extends javax.swing.JPanel {

    private final CaseFrame parent ;
    private final TagsManager tagsManager ;
    private final CaseFacade caseManager ;
    
    private int currentTagIndex;
    
    /** Creates new form CaseInformationPanel */
    public CaseInformationPanel(CaseFrame frame) {
        initComponents();
        
        this.parent = frame;
        this.tagsManager = frame.getTagsManager() ;
        this.caseManager = frame.getCaseManager();
                
        // initializing tags panel
        this.initializingTagsPanel();
        
        // update case information panel
        this.displayCaseInformationPanel();
        this.displayMutableCaseInformationPanel();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        caseInformationPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        caseNameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        createdDateTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lastModifiedTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        casePathTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        indexedTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        itemIndexedTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        caseDescriptionTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        createdByTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        caseSizeTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        caseSourcesTextView = new javax.swing.JTextArea();
        tagsLogsPanel = new javax.swing.JPanel();
        caseLogsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        caseLogsTable = new javax.swing.JTable();
        caseTagsPanel = new javax.swing.JPanel();
        caseTagsViewerPanel = new javax.swing.JPanel();
        caseTagVeiwerPanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        tagNameTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        tagDateTextField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tagContentTextArea = new javax.swing.JTextArea();
        caseTagControllerPanel = new javax.swing.JPanel();
        nextButton = new javax.swing.JButton();
        prevButton = new javax.swing.JButton();
        currentTagLabel = new javax.swing.JLabel();
        caseTagsButtonsPanel = new javax.swing.JPanel();
        newTagsButton = new javax.swing.JButton();
        removeTagsButton = new javax.swing.JButton();
        caseControllerPanel = new javax.swing.JPanel();
        caseHashVerifyPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        caseHashValueTextField = new javax.swing.JTextField();
        verifyButton = new javax.swing.JButton();
        caseSavePanel = new javax.swing.JPanel();
        saveCaseButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        caseInformationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Case Information"));

        jLabel1.setText("Case Name:");

        caseNameTextField.setEditable(false);
        caseNameTextField.setText(" ");

        jLabel2.setText("Created Date:");

        createdDateTextField.setEditable(false);
        createdDateTextField.setText(" ");

        jLabel3.setText("Last Modified:");

        lastModifiedTextField.setEditable(false);
        lastModifiedTextField.setText(" ");

        jLabel4.setText("Case Path:");

        casePathTextField.setEditable(false);
        casePathTextField.setText(" ");

        jLabel5.setText("Indexed:");

        indexedTextField.setEditable(false);
        indexedTextField.setText(" ");

        jLabel6.setText("#of item indexed:");

        itemIndexedTextField.setEditable(false);
        itemIndexedTextField.setText(" ");

        jLabel7.setText("Case Description:");

        caseDescriptionTextField.setEditable(false);
        caseDescriptionTextField.setText(" ");

        jLabel8.setText("Created By:");

        createdByTextField.setEditable(false);
        createdByTextField.setText(" ");

        jLabel10.setText("Case Size:");

        caseSizeTextField.setEditable(false);
        caseSizeTextField.setText(" ");

        jLabel11.setText("Case Sources:");

        caseSourcesTextView.setColumns(20);
        caseSourcesTextView.setEditable(false);
        caseSourcesTextView.setFont(new java.awt.Font("Courier New", 0, 12));
        caseSourcesTextView.setRows(5);
        jScrollPane2.setViewportView(caseSourcesTextView);

        javax.swing.GroupLayout caseInformationPanelLayout = new javax.swing.GroupLayout(caseInformationPanel);
        caseInformationPanel.setLayout(caseInformationPanelLayout);
        caseInformationPanelLayout.setHorizontalGroup(
            caseInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caseInformationPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(caseInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(caseInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(caseSizeTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(caseDescriptionTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(itemIndexedTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(indexedTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(casePathTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(lastModifiedTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(createdDateTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(createdByTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(caseNameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        caseInformationPanelLayout.setVerticalGroup(
            caseInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caseInformationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(caseInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(caseNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(caseInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(createdByTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(caseInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(createdDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(caseInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lastModifiedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(caseInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(casePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(caseInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(indexedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(caseInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(itemIndexedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(caseInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(caseDescriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(caseInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(caseSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(caseInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(caseInformationPanelLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(53, 53, 53))
                    .addGroup(caseInformationPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        add(caseInformationPanel, java.awt.BorderLayout.WEST);

        tagsLogsPanel.setLayout(new java.awt.BorderLayout());

        caseLogsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Case Log(s)"));
        caseLogsPanel.setLayout(new java.awt.BorderLayout());

        caseLogsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Date", "By", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        caseLogsTable.setFillsViewportHeight(true);
        caseLogsTable.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(caseLogsTable);

        caseLogsPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        tagsLogsPanel.add(caseLogsPanel, java.awt.BorderLayout.CENTER);

        caseTagsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Case Tag(s)"));
        caseTagsPanel.setLayout(new java.awt.BorderLayout());

        caseTagsViewerPanel.setLayout(new java.awt.BorderLayout());

        jLabel12.setText("By:");

        tagNameTextField.setEditable(false);
        tagNameTextField.setText(" ");

        jLabel13.setText("Date:");

        tagDateTextField.setEditable(false);
        tagDateTextField.setText(" ");

        jLabel14.setText("Content:");

        tagContentTextArea.setColumns(20);
        tagContentTextArea.setEditable(false);
        tagContentTextArea.setFont(new java.awt.Font("Courier New", 0, 14));
        tagContentTextArea.setRows(5);
        jScrollPane3.setViewportView(tagContentTextArea);

        caseTagControllerPanel.setLayout(new java.awt.BorderLayout());

        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        caseTagControllerPanel.add(nextButton, java.awt.BorderLayout.CENTER);

        prevButton.setText("prev");
        prevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevButtonActionPerformed(evt);
            }
        });
        caseTagControllerPanel.add(prevButton, java.awt.BorderLayout.WEST);

        currentTagLabel.setText("1/N");

        javax.swing.GroupLayout caseTagVeiwerPanelLayout = new javax.swing.GroupLayout(caseTagVeiwerPanel);
        caseTagVeiwerPanel.setLayout(caseTagVeiwerPanelLayout);
        caseTagVeiwerPanelLayout.setHorizontalGroup(
            caseTagVeiwerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caseTagVeiwerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(caseTagVeiwerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(caseTagVeiwerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(caseTagVeiwerPanelLayout.createSequentialGroup()
                        .addComponent(tagNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tagDateTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                    .addGroup(caseTagVeiwerPanelLayout.createSequentialGroup()
                        .addComponent(caseTagControllerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                        .addComponent(currentTagLabel)))
                .addContainerGap())
        );
        caseTagVeiwerPanelLayout.setVerticalGroup(
            caseTagVeiwerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caseTagVeiwerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(caseTagVeiwerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tagNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(tagDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(caseTagVeiwerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(caseTagVeiwerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(currentTagLabel)
                    .addComponent(caseTagControllerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        caseTagsViewerPanel.add(caseTagVeiwerPanel, java.awt.BorderLayout.PAGE_START);

        caseTagsPanel.add(caseTagsViewerPanel, java.awt.BorderLayout.CENTER);

        caseTagsButtonsPanel.setPreferredSize(new java.awt.Dimension(100, 100));

        newTagsButton.setText("New");
        newTagsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newTagsButtonActionPerformed(evt);
            }
        });

        removeTagsButton.setText("Remove");
        removeTagsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeTagsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout caseTagsButtonsPanelLayout = new javax.swing.GroupLayout(caseTagsButtonsPanel);
        caseTagsButtonsPanel.setLayout(caseTagsButtonsPanelLayout);
        caseTagsButtonsPanelLayout.setHorizontalGroup(
            caseTagsButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caseTagsButtonsPanelLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(caseTagsButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(newTagsButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeTagsButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        caseTagsButtonsPanelLayout.setVerticalGroup(
            caseTagsButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caseTagsButtonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newTagsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(removeTagsButton)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        caseTagsPanel.add(caseTagsButtonsPanel, java.awt.BorderLayout.EAST);

        tagsLogsPanel.add(caseTagsPanel, java.awt.BorderLayout.PAGE_START);

        add(tagsLogsPanel, java.awt.BorderLayout.CENTER);

        caseControllerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Case Controller"));
        caseControllerPanel.setLayout(new java.awt.BorderLayout());

        jLabel9.setText("Case Hash Value:");

        caseHashValueTextField.setText(" ");

        verifyButton.setText("Verifey");
        verifyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verifyButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout caseHashVerifyPanelLayout = new javax.swing.GroupLayout(caseHashVerifyPanel);
        caseHashVerifyPanel.setLayout(caseHashVerifyPanelLayout);
        caseHashVerifyPanelLayout.setHorizontalGroup(
            caseHashVerifyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caseHashVerifyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(caseHashValueTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(verifyButton)
                .addContainerGap())
        );
        caseHashVerifyPanelLayout.setVerticalGroup(
            caseHashVerifyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caseHashVerifyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(caseHashVerifyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(caseHashValueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(verifyButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        caseControllerPanel.add(caseHashVerifyPanel, java.awt.BorderLayout.CENTER);

        saveCaseButton.setText("Save  Case");
        saveCaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveCaseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout caseSavePanelLayout = new javax.swing.GroupLayout(caseSavePanel);
        caseSavePanel.setLayout(caseSavePanelLayout);
        caseSavePanelLayout.setHorizontalGroup(
            caseSavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, caseSavePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveCaseButton, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                .addContainerGap())
        );
        caseSavePanelLayout.setVerticalGroup(
            caseSavePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, caseSavePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saveCaseButton)
                .addContainerGap())
        );

        caseControllerPanel.add(caseSavePanel, java.awt.BorderLayout.EAST);

        add(caseControllerPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void newTagsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTagsButtonActionPerformed
        TagsDialog tagDialog = new TagsDialog(this.parent,true);
        tagDialog.setVisible(true);
        
        Tag tag = tagDialog.getTag();
        this.addTag(tag);
    }//GEN-LAST:event_newTagsButtonActionPerformed

    private void removeTagsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeTagsButtonActionPerformed
        this.removeTag();
    }//GEN-LAST:event_removeTagsButtonActionPerformed

    private void verifyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verifyButtonActionPerformed
        String hash = this.caseHashValueTextField.getText().trim();
        if ( !hash.isEmpty() )
            HashVerifier.newInstance(this.parent, hash, this.caseManager.getCase().getEvidenceSourceLocation().get(0)).start();
        else
            JOptionPane.showMessageDialog(this, "Please enter the original hash value",
                    "Missing Hash Value", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_verifyButtonActionPerformed

    private void saveCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveCaseButtonActionPerformed
        saveCaseModifications();
    }//GEN-LAST:event_saveCaseButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        showNextTag();
    }//GEN-LAST:event_nextButtonActionPerformed

    private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevButtonActionPerformed
        showPreviousTag();
    }//GEN-LAST:event_prevButtonActionPerformed

    /**
     * Save case modifications
     */
    public void saveCaseModifications() {
        if ( this.tagsManager.saveTags() ) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this, "Case have been saved", 
                    "Saving Case Message", JOptionPane.INFORMATION_MESSAGE);
            
            // update current
            this.tagsManager.updateMonitorChangingList();
        }
    }
    
    /**
     * go back to previous tag, and show it
     */
    public void showPreviousTag() {
        if ( this.currentTagIndex > 0 ) {
            this.currentTagIndex--;
            displayCurrentTag(currentTagIndex);
        }
    }
    
    /**
     * show the next tag if there is a tag
     */
    public void showNextTag() {
        if ( this.currentTagIndex < this.tagsManager.getTags().size() ) {
            this.currentTagIndex++;
            displayCurrentTag(currentTagIndex);
        }        
    }
    
    /**
     * add new tag, then update the tags indexer
     */
    private void addTag(final Tag tag) {
        if ( tag != null ) {
            this.tagsManager.addTag(tag);
            int index = this.tagsManager.getTags().indexOf(tag);
            this.currentTagIndex = index;
            displayCurrentTag(currentTagIndex);
        }
    }
    
    /**
     * remove current tag
     */
    private void removeTag() {
        this.tagsManager.removeTag(this.currentTagIndex);
        this.initializingTagsPanel();
    }
    
    /**
     * initializing tags panel 
     */
    public void initializingTagsPanel() {
        List<Tag> tags = this.tagsManager.getTags();
        this.currentTagIndex = 0;
        
        if ( !tags.isEmpty() )
            this.displayTagElement(tags.get(this.currentTagIndex));
        else
            this.resetTagsElements();
        
        this.currentTagLabel.setText("(" + this.currentTagIndex + "/" + tags.size() + ")");
        this.checkingButtons();
    }
    
    /**
     * display tag specified by current tag index
     */
    private void displayCurrentTag(final int index) {
        List<Tag> tags = this.tagsManager.getTags();
        Tag tag = tags.get(index);
        displayTagElement(tag);
        this.currentTagLabel.setText("(" + index + "/" + tags.size() + ")");
        this.checkingButtons();
    }
    
    /*
     * display tag in swing ui elements, and then revalidate the frame
     */
    private void displayTagElement(final Tag tag) {
        assert tag != null; 
        this.tagNameTextField.setText(tag.getName());
        this.tagDateTextField.setText(DateUtil.formatDate(tag.getDate()));
        this.tagContentTextArea.setText(tag.getMessage());  
        this.revalidate();
    }
    
    /**
     * Resetting Tags Element to empty string
     * this because when delete last tag from list
     * we should clean the ui elements
     */
    private void resetTagsElements() {
        this.tagNameTextField.setText("");
        this.tagDateTextField.setText("");
        this.tagContentTextArea.setText("");
        this.currentTagLabel.setText("(" + 0 + "/" + this.tagsManager.getTags().size() + ")");
    }
    
    /**
     * checking buttons on frame, to disable it if no data
     */
    private void checkingButtons() {
        /**
         * disable remove button when their is not tags
         */
        if ( this.tagsManager.getTags().isEmpty() )
            this.removeTagsButton.setEnabled(false);
        else
            this.removeTagsButton.setEnabled(true);
        
        /**
         * disable previous button
         */
        if ( this.currentTagIndex == 0 )
            this.prevButton.setEnabled(false);
        else
            this.prevButton.setEnabled(true);
        
        /**
         * disable next button
         */
        if ( this.currentTagIndex < this.tagsManager.getTags().size()-1)
            this.nextButton.setEnabled(true);
        else
            this.nextButton.setEnabled(false);
    }
    
    /**
     * display case information in the related panel
     */
    public void displayCaseInformationPanel() {
        this.caseNameTextField.setText(this.caseManager.getCase().getCaseName());
        this.createdDateTextField.setText(DateUtil.formatDate(this.caseManager.getCase().getCreateTime()));
        this.createdByTextField.setText(this.caseManager.getCase().getInvestigatorName());
        this.casePathTextField.setText(this.caseManager.getCase().getCaseLocation());
        this.caseDescriptionTextField.setText(this.caseManager.getCase().getDescription());
        
        StringBuilder paths = new StringBuilder();
        for(String doc: this.caseManager.getCase().getEvidenceSourceLocation())
            paths.append(doc).append("\n");
        
        this.caseSourcesTextView.setText(paths.toString());   // clear the field and append new data
    }
    
    /**
     * Display information on case panel
     * this information will be changed after each indexing
     */
    public void displayMutableCaseInformationPanel() {
        CaseHistory history = this.caseManager.getCaseHistory();
        
        this.lastModifiedTextField.setText(history.getLastModified());
        this.indexedTextField.setText(String.valueOf(history.getIsCaseIndexed()));
        this.itemIndexedTextField.setText(String.valueOf(history.getNumberOfItemsIndexed()));
        this.caseSizeTextField.setText(String.valueOf(history.getCaseSize()));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel caseControllerPanel;
    private javax.swing.JTextField caseDescriptionTextField;
    private javax.swing.JTextField caseHashValueTextField;
    private javax.swing.JPanel caseHashVerifyPanel;
    private javax.swing.JPanel caseInformationPanel;
    private javax.swing.JPanel caseLogsPanel;
    private javax.swing.JTable caseLogsTable;
    private javax.swing.JTextField caseNameTextField;
    private javax.swing.JTextField casePathTextField;
    private javax.swing.JPanel caseSavePanel;
    private javax.swing.JTextField caseSizeTextField;
    private javax.swing.JTextArea caseSourcesTextView;
    private javax.swing.JPanel caseTagControllerPanel;
    private javax.swing.JPanel caseTagVeiwerPanel;
    private javax.swing.JPanel caseTagsButtonsPanel;
    private javax.swing.JPanel caseTagsPanel;
    private javax.swing.JPanel caseTagsViewerPanel;
    private javax.swing.JTextField createdByTextField;
    private javax.swing.JTextField createdDateTextField;
    private javax.swing.JLabel currentTagLabel;
    private javax.swing.JTextField indexedTextField;
    private javax.swing.JTextField itemIndexedTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField lastModifiedTextField;
    private javax.swing.JButton newTagsButton;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton prevButton;
    private javax.swing.JButton removeTagsButton;
    private javax.swing.JButton saveCaseButton;
    private javax.swing.JTextArea tagContentTextArea;
    private javax.swing.JTextField tagDateTextField;
    private javax.swing.JTextField tagNameTextField;
    private javax.swing.JPanel tagsLogsPanel;
    private javax.swing.JButton verifyButton;
    // End of variables declaration//GEN-END:variables
}