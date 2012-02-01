/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ChatSourceViewerPanel.java
 *
 * Created on Nov 30, 2011, 7:42:02 AM
 */
package edu.coeia.viewer;

import edu.coeia.items.ChatItem;
import edu.coeia.items.Item;

/**
 *
 * @author wajdyessam
 */

class ChatSourceViewerPanel extends javax.swing.JPanel {
    private final String keyword ;
    private final SourceViewerDialog searchViewerDialog ;
    private final Item item;
    
    /** Creates new form ChatSourceViewerPanel */
    public ChatSourceViewerPanel(SourceViewerDialog dialog, final Item item) {
        initComponents();
        
        this.searchViewerDialog = dialog;
        this.keyword = dialog.getQueryString();
        this.item = item;
        
        this.displayDocumentInformation();
    }

    private void displayDocumentInformation () {        
        ChatItem chatItem = (ChatItem) this.item;
        
        String chatAgent = chatItem.getChatAgent();
        String chatPath = chatItem.getChatFilePath();
        String date = chatItem.getDate();
        String from = chatItem.getFrom();
        String to = chatItem.getTo();
        String message = chatItem.getMessageText();

        this.chatAgentTextField.setText(chatAgent);
        this.chatPathTextField.setText(chatPath);
        this.dateTextField.setText(date);
        this.chatFromTextField.setText(from);
        this.chatToTextField.setText(to);
        this.messageTextField.setText(message);
    }
        
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        properitiesPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        chatAgentTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        chatPathTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        dateTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        messageTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        chatFromTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        chatToTextField = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jLabel1.setText("Chat Agent:");

        chatAgentTextField.setEditable(false);
        chatAgentTextField.setText(" ");

        jLabel2.setText("Chat Path:");

        chatPathTextField.setEditable(false);
        chatPathTextField.setText(" ");

        jLabel3.setText("Last Modification:");

        dateTextField.setEditable(false);
        dateTextField.setText(" ");

        jLabel4.setText("Message:");

        messageTextField.setEditable(false);
        messageTextField.setText(" ");

        jLabel5.setText("Message From:");

        chatFromTextField.setEditable(false);
        chatFromTextField.setText(" ");

        jLabel6.setText("Chat To:");

        chatToTextField.setEditable(false);
        chatToTextField.setText(" ");

        javax.swing.GroupLayout properitiesPanelLayout = new javax.swing.GroupLayout(properitiesPanel);
        properitiesPanel.setLayout(properitiesPanelLayout);
        properitiesPanelLayout.setHorizontalGroup(
            properitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(properitiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(properitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(properitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(messageTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                    .addComponent(dateTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                    .addComponent(chatPathTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                    .addComponent(chatAgentTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                    .addComponent(chatFromTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                    .addComponent(chatToTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE))
                .addContainerGap())
        );
        properitiesPanelLayout.setVerticalGroup(
            properitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(properitiesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(properitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(chatAgentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(properitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(chatPathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(properitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(dateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(properitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(messageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(properitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(chatFromTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(properitiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(chatToTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(properitiesPanel, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField chatAgentTextField;
    private javax.swing.JTextField chatFromTextField;
    private javax.swing.JTextField chatPathTextField;
    private javax.swing.JTextField chatToTextField;
    private javax.swing.JTextField dateTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField messageTextField;
    private javax.swing.JPanel properitiesPanel;
    // End of variables declaration//GEN-END:variables
}
