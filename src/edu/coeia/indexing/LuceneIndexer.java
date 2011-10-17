/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 *
 */

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory ;
import org.apache.lucene.util.Version ;

import com.pff.PSTException ;
import edu.coeia.cases.Case;
import edu.coeia.util.FilesPath;

import java.io.FileNotFoundException ;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.StopAnalyzer;

import org.apache.tika.exception.TikaException;

public class LuceneIndexer {

    private static IndexWriter writer ;
    private static Case caseObject; 
    
    /*
     * Static Factory Method 
     * Create New Instance of Lucene Indexer
     */
    public static LuceneIndexer getInstance(Case aCase, boolean createIndex) throws IOException{ 
        return new LuceneIndexer(aCase, createIndex);
    }
    
    // last parameter will create new index folder
    private LuceneIndexer (Case aCase) throws IOException {
       this(aCase, true);
    }

    // add index to exists index folder if boolean value is false
    private LuceneIndexer (Case aCase, boolean newIndex) throws IOException {
        File indexDir = new File(aCase.getIndexLocation() + "\\" +  FilesPath.INDEX_PATH);
        
        if ( !indexDir.exists() ) {
            throw new IOException("not found indexing folder");
        }

	caseObject = aCase; 
      
        // using stop analyzer
        writer = new IndexWriter(FSDirectory.open(indexDir), new StopAnalyzer(Version.LUCENE_20, new File(FilesPath.STOP_WORD_FILE)),
                true, IndexWriter.MaxFieldLength.UNLIMITED);

	writer.setUseCompoundFile(false);
    }

    public int getIndexNumber () throws IOException {
        int numIndexed = writer.numDocs();
        
        return numIndexed ;
    }

    public void closeIndex () throws IOException {
        writer.optimize();
	writer.close();
    }
    
    public static boolean indexFile(File file)  
            throws IOException, FileNotFoundException, PSTException, TikaException {
        return indexFile(file, 0);
    }
    
    public static boolean indexFile(File file, int parentId)  
            throws IOException, FileNotFoundException, PSTException, TikaException {
   
        try {
            
            Indexer indexType = IndexerFactory.getIndexer(writer, file, caseObject.getCacheImages(),
                    caseObject.getIndexLocation(), parentId);
            
            return indexType.doIndexing();
        }
        catch(UnsupportedOperationException e){
            System.out.println(e.getMessage());
        }
        
        return false;
    }
    
    public static boolean indexYahooDir(File path) {

        YahooChatIndexer indexer = YahooChatIndexer.newInstance(writer, path, "", false, 
                caseObject.getIndexLocation(), new NoneImageExtractor());
        
        return indexer.doIndexing();
    }
}
