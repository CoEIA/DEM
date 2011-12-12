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
    
    public List<String> getImagesPath () throws IOException {
        List<String> aList = new ArrayList<String>();

        TermEnum te = indexReader.terms(new Term("mime","") );
        while ( te.next() ) {
            Term currentTerm = te.term();

            if ( ! currentTerm.field().equals("mime"))
                continue ;

            String termText = currentTerm.text();
            aList.add(termText);
        }

        te.close();
        return (aList);
    }

    public void close () throws IOException {
        indexReader.close();
    }
}
