

package edu.coeia.gui.images;


import edu.coeia.cases.Case;
import edu.coeia.image.ImageViewer;

import edu.coeia.utility.GPSData;
import edu.coeia.utility.Utilities;
import edu.coeia.utility.GeoTagging;
import edu.coeia.utility.ImageLabel;

import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.border.TitledBorder;
import javax.swing.JOptionPane ;
import javax.swing.ImageIcon ;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage ;
import java.awt.Graphics2D ;
import java.awt.AlphaComposite ;
import java.awt.Image ;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date ;

import java.awt.Desktop ;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;

import java.io.File ;
import java.io.IOException ;

import java.net.URI ;


/*
 * ImagesViewerPanel.java
 *
 * @author wajdyessam
 * 
 * Created on Aug 10, 2011, 4:21:09 PM
 */

public class ImagesViewerPanel extends javax.swing.JPanel {

    private Case index ;
    
    private int imageIndex  = 0;
    private int totalImagePage, currentImagePage ;
    
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    
    /** Creates new form ImagesViewerPanel */
    public ImagesViewerPanel(Case aIndex) {
        initComponents();
        this.index = aIndex;
        disableNotIndexedComponent();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ImageDisplayPanel = new javax.swing.JPanel();
        ImageControlPanel = new javax.swing.JPanel();
        showImagesButton = new javax.swing.JButton();
        nextPageButton = new javax.swing.JButton();
        prePageButton = new javax.swing.JButton();
        ImageStatusPanel = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        imagePathLabel = new javax.swing.JLabel();
        imageSizeLabel = new javax.swing.JLabel();
        imageDateLabel = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        geoTagLbl = new javax.swing.JLabel();

        ImageDisplayPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Images Found", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        ImageDisplayPanel.setLayout(new java.awt.GridLayout(4, 4));

        ImageControlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Images Viewer", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        showImagesButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        showImagesButton.setText("Show Images");
        showImagesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showImagesButtonActionPerformed(evt);
            }
        });

        nextPageButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        nextPageButton.setText("Next Page");
        nextPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextPageButtonActionPerformed(evt);
            }
        });

        prePageButton.setFont(new java.awt.Font("Tahoma", 1, 11));
        prePageButton.setText("Previous Page");
        prePageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prePageButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ImageControlPanelLayout = new javax.swing.GroupLayout(ImageControlPanel);
        ImageControlPanel.setLayout(ImageControlPanelLayout);
        ImageControlPanelLayout.setHorizontalGroup(
            ImageControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ImageControlPanelLayout.createSequentialGroup()
                .addGap(589, 589, 589)
                .addComponent(showImagesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
                .addComponent(nextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prePageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        ImageControlPanelLayout.setVerticalGroup(
            ImageControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ImageControlPanelLayout.createSequentialGroup()
                .addGroup(ImageControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(prePageButton, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(nextPageButton, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(showImagesButton, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addContainerGap())
        );

        ImageStatusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Image Information"));

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel32.setForeground(new java.awt.Color(0, 0, 204));
        jLabel32.setText("Image Path:");

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel34.setForeground(new java.awt.Color(0, 0, 153));
        jLabel34.setText("Image Size:");

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel35.setForeground(new java.awt.Color(0, 0, 153));
        jLabel35.setText("Image Modified Date:");

        imagePathLabel.setText(" ");

        imageSizeLabel.setText(" ");

        imageDateLabel.setText(" ");

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel38.setForeground(new java.awt.Color(0, 0, 153));
        jLabel38.setText("GeoTagging");

        geoTagLbl.setText(" ");

        javax.swing.GroupLayout ImageStatusPanelLayout = new javax.swing.GroupLayout(ImageStatusPanel);
        ImageStatusPanel.setLayout(ImageStatusPanelLayout);
        ImageStatusPanelLayout.setHorizontalGroup(
            ImageStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ImageStatusPanelLayout.createSequentialGroup()
                .addGroup(ImageStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel35)
                    .addComponent(jLabel32)
                    .addComponent(jLabel34))
                .addGap(18, 18, 18)
                .addGroup(ImageStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(imageDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(imagePathLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ImageStatusPanelLayout.createSequentialGroup()
                        .addComponent(imageSizeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel38)
                        .addGap(18, 18, 18)
                        .addComponent(geoTagLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        ImageStatusPanelLayout.setVerticalGroup(
            ImageStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ImageStatusPanelLayout.createSequentialGroup()
                .addGroup(ImageStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imagePathLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ImageStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(imageSizeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel38)
                    .addComponent(geoTagLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ImageStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(imageDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 738, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(ImageDisplayPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
                        .addComponent(ImageControlPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ImageStatusPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 361, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(ImageControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ImageDisplayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(ImageStatusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

private void showImagesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showImagesButtonActionPerformed
        try {
            if ( index.getIndexStatus() == false ) {
                JOptionPane.showMessageDialog(this, "please do the indexing operation first before do any operation",
                    "Case is not indexed",JOptionPane.ERROR_MESSAGE );
                return ;
            }

            imageIndex = 0;
            totalImagePage = getNumberOfImages() / 16 ;
            currentImagePage = 0 ;
            
            showImages();
        }
        catch (IOException e){
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
}//GEN-LAST:event_showImagesButtonActionPerformed

private void nextPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextPageButtonActionPerformed
        try {
            if ( index.getIndexStatus() == false ) {
                JOptionPane.showMessageDialog(this, "please do the indexing operation first before do any operation",
                    "Case is not indexed",JOptionPane.ERROR_MESSAGE );
                return ;
            }

            if (  imageIndex != ImageViewer.getInstance(index).size() ) {
                imageIndex += 16;
            }

            if (currentImagePage < totalImagePage ) {
                currentImagePage++;
            }
            
            showImages();
        }
        catch (IOException e){
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
}//GEN-LAST:event_nextPageButtonActionPerformed

private void prePageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prePageButtonActionPerformed
        try {
            if ( index.getIndexStatus() == false ) {
                JOptionPane.showMessageDialog(this, "please do the indexing operation first before do any operation",
                    "Case is not indexed",JOptionPane.ERROR_MESSAGE );
                return ;
            }

            if ( imageIndex != 0 ) {
                imageIndex -= 16 ;
            }

            if ( currentImagePage > 0 )
                currentImagePage-- ;
            
            showImages();
        }
        catch (IOException e){
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
}
        
    private void showImages () throws IOException {
        ImageDisplayPanel.removeAll();
        checkImageViewerButtons();
        
        for(int i=imageIndex; i<imageIndex + (4*4) ; i++ ) {
            try {
                String name = ImageViewer.getInstance(index).get(i);
                File imageFile = new File(name);
                BufferedImage myPicture = ImageIO.read(imageFile);
                BufferedImage newPictue = createResizedCopy(myPicture,30,30,false);

                ImageLabel picLabel = new ImageLabel( name, new ImageIcon( newPictue ));
                picLabel.addMouseListener( new MouseAdapter() {
                    @Override
                    public void mouseEntered (java.awt.event.MouseEvent event) {
                        ImageLabel lbl = (ImageLabel) event.getSource();
                        setImageInformation(lbl.getPath());
                    }

                    @Override
                    public void mousePressed (java.awt.event.MouseEvent event){
                        if ( (event.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK ) != 0 ) {
                            showPopupOpen(event);
                        }
                    }
                });
                ImageDisplayPanel.add(picLabel);
            }
            catch (IndexOutOfBoundsException e) {logger.log(Level.SEVERE, "Uncaught exception", e); }
        }

        // change border title name
        ( (TitledBorder) ImageDisplayPanel.getBorder() ).setTitle("Images Found Page(" + currentImagePage + "/" +
                totalImagePage + ")");

        ImageDisplayPanel.repaint();
        ImageDisplayPanel.revalidate();
    }

    private void showPopupOpen (MouseEvent event) {
        final ImageLabel lbl = (ImageLabel) event.getSource();
        JPopupMenu popup = new JPopupMenu();
        JButton selectBtn = new JButton("Select  Image Location in PC");
        JButton openBtn   = new JButton("Open Image With Image Viewer");

        selectBtn.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed (java.awt.event.ActionEvent event) {
                try {
                    selectImage(lbl.getPath());
                }
                catch (Exception e){
                }
            }
        });
        
        openBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    Desktop desktop = null ;
                    
                    if (Desktop.isDesktopSupported()) {
                        desktop = Desktop.getDesktop();

                        if ( desktop.isSupported(Desktop.Action.OPEN) ) {
                            desktop.open(new File(lbl.getPath()));
                        }
                    }
                }
                catch (Exception e){
                }
            }
        });
        
        JButton mapButton = null;

         try {
            if ( GeoTagging.hasGoeTag(lbl.getPath()) ) {
                mapButton  = new JButton("Show GPS  With Google  Map");
                mapButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        try {
                            Desktop desktop = null;

                            if (Desktop.isDesktopSupported()) {
                                desktop = Desktop.getDesktop();

                                if ( desktop.isSupported(Desktop.Action.BROWSE) ) {
                                    GPSData data =  GeoTagging.getGPS(lbl.getPath());
                                    String url = "http://maps.google.com/maps?q=" + data.location ;
                                    URI uri = new URI(url);
                                    desktop.browse(uri);
                                }
                            }
                        }
                        catch (Exception e){
                        }
                    }
                });
            }
        }
        catch (Exception e) {}
        
        popup.add(selectBtn);
        popup.add(openBtn);

         try {
            if ( GeoTagging.hasGoeTag(lbl.getPath()) && mapButton != null )
                popup.add(mapButton);
        }
        catch (Exception e) {}


        lbl.setComponentPopupMenu(popup);
    }
        
    private static void selectImage (String path) throws Exception{
        Runtime rt = Runtime.getRuntime();
        rt.exec("explorer /select," + path);
    }

        
    private void setImageInformation (String path) {
        File file = new File(path);

        imagePathLabel.setText( file.getAbsolutePath());
        imageSizeLabel.setText( Utilities.toKB(file.length()) + " KB" );
        imageDateLabel.setText( new Date(file.lastModified()) + "");

        try {
            if ( GeoTagging.hasGoeTag(path) )
                geoTagLbl.setText("Yes");
            else
                geoTagLbl.setText("No");
        }
        catch (Exception e) {logger.log(Level.SEVERE, "Uncaught exception", e);}
        
    }

    private BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
                g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();

        return scaledBI;
    }
    
    private int getNumberOfImages () {
        int count = 0 ;
        
        try {
            for(int i=imageIndex;  ; i++ ) {
                ImageViewer.getInstance(index).get(i);
                count++;
            }
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }

        return count ;
    }

    private void checkImageViewerButtons () {
        if ( currentImagePage == 0 ) {
            nextPageButton.setEnabled(true);
            prePageButton.setEnabled(false);
        }
        else if ( currentImagePage == totalImagePage ) {
            nextPageButton.setEnabled(false);
            prePageButton.setEnabled(true);
        }
        else {
            nextPageButton.setEnabled(true);
            prePageButton.setEnabled(true);
        }
}//GEN-LAST:event_prePageButtonActionPerformed

    private void disableNotIndexedComponent () {
        if (index.getCacheImages() == false ) {
            showImagesButton.setEnabled(false);
            nextPageButton.setEnabled(false);
            prePageButton.setEnabled(false);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ImageControlPanel;
    private javax.swing.JPanel ImageDisplayPanel;
    private javax.swing.JPanel ImageStatusPanel;
    private javax.swing.JLabel geoTagLbl;
    private javax.swing.JLabel imageDateLabel;
    private javax.swing.JLabel imagePathLabel;
    private javax.swing.JLabel imageSizeLabel;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JButton nextPageButton;
    private javax.swing.JButton prePageButton;
    private javax.swing.JButton showImagesButton;
    // End of variables declaration//GEN-END:variables
}
