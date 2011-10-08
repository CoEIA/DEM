package edu.coeia.indexing;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wajdyessam
 */


import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.extractor.ContainerExtractor;
import org.apache.tika.extractor.ParserContainerExtractor;
import org.apache.tika.extractor.EmbeddedResourceHandler;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.mime.MediaType;

import java.io.File ;
import java.io.OutputStream ;
import java.io.FileOutputStream; 
import java.io.IOException ;

public class TikaImageExtractor {
    private String filename ;
    private String destination;

    public static TikaImageExtractor getExtractor(String file, String destination) {
        return new TikaImageExtractor(file, destination);
    }

    public EmbeddedObjectHandler extract () {
        EmbeddedObjectHandler handler = new EmbeddedObjectHandler(destination);
        
        try {
            ContainerExtractor extractor = new ParserContainerExtractor();
            handler =  process(filename, extractor, true);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        return handler ;
    }
    
    private TikaImageExtractor(String file, String destination) {
        this.filename = file;
        this.destination = destination;
    }
    
    private EmbeddedObjectHandler process(String filename, ContainerExtractor extractor, boolean recurse) throws Exception {
        TikaInputStream stream = getTikaInputStream(filename);
        //System.out.println("is supported: " + extractor.isSupported(stream));
        
        try {
            // Process it
            EmbeddedObjectHandler handler = new EmbeddedObjectHandler(this.destination);
            
            if(recurse) {
                extractor.extract(stream, extractor, handler);
            } else {
                extractor.extract(stream, null, handler);
            }

            // So they can check what happened
            return handler;
        } finally {
            stream.close();
        }
    }
    
    private TikaInputStream getTikaInputStream(String filename) throws Exception {
        return TikaInputStream.get(new File(filename));
    }
    
    public static class EmbeddedObjectHandler implements EmbeddedResourceHandler {
        public List<String> filenames = new ArrayList<String>();
        public List<MediaType> mediaTypes = new ArrayList<MediaType>();

        private String destination ;
        
        public EmbeddedObjectHandler() { }
        
        public EmbeddedObjectHandler(String destination) {
            this.destination = destination;
        }
        
        @Override
        public void handle(String filename, MediaType mediaType, InputStream stream) {
            filenames.add(filename);
            mediaTypes.add(mediaType);

            //System.out.println("handle file: " + filename);
            handleImage(stream, filename, mediaType.toString());
        }
        
        private void handleImage(InputStream stream, String filename, String type) {
            count++;
            
            long value =  new java.util.Date().getTime();
            
            // Give it a sensible name if needed
            if(filename == null) {
                filename = "image-" + value + ".";
                filename += type.substring(type.indexOf('/')+1);
            }
            else {
                filename = "image-" + value + ".";
                filename += type.substring(type.indexOf('/')+1);
            }
            
            // Save the image
            extractEmbbeddedImage(stream, filename, type);
        }
    
        private void extractEmbbeddedImage(InputStream stream, String filename, String type) {
            try {
                String filePath = this.destination + "\\" + filename;
                File file = new File(filePath);
                //System.out.println("saved into: " + file.getAbsolutePath());
                
                OutputStream outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int length = 0;

                while ( (length = stream.read(buffer)) > 0 ) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.close();
            }
            catch (IOException e) {
            }
        }
    
        private static int count = 0 ;
    }
}
