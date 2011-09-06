/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.main.util;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.email.util.Message;
import java.io.File ;
import java.io.FileNotFoundException ;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.BufferedReader ;
import java.io.InputStreamReader ;
import java.io.IOException ;

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
import javax.swing.filechooser.FileSystemView;

import java.awt.Point ;
import java.awt.Rectangle ;
import java.awt.Component ;
import java.awt.Toolkit ;
import java.awt.datatransfer.StringSelection ;

import java.util.List ;
import java.util.Calendar ;
import java.util.Date ;
import java.util.Scanner ;
import java.util.ArrayList ;

import java.text.SimpleDateFormat ;
import java.text.DateFormat ;
import java.text.DecimalFormat;

public class Utilities {

    public static boolean isALocalDirve (String path) {
        boolean result = false;

        File file = new File(path);
        String desc = FileSystemView.getFileSystemView().getSystemTypeDescription(file);

        if ( desc.startsWith("Local Disk") )
                result = true;

        return (result);
    }

   // Execute program and read the output stream
    public static ArrayList<String> readProgramOutputStream (String path) throws IOException {
	Process process = Runtime.getRuntime().exec(path);
	BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()) );

	String line = null ;
	ArrayList<String> result = new ArrayList<String>();
	while ( (line = input.readLine() ) != null ) {
            result.add( line );
	}

	input.close();
	return ( result );
    }

    public static String filterLine (String line) {
        StringBuilder result = new StringBuilder() ;

        for (int i=0; i<line.length(); i++) {
            if ( i % 60 == 0)
                result.append("<br></br>");

            result.append(line.charAt(i));
        }

        return result.toString() ;
    }
    
    public static String reverseHost ( String host ) {
        StringBuilder tmp = new StringBuilder();

        for (int i=host.length()-2; i>=0; i--)
                tmp.append( host.charAt(i) );

        return tmp.toString();
    }

    public static String getFireFoxUserName (String path) {
        int sIndex = path.indexOf("Settings\\") + "Settings".length() + 1 ;
        int eIndex = path.indexOf("Application Data") - 1 ;

        String user = path.substring(sIndex, eIndex);
        return user ;
    }

    public static void setToClipBoard (String content) {
        StringSelection str = new StringSelection(content);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);
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
        
        writeToFile(data,name);
    }

    public static String getTimeFromMilliseconds (long ms) {
	int time = (int) ms / 1000 ;
	int seconds = time %  60 ;
	int mintues = (time % 3600 ) / 60 ;
	int hours = time / 3600 ;

	String format = String.format("%%0%dd", 2);
	String secondsString = String.format(format, seconds);
	String mintuesString = String.format(format, mintues);
	String hoursString   = String.format(format, hours  );

	return hoursString + ":" + mintuesString + ":" + secondsString ;
    }

    public static String getExtension (File f){
        if ( !f.exists() || f.isDirectory() ) {
            //System.out.println("File: " + f.getAbsolutePath());
            return null ;
        }
        
        int index = f.getAbsolutePath().lastIndexOf(".");
        
        if ((index < 0) && (index >= f.toString().length()))
            return null ;

        String ext = f.toString().substring(index+1);

        return (ext);
    }

    public static String getExtension (String f){
        return getExtension(new File(f));
    }

    public static boolean isFound (List<String> aList , String userName) {
        for (String str: aList) {
            if ( userName.equals(str.trim()))
                return (true);
        }

        return false;
    }

    public static boolean isMessageFound (List<Message> aList , String userName) {
        for (Message str: aList) {
            if ( userName.equals(str.getSenderName().trim()))
                return (true);
        }

        return false;
    }

    public static int getMessageIndex (List<Message> list, Message msg) {
        for (int i=0 ; i<list.size() ; i++) {
            if ( msg.getSenderName().equals(list.get(i).getSenderName()))
                return i ;
        }

        return -1 ;
    }

    public static boolean isExtentionAllowed (List<String> extensionsAllowed, String ext) {
        for (String str: extensionsAllowed)
            if ( str.equalsIgnoreCase(ext))
                return true ;

        return (false);
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

    public static void removeAllRows (JTable table) {
        TableModel model = table.getModel();
        ( (DefaultTableModel) model ).getDataVector().removeAllElements();
        ( (DefaultTableModel) model ).fireTableDataChanged();

        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);
        sorter.setRowFilter(null);
    }

    public static void sortTable( JTable table, int index ) {
        table.setAutoCreateRowSorter(true);
        DefaultRowSorter<?,?> sorter = (DefaultRowSorter<?,?>) table.getRowSorter();

        ArrayList<SortKey> list = new ArrayList<SortKey>();
        SortKey sort = new RowSorter.SortKey(index, SortOrder.ASCENDING) ;
        list.add( sort );
        sorter.setSortKeys(list);
        sorter.sort();
    }
    
    public static String getCurrentDate () {
        Date d = new Date();
        return formatDate(d);
    }

    public static String getFirstDate () {
        Date d = new Date(0);
        return formatDate(d);
    }

    public static String formatDate (Date d) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	Calendar c = Calendar.getInstance();
	c.setTime(d);
	return df.format( c.getTime() );
    }
    
    public static String formatDateForLogFileName(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return dateFormat.format(calendar.getTime());
    }

    public static String formatDateTime (Date d) {
        DateFormat df = new SimpleDateFormat("hh:mm:ss - dd/MM/yyyy");
	Calendar c = Calendar.getInstance();
	c.setTime(d);
	return df.format( c.getTime() );
    }
    
    public static String getFileContent (File file) throws FileNotFoundException {
        Scanner input = new Scanner(file);
        StringBuilder content = new StringBuilder("");
        while ( input.hasNext() )
            content.append( input.nextLine() );

        return content.toString();
    }

   public static String getFileContentWithSpace (File file) throws FileNotFoundException {
        Scanner input = new Scanner(file);
        StringBuilder content = new StringBuilder("");
        while ( input.hasNext() )
            content.append( input.nextLine() + " ");

        return content.toString();
    }

    public static ArrayList<String> getFileContentInArrayList (File file) throws FileNotFoundException {
        ArrayList<String> aList = new ArrayList<String>();
        Scanner input = new Scanner(file);

        while ( input.hasNext() ) {
            aList.add(input.nextLine());
        }

        return (aList);
    }

    public static double toKB (double n) {
        return n/1024 ;
    }

    public static double toMB (double n) {
        return toKB(n)/ 1024 ;
    }

    public static double toGB (double n) {
        return toMB(n) / 1024 ;
    }

    public static String formatSize (double number) {
        return new DecimalFormat("##.##").format(number) ;
    }

    public static boolean removeDirectory (File dirPath) {
        if ( dirPath.isDirectory() ) {
            File[] files = dirPath.listFiles() ;

            for(File file: files ) {
                if ( file.isDirectory() )
                    removeDirectory(file);
                else {
                    boolean status = file.delete() ;

                    if ( ! status )
                        return false ;
                }
            }
        }

        return dirPath.delete() ;
    }

    public static void writeToFile (ArrayList<String> data, String fileName) throws FileNotFoundException, UnsupportedEncodingException {
        File file = new File(fileName);
        PrintWriter writer = new PrintWriter(file,"UTF-8");

        for(String line: data) {
            writer.println(line );
        }

        writer.close();
    }
}
