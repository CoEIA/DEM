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

import edu.coeia.cases.Case;
import edu.coeia.util.FilesPath;

import java.io.File;
import java.io.IOException;

import java.util.List;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory ;
import org.apache.lucene.util.Version ;

public final class LuceneIndex {

    private final IndexWriter writer ;
    private final Case caseObject; 
    
    /*
     * Static Factory Method 
     * Create New Instance of Lucene Indexer
     */
    public static LuceneIndex newInstance(Case aCase) throws IOException{ 
        return new LuceneIndex(aCase);
    }
    
    private LuceneIndex (Case aCase) throws IOException {
        File indexDir = new File(aCase.getCaseLocation() + "\\" +  FilesPath.INDEX_PATH);
        
        if ( !indexDir.exists() ) {
            throw new IOException("not found indexing folder");
        }

	this.caseObject = aCase; 
      
        // using stop analyzer
        this.writer = new IndexWriter(FSDirectory.open(indexDir), new StopAnalyzer(Version.LUCENE_20, 
                    new File(FilesPath.STOP_WORD_FILE)),
                    true, IndexWriter.MaxFieldLength.UNLIMITED);

	this.writer.setUseCompoundFile(false);
    }

    void writeEvidenceLocation(final List<String> paths) throws CorruptIndexException,
            IOException {
        Document document = new Document();
        
        for (String path: paths)
            document.add( new Field(IndexingConstant.EVIDENCE_PATH, path, 
                    Field.Store.YES, Field.Index.NO));
        
        this.writer.addDocument(document);
    }
    
    Case getCase () { return this.caseObject ; }
    IndexWriter getWriter () { return this.writer ; }
    
    int getIndexNumber () throws IOException {
        int numIndexed = writer.numDocs();
        return numIndexed ;
    }

    void closeIndex () throws IOException {
        writer.optimize();
	writer.close();
    }
    
    public boolean indexFile(File file) throws UnsupportedOperationException{
        return indexFile(file, 0);
    }
    
    public boolean indexFile(File file, int parentId) throws UnsupportedOperationException{ 
        try {
            Indexer indexType = IndexerFactory.getIndexer(this, file, parentId);
            
            if ( indexType == null )
                return false;
            
            return indexType.doIndexing();
        }
        catch(NullPointerException e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
        catch(UnsupportedOperationException e){
            throw new UnsupportedOperationException(e.getMessage());
        }
    }
    
    public boolean indexDir(File dir){
        boolean status = false;
        
        try {
            Indexer indexType = IndexerFactory.getFolderIndexer(this, dir);
            status = indexType.doIndexing();
        }
        catch(UnsupportedOperationException e){
            //System.out.println(e.getMessage());
            //e.printStackTrace();
        }
        
        return status;
    }
}
