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
