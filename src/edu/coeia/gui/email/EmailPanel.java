
package edu.coeia.gui.email;

import edu.coeia.gui.utilties.GUIComponent ;


import edu.coeia.internet.IEHandler;
import edu.coeia.internet.MozillaHandler;

import edu.coeia.utility.Utilities;
import edu.coeia.utility.FilesPath ;
import edu.coeia.utility.FilesFilter ;
import edu.coeia.utility.Tuple ;

import edu.coeia.utility.MetaDataExtraction ;
import edu.coeia.utility.FireFoxHTMLReportGenerator;


import edu.coeia.cases.Case;

import edu.coeia.chat.MSNParser;
import edu.coeia.chat.YahooMessage ;
import edu.coeia.chat.YahooMessageDecoder;
import edu.coeia.chat.YahooMessageReader;
import edu.coeia.chat.SkypeMessage;
import edu.coeia.chat.SkypeParser;

import edu.coeia.search.PSTSearcher;
import edu.coeia.internet.InternetSummaryDate ;


import edu.coeia.email.EmailReaderThread;
import edu.coeia.email.MessageHeader ;

import java.awt.CardLayout ;
import java.awt.BorderLayout;

import java.awt.Toolkit ;

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

import edu.coeia.gui.utilties.InfiniteProgressPanel;
import java.awt.event.InputEvent;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.document.Document;


/*
 * EmailPanel.java
 *
 * @author wajdyessam
 * 
 * Created on Aug 10, 2011, 4:17:30 PM
 * 
 */

public class EmailPanel extends javax.swing.JPanel {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private Case index;
    private PSTFile pstFile ;
     
    private JWebBrowser webBrowser = new JWebBrowser();    
    private EmailTableModel emailTableModel;
    private DefaultMutableTreeNode top ;
    
    private JFileChooser fileChooser ;
    
    /** Creates new form EmailPanel */
    public EmailPanel(Case aIndex) {
        initComponents();
        
        this.index = aIndex;
        
        // configure file chooser to select files (txt)
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FilesFilter("Text Files (*.txt)", "txt"));
        
        // add a native web browser
        webBrowser.setBarsVisible(false);
        webBrowser.setStatusBarVisible(false);
        HTMLRenderPanel.add(webBrowser, BorderLayout.CENTER);
        
        emailSearchTextField.getDocument().addDocumentListener(new EmailSearchInputListener());        
        
        // jcalendar initilizing
        toDatePanel.add(new JDateChooser(new Date()),BorderLayout.CENTER);
        fromDatePanel.add(new JDateChooser(new Date(0)),BorderLayout.CENTER);        
        
        // set outlook data to outlook combobox
        for (String pstPath: index.getPstPath())
            outlookComboBox.addItem( pstPath );      
        
        disableNotIndexedComponent();
        Utilities.setTableAlignmentValue(inboxTable, 2);
        Utilities.setTableAlignmentValue(sentItemTable, 2);
        Utilities.setTableAlignmentValue(espTable, 1);
        Utilities.setTableAlignmentValue(locationTable, 1);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
                .addComponent(emailSearchTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(emailSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
        );
        messagePanelLayout.setVerticalGroup(
            messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messagePanelLayout.createSequentialGroup()
                .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(messagePanelLayout.createSequentialGroup()
                        .addGap(252, 252, 252)
                        .addGroup(messagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(emailSearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emailSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 676, Short.MAX_VALUE)
                .addContainerGap())
        );
        TXTRenderPanelLayout.setVerticalGroup(
            TXTRenderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TXTRenderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
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
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 676, Short.MAX_VALUE)
                .addContainerGap())
        );
        messageHeaderPanelLayout.setVerticalGroup(
            messageHeaderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(messageHeaderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("Message Header", messageHeaderPanel);

        javax.swing.GroupLayout renderPanelLayout = new javax.swing.GroupLayout(renderPanel);
        renderPanel.setLayout(renderPanelLayout);
        renderPanelLayout.setHorizontalGroup(
            renderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
        );
        renderPanelLayout.setVerticalGroup(
            renderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel1.setForeground(new java.awt.Color(0, 70, 213));
        jLabel1.setText("Select Outlook File From Index:");

        loadPstButton.setFont(new java.awt.Font("Tahoma", 1, 11));
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
                .addComponent(outlookComboBox, 0, 285, Short.MAX_VALUE)
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
            .addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
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
            .addComponent(clusteringTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
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
                    .addComponent(messagePanel, 0, 161, Short.MAX_VALUE)
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
                        .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fromDatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(toDatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
        );
        inboxPanelLayout.setVerticalGroup(
            inboxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
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
            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
        );
        sentItemPanelLayout.setVerticalGroup(
            sentItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
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
            .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
        );
        espPanelLayout.setVerticalGroup(
            espPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
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
            .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
        );
        locationPanelLayout.setVerticalGroup(
            locationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
        );

        correlationResultPanel.add(locationPanel, "locationCard");

        javax.swing.GroupLayout messageFrequencyPanelLayout = new javax.swing.GroupLayout(messageFrequencyPanel);
        messageFrequencyPanel.setLayout(messageFrequencyPanelLayout);
        messageFrequencyPanelLayout.setHorizontalGroup(
            messageFrequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 701, Short.MAX_VALUE)
        );
        messageFrequencyPanelLayout.setVerticalGroup(
            messageFrequencyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 329, Short.MAX_VALUE)
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
                    .addComponent(correlationResultPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE))
                .addContainerGap())
        );
        outlookCorrelationsPanelLayout.setVerticalGroup(
            outlookCorrelationsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outlookCorrelationsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(correlationResultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addContainerGap())
        );

