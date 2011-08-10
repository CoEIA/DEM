/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * OfflineMinningFrame.java
 *
 * Created on Apr 28, 2010, 11:10:01 AM
 */

package edu.coeia.gui;

import edu.coeia.gui.component.GUIComponent ;
import edu.coeia.gui.component.IndexGUIComponent;

import edu.coeia.internet.IEHandler;
import edu.coeia.internet.MozillaHandler;

import edu.coeia.utility.Utilities;
import edu.coeia.utility.FilesPath ;
import edu.coeia.utility.FilesFilter ;
import edu.coeia.utility.Tuple ;
import edu.coeia.utility.ImageLabel;
import edu.coeia.utility.MetaDataExtraction ;
import edu.coeia.utility.FireFoxHTMLReportGenerator;
import edu.coeia.utility.GPSData;
import edu.coeia.utility.GeoTagging;

import edu.coeia.cases.Case;

import edu.coeia.chat.MSNParser;
import edu.coeia.chat.YahooMessage ;
import edu.coeia.chat.YahooMessageDecoder;
import edu.coeia.chat.YahooMessageReader;
import edu.coeia.chat.SkypeMessage;
import edu.coeia.chat.SkypeParser;

import edu.coeia.search.PSTSearcher;
import edu.coeia.internet.InternetSummaryDate ;
import edu.coeia.image.ImageViewer;

import edu.coeia.email.EmailReaderThread;
import edu.coeia.email.MessageHeader ;

import java.awt.CardLayout ;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage ;
import java.awt.Graphics2D ;
import java.awt.AlphaComposite ;
import java.awt.Image ;
import java.awt.Toolkit ;
import java.awt.event.InputEvent ;
import java.awt.Desktop ;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.border.TitledBorder;
import javax.swing.JFileChooser ;
import javax.swing.JTextField ;
import javax.swing.JComboBox ;
import javax.swing.JOptionPane ;
import javax.swing.JTable ;
import javax.swing.RowFilter ;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter ;
import javax.swing.table.TableModel ;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.DefaultTreeModel ;
import javax.swing.ListSelectionModel ;
import javax.swing.event.ListSelectionListener ;
import javax.swing.event.ListSelectionEvent ;
import javax.swing.event.TreeSelectionListener ;
import javax.swing.event.TreeSelectionEvent ;
import javax.swing.JPanel ;
import javax.swing.JFrame ;
import javax.swing.event.DocumentEvent ;
import javax.swing.event.DocumentListener ;
import javax.swing.JLabel;
import javax.swing.ImageIcon ;
import javax.imageio.ImageIO;

import java.io.File ;
import java.io.FileNotFoundException ;
import java.io.IOException ;
import java.io.FilenameFilter ;

import java.util.List; 
import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.Vector ;
import java.util.Iterator ;
import java.util.Map ;
import java.util.Set ;
import java.util.Date ;
import java.util.regex.PatternSyntaxException;

import java.sql.SQLException ;

import java.net.URISyntaxException ;
import java.net.URI ;

import com.pff.PSTFile ;
import com.pff.PSTFolder ;
import com.pff.PSTMessage ;
import com.pff.PSTContact ;
import com.pff.PSTTask ;
import com.pff.PSTActivity ;
import com.pff.PSTRss ;
import com.pff.PSTMessageStore ;
import com.pff.PSTException;
import com.pff.PSTObject ;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

import org.mcavallo.opencloud.Cloud ;
import org.mcavallo.opencloud.Tag ;

import com.toedter.calendar.JDateChooser;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.document.Document;

/**
 *
 * @author wajdyessam
 *
 */

public class OfflineMinningFrame extends javax.swing.JFrame {
    private JFileChooser fileChooser ;
    
    private JWebBrowser webBrowser = new JWebBrowser();
    private JWebBrowser fileBrowser = new JWebBrowser();

    // msn chat
    private DefaultMutableTreeNode rootMSNNode ;
    private MSNParser msnParser ;
    private JWebBrowser msnChat = new JWebBrowser();

    // yaho chat
    private DefaultMutableTreeNode rootYahooNode;
    private JWebBrowser yahooChat = new JWebBrowser();

    // skype Chat
    private DefaultMutableTreeNode rootSkypeNode ;
    
    private EmailTableModel emailTableModel;
    private DefaultMutableTreeNode top ;
    
    private Case index ;
    private PSTFile pstFile ;
    private JFrame mainFrame ;

    private List<String> imagesPath ;

    // index for image panel
    private int imageIndex  = 0;
    private int totalImagePage, currentImagePage ;

    private IndexerThread indexerThread ;

    private final String APPLICATION_NAME = "Digital Evidence Miner (Beta Version): ";
    private String applicationTitle;
    private boolean startIndexButtonFlag = true ;

    private List<String> listOfOpeningCase ;

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private FileHandler handler ;

    /** Creates new form OfflineMinningFrame */
    public OfflineMinningFrame(Case AIndex, boolean state, String title, List<String> list) {
        initComponents();

        try {
            handler = new FileHandler("GUI.log");
            logger.addHandler(handler);
            logger.log(Level.INFO, "DEM Main Frame");
        }
        catch (Exception e ) { logger.log(Level.SEVERE, "Uncaught exception", e);}
        
        Toolkit kit = Toolkit.getDefaultToolkit();
        this.setIconImage(kit.getImage(this.getClass().getResource("resources/dem-icon.png")));

        this.index = AIndex ;
        this.applicationTitle = "File System Search Window";
        this.listOfOpeningCase = list;
        
        // set frame resizable and set frame title
        this.setTitle(APPLICATION_NAME + applicationTitle);
        this.setResizable(true);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // configure file chooser to select files (txt)
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FilesFilter("Text Files (*.txt)", "txt"));
        
        // add document listsner for searching fields
        mozillaSearchField.getDocument().addDocumentListener(new MozillaInputListener());
        IESearchField.getDocument().addDocumentListener(new IEInputListener());
        emailSearchTextField.getDocument().addDocumentListener(new EmailSearchInputListener());
        cloudsFilterTextField.getDocument().addDocumentListener(new CloudsInputListener());
        summaryTextField.getDocument().addDocumentListener(new SummaryInputListener());
        
        // add a native web browser
        webBrowser.setBarsVisible(false);
        webBrowser.setStatusBarVisible(false);
        HTMLRenderPanel.add(webBrowser, BorderLayout.CENTER);

        // add file browser
        fileBrowser.setBarsVisible(false);
        fileBrowser.setStatusBarVisible(false);
        fileRenderPanel.add(fileBrowser, BorderLayout.CENTER);  

        //add a native web browser
        tagsPanel.setLayout(new WrapLayout());
        
        // jcalendar initilizing
        toDatePanel.add(new JDateChooser(new Date()),BorderLayout.CENTER);
        fromDatePanel.add(new JDateChooser(new Date(0)),BorderLayout.CENTER);

        // center integer values in tables
        Utilities.setTableAlignmentValue(cloudsTable, 1);
        Utilities.setTableAlignmentValue(inboxTable, 2);
        Utilities.setTableAlignmentValue(sentItemTable, 2);
        Utilities.setTableAlignmentValue(espTable, 1);
        Utilities.setTableAlignmentValue(locationTable, 1);
        Utilities.setTableAlignmentValue(summaryTable, 1);
        
        // set outlook data to outlook combobox
        for (String pstPath: index.getPstPath())
            outlookComboBox.addItem( pstPath );

        // set ff & IE data to thier combobox
        for (String ffPath: index.getFFPath() )
            ffComboBox.addItem(ffPath);

        for (String iePath: index.getIePath())
            ieComboBox.addItem(iePath);

        // set msn & yahoo & skype data to thier combobox
        for (String yahooPath: index.getYahooPath())
            yahooComboBox.addItem(yahooPath);

        for (String msnPath: index.getMsnPath())
            msnComboBox.addItem(msnPath);

        for (String skypePath: index.getSkypePath())
            skypeComboBox.addItem(skypePath);
        
        // yahoo chat display area
        yahooChatContentPanel.add(yahooChat, BorderLayout.CENTER);
        yahooChat.setBarsVisible(false);
        yahooChat.setStatusBarVisible(false);

        // msn chat display area
        msnChat.setBarsVisible(false);
        msnChat.setStatusBarVisible(false);
        msnChatContentPanel.add(msnChat, BorderLayout.CENTER);

        // craete image path list
        imagesPath = new ArrayList<String>();

        // wide the data in search table
        Utilities.packColumns(searchTable, 0);

        // diseable every component that will not indexing
        disableNotIndexedComponent();

        // set start and end button
        startIndexButton.setEnabled(startIndexButtonFlag);
        stopIndexingButton.setEnabled(! startIndexButtonFlag);
        
        // add close event
        this.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosed (WindowEvent event){
                doChecking();
            }

            @Override
            public void windowClosing (WindowEvent event){
                doChecking();
            }

