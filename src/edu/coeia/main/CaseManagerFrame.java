/* 
 * Copyright (C) 2014 Center of Excellence in Information Assurance
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.coeia.main;

/* import internal classes */
import edu.coeia.cases.Case;
import edu.coeia.cases.CaseFacade;
import edu.coeia.cases.management.ApplicationManager;
import edu.coeia.cases.management.CaseOperations;
import edu.coeia.cases.management.CaseOperations.CASE_OPERATION_TYPE;
import edu.coeia.constants.ResourceManager;
import edu.coeia.constants.SystemConstant;
import edu.coeia.gutil.GuiUtil;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.licence.LicenceManager;
import edu.coeia.util.ApplicationLogging;
import edu.coeia.util.DateUtil;
import edu.coeia.wizard.CaseWizardDialog;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/*
 * CaseManagerFrame the main window in DEM
 * 
 * @author Wajdy Essam
 * Created on 16/07/2010, 01:09:17 م
 */
public final class CaseManagerFrame extends javax.swing.JFrame {

    /**
     * Select the License Model Change This line when Produce Different
     * Application with Different License Model BETA_LICENSE will work 60 days
     * Full_LICENSE will require smart card filled with the number of case
     * required
     */
    private static final LicenceManager licenseManager = LicenceManager.FULL_LICENSE; // select beta version
    /**
     * Application Manager Singleton object will create folder structures for
     * the cases and handle the list of all opening case to prevent opening the
     * same case more than one time
     */
    private static final ApplicationManager applicationManager = ApplicationManager.Manager;
    /**
     * Logger Object
     */
    private final static Logger logger = ApplicationLogging.getLogger();

    /**
     * Creates new form CaseManagerFrame
     */
    public CaseManagerFrame() {
        initComponents(); // put swing components
        initJFrame();    // set size and location and title
        setTableModel();    // init table header
        checkBetaLicense(); // check for expiration if beta license
        readCases();     // read cases into table
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        caseManagerDataPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        recentCaseTable = new javax.swing.JTable() { public boolean isCellEditable(int rowIndex, int vColIndex) { return false; } }
        ;
        caseManagerButtonsPanel = new javax.swing.JPanel();
        newCaseButton = new javax.swing.JButton();
        loadCaseButton = new javax.swing.JButton();
        removeCaseButton = new javax.swing.JButton();
        checkLicenseButton = new javax.swing.JButton();
        importCaseButton = new javax.swing.JButton();
        exportCaseButton = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        langaugePanel = new javax.swing.JPanel();
        englishRadioButton = new javax.swing.JRadioButton();
        arabicRadioButton = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("edu/coeia/main/Bundle"); // NOI18N
        setTitle(bundle.getString("CaseManagerFrame.title")); // NOI18N
        setResizable(false);

        caseManagerDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CaseManagerFrame.caseManagerDataPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        recentCaseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        recentCaseTable.setFillsViewportHeight(true);
        recentCaseTable.setGridColor(new java.awt.Color(255, 255, 255));
        recentCaseTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        recentCaseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                recentCaseTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(recentCaseTable);

        javax.swing.GroupLayout caseManagerDataPanelLayout = new javax.swing.GroupLayout(caseManagerDataPanel);
        caseManagerDataPanel.setLayout(caseManagerDataPanelLayout);
        caseManagerDataPanelLayout.setHorizontalGroup(
            caseManagerDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caseManagerDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        caseManagerDataPanelLayout.setVerticalGroup(
            caseManagerDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caseManagerDataPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        caseManagerButtonsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CaseManagerFrame.caseManagerButtonsPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        newCaseButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        newCaseButton.setText(bundle.getString("CaseManagerFrame.newCaseButton.text")); // NOI18N
        newCaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCaseButtonActionPerformed(evt);
            }
        });

        loadCaseButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        loadCaseButton.setText(bundle.getString("CaseManagerFrame.loadCaseButton.text")); // NOI18N
        loadCaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadCaseButtonActionPerformed(evt);
            }
        });

        removeCaseButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        removeCaseButton.setText(bundle.getString("CaseManagerFrame.removeCaseButton.text")); // NOI18N
        removeCaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeCaseButtonActionPerformed(evt);
            }
        });

        checkLicenseButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        checkLicenseButton.setText(bundle.getString("CaseManagerFrame.checkLicenseButton.text")); // NOI18N
        checkLicenseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkLicenseButtonActionPerformed(evt);
            }
        });

        importCaseButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        importCaseButton.setText(bundle.getString("CaseManagerFrame.importCaseButton.text")); // NOI18N
        importCaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importCaseButtonActionPerformed(evt);
            }
        });

        exportCaseButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        exportCaseButton.setText(bundle.getString("CaseManagerFrame.exportCaseButton.text")); // NOI18N
        exportCaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportCaseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout caseManagerButtonsPanelLayout = new javax.swing.GroupLayout(caseManagerButtonsPanel);
        caseManagerButtonsPanel.setLayout(caseManagerButtonsPanelLayout);
        caseManagerButtonsPanelLayout.setHorizontalGroup(
            caseManagerButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caseManagerButtonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(caseManagerButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(newCaseButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadCaseButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(removeCaseButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(importCaseButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(caseManagerButtonsPanelLayout.createSequentialGroup()
                        .addGroup(caseManagerButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(checkLicenseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(exportCaseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        caseManagerButtonsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {checkLicenseButton, exportCaseButton, importCaseButton, loadCaseButton, newCaseButton, removeCaseButton});

        caseManagerButtonsPanelLayout.setVerticalGroup(
            caseManagerButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(caseManagerButtonsPanelLayout.createSequentialGroup()
                .addComponent(newCaseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loadCaseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeCaseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(importCaseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportCaseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkLicenseButton)
                .addContainerGap(96, Short.MAX_VALUE))
        );

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/main/resources/caseManager.jpg"))); // NOI18N
        jToolBar1.add(jLabel2);

        langaugePanel.setBackground(new java.awt.Color(0, 0, 0));

        englishRadioButton.setBackground(new java.awt.Color(0, 0, 0));
        buttonGroup1.add(englishRadioButton);
        englishRadioButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        englishRadioButton.setForeground(new java.awt.Color(255, 255, 255));
        englishRadioButton.setText(bundle.getString("CaseManagerFrame.englishRadioButton.text")); // NOI18N
        englishRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                englishRadioButtonActionPerformed(evt);
            }
        });

        arabicRadioButton.setBackground(new java.awt.Color(0, 0, 0));
        buttonGroup1.add(arabicRadioButton);
        arabicRadioButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        arabicRadioButton.setForeground(new java.awt.Color(255, 255, 255));
        arabicRadioButton.setText(bundle.getString("CaseManagerFrame.arabicRadioButton.text")); // NOI18N
        arabicRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arabicRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout langaugePanelLayout = new javax.swing.GroupLayout(langaugePanel);
        langaugePanel.setLayout(langaugePanelLayout);
        langaugePanelLayout.setHorizontalGroup(
            langaugePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(langaugePanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(englishRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(arabicRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        langaugePanelLayout.setVerticalGroup(
            langaugePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(langaugePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(langaugePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(englishRadioButton)
                    .addComponent(arabicRadioButton)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(caseManagerButtonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(caseManagerDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(langaugePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(caseManagerDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(caseManagerButtonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(langaugePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadCaseButtonActionPerformed
        this.loadCaseAction();
    }//GEN-LAST:event_loadCaseButtonActionPerformed

    private void newCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCaseButtonActionPerformed
        try {
            this.createNewCaseAction();
        } catch (Exception e) {
            logger.severe(String.format("Exception - Cannot Create The Case: %s", e.getMessage()));
        }
    }//GEN-LAST:event_newCaseButtonActionPerformed

    private void removeCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeCaseButtonActionPerformed
        this.removeCaseAction();
    }//GEN-LAST:event_removeCaseButtonActionPerformed

    private void recentCaseTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recentCaseTableMouseClicked
        if (evt.getClickCount() == 2) { // double click
            this.loadCaseAction();
        }
    }//GEN-LAST:event_recentCaseTableMouseClicked

    private void checkLicenseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkLicenseButtonActionPerformed
        this.checkLicenseAction();
    }//GEN-LAST:event_checkLicenseButtonActionPerformed

    private void importCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importCaseButtonActionPerformed
        this.importCaseAction();
    }//GEN-LAST:event_importCaseButtonActionPerformed

    private void exportCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportCaseButtonActionPerformed
        this.exportCaseAction();
    }//GEN-LAST:event_exportCaseButtonActionPerformed

    private void englishRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_englishRadioButtonActionPerformed
        ResourceManager.setLanguage(ResourceManager.Language.ENGLISH);
        changeLanguage(ResourceManager.getLanguage());

    }//GEN-LAST:event_englishRadioButtonActionPerformed

    private void arabicRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arabicRadioButtonActionPerformed
        ResourceManager.setLanguage(ResourceManager.Language.ARABIC);
        changeLanguage(ResourceManager.getLanguage());
    }//GEN-LAST:event_arabicRadioButtonActionPerformed

    private void changeLanguage(Locale locale) {
        Locale.setDefault(locale);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("edu/coeia/main/Bundle", locale); // NOI18N

        setTitle(bundle.getString("CaseManagerFrame.title")); // NOI18N
        caseManagerDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CaseManagerFrame.caseManagerDataPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        caseManagerButtonsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("CaseManagerFrame.caseManagerButtonsPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        newCaseButton.setText(bundle.getString("CaseManagerFrame.newCaseButton.text")); // NOI18N
        loadCaseButton.setText(bundle.getString("CaseManagerFrame.loadCaseButton.text")); // NOI18N
        removeCaseButton.setText(bundle.getString("CaseManagerFrame.removeCaseButton.text")); // NOI18N
        checkLicenseButton.setText(bundle.getString("CaseManagerFrame.checkLicenseButton.text")); // NOI18N
        importCaseButton.setText(bundle.getString("CaseManagerFrame.importCaseButton.text")); // NOI18N
        exportCaseButton.setText(bundle.getString("CaseManagerFrame.exportCaseButton.text")); // NOI18N
        englishRadioButton.setText(bundle.getString("CaseManagerFrame.englishRadioButton.text")); // NOI18N
        arabicRadioButton.setText(bundle.getString("CaseManagerFrame.arabicRadioButton.text")); // NOI18N

        JTableHeader th = recentCaseTable.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        
        tcm.getColumn(0).setHeaderValue(ResourceManager.getText("edu/coeia/main/Bundle", "Case Name"));
        tcm.getColumn(1).setHeaderValue(ResourceManager.getText("edu/coeia/main/Bundle", "Investigator Name"));
        tcm.getColumn(2).setHeaderValue(ResourceManager.getText("edu/coeia/main/Bundle", "Case Creation Date"));
        tcm.getColumn(3).setHeaderValue(ResourceManager.getText("edu/coeia/main/Bundle", "Case Description"));

        th.repaint();        

        this.applyComponentOrientation(ComponentOrientation.getOrientation(locale));
    }

    private void checkLicenseAction() {
        if (licenseManager.isFullVersion()) { // show smart card inserting/usage dialog
            
            //Fix for GPL!
            //SmartCardDialog scd = new SmartCardDialog(this, true, true);
            //scd.setVisible(true);
            
             JOptionPane.showMessageDialog(this,
                    "This program is free software: you can redistribute it and/or modify\n it under the terms of the GNU General Public License as published by\n the Free Software Foundation, either version 3 of the License, or\n (at your option) any later version.\n" +
                    "\n" + "This program is distributed in the hope that it will be useful\n,but WITHOUT ANY WARRANTY; without even the implied warranty of\n MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n GNU General Public License for more details.",
                    "Copyright (C) 2014 - Center of Excellence in Information Assurance",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            int diff = licenseManager.getRemainingDays();
            JOptionPane.showMessageDialog(this,
                    java.text.MessageFormat.format(ResourceManager.getText("edu/coeia/main/Bundle", "REMAINING DAYS"), new Object[]{diff, "Trial Version",}),
                    java.text.MessageFormat.format(ResourceManager.getText("edu/coeia/main/Bundle", "REMAINING DAYS"), new Object[]{diff, "Trial Version",}),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void importCaseAction() {
        CaseOperations operation = new CaseOperations(this, "", CASE_OPERATION_TYPE.IMPORT);
        operation.start();
    }

    private void exportCaseAction() {
        try {
            String caseName = getSelectedCase();
            CaseOperations operation = new CaseOperations(this, caseName, CASE_OPERATION_TYPE.EXPORT);
            operation.start();

        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this,
                    ResourceManager.getText("edu/coeia/main/Bundle", "PLEASE SELECT THE CASE YOU WANT TO OPEN"),
                    ResourceManager.getText("edu/coeia/main/Bundle", "NO CASE IS SELECTED"),
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            logger.severe(String.format(java.util.ResourceBundle.getBundle("edu/coeia/main/Bundle").getString("EXCEPTION - CANNOT EXPORT THE CASE"), e.getMessage()));
        }
    }

    private void loadCaseAction() {
        try {
            String caseName = getSelectedCase();

            CaseOperations operation = new CaseOperations(this, caseName, CASE_OPERATION_TYPE.LOAD);
            operation.start();
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this,
                    ResourceManager.getText("edu/coeia/main/Bundle", "PLEASE SELECT THE CASE YOU WANT TO OPEN"),
                    ResourceManager.getText("edu/coeia/main/Bundle", "NO CASE IS SELECTED"),
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            logger.severe(String.format(java.util.ResourceBundle.getBundle("edu/coeia/main/Bundle").getString("EXCEPTION - CANNOT LOAD THE CASE"), e.getMessage()));

            JOptionPane.showMessageDialog(this,
                    ResourceManager.getText("edu/coeia/main/Bundle", "THE LOCATION FOR THIS INDEX IS NOT FOUNDED, PLEASE RECREATE THE CASE AGAIN"),
                    ResourceManager.getText("edu/coeia/main/Bundle", "INDEX FILE NOT FOUND!"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeCaseAction() {
        try {
            String caseName = getSelectedCase();

            if (!applicationManager.isRunningCase(caseName)) {
                CaseOperations deleteCase = new CaseOperations(this, caseName, CASE_OPERATION_TYPE.REMOVE);
                deleteCase.start();
            } else {
                JOptionPane.showMessageDialog(this,
                        ResourceManager.getText("edu/coeia/main/Bundle", "THIS CASE IS ALREADY OPENING, CLOSE IT FIRST TO REMOVE IT"),
                        ResourceManager.getText("edu/coeia/main/Bundle", "PLEASE CLOSE THE OPENINING CASE"),
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this,
                    ResourceManager.getText("edu/coeia/main/Bundle", "PLEASE SELECT THE CASE YOU WANT TO OPEN"),
                    ResourceManager.getText("edu/coeia/main/Bundle", "NO CASE IS SELECTED"),
                    JOptionPane.INFORMATION_MESSAGE);

            return;
        }
    }

    private void createNewCaseAction() throws Exception {
        CaseWizardDialog indexWizard = new CaseWizardDialog(CaseManagerFrame.this, true);
        indexWizard.setVisible(true);

        CaseFacade facade = indexWizard.getCaseFacade();
        if (facade == null) {
            logger.info("Create New Case Cancled");
            return;
        }

        logger.info("Create New Case Don Successfully");

        readCases(); // update recent table with this new information

        if (indexWizard.checkDirectIndex()) {
            try {
                CaseOperations operation = new CaseOperations(this, facade.getCase().getCaseName(), CASE_OPERATION_TYPE.LOAD_AND_INDEX);
                operation.start();
            } catch (Exception e) {
                logger.severe("Cannot Index the case directly after create it");
                logger.severe(String.format("Exception - Cannot Load the Case After Creation: %s", e.getMessage()));
            }
        }
    }

    /**
     * checkBetaLicense Check the Time for BETA_LICENSE if expired then exit
     * from the application and show user friendly message
     */
    private void checkBetaLicense() {
        if (!licenseManager.isFullVersion() && licenseManager.isExpireNow()) {
            JOptionPane.showMessageDialog(this,
                    ResourceManager.getText("edu/coeia/main/Bundle", "YOUR SOFTWARE HAS BEEN EXPIRED!"),
                    ResourceManager.getText("edu/coeia/main/Bundle", "PLEASE PURCHASE THE FULL VERSION..."),
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Screen Initializing set JFrame look and feel, set JFrame location and set
     * JFrame title Save Application time when close JFrame (window close event)
     */
    private void initJFrame() {
        /**
         * set look and feel to windows look
         */
        GuiUtil.changeLookAndFeel(SystemConstant.WINDOWS_LOOK_AND_FEEL, this);

        /**
         * set application in middle of screen *
         */
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        // set default application language
        ResourceManager.setLanguage(ResourceManager.Language.ENGLISH);
        this.englishRadioButton.setSelected(true);

        // set application title and default location when startup
        this.setLocation(width / 4, height / 4);
        this.setTitle(ResourceManager.getText("edu/coeia/main/Bundle", "DIGITAL EVIDENCE MINER: CASE MANAGER WINDOW"));

        // add close event
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                licenseManager.saveUsage();
            }
        });
    }

    private void setTableModel() {
        recentCaseTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    ResourceManager.getText("edu/coeia/main/Bundle", "Case Name"),
                    ResourceManager.getText("edu/coeia/main/Bundle", "Investigator Name"),
                    ResourceManager.getText("edu/coeia/main/Bundle", "Case Creation Date"),
                    ResourceManager.getText("edu/coeia/main/Bundle", "Case Description") 
                }) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });                
    }

    /*
     * Read cases from files and update recent table
     * this mehtod also be called from importer thread after import new case
     */
    public void readCases() {
        JTableUtil.removeAllRows(this.recentCaseTable);

        // read cases into case JTable
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Case> cases = ApplicationManager.Manager.getCases();

                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            for (Case aCase : cases) {
                                insertIntoCaseTable(aCase);
                            }
                        }
                    });
                } catch (Exception ex) {
                    logger.severe("Cannot reading the list of cases");
                }
            }
        }).start();
    }

    /*
     * Add index information to recent table (used when update recent)
     */
    private void insertIntoCaseTable(Case index) {
        Object[] object = {
            index.getCaseName(), index.getInvestigatorName(),
            DateUtil.formatDateTime(index.getCreateTime()), index.getDescription(),};

        JTableUtil.addRowToJTable(this.recentCaseTable, object);
    }

    private String getSelectedCase() {
        int row = recentCaseTable.getSelectedRow();

        if (row < 0) {
            throw new NullPointerException("Case is Not Selected");
        }

        String indexName = (String) recentCaseTable.getValueAt(row, 0);
        return indexName;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton arabicRadioButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel caseManagerButtonsPanel;
    private javax.swing.JPanel caseManagerDataPanel;
    private javax.swing.JButton checkLicenseButton;
    private javax.swing.JRadioButton englishRadioButton;
    private javax.swing.JButton exportCaseButton;
    private javax.swing.JButton importCaseButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel langaugePanel;
    private javax.swing.JButton loadCaseButton;
    private javax.swing.JButton newCaseButton;
    private javax.swing.JTable recentCaseTable;
    private javax.swing.JButton removeCaseButton;
    // End of variables declaration//GEN-END:variables
}
