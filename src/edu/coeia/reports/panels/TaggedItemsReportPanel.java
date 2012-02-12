/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TaggedItemsReportPanel.java
 *
 * Created on Jan 16, 2012, 2:11:26 PM
 */
package edu.coeia.reports.panels;

import edu.coeia.reports.ReportPanel;
import edu.coeia.reports.DatasourceXml;
import edu.coeia.reports.RawResultFile;
import edu.coeia.tags.TagsManager;

import java.io.IOException;

/**
 *
 * @author Ahmed
 */
public class TaggedItemsReportPanel extends javax.swing.JPanel implements ReportGenerator {
    private final ReportPanel reportPanel ;
    private final TagsManager tags;
    
    /** Creates new form TaggedItemsReportPanel */
    public TaggedItemsReportPanel(ReportPanel panel, TagsManager manager) {
        initComponents();
        this.reportPanel = panel;
        this.tags  = manager;
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

        jLabel1.setText("This report will listting all the tagged items  inside the case.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(134, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1)
                .addContainerGap(87, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    public DatasourceXml generateReport() throws IOException {
        return RawResultFile.getTaggedItems(tags, this.reportPanel.getCaseFacade());
    }
}
