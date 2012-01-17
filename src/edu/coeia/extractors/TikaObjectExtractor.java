package edu.coeia.extractors ;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.util.FileUtil;

import java.io.IOException;
import java.io.File ;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tika.extractor.ContainerExtractor;
import org.apache.tika.extractor.ParserContainerExtractor;
import org.apache.tika.extractor.EmbeddedResourceHandler;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.mime.MediaType;

public class TikaObjectExtractor {
    private String filename ;
    private String destination;
    
    /**
     * Type of Object we want to extract objects from it
     * CONTAINER mean that the object is container file (OLE2, OOXML) like DOC, DOCX...
     * and we will extract everything from it
     * 
     * ARCHIVE mean that the object is archive file (ZIP..) and we will extract files from it
     */
    public static enum OBJECT_TYPE {CONTAINER, ARCHIVE}
    
    private OBJECT_TYPE type ;
    
    public static TikaObjectExtractor getExtractor(String file, String destination, OBJECT_TYPE type ) {
        return new TikaObjectExtractor(file, destination, type);
    }
    
    private TikaObjectExtractor(String file, String destination, OBJECT_TYPE type) {
        this.filename = file;
        this.destination = destination;
        this.type = type ;
    }
    
    public EmbeddedObjectHandler extract () {
        EmbeddedObjectHandler handler = null;
        
        try {
            ContainerExtractor extractor = new ParserContainerExtractor();
            handler =  process(filename, extractor);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return handler ;
    }
    
    private EmbeddedObjectHandler process(String filename, ContainerExtractor extractor) throws Exception {
        TikaInputStream stream = getTikaInputStream(filename);
        EmbeddedObjectHandler handler = null;
        
        try {
            // Process it
            handler = new EmbeddedObjectHandler(this.destination, this.type);
            
            if( type == OBJECT_TYPE.CONTAINER) { // recursive extractor to get all items inside document
                extractor.extract(stream, extractor, handler);
            } else if ( type == OBJECT_TYPE.ARCHIVE ) { // extract all object inside document
                extractor.extract(stream, null, handler);
            }
        } finally {
            stream.close();
        }
        
        // handler will contain all information about the extracted object and they path
        return handler;
    }
    
    private TikaInputStream getTikaInputStream(String filename) throws Exception {
        return TikaInputStream.get(new File(filename));
    }
    
    /*
     * Object Location hold information about the file extracted from object
     */
    public static class ObjectLocation {
        public final String fileName ;
        public final String type;
        public final String extension ;
        public final String oldFilePath ;
        public final String newFilePath ;
        
        public ObjectLocation(String fn, String t, String oldF, String newF, String ext) {
            this.fileName = fn;
            this.type = t;
            this.extension = ext;
            this.oldFilePath = oldF; 
            this.newFilePath = newF;
        }
        
        @Override
        public String toString() {
            return String.format("ObjectLocation[%s, %s, %s, %s, %s]", fileName, type, 
                    extension, oldFilePath, newFilePath);
        }
    }
    
    public static class EmbeddedObjectHandler implements EmbeddedResourceHandler {
        private String destination ;
        private List<ObjectLocation> locations = new ArrayList<ObjectLocation>();
        private OBJECT_TYPE type; 
        
        public EmbeddedObjectHandler(String destination, OBJECT_TYPE type) {
            this.destination = destination;
            this.type = type ;
        }
        
        public List<ObjectLocation> getLocations() { return Collections.unmodifiableList(this.locations); }
        
        @Override
        public void handle(String filename, MediaType mediaType, InputStream stream) {   
            // ignore ole file
            if ( filename == null || filename.startsWith("ole-") )
                return ;
            try {
                extractEmbbeddedObject(stream, filename, mediaType.toString(), mediaType.getSubtype());
            } catch (IOException ex) {
                Logger.getLogger(TikaObjectExtractor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
        private void extractEmbbeddedObject(InputStream stream, String originalFilePath, String type, String ext) throws IOException {
            String newFileName = originalFilePath ;

            if (this.type == OBJECT_TYPE.CONTAINER ) { // extract images  from file 
                // exported name will be the time stamp in nanosecond
                long value =  System.nanoTime();
                newFileName = "image-" + value + ".";
                newFileName += type.substring(type.indexOf('/')+1);
            }

            // write object in same directory structures (if its inside archive file)
            // it will create nessacery folders to move the objects
            File objectFile = new File(this.destination + "\\" + newFileName);
            File destinationParent = objectFile.getParentFile();
            destinationParent.mkdirs();
        
            // get distenation path
            String distenationPath = objectFile.getAbsolutePath();

            // create location object
            ObjectLocation location = new ObjectLocation(objectFile.getName(), type, originalFilePath,
                    distenationPath, ext);
            locations.add(location);

            // write object to file
            FileUtil.saveObject(stream, objectFile.getName(), destinationParent.getAbsolutePath());
        }
    }
}
