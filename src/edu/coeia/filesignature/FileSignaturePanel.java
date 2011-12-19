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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class FileSignaturePanel extends javax.swing.JPanel implements Runnable {

    protected DefaultTreeModel m_model;
    private FileSignature instance;
    private FileTreeModel model;
    private File node;
    private List<FileSignature> listFiles;

    public FileSignaturePanel() {
    }

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

    private void fillDataBaseTable() throws IOException {
        listFiles = instance.ParseFile();
        for (FileSignature fs : listFiles) {
            Object[] arr = {Arrays.toString(fs.getExtension()), fs.getSignature(), fs.getType(), fs.getID()};
            JTableUtil.addRowToJTable(SignatureTableDB, arr);
        }
    }

    private void FillSignatureTable(Object[] arr) {
        JTableUtil.addRowToJTable(FileAnalysisTable, arr);
    }
    
    private void FillTableUnknownFile(String FileName, String Status) {
        Object[] arr = {FileName, Status};
        JTableUtil.addRowToJTable(FileAnalysisTable, arr);
    }

    public class FolderTraversar {

        private String indent = "";
        private File originalFileObject;
        private File fileObject;

        public FolderTraversar(File fileObject) {
            this.originalFileObject = fileObject;
            this.fileObject = fileObject;
        }

        public void traverse() {
            recursiveTraversal(fileObject);
        }

        public void recursiveTraversal(File fileObject) {

        if (fileObject.isDirectory()) {
            indent = getIndent(fileObject);
            System.out.println(indent + fileObject.getName());
            try {
                File allFiles[] = fileObject.listFiles();
                for (File aFile : allFiles) {
                    recursiveTraversal(aFile);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (fileObject.isFile()) {
            System.out.println(indent + "  " + fileObject.getName());
            TestFileAnalysis(fileObject);
        }
        }

        private String getIndent(File fileObject) {
            String original = originalFileObject.getAbsolutePath();
            String fileStr = fileObject.getAbsolutePath();
            String subString =
                    fileStr.substring(original.length(), fileStr.length());

            String indent = "";
            for (int index = 0; index < subString.length(); index++) {
                char aChar = subString.charAt(index);
                if (aChar == File.separatorChar) {
                    indent = indent + "  ";
                }
            }
            return indent;
        }
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
                "File Name", "Status", "Signature", "File Extensions"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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

  private void TestFileAnalysis(File file) {
    boolean matched = false;
    boolean unKnown = true;
    Object[] status_matched = new Object[4];
    Object[] status_aliased = new Object[4];
    Set<String> signatureList = new HashSet<String>();
    List<String> extensionsList = new ArrayList<String>();
    if (file == null) {
        return;                             
    }
    try {
        for (FileSignature fs : listFiles) {
            if (FileSignature.matchBadSignature(file, fs)) {
                status_matched = FormatTable(file, "Matched File", fs, signatureList,extensionsList);
                matched = true;
                unKnown = false;
            }
            if (FileSignature.matchAliasSignature(file, fs) && !matched) {
                if (FileSignature.isknownFile(file, fs)) {
                    status_aliased = FormatTable(file, "Aliased File and it is Known", fs, signatureList,extensionsList);
                    unKnown = false;
                    matched = false;
                }
            }
        } // End For 

        // Check All States
        if (unKnown && !matched) {
            FillTableUnknownFile(file.getName(), "Unknown");
        }
        if (!unKnown && !matched) {
            FillSignatureTable(status_aliased);
        }
        if (!unKnown && matched) {
            FillSignatureTable(status_matched);
        }

    } catch (FileNotFoundException ex) {
        Logger.getLogger(FileSignaturePanel.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(FileSignaturePanel.class.getName()).log(Level.SEVERE, null, ex);
    }


    }

private Object[] FormatTable(File file, String message, FileSignature fs, Set<String> SignatureList, List<String> Exenstions) {

    Object[] status_msg = new Object[4];

    status_msg[0] = file.getName();
    status_msg[1] = message;
    
    SignatureList.add(fs.getSignature());
    String formatedSignatures = Utilities.getFormattedStringHash(SignatureList);
    status_msg[2] = formatedSignatures;

    Exenstions.add(Arrays.toString(fs.getExtension()));
    String formatedExtensions = Utilities.getFormattedString(Exenstions);
    status_msg[3] = formatedExtensions;

    return status_msg;
    }
private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
    boolean isDirectory = node.isDirectory();
    JTableUtil.removeAllRows(FileAnalysisTable);
    Thread newThrd = new Thread(this);
    if (isDirectory) {
        newThrd.start();
    } else {
        TestFileAnalysis(node);
    }

}//GEN-LAST:event_jButton1ActionPerformed

    public void run() {
        System.out.println("MyThread starting.");
        FolderTraversar ft = new FolderTraversar(node);
        ft.traverse();
        System.out.println("MyThread terminating.");
    }
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