        emailTabbedPane.addTab("Microsoft Outlook Statistics", outlookCorrelationsPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 762, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(emailTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 742, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 559, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(emailTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

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
            //this.setGlassPane(i);
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
    
    private PSTMessage getMessage (long id)throws IOException, PSTException {
        return (PSTMessage) PSTObject.detectAndLoadPSTObject(pstFile, id);
    }
    
    private void showVisualization (String from, String to, PSTFile pst, String path, String title, String folderName,EmailVisualizationThread.FolderType type) {
        InfiniteProgressPanel i = new InfiniteProgressPanel(title);
        //this.setGlassPane(i);
        i.start();

        EmailVisualizationThread thread = new EmailVisualizationThread(null, i, folderName , pst, path, from, to, type);
        thread.execute();
    }
    
    public void filterEmailSearch () {
        String text = emailSearchTextField.getText().trim();
        filterTable(emailTable, text);
    }
     
    private class EmailSearchInputListener implements DocumentListener {
        public void changedUpdate(DocumentEvent e){filterEmailSearch();}
        public void removeUpdate (DocumentEvent e){filterEmailSearch();}
        public void insertUpdate (DocumentEvent e){filterEmailSearch();}
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
    
    private void showPopup (java.awt.event.MouseEvent event) {
        final JTable table = (JTable) event.getSource();
        JPopupMenu popup = new JPopupMenu();
        JButton btn = new JButton("Export to CSV File");
        
        btn.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed (java.awt.event.ActionEvent event) {
                try {
                    FilesFilter ffFilter = new FilesFilter("Comma Seperated Value","CSV");
                    fileChooser.setFileFilter(ffFilter);

                    int result = fileChooser.showSaveDialog(null);

                    if ( result == JFileChooser.APPROVE_OPTION) {
                        String name = fileChooser.getSelectedFile().getAbsolutePath();
                        Utilities.exportJTable(table,name);
                    }
                }
                catch (Exception e){
                }
            }
        });

        popup.add(btn);
        table.setComponentPopupMenu(popup);
    }    
    
    private void disableNotIndexedComponent () {
        // close email if there is no pst file
        if ( index.getPstPath().isEmpty() ) {
           // emailPanel.setEnabled(false);
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
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel HTMLRenderPanel;
    private javax.swing.JPanel TXTRenderPanel;
    private javax.swing.JTabbedPane clusteringTabbedPane;
    private javax.swing.JComboBox correlationComboBox;
    private javax.swing.JPanel correlationResultPanel;
    private javax.swing.JButton emailSearchButton;
    private javax.swing.JTextField emailSearchTextField;
    private javax.swing.JTabbedPane emailTabbedPane;
    private javax.swing.JTable emailTable;
    private javax.swing.JButton emailVisualizationButton;
    private javax.swing.JPanel espPanel;
    private javax.swing.JTable espTable;
    private javax.swing.JTree folderTree;
    private javax.swing.JPanel fromDatePanel;
    private javax.swing.JEditorPane headerEditorPane;
    private javax.swing.JPanel inboxPanel;
    private javax.swing.JTable inboxTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JButton loadPstButton;
    private javax.swing.JPanel locationPanel;
    private javax.swing.JTable locationTable;
    private javax.swing.JPanel messageFrequencyPanel;
    private javax.swing.JPanel messageHeaderPanel;
    private javax.swing.JPanel messagePanel;
    private javax.swing.JComboBox outlookComboBox;
    private javax.swing.JPanel outlookCorrelationsPanel;
    private javax.swing.JPanel outlookPanel;
    private javax.swing.JPanel renderPanel;
    private javax.swing.JPanel sentItemPanel;
    private javax.swing.JTable sentItemTable;
    private javax.swing.JEditorPane textEditorPane;
    private javax.swing.JPanel toDatePanel;
    private javax.swing.JScrollPane treeScrollPane;
    // End of variables declaration//GEN-END:variables
}