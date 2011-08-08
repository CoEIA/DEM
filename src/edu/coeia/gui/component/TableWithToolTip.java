/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.gui.component;

/**
 *
 * @author wajdyessam
*/

import java.awt.Component ;

import javax.swing.JComponent ;
import javax.swing.JTable ;
import javax.swing.table.TableCellRenderer ;

public class TableWithToolTip extends JTable{

    // code from: http://www.exampledepot.com/egs/javax.swing.table/Tips.html
    @Override
    public Component prepareRenderer(TableCellRenderer renderer,int rowIndex, int vColIndex) {
        Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
        if (c instanceof JComponent) {
            JComponent jc = (JComponent)c;
            jc.setToolTipText((String)getValueAt(rowIndex, vColIndex));
        }
        return c;
    }
}
