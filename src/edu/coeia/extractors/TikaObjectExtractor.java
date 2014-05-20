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
package edu.coeia.extractors ;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.indexing.Indexer;
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
    private final String filename ;
    private final String destination;
    private final OBJECT_TYPE type ;
    private final Indexer indexer;
    /**
     * Type of Object we want to extract objects from it
     * CONTAINER mean that the object is container file (OLE2, OOXML) like DOC, DOCX...
     * and we will extract everything from it
     * 
     * ARCHIVE mean that the object is archive file (ZIP..) and we will extract files from it
     */
    public static enum OBJECT_TYPE {CONTAINER, ARCHIVE}
    
    
    public static TikaObjectExtractor newInstance(final Indexer indexer,
            String file, String destination, OBJECT_TYPE type ) {
        return new TikaObjectExtractor(indexer, file, destination, type);
    }
    
    public EmbeddedObjectCollections extract () throws Exception {
        ContainerExtractor extractor = new ParserContainerExtractor();        
        return process(filename, extractor);
    }
        
    private TikaObjectExtractor(final Indexer indexer, String file, String destination, OBJECT_TYPE type) {
        this.filename = file;
        this.destination = destination;
        this.type = type ;
        this.indexer = indexer;
    }
    
    private EmbeddedObjectCollections process(String filename, ContainerExtractor extractor) throws Exception {
        TikaInputStream stream = getTikaInputStream(filename);
        EmbeddedObjectCollections handler = null;
        
        try {
            // Process it
            handler = new EmbeddedObjectCollections(this.destination, this.type);
            
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
    public class ExtractedObjectInfo {
        private final String fileName ;
        private final String type;
        private final String extension ;
        private final String oldFilePath ;
        private final String newFilePath ;
        
        public ExtractedObjectInfo(String fn, String t, String oldF, String newF, String ext) {
            this.fileName = fn;
            this.type = t;
            this.extension = ext;
            this.oldFilePath = oldF; 
            this.newFilePath = newF;
        }
        
        public String getFileName() { return this.fileName ; }
        public String getFileType() { return this.type; }
        public String getFileExtension() { return this.extension; }
        public String getFileOldPath() { return this.oldFilePath; }
        public String getFileNewPath() { return this.newFilePath ; }
        
        @Override
        public String toString() {
            return String.format("ObjectLocation[%s, %s, %s, %s, %s]", fileName, type, 
                    extension, oldFilePath, newFilePath);
        }
    }
    
    public class EmbeddedObjectCollections implements EmbeddedResourceHandler {
        private final String destination ;
        private final List<ExtractedObjectInfo> locations;
        private final OBJECT_TYPE type; 
        
        public EmbeddedObjectCollections(String destination, OBJECT_TYPE type) {
            this.destination = destination;
            this.type = type ;
            this.locations = new ArrayList<ExtractedObjectInfo>();
        }
        
        public List<ExtractedObjectInfo> getLocations() { return Collections.unmodifiableList(this.locations); }
        
        @Override
        public void handle(String filename, MediaType mediaType, InputStream stream) {   
            if ( filename == null)
                return ;
            
            try {
                extractEmbbeddedObject(stream, filename, mediaType.toString(), mediaType.getSubtype());
            } catch (IOException ex) {
                ex.printStackTrace();
                Logger.getLogger(TikaObjectExtractor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
        private void extractEmbbeddedObject(InputStream stream, String originalFilePath, String type, String ext) throws IOException {
            String newFileName = originalFilePath ;

            if (this.type == OBJECT_TYPE.CONTAINER ) { // extract images  from file 
                // exported name will be parent id- documentid
                String value = indexer.getParentId() + "-" + indexer.getId();
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
            ExtractedObjectInfo location = new ExtractedObjectInfo(objectFile.getName(), type, originalFilePath,
                    distenationPath, ext);
            locations.add(location);

            // write object to file
            FileUtil.saveObject(stream, objectFile.getName(), destinationParent.getAbsolutePath());
        }
    }
}
