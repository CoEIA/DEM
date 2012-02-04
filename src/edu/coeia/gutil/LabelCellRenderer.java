/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.gutil;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author wajdyessam
 */
public class LabelCellRenderer  extends JLabel implements TableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {

        JLabel lbl = (JLabel) value;
        this.setText(lbl.getText());
        this.setIcon(lbl.getIcon());
        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setHorizontalTextPosition(SwingConstants.CENTER);

        lbl.setOpaque(true);

        if (isSelected) {
            lbl.setBackground(table.getSelectionBackground());
            lbl.setForeground(table.getSelectionForeground());
        } else {
            lbl.setBackground(table.getBackground());
            lbl.setForeground(table.getForeground());
        }

        return lbl;
    }
}
