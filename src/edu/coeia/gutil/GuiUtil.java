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
    public static void changeLookAndFeel (final String lookName, JFrame frame ) throws Exception  {
        UIManager.setLookAndFeel(lookName);
        SwingUtilities.updateComponentTreeUI(frame);
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
}
