/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.gutil;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.util.FileUtil;

import javax.swing.JLabel;
import javax.swing.JViewport ;
import javax.swing.JTable ;
import javax.swing.table.TableModel ;
import javax.swing.table.DefaultTableColumnModel ;
import javax.swing.table.TableColumn ;
import javax.swing.table.TableCellRenderer ;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter ;
import javax.swing.RowSorter ;
import javax.swing.SortOrder;
import javax.swing.DefaultRowSorter ;
import javax.swing.RowSorter.SortKey ;
import javax.swing.RowFilter ;

import java.awt.Point ;
import java.awt.Rectangle ;
import java.awt.Component ;

import java.util.List ;
import java.util.ArrayList ;
import java.util.regex.PatternSyntaxException;

public class JTableUtil {
    
    public static void removeAllRows (JTable table) {
        if ( table.getModel().getRowCount() <= 0 )
            return; 
        
        TableModel model = table.getModel();
        ( (DefaultTableModel) model ).getDataVector().removeAllElements();
        ( (DefaultTableModel) model ).fireTableDataChanged();

        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);
        sorter.setRowFilter(null);
    }
    
    public static void scrollToVisible(JTable table, int rowIndex, int vColIndex) {
        if (!(table.getParent() instanceof JViewport))
            { return; }

        JViewport viewport = (JViewport)table.getParent();

        // This rectangle is relative to the table where the
        // northwest corner of cell (0,0) is always (0,0).
        Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);

        // The location of the viewport relative to the table
        Point pt = viewport.getViewPosition();

        // Translate the cell location so that it is relative
        // to the view, assuming the northwest corner of the
        // view is (0,0)
        rect.setLocation(rect.x-pt.x, rect.y-pt.y);

        // Scroll the area into view
        viewport.scrollRectToVisible(rect);
    }
    
    public static void packColumns(JTable table, int margin) {
        for (int c=0; c<table.getColumnCount(); c++) {
            packColumn(table, c, 2);
        }
    }

    // Sets the preferred width of the visible column specified by vColIndex. The column
    // will be just wide enough to show the column head and the widest cell in the column.
    // margin pixels are added to the left and right
    // (resulting in an additional width of 2*margin pixels).
    public static void packColumn(JTable table, int vColIndex, int margin) {
        TableModel model = table.getModel();
        DefaultTableColumnModel colModel = (DefaultTableColumnModel)table.getColumnModel();
        TableColumn col = colModel.getColumn(vColIndex);
        int width = 0;

        // Get width of column header
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }
        Component comp = renderer.getTableCellRendererComponent(
            table, col.getHeaderValue(), false, false, 0, 0);
        width = comp.getPreferredSize().width;

        // Get maximum width of column data
        for (int r=0; r<table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, vColIndex);
            comp = renderer.getTableCellRendererComponent(
                table, table.getValueAt(r, vColIndex), false, false, r, vColIndex);
            width = Math.max(width, comp.getPreferredSize().width);
        }

        // Add margin
        width += 2*margin;

        // Set the width
        col.setPreferredWidth(width);
    }

    public static void setTableAlignmentValue (JTable table, int index) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment( JLabel.CENTER );
        table.getColumnModel().getColumn(index).setCellRenderer(renderer );
    }

    public static void sortTable( JTable table, int index ) {
        table.setAutoCreateRowSorter(true);
        DefaultRowSorter<?,?> sorter = (DefaultRowSorter<?,?>) table.getRowSorter();

        List<SortKey> list = new ArrayList<SortKey>();
        SortKey sort = new RowSorter.SortKey(index, SortOrder.ASCENDING) ;
        list.add( sort );
        sorter.setSortKeys(list);
        sorter.sort();
    }
    
    public static void exportJTable (JTable table, String name)  throws Exception {
        StringBuilder content = new StringBuilder("");

        for (int i=0; i<table.getModel().getRowCount(); i++){
            for (int j=0; j<table.getModel().getColumnCount(); j++) {
                int col = table.convertColumnIndexToView(j);
                String value = null ;

                try {
                    value = (String) table.getModel().getValueAt(i, col);
                }
                catch (java.lang.ClassCastException e) {
                    value = String.valueOf( table.getModel().getValueAt(i, col) );
                }
                
                if ( value == null)
                    value = "" ;

                value.replaceAll(",", "");
                content.append(value + ",");
            }
            content.append("\n");
        }

        ArrayList<String> data = new ArrayList<String>();
        data.add(content.toString());
        
        FileUtil.writeToFile(data,name);
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
    
    public static void filterTable (final JTable table, final ArrayList<String> aList) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);

        sorter.setRowFilter( new RowFilter<TableModel, Object>() {
            public boolean include (Entry entry) {
                String row = String.valueOf(entry.getValue(0));
                
                if ( aList.contains(row))
                    return true;
                else
                    return false;

            }
        });
    }
    
    /**
     * Add new row to table
     */
    public static void addRowToJTable(final JTable table, final Object[] data) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.addRow(data);
    }
}
