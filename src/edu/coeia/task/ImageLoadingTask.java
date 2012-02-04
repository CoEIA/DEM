/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.multimedia.ImageViewerPanel;
import edu.coeia.cases.Case;
import edu.coeia.cases.CaseFacade;
import edu.coeia.gutil.ImageLabel;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.multimedia.GeoTagging;
import edu.coeia.multimedia.GeoTagging.GPSData;
import edu.coeia.util.FilesPath;
import edu.coeia.util.Tuple;
import edu.coeia.util.Utilities;

import edu.coeia.viewer.SearchResultParamter;
import edu.coeia.viewer.SourceViewerDialog;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

import java.io.File;
import java.io.IOException;

import java.net.URI;

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
public class ImageLoadingTask implements Task{
    private final TaskThread thread;
    private final Case aCase;
    private final ImageViewerPanel panel;
    private final CaseFacade caseFacade; 
    
    public ImageLoadingTask(final CaseFacade caseFacade, final ImageViewerPanel panel) {
        this.thread = new TaskThread(this);
        this.caseFacade = caseFacade;
        this.aCase = this.caseFacade.getCase();
        this.panel = panel;
    }
    
    @Override
    public void startTask() {
        this.thread.execute();
    }
    
    @Override
    public void doTask() throws Exception {
        if ( !this.panel.isImageSizeIsComputed()) {
            int total = this.getNumberOfImages();
            this.panel.setTotalNumberOfImages(total);
            this.panel.setImageSizeFlag();
            this.panel.computeNumberOfPages();
        }
        
        this.displayImages(this.loadItems(this.panel.getCurrentImageNo(), this.panel.getImagePerPage()));
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.thread.isCancelledThread();
    }
    
    private void displayImages(final List<Tuple<String, Integer>> images) throws IOException {
        this.panel.getRenderPanel().removeAll();
        this.panel.checkImageControllingButtons();
        
        List<ImageViewerPanel.ImageIconWithDocumentId> icons = new ArrayList<ImageViewerPanel.ImageIconWithDocumentId>();
        List<Integer> ids = new ArrayList<Integer>();
        
        for(Tuple<String, Integer> document: images) {
            try {
                String image = document.getA();
                int id = document.getB();
                
                File imageFile = new File(image);
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                BufferedImage scaledImage = createThumbnail(bufferedImage);
                ImageViewerPanel.ImageIconWithDocumentId icon = new ImageViewerPanel.ImageIconWithDocumentId(scaledImage, imageFile.getName(), id);
                icons.add(icon);
                
                ids.add(id);
            }
            catch(Exception e) {
                System.out.println(document.getA() + " cannot be veweing");
            }
        }
        
        panel.setIds(ids);
        
        final ImageViewerPanel.FilteredList  list = new ImageViewerPanel.FilteredList(this.panel.getFilterTextField());
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setCellRenderer(new ImageViewerPanel.MyCellRenderer());
        list.setVisibleRowCount(this.panel.getRowsNumber());
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.addListSelectionListener(new ListSelectionListener() { 
            @Override
            public void valueChanged(final ListSelectionEvent event) {                
                ImageViewerPanel.ImageIconWithDocumentId document = (ImageViewerPanel.ImageIconWithDocumentId) list.getSelectedValue();
                panel.setStatusInformation(document);
            }
        });
        
        for(ImageViewerPanel.ImageIconWithDocumentId icon: icons) {
            list.addItem(icon);
        }
        
        list.update();
        
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if ( event.getClickCount() == 2 ) {
                    ImageViewerPanel.ImageIconWithDocumentId document = (ImageViewerPanel.ImageIconWithDocumentId) list.getSelectedValue();
                    int id = document.getId();
                    SearchResultParamter searchResultParamter = new SearchResultParamter("", id, panel.getIds());
                    SourceViewerDialog dialog = new SourceViewerDialog(panel.getCaseFrame(), true, searchResultParamter);
                    dialog.setVisible(true);
                }
            }
        });
        
        JScrollPane pane = new JScrollPane (list, 
             ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
             ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.panel.getRenderPanel().add(pane, BorderLayout.CENTER);
    }
        
    private List<Tuple<String, Integer> > loadItems(int from, int size) throws IOException {
        List<Tuple<String, Integer> > files = new ArrayList<Tuple<String, Integer> >();
        
        String indexDir = this.aCase.getCaseLocation() + File.separator + FilesPath.INDEX_PATH;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        
        int counter = 0;
        
        for (int i=0; i < indexReader.maxDoc(); i++) {
            if ( this.isCancelledTask() )
                return files;
            
            Document document = indexReader.document(i);
            
            if (document != null) {
                Field field = document.getField(IndexingConstant.FILE_MIME);
                
                if (field != null && field.stringValue() != null) {
                    String fullpath = "";
                    int id = Integer.parseInt(document.get(IndexingConstant.DOCUMENT_ID));
                    
                    if (IndexingConstant.isImageDocument(document)) {
                        String path = document.get(IndexingConstant.FILE_PATH);
                        if ( path.contains(this.aCase.getCaseName() + File.separator + FilesPath.CASE_ARCHIVE_EXTRACTION) ) 
                            fullpath = path;
                        else
                            fullpath = this.caseFacade.getFullPath(document.get(IndexingConstant.FILE_PATH));
                    }
                    
                    if ( !fullpath.isEmpty()) {
                        counter++;
                        
                        if ( files.size() >= size) 
                            break;
                        
                        if ( counter >= from )
                            files.add(new Tuple<String, Integer>(fullpath, Integer.valueOf(id)));
                    }
                }
            }
        }
        
        indexReader.close();
        return files;
    }
    
    private int getNumberOfImages() throws IOException {
        int counter = 0;
        String indexDir = this.aCase.getCaseLocation() + File.separator + FilesPath.INDEX_PATH;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
                            
        for (int i = 0; i < indexReader.maxDoc(); i++) {
            Document document = indexReader.document(i);
            
            if (document != null) {
                Field field = document.getField(IndexingConstant.FILE_MIME);
                
                if (field != null && field.stringValue() != null) {
                    String fullpath = "";
                    
                    if ( IndexingConstant.isImageDocument(document) ) {
                        String path = document.get(IndexingConstant.FILE_PATH);
                        if ( path.contains(this.aCase.getCaseName() + File.separator + FilesPath.CASE_ARCHIVE_EXTRACTION) ) 
                            fullpath = path;
                        else
                            fullpath = this.caseFacade.getFullPath(document.get(IndexingConstant.FILE_PATH));
                    }
                    
                    if ( ! fullpath.isEmpty() ) {
                        counter++;
                    }
                }
            }
        }
        
        indexReader.close();
        return counter;
    }
        
    private BufferedImage createThumbnail(BufferedImage img) {
        img = Scalr.resize(img, this.panel.getScaleFactor());
        return pad(img, this.panel.getPadFactor());
    }
    
    private void showPopupOpen (MouseEvent event) {
        final ImageLabel lbl = (ImageLabel) event.getSource();
        JPopupMenu popup = new JPopupMenu();
        JButton selectBtn = new JButton("Select  Image Location in PC");
        JButton openBtn   = new JButton("Open Image With Image Viewer");

        selectBtn.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed (java.awt.event.ActionEvent event) {
                try {
                    Utilities.selectObjectInExplorer(lbl.getPath());
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
        catch (Exception e) {e.printStackTrace();}


        lbl.setComponentPopupMenu(popup);
    }
}
