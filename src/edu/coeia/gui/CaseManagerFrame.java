package edu.coeia.gui;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CaseManagerFrame.java
 *
 * Created on 16/07/2010, 01:09:17 م
 */

/* import internal classes */
import edu.coeia.utility.FilesPath ;
import edu.coeia.utility.Utilities;
import edu.coeia.index.IndexInformation;
import edu.coeia.index.IndexOperation ;

/* import sun classes */
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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar ;
import java.util.List;

import java.awt.Toolkit ;
import java.awt.Dimension ;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/* import Third Party Libraries */
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import java.util.prefs.Preferences;

/**
 *
 * @author Wajdy Essam
 *
 */

public class CaseManagerFrame extends javax.swing.JFrame {

    //private static final String WINDOWS = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" ;
    //private static final String RAVEN = "org.jvnet.substance.skin.SubstanceRavenGraphiteLookAndFeel";
    //private static final String BUS = "org.jvnet.substance.skin.SubstanceBusinessLookAndFeel";
    private static final String DUST = "org.jvnet.substance.skin.SubstanceDustLookAndFeel";

    private List<String> listOfOpeningCase ;

    // this code for smart card!!
    
    
    
    //private Timer timer;

//    public int[] ATRLen = new int[1];
//    public int[] hContext = new int[1];
//    public int[] cchReaders = new int[1];
//    public int[] hCard = new int[1];
//    public int[] PrefProtocols = new int[1];
//    public byte[] ATRVal = new byte[128];
//    public byte[] szReaders = new byte[1024];
//    public int[] RecvLen = new int[1];
//    public int SendLen = 0;
//    public byte[] SendBuff = new byte[300];
//    public byte[] RecvBuff = new byte[300];
//    public int[] nBytesRet = new int[1];

    //JPCSC Variables
//    public int retCode;
    //public boolean connActive= false;
//    public final int INVALID_SW1SW2 = -450;
//    static JacspcscLoader jacs = new JacspcscLoader();
//
//    // licence chcker varaiable
    //private boolean isExpire = true ;

    // to compile for smart card usage
    //private boolean smartCardSupport = true ;

    // determine full version or beta version
    // true  - full version should work using smart card licence
    // false - beta version should be trial version for 15 days
    private boolean isFullVersion = false;
    private static final int TRIAL_LENGTH = 60;
    
