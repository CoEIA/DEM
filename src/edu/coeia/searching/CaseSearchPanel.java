/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CaseSearchPanel.java
 *
 * Created on Oct 22, 2011, 9:44:48 AM
 */
package edu.coeia.searching;

import edu.coeia.cases.Case;

import javax.swing.JFrame;


/**
 *
 * @author wajdyessam
 */
public class CaseSearchPanel extends javax.swing.JPanel {

    private AdvancedSearchPanel advancedSearchPanel ;
    
    /** Creates new form CaseSearchPanel */
    public CaseSearchPanel(Case aIndex, JFrame parentFrame) {
        initComponents();
        
        // make tap panel
        this.advancedSearchPanel = new AdvancedSearchPanel(aIndex, parentFrame);
        //ConnectedSearchPanel connectedSearchPanel = new ConnectedSearchPanel();
        
        this.caseSearchTappedPane.add("Advanced Search", advancedSearchPanel);
        //this.caseSearchTappedPane.add("Connected Search", connectedSearchPanel);
    }
    
    public void setFocusInAdvancedSearchPanel () {
        this.advancedSearchPanel.setQueryTextFeildFocusable();
    }

    public void closeSearcher() {
        this.advancedSearchPanel.closeLuceneSearch();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        caseSearchTappedPane = new javax.swing.JTabbedPane();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(caseSearchTappedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(caseSearchTappedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane caseSearchTappedPane;
    // End of variables declaration//GEN-END:variables
}
