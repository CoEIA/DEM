package edu.coeia.gui;


/* import internal classes */
import edu.coeia.utility.FilesPath ;
import edu.coeia.utility.Utilities;
import edu.coeia.cases.Case;
import edu.coeia.license.LicenseManager;
import edu.coeia.cases.CaseManager ;

/* import sun classes */
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager ;
import javax.swing.SwingUtilities ;
import javax.swing.JTable ;
import javax.swing.table.DefaultTableModel ;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter ;
import javax.swing.JOptionPane ;

import java.io.IOException ;
import java.io.File ;
import java.io.FileWriter ;
import java.io.PrintWriter ;
import java.io.FileNotFoundException ;

import java.util.ArrayList ;

import java.awt.Toolkit ;
import java.awt.Dimension ;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/* import Third Party Libraries */
import chrriis.dj.nativeswing.swtimpl.NativeInterface;

/*
 * CaseManagerFrame the main entry point to DEM
 * @author Wajdy Essam
 * Created on 16/07/2010, 01:09:17 م
 */

public class CaseManagerFrame extends javax.swing.JFrame {
   
    /**
     *  Select the License Model
     *  Change This line when Produce Different Application with Different License Model
     *  BETA_LICENSE will work 60 days
     *  Full_LICENSE will require smart card filled with the number of case required
     */
    private static final LicenseManager licenseManager = LicenseManager.BETA_LICENSE; // select beta version
    
    
    /**
     * Case Manager Object
     * will create cases folder if no folder exists or some files in this folder are missing
     * and handle the list of all opening case to prevent opening the same case more than one time
     */
    private static final CaseManager caseManager = CaseManager.Manager ;
    
    
    /** Creates new form CaseManagerFrame */
    public CaseManagerFrame() {
        initComponents(); // put swing components
        initJFrame();    // set size and location and title
        checkBetaLicense(); // check for expiration if beta license
        readCases();     // read cases into table
    }
   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        caseManagerDataPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        recentCaseTable = new javax.swing.JTable() { public boolean isCellEditable(int rowIndex, int vColIndex) { return false; } }
        ;
        jScrollPane2 = new javax.swing.JScrollPane();
        caseInformationTable = new javax.swing.JTable(){ public boolean isCellEditable(int rowIndex, int vColIndex) { return false; } };
        caseManagerButtonsPanel = new javax.swing.JPanel();
        newCaseButton = new javax.swing.JButton();
        loadCaseButton = new javax.swing.JButton();
        removeCaseButton = new javax.swing.JButton();
        checkLicenseButton = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Digital Evidence Miner: Case Manager");
        setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/edu/coeia/gui/resources/dem-icon.png")));
        setResizable(false);

        caseManagerDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Recent Cases Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        recentCaseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Case Name","Investigator Name","Case Date","Case Description",
                "Case Indexed"
            }

        )
    );
    recentCaseTable.setFillsViewportHeight(true);
    recentCaseTable.setGridColor(new java.awt.Color(255, 255, 255));
    recentCaseTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    recentCaseTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            recentCaseTableMouseClicked(evt);
        }
    });
    jScrollPane1.setViewportView(recentCaseTable);

    caseInformationTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "Property" , "Value"
        }
    ));
    jScrollPane2.setViewportView(caseInformationTable);

    javax.swing.GroupLayout caseManagerDataPanelLayout = new javax.swing.GroupLayout(caseManagerDataPanel);
    caseManagerDataPanel.setLayout(caseManagerDataPanelLayout);
    caseManagerDataPanelLayout.setHorizontalGroup(
        caseManagerDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, caseManagerDataPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(caseManagerDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE))
            .addContainerGap())
    );
    caseManagerDataPanelLayout.setVerticalGroup(
        caseManagerDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(caseManagerDataPanelLayout.createSequentialGroup()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
    );

    caseManagerButtonsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Case Operations", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

    newCaseButton.setFont(new java.awt.Font("Tahoma", 1, 11));
    newCaseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/1295181153_manilla-folder-new.png"))); // NOI18N
    newCaseButton.setText("New Case");
    newCaseButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            newCaseButtonActionPerformed(evt);
        }
    });

    loadCaseButton.setFont(new java.awt.Font("Tahoma", 1, 11));
    loadCaseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/open_case.png"))); // NOI18N
    loadCaseButton.setText("Load & Open Case");
    loadCaseButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            loadCaseButtonActionPerformed(evt);
        }
    });

    removeCaseButton.setFont(new java.awt.Font("Tahoma", 1, 11));
    removeCaseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/remove_case.png"))); // NOI18N
    removeCaseButton.setText("Remove Case");
    removeCaseButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            removeCaseButtonActionPerformed(evt);
        }
    });

    checkLicenseButton.setFont(new java.awt.Font("Tahoma", 1, 11));
    checkLicenseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/1295179145_User Card.png"))); // NOI18N
    checkLicenseButton.setText("Check License");
    checkLicenseButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            checkLicenseButtonActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout caseManagerButtonsPanelLayout = new javax.swing.GroupLayout(caseManagerButtonsPanel);
    caseManagerButtonsPanel.setLayout(caseManagerButtonsPanelLayout);
    caseManagerButtonsPanelLayout.setHorizontalGroup(
        caseManagerButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(caseManagerButtonsPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(caseManagerButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(newCaseButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addComponent(checkLicenseButton, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addComponent(removeCaseButton, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addComponent(loadCaseButton, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
            .addContainerGap())
    );
    caseManagerButtonsPanelLayout.setVerticalGroup(
        caseManagerButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(caseManagerButtonsPanelLayout.createSequentialGroup()
            .addComponent(newCaseButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(loadCaseButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
            .addComponent(removeCaseButton)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(checkLicenseButton))
    );

    jToolBar1.setFloatable(false);
    jToolBar1.setRollover(true);

    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(153, 153, 153));
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/caseManager.jpg"))); // NOI18N
    jToolBar1.add(jLabel2);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(caseManagerDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(caseManagerButtonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(caseManagerDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(caseManagerButtonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadCaseButtonActionPerformed
        try {
            String indexName = getSelectedCase();
            loadCase(indexName);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "the location for this index is not founded, please recreate the case again", "Index File not Found!",
                JOptionPane.ERROR_MESSAGE);
        }
        catch (ClassNotFoundException e){
        }
    }//GEN-LAST:event_loadCaseButtonActionPerformed

    private void newCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCaseButtonActionPerformed
        try {
            CaseWizardDialog indexWizard = new CaseWizardDialog(CaseManagerFrame.this,true, licenseManager.isFullVersion());
            indexWizard.setVisible(true);
            
            Case index = indexWizard.getIndex();
            if ( index == null) {
                return ;
            }
            
            updateIndexesInfoFile(index); // update indexes info file with new index
            readCases(); // update recent table with this new information
        }
        catch (IOException e){
        }
    }//GEN-LAST:event_newCaseButtonActionPerformed

    private void removeCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeCaseButtonActionPerformed
       int row = recentCaseTable.getSelectedRow();

        if ( row < 0 ) {
            JOptionPane.showMessageDialog(this, "please select the case you want to remove",
                "No Case is Selected", JOptionPane.INFORMATION_MESSAGE);
            return ;
        }

        String indexName = (String) recentCaseTable.getValueAt(row, 0);
        removeCase(indexName);
        
        readCases(); // update view table
        removeAllRows(caseInformationTable); // remove entrie in case information table
    }//GEN-LAST:event_removeCaseButtonActionPerformed

    private void recentCaseTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recentCaseTableMouseClicked
        if ( evt.getClickCount() == 2 ) { // double click
            try {
                showCaseInformation();
                String caseName = getSelectedCase();
                loadCase(caseName);
            }
            catch (IOException e){
                JOptionPane.showMessageDialog(this, "the location for this index is not founded, please recreate the case again", "Index File not Found!",
                    JOptionPane.ERROR_MESSAGE);
            }
            catch (ClassNotFoundException e){
            }
        }
        else { // one click , then show information
            showCaseInformation();
        }
    }//GEN-LAST:event_recentCaseTableMouseClicked

    private void checkLicenseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkLicenseButtonActionPerformed
        if ( licenseManager.isFullVersion() ) { // show smart card inserting/usage dialog
            SmartCardDialog scd = new SmartCardDialog(this, true, true);
            scd.setVisible(true);
        }
        else {
            int diff = licenseManager.getRemainingDays();
            JOptionPane.showMessageDialog(this, "Remaining days: " + diff, "Trial Version",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_checkLicenseButtonActionPerformed

    
    /**
     * checkBetaLicense
     * Check the Time for BETA_LICENSE
     * if expired then exit from the application and show user friendly message
     */
    private void checkBetaLicense() {
        if ( ! licenseManager.isFullVersion() && licenseManager.isExpireNow() ) {
            JOptionPane.showMessageDialog(this, "Your software has been expired!","please purchase the full version...",JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    /**
     *  Screen Initializing
     *  set JFrame look and feel, set JFrame location and set JFrame title
     *  Save Application time when close JFrame (window close event)
     */
    private void initJFrame() {
        try { 
            changeLookAndFeel(lookAndFeelName);  // set look and feel to windows look 
        }
        catch (Exception e){
        }

        /** set application in middle of screen **/
        Toolkit kit = Toolkit.getDefaultToolkit() ;
        Dimension screenSize = kit.getScreenSize() ;
        int width = screenSize.width ;
        int height = screenSize.height;

        // set application title and default location when startup
        this.setLocation( width / 4, height / 4);
        this.setTitle("Digital Evidence Miner (Beta Version): Case Manager Window");

        // add close event
        this.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent event){
                licenseManager.saveUsage();
            }
        });
    }
    
    /*
     * Change the look and Feel
     */
    private void changeLookAndFeel ( String lookName ) throws Exception  {
        UIManager.setLookAndFeel(lookName);
        SwingUtilities.updateComponentTreeUI(this);
    }

    /*
     * Read cases from files and update recent table
     */
    private void readCases() {
        try {
            updateRecentTable(); // read cases into case JTable 
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CaseManagerFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CaseManagerFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CaseManagerFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
     * Update recent table by reading cases (reading from indexes_info file) info table
     */
    private void updateRecentTable () throws FileNotFoundException, IOException, ClassNotFoundException {
        File indexesInfo = new File(FilesPath.INDEXES_INFO);
        ArrayList<String> indexesInfoContent  = Utilities.getFileContentInArrayList(indexesInfo);

        // clear value on table before adding new values
        removeAllRows(recentCaseTable);

        for(String path: indexesInfoContent) {
            Case index = Case.getCase(path);
            addIndexInformationToTable(index);
        }
    }
    
    /*
     * Add index information to recent table (used when update recent)
     */
    private void addIndexInformationToTable (Case index) {
        DefaultTableModel model = (DefaultTableModel) recentCaseTable.getModel();
        model.addRow( new Object[] {index.getIndexName(), index.getInvestigatorName(), Utilities.formatDateTime(index.getCreateTime()), index.getDescription(),
            index.getIndexStatus() });
    }
    
    /*
     * Show case information down in case manager
     */
    private void showCaseInformation () {
        int row = recentCaseTable.getSelectedRow();

        if ( row < 0 )
            return ;

        String indexName = (String) recentCaseTable.getValueAt(row, 0);

        // clear value on table before adding new values
        removeAllRows(caseInformationTable);

        try {
            Case index = getIndexInformationFromIndexName(indexName);
            DefaultTableModel model = (DefaultTableModel) caseInformationTable.getModel();

            model.addRow( new Object[] { "Index Name" , index.getIndexName() });
            model.addRow( new Object[] { "Index Path" , index.getIndexLocation() });
            model.addRow( new Object[] { "Created Date and Time" , Utilities.formatDateTime(index.getCreateTime()) } );
            model.addRow( new Object[] { "Data Indexed Size" , Utilities.formatSize(Utilities.toKB(index.getDataIndexedSize())) + " KB"  });
            model.addRow( new Object[] { "Extensions Allowed" , index.getExtensionAllowed() });

            addListToRow(index.getDocumentInIndex(),"Case In Index",model);
            addListToRow(index.getPstPath(),"Outlook Documents in Index", model);
            addListToRow(index.getIePath(),"IE Paths in Index", model);
            addListToRow(index.getFFPath(), "FF Paths in Index", model);
            addListToRow(index.getMsnPath(),"MSN Paths in Index", model);
            addListToRow(index.getYahooPath(),"Yahoo Paths in Index", model);
            addListToRow(index.getSkypePath(), "Skype Paths in Index", model);

            model.addRow( new Object[] { "Cache All Images" , index.getCacheImages() });
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "the location for this index is not founded, please recreate the case again", "Index File not Found!",
                JOptionPane.ERROR_MESSAGE);
        }
        catch (ClassNotFoundException e){
        }
    }
        
    // add value from list to case information table
    private void addListToRow (ArrayList<String> data, String text, DefaultTableModel model) {
        for (String s: data ) {
            model.addRow( new Object[] { text , s });
            text = "" ;
        }
    }
    
    private String getSelectedCase () {
        int row = recentCaseTable.getSelectedRow();

        if ( row < 0 ) {
            JOptionPane.showMessageDialog(this, "please select the case you want to open",
                    "No Case is Selected", JOptionPane.INFORMATION_MESSAGE);
            return null ;
        }

        String indexName = (String) recentCaseTable.getValueAt(row, 0);
        return indexName; 
    }

    private void loadCase (String caseName ) throws FileNotFoundException, IOException, ClassNotFoundException{
        if ( caseName != null ) {
            if ( !caseManager.isContain(caseName)) {
                Case index = getIndexInformationFromIndexName(caseName);

                caseManager.addCase(caseName);

                OfflineMinningFrame mainFrame = new OfflineMinningFrame(index,false, index.getIndexName() + " Case",
                        caseManager.getList());
                mainFrame.setVisible(true);
            }
            else {
                JOptionPane.showMessageDialog(this, "This case is already opening",
                        "Already Openining Case", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void removeCase (String caseName) {
        try {
            Case index = getIndexInformationFromIndexName(caseName);
            File file = new File( index.getIndexLocation() );
            
            if ( Utilities.removeDirectory(file) ) {
                ArrayList<String> indexPtr = Utilities.getFileContentInArrayList(new File(FilesPath.INDEXES_INFO) );
                ArrayList<String> newIndexPtr = new ArrayList<String>();
                
                for (String line: indexPtr) {
                    String name = line.split("-")[0].trim();
                    String path = line.split("-")[1].trim();

                    if ( name.equals(index.getIndexName()) && path.equals(index.getIndexLocation()))
                        continue ;

                    newIndexPtr.add(line);
                }

                // write new index information to file
                Utilities.writeToFile(newIndexPtr, FilesPath.INDEXES_INFO);
            }
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "the location for this index is not founded, please recreate the case again", "Index File not Found!",
                JOptionPane.ERROR_MESSAGE);
        }
        catch (ClassNotFoundException e){
        }
    }
    
    // add entry to indexes info file
    private void updateIndexesInfoFile (Case index) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(FilesPath.INDEXES_INFO), true));
        writer.println(index.getIndexName() + " - " + index.getIndexLocation());
        writer.close();
    }

    private void removeAllRows (JTable table) {
        if ( table.getModel().getRowCount() <= 0 )
            return; 
        
        TableModel model = table.getModel();
        ( (DefaultTableModel) model ).getDataVector().removeAllElements();
        ( (DefaultTableModel) model ).fireTableDataChanged();

        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);
        sorter.setRowFilter(null);
    }

    /*
     * Get index path from index name 
     * @return IndexInformation 
     */
    private Case getIndexInformationFromIndexName (String indexName) throws FileNotFoundException, IOException, ClassNotFoundException {
        File indexesInfo = new File(FilesPath.INDEXES_INFO);
        ArrayList<String> indexesInfoContent  = Utilities.getFileContentInArrayList(indexesInfo);

        for(String path: indexesInfoContent) {
            Case index = Case.getCase(path);

            if ( index.getIndexName().equals(indexName))
                return index ;
        }

        return null ;
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        NativeInterface.open(); // used for swing DJ Library
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CaseManagerFrame().setVisible(true);
            }
        });
    }
    
    
    //private static final String lookAndFeelName = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" ;
    //private static final String lookAndFeelName = "org.jvnet.substance.skin.SubstanceRavenGraphiteLookAndFeel";
    private static final String lookAndFeelName = "org.jvnet.substance.skin.SubstanceBusinessLookAndFeel";
    
    //private static final String lookAndFeelName = "org.jvnet.substance.skin.SubstanceDustLookAndFeel";
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable caseInformationTable;
    private javax.swing.JPanel caseManagerButtonsPanel;
    private javax.swing.JPanel caseManagerDataPanel;
    private javax.swing.JButton checkLicenseButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton loadCaseButton;
    private javax.swing.JButton newCaseButton;
    private javax.swing.JTable recentCaseTable;
    private javax.swing.JButton removeCaseButton;
    // End of variables declaration//GEN-END:variables

}
