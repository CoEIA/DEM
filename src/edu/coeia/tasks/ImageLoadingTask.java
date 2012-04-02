/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tasks;

import edu.coeia.multimedia.ImageViewerPanel;
import edu.coeia.cases.Case;
import edu.coeia.cases.CaseFacade;
import edu.coeia.gutil.ImageLabel;
import edu.coeia.constants.IndexingConstant;
import edu.coeia.multimedia.GeoTagging;
import edu.coeia.multimedia.GeoTagging.GPSData;
import edu.coeia.constants.ApplicationConstants;
import edu.coeia.util.Utilities;
import edu.coeia.viewer.SearchResultParamter;
import edu.coeia.viewer.SourceViewerDialog;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;

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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.store.Directory ;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;

import static org.imgscalr.Scalr.*;
import org.imgscalr.Scalr;

/**
 *
 * @author wajdyessam
 */
public class ImageLoadingTask implements Task{
    private final BackgroundProgressDialog dialog ;
    private final Case aCase;
    private final ImageViewerPanel panel;
    private final CaseFacade caseFacade; 
    
    public ImageLoadingTask(final CaseFacade caseFacade, final ImageViewerPanel panel) {
        this.dialog = new BackgroundProgressDialog(null, true, this);
        this.caseFacade = caseFacade;
        this.aCase = this.caseFacade.getCase();
        this.panel = panel;
    }
    
    @Override
    public void startTask() {
        this.dialog.startThread();
    }
    
    @Override
    public void doTask() throws Exception {
        if ( !this.panel.isImageSizeIsComputed()) {
            int total = this.getNumberOfImagesFast();
            this.panel.setTotalNumberOfImages(total);
            this.panel.setImageSizeFlag();
            this.panel.computeNumberOfPages();
        }

        Set<ImagePathAndId> items = this.loadItemsFast(this.panel.getCurrentImageNo(),this.panel.getImagePerPage());
        this.displayImages(items);
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.dialog.isCancelledThread();
    }
    
