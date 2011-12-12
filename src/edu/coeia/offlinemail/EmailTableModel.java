/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.offlinemail;

/**
 *
 * @author wajdyessam
 */

import com.pff.PSTFile ;

import javax.swing.table.AbstractTableModel ;

import java.util.ArrayList;

class EmailTableModel extends AbstractTableModel {
    private ArrayList<MessageHeader> map = new ArrayList<MessageHeader>();
    private PSTFile pstFile ;
    private String path ;
    
    public EmailTableModel (PSTFile pst, String path) {
        this.pstFile = pst ;
        this.path = path ;
    }
    
    String[] columnNames = {
    	"Descriptor ID",
    	"Subject",
    	"From",
    	"To",
    	"Date",
    	"Has Attachments"
    };

    String[][] rowData = {{"","","","",""}};
    int rowCount = 0;

    @Override
    public String getColumnName(int col) {
        return columnNames[col].toString();
    }

    public int getColumnCount() { return columnNames.length; }

    public MessageHeader getMessageAtRow(int row) {
        if ( map == null )
            return null ;

        return map.get(row);
    }

    public int getRowCount() {
    	try {
            return map.size();
    	} catch (Exception err) {
            return 0 ;
    	}
    }

    public Object getValueAt(int row, int col) {
    	try {
            MessageHeader next = getMessageAtRow(row);
            switch (col) {
                case 0:
                    return next.getID();
                case 1:
                    return next.getSubject();
                case 2:
                    return next.getFrom();
                case 3:
                    return next.getTo();
                case 4:
                    return next.getDate();
                case 5:
                    return (next.hasAttachment() ? "Yes" : "No");
            }
        } catch (Exception e) {
        }

        return "";
    }

    @Override
    public boolean isCellEditable(int row, int col) { return false; }

    public void setFolder(String name) {
        map = EmailReader.getInstance(pstFile, path,null);
        map = filterMap(name);
    	this.fireTableDataChanged();
    }

    private ArrayList<MessageHeader> filterMap (String name) {
        ArrayList<MessageHeader> msgs = new ArrayList<MessageHeader>();
        
        for (MessageHeader m: map) {
            if ( m.getLocation().equals(name))
                msgs.add(m);
        }

        return msgs;
    }
}
