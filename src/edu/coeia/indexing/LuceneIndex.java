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

public final class LuceneIndex {

    private IndexWriter writer ;
    private Case caseObject; 
    
    /*
     * Static Factory Method 
     * Create New Instance of Lucene Indexer
     */
    public static LuceneIndex newInstance(Case aCase) throws IOException{ 
        return new LuceneIndex(aCase);
    }
    
    // add index to exists index folder if boolean value is false
    private LuceneIndex (Case aCase) throws IOException {
        File indexDir = new File(aCase.getCaseLocation() + "\\" +  FilesPath.INDEX_PATH);
        
        if ( !indexDir.exists() ) {
            throw new IOException("not found indexing folder");
        }

	caseObject = aCase; 
      
        // using stop analyzer
        writer = new IndexWriter(FSDirectory.open(indexDir), new StopAnalyzer(Version.LUCENE_20, new File(FilesPath.STOP_WORD_FILE)),
                true, IndexWriter.MaxFieldLength.UNLIMITED);

	writer.setUseCompoundFile(false);
    }

    public Case getCase () { return this.caseObject ; }
    public IndexWriter getWriter () { return this.writer ; }
    
    public int getIndexNumber () throws IOException {
        int numIndexed = writer.numDocs();
        return numIndexed ;
    }

    public void closeIndex () throws IOException {
        writer.optimize();
	writer.close();
    }
    
    public boolean indexFile(File file)  
            throws IOException, FileNotFoundException, PSTException, TikaException {
        return indexFile(file, 0);
    }
    
    public boolean indexFile(File file, int parentId)  
            throws IOException, FileNotFoundException, PSTException, TikaException {
   
        try {
            Indexer indexType = IndexerFactory.getIndexer(this, file, parentId);
            return indexType.doIndexing();
        }
        catch(UnsupportedOperationException e){
            System.out.println(e.getMessage());
        }
        
        return false;
    }
    
    public boolean indexDir(File dir) 
            throws IOException, FileNotFoundException, PSTException, TikaException {

        try {
            Indexer indexType = IndexerFactory.getFolderIndexer(this, dir);
            return indexType.doIndexing();
        }
        catch(UnsupportedOperationException e){
            System.out.println(e.getMessage());
        }
        
        return false;
    }
}
