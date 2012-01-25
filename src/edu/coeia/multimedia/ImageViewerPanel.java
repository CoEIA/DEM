/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ImageViewerPanel.java
 *
 * Created on Jan 25, 2012, 11:14:19 AM
 */
package edu.coeia.multimedia;

import edu.coeia.cases.Case;
import edu.coeia.cases.CasePathHandler;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.util.FilesPath;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;

import javax.imageio.ImageIO;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.IndexReader ;
import org.apache.lucene.store.Directory ;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import static org.imgscalr.Scalr.*;
import org.imgscalr.Scalr;

/**
 *
 * @author wajdyessam
 */
public class ImageViewerPanel extends javax.swing.JPanel {

    private int SCALE_FACTOR = 120;
    private int PAD_FACTOR = 2;
    private int ROWS_NUMBER = 10;
    private int IMAGE_PER_PAGE = 80;
    
    private final Case aCase;
    private int noOfImages ;
    
    /** Creates new form ImageViewerPanel */
    public ImageViewerPanel(final Case aCase) {
        initComponents();
        this.aCase = aCase;
    }
    
    private void displayImages(final List<String> images) throws IOException {
        this.renderPanel.removeAll();
        
        List<Icon> icons = new ArrayList<Icon>();
        for(String image: images) {
            try {
                File imageFile = new File(image);
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                BufferedImage scaledImage = createThumbnail(bufferedImage);
                Icon icon = new ImageIcon(scaledImage, imageFile.getName());
                icons.add(icon);
            }
            catch(Exception e) {
                System.out.println(image + " cannot be veweing");
            }
        }
        
        JList list = new JList(icons.toArray());
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setCellRenderer(new MyCellRenderer());
        list.setVisibleRowCount(ROWS_NUMBER);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        
        JScrollPane pane = new JScrollPane(list);
        this.renderPanel.add(pane, BorderLayout.CENTER);
    }
    
    private BufferedImage createThumbnail(BufferedImage img) {
        img = Scalr.resize(img, SCALE_FACTOR);
        return pad(img, PAD_FACTOR);
    }
        
    private class MyCellRenderer extends JLabel implements ListCellRenderer {
        public MyCellRenderer() {
            this.setOpaque(true);
        }
        
        @Override
        public Component getListCellRendererComponent(JList list, Object value, 
                int index, boolean isSelected, boolean cellHasFocus) {
            
            ImageIcon icon = (ImageIcon) value;
            
            this.setText(icon.getDescription());
            this.setIcon(icon);
            this.setVerticalTextPosition(SwingConstants.BOTTOM);
            this.setHorizontalTextPosition(SwingConstants.CENTER);
            
            if ( isSelected ) {
                this.setBackground(Color.GRAY);
                this.setForeground(Color.WHITE);
            }
            else {
                this.setBackground(Color.WHITE);
                this.setForeground(Color.BLACK);
            }
            
            return this;
        }
    }
    
    private List<String> loadItems() throws IOException {
        List<String> files = new ArrayList<String>();
        
        String indexDir = this.aCase.getCaseLocation() + File.separator + FilesPath.INDEX_PATH;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        
        CasePathHandler handler = CasePathHandler.newInstance(aCase.getCaseLocation());
        handler.readConfiguration();
                            
        for (int i = 0; i < indexReader.maxDoc(); i++) {
            Document document = indexReader.document(i);
            
            if (document != null) {
                Field field = document.getField(IndexingConstant.FILE_MIME);
                
                if (field != null && field.stringValue() != null) {
                    String documentExtension = field.stringValue();
                    String fullpath = "";
                    
                    if (isImage(documentExtension) ) {
                        fullpath = handler.getFullPath(document.get(IndexingConstant.FILE_PATH));
                    }
                    
                    if ( ! fullpath.isEmpty() ) {
                        files.add(fullpath);
                    }
                }
            }
        }
        
        indexReader.close();
        return files;
    }
   
