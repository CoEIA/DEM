/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.gutil;

import edu.coeia.util.GUIFileFilter ;

import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JFileChooser ;
import javax.swing.JTable ;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.CardLayout ;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 *
 * @author wajdyessam
 */

public class GuiUtil {
    
    public static void showPanel (String panelName, JPanel name) {
        CardLayout card = (CardLayout) name.getLayout();
        card.show(name, panelName);
    }
    
    /*
     * Change the look and Feel
     */
    public static void changeLookAndFeel (final String lookName, final JFrame frame ){
        try {
            UIManager.setLookAndFeel(lookName);
            SwingUtilities.updateComponentTreeUI(frame);
            frame.pack();
        }
        catch(Exception e) {
            
        }
        
//        UIManager.setLookAndFeel(lookName);
//        CaseMainFrame caseFrame = (CaseMainFrame) frame;
//        CaseManagerFrame parent = caseFrame.getParentFrame();
//        SwingUtilities.updateComponentTreeUI(parent);
//        parent.pack();
    }
    
    public static void showPopup (java.awt.event.MouseEvent event) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new GUIFileFilter("Text Files (*.txt)", "txt"));
    
        final JTable table = (JTable) event.getSource();
        JPopupMenu popup = new JPopupMenu();
        JButton btn = new JButton("Export to CSV File");
        
        btn.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed (java.awt.event.ActionEvent event) {
                try {
                    GUIFileFilter ffFilter = new GUIFileFilter("Comma Seperated Value","CSV");
                    fileChooser.setFileFilter(ffFilter);

                    int result = fileChooser.showSaveDialog(null);

                    if ( result == JFileChooser.APPROVE_OPTION) {
                        String name = fileChooser.getSelectedFile().getAbsolutePath();
                        JTableUtil.exportJTable(table,name);
                    }
                }
                catch (Exception e){
                }
            }
        });

        popup.add(btn);
        table.setComponentPopupMenu(popup);
    }
    
    public static void showErrorMessage(final JComponent parent,
            final String message, 
            final String title ) {
        
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public static void showInformationMessage(final JComponent parent, 
            final String messge, final String title) {
        
        JOptionPane.showMessageDialog(parent, messge, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
