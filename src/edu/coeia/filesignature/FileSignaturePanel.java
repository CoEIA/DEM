/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FileSignaturePanel.java
 *
 * Created on Dec 10, 2011, 9:08:19 AM
 */
package edu.coeia.filesignature;

import edu.coeia.cases.Case;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.util.Utilities;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.mahout.math.Arrays;

/**
 *
 * @author Ahmed
 */
public class FileSignaturePanel extends javax.swing.JPanel   {
    
    protected DefaultTreeModel m_model;
    private FileSignature instance;
    private FileTreeModel model;
    private File          node;
    private List<FileSignature> listFiles; 
    /** Creates new form FileSignaturePanel */
    public FileSignaturePanel(Case aCase) {
        
        initComponents();
 
         instance = new FileSignature();
         List<String> caseLocation = aCase.getEvidenceSourceLocation();
        
        
        try {
            fillDataBaseTable();
        } catch (IOException ex) {
            Logger.getLogger(FileSignaturePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
        model = new FileTreeModel(new File(caseLocation.get(0)));
 
        FolderListTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        FolderListTree.setModel(model);
     

    }
       
        
    
    public void iterateFolders(File folder) {

        String[] children = folder.list();
        if (children == null) {
            System.out.println("does not exist or is not a directory");
        } else {
            for (int i = 0; i < children.length; i++) {
                String filename = children[i];
            }

        }
    }

    
    private void fillDataBaseTable() throws IOException {
        listFiles = instance.ParseFile();
        for (FileSignature fs : listFiles) {
            Object[] arr = {Arrays.toString(fs.getExtension()), fs.getSignature(), fs.getType(), fs.getID()};
            JTableUtil.addRowToJTable(SignatureTableDB, arr);
        }
    }

    private void FillSignatureTable(FileSignature fs, String FileName, String Status) {
        Object[] arr = {FileName, Arrays.toString(fs.getExtension()), Status, fs.getSignature(), fs.getType(), fs.getID()};
        JTableUtil.addRowToJTable(FileAnalysisTable, arr);
    }

    private void FillTableUnknownFile(String FileName, String Status) {
        Object[] arr = {FileName, Status};
        JTableUtil.addRowToJTable(FileAnalysisTable, arr);
    }

     
    /**
     * The methods in this class allow the JTree component to traverse
     * the file system tree, and display the files and directories.
     **/
    class FileTreeModel implements TreeModel {
        // We specify the root directory when we create the model.

        protected File root;
        
        public FileTreeModel(File root) {
            this.root = root;
        }

        // The model knows how to return the root object of the tree
        public Object getRoot() {
            return root;
        }

        // Tell JTree whether an object in the tree is a leaf or not
        public boolean isLeaf(Object node) {
            return ((File) node).isFile();
        }

        // Tell JTree how many children a node has
        public int getChildCount(Object parent) {
            String[] children = ((File) parent).list();
            if (children == null) {
                return 0;
            }
            return children.length;
        }

        // Fetch any numbered child of a node for the JTree.
        // Our model returns File objects for all nodes in the tree.  The
        // JTree displays these by calling the File.toString() method.
        public Object getChild(Object parent, int index) {
            String[] children = ((File) parent).list();
            if ((children == null) || (index >= children.length)) {
                return null;
            }
            return new File((File) parent, children[index]);
        }

        // Figure out a child's position in its parent node.
        public int getIndexOfChild(Object parent, Object child) {
            String[] children = ((File) parent).list();
            if (children == null) {
                return -1;
            }
            String childname = ((File) child).getName();
            for (int i = 0; i < children.length; i++) {
                if (childname.equals(children[i])) {
                    return i;
                }
            }
            return -1;
        }

        // This method is only invoked by the JTree for editable trees.  
        // This TreeModel does not allow editing, so we do not implement 
        // this method.  The JTree editable property is false by default.
        public void valueForPathChanged(TreePath path, Object newvalue) {
        }

        // Since this is not an editable tree model, we never fire any events,
        // so we don't actually have to keep track of interested listeners.
        public void addTreeModelListener(TreeModelListener l) {
        }

        public void removeTreeModelListener(TreeModelListener l) {
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        SignatureTableDB = new javax.swing.JTable();
        SelectFolderPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        FolderListTree = new javax.swing.JTree();
        jButton1 = new javax.swing.JButton();
        FileAnalysisPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        FileAnalysisTable = new javax.swing.JTable();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("DEM FileSignature DataBase:"));

        SignatureTableDB.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File Extension", "File Signature", "File Type", "File Category"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(SignatureTableDB);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                .addContainerGap())
        );

        SelectFolderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Select Folder"));

        FolderListTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                FolderListTreeValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(FolderListTree);

        jButton1.setText("Analyse File Signature");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SelectFolderPanelLayout = new javax.swing.GroupLayout(SelectFolderPanel);
        SelectFolderPanel.setLayout(SelectFolderPanelLayout);
        SelectFolderPanelLayout.setHorizontalGroup(
            SelectFolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SelectFolderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SelectFolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE))
                .addContainerGap())
        );
        SelectFolderPanelLayout.setVerticalGroup(
            SelectFolderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SelectFolderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        FileAnalysisPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("File Analysis "));

        FileAnalysisTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "File Name", "Status", "File Extension", "Signature", "File Type", "File Category"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(FileAnalysisTable);

        javax.swing.GroupLayout FileAnalysisPanelLayout = new javax.swing.GroupLayout(FileAnalysisPanel);
        FileAnalysisPanel.setLayout(FileAnalysisPanelLayout);
        FileAnalysisPanelLayout.setHorizontalGroup(
            FileAnalysisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FileAnalysisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE))
        );
        FileAnalysisPanelLayout.setVerticalGroup(
            FileAnalysisPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FileAnalysisPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SelectFolderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(FileAnalysisPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SelectFolderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(FileAnalysisPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
    boolean isDirectory = node.isDirectory();
    boolean matched = false;
    boolean unKnown     = true;
  
    if (isDirectory) {
    } else {
        try {
            for (FileSignature fs : listFiles) {
                if (FileSignature.matchBadSignature(node, fs)) {
                    //    JOptionPane.showMessageDialog(this, "Matched File ", "Verified", JOptionPane.ERROR_MESSAGE);
                    FillSignatureTable(fs, node.getName(), "Matched File");
                    matched = true;
                    unKnown = false;
                    break;
                }
                if (FileSignature.matchAliasSignature(node, fs)) {

                    if (FileSignature.isknownFile(node, fs)) {

                        FillSignatureTable(fs, node.getName(), " Alias file and it is a Known File");
                        unKnown = false;
                        matched = false;
                        
                    }
                }
              
            }
            if ( unKnown && !matched) {
                FillTableUnknownFile(node.getName(), "Unknown");
            }
            
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(FileSignaturePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileSignaturePanel.class.getName()).log(Level.SEVERE, null, ex);
        }



    }
   
               
}//GEN-LAST:event_jButton1ActionPerformed

private void FolderListTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_FolderListTreeValueChanged
 // TODO add your handling code here:
        node = (File) evt.getPath().getLastPathComponent();

        if (node == null) {
            return;
        }
    
}//GEN-LAST:event_FolderListTreeValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel FileAnalysisPanel;
    private javax.swing.JTable FileAnalysisTable;
    private javax.swing.JTree FolderListTree;
    private javax.swing.JPanel SelectFolderPanel;
    private javax.swing.JTable SignatureTableDB;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