    private boolean isImage(String extension) {
        String[] extensions = {"jpg", "bmp", "gif", "tif", "png","psd"};
        return Arrays.asList(extensions).contains(extension);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ImageOptionPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        scaleTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        padTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        rowsTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        imagerPerPageTextField = new javax.swing.JTextField();
        applyOptionButton = new javax.swing.JButton();
        ImagePanel = new javax.swing.JPanel();
        controlPanel = new javax.swing.JPanel();
        nextLabel = new javax.swing.JLabel();
        backLabel = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        renderPanel = new javax.swing.JPanel();
        filterPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        filterImageTextField = new javax.swing.JTextField();
        loadImageButton = new javax.swing.JButton();
        StatusPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        imagePathTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        imageSizeTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        lastModificationTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        hasGeoTaggingTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        metadataTextArea = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        ImageOptionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Viewing Options"));

        jLabel1.setText("Scale Factor:");

        scaleTextField.setText(" ");

        jLabel2.setText("Image Pad:");

        padTextField.setText(" ");

        jLabel3.setText("No Row:");

        rowsTextField.setText(" ");

        jLabel4.setText("Image Per Page:");

        imagerPerPageTextField.setText(" ");

        applyOptionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/main/resources/save.png"))); // NOI18N
        applyOptionButton.setText("Apply Options");
        applyOptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyOptionButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ImageOptionPanelLayout = new javax.swing.GroupLayout(ImageOptionPanel);
        ImageOptionPanel.setLayout(ImageOptionPanelLayout);
        ImageOptionPanelLayout.setHorizontalGroup(
            ImageOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ImageOptionPanelLayout.createSequentialGroup()
                .addGroup(ImageOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scaleTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(padTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(rowsTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ImageOptionPanelLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel1))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ImageOptionPanelLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel3))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ImageOptionPanelLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel4))
                    .addComponent(imagerPerPageTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .addComponent(applyOptionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        ImageOptionPanelLayout.setVerticalGroup(
            ImageOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ImageOptionPanelLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scaleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(padTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rowsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(imagerPerPageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(applyOptionButton)
                .addGap(159, 159, 159))
        );

        add(ImageOptionPanel, java.awt.BorderLayout.WEST);

        ImagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Image Viewer"));
        ImagePanel.setLayout(new java.awt.BorderLayout());

        nextLabel.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        nextLabel.setForeground(new java.awt.Color(0, 51, 204));
        nextLabel.setText("Next");

        backLabel.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        backLabel.setForeground(new java.awt.Color(0, 51, 204));
        backLabel.setText("Back");

        jLabel13.setText("1");

        jLabel14.setText("2");

        jLabel15.setText("3");

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nextLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(backLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 289, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addGap(42, 42, 42))
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextLabel)
                    .addComponent(backLabel)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ImagePanel.add(controlPanel, java.awt.BorderLayout.SOUTH);

        renderPanel.setLayout(new java.awt.BorderLayout());
        ImagePanel.add(renderPanel, java.awt.BorderLayout.CENTER);

        jLabel5.setText("Filter:");

        filterImageTextField.setText(" ");

        loadImageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/main/resources/kview.png"))); // NOI18N
        loadImageButton.setText("Load Images");
        loadImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadImageButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout filterPanelLayout = new javax.swing.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filterImageTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loadImageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, filterPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(filterImageTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadImageButton))
                .addGap(20, 20, 20))
        );

        ImagePanel.add(filterPanel, java.awt.BorderLayout.NORTH);

        add(ImagePanel, java.awt.BorderLayout.CENTER);

        StatusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Image Status"));

        jLabel6.setText("Image Path:");

        imagePathTextField.setText(" ");

        jLabel7.setText("Image Size:");

        imageSizeTextField.setText(" ");

        jLabel8.setText("Last Modification Date:");

        lastModificationTextField.setText(" ");

        jLabel9.setText("Has GeoTagging:");

        hasGeoTaggingTextField.setText(" ");

        jLabel10.setText("Metadata:");

        metadataTextArea.setColumns(20);
        metadataTextArea.setRows(5);
        jScrollPane1.setViewportView(metadataTextArea);

        javax.swing.GroupLayout StatusPanelLayout = new javax.swing.GroupLayout(StatusPanel);
        StatusPanel.setLayout(StatusPanelLayout);
        StatusPanelLayout.setHorizontalGroup(
            StatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StatusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(StatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(imagePathTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imageSizeTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastModificationTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(hasGeoTaggingTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        StatusPanelLayout.setVerticalGroup(
            StatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StatusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(imagePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(imageSizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lastModificationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hasGeoTaggingTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(StatusPanel, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    private void loadImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadImageButtonActionPerformed
        try {
            List<String> images = this.loadItems();
            this.noOfImages = images.size();
            this.displayImages(images.subList(0, IMAGE_PER_PAGE));
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(ImageViewerPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_loadImageButtonActionPerformed

    private void applyOptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyOptionButtonActionPerformed
       // set the new variable after checking it correctenss
        
       // then ask for recomputiong the panel witn these new options
        this.recomputeTheImagePages();
    }//GEN-LAST:event_applyOptionButtonActionPerformed

    private void recomputeTheImagePages() {
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ImageOptionPanel;
    private javax.swing.JPanel ImagePanel;
    private javax.swing.JPanel StatusPanel;
    private javax.swing.JButton applyOptionButton;
    private javax.swing.JLabel backLabel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JTextField filterImageTextField;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JTextField hasGeoTaggingTextField;
    private javax.swing.JTextField imagePathTextField;
    private javax.swing.JTextField imageSizeTextField;
    private javax.swing.JTextField imagerPerPageTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField lastModificationTextField;
    private javax.swing.JButton loadImageButton;
    private javax.swing.JTextArea metadataTextArea;
    private javax.swing.JLabel nextLabel;
    private javax.swing.JTextField padTextField;
    private javax.swing.JPanel renderPanel;
    private javax.swing.JTextField rowsTextField;
    private javax.swing.JTextField scaleTextField;
    // End of variables declaration//GEN-END:variables
}
