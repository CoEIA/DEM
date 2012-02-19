/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ListFileAutherReportPanel.java
 *
 * Created on Jan 14, 2012, 8:17:57 AM
 */
package edu.coeia.reports.panels;

import edu.coeia.reports.ReportPanel;
import edu.coeia.gutil.JListUtil;
import edu.coeia.reports.DatasourceXml;
import edu.coeia.reports.IndexUtil;
import edu.coeia.reports.RawResultFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author wajdyessam
 */
public class FilesAutherReportPanel extends javax.swing.JPanel implements ReportGenerator, Runnable{

    private ReportPanel reportPanel ;
    private DefaultListModel srcListModel, destListModel;
    private Thread thread;
    /** Creates new form ListFileAutherReportPanel */
    public FilesAutherReportPanel(ReportPanel panel) {
        initComponents();
        this.reportPanel = panel;
        this.srcListModel = new DefaultListModel();
        this.destListModel = new DefaultListModel();
        
       
    }
    
    private void fillList() {
        try {
            List<String> authers = IndexUtil.getAllAuthers(this.reportPanel.getCaseFacade());
            for(String auther: authers) {
                JListUtil.addToList(auther, srcListModel, srcList);
            }
        } catch (IOException ex) {
            Logger.getLogger(FilesAutherReportPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public DatasourceXml generateReport() {
        DatasourceXml xmlSource= new DatasourceXml();
        List<String> authers = new ArrayList<String>();
        
        // make up list of authers
        for(int i=0; i<this.destListModel.getSize(); i++ ){
            String value = String.valueOf(this.destListModel.get(i));
            authers.add(value);
        }
        
         try {
            xmlSource = RawResultFile.getFileSystemXmlFile(
                    IndexUtil.getAllFilesHaveAuthers(this.reportPanel.getCaseFacade(), authers)
            ,this.reportPanel.getCaseFacade());
        } catch (IOException ex) {
            Logger.getLogger(FilesReportPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return xmlSource;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        srcList = new javax.swing.JList();
        moveAllButton = new javax.swing.JButton();
        moveOneButton = new javax.swing.JButton();
        returnOneButton = new javax.swing.JButton();
        returnAllButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        destList = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();

        jLabel1.setText("this report will display all the files that have specific authers");

        jLabel2.setText("please specify the auther you want");

        jScrollPane1.setViewportView(srcList);

        moveAllButton.setText(">>");
        moveAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveAllButtonActionPerformed(evt);
            }
        });

        moveOneButton.setText(">");
        moveOneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveOneButtonActionPerformed(evt);
            }
        });

        returnOneButton.setText("<");
        returnOneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnOneButtonActionPerformed(evt);
            }
        });

        returnAllButton.setText("<<");
        returnAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnAllButtonActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(destList);

        jButton1.setText("Load");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(moveAllButton, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                            .addComponent(returnAllButton, 0, 0, Short.MAX_VALUE)
                            .addComponent(returnOneButton, 0, 0, Short.MAX_VALUE)
                            .addComponent(moveOneButton, 0, 0, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(73, 73, 73))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(moveAllButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(moveOneButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(returnOneButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(returnAllButton))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void moveAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveAllButtonActionPerformed

        if (!srcListModel.isEmpty()) {
            for (int i = 0; i < this.srcListModel.getSize(); i++) {
                String value = String.valueOf(this.srcListModel.get(i));
                JListUtil.addToList(value, destListModel, destList);
            }
        }
    }//GEN-LAST:event_moveAllButtonActionPerformed

    private void moveOneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveOneButtonActionPerformed

            String value = String.valueOf(this.srcList.getSelectedValue());
            if (!JListUtil.existsInModel(value, destListModel)) {
                JListUtil.addToList(value, destListModel, destList);
            }
      
    }//GEN-LAST:event_moveOneButtonActionPerformed

    private void returnOneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnOneButtonActionPerformed

        if(!destListModel.isEmpty()){
        int index = this.destList.getSelectedIndex();
        if ( index < 0 ) return;
        
        this.destListModel.remove(index);
        }
    }//GEN-LAST:event_returnOneButtonActionPerformed

    private void returnAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnAllButtonActionPerformed
        //this.destList.removeAll();
        if(!destListModel.isEmpty()){
        this.destListModel.removeAllElements();
        }
    }//GEN-LAST:event_returnAllButtonActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
    
        thread = new Thread(this);
        thread.start();
            
}//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList destList;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton moveAllButton;
    private javax.swing.JButton moveOneButton;
    private javax.swing.JButton returnAllButton;
    private javax.swing.JButton returnOneButton;
    private javax.swing.JList srcList;
    // End of variables declaration//GEN-END:variables

    public void run() {
        
       this.fillList();   
    }
}