    /** Creates new form CaseManagerFrame */
    public CaseManagerFrame() {
        initComponents();

        this.listOfOpeningCase = new ArrayList<String>();
        
        /** set look and feel to windows look */
        try {
            changeLookAndFeel(DUST);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        /** set application in middle of screen **/
        Toolkit kit = Toolkit.getDefaultToolkit() ;
        Dimension screenSize = kit.getScreenSize() ;
        int width = screenSize.width ;
        int height = screenSize.height;

        setLocation( width / 4, height / 4);
        //this.setIconImage(kit.getImage(this.getClass().getResource("resources\\textCloud.png")));

        this.setTitle("Digital Evidence Miner (Beta Version): Case Manager Window");

        /** initApplication:
         *  check Application existsting folder
         *  if not found: create new application folder
         *  if found: open the indexes pointers
         *  and fill the table with information
        */

        // add close event
        this.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent event){
                saveUsage();
            }
        });

        try {

            // if its beta version
            if ( ! isFullVersion ) {
                if ( isExpireNow() ) {
                    JOptionPane.showMessageDialog(this, "Your software has been expired!",
                            "please purchase the full version...",
                            JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            }

            initApplication();

//            smartCardSupport = false;
//
//            if ( smartCardSupport ) {
//                enableButtons(connActive);
//                timer = new Timer(500, CheckCard);
//                timer.start();
//            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }        
    }

    private void changeLookAndFeel ( String lookName ) throws Exception  {
        UIManager.setLookAndFeel(lookName);
        SwingUtilities.updateComponentTreeUI(this);
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
            e.printStackTrace();
        }
    }//GEN-LAST:event_loadCaseButtonActionPerformed

    private void newCaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newCaseButtonActionPerformed
        try {
            IndexWizard indexWizard = new IndexWizard(CaseManagerFrame.this,true, isFullVersion);
            indexWizard.setVisible(true);
            IndexInformation index = indexWizard.getIndex();

            if ( index == null) {
//                JOptionPane.showMessageDialog(CaseManagerFrame.this, "Cannot Creating Index",
//                        "Operation is not Completed",JOptionPane.ERROR_MESSAGE);
                return ;
            }

            // update indexes info file with new index
            updateIndexesInfoFile(index);

            // update recent table with this new information
            updateRecentTable();

        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
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

        try {
            IndexInformation index = getIndexInformationFromIndexName(indexName);
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

                // update view table
                updateRecentTable();
                
                // remove entrie in case information table
                removeAllRows(caseInformationTable);
            }
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "the location for this index is not founded, please recreate the case again", "Index File not Found!",
                JOptionPane.ERROR_MESSAGE);
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_removeCaseButtonActionPerformed

    private void recentCaseTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_recentCaseTableMouseClicked
        
        if ( evt.getClickCount() == 2 ) {
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
                e.printStackTrace();
            }
        }
        else {
            showCaseInformation();
        }
    }//GEN-LAST:event_recentCaseTableMouseClicked

    private void checkLicenseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkLicenseButtonActionPerformed
        // show smart card inserting/usage dialog
        if ( isFullVersion ) {
            SmartCardDialog scd = new SmartCardDialog(this, true, true);
            scd.setVisible(true);
        }
        else {
            Date currentDate = new Date();

            Preferences root = Preferences.userRoot();
            Preferences node = root.node("/com/coeia/dem");

            String installTime = node.get("installTime",String.valueOf(currentDate.getTime()));
            Date installDate = new Date(Long.valueOf(installTime));

            Date expireDate = addDays(installDate, TRIAL_LENGTH);

            int diff = subtractDays( expireDate, currentDate);
            JOptionPane.showMessageDialog(this, "Remaining days: " + diff, "Trial Version",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_checkLicenseButtonActionPerformed

    // from: http://www.velocityreviews.com/forums/t139746-how-to-subtract-dates.html
    private int subtractDays(Date date1, Date date2) {
        GregorianCalendar gc1 = new GregorianCalendar();  gc1.setTime(date1);
        GregorianCalendar gc2 = new GregorianCalendar();  gc2.setTime(date2);

        int days1 = 0;
        int days2 = 0;
        int maxYear = Math.max(gc1.get(Calendar.YEAR), gc2.get(Calendar.YEAR));

        GregorianCalendar gctmp = (GregorianCalendar) gc1.clone();
        for (int f = gctmp.get(Calendar.YEAR);  f < maxYear;  f++)
            {days1 += gctmp.getActualMaximum(Calendar.DAY_OF_YEAR);  gctmp.add(Calendar.YEAR, 1);}

        gctmp = (GregorianCalendar) gc2.clone();
        for (int f = gctmp.get(Calendar.YEAR);  f < maxYear;  f++)
            {days2 += gctmp.getActualMaximum(Calendar.DAY_OF_YEAR);  gctmp.add(Calendar.YEAR, 1);}

        days1 += gc1.get(Calendar.DAY_OF_YEAR) - 1;
        days2 += gc2.get(Calendar.DAY_OF_YEAR) - 1;

        return (days1 - days2);
    }
    
    /**
     *
     * private Functions
     *
    */

    private boolean isExpireNow() {
        boolean state = false;

        Date currentDate = new Date();
        
        //Preferences node = Preferences.userNodeForPackage(this.getClass());
        
        Preferences root = Preferences.userRoot();
        Preferences node = root.node("/com/coeia/dem");

        String installTime = node.get("installTime",String.valueOf(currentDate.getTime()));
        String lastUsage = node.get("LastUsage", String.valueOf(currentDate.getTime()));

        Date installDate = new Date(Long.valueOf(installTime));
        Date lastDate = new Date(Long.valueOf(lastUsage));

        if ( lastDate.after(currentDate) ) {
            state = true;
        }
        else if ( lastDate.before(currentDate) ) {
            Date expireDate = addDays(installDate, TRIAL_LENGTH);

            if ( expireDate.before(currentDate))
                state = true;
            else
                state = false;
        }
        else {
            node.put("installTime", String.valueOf(currentDate.getTime()));
            node.put("LastUsage", String.valueOf(currentDate.getTime()));
            state = false;
        }

        return state;
    }

    private void saveUsage () {
         Date currentDate = new Date();

        Preferences root = Preferences.userRoot();
        Preferences node = root.node("/com/coeia/dem");
        
        node.put("LastUsage", String.valueOf(currentDate.getTime()));
    }

    private static Date addDays (Date date, int days) {
        GregorianCalendar now = new GregorianCalendar();
        now.setTime(date);
        now.add(Calendar.DATE, days);

        return (now.getTime());
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
            if ( !this.listOfOpeningCase.contains(caseName)) {
                IndexInformation index = getIndexInformationFromIndexName(caseName);

                this.listOfOpeningCase.add(caseName);

                OfflineMinningFrame mainFrame = new OfflineMinningFrame(index,false, index.getIndexName() + " Case",
                        listOfOpeningCase);
                mainFrame.setVisible(true);
            }
            else {
                JOptionPane.showMessageDialog(this, "This case is already opening",
                        "Already Openining Case", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void showCaseInformation () {
        int row = recentCaseTable.getSelectedRow();

        if ( row < 0 )
            return ;

        String indexName = (String) recentCaseTable.getValueAt(row, 0);

        // clear value on table before adding new values
        if ( caseInformationTable.getModel().getRowCount() > 0 )
            removeAllRows(caseInformationTable);

        try {
            IndexInformation index = getIndexInformationFromIndexName(indexName);
            DefaultTableModel model = (DefaultTableModel) caseInformationTable.getModel();

            model.addRow( new Object[] { "Index Name" , index.getIndexName() });
            model.addRow( new Object[] { "Index Path" , index.getIndexLocation() });
            model.addRow( new Object[] { "Created Date and Time" , Utilities.formatDateTime(index.getCreateTime()) } );
            model.addRow( new Object[] { "Data Indexed Size" , Utilities.formatSize(Utilities.toKB(index.getDataIndexedSize())) + " KB"  });
            model.addRow( new Object[] { "Extensions Allowed" , index.getExtensionAllowed() });

            addValues(index.getDocumentInIndex(),"Case In Index",model);
            addValues(index.getPstPath(),"Outlook Documents in Index", model);
            addValues(index.getIePath(),"IE Paths in Index", model);
            addValues(index.getFFPath(), "FF Paths in Index", model);
            addValues(index.getMsnPath(),"MSN Paths in Index", model);
            addValues(index.getYahooPath(),"Yahoo Paths in Index", model);
            addValues(index.getSkypePath(), "Skype Paths in Index", model);

            model.addRow( new Object[] { "Cache All Images" , index.getCacheImages() });
            //model.addRow( new Object[] { "Look Inside Compressed Files" , index.getCheckCompressed() });
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "the location for this index is not founded, please recreate the case again", "Index File not Found!",
                JOptionPane.ERROR_MESSAGE);
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
   }

    // add value from arraylist to case information table
    private void addValues (ArrayList<String> data, String text, DefaultTableModel model) {
        for (String s: data ) {
            model.addRow( new Object[] { text , s });
            text = "" ;
        }
    }

    // add entry to indexes info file
    private void updateIndexesInfoFile (IndexInformation index) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(new File(FilesPath.INDEXES_INFO), true));
        writer.println(index.getIndexName() + " - " + index.getIndexLocation());
        writer.close();
    }
    
    // create new application folder or open it
    private void initApplication () throws IOException,ClassNotFoundException {
        File root = new File(FilesPath.APPLICATION_PATH);
        File cases = new File(FilesPath.CASES_PATH);
        File indexesInfo = new File(FilesPath.INDEXES_INFO);
        File tmpFile = new File(FilesPath.TMP_PATH);
        
        if ( ! root.exists() ) {
           createApplicationFolders(root, cases, indexesInfo, tmpFile);
        }
        else {
            System.out.println("Open: " + FilesPath.INDEXES_INFO);
            try {
                updateRecentTable();
            }
            catch (IOException e) {
                // if thers is error (like not found index.txt file) recreate all the files)
                createApplicationFolders(root, cases, indexesInfo, tmpFile);
            }
        }
    }

    private void createApplicationFolders (File root, File cases, File indexesInfo, File tmpFile) throws IOException{
        if  ( ! root.exists() )
            root.mkdir();   // make offline folder in applicationData
        
         if  ( ! cases.exists() )
             cases.mkdir();  // make offlinemining\cases

         if  ( ! indexesInfo.exists() )
             indexesInfo.createNewFile();  // make offlinemining\indexes.txt

         if  ( ! tmpFile.exists() )
             tmpFile.mkdir();

        // craete tmp files
        new File(FilesPath.HIS_TMP).createNewFile();
        new File(FilesPath.PASS_TMP).createNewFile();
        new File(FilesPath.CORRE_FILE).createNewFile();

        System.out.println("Create new index information dir");
    }

    // update recent table by reading indexes info file
    public void updateRecentTable () throws FileNotFoundException, IOException, ClassNotFoundException {
        File indexesInfo = new File(FilesPath.INDEXES_INFO);
        ArrayList<String> indexesInfoContent  = Utilities.getFileContentInArrayList(indexesInfo);

        // clear value on table before adding new values
        if ( recentCaseTable.getModel().getRowCount() > 0 )
            removeAllRows(recentCaseTable);

        for(String path: indexesInfoContent) {
            IndexInformation index = getIndexInformation(path);
            addIndexInformationToTable(index);
        }
    }

    // get indexInformation from line of string (was reading from indexes_info file)
    private IndexInformation getIndexInformation (String line) throws IOException,ClassNotFoundException {
        String name = line.split("-")[0].trim();
        String path = line.split("-")[1].trim();

        IndexInformation index = IndexOperation.readIndex(new File(path + "\\" + name + ".DAT"));

        return index ;
    }
    
    // add index information to recent table
    private void addIndexInformationToTable (IndexInformation index) {
        DefaultTableModel model = (DefaultTableModel) recentCaseTable.getModel();
        model.addRow( new Object[] {index.getIndexName(), index.getInvestigatorName(), Utilities.formatDateTime(index.getCreateTime()), index.getDescription(),
            index.getIndexStatus() });
    }

    private void removeAllRows (JTable table) {
        TableModel model = table.getModel();
        ( (DefaultTableModel) model ).getDataVector().removeAllElements();
        ( (DefaultTableModel) model ).fireTableDataChanged();

        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);
        sorter.setRowFilter(null);
    }

    // get index path from index name
    private IndexInformation getIndexInformationFromIndexName (String indexName) throws FileNotFoundException, IOException, ClassNotFoundException {
        File indexesInfo = new File(FilesPath.INDEXES_INFO);
        ArrayList<String> indexesInfoContent  = Utilities.getFileContentInArrayList(indexesInfo);

        for(String path: indexesInfoContent) {
            IndexInformation index = getIndexInformation(path);

            if ( index.getIndexName().equals(indexName))
                return index ;
        }

        return null ;
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        NativeInterface.open();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CaseManagerFrame().setVisible(true);
            }
        });
    }

