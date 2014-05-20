/* 
 * Copyright (C) 2014 Center of Excellence in Information Assurance
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.coeia.gutil;

import edu.coeia.util.GUIFileFilter ;
import edu.coeia.util.Utilities;

import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JFileChooser ;
import javax.swing.JTable ;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import java.awt.CardLayout ;
import java.awt.Desktop;
import java.awt.event.ActionEvent;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import java.net.URISyntaxException;

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
    }
    
    public static void showPopUpForTableIfEnabled(final JTable table, final MouseEvent event) {
        if ( (event.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
            if ( table.isEnabled() )
                GuiUtil.showPopup(event);
        }
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
    
    public void showPopupWithLunch (java.awt.event.MouseEvent event) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new GUIFileFilter("Text Files (*.txt)", "txt"));
        
        final JTable table = (JTable) event.getSource();
        JPopupMenu popup = new JPopupMenu();

        // select table row with right click
        // get the coordinates of the mouse click
        java.awt.Point p = event.getPoint();

        // get the row index that contains that coordinate
        int rowNumber = table.rowAtPoint( p );

        // Get the ListSelectionModel of the JTable
        ListSelectionModel model = table.getSelectionModel();

        // set the selected interval of rows. Using the "rowNumber"
        // variable for the beginning and end selects only that one row.
        model.setSelectionInterval( rowNumber, rowNumber );

        // add export item
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
                    e.printStackTrace();
                }
            }
        });

        // add copy item
        JButton copyBtn = new JButton("Copy URL");
        copyBtn.addActionListener( new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                String data = (String) table.getValueAt(row, 0);
                Utilities.setToClipBoard(data);
            }
        });

        // add lunch browser
        JButton lunchBtn = new JButton("Lunch Browser");
        lunchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed (ActionEvent event){
                Desktop desktop = null ;

                if (Desktop.isDesktopSupported()) {
                    desktop = Desktop.getDesktop();

                    if ( desktop.isSupported(Desktop.Action.BROWSE) ) {
                        try {
                            int row = table.getSelectedRow();
                            String data = (String) table.getValueAt(row, 0);
               
                            java.net.URI uri = new java.net.URI(data);
                            desktop.browse(uri);
                        }
                        catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        

        popup.add(btn);
        popup.add(copyBtn);
        popup.add(lunchBtn);
        
        table.setComponentPopupMenu(popup);
    }
}