    private void displayImages(final Set<ImagePathAndId> images) throws IOException {
        this.panel.getRenderPanel().removeAll();
        this.panel.checkImageControllingButtons();
        
        List<ImageViewerPanel.ImageIconWithDocumentId> icons = new ArrayList<ImageViewerPanel.ImageIconWithDocumentId>();
        List<Integer> ids = new ArrayList<Integer>();
        
        final ImageViewerPanel.FilteredList list = new ImageViewerPanel.FilteredList(this.panel.getFilterTextField());
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
        
        for(ImagePathAndId document: images) {
            try {
                String image = document.imagePath;
                int id = document.imageId;
                
                File imageFile = new File(image);
                BufferedImage bufferedImage = ImageIO.read(imageFile);
                BufferedImage scaledImage = createThumbnail(bufferedImage);
                ImageViewerPanel.ImageIconWithDocumentId icon =
                        new ImageViewerPanel.ImageIconWithDocumentId(scaledImage, imageFile.getName(), id);
                icons.add(icon);
                list.addItem(icon);
                ids.add(id);
            }
            catch(Exception e) {
                System.out.println(document.imagePath + " cannot be veweing");
            }
        }
        
        panel.setIds(ids);
        list.update();
        
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if ( event.getClickCount() == 2 ) {
                    ImageViewerPanel.ImageIconWithDocumentId document = (ImageViewerPanel.ImageIconWithDocumentId) list.getSelectedValue();
                    if ( document == null ) 
                        return ;
                    int id = document.getId();
                    SearchResultParamter searchResultParamter = new SearchResultParamter("", id, panel.getIds());
                    SourceViewerDialog dialog = new SourceViewerDialog(panel.getCaseFrame(), true, searchResultParamter);
                    dialog.setVisible(true);
                }
            }
        });
        
        EventQueue.invokeLater(new Runnable() { 
            @Override
            public void run() {
                JScrollPane pane = new JScrollPane (list, 
                     ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
                     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                panel.getRenderPanel().add(pane, BorderLayout.CENTER);
            }
        });
    }
    
    private class ImagePathAndId {
        final String imagePath;
        final int imageId;

        public ImagePathAndId(final String path, final int id) {
            this.imagePath = path;
            this.imageId = id;
        }
        
        @Override
        public boolean equals(Object otherObject) {
            if ( this == otherObject )
                return true;
            
            if ( !(otherObject instanceof ImagePathAndId) )
                return false;
            
            ImagePathAndId other = (ImagePathAndId) otherObject;
            return other.imagePath.equals(this.imagePath);
        }
        
        @Override
        public int hashCode() {
            return 7 * this.imagePath.hashCode();
        }
    }
    
    private Set<ImagePathAndId> loadItemsFast(int from, int size) throws IOException {
        Set<ImagePathAndId> files = new HashSet<ImagePathAndId>();
        int counter = 0;
        
        try {
            Directory directory = FSDirectory.open(new File(this.caseFacade.getCaseIndexFolderLocation()));   
            IndexSearcher searcher = new IndexSearcher(directory);
            QueryParser parser = new QueryParser(Version.LUCENE_30, IndexingConstant.DOCUMENT_TYPE, new StopAnalyzer(Version.LUCENE_30));     
            parser.setAllowLeadingWildcard(true);
            Query query = parser.parse(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_GENERAL_TYPE.IMAGE));
            
            TopDocs topDocs = searcher.search(query, 500000);

            for(ScoreDoc scoreDoc: topDocs.scoreDocs) {
                Document document = searcher.doc(scoreDoc.doc);
                String imageExtension = document.get(IndexingConstant.FILE_MIME);
                
                if ( imageExtension != null && !imageExtension.trim().isEmpty() &&
                        Arrays.asList(imageExtensions).contains(imageExtension ) ) {
                    
                    String fullpath = "";
                    int id = Integer.parseInt(document.get(IndexingConstant.DOCUMENT_ID));
                    
                    if ( IndexingConstant.isImageDocument(document) ) {
                        String path = document.get(IndexingConstant.FILE_PATH);
                        
                        if ( path.contains(this.aCase.getCaseName() + File.separator + ApplicationConstants.CASE_ARCHIVE_FOLDER) ) 
                            fullpath = path;
                        else
                            fullpath = this.caseFacade.getFullPath(document.get(IndexingConstant.FILE_PATH));
                    }
                    
                    if ( ! fullpath.isEmpty() ) {
                        counter++;
                        
                        if ( files.size() >= size) 
                            break;
                        
                        if ( counter >= from ) {
                            files.add(new ImagePathAndId(fullpath, Integer.valueOf(id)));
                        }
                    }
                }
            }
            
            searcher.close();
        } catch (ParseException ex) {
            Logger.getLogger(ChatRefreshTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return files;
    }
        
    private int getNumberOfImagesFast() throws IOException {
        int numberOfImages = 0;
        
        try {
            Directory directory = FSDirectory.open(new File(
                this.caseFacade.getCaseIndexFolderLocation()
            ));
            
            IndexSearcher searcher = new IndexSearcher(directory);
            QueryParser parser = new QueryParser(Version.LUCENE_30, 
                    IndexingConstant.DOCUMENT_TYPE, new StopAnalyzer(Version.LUCENE_30));
            parser.setAllowLeadingWildcard(true);
            Query query = parser.parse(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_GENERAL_TYPE.IMAGE));
            
            TopDocs topDocs = searcher.search(query, 500000);

            for(ScoreDoc scoreDoc: topDocs.scoreDocs) {
                Document document = searcher.doc(scoreDoc.doc);
                String imageExtension = document.get(IndexingConstant.FILE_MIME);
                
                if ( imageExtension != null && !imageExtension.trim().isEmpty() &&
                        Arrays.asList(imageExtensions).contains(imageExtension )  ) {
                    String fullpath = "";
                    
                    if ( IndexingConstant.isImageDocument(document) ) {
                        String path = document.get(IndexingConstant.FILE_PATH);
                        if ( path.contains(this.aCase.getCaseName() + File.separator + ApplicationConstants.CASE_ARCHIVE_FOLDER) ) 
                            fullpath = path;
                        else
                            fullpath = this.caseFacade.getFullPath(document.get(IndexingConstant.FILE_PATH));
                    }
                    
                    if ( ! fullpath.isEmpty() ) {
                        numberOfImages++;
                    }
                }
            }
            
            searcher.close();
        } catch (ParseException ex) {
            Logger.getLogger(ChatRefreshTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return numberOfImages;
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
    
    private final static String[] imageExtensions = {
        "jpg", "jpeg", "bmp", "gif", "png", 
        "JPG", "JPEG", "BMP", "GIF", "PNG" 
    };
}
