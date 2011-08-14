/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.main.utilties;

import edu.coeia.utility.Utilities;
import edu.coeia.utility.FilesFilter ;

import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JFileChooser ;
import javax.swing.JTable ;
import javax.swing.JPanel;
import javax.swing.RowFilter ;
import javax.swing.table.TableRowSorter ;
import javax.swing.table.TableModel ;

import java.awt.CardLayout ;

import java.util.regex.PatternSyntaxException;

/**
 *
 * @author wajdyessam
 */

public class GuiUtil {
    
    public static void showPanel (String panelName, JPanel name) {
        CardLayout card = (CardLayout) name.getLayout();
        card.show(name, panelName);
    }
    
    // filer table, ignore case (case insensitive)
    public static void filterTable (JTable table, String text) {
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
    
    public static void showPopup (java.awt.event.MouseEvent event) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FilesFilter("Text Files (*.txt)", "txt"));
    
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
}
