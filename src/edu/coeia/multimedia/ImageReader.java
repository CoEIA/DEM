/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.multimedia;

/**
 *
 * @author wajdyessam
 *
 */

import edu.coeia.indexing.IndexingConstant;
import org.apache.lucene.index.IndexReader ;
import org.apache.lucene.store.Directory ;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.TermEnum ;
import org.apache.lucene.index.Term ;

import java.io.IOException ;
import java.io.File ;

import java.util.List ;
import java.util.ArrayList ;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class ImageReader {
    private String indexDir ;
    private Directory dir ;
    private IndexReader indexReader ;

    private static final Logger logger = Logger.getLogger(edu.coeia.util.FilesPath.LOG_NAMESPACE);

    public ImageReader (String location) throws IOException{
        indexDir = location ;
        dir = FSDirectory.open(new File(indexDir));
        indexReader = IndexReader.open(dir);
        
        logger.info("ImageReader Constructor");
    }
     public List<String> getImages() throws Exception {
       List<String> aList = new ArrayList<String>();

        for (int i=0; i<indexReader.maxDoc(); i++) {
            Document document = indexReader.document(i);
            if ( document != null ) {
                Field field = document.getField(IndexingConstant.FILE_MIME);
                if ( field != null && field.stringValue() != null) {
                  String documentExtension = field.stringValue();
                  if(documentExtension.equals("jpg"))
                  {
                      String path  = document.get(IndexingConstant.FILE_NAME);
                      aList.add(path);
                  }
                }
            }
        }
         return (aList);  
    }
  
    public void close () throws IOException {
        indexReader.close();
    }
}
