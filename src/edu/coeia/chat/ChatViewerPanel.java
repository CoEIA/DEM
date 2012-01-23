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

import edu.coeia.cases.Case;
import edu.coeia.gutil.JListUtil;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.items.ChatItem;
import edu.coeia.items.ItemFactory;
import edu.coeia.util.FilesPath;

import java.awt.Cursor;
import java.awt.EventQueue;

import java.io.File;
import java.io.IOException;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author wajdyessam
 */
public class ChatViewerPanel extends javax.swing.JPanel {

    private final Case aCase;
    private final DefaultListModel chatListModel;
    private final String agent;
    
    /** Creates new form ChatViewerPanel */
    public ChatViewerPanel(final Case aCase, final String agent) {
        initComponents();
        this.aCase = aCase;
        this.chatListModel = new DefaultListModel();
        this.agent = agent;
        
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

        refreshButton.setText("Refresh List");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });
        chatLoadingPanel.add(refreshButton);

        loadItemButton.setText("Load Selected Item");
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

        jLabel1.setText("Filter Table:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filterTable, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
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
            this.chatListModel.removeAllElements();
            this.refreshList();
        } catch (Exception ex) {
            Logger.getLogger(ChatViewerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void loadItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadItemButtonActionPerformed
        try {
            JTableUtil.removeAllRows(this.chatTable);
            
            int row = this.chatJList.getSelectedIndex();
            if (row == - 1)
                return;
            
            String path = String.valueOf(this.chatJList.getSelectedValue());
            this.displayChatSessions(path);
            
        } catch (IOException ex) {
            Logger.getLogger(ChatViewerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }//GEN-LAST:event_loadItemButtonActionPerformed

    private void refreshList() throws Exception {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        ProgressDialog progressDialog = new ProgressDialog(null, false);
        progressDialog.setVisible(true);
        
        Task task = new Task(progressDialog);
        task.execute();
        
//        Set<String> files = getChatFilePath(this.agent);
//        for(String file: files) {
//            File path = new File(file);
//            JListUtil.addToList(path.getName(), chatListModel, chatJList);
//        }
    }

    private class Task extends SwingWorker<Void, Void> {
        private final ProgressDialog dialog;
        
        public Task (final ProgressDialog dialog) {
            this.dialog = dialog;
        }
        
        @Override
        public Void doInBackground() {
            try {
                getChatFilePath();
            } catch (IOException ex) {
                Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        private Set<String> getChatFilePath() throws IOException{
            String indexDir = aCase.getCaseLocation() + "\\" + FilesPath.INDEX_PATH;
            Directory dir = FSDirectory.open(new File(indexDir));
            IndexReader indexReader = IndexReader.open(dir);
            Set<String> aList = new HashSet<String>();
            this.dialog.setMax(indexReader.maxDoc());
            
            for (int i=0; i<indexReader.maxDoc(); i++) {
                this.dialog.setValue(i);
                Document document = indexReader.document(i);
                if ( document != null ) {
                    Field field = document.getField(IndexingConstant.CHAT_FILE);
                    if ( field != null && field.stringValue() != null) {

                       if ( document.getField(IndexingConstant.CHAT_AGENT).stringValue().equals(agent)) {
                           String chatFile = field.stringValue();
                           final File path = new File(chatFile);
                           EventQueue.invokeLater(new Runnable() {
                               @Override
                               public void run() { 
                                   JListUtil.addToList(path.getName(), chatListModel, chatJList);
                               }
                           });
                           
                       }
                    }
                }
            }
            indexReader.close();

            return aList;
        }

        @Override
        public void done() {
            System.out.println("end");
            this.dialog.dispose();
            setCursor(null);
        }
    }
    
    private Set<String> getChatFilePath(final String agent) throws IOException{
        String indexDir = this.aCase.getCaseLocation() + "\\" + FilesPath.INDEX_PATH;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        Set<String> aList = new HashSet<String>();
        
        for (int i=0; i<indexReader.maxDoc(); i++) {
            Document document = indexReader.document(i);
            if ( document != null ) {
                Field field = document.getField(IndexingConstant.CHAT_FILE);
                if ( field != null && field.stringValue() != null) {
                    
                   if ( document.getField(IndexingConstant.CHAT_AGENT).stringValue().equals(agent)) {
                       String chatFile = field.stringValue();
                       aList.add(chatFile);
                   }
                }
            }
        }
        indexReader.close();
        
        return aList;
    }
    
    private void displayChatSessions(final String fileName) throws IOException{
        String indexDir = this.aCase.getCaseLocation() + "\\" + FilesPath.INDEX_PATH;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);

        for (int i=0; i<indexReader.maxDoc(); i++) {
            Document document = indexReader.document(i);
            if ( document != null ) {
                Field field = document.getField(IndexingConstant.CHAT_FILE);
                if ( field != null && field.stringValue() != null) {
                    
                   if ( field.stringValue().endsWith(fileName)) {
                       ChatItem item = (ChatItem) ItemFactory.newInstance(document, aCase);
                       Object[] data = new Object[] {item.getFrom(), item.getTo(), item.getMessageText(),
                            item.getDate()};
                       JTableUtil.addRowToJTable(chatTable, data);
                       
                   }
                }
            }
        }
        indexReader.close();
    }
    
    
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