            public void doChecking () {
                try {
                    // release all obejct in this object

                    if ( index != null ) {
                        String caseName = index.getIndexName() ;

                        if ( !caseName.isEmpty() )
                            listOfOpeningCase.remove(caseName);
                        }

                    if ( index != null ) {
                        index = null ;
                    }

                    if ( pstFile != null ) {
                        pstFile = null ;
                    }

                    if (  imagesPath != null ) {
                        imagesPath.clear();
                        imagesPath = null ;
                    }
                    
                    if ( indexerThread != null) {
                        indexerThread.cancel(true);
                        //indexerThread.closeIndex();
                        indexerThread = null ;
                        dispose();
                    }
                    else {
                    }
                }
                catch (Exception e){
                    logger.log(Level.SEVERE, "Uncaught exception", e);
                }
            }
        });

        // display indexing information if already indexing
        if ( index.getIndexStatus() ) {
            indexDateLbl.setText(index.getLastIndexDate());
            timeLbl.setText(index.getIndexingTime());
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

        indexGroupButton = new javax.swing.ButtonGroup();
        headerGroupButton = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        fileSystemToggleButton = new javax.swing.JToggleButton();
        emailToggleButton = new javax.swing.JToggleButton();
        internetSurfingToggleButton = new javax.swing.JToggleButton();
        chatToggleButton = new javax.swing.JToggleButton();
        imageViewerToggleButton = new javax.swing.JToggleButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        CardPanel = new javax.swing.JPanel();
        fileSystemPanel = new javax.swing.JPanel();
        fileSystemTappedPane = new javax.swing.JTabbedPane();
        IndexFileSystemPanel = new javax.swing.JPanel();
        indexFileSystemButtonsPanel = new javax.swing.JPanel();
        jToolBar4 = new javax.swing.JToolBar(javax.swing.JToolBar.VERTICAL);
        indexFilesToggleButton = new javax.swing.JToggleButton();
        textCloudsToggleButton = new javax.swing.JToggleButton();
        indexVisualizationToggleButton = new javax.swing.JToggleButton();
        indexCardsPanel = new javax.swing.JPanel();
        indexPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        jLabel23 = new javax.swing.JLabel();
        currentFileLbl = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        sizeOfFileLbl = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        numberOfFilesLbl = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        fileExtensionLbl = new javax.swing.JLabel();
        numberOfErrorFilesLbl = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        bigSizeMsgLbl = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        indexTable = new javax.swing.JTable();
        startIndexButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        timeLbl = new javax.swing.JLabel();
        indexDateLbl = new javax.swing.JLabel();
        stopIndexingButton = new javax.swing.JButton();
        textCloudsPanel = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        tagSelectButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        tagsNumberTextField = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        tagsExcludeTextField = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        tagsDisplayComboBox = new javax.swing.JComboBox();
        jPanel16 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane10 = new javax.swing.JScrollPane();
        tagsPanel = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane22 = new javax.swing.JScrollPane();
        cloudsTable = new javax.swing.JTable();
        jLabel33 = new javax.swing.JLabel();
        cloudsFilterTextField = new javax.swing.JTextField();
        indexVisualizingPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        indexVisulizingButton = new javax.swing.JButton();
        indexVisualizingPiePanel = new javax.swing.JPanel();
        SearchFileSystemPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        queryTextField = new javax.swing.JTextField();
        keywordsListButton = new javax.swing.JButton();
        startSearchingButton = new javax.swing.JButton();
        clearFieldsButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        searchProgressBard = new javax.swing.JProgressBar();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        searchTable = new javax.swing.JTable();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        clusterPathTree = new javax.swing.JTree();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane27 = new javax.swing.JScrollPane();
        clusterTypeTree = new javax.swing.JTree();
        jPanel6 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        searchTime = new javax.swing.JLabel();
        searchingDateLbl = new javax.swing.JLabel();
        indexDirLbl2 = new javax.swing.JLabel();
        userQueryLbl = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        searchFileNameLbl = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        searchFileExtensionLbl = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        searchFileSizeLbl = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        fileRenderPanel = new javax.swing.JPanel();
        FileMetaDataPanel = new javax.swing.JPanel();
        jScrollPane28 = new javax.swing.JScrollPane();
        metaDataTextArea = new javax.swing.JTextArea();
        emailPanel = new javax.swing.JPanel();
        emailTabbedPane = new javax.swing.JTabbedPane();
        outlookPanel = new javax.swing.JPanel();
        messagePanel = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        emailTable = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        emailSearchTextField = new javax.swing.JTextField();
        emailSearchButton = new javax.swing.JButton();
        renderPanel = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        HTMLRenderPanel = new javax.swing.JPanel();
        TXTRenderPanel = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        textEditorPane = new javax.swing.JEditorPane();
        messageHeaderPanel = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        headerEditorPane = new javax.swing.JEditorPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        outlookComboBox = new javax.swing.JComboBox();
        loadPstButton = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        clusteringTabbedPane = new javax.swing.JTabbedPane();
        jPanel26 = new javax.swing.JPanel();
        treeScrollPane = new javax.swing.JScrollPane();
        folderTree = new javax.swing.JTree(){
            public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                DefaultMutableTreeNode nodeValue = (DefaultMutableTreeNode)value;
                if (nodeValue.getUserObject() instanceof PSTFolder) {
                    PSTFolder folderValue = (PSTFolder)nodeValue.getUserObject();

                    return folderValue.getDescriptorNode().descriptorIdentifier+" - "+folderValue.getDisplayName()+" "+folderValue.getAssociateContentCount()+"";
                } else if (nodeValue.getUserObject() instanceof PSTMessageStore) {
                    PSTMessageStore folderValue = (PSTMessageStore)nodeValue.getUserObject();
                    return folderValue.getDisplayName();
                } else {
                    return value.toString();
                }
            }
        };
        outlookCorrelationsPanel = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        correlationComboBox = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        emailVisualizationButton = new javax.swing.JButton();
        toDatePanel = new javax.swing.JPanel();
        fromDatePanel = new javax.swing.JPanel();
        correlationResultPanel = new javax.swing.JPanel();
        inboxPanel = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        inboxTable = new javax.swing.JTable();
        sentItemPanel = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        sentItemTable = new javax.swing.JTable();
        espPanel = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        espTable = new javax.swing.JTable();
        locationPanel = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        locationTable = new javax.swing.JTable();
        messageFrequencyPanel = new javax.swing.JPanel();
        internetSurfingPanel = new javax.swing.JPanel();
        internetSurfingTappedPane = new javax.swing.JTabbedPane();
        summaryInternetPanel = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        summaryInternetButton = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        summaryTable = new javax.swing.JTable();
        jLabel30 = new javax.swing.JLabel();
        summaryTextField = new javax.swing.JTextField();
        mozillaPanel = new javax.swing.JPanel();
        mozillaButtonsPanel = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar(javax.swing.JToolBar.VERTICAL);
        ffSummaryButtton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        webHistoryButton = new javax.swing.JButton();
        bookmarButton = new javax.swing.JButton();
        cookiesButton = new javax.swing.JButton();
        downloadButton = new javax.swing.JButton();
        logginsButton = new javax.swing.JButton();
        mozillaResultPanel = new javax.swing.JPanel();
        webHistoryPanel = new javax.swing.JPanel();
        webHistoryScrollPane = new javax.swing.JScrollPane();
        webHistoryTable = new edu.coeia.gui.component.TableWithToolTip();
        bookmarkHistory = new javax.swing.JPanel();
        bookmarkScrollPane = new javax.swing.JScrollPane();
        bookmarkTable = new edu.coeia.gui.component.TableWithToolTip();
        cookiesPanel = new javax.swing.JPanel();
        cookiesScrollPane = new javax.swing.JScrollPane();
        cookiesTable = new edu.coeia.gui.component.TableWithToolTip();
        downloadPanel = new javax.swing.JPanel();
        downloadScrollPane = new javax.swing.JScrollPane();
        downloadTable = new edu.coeia.gui.component.TableWithToolTip();
        logginsPanel = new javax.swing.JPanel();
        logginsScrollPane = new javax.swing.JScrollPane();
        logginsTable = new edu.coeia.gui.component.TableWithToolTip();
        ffSummaryPanel = new javax.swing.JPanel();
        ffSummaryDataPanel = new javax.swing.JPanel();
        ffSumarryTappnedPane = new javax.swing.JTabbedPane();
        topHostPanel = new javax.swing.JPanel();
        jScrollPane24 = new javax.swing.JScrollPane();
        topHostTable = new javax.swing.JTable();
        topURLPanel = new javax.swing.JPanel();
        jScrollPane25 = new javax.swing.JScrollPane();
        topURLTable = new javax.swing.JTable();
        searchEnginePanel = new javax.swing.JPanel();
        jScrollPane26 = new javax.swing.JScrollPane();
        searchEngineTable = new javax.swing.JTable();
        ffSummaryButtonsPanel = new javax.swing.JPanel();
        ffViewHTMLReportButton = new javax.swing.JButton();
        ffVisualizingVisitedHostButton = new javax.swing.JButton();
        mozillaSearchPanel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        mozillaSearchField = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        mozillaFilterComboBox = new javax.swing.JComboBox();
        jPanel12 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        ffComboBox = new javax.swing.JComboBox();
        loadFFButton = new javax.swing.JButton();
        IEPanel = new javax.swing.JPanel();
        IEButtonsPanel = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar(javax.swing.JToolBar.VERTICAL);
        webHistoryButton1 = new javax.swing.JButton();
        bookmarButton1 = new javax.swing.JButton();
        cookiesButton1 = new javax.swing.JButton();
        cacheButton = new javax.swing.JButton();
        logginsButton1 = new javax.swing.JButton();
        IESearchPanel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        IESearchField = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        IEFilterComboBox = new javax.swing.JComboBox();
        IEResultPanel = new javax.swing.JPanel();
        IEWebHistoryPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        IEWebHistoryTable = new edu.coeia.gui.component.TableWithToolTip();
        IEBookmarkPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        IEBookmarkTable = new edu.coeia.gui.component.TableWithToolTip();
        IECookiesPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        IECookiesTable = new edu.coeia.gui.component.TableWithToolTip();
        IECachePanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        IECacheTable = new edu.coeia.gui.component.TableWithToolTip();
        IELogginsPanel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        IELogginsTable = new edu.coeia.gui.component.TableWithToolTip();
        jPanel13 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        ieComboBox = new javax.swing.JComboBox();
        loadIEButton = new javax.swing.JButton();
        ChatPanel = new javax.swing.JPanel();
        chatPanelTappedPane = new javax.swing.JTabbedPane();
        WindowsLivePanel = new javax.swing.JPanel();
        msnChatContentPanel = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        msnChatTree = new javax.swing.JTree();
        jPanel14 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        msnComboBox = new javax.swing.JComboBox();
        loadMSNButton = new javax.swing.JButton();
        yahooMessangerPanel = new javax.swing.JPanel();
        jScrollPane21 = new javax.swing.JScrollPane();
        yahooChatTree = new javax.swing.JTree();
        yahooChatContentPanel = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        yahooComboBox = new javax.swing.JComboBox();
        loadYahooButton = new javax.swing.JButton();
        skypePanel = new javax.swing.JPanel();
        skypeChatContentPanel = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        skypeTable = new javax.swing.JTable();
        jScrollPane23 = new javax.swing.JScrollPane();
        skypeChatTree = new javax.swing.JTree();
        jPanel20 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        skypeComboBox = new javax.swing.JComboBox();
        loadSkypeButton = new javax.swing.JButton();
        ImagesViewerPanel = new javax.swing.JPanel();
        imagePanel = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        showImagesButton = new javax.swing.JButton();
        nextPageButton = new javax.swing.JButton();
        prePageButton = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        imagePathLabel = new javax.swing.JLabel();
        imageSizeLabel = new javax.swing.JLabel();
        imageDateLabel = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        geoTagLbl = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        windowsMenuItem = new javax.swing.JMenuItem();
        recentMenuItem = new javax.swing.JMenuItem();
        aboutMenu = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Digital Evidence Miner ");
        setIconImages(null);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        headerGroupButton.add(fileSystemToggleButton);
        fileSystemToggleButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        fileSystemToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/search.png"))); // NOI18N
        fileSystemToggleButton.setText("File System Search");
        fileSystemToggleButton.setFocusable(false);
        fileSystemToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        fileSystemToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        fileSystemToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileSystemToggleButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(fileSystemToggleButton);

        headerGroupButton.add(emailToggleButton);
        emailToggleButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        emailToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/email.png"))); // NOI18N
        emailToggleButton.setText("Email Search");
        emailToggleButton.setFocusable(false);
        emailToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        emailToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        emailToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailToggleButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(emailToggleButton);

        headerGroupButton.add(internetSurfingToggleButton);
        internetSurfingToggleButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        internetSurfingToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/webButton.png"))); // NOI18N
        internetSurfingToggleButton.setText("Internet Surfing");
        internetSurfingToggleButton.setFocusable(false);
        internetSurfingToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        internetSurfingToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        internetSurfingToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                internetSurfingToggleButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(internetSurfingToggleButton);

        headerGroupButton.add(chatToggleButton);
        chatToggleButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        chatToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/Chat.png"))); // NOI18N
        chatToggleButton.setText("Instant Chat Search");
        chatToggleButton.setFocusable(false);
        chatToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        chatToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        chatToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chatToggleButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(chatToggleButton);

        headerGroupButton.add(imageViewerToggleButton);
        imageViewerToggleButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        imageViewerToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/kview.png"))); // NOI18N
        imageViewerToggleButton.setText("Images Viewer");
        imageViewerToggleButton.setFocusable(false);
        imageViewerToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        imageViewerToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        imageViewerToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageViewerToggleButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(imageViewerToggleButton);

        jLabel10.setText("                                                     ");
        jToolBar1.add(jLabel10);

        jLabel39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/DLogo.JPG"))); // NOI18N
        jLabel39.setText(" ");
        jToolBar1.add(jLabel39);

        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/coeiaLogo.JPG"))); // NOI18N
        jLabel40.setText(" ");
        jToolBar1.add(jLabel40);

        CardPanel.setLayout(new java.awt.CardLayout());

        indexFileSystemButtonsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        indexGroupButton.add(indexFilesToggleButton);
        indexFilesToggleButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        indexFilesToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/help_index.png"))); // NOI18N
        indexFilesToggleButton.setText("Index File System");
        indexFilesToggleButton.setFocusable(false);
        indexFilesToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        indexFilesToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        indexFilesToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                indexFilesToggleButtonActionPerformed(evt);
            }
        });
        jToolBar4.add(indexFilesToggleButton);

        indexGroupButton.add(textCloudsToggleButton);
        textCloudsToggleButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        textCloudsToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/old-edit-find.png"))); // NOI18N
        textCloudsToggleButton.setText("Index Tags Cloud");
        textCloudsToggleButton.setFocusable(false);
        textCloudsToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        textCloudsToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        textCloudsToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textCloudsToggleButtonActionPerformed(evt);
            }
        });
        jToolBar4.add(textCloudsToggleButton);

        indexGroupButton.add(indexVisualizationToggleButton);
        indexVisualizationToggleButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        indexVisualizationToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/pie_chart.png"))); // NOI18N
        indexVisualizationToggleButton.setText("Index Visualization");
        indexVisualizationToggleButton.setFocusable(false);
        indexVisualizationToggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        indexVisualizationToggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        indexVisualizationToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                indexVisualizationToggleButtonActionPerformed(evt);
            }
        });
        jToolBar4.add(indexVisualizationToggleButton);

        javax.swing.GroupLayout indexFileSystemButtonsPanelLayout = new javax.swing.GroupLayout(indexFileSystemButtonsPanel);
        indexFileSystemButtonsPanel.setLayout(indexFileSystemButtonsPanelLayout);
        indexFileSystemButtonsPanelLayout.setHorizontalGroup(
            indexFileSystemButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 137, Short.MAX_VALUE)
        );
        indexFileSystemButtonsPanelLayout.setVerticalGroup(
            indexFileSystemButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar4, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
        );

        indexCardsPanel.setLayout(new java.awt.CardLayout());

        indexPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Index File System", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel23.setText("Current File:");

        currentFileLbl.setFont(new java.awt.Font("Tahoma", 1, 11));
        currentFileLbl.setForeground(new java.awt.Color(0, 0, 255));
        currentFileLbl.setText(" ");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel25.setText("File Size:");

        sizeOfFileLbl.setFont(new java.awt.Font("Tahoma", 1, 11));
        sizeOfFileLbl.setForeground(new java.awt.Color(0, 0, 255));
        sizeOfFileLbl.setText(" ");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel27.setText("Number of Files in Index:");

        numberOfFilesLbl.setFont(new java.awt.Font("Tahoma", 1, 11));
        numberOfFilesLbl.setForeground(new java.awt.Color(0, 0, 255));
        numberOfFilesLbl.setText(" ");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel24.setText("File Extension:");

        fileExtensionLbl.setFont(new java.awt.Font("Tahoma", 1, 11));
        fileExtensionLbl.setForeground(new java.awt.Color(0, 0, 255));
        fileExtensionLbl.setText(" ");

        numberOfErrorFilesLbl.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        numberOfErrorFilesLbl.setForeground(new java.awt.Color(0, 0, 255));
        numberOfErrorFilesLbl.setText(" ");

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel41.setText("Number of Files Cannot Indexed:");

        bigSizeMsgLbl.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        bigSizeMsgLbl.setForeground(new java.awt.Color(255, 0, 0));
        bigSizeMsgLbl.setText(" ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bigSizeMsgLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 978, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel27)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(numberOfFilesLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(66, 66, 66)
                                        .addComponent(sizeOfFileLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(83, 83, 83)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(fileExtensionLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                                    .addComponent(numberOfErrorFilesLbl, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(currentFileLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 890, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 968, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(currentFileLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(sizeOfFileLbl)
                    .addComponent(fileExtensionLbl)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(numberOfFilesLbl)
                    .addComponent(jLabel41)
                    .addComponent(numberOfErrorFilesLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bigSizeMsgLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "logging", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        indexTable.setAutoCreateRowSorter(true);
        indexTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "File Type", "File Path", "Indexing Status"
            }
        ));
        jScrollPane1.setViewportView(indexTable);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 978, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addContainerGap())
        );

        startIndexButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        startIndexButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/database.png"))); // NOI18N
        startIndexButton.setText("Start Indexing");
        startIndexButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startIndexButtonActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Indexing History", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setText("Last Indexing Date:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setText("Indexing Finishing Time:");

        timeLbl.setFont(new java.awt.Font("Tahoma", 1, 11));
        timeLbl.setForeground(new java.awt.Color(0, 0, 255));
        timeLbl.setText(" ");

        indexDateLbl.setFont(new java.awt.Font("Tahoma", 1, 11));
        indexDateLbl.setForeground(new java.awt.Color(0, 0, 255));
        indexDateLbl.setText(" ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(indexDateLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                .addGap(87, 87, 87)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(timeLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(424, 424, 424))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(indexDateLbl))
                    .addComponent(timeLbl)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        stopIndexingButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        stopIndexingButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/cancel.png"))); // NOI18N
        stopIndexingButton.setText("Stop Indexing");
        stopIndexingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopIndexingButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout indexPanelLayout = new javax.swing.GroupLayout(indexPanel);
        indexPanel.setLayout(indexPanelLayout);
        indexPanelLayout.setHorizontalGroup(
            indexPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(indexPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(indexPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(indexPanelLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
            .addGroup(indexPanelLayout.createSequentialGroup()
                .addGap(214, 214, 214)
                .addComponent(startIndexButton, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(stopIndexingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(294, Short.MAX_VALUE))
        );
        indexPanelLayout.setVerticalGroup(
            indexPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(indexPanelLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(indexPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startIndexButton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stopIndexingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        indexCardsPanel.add(indexPanel, "indexCard");

        textCloudsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Index Tags Cloud", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        tagSelectButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        tagSelectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/textCloud.png"))); // NOI18N
        tagSelectButton.setText("Generate Text Clouds");
        tagSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagSelectButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Number Of Tags: ");

        tagsNumberTextField.setText(" 200");

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel36.setText("Exclude Tags Less Than:");

        tagsExcludeTextField.setText("1");
        tagsExcludeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagsExcludeTextFieldActionPerformed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel37.setText("Display Type:");

        tagsDisplayComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Alphabetically Sort - Ascending", "Alphabetically Sort - descending ", "Score Sort - Ascending", "Score Sort - descending" }));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(tagsNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72)
                        .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tagsExcludeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tagsDisplayComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 652, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tagSelectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel2)
                            .addComponent(tagsNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tagsExcludeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37)
                            .addComponent(tagsDisplayComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(tagSelectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        tagsPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout tagsPanelLayout = new javax.swing.GroupLayout(tagsPanel);
        tagsPanel.setLayout(tagsPanelLayout);
        tagsPanelLayout.setHorizontalGroup(
            tagsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1757, Short.MAX_VALUE)
        );
        tagsPanelLayout.setVerticalGroup(
            tagsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 613, Short.MAX_VALUE)
        );

        jScrollPane10.setViewportView(tagsPanel);

        jTabbedPane1.addTab("Tags Cloud", jScrollPane10);

        jScrollPane22.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        cloudsTable.setFont(new java.awt.Font("Tahoma", 1, 11));
        cloudsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Word", "Frequency"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        cloudsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cloudsTableMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cloudsTableMouseReleased(evt);
            }
        });
        jScrollPane22.setViewportView(cloudsTable);

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel33.setForeground(new java.awt.Color(0, 70, 213));
        jLabel33.setText("Filter Table:");

        cloudsFilterTextField.setText(" ");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 979, Short.MAX_VALUE)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cloudsFilterTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane22, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(cloudsFilterTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Words Frequency", jPanel19);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1004, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout textCloudsPanelLayout = new javax.swing.GroupLayout(textCloudsPanel);
        textCloudsPanel.setLayout(textCloudsPanelLayout);
        textCloudsPanelLayout.setHorizontalGroup(
            textCloudsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, textCloudsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(textCloudsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        textCloudsPanelLayout.setVerticalGroup(
            textCloudsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, textCloudsPanelLayout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        indexCardsPanel.add(textCloudsPanel, "textCloudsCard");

        indexVisualizingPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Index Visualization", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        indexVisulizingButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        indexVisulizingButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/chart_pie.png"))); // NOI18N
        indexVisulizingButton.setText("Index Visualization");
        indexVisulizingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                indexVisulizingButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(246, 246, 246)
                .addComponent(indexVisulizingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(556, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(indexVisulizingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        indexVisualizingPiePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Visualizing File Extension in Index", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        indexVisualizingPiePanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout indexVisualizingPanelLayout = new javax.swing.GroupLayout(indexVisualizingPanel);
        indexVisualizingPanel.setLayout(indexVisualizingPanelLayout);
        indexVisualizingPanelLayout.setHorizontalGroup(
            indexVisualizingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(indexVisualizingPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(indexVisualizingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(indexVisualizingPiePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        indexVisualizingPanelLayout.setVerticalGroup(
            indexVisualizingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(indexVisualizingPanelLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(indexVisualizingPiePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE))
        );

        indexCardsPanel.add(indexVisualizingPanel, "indexVisualizingCard");

        javax.swing.GroupLayout IndexFileSystemPanelLayout = new javax.swing.GroupLayout(IndexFileSystemPanel);
        IndexFileSystemPanel.setLayout(IndexFileSystemPanelLayout);
        IndexFileSystemPanelLayout.setHorizontalGroup(
            IndexFileSystemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IndexFileSystemPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(indexFileSystemButtonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(indexCardsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1040, Short.MAX_VALUE)
                .addContainerGap())
        );
        IndexFileSystemPanelLayout.setVerticalGroup(
            IndexFileSystemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, IndexFileSystemPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(IndexFileSystemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(indexCardsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(indexFileSystemButtonsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        fileSystemTappedPane.addTab("Index File System", IndexFileSystemPanel);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Search Informations"));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel8.setText("User Query");

        queryTextField.setText(" ");

        keywordsListButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        keywordsListButton.setText("Advanced Search Options");
        keywordsListButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keywordsListButtonActionPerformed(evt);
            }
        });

        startSearchingButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        startSearchingButton.setText("Start Searching");
        startSearchingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startSearchingButtonActionPerformed(evt);
            }
        });

        clearFieldsButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        clearFieldsButton.setText("Clear Fields");
        clearFieldsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearFieldsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startSearchingButton, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
                    .addComponent(queryTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(clearFieldsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(keywordsListButton, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(keywordsListButton)
                        .addGap(18, 18, 18)
                        .addComponent(clearFieldsButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(queryTextField)))
                        .addGap(18, 18, 18)
                        .addComponent(startSearchingButton)))
                .addGap(19, 19, 19))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        searchTable.setAutoCreateRowSorter(true);
        searchTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "File Path"
            }
        ));
        searchTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        searchTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        searchTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchTableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                searchTableMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                searchTableMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(searchTable);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
        );

        jTabbedPane4.addTab("Search Result", jPanel22);

        clusterPathTree.setModel(null);
        clusterPathTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                clusterPathTreeValueChanged(evt);
            }
        });
        jScrollPane12.setViewportView(clusterPathTree);

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
        );

        jTabbedPane4.addTab("Path Clustering", jPanel24);

        clusterTypeTree.setModel(null);
        clusterTypeTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                clusterTypeTreeValueChanged(evt);
            }
        });
        jScrollPane27.setViewportView(clusterTypeTree);

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane27, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane27, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
        );

        jTabbedPane4.addTab("Type Clustering", jPanel29);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(searchProgressBard, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
            .addComponent(jTabbedPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jTabbedPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(searchProgressBard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Searching History"));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel11.setText("Last Searching Date:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel12.setText("Searching Finishing Time:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel13.setText("Last Index Directory:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel14.setText("Last User Query:");

        searchTime.setFont(new java.awt.Font("Tahoma", 1, 11));
        searchTime.setForeground(new java.awt.Color(0, 0, 255));
        searchTime.setText(" ");

        searchingDateLbl.setFont(new java.awt.Font("Tahoma", 1, 11));
        searchingDateLbl.setForeground(new java.awt.Color(0, 0, 255));
        searchingDateLbl.setText(" ");

        indexDirLbl2.setFont(new java.awt.Font("Tahoma", 1, 11));
        indexDirLbl2.setForeground(new java.awt.Color(0, 0, 255));
        indexDirLbl2.setText(" ");

        userQueryLbl.setFont(new java.awt.Font("Tahoma", 1, 11));
        userQueryLbl.setForeground(new java.awt.Color(0, 0, 255));
        userQueryLbl.setText(" ");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(indexDirLbl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchingDateLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(userQueryLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchTime, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
                .addContainerGap(398, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(searchingDateLbl))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(indexDirLbl2)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(searchTime))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(userQueryLbl))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("File Information"));

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel26.setText("File Name:");

        searchFileNameLbl.setFont(new java.awt.Font("Tahoma", 1, 11));
        searchFileNameLbl.setForeground(new java.awt.Color(0, 0, 255));
        searchFileNameLbl.setText(" ");

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel29.setText("File Extension:");

        searchFileExtensionLbl.setFont(new java.awt.Font("Tahoma", 1, 11));
        searchFileExtensionLbl.setForeground(new java.awt.Color(0, 0, 255));
        searchFileExtensionLbl.setText(" ");

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel31.setText("File Size:");

        searchFileSizeLbl.setFont(new java.awt.Font("Tahoma", 1, 11));
        searchFileSizeLbl.setForeground(new java.awt.Color(0, 0, 255));
        searchFileSizeLbl.setText(" ");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchFileSizeLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchFileExtensionLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchFileNameLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(searchFileNameLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(searchFileExtensionLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(searchFileSizeLbl))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fileRenderPanel.setLayout(new java.awt.BorderLayout());
        jTabbedPane2.addTab("Full Text Content", fileRenderPanel);

        metaDataTextArea.setColumns(20);
        metaDataTextArea.setRows(5);
        jScrollPane28.setViewportView(metaDataTextArea);

        javax.swing.GroupLayout FileMetaDataPanelLayout = new javax.swing.GroupLayout(FileMetaDataPanel);
        FileMetaDataPanel.setLayout(FileMetaDataPanelLayout);
        FileMetaDataPanelLayout.setHorizontalGroup(
            FileMetaDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane28, javax.swing.GroupLayout.DEFAULT_SIZE, 855, Short.MAX_VALUE)
        );
        FileMetaDataPanelLayout.setVerticalGroup(
            FileMetaDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane28, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("File MetaData", FileMetaDataPanel);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 860, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout SearchFileSystemPanelLayout = new javax.swing.GroupLayout(SearchFileSystemPanel);
        SearchFileSystemPanel.setLayout(SearchFileSystemPanelLayout);
        SearchFileSystemPanelLayout.setHorizontalGroup(
            SearchFileSystemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchFileSystemPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SearchFileSystemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SearchFileSystemPanelLayout.createSequentialGroup()
                        .addGroup(SearchFileSystemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(SearchFileSystemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        SearchFileSystemPanelLayout.setVerticalGroup(
            SearchFileSystemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SearchFileSystemPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SearchFileSystemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SearchFileSystemPanelLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(SearchFileSystemPanelLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(11, 11, 11)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        fileSystemTappedPane.addTab("Search in File System", SearchFileSystemPanel);

        javax.swing.GroupLayout fileSystemPanelLayout = new javax.swing.GroupLayout(fileSystemPanel);
        fileSystemPanel.setLayout(fileSystemPanelLayout);
        fileSystemPanelLayout.setHorizontalGroup(
            fileSystemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fileSystemTappedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1216, Short.MAX_VALUE)
        );
        fileSystemPanelLayout.setVerticalGroup(
            fileSystemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fileSystemTappedPane)
        );

        CardPanel.add(fileSystemPanel, "fileSystemCard");

        messagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        emailTable.setAutoCreateRowSorter(true);
        emailTable.setFillsViewportHeight(true);
        jScrollPane9.setViewportView(emailTable);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel19.setForeground(new java.awt.Color(0, 70, 213));
        jLabel19.setText("Search String:");

        emailSearchTextField.setText(" ");

        emailSearchButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        emailSearchButton.setText("Search in Content");
        emailSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailSearchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout messagePanelLayout = new javax.swing.GroupLayout(messagePanel);
        messagePanel.setLayout(messagePanelLayout);
        messagePanelLayout.setHorizontalGroup(
            messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messagePanelLayout.createSequentialGroup()
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(emailSearchTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(emailSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 955, Short.MAX_VALUE)
        );
        messagePanelLayout.setVerticalGroup(
            messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messagePanelLayout.createSequentialGroup()
                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(messagePanelLayout.createSequentialGroup()
                        .addGap(252, 252, 252)
                        .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(emailSearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emailSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        renderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        HTMLRenderPanel.setLayout(new java.awt.BorderLayout());
        jTabbedPane3.addTab("Message Text", HTMLRenderPanel);

        jScrollPane17.setViewportView(textEditorPane);

        javax.swing.GroupLayout TXTRenderPanelLayout = new javax.swing.GroupLayout(TXTRenderPanel);
        TXTRenderPanel.setLayout(TXTRenderPanelLayout);
        TXTRenderPanelLayout.setHorizontalGroup(
            TXTRenderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TXTRenderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 1150, Short.MAX_VALUE)
                .addContainerGap())
        );
        TXTRenderPanelLayout.setVerticalGroup(
            TXTRenderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TXTRenderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("HTML Code", TXTRenderPanel);

        jScrollPane8.setViewportView(headerEditorPane);

        javax.swing.GroupLayout messageHeaderPanelLayout = new javax.swing.GroupLayout(messageHeaderPanel);
        messageHeaderPanel.setLayout(messageHeaderPanelLayout);
        messageHeaderPanelLayout.setHorizontalGroup(
            messageHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messageHeaderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 1150, Short.MAX_VALUE)
                .addContainerGap())
        );
        messageHeaderPanelLayout.setVerticalGroup(
            messageHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messageHeaderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Message Header", messageHeaderPanel);

        javax.swing.GroupLayout renderPanelLayout = new javax.swing.GroupLayout(renderPanel);
        renderPanel.setLayout(renderPanelLayout);
        renderPanelLayout.setHorizontalGroup(
            renderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1175, Short.MAX_VALUE)
        );
        renderPanelLayout.setVerticalGroup(
            renderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setForeground(new java.awt.Color(0, 70, 213));
        jLabel1.setText("Select Outlook File From Index:");

        loadPstButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        loadPstButton.setText("Load Outlook File");
        loadPstButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadPstButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(outlookComboBox, 0, 759, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loadPstButton, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(outlookComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadPstButton)))
        );

        folderTree.setModel(null);
        folderTree.setShowsRootHandles(true);
        treeScrollPane.setViewportView(folderTree);

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
        );

        clusteringTabbedPane.addTab("Mail Box", jPanel26);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clusteringTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clusteringTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout outlookPanelLayout = new javax.swing.GroupLayout(outlookPanel);
        outlookPanel.setLayout(outlookPanelLayout);
        outlookPanelLayout.setHorizontalGroup(
            outlookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, outlookPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outlookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(renderPanel, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, outlookPanelLayout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(messagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        outlookPanelLayout.setVerticalGroup(
            outlookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outlookPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outlookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(messagePanel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(renderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        emailTabbedPane.addTab("Microsoft Outlook Viewing", outlookPanel);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Select Information Need", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel20.setForeground(new java.awt.Color(0, 70, 213));
        jLabel20.setText("Statistics For:");

        correlationComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Emails per senders", "Emails per reciever", "Emails per senders/reciever domains", "Emails per users locations", "Frequency of Messages" }));
        correlationComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                correlationComboBoxActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel21.setForeground(new java.awt.Color(0, 70, 213));
        jLabel21.setText("From Date:");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel22.setForeground(new java.awt.Color(0, 70, 213));
        jLabel22.setText("To Date:");

        emailVisualizationButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        emailVisualizationButton.setText(" Visualization of the Result");
        emailVisualizationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailVisualizationButtonActionPerformed(evt);
            }
        });

        toDatePanel.setLayout(new java.awt.BorderLayout());

        fromDatePanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel20)
                        .addGap(18, 18, 18)
                        .addComponent(correlationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fromDatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(toDatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(306, 306, 306)
                        .addComponent(emailVisualizationButton, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(toDatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel20)
                                .addComponent(correlationComboBox)
                                .addComponent(jLabel21))
                            .addComponent(fromDatePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(22, 22, 22)
                .addComponent(emailVisualizationButton)
                .addContainerGap())
        );

        correlationResultPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Statistics Result", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        correlationResultPanel.setLayout(new java.awt.CardLayout());

        inboxTable.setAutoCreateRowSorter(true);
        inboxTable.setFont(new java.awt.Font("Tahoma", 1, 11));
        inboxTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Sender Name","Receiver Name","Number of Message","Date"
            }
        ) {
            public Class<?> getColumnClass( int index) {
                switch (index) {
                    case 0:
                    case 1:
                    case 3:
                    return Object.class;

                    case 2:
                    return Integer.class;
                }

                return Object.class;
            }
        });
        inboxTable.setRowSorter(new TableRowSorter<TableModel>(inboxTable.getModel()));
        inboxTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                inboxTableMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                inboxTableMouseReleased(evt);
            }
        });
        jScrollPane13.setViewportView(inboxTable);

        javax.swing.GroupLayout inboxPanelLayout = new javax.swing.GroupLayout(inboxPanel);
        inboxPanel.setLayout(inboxPanelLayout);
        inboxPanelLayout.setHorizontalGroup(
            inboxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 1175, Short.MAX_VALUE)
        );
        inboxPanelLayout.setVerticalGroup(
            inboxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
        );

        correlationResultPanel.add(inboxPanel, "inboxCard");

        sentItemTable.setAutoCreateRowSorter(true);
        sentItemTable.setFont(new java.awt.Font("Tahoma", 1, 11));
        sentItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Sender Name","Receiver Name","Number of Message","Date"
            }
        ){
            public Class<?> getColumnClass( int index) {
                switch (index) {
                    case 0:
                    case 1:
                    case 3:
                    return Object.class;

                    case 2:
                    return Integer.class;
                }

                return Object.class;
            }
        });
        sentItemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                sentItemTableMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                sentItemTableMouseReleased(evt);
            }
        });
        jScrollPane14.setViewportView(sentItemTable);

        javax.swing.GroupLayout sentItemPanelLayout = new javax.swing.GroupLayout(sentItemPanel);
        sentItemPanel.setLayout(sentItemPanelLayout);
        sentItemPanelLayout.setHorizontalGroup(
            sentItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 1175, Short.MAX_VALUE)
        );
        sentItemPanelLayout.setVerticalGroup(
            sentItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
        );

        correlationResultPanel.add(sentItemPanel, "sentItemCard");

        espTable.setAutoCreateRowSorter(true);
        espTable.setFont(new java.awt.Font("Tahoma", 1, 11));
        espTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Email Provider Name","Number of Message From It"
            }
        ){
            public Class<?> getColumnClass( int index) {
                switch (index) {
                    case 0:
                    return Object.class;

                    case 1:
                    return Integer.class;
                }

                return Object.class;
            }
        });
        espTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                espTableMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                espTableMouseReleased(evt);
            }
        });
        jScrollPane15.setViewportView(espTable);

        javax.swing.GroupLayout espPanelLayout = new javax.swing.GroupLayout(espPanel);
        espPanel.setLayout(espPanelLayout);
        espPanelLayout.setHorizontalGroup(
            espPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 1175, Short.MAX_VALUE)
        );
        espPanelLayout.setVerticalGroup(
            espPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
        );

        correlationResultPanel.add(espPanel, "espCard");

        locationTable.setAutoCreateRowSorter(true);
        locationTable.setFont(new java.awt.Font("Tahoma", 1, 11));
        locationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Location","Number of Messages"
            }
        ){
            public Class<?> getColumnClass( int index) {
                switch (index) {
                    case 0:
                    return Object.class;

                    case 1:
                    return Integer.class;
                }

                return Object.class;
            }
        });
        locationTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                locationTableMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                locationTableMouseReleased(evt);
            }
        });
        jScrollPane16.setViewportView(locationTable);

        javax.swing.GroupLayout locationPanelLayout = new javax.swing.GroupLayout(locationPanel);
        locationPanel.setLayout(locationPanelLayout);
        locationPanelLayout.setHorizontalGroup(
            locationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 1175, Short.MAX_VALUE)
        );
        locationPanelLayout.setVerticalGroup(
            locationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
        );

        correlationResultPanel.add(locationPanel, "locationCard");

        javax.swing.GroupLayout messageFrequencyPanelLayout = new javax.swing.GroupLayout(messageFrequencyPanel);
        messageFrequencyPanel.setLayout(messageFrequencyPanelLayout);
        messageFrequencyPanelLayout.setHorizontalGroup(
            messageFrequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1175, Short.MAX_VALUE)
        );
        messageFrequencyPanelLayout.setVerticalGroup(
            messageFrequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 614, Short.MAX_VALUE)
        );

        correlationResultPanel.add(messageFrequencyPanel, "messageFrequencyCard");

        javax.swing.GroupLayout outlookCorrelationsPanelLayout = new javax.swing.GroupLayout(outlookCorrelationsPanel);
        outlookCorrelationsPanel.setLayout(outlookCorrelationsPanelLayout);
        outlookCorrelationsPanelLayout.setHorizontalGroup(
            outlookCorrelationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, outlookCorrelationsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outlookCorrelationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(correlationResultPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1191, Short.MAX_VALUE))
                .addContainerGap())
        );
        outlookCorrelationsPanelLayout.setVerticalGroup(
            outlookCorrelationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outlookCorrelationsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(correlationResultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
                .addContainerGap())
        );

        emailTabbedPane.addTab("Microsoft Outlook Statistics", outlookCorrelationsPanel);

        javax.swing.GroupLayout emailPanelLayout = new javax.swing.GroupLayout(emailPanel);
        emailPanel.setLayout(emailPanelLayout);
        emailPanelLayout.setHorizontalGroup(
            emailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(emailTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1216, Short.MAX_VALUE)
        );
        emailPanelLayout.setVerticalGroup(
            emailPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(emailTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 822, Short.MAX_VALUE)
        );

        CardPanel.add(emailPanel, "emailCard");

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Summary Browsing  History", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        summaryInternetButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        summaryInternetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/history.png"))); // NOI18N
        summaryInternetButton.setText(" History Summary");
        summaryInternetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                summaryInternetButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(645, Short.MAX_VALUE)
                .addComponent(summaryInternetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(334, 334, 334))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addComponent(summaryInternetButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Summary Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        summaryTable.setAutoCreateRowSorter(true);
        summaryTable.setFont(new java.awt.Font("Tahoma", 1, 11));
        summaryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Web Site","Number of Visits","Date of last Visit"
            }
        ){
            public Class<?> getColumnClass( int index) {
                switch (index) {
                    case 0:
                    case 2:
                    return String.class;

                    case 1:
                    return Integer.class;
                }

                return Object.class;
            }
        }
    );
    summaryTable.setFillsViewportHeight(true);
    summaryTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            summaryTableMousePressed(evt);
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            summaryTableMouseReleased(evt);
        }
    });
    jScrollPane11.setViewportView(summaryTable);

    jLabel30.setFont(new java.awt.Font("Tahoma", 1, 11));
    jLabel30.setForeground(new java.awt.Color(0, 70, 213));
    jLabel30.setText("Filter Table:");

    summaryTextField.setText(" ");

    javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
    jPanel21.setLayout(jPanel21Layout);
    jPanel21Layout.setHorizontalGroup(
        jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel21Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1155, Short.MAX_VALUE)
                .addGroup(jPanel21Layout.createSequentialGroup()
                    .addComponent(jLabel30)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(summaryTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 1079, Short.MAX_VALUE)))
            .addContainerGap())
    );
    jPanel21Layout.setVerticalGroup(
        jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel21Layout.createSequentialGroup()
            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel30)
                .addComponent(summaryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(21, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout summaryInternetPanelLayout = new javax.swing.GroupLayout(summaryInternetPanel);
    summaryInternetPanel.setLayout(summaryInternetPanelLayout);
    summaryInternetPanelLayout.setHorizontalGroup(
        summaryInternetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(summaryInternetPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(summaryInternetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    summaryInternetPanelLayout.setVerticalGroup(
        summaryInternetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(summaryInternetPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(55, Short.MAX_VALUE))
    );

    internetSurfingTappedPane.addTab("Summary Browsing Information", summaryInternetPanel);

    mozillaButtonsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Browser Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

    jToolBar2.setFloatable(false);
    jToolBar2.setRollover(true);

    ffSummaryButtton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/view_text.png"))); // NOI18N
    ffSummaryButtton.setText("Summary");
    ffSummaryButtton.setPreferredSize(new java.awt.Dimension(55, 55));
    ffSummaryButtton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ffSummaryButttonActionPerformed(evt);
        }
    });
    jToolBar2.add(ffSummaryButtton);
    jToolBar2.add(jSeparator2);

    webHistoryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/web.png"))); // NOI18N
    webHistoryButton.setText("Web History");
    webHistoryButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            webHistoryButtonActionPerformed(evt);
        }
    });
    jToolBar2.add(webHistoryButton);

    bookmarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/bookmarks.png"))); // NOI18N
    bookmarButton.setText("Bookmark");
    bookmarButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            bookmarButtonActionPerformed(evt);
        }
    });
    jToolBar2.add(bookmarButton);

    cookiesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/autostart.png"))); // NOI18N
    cookiesButton.setText("Cookies");
    cookiesButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cookiesButtonActionPerformed(evt);
        }
    });
    jToolBar2.add(cookiesButton);

    downloadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/download.png"))); // NOI18N
    downloadButton.setText("Download");
    downloadButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            downloadButtonActionPerformed(evt);
        }
    });
    jToolBar2.add(downloadButton);

    logginsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/lists.png"))); // NOI18N
    logginsButton.setText("Loggins");
    logginsButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            logginsButtonActionPerformed(evt);
        }
    });
    jToolBar2.add(logginsButton);

    javax.swing.GroupLayout mozillaButtonsPanelLayout = new javax.swing.GroupLayout(mozillaButtonsPanel);
    mozillaButtonsPanel.setLayout(mozillaButtonsPanelLayout);
    mozillaButtonsPanelLayout.setHorizontalGroup(
        mozillaButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(mozillaButtonsPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, Short.MAX_VALUE))
    );
    mozillaButtonsPanelLayout.setVerticalGroup(
        mozillaButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)
    );

    mozillaResultPanel.setLayout(new java.awt.CardLayout());

    webHistoryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Web History", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

    webHistoryTable.setAutoCreateRowSorter(true);
    webHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "Date","Url","Title","Visit Count","Typed"
        }
    ){
        public Class<?> getColumnClass( int index) {
            switch (index) {
                case 0:
                case 1:
                case 2:
                return Object.class;

                case 3:
                return Integer.class;

                case 4:
                return Integer.class;
            }

            return Object.class;
        }
    });
    webHistoryTable.setFillsViewportHeight(true);
    webHistoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            webHistoryTableMousePressed(evt);
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            webHistoryTableMouseReleased(evt);
        }
    });
    webHistoryScrollPane.setViewportView(webHistoryTable);

    javax.swing.GroupLayout webHistoryPanelLayout = new javax.swing.GroupLayout(webHistoryPanel);
    webHistoryPanel.setLayout(webHistoryPanelLayout);
    webHistoryPanelLayout.setHorizontalGroup(
        webHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(webHistoryPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(webHistoryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE)
            .addContainerGap())
    );
    webHistoryPanelLayout.setVerticalGroup(
        webHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(webHistoryPanelLayout.createSequentialGroup()
            .addComponent(webHistoryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .addContainerGap())
    );

    mozillaResultPanel.add(webHistoryPanel, "webHistoryCard");

    bookmarkHistory.setBorder(javax.swing.BorderFactory.createTitledBorder("Bookmark"));

    bookmarkTable.setAutoCreateRowSorter(true);
    bookmarkTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "TITLE","URL","DATE ADDED"
        }
    ));
    bookmarkTable.setFillsViewportHeight(true);
    bookmarkTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            bookmarkTableMousePressed(evt);
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            bookmarkTableMouseReleased(evt);
        }
    });
    bookmarkScrollPane.setViewportView(bookmarkTable);

    javax.swing.GroupLayout bookmarkHistoryLayout = new javax.swing.GroupLayout(bookmarkHistory);
    bookmarkHistory.setLayout(bookmarkHistoryLayout);
    bookmarkHistoryLayout.setHorizontalGroup(
        bookmarkHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(bookmarkHistoryLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(bookmarkScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE)
            .addContainerGap())
    );
    bookmarkHistoryLayout.setVerticalGroup(
        bookmarkHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(bookmarkHistoryLayout.createSequentialGroup()
            .addComponent(bookmarkScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .addContainerGap())
    );

    mozillaResultPanel.add(bookmarkHistory, "bookmarkCard");

    cookiesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Cookies"));

    cookiesTable.setAutoCreateRowSorter(true);
    cookiesTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "HOST","PATH","NAME","VALUE","LAST ACCESS TIME","EXPIRY",
            "IS SECURE","IS HTTP ONLY"
        }
    ));
    cookiesTable.setFillsViewportHeight(true);
    cookiesTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            cookiesTableMousePressed(evt);
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            cookiesTableMouseReleased(evt);
        }
    });
    cookiesScrollPane.setViewportView(cookiesTable);

    javax.swing.GroupLayout cookiesPanelLayout = new javax.swing.GroupLayout(cookiesPanel);
    cookiesPanel.setLayout(cookiesPanelLayout);
    cookiesPanelLayout.setHorizontalGroup(
        cookiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(cookiesPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(cookiesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE)
            .addContainerGap())
    );
    cookiesPanelLayout.setVerticalGroup(
        cookiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(cookiesPanelLayout.createSequentialGroup()
            .addComponent(cookiesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .addContainerGap())
    );

    mozillaResultPanel.add(cookiesPanel, "cookiesCard");

    downloadPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Download"));

    downloadTable.setAutoCreateRowSorter(true);
    downloadTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "NAME","SOURCE","TARGET","START TIME","END TIME"
        }
    ));
    downloadTable.setFillsViewportHeight(true);
    downloadTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            downloadTableMousePressed(evt);
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            downloadTableMouseReleased(evt);
        }
    });
    downloadScrollPane.setViewportView(downloadTable);

    javax.swing.GroupLayout downloadPanelLayout = new javax.swing.GroupLayout(downloadPanel);
    downloadPanel.setLayout(downloadPanelLayout);
    downloadPanelLayout.setHorizontalGroup(
        downloadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(downloadPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(downloadScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE)
            .addContainerGap())
    );
    downloadPanelLayout.setVerticalGroup(
        downloadPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(downloadPanelLayout.createSequentialGroup()
            .addComponent(downloadScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .addContainerGap())
    );

    mozillaResultPanel.add(downloadPanel, "downloadCard");

    logginsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Loggins"));

    logginsTable.setAutoCreateRowSorter(true);
    logginsTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "HOST NAME","USERNAME FIELD",
            "PASSWORD FIELD","ENCRYPTED USERNAME","ENCRYPTED PASSWORD"
        }
    ));
    logginsTable.setFillsViewportHeight(true);
    logginsTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            logginsTableMousePressed(evt);
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            logginsTableMouseReleased(evt);
        }
    });
    logginsScrollPane.setViewportView(logginsTable);

    javax.swing.GroupLayout logginsPanelLayout = new javax.swing.GroupLayout(logginsPanel);
    logginsPanel.setLayout(logginsPanelLayout);
    logginsPanelLayout.setHorizontalGroup(
        logginsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(logginsPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(logginsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE)
            .addContainerGap())
    );
    logginsPanelLayout.setVerticalGroup(
        logginsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(logginsPanelLayout.createSequentialGroup()
            .addComponent(logginsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .addContainerGap())
    );

    mozillaResultPanel.add(logginsPanel, "logginsCard");

    topHostTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "Host Name", "Total Visit Count"
        }
    ));
    jScrollPane24.setViewportView(topHostTable);

    javax.swing.GroupLayout topHostPanelLayout = new javax.swing.GroupLayout(topHostPanel);
    topHostPanel.setLayout(topHostPanelLayout);
    topHostPanelLayout.setHorizontalGroup(
        topHostPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane24, javax.swing.GroupLayout.DEFAULT_SIZE, 1002, Short.MAX_VALUE)
    );
    topHostPanelLayout.setVerticalGroup(
        topHostPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane24, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
    );

    ffSumarryTappnedPane.addTab("Top 20 Visitied Host", topHostPanel);

    topURLTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "URL","Title","Visit Count"
        }
    ));
    jScrollPane25.setViewportView(topURLTable);

    javax.swing.GroupLayout topURLPanelLayout = new javax.swing.GroupLayout(topURLPanel);
    topURLPanel.setLayout(topURLPanelLayout);
    topURLPanelLayout.setHorizontalGroup(
        topURLPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane25, javax.swing.GroupLayout.DEFAULT_SIZE, 1002, Short.MAX_VALUE)
    );
    topURLPanelLayout.setVerticalGroup(
        topURLPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane25, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
    );

    ffSumarryTappnedPane.addTab("Top 20 Visited url", topURLPanel);

    searchEngineTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "URL","Date"
        }
    ));
    jScrollPane26.setViewportView(searchEngineTable);

    javax.swing.GroupLayout searchEnginePanelLayout = new javax.swing.GroupLayout(searchEnginePanel);
    searchEnginePanel.setLayout(searchEnginePanelLayout);
    searchEnginePanelLayout.setHorizontalGroup(
        searchEnginePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane26, javax.swing.GroupLayout.DEFAULT_SIZE, 1002, Short.MAX_VALUE)
    );
    searchEnginePanelLayout.setVerticalGroup(
        searchEnginePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane26, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
    );

    ffSumarryTappnedPane.addTab("Searching on search engine", searchEnginePanel);

    javax.swing.GroupLayout ffSummaryDataPanelLayout = new javax.swing.GroupLayout(ffSummaryDataPanel);
    ffSummaryDataPanel.setLayout(ffSummaryDataPanelLayout);
    ffSummaryDataPanelLayout.setHorizontalGroup(
        ffSummaryDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(ffSumarryTappnedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1007, Short.MAX_VALUE)
    );
    ffSummaryDataPanelLayout.setVerticalGroup(
        ffSummaryDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(ffSumarryTappnedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
    );

    ffSummaryButtonsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    ffViewHTMLReportButton.setText("View HTML Report");
    ffViewHTMLReportButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ffViewHTMLReportButtonActionPerformed(evt);
        }
    });

    ffVisualizingVisitedHostButton.setText("Visualizing Visited Host");
    ffVisualizingVisitedHostButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            ffVisualizingVisitedHostButtonActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout ffSummaryButtonsPanelLayout = new javax.swing.GroupLayout(ffSummaryButtonsPanel);
    ffSummaryButtonsPanel.setLayout(ffSummaryButtonsPanelLayout);
    ffSummaryButtonsPanelLayout.setHorizontalGroup(
        ffSummaryButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(ffSummaryButtonsPanelLayout.createSequentialGroup()
            .addGap(113, 113, 113)
            .addComponent(ffViewHTMLReportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 488, Short.MAX_VALUE)
            .addComponent(ffVisualizingVisitedHostButton, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(113, 113, 113))
    );
    ffSummaryButtonsPanelLayout.setVerticalGroup(
        ffSummaryButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(ffSummaryButtonsPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(ffSummaryButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ffViewHTMLReportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(ffVisualizingVisitedHostButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(15, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout ffSummaryPanelLayout = new javax.swing.GroupLayout(ffSummaryPanel);
    ffSummaryPanel.setLayout(ffSummaryPanelLayout);
    ffSummaryPanelLayout.setHorizontalGroup(
        ffSummaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ffSummaryPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(ffSummaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(ffSummaryButtonsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ffSummaryDataPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    ffSummaryPanelLayout.setVerticalGroup(
        ffSummaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ffSummaryPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(ffSummaryDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGap(18, 18, 18)
            .addComponent(ffSummaryButtonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );

    mozillaResultPanel.add(ffSummaryPanel, "ffSummaryCard");

    mozillaSearchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Searching and Sorting", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

    jLabel15.setText("Search String:");

    mozillaSearchField.setText(" ");

    jLabel16.setText("URL Filter:");

    mozillaFilterComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
        "Filter By Date",
        "Filrer By URL Name"
    }));
    mozillaFilterComboBox.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            mozillaFilterComboBoxActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout mozillaSearchPanelLayout = new javax.swing.GroupLayout(mozillaSearchPanel);
    mozillaSearchPanel.setLayout(mozillaSearchPanelLayout);
    mozillaSearchPanelLayout.setHorizontalGroup(
        mozillaSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(mozillaSearchPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(mozillaSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel16)
                .addComponent(jLabel15))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(mozillaSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(mozillaFilterComboBox, 0, 913, Short.MAX_VALUE)
                .addComponent(mozillaSearchField, javax.swing.GroupLayout.DEFAULT_SIZE, 913, Short.MAX_VALUE))
            .addContainerGap())
    );
    mozillaSearchPanelLayout.setVerticalGroup(
        mozillaSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(mozillaSearchPanelLayout.createSequentialGroup()
            .addGroup(mozillaSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel15)
                .addComponent(mozillaSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(mozillaSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel16)
                .addComponent(mozillaFilterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
    jLabel3.setForeground(new java.awt.Color(0, 70, 213));
    jLabel3.setText("Select FireFox File From Index:");

    loadFFButton.setFont(new java.awt.Font("Tahoma", 1, 11));
    loadFFButton.setText("Load Profile");
    loadFFButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            loadFFButtonActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
    jPanel12.setLayout(jPanel12Layout);
    jPanel12Layout.setHorizontalGroup(
        jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel12Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel3)
            .addGap(18, 18, 18)
            .addComponent(ffComboBox, 0, 820, Short.MAX_VALUE)
            .addGap(18, 18, 18)
            .addComponent(loadFFButton, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );
    jPanel12Layout.setVerticalGroup(
        jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel12Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(ffComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(loadFFButton))
            .addContainerGap(16, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout mozillaPanelLayout = new javax.swing.GroupLayout(mozillaPanel);
    mozillaPanel.setLayout(mozillaPanelLayout);
    mozillaPanelLayout.setHorizontalGroup(
        mozillaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(mozillaPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(mozillaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mozillaPanelLayout.createSequentialGroup()
                    .addComponent(mozillaButtonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(mozillaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(mozillaResultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1027, Short.MAX_VALUE)
                        .addComponent(mozillaSearchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    mozillaPanelLayout.setVerticalGroup(
        mozillaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(mozillaPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(mozillaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mozillaPanelLayout.createSequentialGroup()
                    .addComponent(mozillaSearchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(mozillaResultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE))
                .addComponent(mozillaButtonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );

    internetSurfingTappedPane.addTab("Mozilla FireFox Browser", mozillaPanel);

    IEButtonsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Browser Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

    jToolBar3.setFloatable(false);
    jToolBar3.setRollover(true);

    webHistoryButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/web.png"))); // NOI18N
    webHistoryButton1.setText("Web History");
    webHistoryButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            webHistoryButton1ActionPerformed(evt);
        }
    });
    jToolBar3.add(webHistoryButton1);

    bookmarButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/bookmarks.png"))); // NOI18N
    bookmarButton1.setText("Bookmark");
    bookmarButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            bookmarButton1ActionPerformed(evt);
        }
    });
    jToolBar3.add(bookmarButton1);

    cookiesButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/autostart.png"))); // NOI18N
    cookiesButton1.setText("Cookies");
    cookiesButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cookiesButton1ActionPerformed(evt);
        }
    });
    jToolBar3.add(cookiesButton1);

    cacheButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/download.png"))); // NOI18N
    cacheButton.setText("Cache");
    cacheButton.setEnabled(false);
    cacheButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cacheButtonActionPerformed(evt);
        }
    });
    jToolBar3.add(cacheButton);

    logginsButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/gui/resources/lists.png"))); // NOI18N
    logginsButton1.setText("Loggins");
    logginsButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            logginsButton1ActionPerformed(evt);
        }
    });
    jToolBar3.add(logginsButton1);

    javax.swing.GroupLayout IEButtonsPanelLayout = new javax.swing.GroupLayout(IEButtonsPanel);
    IEButtonsPanel.setLayout(IEButtonsPanelLayout);
    IEButtonsPanelLayout.setHorizontalGroup(
        IEButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IEButtonsPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 131, Short.MAX_VALUE)
            .addContainerGap())
    );
    IEButtonsPanelLayout.setVerticalGroup(
        IEButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IEButtonsPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 637, Short.MAX_VALUE)
            .addContainerGap())
    );

    IESearchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Searching and Sorting", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

    jLabel17.setText("Search String:");

    IESearchField.setText(" ");

    jLabel18.setText("URL Filter:");

    IEFilterComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
        "Filter By Date",
        "Filrer By URL Name"
    }));
    IEFilterComboBox.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            IEFilterComboBoxActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout IESearchPanelLayout = new javax.swing.GroupLayout(IESearchPanel);
    IESearchPanel.setLayout(IESearchPanelLayout);
    IESearchPanelLayout.setHorizontalGroup(
        IESearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IESearchPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(IESearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel18)
                .addComponent(jLabel17))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(IESearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(IEFilterComboBox, 0, 916, Short.MAX_VALUE)
                .addComponent(IESearchField, javax.swing.GroupLayout.DEFAULT_SIZE, 916, Short.MAX_VALUE))
            .addContainerGap())
    );
    IESearchPanelLayout.setVerticalGroup(
        IESearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IESearchPanelLayout.createSequentialGroup()
            .addGroup(IESearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel17)
                .addComponent(IESearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(IESearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel18)
                .addComponent(IEFilterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    IEResultPanel.setLayout(new java.awt.CardLayout());

    IEWebHistoryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Web History", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

    IEWebHistoryTable.setAutoCreateRowSorter(true);
    IEWebHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {
            "URL","TITLE","HITS","MODIFIED DATE","EXPIRATION DATE",
            "USER NAME"
        }
    ));
    IEWebHistoryTable.setFillsViewportHeight(true);
    IEWebHistoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            IEWebHistoryTableMousePressed(evt);
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            IEWebHistoryTableMouseReleased(evt);
        }
    });
    jScrollPane5.setViewportView(IEWebHistoryTable);

    javax.swing.GroupLayout IEWebHistoryPanelLayout = new javax.swing.GroupLayout(IEWebHistoryPanel);
    IEWebHistoryPanel.setLayout(IEWebHistoryPanelLayout);
    IEWebHistoryPanelLayout.setHorizontalGroup(
        IEWebHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IEWebHistoryPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 994, Short.MAX_VALUE)
            .addContainerGap())
    );
    IEWebHistoryPanelLayout.setVerticalGroup(
        IEWebHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
    );

    IEResultPanel.add(IEWebHistoryPanel, "IEWebHisrotyCard");

    IEBookmarkPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Bookmark"));

    IEBookmarkTable.setAutoCreateRowSorter(true);
    IEBookmarkTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] { } ,
        new String [] {
            "FILE NAME"
        }
    ));
    IEBookmarkTable.setFillsViewportHeight(true);
    IEBookmarkTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            IEBookmarkTableMousePressed(evt);
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            IEBookmarkTableMouseReleased(evt);
        }
    });
    jScrollPane6.setViewportView(IEBookmarkTable);

    javax.swing.GroupLayout IEBookmarkPanelLayout = new javax.swing.GroupLayout(IEBookmarkPanel);
    IEBookmarkPanel.setLayout(IEBookmarkPanelLayout);
    IEBookmarkPanelLayout.setHorizontalGroup(
        IEBookmarkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IEBookmarkPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 994, Short.MAX_VALUE)
            .addContainerGap())
    );
    IEBookmarkPanelLayout.setVerticalGroup(
        IEBookmarkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IEBookmarkPanelLayout.createSequentialGroup()
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .addContainerGap())
    );

    IEResultPanel.add(IEBookmarkPanel, "IEBookmarkCard");

    IECookiesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Cookies"));

    IECookiesTable.setAutoCreateRowSorter(true);
    IECookiesTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "DATE","SITE","FILE"
        }
    ));
    IECookiesTable.setFillsViewportHeight(true);
    IECookiesTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            IECookiesTableMousePressed(evt);
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            IECookiesTableMouseReleased(evt);
        }
    });
    jScrollPane4.setViewportView(IECookiesTable);

    javax.swing.GroupLayout IECookiesPanelLayout = new javax.swing.GroupLayout(IECookiesPanel);
    IECookiesPanel.setLayout(IECookiesPanelLayout);
    IECookiesPanelLayout.setHorizontalGroup(
        IECookiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IECookiesPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 994, Short.MAX_VALUE)
            .addContainerGap())
    );
    IECookiesPanelLayout.setVerticalGroup(
        IECookiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IECookiesPanelLayout.createSequentialGroup()
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .addContainerGap())
    );

    IEResultPanel.add(IECookiesPanel, "IECookiesCard");

    IECachePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Cache"));

    IECacheTable.setAutoCreateRowSorter(true);
    IECacheTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "DATE","URL","FILE"
        }
    ));
    IECacheTable.setFillsViewportHeight(true);
    IECacheTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            IECacheTableMousePressed(evt);
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            IECacheTableMouseReleased(evt);
        }
    });
    jScrollPane3.setViewportView(IECacheTable);

    javax.swing.GroupLayout IECachePanelLayout = new javax.swing.GroupLayout(IECachePanel);
    IECachePanel.setLayout(IECachePanelLayout);
    IECachePanelLayout.setHorizontalGroup(
        IECachePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IECachePanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 994, Short.MAX_VALUE)
            .addContainerGap())
    );
    IECachePanelLayout.setVerticalGroup(
        IECachePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IECachePanelLayout.createSequentialGroup()
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .addContainerGap())
    );

    IEResultPanel.add(IECachePanel, "IECacheCard");

    IELogginsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Loggins"));

    IELogginsTable.setAutoCreateRowSorter(true);
    IELogginsTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "Entry Name","TYPE","STORED IN","USERNAME","PASSWORD"
        }
    ));
    IELogginsTable.setFillsViewportHeight(true);
    IELogginsTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            IELogginsTableMousePressed(evt);
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            IELogginsTableMouseReleased(evt);
        }
    });
    jScrollPane7.setViewportView(IELogginsTable);

    javax.swing.GroupLayout IELogginsPanelLayout = new javax.swing.GroupLayout(IELogginsPanel);
    IELogginsPanel.setLayout(IELogginsPanelLayout);
    IELogginsPanelLayout.setHorizontalGroup(
        IELogginsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IELogginsPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 994, Short.MAX_VALUE)
            .addContainerGap())
    );
    IELogginsPanelLayout.setVerticalGroup(
        IELogginsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IELogginsPanelLayout.createSequentialGroup()
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .addContainerGap())
    );

    IEResultPanel.add(IELogginsPanel, "IELogginsCard");

    jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
    jLabel6.setForeground(new java.awt.Color(0, 70, 213));
    jLabel6.setText("Select IE File From Index:");

    loadIEButton.setFont(new java.awt.Font("Tahoma", 1, 11));
    loadIEButton.setText("Load Profile");
    loadIEButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            loadIEButtonActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
    jPanel13.setLayout(jPanel13Layout);
    jPanel13Layout.setHorizontalGroup(
        jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel13Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel6)
            .addGap(18, 18, 18)
            .addComponent(ieComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(loadIEButton, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
            .addContainerGap())
    );
    jPanel13Layout.setVerticalGroup(
        jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel13Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6)
                .addComponent(ieComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(loadIEButton))
            .addContainerGap(16, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout IEPanelLayout = new javax.swing.GroupLayout(IEPanel);
    IEPanel.setLayout(IEPanelLayout);
    IEPanelLayout.setHorizontalGroup(
        IEPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IEPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(IEPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(IEPanelLayout.createSequentialGroup()
                    .addComponent(IEButtonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(IEPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(IEResultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1030, Short.MAX_VALUE)
                        .addComponent(IESearchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    IEPanelLayout.setVerticalGroup(
        IEPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(IEPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(IEPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, IEPanelLayout.createSequentialGroup()
                    .addComponent(IESearchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(IEResultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE))
                .addComponent(IEButtonsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );

    internetSurfingTappedPane.addTab("Microsoft Internet Explorer Broswer", IEPanel);

    javax.swing.GroupLayout internetSurfingPanelLayout = new javax.swing.GroupLayout(internetSurfingPanel);
    internetSurfingPanel.setLayout(internetSurfingPanelLayout);
    internetSurfingPanelLayout.setHorizontalGroup(
        internetSurfingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(internetSurfingTappedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1216, Short.MAX_VALUE)
    );
    internetSurfingPanelLayout.setVerticalGroup(
        internetSurfingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(internetSurfingPanelLayout.createSequentialGroup()
            .addComponent(internetSurfingTappedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
            .addContainerGap())
    );

    CardPanel.add(internetSurfingPanel, "internetSurfingCard");

    msnChatContentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
    msnChatContentPanel.setLayout(new java.awt.BorderLayout());

    msnChatTree.setModel(null);
    msnChatTree.setShowsRootHandles(true);
    msnChatTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
        public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
            msnChatTreeValueChanged(evt);
        }
    });
    jScrollPane20.setViewportView(msnChatTree);

    jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11));
    jLabel7.setForeground(new java.awt.Color(0, 70, 213));
    jLabel7.setText("Select Window Live File From Index:");

    loadMSNButton.setFont(new java.awt.Font("Tahoma", 1, 11));
    loadMSNButton.setText("Load Conversations");
    loadMSNButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            loadMSNButtonActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
    jPanel14.setLayout(jPanel14Layout);
    jPanel14Layout.setHorizontalGroup(
        jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel14Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel7)
            .addGap(18, 18, 18)
            .addComponent(msnComboBox, 0, 770, Short.MAX_VALUE)
            .addGap(18, 18, 18)
            .addComponent(loadMSNButton, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    jPanel14Layout.setVerticalGroup(
        jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel14Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel7)
                .addComponent(msnComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(loadMSNButton))
            .addContainerGap(16, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout WindowsLivePanelLayout = new javax.swing.GroupLayout(WindowsLivePanel);
    WindowsLivePanel.setLayout(WindowsLivePanelLayout);
    WindowsLivePanelLayout.setHorizontalGroup(
        WindowsLivePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(WindowsLivePanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(WindowsLivePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WindowsLivePanelLayout.createSequentialGroup()
                    .addComponent(jScrollPane20, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                    .addGap(18, 18, 18)
                    .addComponent(msnChatContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE))
                .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    WindowsLivePanelLayout.setVerticalGroup(
        WindowsLivePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WindowsLivePanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(WindowsLivePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jScrollPane20, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
                .addComponent(msnChatContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE))
            .addContainerGap())
    );

    chatPanelTappedPane.addTab("Windows Live Messanger", WindowsLivePanel);

    yahooChatTree.setModel(null);
    yahooChatTree.setShowsRootHandles(true);
    yahooChatTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
        public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
            yahooChatTreeValueChanged(evt);
        }
    });
    jScrollPane21.setViewportView(yahooChatTree);

    yahooChatContentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
    yahooChatContentPanel.setLayout(new java.awt.BorderLayout());

    jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11));
    jLabel9.setForeground(new java.awt.Color(0, 70, 213));
    jLabel9.setText("Select Yahoo File From Index:");

    loadYahooButton.setFont(new java.awt.Font("Tahoma", 1, 11));
    loadYahooButton.setText("Load Conversations");
    loadYahooButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            loadYahooButtonActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
    jPanel17.setLayout(jPanel17Layout);
    jPanel17Layout.setHorizontalGroup(
        jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel17Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel9)
            .addGap(18, 18, 18)
            .addComponent(yahooComboBox, 0, 788, Short.MAX_VALUE)
            .addGap(18, 18, 18)
            .addComponent(loadYahooButton, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );
    jPanel17Layout.setVerticalGroup(
        jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel17Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel9)
                .addComponent(yahooComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(loadYahooButton))
            .addContainerGap(18, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout yahooMessangerPanelLayout = new javax.swing.GroupLayout(yahooMessangerPanel);
    yahooMessangerPanel.setLayout(yahooMessangerPanelLayout);
    yahooMessangerPanelLayout.setHorizontalGroup(
        yahooMessangerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(yahooMessangerPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(yahooMessangerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(yahooMessangerPanelLayout.createSequentialGroup()
                    .addComponent(jScrollPane21, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(yahooChatContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 963, Short.MAX_VALUE)))
            .addContainerGap())
    );
    yahooMessangerPanelLayout.setVerticalGroup(
        yahooMessangerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, yahooMessangerPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(yahooMessangerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(yahooChatContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)
                .addComponent(jScrollPane21, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE))
            .addContainerGap())
    );

    chatPanelTappedPane.addTab("Yahoo Messanger", yahooMessangerPanel);

    skypeChatContentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    skypeTable.setFont(new java.awt.Font("Tahoma", 1, 11));
    skypeTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
        },
        new String [] {
            "Auther","Partner","Time","Message"
        }
    ));
    skypeTable.setGridColor(new java.awt.Color(255, 255, 255));
    skypeTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mousePressed(java.awt.event.MouseEvent evt) {
            skypeTableMousePressed(evt);
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            skypeTableMouseReleased(evt);
        }
    });
    jScrollPane18.setViewportView(skypeTable);

    skypeChatTree.setModel(null);
    skypeChatTree.setShowsRootHandles(true);
    skypeChatTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
        public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
            skypeChatTreeValueChanged(evt);
        }
    });
    jScrollPane23.setViewportView(skypeChatTree);

    javax.swing.GroupLayout skypeChatContentPanelLayout = new javax.swing.GroupLayout(skypeChatContentPanel);
    skypeChatContentPanel.setLayout(skypeChatContentPanelLayout);
    skypeChatContentPanelLayout.setHorizontalGroup(
        skypeChatContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(skypeChatContentPanelLayout.createSequentialGroup()
            .addComponent(jScrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE))
    );
    skypeChatContentPanelLayout.setVerticalGroup(
        skypeChatContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
        .addComponent(jScrollPane23, javax.swing.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)
    );

    jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    jLabel28.setFont(new java.awt.Font("Tahoma", 1, 11));
    jLabel28.setForeground(new java.awt.Color(0, 70, 213));
    jLabel28.setText("Select Skypee File From Index:");

    loadSkypeButton.setFont(new java.awt.Font("Tahoma", 1, 11));
    loadSkypeButton.setText("Load Conversations");
    loadSkypeButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            loadSkypeButtonActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
    jPanel20.setLayout(jPanel20Layout);
    jPanel20Layout.setHorizontalGroup(
        jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel20Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel28)
            .addGap(18, 18, 18)
            .addComponent(skypeComboBox, 0, 798, Short.MAX_VALUE)
            .addGap(18, 18, 18)
            .addComponent(loadSkypeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    jPanel20Layout.setVerticalGroup(
        jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel20Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel28)
                .addComponent(skypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(loadSkypeButton))
            .addContainerGap(16, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout skypePanelLayout = new javax.swing.GroupLayout(skypePanel);
    skypePanel.setLayout(skypePanelLayout);
    skypePanelLayout.setHorizontalGroup(
        skypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(skypePanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(skypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(skypeChatContentPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    skypePanelLayout.setVerticalGroup(
        skypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(skypePanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(skypeChatContentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );

    chatPanelTappedPane.addTab("Skype Messanger", skypePanel);

    javax.swing.GroupLayout ChatPanelLayout = new javax.swing.GroupLayout(ChatPanel);
    ChatPanel.setLayout(ChatPanelLayout);
    ChatPanelLayout.setHorizontalGroup(
        ChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(chatPanelTappedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1216, Short.MAX_VALUE)
    );
    ChatPanelLayout.setVerticalGroup(
        ChatPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(chatPanelTappedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 822, Short.MAX_VALUE)
    );

    CardPanel.add(ChatPanel, "chatCard");

    imagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Images Found", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
    imagePanel.setLayout(new java.awt.GridLayout(4, 4));

    jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Images Viewer", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

    showImagesButton.setFont(new java.awt.Font("Tahoma", 1, 11));
    showImagesButton.setText("Show Images");
    showImagesButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            showImagesButtonActionPerformed(evt);
        }
    });

    nextPageButton.setFont(new java.awt.Font("Tahoma", 1, 11));
    nextPageButton.setText("Next Page");
    nextPageButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            nextPageButtonActionPerformed(evt);
        }
    });

    prePageButton.setFont(new java.awt.Font("Tahoma", 1, 11));
    prePageButton.setText("Previous Page");
    prePageButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            prePageButtonActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
    jPanel23.setLayout(jPanel23Layout);
    jPanel23Layout.setHorizontalGroup(
        jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
            .addGap(589, 589, 589)
            .addComponent(showImagesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
            .addComponent(nextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(prePageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );
    jPanel23Layout.setVerticalGroup(
        jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
            .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(prePageButton, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addComponent(nextPageButton, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addComponent(showImagesButton, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
            .addContainerGap())
    );

    jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder("Image Information"));

    jLabel32.setFont(new java.awt.Font("Tahoma", 1, 11));
    jLabel32.setForeground(new java.awt.Color(0, 0, 204));
    jLabel32.setText("Image Path:");

    jLabel34.setFont(new java.awt.Font("Tahoma", 1, 11));
    jLabel34.setForeground(new java.awt.Color(0, 0, 153));
    jLabel34.setText("Image Size:");

    jLabel35.setFont(new java.awt.Font("Tahoma", 1, 11));
    jLabel35.setForeground(new java.awt.Color(0, 0, 153));
    jLabel35.setText("Image Modified Date:");

    imagePathLabel.setText(" ");

    imageSizeLabel.setText(" ");

    imageDateLabel.setText(" ");

    jLabel38.setFont(new java.awt.Font("Tahoma", 1, 11));
    jLabel38.setForeground(new java.awt.Color(0, 0, 153));
    jLabel38.setText("GeoTagging");

    geoTagLbl.setText(" ");

    javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
    jPanel28.setLayout(jPanel28Layout);
    jPanel28Layout.setHorizontalGroup(
        jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel28Layout.createSequentialGroup()
            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel35)
                .addComponent(jLabel32)
                .addComponent(jLabel34))
            .addGap(18, 18, 18)
            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(imageDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imagePathLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel28Layout.createSequentialGroup()
                    .addComponent(imageSizeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabel38)
                    .addGap(18, 18, 18)
                    .addComponent(geoTagLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addContainerGap(496, Short.MAX_VALUE))
    );
    jPanel28Layout.setVerticalGroup(
        jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel28Layout.createSequentialGroup()
            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(imagePathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel32))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel34)
                .addComponent(imageSizeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel38)
                .addComponent(geoTagLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel35)
                .addComponent(imageDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );

    javax.swing.GroupLayout ImagesViewerPanelLayout = new javax.swing.GroupLayout(ImagesViewerPanel);
    ImagesViewerPanel.setLayout(ImagesViewerPanelLayout);
    ImagesViewerPanelLayout.setHorizontalGroup(
        ImagesViewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ImagesViewerPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addGroup(ImagesViewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(imagePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1196, Short.MAX_VALUE)
                .addComponent(jPanel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())
    );
    ImagesViewerPanelLayout.setVerticalGroup(
        ImagesViewerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(ImagesViewerPanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );

    CardPanel.add(ImagesViewerPanel, "imagesViewerCard");

    fileMenu.setText("File");
    fileMenu.setFont(new java.awt.Font("Tahoma", 1, 11));

    exitMenuItem.setText(" Exit");
    exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            exitMenuItemActionPerformed(evt);
        }
    });
    fileMenu.add(exitMenuItem);

    jMenuBar1.add(fileMenu);

    toolsMenu.setText("Tools");
    toolsMenu.setFont(new java.awt.Font("Tahoma", 1, 11));

    windowsMenuItem.setText("Windows Information");
    windowsMenuItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            windowsMenuItemActionPerformed(evt);
        }
    });
    toolsMenu.add(windowsMenuItem);

    recentMenuItem.setText("Recent Documents");
    recentMenuItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            recentMenuItemActionPerformed(evt);
        }
    });
    toolsMenu.add(recentMenuItem);

    jMenuBar1.add(toolsMenu);

    aboutMenu.setText("About");
    aboutMenu.setFont(new java.awt.Font("Tahoma", 1, 11));

    helpMenuItem.setText("Help");
    helpMenuItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            helpMenuItemActionPerformed(evt);
        }
    });
    aboutMenu.add(helpMenuItem);
    aboutMenu.add(jSeparator1);

    aboutMenuItem.setText("About");
    aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            aboutMenuItemActionPerformed(evt);
        }
    });
    aboutMenu.add(aboutMenuItem);

    jMenuBar1.add(aboutMenu);

    setJMenuBar(jMenuBar1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1226, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addComponent(CardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1216, Short.MAX_VALUE)
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(CardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    getAccessibleContext().setAccessibleName("DEM");

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startIndexButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startIndexButtonActionPerformed
        File indexLocation = new File ( index.getIndexLocation() + "\\" + FilesPath.INDEX_PATH );

        startIndexButton.setEnabled(! startIndexButtonFlag);
        stopIndexingButton.setEnabled(startIndexButtonFlag);

        Utilities.removeAllRows(indexTable);
        
        IndexGUIComponent indexGUI = new IndexGUIComponent(progressBar,indexTable,indexDateLbl
            ,timeLbl,currentFileLbl, sizeOfFileLbl, numberOfFilesLbl, fileExtensionLbl, numberOfErrorFilesLbl,bigSizeMsgLbl, startIndexButton,
            stopIndexingButton);

        indexerThread = new IndexerThread(indexLocation,indexGUI,index,imagesPath);
        indexerThread.execute();
    }//GEN-LAST:event_startIndexButtonActionPerformed

    private void startSearchingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startSearchingButtonActionPerformed
        startSearching(index.getExtensionAllowed());
    }//GEN-LAST:event_startSearchingButtonActionPerformed

    private void startSearching (List<String> supportedExtension) {
        removeSearchField(false,false);

        if ( index.getIndexStatus() == false ) {
            JOptionPane.showMessageDialog(this, "please do the indexing operation first before do any operation",
                    "Case is not indexed",JOptionPane.ERROR_MESSAGE );
            return ;
        }

        File indexLocation = new File (index.getIndexLocation() + "\\" + FilesPath.INDEX_PATH);
        String queryString = queryTextField.getText().trim();

        if ( queryString.isEmpty() ) {
            JOptionPane.showMessageDialog(this, "please fill the query string and choose an index location");
            return  ;
        }

        Utilities.packColumns(searchTable, 2);

        searchProgressBard.setIndeterminate(true);

        GUIComponent searchGUI = new GUIComponent(searchProgressBard,searchTable,searchingDateLbl,indexDirLbl2,
            userQueryLbl,searchTime, supportedExtension , clusterPathTree,clusterTypeTree);

         SearcherThread sThread = new SearcherThread(indexLocation,queryString,searchGUI);
         sThread.execute();
    }

    private void clearFieldsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearFieldsButtonActionPerformed
        removeSearchField(true,false);
    }//GEN-LAST:event_clearFieldsButtonActionPerformed

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
    
    private void webHistoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webHistoryButtonActionPerformed
        showPanel("webHistoryCard",mozillaResultPanel);

        // reset mozilla text field
        resetInternetSurfing(mozillaSearchField,mozillaFilterComboBox);

        // read web history and add it to table
         try {
             String path = (String) ffComboBox.getSelectedItem() + "\\" ;
             fillWebHistoryTable(path);

//            // Pack the all columns of the table
//            int margin = 1;
//            Utilities.packColumns(webHistoryTable, margin);
            
         }
       catch (SQLException e){
//              e.printStackTrace();
//             JOptionPane.showMessageDialog(this, "Please Close FireFox Browser Right Now and Try Again",
//                     "FireFox Browser Already Running", JOptionPane.ERROR_MESSAGE);
//             return ;
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (ClassNotFoundException e){
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (InstantiationException e){
            logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (IllegalAccessException e){
            logger.log(Level.SEVERE, "Uncaught exception", e);
       }
    }//GEN-LAST:event_webHistoryButtonActionPerformed

    private void bookmarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookmarButtonActionPerformed
        showPanel("bookmarkCard",mozillaResultPanel);

        // reset mozilla text field
        resetInternetSurfing(mozillaSearchField,mozillaFilterComboBox);
        
        // read bookmar and add it to table
        try {
            String path = (String) ffComboBox.getSelectedItem() + "\\" ;
            fillBookmarkTable(path);
//
//            // Pack the all columns of the table
//            int margin = 1;
//            Utilities.packColumns(bookmarkTable, margin);
            
        }
       catch (SQLException e){
//              e.printStackTrace();
//             JOptionPane.showMessageDialog(this, "Please Close FireFox Browser Right Now and Try Again",
//                     "FireFox Browser Already Running", JOptionPane.ERROR_MESSAGE);
//             return ;
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (ClassNotFoundException e){
             //e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (InstantiationException e){
            //e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (IllegalAccessException e){
            //e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
    }//GEN-LAST:event_bookmarButtonActionPerformed

    private void cookiesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cookiesButtonActionPerformed
       showPanel("cookiesCard",mozillaResultPanel);

       // reset mozilla text field
        resetInternetSurfing(mozillaSearchField,mozillaFilterComboBox);
        
       // fill cookies table
       try {
           String path = (String) ffComboBox.getSelectedItem() + "\\" ;
           fillCookiesTable(path);

//           // Pack the all columns of the table
//           int margin = 1;
//           Utilities.packColumns(cookiesTable, margin);
       }
       catch (SQLException e){
//              e.printStackTrace();
//             JOptionPane.showMessageDialog(this, "Please Close FireFox Browser Right Now and Try Again",
//                     "FireFox Browser Already Running", JOptionPane.ERROR_MESSAGE);
//             return ;
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (ClassNotFoundException e){
             //e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (InstantiationException e){
            //e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (IllegalAccessException e){
            //e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
    }//GEN-LAST:event_cookiesButtonActionPerformed

    private void downloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadButtonActionPerformed
       showPanel("downloadCard",mozillaResultPanel);

        // reset mozilla text field
        resetInternetSurfing(mozillaSearchField,mozillaFilterComboBox);
        
       // fill download table
       try{
           String path = (String) ffComboBox.getSelectedItem() + "\\" ;
           fillDownloadTable(path);

           // Pack the all columns of the table
           //int margin = 1;
           //Utilities.packColumns(downloadTable, margin);

           Utilities.scrollToVisible(downloadTable, 0, 0);
       }
       catch (SQLException e){
//              e.printStackTrace();
//             JOptionPane.showMessageDialog(this, "Please Close FireFox Browser Right Now and Try Again",
//                     "FireFox Browser Already Running", JOptionPane.ERROR_MESSAGE);
//             return ;
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (ClassNotFoundException e){
             //e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (InstantiationException e){
            //e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (IllegalAccessException e){
            //e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
    }//GEN-LAST:event_downloadButtonActionPerformed

    private void logginsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logginsButtonActionPerformed
       showPanel("logginsCard",mozillaResultPanel);

       // reset mozilla text field
        resetInternetSurfing(mozillaSearchField,mozillaFilterComboBox);
        
       // fill loggins table
       try {
           String path = (String) ffComboBox.getSelectedItem() + "\\" ;
           fillLogginsTable(path);

//           // Pack the all columns of the table
//           int margin = 1;
//           Utilities.packColumns(logginsTable, margin);
       }
       catch (SQLException e){
//              e.printStackTrace();
//             JOptionPane.showMessageDialog(this, "Please Close FireFox Browser Right Now and Try Again",
//                     "FireFox Browser Already Running", JOptionPane.ERROR_MESSAGE);
//             return ;
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (ClassNotFoundException e){
             //e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (InstantiationException e){
            //e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (IllegalAccessException e){
            //e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
    }//GEN-LAST:event_logginsButtonActionPerformed

    private void mozillaFilterComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mozillaFilterComboBoxActionPerformed
        // date sorting for all tables in mozilla
        if (mozillaFilterComboBox.getSelectedIndex() == 0  ) {
            Utilities.sortTable(webHistoryTable,0);
            Utilities.sortTable(bookmarkTable,2);
            Utilities.sortTable(cookiesTable,4);
            Utilities.sortTable(logginsTable,3);
        }
        else if ( mozillaFilterComboBox.getSelectedIndex() == 1  ) {
            Utilities.sortTable(webHistoryTable,1);
            Utilities.sortTable(bookmarkTable,1);
            Utilities.sortTable(cookiesTable,0);
            Utilities.sortTable(downloadTable,1);
            Utilities.sortTable(logginsTable,0);
        }
    }//GEN-LAST:event_mozillaFilterComboBoxActionPerformed

    private void webHistoryButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webHistoryButton1ActionPerformed
       showPanel("IEWebHisrotyCard", IEResultPanel);

        // reset IE text field
        resetInternetSurfing(IESearchField,IEFilterComboBox);
        
       try {
           String userPath = (String) ieComboBox.getSelectedItem() ;
           if ( FilesPath.isValidPath(userPath, FilesPath.IE_HISTORY) ) {
               String path = FilesPath.getIEHistoryPath(userPath);

               fillIEWebHistoryTable(path);

               // Pack the all columns of the table
               int margin = 1;
               Utilities.packColumns(IEWebHistoryTable, margin);
           }
           else {
                System.out.println("not found in: " + userPath);
               
                // remove old data
                if ( IEWebHistoryTable.getModel().getRowCount() > 0 )
                     Utilities.removeAllRows(IEWebHistoryTable);
           }
       }
       catch (IOException e){
           e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
       catch (InterruptedException e){
           e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
    }//GEN-LAST:event_webHistoryButton1ActionPerformed

    private void bookmarButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookmarButton1ActionPerformed
       showPanel("IEBookmarkCard", IEResultPanel);

        // reset IE text field
        resetInternetSurfing(IESearchField,IEFilterComboBox);

       try {
           String userPath = (String) ieComboBox.getSelectedItem() ;
           if ( FilesPath.isValidPath(userPath, FilesPath.IE_FAVORITE) ) {
               String path = FilesPath.getIEFavoritePath(userPath);

               fillIEBookmarkTable(path);

               // Pack the all columns of the table
               int margin = 1;
               Utilities.packColumns(IEBookmarkTable, margin);
           }
           else {
                System.out.println("not found in: " + userPath);

                // remove old data
                if ( IEBookmarkTable.getModel().getRowCount() > 0 )
                    Utilities.removeAllRows(IEBookmarkTable);
           }
       }
       catch (IOException e){
           e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
    }//GEN-LAST:event_bookmarButton1ActionPerformed

    private void cookiesButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cookiesButton1ActionPerformed
        showPanel("IECookiesCard", IEResultPanel);

        // reset IE text field
        resetInternetSurfing(IESearchField,IEFilterComboBox);
        try {
            String userPath = (String) ieComboBox.getSelectedItem() ;
            if ( FilesPath.isValidPath(userPath, FilesPath.IE_COOKIES) ) {
                String path = FilesPath.getIECookiesPath(userPath);
                System.out.println("cookies path: " + path);

                fillIECookiesTable(path);

                // Pack the all columns of the table
                int margin = 1;
                Utilities.packColumns(IECookiesTable, margin);
            }
            else {
                System.out.println("not found in: " + userPath);

                // remove old data
                if ( IECookiesTable.getModel().getRowCount() > 0 )
                     Utilities.removeAllRows(IECookiesTable);
           }
        }
        catch (IOException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }//GEN-LAST:event_cookiesButton1ActionPerformed

    private void cacheButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cacheButtonActionPerformed
        showPanel("IECacheCard", IEResultPanel);

        // reset IE text field
        resetInternetSurfing(IESearchField,IEFilterComboBox);
        try {
            String userPath = (String) ieComboBox.getSelectedItem() ;
            if ( FilesPath.isValidPath(userPath, FilesPath.IE_CACHE) ) {
                String path = FilesPath.getIECachePath(userPath);
                //System.out.println("cache: " + path);

                fillIECacheTable(path);

                // Pack the all columns of the table
                int margin = 1;
                Utilities.packColumns(IECacheTable, margin);
            }
            else {
                System.out.println("not found in: " + userPath);

                // remove old data
                if ( IECacheTable.getModel().getRowCount() > 0 )
                     Utilities.removeAllRows(IECacheTable);
           }
        }
        catch (IOException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }//GEN-LAST:event_cacheButtonActionPerformed

    private void logginsButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logginsButton1ActionPerformed
        showPanel("IELogginsCard", IEResultPanel);
        
        // reset IE text field
        resetInternetSurfing(IESearchField,IEFilterComboBox);
        try {
            String p = FilesPath.getIEPassword();
            System.out.println("loggin: " + p);

            fillIELogginsTable(p);

           // Pack the all columns of the table
           int margin = 1;
           Utilities.packColumns(IELogginsTable, margin);
        }
        catch (IOException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
        catch (InterruptedException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }//GEN-LAST:event_logginsButton1ActionPerformed

    private void resetInternetSurfing (JTextField f, JComboBox box){
        f.setText("");
        box.setSelectedIndex(0);
    }
    
    private void IEFilterComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IEFilterComboBoxActionPerformed
        // date sorting for all tables in IE
        if (IEFilterComboBox.getSelectedIndex() == 0  ) {
            Utilities.sortTable(IEWebHistoryTable,3);
            Utilities.sortTable(IECacheTable,0);
            Utilities.sortTable(IECookiesTable,0);
        }
        else if ( IEFilterComboBox.getSelectedIndex() == 1  ) {
            Utilities.sortTable(IEWebHistoryTable,0);
            Utilities.sortTable(IEBookmarkTable,0);
            Utilities.sortTable(IECookiesTable,1);
            Utilities.sortTable(IECacheTable,1);
            Utilities.sortTable(IELogginsTable,0);
        }
    }//GEN-LAST:event_IEFilterComboBoxActionPerformed

    private void searchTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchTableMouseClicked
        // set summary panel
        try {
            if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
                showPopup(evt);
                return ;
            }

            // other click event
            int row = searchTable.getSelectedRow();
            String filePath = (String) searchTable.getValueAt(row, 0);

            showInformation(filePath);
        }
        catch (Exception e ){
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }//GEN-LAST:event_searchTableMouseClicked

    private void showInformation (String filePath) throws Exception {
        File tmp = new File(filePath);
        String keyword = queryTextField.getText().trim();

        searchFileNameLbl.setText(tmp.getName());
        searchFileExtensionLbl.setText(Utilities.getExtension(tmp));
        searchFileSizeLbl.setText(tmp.length() + "");

        // set summary panel and file rendered panel depen on file
        if (Utilities.getExtension(tmp).equalsIgnoreCase("txt")) {
            String content = Utilities.getFileContent(tmp) ;
            String highlither = "<span style=\"background-color: #FFFF00\">" + keyword +  "</span>" ;
            String rep = content.replace(keyword, highlither);
            fileBrowser.setHTMLContent(rep);
        }
        else if ( Utilities.getExtension(tmp).equalsIgnoreCase("pdf") ) {
            fileBrowser.navigate(filePath + "#search= " + keyword + "");
        }
        else if ( Utilities.getExtension(tmp).equalsIgnoreCase("html") ||
                  Utilities.getExtension(tmp).equalsIgnoreCase("htm") ||
                  Utilities.getExtension(tmp).equalsIgnoreCase("mht") ) {
            fileBrowser.navigate(filePath);
        }
        else {
            fileBrowser.navigate(filePath);
        }

        // show matadata information for file
        String metaData = MetaDataExtraction.getMetaData(filePath);
        metaDataTextArea.setText(metaData);

        fileRenderPanel.validate();
    }

    private void tagSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagSelectButtonActionPerformed
        try {
            if ( index.getIndexStatus() == false ) {
                JOptionPane.showMessageDialog(this, "please do the indexing operation first before do any operation",
                    "Case is not indexed",JOptionPane.ERROR_MESSAGE );
                return ;
            }

            // remove data
            Utilities.removeAllRows(cloudsTable);
            tagsPanel.removeAll();

            tagsPanel.repaint();
            tagsPanel.validate();

//            IndexerReader ir  = new IndexerReader(index.getIndexLocation() + "\\" +  FilesPath.INDEX_PATH);
//            HashMap<String,Integer> tagsMap = ir.getAllTermFreqFromBody();
//            ir.close();
//            setTags(tagsMap);

            InfiniteProgressPanel i = new InfiniteProgressPanel("Loading Index Tags Clouds...");
            this.setGlassPane(i);
            i.start();

            String indexPath = index.getIndexLocation() + "\\" + FilesPath.INDEX_PATH;
            String indexName = index.getIndexName() ;

            IndexReaderThread thread = new IndexReaderThread(i, indexPath, indexName, IndexReaderThread.IndexItem.TAGS, OfflineMinningFrame.this);
            thread.execute();

            tagsPanel.repaint();
            tagsPanel.validate();
        }
        catch (NumberFormatException n){
            JOptionPane.showMessageDialog(this, "number is not correct",
                        "integer number is no correct", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.SEVERE, "Uncaught exception", n);
        }
        catch (IOException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }//GEN-LAST:event_tagSelectButtonActionPerformed

    private void indexVisulizingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_indexVisulizingButtonActionPerformed
        try {
            if ( index.getIndexStatus() == false ) {
                JOptionPane.showMessageDialog(this, "please do the indexing operation first before do any operation",
                    "Case is not indexed",JOptionPane.ERROR_MESSAGE );
                return ;
            }

            indexVisualizingPiePanel.removeAll();

            String indexPath = index.getIndexLocation() + "\\" + FilesPath.INDEX_PATH;
            String indexName = index.getIndexName() ;
            
            InfiniteProgressPanel i = new InfiniteProgressPanel("Loading Index Extensions ...");
            this.setGlassPane(i);
            i.start();

            IndexReaderThread thread = new IndexReaderThread(i, indexPath, indexName, IndexReaderThread.IndexItem.EXT, OfflineMinningFrame.this);
            thread.execute();

            indexVisualizingPiePanel.repaint();
            indexVisualizingPiePanel.validate();
        }
        catch (IOException e){
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }//GEN-LAST:event_indexVisulizingButtonActionPerformed

    public void setIndexVisualizationPanel (JPanel panel) {
        indexVisualizingPiePanel.add( panel ,BorderLayout.CENTER);
        indexVisualizingPiePanel.validate();
    }

    public void setTags (HashMap<String,Integer> tagsMap) {
        int excludeNumber = Integer.parseInt(tagsExcludeTextField.getText().trim());
        int tagsNumber = Integer.parseInt(tagsNumberTextField.getText().trim());

        if ( excludeNumber < 0 ) {
            logger.log(Level.INFO, "exlude number less than zero");
            JOptionPane.showMessageDialog(this, "number is not correct","please enter valid integer", JOptionPane.ERROR_MESSAGE);

            return;
        }

        if ( tagsNumber < 0 ) {
            logger.log(Level.INFO, "tags number less than zero");
            JOptionPane.showMessageDialog(this, "number is not correct","please enter valid integer", JOptionPane.ERROR_MESSAGE);

            return ;
        }
            
        if ( tagsNumber > tagsMap.size()) {
            JOptionPane.showMessageDialog(this, "number is greater than words in index",
                    "Too Much Input", JOptionPane.ERROR_MESSAGE);
            tagsNumberTextField.setText( (tagsMap.size()/3) + "");
            return ;
        }

        // create cloud
        Cloud cloud = new Cloud();
        cloud.setMaxWeight(50.0);
        cloud.setThreshold(excludeNumber); //show just tags with this number
        cloud.setMaxTagsToDisplay(tagsNumber);

        Set set = tagsMap.entrySet();
        Iterator itr= set.iterator();
        while ( itr.hasNext() ) {
            Map.Entry me = (Map.Entry) itr.next();

            String text = (String) me.getKey();
            int value   = (Integer)me.getValue();

             ((DefaultTableModel) cloudsTable.getModel() ).addRow( new Object[]
                { text , value }
             );

            Tag tag = new Tag(text, value);
            tag.setLink("Term: " + text + " Frequnecy: " + value);
            tag.setScore(value);

            cloud.addTag(tag);
        }

        List<Tag> tags = null;

        if ( tagsDisplayComboBox.getSelectedIndex() == 0 )
            tags = cloud.tags(new Tag.NameComparatorAsc() ) ;
        else if ( tagsDisplayComboBox.getSelectedIndex() == 1)
            tags = cloud.tags(new Tag.NameComparatorDesc() );
        else if ( tagsDisplayComboBox.getSelectedIndex() == 2)
            tags = cloud.tags( new Tag.ScoreComparatorAsc() );
        else if ( tagsDisplayComboBox.getSelectedIndex() == 3)
            tags = cloud.tags( new Tag.ScoreComparatorDesc());

        for (Tag tag: tags) {
            JLabel lbl = new JLabel(tag.getName());
            lbl.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, tag.getWeightInt()));
            lbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed (MouseEvent e) {
                    JLabel m = (JLabel) e.getSource();
                    doSearch(m.getText());
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    JLabel m = (JLabel) e.getSource();
                    doSearch(m.getText());
                }

                public void doSearch(String text){
                    fileSystemTappedPane.setSelectedIndex(1);
                    queryTextField.setText(text);
                }
            });

            lbl.setToolTipText(tag.getName() + " repeated: " + tag.getScoreInt());
            lbl.setForeground(java.awt.Color.BLUE.darker());
            lbl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            tagsPanel.add(lbl);
            lbl = null ;
        }
    }
    
    private ArrayList<String> searchEmail () {
        ArrayList<String> aList = new ArrayList<String>();
        
        try {
            File indexLocation = new File (index.getIndexLocation() + "\\" + FilesPath.INDEX_PATH);
            PSTSearcher pSearcher = new PSTSearcher(indexLocation);
            int len = pSearcher.search(emailSearchTextField.getText().trim(), PSTSearcher.SearchField.CONTENT);
            System.out.println("length of search = " + len);
           
            for (String path: index.getPstPath()) {
                if ( path.equals(outlookComboBox.getSelectedItem())) {
                    for (int i=0 ; i<len ; i++){
                        Document doc = pSearcher.getDocHits(i);
                        int id =  Integer.parseInt(doc.get("mailid")) ;
                        String location =  doc.get("location") ;
                        PSTMessage msg = null;

                        try {
                            msg = getMessage(id);
                        }
                        catch (PSTException e) {
                            
                        }

                        if ( msg != null ) {
                            aList.add(String.valueOf(msg.getDescriptorNode().descriptorIdentifier));
                        }
                        else {
                            System.out.println("msg == null (3701) ");
                        }
                    }

                    pSearcher.closeSearcher();
                    return aList ;
                }
            }
        }
        catch (Exception e){
            //e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);

        }

        if ( ! aList.isEmpty())
             return aList ;
        
        return null ;
    }

    private void filterEmailTable (final JTable table, final ArrayList<String> aList) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);

        sorter.setRowFilter( new RowFilter<TableModel, Object>() {
            public boolean include (Entry entry) {
                String row = String.valueOf(entry.getValue(0));
                
                if ( aList.contains(row))
                    return true;
                else
                    return false;

            }
        });
    }
        
    private void msnChatTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_msnChatTreeValueChanged
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) msnChatTree.getLastSelectedPathComponent();

        if ( node == null || node.isRoot() )
            return ;

        String parent = node.getParent().toString();

        if ( FilesPath.getOSType() == FilesPath.OS_TYPE.XP){
            String value = msnParser.getAllUserLoggingPath().get(parent);

            if ( value != null ) {
                File file = new File( msnParser.getFileFromPath(value,node.getUserObject().toString()));
                if ( file.exists() ) {
                    msnChat.navigate(file.getAbsolutePath());
                    msnChatContentPanel.revalidate();
                }
            }
            else {
                if ( ! node.isLeaf() )
                    msnChat.setHTMLContent("");
                else
                    msnChat.setHTMLContent("there is no history for this user");

                msnChatContentPanel.revalidate();
            }
        }
        else {
            File file = new File( msnComboBox.getSelectedItem() + "\\" + node.getUserObject().toString());
            if ( file.exists() ) {
                msnChat.navigate(file.getAbsolutePath());
                msnChatContentPanel.revalidate();
            }
        }
    }//GEN-LAST:event_msnChatTreeValueChanged

    private void yahooChatTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_yahooChatTreeValueChanged
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) yahooChatTree.getLastSelectedPathComponent();

        if ( node == null || node.isRoot() || ! node.isLeaf()) {
            return ;
        }

        String parent = node.getParent().toString();
        String current= node.getUserObject().toString();
        
        try {
            String path = (String) yahooComboBox.getSelectedItem() ;
            ArrayList<ArrayList<YahooMessage>> msgs  = YahooMessageReader.getInstance(path).get(parent).get(current) ;
            if ( msgs == null ) {
                return ;
            }

            StringBuilder text = new StringBuilder("");
            
            for (ArrayList<YahooMessage> aList: msgs) {
                for (YahooMessage msg: aList) {
                    if ( msg.getMessagePath() == YahooMessage.MESSAGE_PATH.SOURCE_TO_DEST ) {
                        text.append(msg.getProfileName() + " at (" );
                    }
                    else {
                        text.append(msg.getDestinationName() + " at (" );
                    }

                    text.append( Utilities.formatDateTime(msg.getTimeStamp()) + ") :  " );
                    
                    byte[] cipher = msg.getCipherText();
                    String name   = msg.getProfileName();
                    byte[] plainText  = YahooMessageDecoder.decode(cipher,name);

                    String plain = new String(plainText, "UTF-8");
                    text.append( plain );
                    text.append("<br>");
                }

                text.append("<br>");
                text.append("<br>");
            }

            yahooChat.setHTMLContent(text.toString());
            yahooChatContentPanel.revalidate();
        }
        catch (IOException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);

        }
    }//GEN-LAST:event_yahooChatTreeValueChanged

    private void keywordsListButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keywordsListButtonActionPerformed
        AdvancedSearchDialog asd = new AdvancedSearchDialog(OfflineMinningFrame.this, true, index.getExtensionAllowed());
        asd.setVisible(true);

        String query = asd.getQuery() ;
        List<String> ext = asd.getSupportedExtensions() ;

        if ( query == null || query.isEmpty() )
            return ;

        if ( ext == null || ext.size() == 0)
            return ;

        queryTextField.setText(query);
        startSearching(ext);
    }//GEN-LAST:event_keywordsListButtonActionPerformed
    
    private void loadFFButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadFFButtonActionPerformed
        String path = (String) ffComboBox.getSelectedItem() + "\\" ;
        
        try {
            fillWebHistoryTable(path);
            fillBookmarkTable(path);
            fillCookiesTable(path);
            fillDownloadTable(path);
            fillLogginsTable(path);
        }
        catch (SQLException e){
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
        catch (ClassNotFoundException e){
             logger.log(Level.SEVERE, "Uncaught exception", e);
        }
        catch (InstantiationException e){
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
        catch (IllegalAccessException e){
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }//GEN-LAST:event_loadFFButtonActionPerformed

    private void loadIEButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadIEButtonActionPerformed
        String path = (String) ieComboBox.getSelectedItem();
        System.out.println("path: " + path);
    }//GEN-LAST:event_loadIEButtonActionPerformed

    private void loadMSNButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadMSNButtonActionPerformed
        String path = (String) msnComboBox.getSelectedItem();

        try {
            fillMessangerTree(path);
            msnChatTree.setModel(new DefaultTreeModel(rootMSNNode));
        }
        catch (IOException e){
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }//GEN-LAST:event_loadMSNButtonActionPerformed

    public void fillMessangerTree (String path) throws IOException {
        rootMSNNode = new DefaultMutableTreeNode("Windows Live Messanger");

        if ( FilesPath.getOSType() == FilesPath.OS_TYPE.XP) {
            path = path.split("\\\\")[0] + "\\" + "WINDOWS\\System32\\reg.exe" ;

            msnParser = new MSNParser(path);
            msnParser.parse();

            HashMap<String,String> map = msnParser.getAllUserLoggingPath();
            ArrayList<String> list = msnParser.getAllUserLoggingToMsn();

            DefaultMutableTreeNode historyNode = new DefaultMutableTreeNode("Users Have Histories");
            DefaultMutableTreeNode normalNode  = new DefaultMutableTreeNode("Users Without Histories");

            rootMSNNode.add(historyNode);
            Set set = map.entrySet();
            Iterator itr = set.iterator();
            while ( itr.hasNext() ) {
                Map.Entry me = (Map.Entry) itr.next();

                String key = (String) me.getKey();
                String value = (String) me.getValue();

                DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(key) ;
                historyNode.add(subNode);
                addOtherUserRelatedToKey(subNode,value);
            }

            rootMSNNode.add(normalNode);
            for (int i=0 ; i<list.size() ; i++)
                normalNode.add(new DefaultMutableTreeNode(list.get(i)));
        }
        else {
            DefaultMutableTreeNode historyNode = new DefaultMutableTreeNode("Users Have Histories");
            rootMSNNode.add(historyNode);

            ArrayList<String> list = getUsersName(path);
            for (String name: list) {
                DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(name) ;
                historyNode.add(subNode);
            }
            
        }
    }

    private ArrayList<String> getUsersName (String path) {
        File[] files = getAllFiles(path);
        ArrayList<String> names = new ArrayList<String>();

        for(File f: files) {
            String name = f.getName();
            names.add(name);
        }

        return names;
    }

    private File[] getAllFiles (String path) {
        File dir = new File(path);

        File[] files = dir.listFiles( new FilenameFilter() {
                public boolean accept (File dir, String name) {
                    return name.toLowerCase().endsWith(".xml");
                }
            }
        );

        return (files);
    }

    private void addOtherUserRelatedToKey (DefaultMutableTreeNode node , String path) throws IOException {
        File[] files = msnParser.getAllFiles(path);

        for (File f: files) {
            DefaultMutableTreeNode tmpNode = new DefaultMutableTreeNode(msnParser.getFileName(f.getName()));
            node.add(tmpNode);
        }
    }
    
    private void loadYahooButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadYahooButtonActionPerformed
        String path = (String) yahooComboBox.getSelectedItem();
        
        try {
            fillYahooTree(path);
            yahooChatTree.setModel(new DefaultTreeModel(rootYahooNode));
        }
        catch (IOException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }  
    }//GEN-LAST:event_loadYahooButtonActionPerformed

    public void fillYahooTree (String path) throws IOException {
        rootYahooNode = new DefaultMutableTreeNode("Yahoo! Chat");
        HashMap<String, HashMap<String, ArrayList<ArrayList<YahooMessage>>>> map = YahooMessageReader.getInstance(path);

        for (Map.Entry<String,HashMap<String,ArrayList<ArrayList<YahooMessage>>>> mapEntry: map.entrySet() ) {
            String currentUserName = mapEntry.getKey();
            DefaultMutableTreeNode nameNode = new DefaultMutableTreeNode(currentUserName);
            rootYahooNode.add(nameNode);


            for(Map.Entry<String,ArrayList<ArrayList<YahooMessage>>> subMapEntry: mapEntry.getValue().entrySet()) {
                String otherUserName = subMapEntry.getKey();
                DefaultMutableTreeNode subNodeName = new DefaultMutableTreeNode(otherUserName);
                nameNode.add(subNodeName);
            }
        }
    }

    private void loadSkypeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadSkypeButtonActionPerformed
        String path = (String) skypeComboBox.getSelectedItem();
        
        try {
            if ( skypeTable.getModel().getRowCount() > 0 )
                 Utilities.removeAllRows(skypeTable);
            
            fillSkypeTree(path);
            skypeChatTree.setModel(new DefaultTreeModel(rootSkypeNode));
        }
        catch (IOException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }//GEN-LAST:event_loadSkypeButtonActionPerformed

    private void skypeChatTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_skypeChatTreeValueChanged
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) skypeChatTree.getLastSelectedPathComponent();

        if ( node == null || node.isRoot() || ! node.isLeaf()) {
            return ;
        }

        String current= node.getUserObject().toString();

        try {
            String path = (String) skypeComboBox.getSelectedItem() ;
            SkypeParser parser = new SkypeParser();
            ArrayList<Tuple<String, ArrayList<SkypeMessage>>> msgs = parser.parseSkypeFile(path);

            for (Tuple<String, ArrayList<SkypeMessage>> user: msgs) {
                if ( user.getA().equals(current)) {
                    DefaultTableModel model = (DefaultTableModel) skypeTable.getModel();
                    for (SkypeMessage msg: user.getB()) {
                        model.addRow(new Object[] {
                            msg.getAuther(),msg.getPartner(), msg.getDate(), msg.getMessageText()
                        });
                    }
                    return ;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }//GEN-LAST:event_skypeChatTreeValueChanged

    private void windowsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_windowsMenuItemActionPerformed
        try {
            ArrayList<String> data = Utilities.readProgramOutputStream("systeminfo.exe");

            WindowsInfoDialog wid = new WindowsInfoDialog(mainFrame, true, data);
            wid.setVisible(true);
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }//GEN-LAST:event_windowsMenuItemActionPerformed

    private void recentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recentMenuItemActionPerformed
        RecentDialog rd = new RecentDialog(mainFrame, true);
        rd.setVisible(true);
    }//GEN-LAST:event_recentMenuItemActionPerformed

    private void summaryInternetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_summaryInternetButtonActionPerformed
        ArrayList<InternetSummaryDate> result = new ArrayList<InternetSummaryDate>();
        String resultString = "" ;

        ArrayList<String> ffPath = index.getFFPath();
        if ( ffPath.size() == 0 )
            resultString += "FireFox is not Selected when create case\n" ;

        for (String path: ffPath) {
            try {
                result.addAll(getFFSummary(path));
            }
            catch (Exception e) {
                logger.log(Level.SEVERE, "Uncaught exception", e);
            }
        }

        ArrayList<String> iePath = index.getIePath();
        if ( iePath.size() == 0 )
            resultString += "IE is not Selected when create case" ;
        
        for (String path: iePath ) {
            try {
                result.addAll(getIESummary(path));
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Uncaught exception", ex);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, "Uncaught exception", ex);
            }
        }

        if ( result.size() == 0 ) {
            JOptionPane.showMessageDialog(this,resultString,"Their is no data to display",
                    JOptionPane.ERROR_MESSAGE);
            return ;
        }

        if ( summaryTable.getModel().getRowCount() > 0 )
            Utilities.removeAllRows(summaryTable);
        
        for (InternetSummaryDate data: result) {
            ((DefaultTableModel)summaryTable.getModel()).addRow( new Object[] {
                data.getURL(), data.getNumberOfVisit(), data.getDate()
            } );
        }
    }//GEN-LAST:event_summaryInternetButtonActionPerformed

    private void showImages () throws IOException {
        imagePanel.removeAll();
        checkImageViewerButtons();
        
        for(int i=imageIndex; i<imageIndex + (4*4) ; i++ ) {
            try {
                String name = ImageViewer.getInstance(index).get(i);
                File imageFile = new File(name);
                BufferedImage myPicture = ImageIO.read(imageFile);
                BufferedImage newPictue = createResizedCopy(myPicture,30,30,false);

                ImageLabel picLabel = new ImageLabel( name, new ImageIcon( newPictue ));
                picLabel.addMouseListener( new MouseAdapter() {
                    @Override
                    public void mouseEntered (java.awt.event.MouseEvent event) {
                        ImageLabel lbl = (ImageLabel) event.getSource();
                        setImageInformation(lbl.getPath());
                    }

                    @Override
                    public void mousePressed (java.awt.event.MouseEvent event){
                        if ( (event.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
                            showPopupOpen(event);
                        }
                    }
                });
                imagePanel.add(picLabel);
            }
            catch (IndexOutOfBoundsException e) {logger.log(Level.SEVERE, "Uncaught exception", e); }
        }

        // change border title name
        ( (TitledBorder) imagePanel.getBorder() ).setTitle("Images Found Page(" + currentImagePage + "/" +
                totalImagePage + ")");

        imagePanel.repaint();
        imagePanel.revalidate();
    }

    private void setImageInformation (String path) {
        File file = new File(path);

        imagePathLabel.setText( file.getAbsolutePath());
        imageSizeLabel.setText( Utilities.toKB(file.length()) + " KB" );
        imageDateLabel.setText( new Date(file.lastModified()) + "");

        try {
            if ( GeoTagging.hasGoeTag(path) )
                geoTagLbl.setText("Yes");
            else
                geoTagLbl.setText("No");
        }
        catch (Exception e) {logger.log(Level.SEVERE, "Uncaught exception", e);}
        
    }

    private BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
                g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();

        return scaledBI;
    }
    
    private void showImagesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showImagesButtonActionPerformed
        try {
            if ( index.getIndexStatus() == false ) {
                JOptionPane.showMessageDialog(this, "please do the indexing operation first before do any operation",
                    "Case is not indexed",JOptionPane.ERROR_MESSAGE );
                return ;
            }

            imageIndex = 0;
            totalImagePage = getNumberOfImages() / 16 ;
            currentImagePage = 0 ;
            
            showImages();
        }
        catch (IOException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }//GEN-LAST:event_showImagesButtonActionPerformed

    private int getNumberOfImages () {
        int count = 0 ;
        
        try {
            for(int i=imageIndex;  ; i++ ) {
                ImageViewer.getInstance(index).get(i);
                count++;
            }
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }

        return count ;
    }

    private void checkImageViewerButtons () {
        if ( currentImagePage == 0 ) {
            nextPageButton.setEnabled(true);
            prePageButton.setEnabled(false);
        }
        else if ( currentImagePage == totalImagePage ) {
            nextPageButton.setEnabled(false);
            prePageButton.setEnabled(true);
        }
        else {
            nextPageButton.setEnabled(true);
            prePageButton.setEnabled(true);
        }
    }
    
    private void nextPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextPageButtonActionPerformed
        try {
            if ( index.getIndexStatus() == false ) {
                JOptionPane.showMessageDialog(this, "please do the indexing operation first before do any operation",
                    "Case is not indexed",JOptionPane.ERROR_MESSAGE );
                return ;
            }

            if (  imageIndex != ImageViewer.getInstance(index).size() ) {
                imageIndex += 16;
            }

            if (currentImagePage < totalImagePage ) {
                currentImagePage++;
            }
            
            showImages();
        }
        catch (IOException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }//GEN-LAST:event_nextPageButtonActionPerformed

    private void prePageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prePageButtonActionPerformed
        try {
            if ( index.getIndexStatus() == false ) {
                JOptionPane.showMessageDialog(this, "please do the indexing operation first before do any operation",
                    "Case is not indexed",JOptionPane.ERROR_MESSAGE );
                return ;
            }

            if ( imageIndex != 0 ) {
                imageIndex -= 16 ;
            }

            if ( currentImagePage > 0 )
                currentImagePage-- ;
            
            showImages();
        }
        catch (IOException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
    }//GEN-LAST:event_prePageButtonActionPerformed

    private void clusterPathTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_clusterPathTreeValueChanged
        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) clusterPathTree.getLastSelectedPathComponent();
            if ( node == null || node.isRoot() || ! node.isLeaf()) {
                return ;
            }
            
            String filePath= node.getUserObject().toString();
            showInformation(filePath);
       }
       catch (Exception e ){
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
    }//GEN-LAST:event_clusterPathTreeValueChanged
    
    private void emailVisualizationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailVisualizationButtonActionPerformed
        // from date to date
        String from = Utilities.formatDate( ((JDateChooser) fromDatePanel.getComponent(0)).getDate() );
        String to   = Utilities.formatDate( ((JDateChooser) toDatePanel.getComponent(0)).getDate() );

        if ( outlookComboBox.getSelectedIndex() < 0 )
            return ;

        String path = (String) outlookComboBox.getSelectedItem() ;

        try {
            pstFile = new PSTFile(path);

            logger.log(Level.INFO, "Starting EmailVisualizationThread Now!");

            int selectedIndex = correlationComboBox.getSelectedIndex();

            switch ( selectedIndex ) {
                case 0 :
                    showVisualization(from, to, pstFile, path, "Inbox Visualization...", "Inbox", EmailVisualizationThread.FolderType.INBOX);
                    break;

                case 1 :
                    showVisualization(from, to, pstFile, path, "Sent Items Visualization...", "Sent Items", EmailVisualizationThread.FolderType.SENT);
                    break;

                case 2 :
                    showVisualization(from, to, pstFile, path, "Email Service Provider Visualization...", "ESP", EmailVisualizationThread.FolderType.ESP);
                    break;

                case 3 :
                    showVisualization(from, to, pstFile, path, "Location Visualization...", "Location", EmailVisualizationThread.FolderType.LOCATION);
                    break;

                case 4 :
                    showVisualization(from, to, pstFile, path, "Messages Communication Visualization...", "Frequency", EmailVisualizationThread.FolderType.FREQUENCY);
                    break;
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        } catch (PSTException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        } catch (IOException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
}//GEN-LAST:event_emailVisualizationButtonActionPerformed

    private void showVisualization (String from, String to, PSTFile pst, String path, String title, String folderName,EmailVisualizationThread.FolderType type) {
        InfiniteProgressPanel i = new InfiniteProgressPanel(title);
        this.setGlassPane(i);
        i.start();

        EmailVisualizationThread thread = new EmailVisualizationThread(OfflineMinningFrame.this, i, folderName , pst, path, from, to, type);
        thread.execute();
    }
    
    private void correlationComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_correlationComboBoxActionPerformed
        if ( correlationComboBox.getSelectedIndex() == 0 )
            showPanel("inboxCard", correlationResultPanel);
        else if ( correlationComboBox.getSelectedIndex() == 1 )
            showPanel("sentItemCard",correlationResultPanel);
        else if ( correlationComboBox.getSelectedIndex() == 2 )
            showPanel("espCard",correlationResultPanel);
        else if ( correlationComboBox.getSelectedIndex() == 3)
            showPanel("relationCard", correlationResultPanel);
        else if ( correlationComboBox.getSelectedIndex() == 4 )
            showPanel("locationCard", correlationResultPanel);
        else if ( correlationComboBox.getSelectedIndex() == 5)
            showPanel("messageFrequencyCard", correlationResultPanel);
}//GEN-LAST:event_correlationComboBoxActionPerformed

    private void loadPstButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadPstButtonActionPerformed
        try {
            this.invalidate();
            
            if ( outlookComboBox.getSelectedIndex() < 0 ) {
                logger.log(Level.INFO, "Please Select An OST File");
                return ;
            }

            String path = (String) outlookComboBox.getSelectedItem();
            pstFile = new PSTFile(path);

            setOutlookHandling(pstFile, path);
            setOutlookEvent();
            
            emailTable.setModel(emailTableModel);
            DefaultTreeModel model = new DefaultTreeModel(top);
            folderTree.setModel(model);

            // time consuming statemnt
            InfiniteProgressPanel i = new InfiniteProgressPanel("Loading MailBox");
            this.setGlassPane(i);
            i.start();

            logger.log(Level.INFO, "Starting EmailReaderThread Now!");

            // run in thread and show progressbar
            EmailReaderThread erThread = new EmailReaderThread(path,pstFile,i);
            erThread.execute();
            
        } catch (FileNotFoundException  e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        } catch (PSTException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        } catch (IOException e){
            e.printStackTrace();
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
}//GEN-LAST:event_loadPstButtonActionPerformed

    private void emailSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailSearchButtonActionPerformed

        if ( index.getIndexStatus() == false ) {
            JOptionPane.showMessageDialog(this, "please do the indexing operation first before do any operation",
                "Case is not indexed",JOptionPane.ERROR_MESSAGE );
            return ;
        }

        if ( emailSearchTextField.getText().trim().isEmpty() ) {
            JOptionPane.showMessageDialog(this, "please write the keyword you want to search",
                "missing search key",JOptionPane.ERROR_MESSAGE );
            return ;
        }

        ArrayList<String> aList = searchEmail();

        if ( aList != null) {
            filterEmailTable(emailTable,aList);
        }
}//GEN-LAST:event_emailSearchButtonActionPerformed

    private void searchTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( searchTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_searchTableMousePressed

    private void searchTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( searchTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_searchTableMouseReleased

    private void cloudsTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cloudsTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( cloudsTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_cloudsTableMousePressed

    private void cloudsTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cloudsTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( cloudsTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_cloudsTableMouseReleased

    private void summaryTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_summaryTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( summaryTable.isEnabled() )
                showPopupWithLunch(evt);
        }
    }//GEN-LAST:event_summaryTableMousePressed

    private void summaryTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_summaryTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( summaryTable.isEnabled() )
                showPopupWithLunch(evt);
        }
    }//GEN-LAST:event_summaryTableMouseReleased

    private void webHistoryTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_webHistoryTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( webHistoryTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_webHistoryTableMousePressed

    private void webHistoryTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_webHistoryTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( webHistoryTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_webHistoryTableMouseReleased

    private void bookmarkTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookmarkTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( bookmarkTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_bookmarkTableMouseReleased

    private void bookmarkTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookmarkTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( bookmarkTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_bookmarkTableMousePressed

    private void cookiesTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cookiesTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( cookiesTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_cookiesTableMousePressed

    private void cookiesTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cookiesTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( cookiesTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_cookiesTableMouseReleased

    private void downloadTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_downloadTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( downloadTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_downloadTableMousePressed

    private void downloadTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_downloadTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( downloadTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_downloadTableMouseReleased

    private void logginsTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logginsTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( logginsTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_logginsTableMousePressed

    private void logginsTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logginsTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( logginsTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_logginsTableMouseReleased

    private void IEWebHistoryTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IEWebHistoryTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( IEWebHistoryTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_IEWebHistoryTableMousePressed

    private void IEWebHistoryTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IEWebHistoryTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( IEWebHistoryTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_IEWebHistoryTableMouseReleased

    private void IEBookmarkTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IEBookmarkTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( IEBookmarkTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_IEBookmarkTableMousePressed

    private void IEBookmarkTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IEBookmarkTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( IEBookmarkTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_IEBookmarkTableMouseReleased

    private void IECookiesTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IECookiesTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( IECookiesTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_IECookiesTableMousePressed

    private void IECookiesTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IECookiesTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( IECookiesTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_IECookiesTableMouseReleased

    private void IECacheTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IECacheTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( IECacheTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_IECacheTableMousePressed

    private void IECacheTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IECacheTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( IECacheTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_IECacheTableMouseReleased

    private void IELogginsTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IELogginsTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( IELogginsTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_IELogginsTableMousePressed

    private void IELogginsTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_IELogginsTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( IELogginsTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_IELogginsTableMouseReleased

    private void skypeTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_skypeTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( skypeTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_skypeTableMousePressed

    private void skypeTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_skypeTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( skypeTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_skypeTableMouseReleased

    private void inboxTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inboxTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( inboxTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_inboxTableMousePressed

    private void inboxTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inboxTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( inboxTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_inboxTableMouseReleased

    private void sentItemTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sentItemTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( sentItemTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_sentItemTableMousePressed

    private void sentItemTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sentItemTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( sentItemTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_sentItemTableMouseReleased

    private void espTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_espTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( espTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_espTableMousePressed

    private void espTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_espTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( espTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_espTableMouseReleased

    private void locationTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_locationTableMousePressed
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( locationTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_locationTableMousePressed

    private void locationTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_locationTableMouseReleased
        if ( (evt.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( locationTable.isEnabled() )
                showPopup(evt);
        }
    }//GEN-LAST:event_locationTableMouseReleased

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        this.dispose();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpMenuItemActionPerformed
        JOptionPane.showMessageDialog(this, "please return to CoEIA web site",
                "to get more help", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_helpMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        JOptionPane.showMessageDialog(this, "All right is reserved to CoEIA 2010",
                "about product", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void ffSummaryButttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ffSummaryButttonActionPerformed
        showPanel("ffSummaryCard",mozillaResultPanel);

        // reset mozilla text field
        resetInternetSurfing(mozillaSearchField,mozillaFilterComboBox);

         try {
            // get profile path
            String path = (String) ffComboBox.getSelectedItem() + "\\" ;

//            // remove all data in tables
//            if ( topHostTable.getModel().getRowCount() > 0 )
//                Utilities.removeAllRows(topHostTable);
//            if ( topURLTable.getModel().getRowCount() > 0 )
//                Utilities.removeAllRows(topURLTable);
//            if ( searchEngineTable.getModel().getRowCount() > 0)
//                Utilities.removeAllRows(searchEngineTable);

            // fill summary data
            fillTopHostTable(path);
            fillTopURLTable(path);
            fillSearchEngineTable(path);
            
            // Pack the all columns of the table
//            int margin = 1;
//            Utilities.packColumns(topHostTable, margin);
//            Utilities.packColumns(topURLTable, margin);
//            Utilities.packColumns(searchEngineTable, margin);
         }
       catch (SQLException e){
           e.printStackTrace();
       }
       catch (ClassNotFoundException e){
           e.printStackTrace();
       }
       catch (InstantiationException e){
           e.printStackTrace();
       }
       catch (IllegalAccessException e){
           e.printStackTrace();
       }
    }//GEN-LAST:event_ffSummaryButttonActionPerformed

    private void ffVisualizingVisitedHostButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ffVisualizingVisitedHostButtonActionPerformed
        try{
            String path = (String) ffComboBox.getSelectedItem() + "\\" ;
            String userName = Utilities.getFireFoxUserName(path);
            
            MozillaHandler mozillaHandler = new MozillaHandler();
            mozillaHandler.connectMozillaDB(path + MozillaHandler.PLCASES_DB);

            ArrayList<ArrayList<String>> rows = mozillaHandler.getMostVisitiedHost();
            mozillaHandler.closeDB();
            
            HashMap<String,Integer> visitedMap = new HashMap<String,Integer>();

            if ( rows == null) {
                return ;
            }
            
            for (ArrayList<String> row: rows){
                visitedMap.put(Utilities.reverseHost(row.get(0)), Integer.parseInt(row.get(1))+1);
            }

            // show visulization
            CorrelationDialog cd = new CorrelationDialog(mainFrame, true, visitedMap,userName,"FireFox Top Visited Hosts");
            cd.setVisible(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_ffVisualizingVisitedHostButtonActionPerformed

    private void ffViewHTMLReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ffViewHTMLReportButtonActionPerformed
        StringBuilder result = new StringBuilder();
        String path = (String) ffComboBox.getSelectedItem() + "\\" ;
        String userName = Utilities.getFireFoxUserName(path);

        try {
            // get top visited host
            MozillaHandler mozillaHandler = new MozillaHandler();
            mozillaHandler.connectMozillaDB(path + MozillaHandler.PLCASES_DB);

            ArrayList<ArrayList<String>> rows = mozillaHandler.getMostVisitiedHost();
            mozillaHandler.closeDB();

            result.append(FireFoxHTMLReportGenerator.getHeader());
            result.append(FireFoxHTMLReportGenerator.getName(new Date().toString(), index.getInvestigatorName(), userName));
            result.append(FireFoxHTMLReportGenerator.getTopHostTable());
            
            for (ArrayList<String> row: rows){
                result.append(FireFoxHTMLReportGenerator.getTopHostRow(Utilities.reverseHost(row.get(0)), Integer.parseInt(row.get(1))+1));
            }

            // get top visited url
            mozillaHandler = new MozillaHandler();
            mozillaHandler.connectMozillaDB(path + MozillaHandler.PLCASES_DB);

            ArrayList<ArrayList<String>> rowsURL = mozillaHandler.getSummary();
            mozillaHandler.closeDB();
            
            result.append(FireFoxHTMLReportGenerator.getVisitedURLTable());
            int swap = 0 ;
            for (ArrayList<String> row: rowsURL){
                if ( swap%2 == 0 )
                    result.append(FireFoxHTMLReportGenerator.getVisitedBlackRow(Utilities.filterLine(row.get(0)), row.get(1), Utilities.filterLine(row.get(2)),
                        Boolean.parseBoolean(row.get(3))));
                else
                    result.append(FireFoxHTMLReportGenerator.getVisitedWhiteRow(Utilities.filterLine(row.get(0)), row.get(1), Utilities.filterLine(row.get(2)),
                        Boolean.parseBoolean(row.get(3))));

                swap++;
            }

            result.append(FireFoxHTMLReportGenerator.getFooter());
            
            ArrayList<String> data = new ArrayList<String>();
            data.add(result.toString());
            
            Utilities.writeToFile(data, FilesPath.FF_REPORT);
            
            // lunch browser
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();

                if ( desktop.isSupported(Desktop.Action.OPEN) ) {
                    desktop.open(new File(FilesPath.FF_REPORT));
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_ffViewHTMLReportButtonActionPerformed

    private void clusterTypeTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_clusterTypeTreeValueChanged
        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) clusterTypeTree.getLastSelectedPathComponent();
            if ( node == null || node.isRoot() || ! node.isLeaf()) {
                return ;
            }

            String filePath= node.getUserObject().toString();
            showInformation(filePath);
       }
       catch (Exception e ){
       }
    }//GEN-LAST:event_clusterTypeTreeValueChanged

    private void indexFilesToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_indexFilesToggleButtonActionPerformed
        showPanel("indexCard", indexCardsPanel);
    }//GEN-LAST:event_indexFilesToggleButtonActionPerformed

    private void textCloudsToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textCloudsToggleButtonActionPerformed
        showPanel("textCloudsCard", indexCardsPanel);
    }//GEN-LAST:event_textCloudsToggleButtonActionPerformed

    private void indexVisualizationToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_indexVisualizationToggleButtonActionPerformed
        showPanel("indexVisualizingCard", indexCardsPanel);
    }//GEN-LAST:event_indexVisualizationToggleButtonActionPerformed

    private void fileSystemToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSystemToggleButtonActionPerformed
        showPanel("fileSystemCard",CardPanel);
        this.setTitle(APPLICATION_NAME + "File System Search Window");
    }//GEN-LAST:event_fileSystemToggleButtonActionPerformed

    private void emailToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailToggleButtonActionPerformed
       showPanel("emailCard",CardPanel);
       this.setTitle(APPLICATION_NAME + "Email Search Window");
    }//GEN-LAST:event_emailToggleButtonActionPerformed

    private void internetSurfingToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_internetSurfingToggleButtonActionPerformed
       showPanel("internetSurfingCard",CardPanel);
       this.setTitle(APPLICATION_NAME + "Internet Surfing Search Window");
    }//GEN-LAST:event_internetSurfingToggleButtonActionPerformed

    private void chatToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chatToggleButtonActionPerformed
        showPanel("chatCard",CardPanel);
        this.setTitle(APPLICATION_NAME + "Instance Chat Search Window");
    }//GEN-LAST:event_chatToggleButtonActionPerformed

    private void imageViewerToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageViewerToggleButtonActionPerformed
         showPanel("imagesViewerCard", CardPanel);
         this.setTitle(APPLICATION_NAME + "Image Viewer Window");
    }//GEN-LAST:event_imageViewerToggleButtonActionPerformed

    private void tagsExcludeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagsExcludeTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tagsExcludeTextFieldActionPerformed

    private void stopIndexingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopIndexingButtonActionPerformed
        if ( indexerThread != null) {
            indexerThread.clearFields();
            indexerThread.cancel(true);
            indexerThread = null ;
        }
    }//GEN-LAST:event_stopIndexingButtonActionPerformed

    /* emial cluster tree
     * private void emailClusterTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {
     * 
     *  try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) emailClusterTree.getLastSelectedPathComponent();
            if ( node == null || node.isRoot() || ! node.isLeaf()) {
                return ;
            }

            ClusterNode cluster = (ClusterNode) node.getUserObject();
            PSTMessage selectedMessage = cluster.getMessage() ;

            if (selectedMessage instanceof PSTContact) {
                PSTContact contact = (PSTContact)selectedMessage;
                textEditorPane.setText(contact.toString());
            } else if (selectedMessage instanceof PSTTask) {
                PSTTask task = (PSTTask)selectedMessage;
                textEditorPane.setText(task.toString());
            } else if (selectedMessage instanceof PSTActivity) {
                PSTActivity journalEntry = (PSTActivity)selectedMessage;
                textEditorPane.setText(journalEntry.toString());
            } else if (selectedMessage instanceof PSTRss) {
                PSTRss rss = (PSTRss)selectedMessage;
                textEditorPane.setText(rss.toString());
            } else if (selectedMessage != null) {
                webBrowser.setHTMLContent(selectedMessage.getBodyHTML());
                HTMLRenderPanel.validate();
                textEditorPane.setText(selectedMessage.getBodyHTML());
                headerEditorPane.setText(selectedMessage.getTransportMessageHeaders());
            }

            textEditorPane.setCaretPosition(0);
            headerEditorPane.setCaretPosition(0);

            //            ArrayList<PSTMessage> msgs = cluster.messageList;
            //            ArrayList<String> data = new ArrayList<String>();
            //
            //            for (PSTMessage m: msgs)
            //                data.add(m.getDescriptorNode().descriptorIdentifier + "");
            //
            //            emailTableModel.setFolder("Inbox");
            //            filterEmailTable(emailTable, data);

        } catch (Exception e){
            e.printStackTrace();
        }
     */


    /* cluster code , button event
     * 
     *         if ( index.getIndexStatus() == false ) {
            JOptionPane.showMessageDialog(this, "please do the indexing operation first before do any operation",
                    "Case is not indexed",JOptionPane.ERROR_MESSAGE );
            return ;
        }

        if ( emailSearchTextField.getText().trim().isEmpty() ) {
            JOptionPane.showMessageDialog(this, "please write the keyword you want to search",
                    "missing search key",JOptionPane.ERROR_MESSAGE );
            return ;
        }

        try {
            String keyword = emailSearchTextField.getText().trim() ;
            if ( outlookComboBox.getSelectedIndex() < 0 )
                return ;

            String path = (String) outlookComboBox.getSelectedItem();

            // time consuming statemnt
            InfiniteProgressPanel i = new InfiniteProgressPanel("Clustering MailBox");
            this.setGlassPane(i);
            i.start();

            ClusteringEmailThread ceTh = new ClusteringEmailThread(keyword, i, emailClusterTree, index, pstFile, path);
            ceTh.execute();

            clusteringTabbedPane.setSelectedIndex(1);
        } catch (Exception e) {
            System.out.println("error in clustering");
            e.printStackTrace();
        }
     */
    private void showPopupOpen (MouseEvent event) {
        final ImageLabel lbl = (ImageLabel) event.getSource();
        JPopupMenu popup = new JPopupMenu();
        JButton selectBtn = new JButton("Select  Image Location in PC");
        JButton openBtn   = new JButton("Open Image With Image Viewer");

        selectBtn.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed (java.awt.event.ActionEvent event) {
                try {
                    selectImage(lbl.getPath());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        
        openBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    Desktop desktop = null ;
                    
                    if (Desktop.isDesktopSupported()) {
                        desktop = Desktop.getDesktop();

                        if ( desktop.isSupported(Desktop.Action.OPEN) ) {
                            desktop.open(new File(lbl.getPath()));
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        
        JButton mapButton = null;

         try {
            if ( GeoTagging.hasGoeTag(lbl.getPath()) ) {
                mapButton  = new JButton("Show GPS  With Google  Map");
                mapButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        try {
                            Desktop desktop = null;

                            if (Desktop.isDesktopSupported()) {
                                desktop = Desktop.getDesktop();

                                if ( desktop.isSupported(Desktop.Action.BROWSE) ) {
                                    GPSData data =  GeoTagging.getGPS(lbl.getPath());
                                    String url = "http://maps.google.com/maps?q=" + data.location ;
                                    URI uri = new URI(url);
                                    desktop.browse(uri);
                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        catch (Exception e) {}
        
        popup.add(selectBtn);
        popup.add(openBtn);

         try {
            if ( GeoTagging.hasGoeTag(lbl.getPath()) && mapButton != null )
                popup.add(mapButton);
        }
        catch (Exception e) {}


        lbl.setComponentPopupMenu(popup);
    }

    private static void selectImage (String path) throws Exception{
        Runtime rt = Runtime.getRuntime();
        rt.exec("explorer /select," + path);
    }

    private void showPopup (java.awt.event.MouseEvent event) {
        final JTable table = (JTable) event.getSource();
        JPopupMenu popup = new JPopupMenu();
        JButton btn = new JButton("Export to CSV File");
        
        btn.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed (java.awt.event.ActionEvent event) {
                try {
                    FilesFilter ffFilter = new FilesFilter("Comma Seperated Value","CSV");
                    fileChooser.setFileFilter(ffFilter);

                    int result = fileChooser.showSaveDialog(OfflineMinningFrame.this);

                    if ( result == JFileChooser.APPROVE_OPTION) {
                        String name = fileChooser.getSelectedFile().getAbsolutePath();
                        Utilities.exportJTable(table,name);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        popup.add(btn);
        table.setComponentPopupMenu(popup);
    }

    private void showPopupWithLunch (java.awt.event.MouseEvent event) {
        final JTable table = (JTable) event.getSource();
        JPopupMenu popup = new JPopupMenu();

        // select table row with right click
        // get the coordinates of the mouse click
        java.awt.Point p = event.getPoint();

        // get the row index that contains that coordinate
        int rowNumber = table.rowAtPoint( p );

        // Get the ListSelectionModel of the JTable
        ListSelectionModel model = table.getSelectionModel();

        // set the selected interval of rows. Using the "rowNumber"
        // variable for the beginning and end selects only that one row.
        model.setSelectionInterval( rowNumber, rowNumber );

        // add export item
        JButton btn = new JButton("Export to CSV File");
        btn.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed (java.awt.event.ActionEvent event) {
                try {
                    FilesFilter ffFilter = new FilesFilter("Comma Seperated Value","CSV");
                    fileChooser.setFileFilter(ffFilter);

                    int result = fileChooser.showSaveDialog(OfflineMinningFrame.this);

                    if ( result == JFileChooser.APPROVE_OPTION) {
                        String name = fileChooser.getSelectedFile().getAbsolutePath();
                        Utilities.exportJTable(table,name);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        // add copy item
        JButton copyBtn = new JButton("Copy URL");
        copyBtn.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                String data = (String) table.getValueAt(row, 0);
                Utilities.setToClipBoard(data);
            }
        });

        // add lunch browser
        JButton lunchBtn = new JButton("Lunch Browser");
        lunchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed (ActionEvent event){
                Desktop desktop = null ;

                if (Desktop.isDesktopSupported()) {
                    desktop = Desktop.getDesktop();

                    if ( desktop.isSupported(Desktop.Action.BROWSE) ) {
                        try {
                            int row = table.getSelectedRow();
                            String data = (String) table.getValueAt(row, 0);
               
                            java.net.URI uri = new java.net.URI(data);
                            desktop.browse(uri);
                        }
                        catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        

        popup.add(btn);
        popup.add(copyBtn);
        popup.add(lunchBtn);
        
        table.setComponentPopupMenu(popup);
    }
    
    public ArrayList<InternetSummaryDate> getFFSummary (String path) throws Exception{
        ArrayList<InternetSummaryDate> result = new ArrayList<InternetSummaryDate>();
        MozillaHandler mozillaHandler = new MozillaHandler();
        mozillaHandler.connectMozillaDB(path + "\\" +  MozillaHandler.PLCASES_DB);

        ArrayList<ArrayList<String>> rows = mozillaHandler.getPlcaseHistory();

        for (int i=0 ; i<rows.size() ; i++) {
            if ( rows.get(i).get(1).startsWith("http://") ) {
                int number = Integer.parseInt(rows.get(i).get(3));
                InternetSummaryDate data = new InternetSummaryDate(rows.get(i).get(1),rows.get(i).get(0),
                   number );
                result.add(data);
            }
        }

        mozillaHandler.closeDB();
        return result ;
    }

    public ArrayList<InternetSummaryDate> getIESummary (String userPath) throws IOException,
    InterruptedException {

        ArrayList<InternetSummaryDate> result = new ArrayList<InternetSummaryDate>();

        if ( FilesPath.isValidPath(userPath, FilesPath.IE_HISTORY) ) {
            String path = FilesPath.getIEHistoryPath(userPath);

            IEHandler ieHandler = new IEHandler();
            ArrayList<String> rows = ieHandler.readProgramOutputFile(path,FilesPath.HIS_TMP);

            for (int i=0 ; i<rows.size() ; i++) {
                 String[] str = rows.get(i).split("\t");

                if ( str.length >= 3  && str[0].startsWith("http://")) {
                    str[2] = str[2].replaceAll(",", "");
                    int number = Integer.parseInt(str[2]);
                    InternetSummaryDate data = new InternetSummaryDate(str[0],str[3],number);
                    result.add(data);
                }
            }
        }

        return result ;
    }
        
    private void fillSkypeTree (String path) throws IOException {
        rootSkypeNode = new DefaultMutableTreeNode("Skype Chat");
        SkypeParser parser = new SkypeParser();
        
        try {
            ArrayList<Tuple<String, ArrayList<SkypeMessage>>> msgs = parser.parseSkypeFile(path);

            if ( msgs == null)
                return ;

            for (Tuple<String, ArrayList<SkypeMessage>> user: msgs) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(user.getA());
                rootSkypeNode.add(node);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private PSTMessage getMessage (long id)throws IOException, PSTException {
        return (PSTMessage) PSTObject.detectAndLoadPSTObject(pstFile, id);
    }
   

//    private List<MessageHeader> getInboxMessage() {
//        List<MessageHeader> data = new ArrayList<MessageHeader>();
//
//        try {
//            File indexLocation = new File (index.getIndexLocation() + "\\" + FilesPath.INDEX_PATH);
//            PSTSearcher pSearcher = new PSTSearcher(indexLocation);
//
//            int len = pSearcher.searchInbox();
//            System.out.println("length of inbox = " + len);
//
//            for (String path: index.getPstPath()) {
//                if ( path.equals(outlookComboBox.getSelectedItem())){
//                    for (int i=0 ; i<len ; i++){
//                        Document doc = pSearcher.getDocHits(i);
//
//                        int id = Integer.parseInt(doc.get("mailid"));
//                        PSTMessage msg = getMessage(id);
//
//                        if ( msg != null ) {
//                            String subject = doc.get(PSTSearcher.SearchField.TITLE.Value());
//                            String from = doc.get(PSTSearcher.SearchField.FROM.Value());
//                            String to = doc.get(PSTSearcher.SearchField.TO.Value());
//                            String date = doc.get(PSTSearcher.SearchField.DATE.Value());
//                            boolean hasAttachment = Boolean.valueOf(doc.get(PSTSearcher.SearchField.HAS_ATTACGMENT.Value()));
//
//                            MessageHeader message = new MessageHeader(id, subject, from, to, date, hasAttachment);
//
//                            data.add(message);
//                        }
//                        else {
//                            System.out.println("msg == null (3701) ");
//                        }
//                    }
//
//                    pSearcher.closeSearcher();
//                }
//            }
//        }
//        catch (Exception e) {
//        }
//
//        return data;
//    }

//    private void inboxCorrelations(String from, String to,PSTFile testPST,  String path) {
//        try{
//            // time consuming statemnt
////            InfiniteProgressPanel i = new InfiniteProgressPanel("Inbox Visualization...");
////            this.setGlassPane(i);
////            //i.start();
////
////            WaitGUIThread wait = new WaitGUIThread(this, i);
////            wait.execute();
//
//            EmailHandler eh = new EmailHandler(testPST, path);
//            ArrayList<Message> data = eh.getSenderName(from, to);
//
//            Map<String,Integer> inboxMap = new HashMap<String,Integer>();
//
//            if ( data == null) {
//                return ;
//            }
//
//            // show report on table
//            if ( inboxTable.getModel().getRowCount() > 0 )
//             Utilities.removeAllRows(inboxTable);
//
//            for (Message msg: data) {
//                ( (DefaultTableModel) inboxTable.getModel() ).addRow(
//                    new Object[] { msg.getSenderName() , msg.getReceiverName() , new Integer(msg.getNumberOfMessage()),
//                    msg.getDate() } );
//
//                inboxMap.put(msg.getSenderName(), msg.getNumberOfMessage());
//            }
//
//            // Pack the all columns of the table
//            int margin = 1;
//            Utilities.packColumns(inboxTable, margin);
//            Utilities.sortTable(inboxTable,1);
//
//            //wait.stop();
//
//            // show visulization
//            CorrelationDialog cd = new CorrelationDialog(mainFrame, true, inboxMap,eh.getUserName(),"Inbox");
//            cd.setVisible(true);
//
//            cd.releaseMemory();
//        }
//        catch (java.text.ParseException e){
//            e.printStackTrace();
//        }
//    }
//
//    private void sentItemCorrelation (String from, String to, PSTFile testPST, String path) {
//        try{
//            EmailHandler eh = new EmailHandler(testPST, path);
//            ArrayList<Message> data = eh.getReceiverName(from, to);
//            HashMap<String,Integer> sentMap = new HashMap<String,Integer>();
//            //Collections.sort(data);
//
//             if ( data == null) {
//                return ;
//            }
//
//            // show report on table
//            if ( sentItemTable.getModel().getRowCount() > 0 )
//             Utilities.removeAllRows(sentItemTable);
//
//            for (Message msg: data) {
//                ( (DefaultTableModel) sentItemTable.getModel() ).addRow(
//                    new Object[] { msg.getSenderName() , msg.getReceiverName() , new Integer(msg.getNumberOfMessage()),
//                    msg.getDate() } );
//
//                sentMap.put(msg.getSenderName(), msg.getNumberOfMessage());
//            }
//
//            // Pack the all columns of the table
//            int margin = 1;
//            Utilities.packColumns(sentItemTable, margin);
//            Utilities.sortTable(sentItemTable,1);
//
//            // show visulization
//            CorrelationDialog cd = new CorrelationDialog(mainFrame, true, sentMap,eh.getUserName(),"Sent Items");
//            cd.setVisible(true);
//            //cd.releaseMemory();
//        }
//        catch (java.text.ParseException e){
//            e.printStackTrace();
//        }
//    }
//
//    private void locationCorrelation(String from, String to, PSTFile testPST, String path) {
//        try{
//            EmailHandler eh = new EmailHandler(testPST, path);
//            HashMap<String,Integer> data = eh.getLocations(from,to);
//
//            if ( data == null) {
//                return ;
//            }
//
//            data.put(eh.getUserName(), 5);
//
//            // show report on table
//            if ( locationTable.getModel().getRowCount() > 0 )
//                 Utilities.removeAllRows(locationTable);
//
//            Set set = data.entrySet();
//            Iterator itr = set.iterator();
//
//            while ( itr.hasNext() ) {
//                Map.Entry me = (Map.Entry) itr.next();
//
//                String name = (String) me.getKey();
//                int num = (Integer) me.getValue();
//
//                if ( name.equals(eh.getUserName()))
//                    continue ;
//
//                ( (DefaultTableModel) locationTable.getModel() ).addRow(
//                    new Object[] { name , new Integer(num) } );
//            }
//
//            // Pack the all columns of the table
//            int margin = 1;
//            Utilities.packColumns(locationTable, margin);
//
//            Utilities.sortTable(locationTable,1);
//
//            // show visulization
//            CorrelationDialog cd = new CorrelationDialog(mainFrame, true, data,eh.getUserName(),"Messages Location");
//            cd.setVisible(true);
//            //cd.releaseMemory();
//        }
//        catch (java.text.ParseException e){
//            e.printStackTrace();
//        }
//    }
//
//    private void espCorrelation(String from, String to,PSTFile testPST, String path) {
//        try {
//            EmailHandler eh = new EmailHandler(testPST, path);
//            HashMap<String,Integer> data = eh.getEspName(from,to);
//
//            if ( data == null) {
//                return ;
//            }
//
//            data.put(eh.getUserName(), 5);
//
//             // show report on table
//            if ( espTable.getModel().getRowCount() > 0 )
//                 Utilities.removeAllRows(espTable);
//
//            Set set = data.entrySet();
//            Iterator itr = set.iterator();
//
//            while ( itr.hasNext() ) {
//                Map.Entry me = (Map.Entry) itr.next();
//
//                String name = (String) me.getKey();
//                int num = (Integer) me.getValue();
//
//                if ( name.equals(eh.getUserName()))
//                    continue ;
//
//                ( (DefaultTableModel) espTable.getModel() ).addRow(
//                    new Object[] { name , new Integer(num) } );
//            }
//
//            // Pack the all columns of the table
//            int margin = 1;
//            Utilities.packColumns(espTable, margin);
//
//            Utilities.sortTable(espTable,1);
//
//            // show visulization
//            CorrelationDialog cd = new CorrelationDialog(mainFrame, true, data,eh.getUserName(),"Email Service Provider");
//            cd.setVisible(true);
//
//            //cd.releaseMemory();
//        }
//        catch (java.text.ParseException e){
//            e.printStackTrace();
//        }
//    }
    
    public void fillWebHistoryTable (String path) throws SQLException,ClassNotFoundException, InstantiationException,
    IllegalAccessException {
        MozillaHandler mozillaHandler = new MozillaHandler();
        mozillaHandler.connectMozillaDB(path + MozillaHandler.PLCASES_DB);

        ArrayList<ArrayList<String>> rows = mozillaHandler.getPlcaseHistory();
        mozillaHandler.closeDB();

        // remove old data
        if ( webHistoryTable.getModel().getRowCount() > 0 ) {
             Utilities.removeAllRows(webHistoryTable);
        }
        
        for (int i=0 ; i<rows.size() ; i++) {
            ((DefaultTableModel)webHistoryTable.getModel()).addRow(new Object[] {
            rows.get(i).get(0),rows.get(i).get(1),rows.get(i).get(2),
            rows.get(i).get(3),rows.get(i).get(4)
            });
        }
    }

    public void fillBookmarkTable (String path) throws SQLException,ClassNotFoundException, InstantiationException,
    IllegalAccessException {
        MozillaHandler mozillaHandler = new MozillaHandler();
        mozillaHandler.connectMozillaDB(path + MozillaHandler.PLCASES_DB);

        ArrayList<ArrayList<String>> rows = mozillaHandler.getBookmark();
        mozillaHandler.closeDB();

        // remove old data
        if ( bookmarkTable.getModel().getRowCount() > 0 )
             Utilities.removeAllRows(bookmarkTable);
        
        for (int i=0 ; i<rows.size() ; i++) {
            ((DefaultTableModel)bookmarkTable.getModel()).addRow(new Object[] {
            rows.get(i).get(0),rows.get(i).get(1),rows.get(i).get(2)
            });
        }
    }

    public void fillCookiesTable (String path) throws SQLException,ClassNotFoundException, InstantiationException,
    IllegalAccessException {
        MozillaHandler mozillaHandler = new MozillaHandler();
        mozillaHandler.connectMozillaDB(path + MozillaHandler.COOKIES_DB);

        ArrayList<ArrayList<String>> rows = mozillaHandler.getCookies();
        mozillaHandler.closeDB();

        // remove old data
        if ( cookiesTable.getModel().getRowCount() > 0 )
             Utilities.removeAllRows(cookiesTable);

        for (int i=0 ; i<rows.size() ; i++) {
            ((DefaultTableModel)cookiesTable.getModel()).addRow(new Object[] {
                rows.get(i).get(0),rows.get(i).get(1),rows.get(i).get(2),
                rows.get(i).get(3),rows.get(i).get(4),rows.get(i).get(5),
                rows.get(i).get(6),rows.get(i).get(7)
            });
        }
    }

    public void fillDownloadTable (String path) throws SQLException,ClassNotFoundException, InstantiationException,
    IllegalAccessException {
        MozillaHandler mozillaHandler = new MozillaHandler();
        mozillaHandler.connectMozillaDB(path + MozillaHandler.DOWNLOAD_DB);

        ArrayList<ArrayList<String>> rows = mozillaHandler.getDownloads();
        mozillaHandler.closeDB();

        // remove old data
        if ( downloadTable.getModel().getRowCount() > 0 )
             Utilities.removeAllRows(downloadTable);

        for (int i=0 ; i<rows.size() ; i++) {
            ((DefaultTableModel) downloadTable.getModel()).addRow(new Object[] {
                rows.get(i).get(0),rows.get(i).get(1),rows.get(i).get(2),
                rows.get(i).get(3),rows.get(i).get(4)
            });
        }
    }

    public void fillLogginsTable (String path) throws SQLException,ClassNotFoundException, InstantiationException,
    IllegalAccessException {
        MozillaHandler mozillaHandler = new MozillaHandler();
        mozillaHandler.connectMozillaDB(path + MozillaHandler.LOGGIN_DB);

        ArrayList<ArrayList<String>> rows = mozillaHandler.getLoggins();
        mozillaHandler.closeDB();

        // remove old data
        if ( logginsTable.getModel().getRowCount() > 0 )
             Utilities.removeAllRows(logginsTable);
        
        for (int i=0 ; i<rows.size() ; i++) {
            ((DefaultTableModel) logginsTable.getModel()).addRow(new Object[] {
                rows.get(i).get(0),rows.get(i).get(1),rows.get(i).get(2),
                rows.get(i).get(3),rows.get(i).get(4)
            });
        }
    }

    public void fillSearchEngineTable (String path) throws SQLException,ClassNotFoundException, InstantiationException,
    IllegalAccessException {
        MozillaHandler mozillaHandler = new MozillaHandler();
        mozillaHandler.connectMozillaDB(path + MozillaHandler.PLCASES_DB);
        
        ArrayList<ArrayList<String>> rows = mozillaHandler.getSearching();
        mozillaHandler.closeDB();

        // remove old data
        if ( searchEngineTable.getModel().getRowCount() > 0 )
             Utilities.removeAllRows(searchEngineTable);

        for (int i=0 ; i<rows.size() ; i++) {
            ((DefaultTableModel) searchEngineTable.getModel()).addRow(new Object[] {
                rows.get(i).get(0),rows.get(i).get(1),
            });
        }
    }

    public void fillTopURLTable (String path) throws SQLException,ClassNotFoundException, InstantiationException,
    IllegalAccessException {
        MozillaHandler mozillaHandler = new MozillaHandler();
        mozillaHandler.connectMozillaDB(path + MozillaHandler.PLCASES_DB);
        
        ArrayList<ArrayList<String>> rows = mozillaHandler.getMostVisitiedURL();
        mozillaHandler.closeDB();
        
        // remove old data
        if ( topURLTable.getModel().getRowCount() > 0 )
             Utilities.removeAllRows(topURLTable);

        for (int i=0 ; i<rows.size() ; i++) {
            ((DefaultTableModel) topURLTable.getModel()).addRow(new Object[] {
                rows.get(i).get(0),rows.get(i).get(1),rows.get(i).get(2)
            });
        }
    }

    public void fillTopHostTable (String path) throws SQLException,ClassNotFoundException, InstantiationException,
    IllegalAccessException {
        MozillaHandler mozillaHandler = new MozillaHandler();
        mozillaHandler.connectMozillaDB(path + MozillaHandler.PLCASES_DB);
        
        ArrayList<ArrayList<String>> rows = mozillaHandler.getMostVisitiedHost();
        mozillaHandler.closeDB();

        // remove old data
        if ( topHostTable.getModel().getRowCount() > 0 )
             Utilities.removeAllRows(topHostTable);

        for (int i=0 ; i<rows.size() ; i++) {
            ((DefaultTableModel) topHostTable.getModel()).addRow(new Object[] {
                Utilities.reverseHost(rows.get(i).get(0)),rows.get(i).get(1),
            });
        }
    }

    public void fillIECacheTable (String path) throws IOException {
        ArrayList<String> rows = Utilities.readProgramOutputStream(path);

         // remove old data
        if ( IECacheTable.getModel().getRowCount() > 0 )
             Utilities.removeAllRows(IECacheTable);
        
        for (int i=0 ; i<rows.size() ; i++) {
             String[] str = rows.get(i).split(",");
             Object[] object = new Object[str.length];

             for (int j=0 ; j<str.length ; j++)
                 object[j] = str[j] ;
             
            ((DefaultTableModel) IECacheTable.getModel()).addRow(object);
        }
    }

    public void fillIECookiesTable (String path) throws IOException {
        ArrayList<String> rows = Utilities.readProgramOutputStream(path);

        for (int i=0 ; i<rows.size() ; i++) {
            if ( rows.get(i).isEmpty() || rows.get(i).length() <= 1 )
                continue;
            
             String[] str = rows.get(i).split(",");
             Object[] object = new Object[str.length];

             for (int j=0 ; j<str.length ; j++) {
                 object[j] = str[j] ;
             }

            ((DefaultTableModel) IECookiesTable.getModel()).addRow(object);
        }
    }

    public void fillIEWebHistoryTable (String path) throws IOException, InterruptedException {
        IEHandler ieHandler = new IEHandler();
        ArrayList<String> rows = ieHandler.readProgramOutputFile(path,FilesPath.HIS_TMP);

        // remove old data
        if ( IEWebHistoryTable.getModel().getRowCount() > 0 )
             Utilities.removeAllRows(IEWebHistoryTable);
        
        for (int i=0 ; i<rows.size() ; i++) {
             String[] str = rows.get(i).split("\t");
             Object[] object = new Object[str.length];
             
             for (int j=0 ; j<str.length ; j++)
                 object[j] = str[j] ;

            ((DefaultTableModel) IEWebHistoryTable.getModel()).addRow(object);
        }
    }

    public void fillIEBookmarkTable (String path) throws IOException {
        ArrayList<String> filesName = new ArrayList<String>();
        IEHandler.getFiles(new File(path) , filesName);

        // remove old data
        if ( IEBookmarkTable.getModel().getRowCount() > 0 )
             Utilities.removeAllRows(IEBookmarkTable);
        
        for (int i=0; i<filesName.size() ; i++){
            ( (DefaultTableModel) IEBookmarkTable.getModel() ).addRow(
                    new Object[] { filesName.get(i) } );
        }
    }

    public void fillIELogginsTable (String path) throws IOException, InterruptedException {
        IEHandler ieHandler = new IEHandler();
        ArrayList<String> pass = ieHandler.readProgramOutputFile(path,FilesPath.PASS_TMP);

        // remove old data
        if ( IELogginsTable.getModel().getRowCount() > 0 )
             Utilities.removeAllRows(IELogginsTable);
        
        for (int i=0 ; i<pass.size(); i++){
            String[] str = pass.get(i).split(",");
            Object[] object = new Object[str.length];

             for (int j=0 ; j<str.length ; j++)
                 object[j] = str[j] ;
            
            ( (DefaultTableModel) IELogginsTable.getModel() ).addRow(object);
        }
    }

    private class SummaryInputListener implements DocumentListener {
        public void changedUpdate   (DocumentEvent e) { filterSummaryTable(); }
        public void removeUpdate    (DocumentEvent e) { filterSummaryTable(); }
        public void insertUpdate    (DocumentEvent e) { filterSummaryTable(); }
    }

    public void filterSummaryTable () {
        String text = summaryTextField.getText().trim();
        filterTable(summaryTable, text);
    }
    
    private class CloudsInputListener implements DocumentListener {
        public void changedUpdate(DocumentEvent e){filterCloudTable();}
        public void removeUpdate (DocumentEvent e){filterCloudTable();}
        public void insertUpdate (DocumentEvent e){filterCloudTable();}
    }

    public void filterCloudTable () {
        String text = cloudsFilterTextField.getText().trim();
        filterTable(cloudsTable, text);
    }
    
    private class EmailSearchInputListener implements DocumentListener {
        public void changedUpdate(DocumentEvent e){filterEmailSearch();}
        public void removeUpdate (DocumentEvent e){filterEmailSearch();}
        public void insertUpdate (DocumentEvent e){filterEmailSearch();}
    }

    public void filterEmailSearch () {
        String text = emailSearchTextField.getText().trim();
        filterTable(emailTable, text);
    }
    
    private class MozillaInputListener implements DocumentListener {
        public void changedUpdate(DocumentEvent e){filterMozillaTables();}
        public void removeUpdate (DocumentEvent e){filterMozillaTables();}
        public void insertUpdate (DocumentEvent e){filterMozillaTables();}
    }

    public void filterMozillaTables () {
        String text = mozillaSearchField.getText().trim();

        // search in all tables for mozilla text search and filetr result
        filterTable(webHistoryTable, text);
        filterTable(bookmarkTable, text);
        filterTable(cookiesTable, text);
        filterTable(downloadTable,text) ;
        filterTable(logginsTable,text);
    }

    private class IEInputListener implements DocumentListener {
        public void changedUpdate(DocumentEvent e){filterIETables();}
        public void removeUpdate (DocumentEvent e){filterIETables();}
        public void insertUpdate (DocumentEvent e){filterIETables();}
    }

    public void filterIETables () {
        String text = IESearchField.getText().trim();

        // search in all tables for mozilla text search and filetr result
        filterTable(IEWebHistoryTable, text);
        filterTable(IEBookmarkTable, text);
        filterTable(IECookiesTable, text);
        filterTable(IECacheTable,text) ;
        filterTable(IELogginsTable,text);
    }

    // filer table, ignore case (case insensitive)
    private void filterTable (JTable table, String text) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);

        if ( text.equalsIgnoreCase(" ") ) {
            sorter.setRowFilter(null);
        }
        else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
            catch (PatternSyntaxException e){
                
            }
        }
    }

    // show panel function
    public void showPanel (String panelName, JPanel name) {
        CardLayout card = (CardLayout) name.getLayout();
        card.show(name, panelName);
    }

    public void setOutlookHandling (PSTFile pstFile, String path) {
         // attempt to open the pst file and initlizing tree
	try { 
            top = new DefaultMutableTreeNode(pstFile.getMessageStore());
            buildTree(top, pstFile.getRootFolder());
            
            emailTableModel = new EmailTableModel(pstFile, path);

	} catch (Exception err) {
            err.printStackTrace();
            return ;
	}
    }

    public void setOutlookEvent () {
        emailTable.setFillsViewportHeight(true);
        ListSelectionModel selectionModel = emailTable.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                try {
                    JTable source = emailTable;
                    int viewRow = source.getSelectedRow();
                    if (viewRow == -1) {
                        return;
                    }
                    
                    int row = emailTable.convertRowIndexToModel(viewRow);
                    MessageHeader msg = emailTableModel.getMessageAtRow(row);
                    PSTMessage selectedMessage = getMessage(msg.getID());
                    
                    if (selectedMessage instanceof PSTContact) {
                        PSTContact contact = (PSTContact) selectedMessage;
                        textEditorPane.setText(contact.toString());
                    } else if (selectedMessage instanceof PSTTask) {
                        PSTTask task = (PSTTask) selectedMessage;
                        textEditorPane.setText(task.toString());
                    } else if (selectedMessage instanceof PSTActivity) {
                        PSTActivity journalEntry = (PSTActivity) selectedMessage;
                        textEditorPane.setText(journalEntry.toString());
                    } else if (selectedMessage instanceof PSTRss) {
                        PSTRss rss = (PSTRss) selectedMessage;
                        textEditorPane.setText(rss.toString());
                    } else if (selectedMessage != null) {
                        webBrowser.setHTMLContent(selectedMessage.getBodyHTML());
                        HTMLRenderPanel.validate();
                        textEditorPane.setText(selectedMessage.getBodyHTML());
                        headerEditorPane.setText(selectedMessage.getTransportMessageHeaders());
                    }
                    textEditorPane.setCaretPosition(0);
                    headerEditorPane.setCaretPosition(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (PSTException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        // tree event handler for changing...
        folderTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) folderTree.getLastSelectedPathComponent();
        	if (node != null) {
                    selectFolder(node.getUserObject().toString());
        	}
            }
        });
    }

    private void buildTree(DefaultMutableTreeNode top, PSTFolder theFolder) {
        // this is recursive, try and keep up.
	try {
            Vector children = theFolder.getSubFolders();
            Iterator childrenIterator = children.iterator();
            while (childrenIterator.hasNext()) {
                PSTFolder folder = (PSTFolder)childrenIterator.next();
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(folder.getDisplayName());

		if (folder.getSubFolders().size() > 0) {
                    buildTree(node, folder);
		}

                top.add(node);
            }
        } catch (Exception err) {
            err.printStackTrace();
            System.exit(1);
	}
    }

    void selectFolder(String folder) {
        // load up the non-folder children.
        emailTableModel.setFolder(folder);
        
        // Pack the all columns of the table
        int margin = 1;
        Utilities.packColumns(emailTable, margin);
    }

    private void disableNotIndexedComponent () {
        // close email if there is no pst file
        if ( index.getPstPath().isEmpty() ) {
            emailPanel.setEnabled(false);
            loadPstButton.setEnabled(false);
            //clusteringButton.setEnabled(false);
            emailSearchButton.setEnabled(false);
            outlookComboBox.setEnabled(false);
            emailSearchButton.setEnabled(false);
            emailVisualizationButton.setEnabled(false);
            inboxTable.setEnabled(false);
            sentItemTable.setEnabled(false);
            espTable.setEnabled(false);
            locationTable.setEnabled(false);
            correlationComboBox.setEnabled(false);
            fromDatePanel.getComponent(0).setEnabled(false);
            toDatePanel.getComponent(0).setEnabled(false);
        }

        // close web browers data
        if ( index.getFFPath().isEmpty() ) {
            mozillaPanel.setEnabled(false);
            ffComboBox.setEnabled(false);
            loadFFButton.setEnabled(false);
            mozillaSearchField.setEnabled(false);
            mozillaFilterComboBox.setEnabled(false);
            webHistoryButton.setEnabled(false);
            bookmarButton.setEnabled(false);
            cookiesButton.setEnabled(false);
            downloadButton.setEnabled(false);
            logginsButton.setEnabled(false);
            webHistoryTable.setEnabled(false);
            ffSummaryButtton.setEnabled(false);
            ffViewHTMLReportButton.setEnabled(false);
            ffVisualizingVisitedHostButton.setEnabled(false);
            bookmarkTable.setEnabled(false);
            cookiesTable.setEnabled(false);
            downloadTable.setEnabled(false);
            logginsTable.setEnabled(false);
        }

        if ( index.getIePath().isEmpty() ) {
            IEPanel.setEnabled(false);
            ieComboBox.setEnabled(false);
            loadIEButton.setEnabled(false);
            IESearchField.setEnabled(false);
            IEFilterComboBox.setEnabled(false);
            webHistoryButton1.setEnabled(false);
            bookmarButton1.setEnabled(false);
            cookiesButton1.setEnabled(false);
            cacheButton.setEnabled(false);
            logginsButton1.setEnabled(false);
            IEWebHistoryTable.setEnabled(false);
            IEBookmarkTable.setEnabled(false);
            IECookiesTable.setEnabled(false);
            IECacheTable.setEnabled(false);
            IELogginsTable.setEnabled(false);
        }

        if ( index.getFFPath().isEmpty() && index.getIePath().isEmpty() ) {
            summaryInternetPanel.setEnabled(false);
            summaryInternetButton.setEnabled(false);
            summaryTable.setEnabled(false);
        }

        // close chat panels
        if (index.getMsnPath().isEmpty()) {
            WindowsLivePanel.setEnabled(false);
            msnComboBox.setEnabled(false);
            msnChat.setEnabled(false);
            msnChatContentPanel.setEnabled(false);
            msnChatTree.setEnabled(false);
            loadMSNButton.setEnabled(false);
        }

        if ( index.getYahooPath().isEmpty()) {
            yahooChatContentPanel.setEnabled(false);
            yahooChat.setEnabled(false);
            yahooChatTree.setEnabled(false);
            yahooComboBox.setEnabled(false);
            yahooMessangerPanel.setEnabled(false);
            loadYahooButton.setEnabled(false);
        }

        if (index.getSkypePath().isEmpty() ) {
            skypePanel.setEnabled(false);
            loadSkypeButton.setEnabled(false);
            skypeComboBox.setEnabled(false);
            skypeChatTree.setEnabled(false);
            skypeChatContentPanel.setEnabled(false);
            skypeTable.setEnabled(false);
        }

        if (index.getCacheImages() == false ) {
            showImagesButton.setEnabled(false);
            nextPageButton.setEnabled(false);
            prePageButton.setEnabled(false);
        }

        if ( index.getDocumentInIndex().isEmpty() ) {
            startIndexButton.setEnabled(false);
            tagSelectButton.setEnabled(false);
            indexVisulizingButton.setEnabled(false);
            startSearchingButton.setEnabled(false);
            clearFieldsButton.setEnabled(false);
            keywordsListButton.setEnabled(false);
            cloudsTable.setEnabled(false);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CardPanel;
    private javax.swing.JPanel ChatPanel;
    private javax.swing.JPanel FileMetaDataPanel;
    private javax.swing.JPanel HTMLRenderPanel;
    private javax.swing.JPanel IEBookmarkPanel;
    private javax.swing.JTable IEBookmarkTable;
    private javax.swing.JPanel IEButtonsPanel;
    private javax.swing.JPanel IECachePanel;
    private javax.swing.JTable IECacheTable;
    private javax.swing.JPanel IECookiesPanel;
    private javax.swing.JTable IECookiesTable;
    private javax.swing.JComboBox IEFilterComboBox;
    private javax.swing.JPanel IELogginsPanel;
    private javax.swing.JTable IELogginsTable;
    private javax.swing.JPanel IEPanel;
    private javax.swing.JPanel IEResultPanel;
    private javax.swing.JTextField IESearchField;
    private javax.swing.JPanel IESearchPanel;
    private javax.swing.JPanel IEWebHistoryPanel;
    private javax.swing.JTable IEWebHistoryTable;
    private javax.swing.JPanel ImagesViewerPanel;
    private javax.swing.JPanel IndexFileSystemPanel;
    private javax.swing.JPanel SearchFileSystemPanel;
    private javax.swing.JPanel TXTRenderPanel;
    private javax.swing.JPanel WindowsLivePanel;
    private javax.swing.JMenu aboutMenu;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JLabel bigSizeMsgLbl;
    private javax.swing.JButton bookmarButton;
    private javax.swing.JButton bookmarButton1;
    private javax.swing.JPanel bookmarkHistory;
    private javax.swing.JScrollPane bookmarkScrollPane;
    private javax.swing.JTable bookmarkTable;
    private javax.swing.JButton cacheButton;
    private javax.swing.JTabbedPane chatPanelTappedPane;
    private javax.swing.JToggleButton chatToggleButton;
    private javax.swing.JButton clearFieldsButton;
    private javax.swing.JTextField cloudsFilterTextField;
    private javax.swing.JTable cloudsTable;
    private javax.swing.JTree clusterPathTree;
    private javax.swing.JTree clusterTypeTree;
    private javax.swing.JTabbedPane clusteringTabbedPane;
    private javax.swing.JButton cookiesButton;
    private javax.swing.JButton cookiesButton1;
    private javax.swing.JPanel cookiesPanel;
    private javax.swing.JScrollPane cookiesScrollPane;
    private javax.swing.JTable cookiesTable;
    private javax.swing.JComboBox correlationComboBox;
    private javax.swing.JPanel correlationResultPanel;
    private javax.swing.JLabel currentFileLbl;
    private javax.swing.JButton downloadButton;
    private javax.swing.JPanel downloadPanel;
    private javax.swing.JScrollPane downloadScrollPane;
    private javax.swing.JTable downloadTable;
    private javax.swing.JPanel emailPanel;
    private javax.swing.JButton emailSearchButton;
    private javax.swing.JTextField emailSearchTextField;
    private javax.swing.JTabbedPane emailTabbedPane;
    private javax.swing.JTable emailTable;
    private javax.swing.JToggleButton emailToggleButton;
    private javax.swing.JButton emailVisualizationButton;
    private javax.swing.JPanel espPanel;
    private javax.swing.JTable espTable;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JComboBox ffComboBox;
    private javax.swing.JTabbedPane ffSumarryTappnedPane;
    private javax.swing.JPanel ffSummaryButtonsPanel;
    private javax.swing.JButton ffSummaryButtton;
    private javax.swing.JPanel ffSummaryDataPanel;
    private javax.swing.JPanel ffSummaryPanel;
    private javax.swing.JButton ffViewHTMLReportButton;
    private javax.swing.JButton ffVisualizingVisitedHostButton;
    private javax.swing.JLabel fileExtensionLbl;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPanel fileRenderPanel;
    private javax.swing.JPanel fileSystemPanel;
    private javax.swing.JTabbedPane fileSystemTappedPane;
    private javax.swing.JToggleButton fileSystemToggleButton;
    private javax.swing.JTree folderTree;
    private javax.swing.JPanel fromDatePanel;
    private javax.swing.JLabel geoTagLbl;
    private javax.swing.JEditorPane headerEditorPane;
    private javax.swing.ButtonGroup headerGroupButton;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JComboBox ieComboBox;
    private javax.swing.JLabel imageDateLabel;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JLabel imagePathLabel;
    private javax.swing.JLabel imageSizeLabel;
    private javax.swing.JToggleButton imageViewerToggleButton;
    private javax.swing.JPanel inboxPanel;
    private javax.swing.JTable inboxTable;
    private javax.swing.JPanel indexCardsPanel;
    private javax.swing.JLabel indexDateLbl;
    private javax.swing.JLabel indexDirLbl2;
    private javax.swing.JPanel indexFileSystemButtonsPanel;
    private javax.swing.JToggleButton indexFilesToggleButton;
    private javax.swing.ButtonGroup indexGroupButton;
    private javax.swing.JPanel indexPanel;
    private javax.swing.JTable indexTable;
    private javax.swing.JToggleButton indexVisualizationToggleButton;
    private javax.swing.JPanel indexVisualizingPanel;
    private javax.swing.JPanel indexVisualizingPiePanel;
    private javax.swing.JButton indexVisulizingButton;
    private javax.swing.JPanel internetSurfingPanel;
    private javax.swing.JTabbedPane internetSurfingTappedPane;
    private javax.swing.JToggleButton internetSurfingToggleButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JButton keywordsListButton;
    private javax.swing.JButton loadFFButton;
    private javax.swing.JButton loadIEButton;
    private javax.swing.JButton loadMSNButton;
    private javax.swing.JButton loadPstButton;
    private javax.swing.JButton loadSkypeButton;
    private javax.swing.JButton loadYahooButton;
    private javax.swing.JPanel locationPanel;
    private javax.swing.JTable locationTable;
    private javax.swing.JButton logginsButton;
    private javax.swing.JButton logginsButton1;
    private javax.swing.JPanel logginsPanel;
    private javax.swing.JScrollPane logginsScrollPane;
    private javax.swing.JTable logginsTable;
    private javax.swing.JPanel messageFrequencyPanel;
    private javax.swing.JPanel messageHeaderPanel;
    private javax.swing.JPanel messagePanel;
    private javax.swing.JTextArea metaDataTextArea;
    private javax.swing.JPanel mozillaButtonsPanel;
    private javax.swing.JComboBox mozillaFilterComboBox;
    private javax.swing.JPanel mozillaPanel;
    private javax.swing.JPanel mozillaResultPanel;
    private javax.swing.JTextField mozillaSearchField;
    private javax.swing.JPanel mozillaSearchPanel;
    private javax.swing.JPanel msnChatContentPanel;
    private javax.swing.JTree msnChatTree;
    private javax.swing.JComboBox msnComboBox;
    private javax.swing.JButton nextPageButton;
    private javax.swing.JLabel numberOfErrorFilesLbl;
    private javax.swing.JLabel numberOfFilesLbl;
    private javax.swing.JComboBox outlookComboBox;
    private javax.swing.JPanel outlookCorrelationsPanel;
    private javax.swing.JPanel outlookPanel;
    private javax.swing.JButton prePageButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextField queryTextField;
    private javax.swing.JMenuItem recentMenuItem;
    private javax.swing.JPanel renderPanel;
    private javax.swing.JPanel searchEnginePanel;
    private javax.swing.JTable searchEngineTable;
    private javax.swing.JLabel searchFileExtensionLbl;
    private javax.swing.JLabel searchFileNameLbl;
    private javax.swing.JLabel searchFileSizeLbl;
    private javax.swing.JProgressBar searchProgressBard;
    private javax.swing.JTable searchTable;
    private javax.swing.JLabel searchTime;
    private javax.swing.JLabel searchingDateLbl;
    private javax.swing.JPanel sentItemPanel;
    private javax.swing.JTable sentItemTable;
    private javax.swing.JButton showImagesButton;
    private javax.swing.JLabel sizeOfFileLbl;
    private javax.swing.JPanel skypeChatContentPanel;
    private javax.swing.JTree skypeChatTree;
    private javax.swing.JComboBox skypeComboBox;
    private javax.swing.JPanel skypePanel;
    private javax.swing.JTable skypeTable;
    private javax.swing.JButton startIndexButton;
    private javax.swing.JButton startSearchingButton;
    private javax.swing.JButton stopIndexingButton;
    private javax.swing.JButton summaryInternetButton;
    private javax.swing.JPanel summaryInternetPanel;
    private javax.swing.JTable summaryTable;
    private javax.swing.JTextField summaryTextField;
    private javax.swing.JButton tagSelectButton;
    private javax.swing.JComboBox tagsDisplayComboBox;
    private javax.swing.JTextField tagsExcludeTextField;
    private javax.swing.JTextField tagsNumberTextField;
    private javax.swing.JPanel tagsPanel;
    private javax.swing.JPanel textCloudsPanel;
    private javax.swing.JToggleButton textCloudsToggleButton;
    private javax.swing.JEditorPane textEditorPane;
    private javax.swing.JLabel timeLbl;
    private javax.swing.JPanel toDatePanel;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JPanel topHostPanel;
    private javax.swing.JTable topHostTable;
    private javax.swing.JPanel topURLPanel;
    private javax.swing.JTable topURLTable;
    private javax.swing.JScrollPane treeScrollPane;
    private javax.swing.JLabel userQueryLbl;
    private javax.swing.JButton webHistoryButton;
    private javax.swing.JButton webHistoryButton1;
    private javax.swing.JPanel webHistoryPanel;
    private javax.swing.JScrollPane webHistoryScrollPane;
    private javax.swing.JTable webHistoryTable;
    private javax.swing.JMenuItem windowsMenuItem;
    private javax.swing.JPanel yahooChatContentPanel;
    private javax.swing.JTree yahooChatTree;
    private javax.swing.JComboBox yahooComboBox;
    private javax.swing.JPanel yahooMessangerPanel;
    // End of variables declaration//GEN-END:variables
}