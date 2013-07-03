/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ChatViewerPanel.java
 *
 * Created on Jan 22, 2012, 9:35:10 AM
 */
package edu.coeia.chat;

import edu.coeia.cases.CaseFacade;
import edu.coeia.constants.AuditingMessages;
import edu.coeia.constants.ResourceManager;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.tasks.ChatLoadingTask;
import edu.coeia.tasks.ChatRefreshTask;
import edu.coeia.util.ApplicationLogging;
import java.awt.ComponentOrientation;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author wajdyessam
 */
public class ChatViewerPanel extends javax.swing.JPanel {

    private final DefaultListModel chatListModel;
    private final String agent;
    private final CaseFacade caseFacade ;
    private final static Logger logger = ApplicationLogging.getLogger();
    
    /** Creates new form ChatViewerPanel */
    public ChatViewerPanel(final CaseFacade caseFacade, final String agent) {
        initComponents();
        this.applyComponentOrientation(ComponentOrientation.getOrientation(ResourceManager.getLanguage()));
        
        this.chatListModel = new DefaultListModel();
        this.agent = agent;
        this.caseFacade = caseFacade ;
        
        this.filterTable.getDocument().addDocumentListener(new DocumentListener() { 
            public void changedUpdate(DocumentEvent e){ doFilter(); }
            public void removeUpdate (DocumentEvent e){ doFilter(); }
            public void insertUpdate (DocumentEvent e){ doFilter(); }
            
            private void doFilter() {JTableUtil.filterTable(chatTable, filterTable.getText().trim());}
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewerPanel = new javax.swing.JPanel();
        selectionPanel = new javax.swing.JPanel();
        chatLoadingPanel = new javax.swing.JPanel();
        refreshButton = new javax.swing.JButton();
        loadItemButton = new javax.swing.JButton();
        chatListPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatJList = new javax.swing.JList();
        tablePanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        chatTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        filterTable = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        viewerPanel.setLayout(new java.awt.BorderLayout());

        selectionPanel.setLayout(new javax.swing.BoxLayout(selectionPanel, javax.swing.BoxLayout.Y_AXIS));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("edu/coeia/chat/Bundle"); // NOI18N
        refreshButton.setText(bundle.getString("ChatViewerPanel.refreshButton.text")); // NOI18N
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        chatLoadingPanel.add(refreshButton);

        loadItemButton.setText(bundle.getString("ChatViewerPanel.loadItemButton.text")); // NOI18N
        loadItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadItemButtonActionPerformed(evt);
            }
        });
        chatLoadingPanel.add(loadItemButton);

        selectionPanel.add(chatLoadingPanel);

        chatListPanel.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(chatJList);

        chatListPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        selectionPanel.add(chatListPanel);

        viewerPanel.add(selectionPanel, java.awt.BorderLayout.WEST);

        tablePanel.setLayout(new java.awt.BorderLayout());

        chatTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "From", "To", "Message", "Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        chatTable.setFillsViewportHeight(true);
        jScrollPane2.setViewportView(chatTable);

        tablePanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jLabel1.setText(bundle.getString("ChatViewerPanel.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filterTable, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(filterTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tablePanel.add(jPanel1, java.awt.BorderLayout.SOUTH);

        viewerPanel.add(tablePanel, java.awt.BorderLayout.CENTER);

        add(viewerPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        try {
            this.caseFacade.audit(AuditingMessages.REFRESHING_CHAT);
            this.chatListModel.removeAllElements();
            
            ChatRefreshTask task = new ChatRefreshTask(this);
            task.startTask();
            
        } catch (Exception ex) {
            logger.severe(String.format("Cannot refreshing chat: %s", ex.getMessage()));
        }
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void loadItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadItemButtonActionPerformed
        try {
            JTableUtil.removeAllRows(this.chatTable);
            int row = this.chatJList.getSelectedIndex();
            if (row == - 1)
                return;
            
            this.caseFacade.audit(AuditingMessages.LOADING_CHAT);
            String path = String.valueOf(this.chatJList.getSelectedValue());
            ChatLoadingTask task = new ChatLoadingTask(this, path);
            task.startTask();
        } catch (Exception ex) {
            logger.severe(String.format("Cannot loding chat item $s, Exception is %s",
                    String.valueOf(this.chatJList.getSelectedValue()), ex.getMessage()));
        }   
    }//GEN-LAST:event_loadItemButtonActionPerformed

    
    public JList getList() { return this.chatJList; }
    public DefaultListModel getListModel() { return this.chatListModel; }
    public String getAgent() { return this.agent; }
    public JTable getTable() { return this.chatTable; }
    public CaseFacade getCaseFacade() { return this.caseFacade; }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList chatJList;
    private javax.swing.JPanel chatListPanel;
    private javax.swing.JPanel chatLoadingPanel;
    private javax.swing.JTable chatTable;
    private javax.swing.JTextField filterTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton loadItemButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JPanel selectionPanel;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JPanel viewerPanel;
    // End of variables declaration//GEN-END:variables
}