//    ActionListener CheckCard = new ActionListener() {
//        public void actionPerformed(ActionEvent e) {
//            if (checkCard()) {
//                smartCardLbl.setText("Smart Card inserted");
//
//                if (  ! connActive ) {
//                    //connect(init());
//                    connActive= true;
//                }
//
////                // check this card is by coeia
////                String key = readKey();
////
////                if ( key == null || ! key.equals("WAJDY")) {
////                    JOptionPane.showMessageDialog(CaseManagerFrame.this,"Please insert the valid smart card",
////                            "Illegal Smart Card", JOptionPane.ERROR_MESSAGE);
////                    return ;
////                }
//
//                enableButtons(connActive);
//
//                // read number of useage
//                checkExpire();
//
//            } else {
//                smartCardLbl.setText("Please Insert Smart Card");
//                //reset();
//                connActive= false;
//                enableButtons(connActive);
//                isExpire = true;
//            }
//        }
//    };
//
//    private void enableButtons (boolean bool) {
//        newCaseButton.setEnabled(bool);
//        loadCaseButton.setEnabled(bool);
//        removeCaseButton.setEnabled(bool);
//        caseNumberLbl.setText("");
//    }
//
//    private void checkExpire () {
//        int caseNumber = readCaseNumber();
//        caseNumberLbl.setText(" " + caseNumber);
//
//        if ( caseNumber == 0) {
//            enableButtons(false);
//            caseNumberLbl.setText(" " + caseNumber);
//
//            if ( isExpire ) {
//                JOptionPane.showMessageDialog(this, "Licence Key Expire", "you must contact CoEIA to renewal licence",
//                    JOptionPane.INFORMATION_MESSAGE);
//                isExpire = false;
//            }
//        }
//        else if ( caseNumber == -1 ) {
//            enableButtons(false);
//        }
//    }
//
//    private boolean checkCard() {
//        boolean data = false;
//
//        try {
//            ArrayList<String> result = Utilities.readProgramOutputStream(FilesPath.SMART_CARD_PROGRAM + " s");
//            data = Boolean.valueOf(result.get(0));
//        }
//        catch (Exception e){
//            data = false;
//        }
//
//        return data;
//    }
//
//    private String readKey () {
//        return "";
//    }
//
//    private void decreaseNumber () {
//         try {
//            ArrayList<String> result = Utilities.readProgramOutputStream(FilesPath.SMART_CARD_PROGRAM + " d");
//        }
//        catch (Exception e){
//        }
//    }
//
//    private int readCaseNumber(){
//        int data = -1;
//
//        try {
//            ArrayList<String> result = Utilities.readProgramOutputStream(FilesPath.SMART_CARD_PROGRAM + " n");
//            data = Integer.valueOf(result.get(0));
//        }
//        catch (Exception e){
//            data = -1;
//        }
//
//        return data;
//    }
    
    
//    public void writeKey(String key) {
//        byte hiByte = 0, loByte = 0;
//        String tmpStr = "", tmpHex = "", chkStr = "";
//        byte[] tmpArray = new byte[56];
//
//        // write to file 1 (DD 44)
//        hiByte = (byte) 0xDD;
//        loByte = (byte) 0x44;
//        chkStr = "91 00";
//        byte lenByte = (byte) 0x0F;
//
//        retCode = selectFile(hiByte, loByte);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//
//        for (int i = 0; i < 2; i++) {
//            tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//            //For single character hex
//            if (tmpHex.length() == 1) {
//                tmpHex = "0" + tmpHex;
//            }
//            tmpStr += " " + tmpHex;
//        }
//
//        if (tmpStr.indexOf(chkStr) < 0) {
//            displayOut(2, 0, "The return string is invalid. Value: " + tmpStr);
//            retCode = INVALID_SW1SW2;
//            return;
//        }
//
//        // 3. Write input data to card
//        tmpArray = key.getBytes();
//        retCode = writeRecord(1, (byte) 0x00, lenByte, (byte) key.length(), tmpArray);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//        displayOut(0, 0, "Data : " + key + " is written to card.");
//    }
//
//    public String readKey() {
//        byte hiByte = 0, loByte = 0, lenByte = 0;
//        String tmpStr = "", tmpHex = "", chkStr = "";
//
//        //select user file AA 11
//        hiByte = (byte) 0xDD;
//        loByte = (byte) 0x44;
//        chkStr = "91 00";
//        lenByte = (byte) 0x0F;
//
//        retCode = selectFile(hiByte, loByte);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return null;
//        }
//
//        for (int i = 0; i < 2; i++) {
//            tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//            //For single character hex
//            if (tmpHex.length() == 1) {
//                tmpHex = "0" + tmpHex;
//            }
//            tmpStr += " " + tmpHex;
//        }
//
//        if (tmpStr.indexOf(chkStr) < 0) {
//            displayOut(2, 0, "The return string is invalid. Value: " + tmpStr);
//            retCode = INVALID_SW1SW2;
//            return null;
//        }
//
//        //read first record of user file selected
//        retCode = readRecord((byte) 0x00, (byte) lenByte);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return null;
//        }
//
//        tmpStr = "";
//        int indx = 0;
//        while (RecvBuff[indx] != (byte) 0x00) {
//            tmpStr = tmpStr + (char) RecvBuff[indx];
//            indx++;
//        }
//
//        //displayOut(0, 0, "Data read is : " + tmpStr);
//        return tmpStr;
//    }
//
//    public void writeCaseNumber(int cn) {
//        byte hiByte = 0, loByte = 0;
//        String tmpStr = "", tmpHex = "", chkStr = "";
//        byte[] tmpArray = new byte[56];
//
//        // write to file 1 (DD 44)
//        hiByte = (byte) 0xDD;
//        loByte = (byte) 0x44;
//        chkStr = "91 00";
//        byte lenByte = (byte) 0x0F;
//
//        retCode = selectFile(hiByte, loByte);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//
//        for (int i = 0; i < 2; i++) {
//            tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//            //For single character hex
//            if (tmpHex.length() == 1) {
//                tmpHex = "0" + tmpHex;
//            }
//            tmpStr += " " + tmpHex;
//        }
//
//        if (tmpStr.indexOf(chkStr) < 0) {
//            displayOut(2, 0, "The return string is invalid. Value: " + tmpStr);
//            retCode = INVALID_SW1SW2;
//            return;
//        }
//
//        // 3. Write input data to card
//        String caseNumber = String.valueOf(cn);
//        tmpArray = caseNumber.getBytes();
//        retCode = writeRecord(1, (byte) 0x01, lenByte, (byte) caseNumber.length(), tmpArray);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//        displayOut(0, 0, "Data : " + caseNumber + " is written to card.");
//    }
//
//    public int readCaseNumber() {
//        int caseNumber = 0;
//        byte hiByte = 0, loByte = 0, lenByte = 0;
//        String tmpStr = "", tmpHex = "", chkStr = "";
//
//        //select user file AA 11
//        hiByte = (byte) 0xDD;
//        loByte = (byte) 0x44;
//        chkStr = "91 00";
//        lenByte = (byte) 0x0F;
//
//        retCode = selectFile(hiByte, loByte);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return -1;
//        }
//
//        for (int i = 0; i < 2; i++) {
//            tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//            //For single character hex
//            if (tmpHex.length() == 1) {
//                tmpHex = "0" + tmpHex;
//            }
//            tmpStr += " " + tmpHex;
//        }
//
//        if (tmpStr.indexOf(chkStr) < 0) {
//            displayOut(2, 0, "The return string is invalid. Value: " + tmpStr);
//            retCode = INVALID_SW1SW2;
//            return -1;
//        }
//
//        //read second record of user file selected
//        retCode = readRecord((byte) 0x01, (byte) lenByte);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return -1;
//        }
//
//        tmpStr = "";
//        int indx = 0;
//        while (RecvBuff[indx] != (byte) 0x00) {
//            // if (indx < txtVal.getText().length())
//            tmpStr = tmpStr + (char) RecvBuff[indx];
//            indx++;
//        }
//
//       // displayOut(0, 0, "Data read is : " + tmpStr);
//
//        caseNumber = Integer.parseInt(tmpStr);
//        return caseNumber;
//    }
//
//    public void reset() {
//        //disconnect
//        if (connActive) {
//            retCode = jacs.jSCardDisconnect(hCard, ACSModule.SCARD_UNPOWER_CARD);
//            connActive = false;
//            print("disconnect");
//        }
//
//        //release context
//        retCode = jacs.jSCardReleaseContext(hContext);
//    }
//
//    public String init() {
//        //1. Establish context and obtain hContext handle
//        retCode = jacs.jSCardEstablishContext(ACSModule.SCARD_SCOPE_USER, 0, 0, hContext);
//
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            print("Calling SCardEstablishContext...FAILED\n");
//            displayOut(1, retCode, "");
//        }
//
//        //2. List PC/SC card readers installed in the system
//        retCode = jacs.jSCardListReaders(hContext, 0, szReaders, cchReaders);
//
//        int offset = 0;
//        String result = null;
//
//        for (int i = 0; i < cchReaders[0] - 1; i++) {
//            if (szReaders[i] == 0x00) {
//                String device = new String(szReaders, offset, i - offset);
//                offset = i + 1;
//                result = device;
//            }
//        }
//
//        if (offset == 0) {
//            result = null ;
//        }
//
//        return result;
//    }
//
//    public void connect(String rdrcon) {
//        if (connActive) {
//            retCode = jacs.jSCardDisconnect(hCard, ACSModule.SCARD_UNPOWER_CARD);
//        }
//
//        retCode = jacs.jSCardConnect(hContext,
//                rdrcon,
//                ACSModule.SCARD_SHARE_SHARED,
//                ACSModule.SCARD_PROTOCOL_T1 | ACSModule.SCARD_PROTOCOL_T0,
//                hCard,
//                PrefProtocols);
//
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            //check if ACR128 SAM is used and use Direct Mode if SAM is not detected
//            if (rdrcon.lastIndexOf("ACR128U SAM") > -1) {
//                retCode = jacs.jSCardConnect(hContext,
//                        rdrcon,
//                        ACSModule.SCARD_SHARE_DIRECT,
//                        0,
//                        hCard,
//                        PrefProtocols);
//
//                if (retCode != ACSModule.SCARD_S_SUCCESS) {
//                    displayOut(1, retCode, "");
//                    connActiconnve = false;
//                    return;
//                } else {
//                    displayOut(0, 0, "Successful connection to " + rdrcon);
//                }
//            } else {
//                displayOut(1, retCode, "");
//                connActive = false;
//                return;
//            }
//        } else {
//            displayOut(0, 0, "Successful connection to " + rdrcon);
//        }
//    }
//
//    public void showATR(String rdrcon) {
//        int tmpWord;
//        int[] state = new int[1];
//        int[] readerLen = new int[1];
//        String tmpStr;
//
//        displayOut(0, 0, "Invoke SCardStatus");
//        //1. Invoke SCardStatus using hCard handle
//        //   and valid reader name
//        state[0] = 0;
//        readerLen[0] = 0;
//        for (int i = 0; i < 128; i++) {
//            ATRVal[i] = 0;
//        }
//
//        tmpWord = 32;
//        ATRLen[0] = tmpWord;
//
//        byte[] tmpReader = rdrcon.getBytes();
//        byte[] readerName = new byte[rdrcon.length() + 1];
//
//        for (int i = 0; i < rdrcon.length(); i++) {
//            readerName[i] = tmpReader[i];
//        }
//        readerName[rdrcon.length()] = 0; //set null terminator
//
//        retCode = jacs.jSCardStatus(hCard,
//                tmpReader,
//                readerLen,
//                state,
//                PrefProtocols,
//                ATRVal,
//                ATRLen);
//
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            displayOut(1, retCode, "");
//        } else {
//            //2. Format ATRVal returned and display string as ATR value
//            //tmpStr = "ATR Length: " & CInt(ATRLen)
//            String strHex;
//            tmpStr = "ATR Length: " + ATRLen[0];
//            displayOut(3, 0, tmpStr);
//            tmpStr = "ATR Value: ";
//
//            for (int i = 0; i < ATRLen[0]; i++) {
//                //Byte to Hex conversion
//                strHex = Integer.toHexString(((Byte) ATRVal[i]).intValue() & 0xFF).toUpperCase();
//
//                //For single character hex
//                if (strHex.length() == 1) {
//                    strHex = "0" + strHex;
//                }
//
//                tmpStr += " " + strHex;
//
//                //new line every 12 bytes
//                if (((i + 1) % 12 == 0) && ((i + 1) < ATRLen[0])) {
//                    tmpStr += "\n  ";
//                }
//            }
//
//            displayOut(3, 0, tmpStr);
//
//            //3. Interpret dwActProtocol returned and display as active protocol
//            tmpStr = "Active Protocol: ";
//
//            switch (PrefProtocols[0]) {
//                case 1:
//                    tmpStr = tmpStr + "T=0";
//                    break;
//                case 2: {
//                    if (rdrcon.lastIndexOf("ACR128U PICC") > -1) {
//                        tmpStr = tmpStr + "T=CL";
//                    } else {
//                        tmpStr = tmpStr + "T=1";
//                    }
//                    break;
//                }
//                default:
//                    tmpStr = "No protocol is defined.";
//                    break;
//            }
//
//            displayOut(3, 0, tmpStr);
//            interpretATR();
//        }
//    }
//
//    public static void print(String msg) {
//        System.out.println(msg);
//    }
//
//    public static void displayOut(int mType, int msgCode, String printText) {
//        switch (mType) {
//            case 1: {
//                print("! " + printText);
//                print(ACSModule.GetScardErrMsg(msgCode) + "\n");
//                break;
//
//            }
//            case 2:
////                print("< " + printText + "\n");
//                break;
//            case 3:
////                print("> " + printText + "\n");
//                break;
//            default:
//                print("- " + printText + "\n");
//        }
//    }
//
//    public void interpretATR() {
//        String RIDVal, cardName;
//        cardName = "";
//
//        //4. Interpret ATR and guess card
//        // 4.1. Mifare cards using ISO 14443 Part 3 Supplemental Document
//
//        if (ATRLen[0] > 14) {
//            RIDVal = "";
//            for (int i = 7; i < 11; i++) {
//                RIDVal = RIDVal + Integer.toHexString(((Byte) ATRVal[i]).intValue() & 0xFF);
//            }
//
//            if (RIDVal.equals("A000000306")) {
//                cardName = "";
//                switch (ATRVal[12]) {
//                    case 0:
//                        cardName = "No card information";
//                        break;
//                    case 1:
//                        cardName = "ISO 14443 A, Part1 Card Type";
//                        break;
//                    case 2:
//                        cardName = "ISO 14443 A, Part2 Card Type";
//                        break;
//                    case 3:
//                        cardName = "ISO 14443 A, Part3 Card Type";
//                        break;
//                    case 5:
//                        cardName = "ISO 14443 B, Part1 Card Type";
//                        break;
//                    case 6:
//                        cardName = "ISO 14443 B, Part2 Card Type";
//                        break;
//                    case 7:
//                        cardName = "ISO 14443 B, Part3 Card Type";
//                        break;
//                    case 9:
//                        cardName = "ISO 15693, Part1 Card Type";
//                        break;
//                    case 10:
//                        cardName = "ISO 15693, Part2 Card Type";
//                        break;
//                    case 11:
//                        cardName = "ISO 15693, Part3 Card Type";
//                        break;
//                    case 12:
//                        cardName = "ISO 15693, Part4 Card Type";
//                        break;
//                    case 13:
//                        cardName = "Contact Card (7816-10) IIC Card Type";
//                        break;
//                    case 14:
//                        cardName = "Contact Card (7816-10) Extended IIC Card Type";
//                        break;
//                    case 15:
//                        cardName = "Contact Card (7816-10) 2WBP Card Type";
//                        break;
//                    case 16:
//                        cardName = "Contact Card (7816-10) 3WBP Card Type";
//                        break;
//                    default:
//                        cardName = "Undefined card";
//                        break;
//                }
//            }
//
//            if (Integer.toHexString(((Byte) ATRVal[12]).intValue() & 0xFF).equals("3")) {
//                if (Integer.toHexString(((Byte) ATRVal[13]).intValue() & 0xFF).equals("0")) {
//                    switch (ATRVal[14]) {
//                        case 0x01:
//                            cardName = cardName + ": Mifare Standard 1K";
//                            break;
//                        case 0x02:
//                            cardName = cardName + ": Mifare Standard 4K";
//                            break;
//                        case 0x03:
//                            cardName = cardName + ": Mifare Ultra light";
//                            break;
//                        case 0x04:
//                            cardName = cardName + ": SLE55R_XXXX";
//                            break;
//                        case 0x06:
//                            cardName = cardName + ": SR176";
//                            break;
//                        case 0x07:
//                            cardName = cardName + ": SRI X4K";
//                            break;
//                        case 0x08:
//                            cardName = cardName + ": AT88RF020";
//                            break;
//                        case 0x09:
//                            cardName = cardName + ": AT88SC0204CRF";
//                            break;
//                        case 0x0A:
//                            cardName = cardName + ": AT88SC0808CRF";
//                            break;
//                        case 0x0B:
//                            cardName = cardName + ": AT88SC1616CRF";
//                            break;
//                        case 0x0C:
//                            cardName = cardName + ": AT88SC3216CRF";
//                            break;
//                        case 0x0D:
//                            cardName = cardName + ": AT88SC6416CRF";
//                            break;
//                        case 0x0E:
//                            cardName = cardName + ": SRF55V10P";
//                            break;
//                        case 0x0F:
//                            cardName = cardName + ": SRF55V02P";
//                            break;
//                        case 0x10:
//                            cardName = cardName + ": SRF55V10S";
//                            break;
//                        case 0x11:
//                            cardName = cardName + ": SRF55V02S";
//                            break;
//                        case 0x12:
//                            cardName = cardName + ": TAG IT";
//                            break;
//                        case 0x13:
//                            cardName = cardName + ": LRI512";
//                            break;
//                        case 0x14:
//                            cardName = cardName + ": ICODESLI";
//                            break;
//                        case 0x15:
//                            cardName = cardName + ": TEMPSENS";
//                            break;
//                        case 0x16:
//                            cardName = cardName + ": I.CODE1";
//                            break;
//                        case 0x17:
//                            cardName = cardName + ": PicoPass 2K";
//                            break;
//                        case 0x18:
//                            cardName = cardName + ": PicoPass 2KS";
//                            break;
//                        case 0x19:
//                            cardName = cardName + ": PicoPass 16K";
//                            break;
//                        case 0x1A:
//                            cardName = cardName + ": PicoPass 16KS";
//                            break;
//                        case 0x1B:
//                            cardName = cardName + ": PicoPass 16K(8x2)";
//                            break;
//                        case 0x1C:
//                            cardName = cardName + ": PicoPass 16KS(8x2)";
//                            break;
//                        case 0x1D:
//                            cardName = cardName + ": PicoPass 32KS(16+16)";
//                            break;
//                        case 0x1E:
//                            cardName = cardName + ": PicoPass 32KS(16+8x2)";
//                            break;
//                        case 0x1F:
//                            cardName = cardName + ": PicoPass 32KS(8x2+16)";
//                            break;
//                        case 0x20:
//                            cardName = cardName + ": PicoPass 32KS(8x2+8x2)";
//                            break;
//                        case 0x21:
//                            cardName = cardName + ": LRI64";
//                            break;
//                        case 0x22:
//                            cardName = cardName + ": I.CODE UID";
//                            break;
//                        case 0x23:
//                            cardName = cardName + ": I.CODE EPC";
//                            break;
//                        case 0x24:
//                            cardName = cardName + ": LRI12";
//                            break;
//                        case 0x25:
//                            cardName = cardName + ": LRI128";
//                            break;
//                        case 0x26:
//                            cardName = cardName + ": Mifare Mini";
//                            break;
//                    }
//                } else {
//                    if (ATRVal[13] == 0xFF) {
//                        switch (ATRVal[14]) {
//                            case 9:
//                                cardName = cardName + ": Mifare Mini";
//                                break;
//                        }
//                    }
//                }
//                displayOut(3, 0, cardName + " is detected.");
//            }
//        }
//
//        if (ATRLen[0] == 11) {
//            RIDVal = "";
//            for (int i = 4; i < 9; i++) {
//                RIDVal = RIDVal + Integer.toHexString(((Byte) ATRVal[i]).intValue() & 0xFF);
//            }
//
//            if (RIDVal.equals("067577810280")) {
//                displayOut(3, 0, "Mifare DESFire is detected.");
//            }
//        }
//
//        if (ATRLen[0] == 17) {
//            RIDVal = "";
//            for (int i = 4; i < 15; i++) {
//                RIDVal = RIDVal + Integer.toHexString(((Byte) ATRVal[i]).intValue() & 0xFF);
//            }
//
//            if (RIDVal.equals("50122345561253544E3381C3")) {
//                displayOut(3, 0, "ST19XRC8E is detected.");
//            }
//        }
//    }
//
//    public void createFile() {
//        byte[] tmpArray = new byte[56];
//
//        retCode = submitIC();
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//
//        retCode = selectFile((byte) 0xFF, (byte) 0x2);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//        displayOut(0, 0, "FF 02 is updated");
//
//        tmpArray[0] = (byte) 0x0;      // 00    Option registers
//        tmpArray[1] = (byte) 0x0;      // 00    Security option register
//        tmpArray[2] = (byte) 0x3;      // 03    No of user files
//        tmpArray[3] = (byte) 0x0;      // 00    Personalization bit
//
//        retCode = writeRecord((byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x04, tmpArray);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//
//        retCode = selectFile((byte) 0xFF, (byte) 0x4);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//        displayOut(0, 0, "Select FF 04");
//
//        retCode = submitIC();
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//
//        // Write to first record of FF 04
//        tmpArray[0] = (byte) 0x0F;       // 15    Record length
//        tmpArray[1] = (byte) 0x02;       // 2     No of records
//        tmpArray[2] = (byte) 0x00;       // 00    Read security attribute
//        tmpArray[3] = (byte) 0x00;       // 00    Write security attribute
//        tmpArray[4] = (byte) 0xDD;       // DD    File identifier
//        tmpArray[5] = (byte) 0x44;       // 44    File identifier
//        tmpArray[6] = (byte) 0x00;
//
//        retCode = writeRecord((byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x06, tmpArray);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//        displayOut(0, 0, "User File DD 44 is defined");
//
//        // Select User File DD 44
//        retCode = selectFile((byte) 0xDD, (byte) 0x44);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//        displayOut(0, 0, "User File DD 44 is selected");
//    }
//
//    public void getFirmWare() {
//        String tmpStr = "", tmpHex = "", tmpData = "";
//
//        clearBuffers();
//        SendLen = 0;
//        RecvLen[0] = 100;
//
//        retCode = jacs.jSCardControl(hCard,
//                ACSModule.IOCTL_SMARTCARD_GET_FIRMWARE_VERSION,
//                SendBuff,
//                SendLen,
//                RecvBuff,
//                RecvLen,
//                nBytesRet);
//
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            displayOut(1, retCode, "");
//            return;
//        } else {
//            tmpStr = "";
//            for (int i = 0; i < 6; i++) {
//                tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//                //For single character hex
//                if (tmpHex.length() == 1) {
//                    tmpHex = "0" + tmpHex;
//                }
//                tmpStr += " " + tmpHex;
//            }
//
//            displayOut(3, 0, "SCardControl: 20 78");
//            displayOut(3, 0, "Response: " + tmpStr.trim());
//
//            byte v1 = (byte) ((RecvBuff[2] << 0x08) | RecvBuff[3]);
//            tmpStr = Integer.toHexString(((Byte) RecvBuff[2]).intValue() & 0xFF).toUpperCase() + Integer.toHexString(v1 & 0xFF).toUpperCase();
//
//            byte v2 = (byte) ((RecvBuff[4] << 0x08) | RecvBuff[5]);
//            tmpData = Integer.toHexString(((Byte) RecvBuff[2]).intValue() & 0xFF).toUpperCase() + Integer.toHexString(v2 & 0xFF).toUpperCase();
//
//            displayOut(3, 0, "Firmware Version: ACR83: 0x" + tmpStr.trim() + ", S3C72M9: 0x" + tmpData.trim());
//        }
//    }
//
//    private void writeToFile(String key, String caseNumber) {
//        byte hiByte = 0, loByte = 0;
//        String tmpStr = "", tmpHex = "", chkStr = "";
//        byte[] tmpArray = new byte[56];
//
//        // write to file 1 (DD 44)
//        hiByte = (byte) 0xDD;
//        loByte = (byte) 0x44;
//        chkStr = "91 00";
//        byte lenByte = (byte) 0x0F;
//
//        retCode = selectFile(hiByte, loByte);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//
//        for (int i = 0; i < 2; i++) {
//            tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//            //For single character hex
//            if (tmpHex.length() == 1) {
//                tmpHex = "0" + tmpHex;
//            }
//            tmpStr += " " + tmpHex;
//        }
//
//        if (tmpStr.indexOf(chkStr) < 0) {
//            displayOut(2, 0, "The return string is invalid. Value: " + tmpStr);
//            retCode = INVALID_SW1SW2;
//            return;
//        }
//
//        // 3. Write input data to card
//        tmpArray = key.getBytes();
//        retCode = writeRecord(1, (byte) 0x00, lenByte, (byte) key.length(), tmpArray);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//        displayOut(0, 0, "Data : " + key + " is written to card.");
//
//        // 3. Write input data to card
//        tmpArray = caseNumber.getBytes();
//        retCode = writeRecord(1, (byte) 0x01, lenByte, (byte) caseNumber.length(), tmpArray);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//        displayOut(0, 0, "Data : " + caseNumber + " is written to card.");
//    }
//
//    private void readFromFile() {
//        byte hiByte = 0, loByte = 0, lenByte = 0;
//        String tmpStr = "", tmpHex = "", chkStr = "";
//
//        //select user file AA 11
//        hiByte = (byte) 0xDD;
//        loByte = (byte) 0x44;
//        chkStr = "91 00";
//        lenByte = (byte) 0x0F;
//
//        retCode = selectFile(hiByte, loByte);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//
//        for (int i = 0; i < 2; i++) {
//            tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//            //For single character hex
//            if (tmpHex.length() == 1) {
//                tmpHex = "0" + tmpHex;
//            }
//            tmpStr += " " + tmpHex;
//        }
//
//        if (tmpStr.indexOf(chkStr) < 0) {
//            displayOut(2, 0, "The return string is invalid. Value: " + tmpStr);
//            retCode = INVALID_SW1SW2;
//            return;
//        }
//
//        //read first record of user file selected
//        retCode = readRecord((byte) 0x00, (byte) lenByte);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//
//        tmpStr = "";
//        int indx = 0;
//        while (RecvBuff[indx] != (byte) 0x00) {
//            // if (indx < txtVal.getText().length())
//            tmpStr = tmpStr + (char) RecvBuff[indx];
//            indx++;
//        }
//        displayOut(0, 0, "Data read is : " + tmpStr);
//
//        //read second record of user file selected
//        retCode = readRecord((byte) 0x01, (byte) lenByte);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return;
//        }
//
//        tmpStr = "";
//        indx = 0;
//        while (RecvBuff[indx] != (byte) 0x00) {
//            // if (indx < txtVal.getText().length())
//            tmpStr = tmpStr + (char) RecvBuff[indx];
//            indx++;
//        }
//        displayOut(0, 0, "Data read is : " + tmpStr);
//    }
//
//    private int readRecord(byte recNo, byte dataLen) {
//        clearBuffers();
//
//        SendBuff[0] = (byte) 0x80;        // CLA
//        SendBuff[1] = (byte) 0xB2;        // INS
//        SendBuff[2] = recNo;             // P1    Record No
//        SendBuff[3] = (byte) 0x00;        // P2
//        SendBuff[4] = dataLen;           // P3    Length of data to be read
//        SendLen = 5;
//        RecvLen[0] = SendBuff[4] + 2;
//
//        String tmpStr = "", tmpHex = "";
//        for (int i = 0; i < SendLen; i++) {
//            tmpHex = Integer.toHexString(((Byte) SendBuff[i]).intValue() & 0xFF).toUpperCase();
//            //For single character hex
//            if (tmpHex.length() == 1) {
//                tmpHex = "0" + tmpHex;
//            }
//            tmpStr += " " + tmpHex;
//        }
//
//        retCode = sendAPDUandDisplay(0, tmpStr);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return retCode;
//        }
//
//        tmpStr = "";
//        for (int i = RecvLen[0] - 2; i < RecvLen[0]; i++) {
//            tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//            //For single character hex
//            if (tmpHex.length() == 1) {
//                tmpHex = "0" + tmpHex;
//            }
//            tmpStr += " " + tmpHex;
//        }
//
//        if (tmpStr.indexOf("90 00") < 0) {
//            displayOut(2, 0, "The return string is invalid. Value: " + tmpStr);
//            retCode = INVALID_SW1SW2;
//        }
//        return retCode;
//    }
//
//    private int writeRecord(int caseType, byte recNo, byte maxDataLen, byte dataLen, byte[] dataIn) {
//        String tmpStr = "", tmpHex = "";
//        // If card data is to be erased before writing new data
//        if (caseType == 1) {
//
//            clearBuffers();
//            SendBuff[0] = (byte) 0x80;          // CLA
//            SendBuff[1] = (byte) 0xD2;          // INS
//            SendBuff[2] = recNo;        	// P1    Record to be written
//            SendBuff[3] = (byte) 0x00;          // P2
//            SendBuff[4] = maxDataLen;           // P3    Length
//
//            for (int i = 0; i < maxDataLen; i++) {
//                SendBuff[i + 5] = (byte) 0x00;
//            }
//
//            SendLen = maxDataLen + 5;
//            RecvLen[0] = 2;
//
//            for (int i = 0; i < SendLen; i++) {
//                tmpHex = Integer.toHexString(((Byte) SendBuff[i]).intValue() & 0xFF).toUpperCase();
//                //For single character hex
//                if (tmpHex.length() == 1) {
//                    tmpHex = "0" + tmpHex;
//                }
//                tmpStr += " " + tmpHex;
//            }
//
//            retCode = sendAPDUandDisplay(0, tmpStr);
//            if (retCode != ACSModule.SCARD_S_SUCCESS) {
//                return retCode;
//            }
//
//            tmpStr = "";
//            for (int i = 0; i < 2; i++) {
//                tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//                //For single character hex
//                if (tmpHex.length() == 1) {
//                    tmpHex = "0" + tmpHex;
//                }
//                tmpStr += " " + tmpHex;
//            }
//
//            if (tmpStr.indexOf("90 00") < 0) {
//                displayOut(0, 0, "Return string is invalid. Value: " + tmpStr);
//                return INVALID_SW1SW2;
//            }
//        }
//
//        //write data to card
//        clearBuffers();
//        SendBuff[0] = (byte) 0x80;          // CLA
//        SendBuff[1] = (byte) 0xD2;          // INS
//        SendBuff[2] = recNo;                // P1    Record to be written
//        SendBuff[3] = (byte) 0x00;          // P2
//        SendBuff[4] = dataLen;              // P3    Length
//        for (int i = 0; i < dataLen; i++) {
//            SendBuff[i + 5] = dataIn[i];
//        }
//
//        SendLen = dataLen + 5;
//        RecvLen[0] = 2;
//
//        tmpStr = "";
//        for (int i = 0; i < SendLen; i++) {
//            tmpHex = Integer.toHexString(((Byte) SendBuff[i]).intValue() & 0xFF).toUpperCase();
//            //For single character hex
//            if (tmpHex.length() == 1) {
//                tmpHex = "0" + tmpHex;
//            }
//            tmpStr += " " + tmpHex;
//        }
//
//        retCode = sendAPDUandDisplay(0, tmpStr);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return retCode;
//        }
//
//        tmpStr = "";
//        for (int i = 0; i < 2; i++) {
//            tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//            //For single character hex
//            if (tmpHex.length() == 1) {
//                tmpHex = "0" + tmpHex;
//            }
//            tmpStr += " " + tmpHex;
//        }
//
//        if (tmpStr.indexOf("90 00") < 0) {
//            displayOut(0, 0, "Return string is invalid. Value: " + tmpStr);
//            return INVALID_SW1SW2;
//        }
//
//        return retCode;
//    }
//
//    private int selectFile(byte hiAddr, byte loAddr) {
//
//        String tmpStr = "", tmpHex = "";
//
//        clearBuffers();
//        SendBuff[0] = (byte) 0x80;
//        SendBuff[1] = (byte) 0xA4;
//        SendBuff[2] = (byte) 0x00;
//        SendBuff[3] = (byte) 0x00;
//        SendBuff[4] = (byte) 0x02;
//        SendBuff[5] = (byte) hiAddr;
//        SendBuff[6] = (byte) loAddr;
//        SendLen = 7;
//        RecvLen[0] = 2;
//
//        for (int i = 0; i < SendLen; i++) {
//            tmpHex = Integer.toHexString(((Byte) SendBuff[i]).intValue() & 0xFF).toUpperCase();
//            //For single character hex
//            if (tmpHex.length() == 1) {
//                tmpHex = "0" + tmpHex;
//            }
//            tmpStr += " " + tmpHex;
//        }
//
//        retCode = sendAPDUandDisplay(0, tmpStr);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return retCode;
//        }
//
//        return retCode;
//    }
//
//    private int submitIC() {
//        clearBuffers();
//
//        SendBuff[0] = (byte) 0x80;        // CLA
//        SendBuff[1] = (byte) 0x20;        // INS
//        SendBuff[2] = (byte) 0x07;        // P1
//        SendBuff[3] = (byte) 0x0;         // P2
//        SendBuff[4] = (byte) 0x08;        // P3
//        SendBuff[5] = (byte) 0x41;        // A
//        SendBuff[6] = (byte) 0x43;        // C
//        SendBuff[7] = (byte) 0x4F;        // O
//        SendBuff[8] = (byte) 0x53;        // S
//        SendBuff[9] = (byte) 0x54;        // T
//        SendBuff[10] = (byte) 0x45;       // E
//        SendBuff[11] = (byte) 0x53;       // S
//        SendBuff[12] = (byte) 0x54;       // T
//
//        SendLen = 13;
//        RecvLen[0] = 2;
//        String tmpStr = "", tmpHex = "";
//
//        for (int i = 0; i < SendLen; i++) {
//            tmpHex = Integer.toHexString(((Byte) SendBuff[i]).intValue() & 0xFF).toUpperCase();
//            //For single character hex
//            if (tmpHex.length() == 1) {
//                tmpHex = "0" + tmpHex;
//            }
//            tmpStr += " " + tmpHex;
//        }
//
//        retCode = sendAPDUandDisplay(0, tmpStr);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return retCode;
//        }
//
//        tmpStr = "";
//        for (int i = 0; i < 2; i++) {
//            tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//            //For single character hex
//            if (tmpHex.length() == 1) {
//                tmpHex = "0" + tmpHex;
//            }
//            tmpStr += " " + tmpHex;
//        }
//
//        if (tmpStr.indexOf("90 00") < 0) {
//            displayOut(0, 0, "Return string is invalid. Value: " + tmpStr);
//            return INVALID_SW1SW2;
//        }
//
//        return retCode;
//    }
//
//    private int sendAPDUandDisplay(int sendType, String ApduIn) {
//        ACSModule.SCARD_IO_REQUEST IO_REQ = new ACSModule.SCARD_IO_REQUEST();
//        ACSModule.SCARD_IO_REQUEST IO_REQ_Recv = new ACSModule.SCARD_IO_REQUEST();
//        IO_REQ.dwProtocol = PrefProtocols[0];
//        IO_REQ.cbPciLength = 8;
//        IO_REQ_Recv.dwProtocol = PrefProtocols[0];
//        IO_REQ_Recv.cbPciLength = 8;
//        displayOut(2, 0, ApduIn);
//        String tmpStr = "", tmpHex = "";
//        RecvLen[0] = 262;
//
//        retCode = jacs.jSCardTransmit(hCard,
//                IO_REQ,
//                SendBuff,
//                SendLen,
//                null,
//                RecvBuff,
//                RecvLen);
//
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            displayOut(1, retCode, "");
//            return retCode;
//        } else {
//            switch (sendType) {
//                case 0:
//                    for (int i = 0; i < RecvLen[0]; i++) {
//                        tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//                        //For single character hex
//                        if (tmpHex.length() == 1) {
//                            tmpHex = "0" + tmpHex;
//                        }
//                        tmpStr += " " + tmpHex;
//                    }
//                    break;
//
//                case 1:
//                    for (int i = RecvLen[0] - 1; i < RecvLen[0]; i++) {
//                        tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//                        //For single character hex
//                        if (tmpHex.length() == 1) {
//                            tmpHex = "0" + tmpHex;
//                        }
//                        tmpStr += " " + tmpHex;
//                    }
//                    if (tmpStr.indexOf("90 00") < 0) {
//                        displayOut(1, 0, "Return bytes are not acceptable.");
//                    } else {
//                        tmpStr = "ATR: ";
//                        for (int i = 0; i < RecvLen[0] - 2; i++) {
//                            tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//                            //For single character hex
//                            if (tmpHex.length() == 1) {
//                                tmpHex = "0" + tmpHex;
//                            }
//                            tmpStr += " " + tmpHex;
//                        }
//                    }
//                    break;
//
//                case 2:
//                    for (int i = RecvLen[0] - 1; i < RecvLen[0]; i++) {
//                        tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//                        //For single character hex
//                        if (tmpHex.length() == 1) {
//                            tmpHex = "0" + tmpHex;
//                        }
//                        tmpStr += " " + tmpHex;
//                    }
//                    if (tmpStr.indexOf("90 00") < 0) {
//                        displayOut(1, 0, "Return bytes are not acceptable.");
//                    } else {
//                        tmpStr = "ATR: ";
//                        for (int i = 0; i < RecvLen[0] - 2; i++) {
//                            tmpHex = Integer.toHexString(((Byte) RecvBuff[i]).intValue() & 0xFF).toUpperCase();
//                            //For single character hex
//                            if (tmpHex.length() == 1) {
//                                tmpHex = "0" + tmpHex;
//                            }
//                            tmpStr += " " + tmpHex;
//                        }
//                    }
//                    break;
//            }
//
//            displayOut(3, 0, tmpStr);
//        }
//        return retCode;
//    }
//
//    private void clearBuffers() {
//        for (int i = 0; i < 300; i++) {
//            SendBuff[i] = 0x00;
//            RecvBuff[i] = 0x00;
//        }
//    }
//
//    public boolean checkCard() {
//        ACSModule.SCARD_READERSTATE readerState = new ACSModule.SCARD_READERSTATE();
//        String deviceName = init();
//
//        if ( deviceName == null ) {
//            return false ;
//        }
//
//        readerState.RdrName = deviceName;
//        retCode = jacs.jSCardGetStatusChange(hContext,
//                0,
//                readerState,
//                1);
//
//        //JOptionPane.showMessageDialog(this, retCode);
//        if (retCode != ACSModule.SCARD_S_SUCCESS) {
//            return false;
//
//        } else {
//            if ((readerState.RdrEventState / 32) % 2 != 0) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//    }

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
