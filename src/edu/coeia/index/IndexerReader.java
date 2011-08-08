/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.index;

/**
 *
 * @author wajdyessam
 *
 */

import edu.coeia.utility.Utilities;

import org.apache.lucene.index.IndexReader ;
import org.apache.lucene.store.Directory ;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.TermEnum ;
import org.apache.lucene.index.Term ;
import org.apache.lucene.document.Document ;

import java.io.IOException ;
import java.io.File ;

import java.util.HashMap ;
import java.util.List ;
import java.util.ArrayList ;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IndexerReader {
    private String indexDir ;
    private Directory dir ;
    private IndexReader indexReader ;

    private static Logger logger = Logger.getLogger("IndexerReader");
    private static FileHandler handler ;

    public IndexerReader (String location) throws IOException{
        indexDir = location ;
        dir = FSDirectory.open(new File(indexDir));
        indexReader = IndexReader.open(dir);

        try {
            handler = new FileHandler("IndexerReader.log");
            logger.addHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Uncaught exception", ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, "Uncaught exception", ex);
        }
    }
    
//    // get terms and frequncy for all terms in docuemnts
//    public HashMap<String,Integer> getAllTermFreqFromBody ()  throws IOException {
//        HashMap<String,Integer> map = new HashMap<String,Integer>();
//        TermEnum te = indexReader.terms(new Term("body","") );
//
//        while ( te.next() ) {
//            Term currentTerm = te.term();
//
//            if ( ! currentTerm.field().equals("body"))
//                continue ;
//
//            String termText = currentTerm.text();
//            int frequency   = indexReader.docFreq(currentTerm);
//
//            map.put(termText,frequency);
//        }
//
//        te.close();
//        return map ;
//    }

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

//    public HashMap<String,Double> getExtensionFreq () {
//        HashMap<String,Double> map = new HashMap<String,Double>();
//        System.out.println("Number of Doc: " + indexReader.numDocs());
//        logger.log(Level.INFO, "Number of Docs: " + indexReader.numDocs());
//
//        try {
//            for (int i=0 ; i<indexReader.numDocs(); i++) {
//                Document doc = indexReader.document(i);
//                String file = doc.get("filename");
//
//                if ( file == null )
//                    continue ;
//
//                String ext = Utilities.getExtension(file).toLowerCase();
//
//                if ( map.get(ext) == null ){
//                    map.put(ext, 1.0);
//                }
//                else
//                    map.put(ext, map.get(ext) + 1);
//            }
//        }
//        catch (IOException e){
//            logger.log(Level.SEVERE, "Uncaught exception", e);
//        }
//
//        logger.log(Level.INFO, "Map size: " + map.size());
//        return map ;
//    }

    public void close () throws IOException {
        indexReader.close();
    }
}
