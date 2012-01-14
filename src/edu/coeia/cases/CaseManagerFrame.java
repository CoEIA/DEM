package edu.coeia.cases;

/* import internal classes */
import edu.coeia.main.CaseFrame;
import edu.coeia.main.SmartCardDialog;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.gutil.GuiUtil;
import edu.coeia.util.DateUtil;
import edu.coeia.util.FilesPath ;
import edu.coeia.util.FileUtil;
import edu.coeia.util.DEMLogger;
import edu.coeia.util.ZipUtil;
import edu.coeia.util.GUIFileFilter;

/* import sun classes */
import javax.swing.JOptionPane ;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;

import java.io.IOException ;
import java.io.File ;
import java.io.FileNotFoundException ;

import java.util.List ;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     * the folder is [Case_forensics, CASES, TMP, HashLibrary, INDEXES.txt] 
     * and handle the list of all opening case to prevent opening the same case more than one time
     */
    private static final CaseManager caseManager = CaseManager.Manager ;
    
    /**
     * Logger Object
     */
    private static final Logger logger = Logger.getLogger(edu.coeia.util.FilesPath.LOG_NAMESPACE);
    
    /** Creates new form CaseManagerFrame */
    public CaseManagerFrame() {
        initComponents(); // put swing components
        initJFrame();    // set size and location and title
        DEMLogger.logging(logger);      // write log
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
        caseManagerButtonsPanel = new javax.swing.JPanel();
        newCaseButton = new javax.swing.JButton();
        loadCaseButton = new javax.swing.JButton();
        removeCaseButton = new javax.swing.JButton();
        checkLicenseButton = new javax.swing.JButton();
        importCaseButton = new javax.swing.JButton();
        exportCaseButton = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Digital Evidence Miner: Case Manager");
        setResizable(false);

        caseManagerDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Recent Cases Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        recentCaseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Case Name","Investigator Name","Case Date","Case Description"
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

    javax.swing.GroupLayout caseManagerDataPanelLayout = new javax.swing.GroupLayout(caseManagerDataPanel);
    caseManagerDataPanel.setLayout(caseManagerDataPanelLayout);
    caseManagerDataPanelLayout.setHorizontalGroup(
        caseManagerDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(caseManagerDataPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .addContainerGap())
    );
    caseManagerDataPanelLayout.setVerticalGroup(
        caseManagerDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(caseManagerDataPanelLayout.createSequentialGroup()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
            .addContainerGap())
    );

    caseManagerButtonsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Case Operations", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

    newCaseButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    newCaseButton.setText("Create New Case");
    newCaseButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            newCaseButtonActionPerformed(evt);
        }
    });

    loadCaseButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    loadCaseButton.setText("Activiate Selected Case");
    loadCaseButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            loadCaseButtonActionPerformed(evt);
        }
    });

    removeCaseButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    removeCaseButton.setText("Remove Selected Case");
    removeCaseButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            removeCaseButtonActionPerformed(evt);
        }
    });

    checkLicenseButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    checkLicenseButton.setText("Check License");
    checkLicenseButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            checkLicenseButtonActionPerformed(evt);
        }
    });

    importCaseButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    importCaseButton.setText("Import Existing Case");
    importCaseButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            importCaseButtonActionPerformed(evt);
        }
    });

    exportCaseButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    exportCaseButton.setText("Export Selected Case");
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
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, caseManagerButtonsPanelLayout.createSequentialGroup()
                    .addGroup(caseManagerButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(newCaseButton, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                        .addComponent(loadCaseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(removeCaseButton, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
                    .addContainerGap())
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, caseManagerButtonsPanelLayout.createSequentialGroup()
                    .addGroup(caseManagerButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(checkLicenseButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                        .addComponent(exportCaseButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                        .addComponent(importCaseButton, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
                    .addContainerGap())))
    );
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
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jToolBar1.setFloatable(false);
    jToolBar1.setRollover(true);

    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(153, 153, 153));
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/main/resources/caseManager.jpg"))); // NOI18N
    jToolBar1.add(jLabel2);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(caseManagerButtonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(caseManagerDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(caseManagerDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(caseManagerButtonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadCaseButtonActionPerformed
        this.loadCaseAction();
    }//GEN-LAST:event_loadCaseButtonActionPerformed

    private void newCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCaseButtonActionPerformed
        try {
            this.createNewCaseAction();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_newCaseButtonActionPerformed

    private void removeCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeCaseButtonActionPerformed
        this.removeCaseAction();
    }//GEN-LAST:event_removeCaseButtonActionPerformed

    private void recentCaseTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recentCaseTableMouseClicked
        if ( evt.getClickCount() == 2 ) { // double click
            this.caseTableDoubleClickedAction();
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

    private void checkLicenseAction() {
        if ( licenseManager.isFullVersion() ) { // show smart card inserting/usage dialog
            SmartCardDialog scd = new SmartCardDialog(this, true, true);
            scd.setVisible(true);
        }
        else {
            int diff = licenseManager.getRemainingDays();
            JOptionPane.showMessageDialog(this, "Remaining days: " + diff, "Trial Version",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void importCaseAction(){
        try {
            final GUIFileFilter SWING_DEM_FILTER = new GUIFileFilter("DEM CASE", 
                FilesPath.DEM_CASE_EXTENSION);
    
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(SWING_DEM_FILTER);
            
            int result = fileChooser.showOpenDialog(this);
            if ( result == JFileChooser.APPROVE_OPTION ) {
                final File file = fileChooser.getSelectedFile();
                
                new SwingWorker<Void, String>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        ZipUtil zipper = new ZipUtil();
                        String fileNameWithOutExt = file.getName().toString().replaceFirst("[.][^.]+$", "");
                        String destPath = caseManager.getCasesPath() + File.separator + fileNameWithOutExt;
                        zipper.decompress(file.getAbsolutePath(), destPath);
                        
                        File filePath = new File(destPath).listFiles()[0];
                        String path = filePath.listFiles()[0].getAbsolutePath();
                        
                        String line = fileNameWithOutExt + " - " + path;
                        Case aCase = CaseManager.getCase(line);
                        aCase.setCaseLocation(path);
                        CaseManager.updateCase(aCase);
                        
                        String prefLocation = aCase.getCaseLocation() + "\\" + "CASE.pref";
                        CaseHistoryHandler.importCaseHistory(prefLocation);
                        readCases(); 
                        
                        return null;
                    }

                    @Override
                    protected void process(List<String> names) {
                    }
                }.execute();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void exportCaseAction() { 
        try {
            logger.info("Load Case Entring");
            String indexName = getSelectedCase();
            Case aCase = CaseManager.getCaseFromCaseName(indexName);
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(indexName + FilesPath.DEM_CASE_EXTENSION) );

            int result = fileChooser.showSaveDialog(this);
            if ( result == JFileChooser.APPROVE_OPTION ) {
                File file = fileChooser.getSelectedFile();
                String caseName = file.getAbsolutePath();
                
                String prefLocation = aCase.getCaseLocation() + "\\" + "CASE.pref";
                CaseHistoryHandler.exportToFile(aCase.getCaseName(), prefLocation);
                
                ZipUtil zipper = new ZipUtil();
                zipper.compress(aCase.getCaseLocation(), caseName);
            }
        }
        catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, "please select the case you want to open",
                    "No Case is Selected", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadCaseAction() {
        try {
            logger.info("Load Case Entring");
            String indexName = getSelectedCase();
            loadCase(indexName, false);
        }
        catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, "please select the case you want to open",
                    "No Case is Selected", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "the location for this index is not founded, please recreate the case again", "Index File not Found!",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void caseTableDoubleClickedAction() {
        try {
            String caseName = getSelectedCase();
            loadCase(caseName, false);
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(this, "the location for this index is not founded, please recreate the case again", "Index File not Found!",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removeCaseAction() {
        String caseName = null; 
        
        try {
            caseName = getSelectedCase();
        }
        catch(NullPointerException e) {
            JOptionPane.showMessageDialog(this, "please select the case you want to remove",
                "No Case is Selected", JOptionPane.INFORMATION_MESSAGE);
            
            return ;
        }
        
        if ( !caseManager.isRunningCase(caseName)) {
            removeCase(caseName);
            logger.info("Remove Case : " + caseName);

            readCases(); // update view table
        }
        else {
            JOptionPane.showMessageDialog(this, "This case is already opening, close it first to remove it",
                    "Please close the openining Case", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void createNewCaseAction() throws Exception{
        logger.info("Create New Case Entring");

        CaseWizardDialog indexWizard = new CaseWizardDialog(CaseManagerFrame.this,true, 
                licenseManager.isFullVersion());
        indexWizard.setVisible(true);

        Case aCase = indexWizard.getCurrentCase();
        if ( aCase == null) {
            logger.info("Create New Case Cancled");
            return ;
        }

        logger.info("Create New Case Don Successfully");
        CaseManager.writeCaseToInfoFile(aCase); // update indexes info file with new index

        readCases(); // update recent table with this new information

        if ( indexWizard.checkDirectIndex()) {
            try {
                loadCase(aCase.getCaseName(), true);
            }
            catch(Exception e){
                logger.severe("Cannot Index the case directly after create it");
                e.printStackTrace();
            }
        }
    }
    
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
             GuiUtil.changeLookAndFeel(lookAndFeelName, this);  // set look and feel to windows look 
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
        this.setTitle("Digital Evidence Miner: Case Manager Window");
        
        // add close event
        this.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent event){
                licenseManager.saveUsage();
            }
        });
    }
    
    /*
     * Read cases from files and update recent table
     */
    private void readCases() {
        try {
            // read cases into case JTable 
            List<Case> cases = CaseManager.getCases();
            for(Case aCase: cases) {
                insertIntoCaseTable(aCase);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(CaseManagerFrame.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
    
    /*
     * Add index information to recent table (used when update recent)
     */
    private void insertIntoCaseTable (Case index) {
        Object[] object = {
            index.getCaseName(), index.getInvestigatorName(), 
            DateUtil.formatDateTime(index.getCreateTime()), index.getDescription(),
        };
        
        JTableUtil.addRowToJTable(this.recentCaseTable, object);
        
    }
    
    private String getSelectedCase () {
        int row = recentCaseTable.getSelectedRow();

        if ( row < 0 ) {
            throw new NullPointerException("Case is Not Selected");
        }

        String indexName = (String) recentCaseTable.getValueAt(row, 0);
        return indexName; 
    }

    private void loadCase (String caseName, boolean startIndex) throws FileNotFoundException, IOException, ClassNotFoundException, Exception{
        if ( caseName != null ) {
            if ( !caseManager.isRunningCase(caseName)) {
                Case aCase = CaseManager.getCaseFromCaseName(caseName);

                // check here for case evience chnaging
                // and update the file before opening the case
                boolean caseSourceIsUptoDate = true;
                
                if ( isCaseHaveChangedSource(aCase) )  {
                    caseSourceIsUptoDate = askAndUpdateNewCaseSource(aCase);
                }
                
                if ( caseSourceIsUptoDate ) {                    
                    caseManager.addCase(caseName);

                    CaseFrame mainFrame = new CaseFrame(aCase, caseManager.getList());
                    mainFrame.setLocationRelativeTo(this);
                    mainFrame.setVisible(true);

                    if ( ! CaseHistoryHandler.get(aCase.getCaseName()).getIsCaseIndexed() ) {
                        logger.info("show direct indexing panel");
                        mainFrame.showIndexDialog(startIndex);
                    }
                }
                else {
                    System.out.println("case folder in changed, and you should privde the correct path");
                }
            }
            else {
                JOptionPane.showMessageDialog(this, "This case is already opening",
                        "Already Openining Case", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private boolean isCaseHaveChangedSource(final Case aCase) throws IOException {
        return !CasePathHandler.newInstance(aCase.getCaseLocation()).getChangedEntries().isEmpty();
    }
    
    private boolean askAndUpdateNewCaseSource(final Case aCase) throws IOException {
        UpdatingCaseEvidenceSourceDialog dialog = new UpdatingCaseEvidenceSourceDialog(this, true, aCase);
        dialog.setVisible(true);
        
        return dialog.getResult();
    }
    
    private void removeCase (String caseName) {
        try {
            Case aCase = CaseManager.getCaseFromCaseName(caseName);
            boolean status = CaseManager.removeCase(aCase);
            
            if ( !status)  {
                System.out.println("error in removing file: " + caseName);
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "the location for this index is not founded, please recreate the case again", "Index File not Found!",
                JOptionPane.ERROR_MESSAGE);
        }
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
    
    private static final String lookAndFeelName = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" ;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel caseManagerButtonsPanel;
    private javax.swing.JPanel caseManagerDataPanel;
    private javax.swing.JButton checkLicenseButton;
    private javax.swing.JButton exportCaseButton;
    private javax.swing.JButton importCaseButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton loadCaseButton;
    private javax.swing.JButton newCaseButton;
    private javax.swing.JTable recentCaseTable;
    private javax.swing.JButton removeCaseButton;
    // End of variables declaration//GEN-END:variables

}
