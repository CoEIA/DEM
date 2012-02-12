/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ListAllExtensionsReportPanel.java
 *
 * Created on Jan 14, 2012, 8:14:03 AM
 */
package edu.coeia.reports.panels;

import edu.coeia.reports.ReportPanel;
import edu.coeia.reports.DatasourceXml;
import edu.coeia.reports.IndexUtil;
import edu.coeia.reports.RawResultFile;

import java.io.IOException;

import java.util.Map;

/**
 *
 * @author wajdyessam
 */
public class FilesExtensionReportPanel extends javax.swing.JPanel implements ReportGenerator{

    private ReportPanel reportPanel ;
    
    /** Creates new form ListAllExtensionsReportPanel */
    public FilesExtensionReportPanel(ReportPanel panel) {
        initComponents();
        this.reportPanel = panel;
    }

    @Override
    public DatasourceXml generateReport() throws IOException {       
        Map<String, Double> extensions = IndexUtil.getAllFilesFrequency(this.reportPanel.getCaseFacade());
        return RawResultFile.getExtensionFrequencyXmlFile(extensions, this.reportPanel.getCaseFacade());
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

        jLabel1.setText("this report will display the frequency of all files extensions inside the case");

        jLabel2.setText("also will display pie chart to visualize the result ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap(67, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables


}
