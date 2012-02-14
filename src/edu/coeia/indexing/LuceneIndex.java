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

import edu.coeia.constants.IndexingConstant;
import edu.coeia.indexing.dialogs.IndexingDialog;
import edu.coeia.cases.CaseFacade;
import edu.coeia.constants.ApplicationConstants;

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
    private final CaseFacade caseFacade;
    
    /*
     * Static Factory Method 
     * Create New Instance of Lucene Indexer
     */
    public static LuceneIndex newInstance(final CaseFacade caseFacade) throws IOException{ 
        return new LuceneIndex(caseFacade);
    }
    
    private LuceneIndex (final CaseFacade caseFacade) throws IOException {
        File indexDir = new File(caseFacade.getCaseIndexFolderLocation());
        
        if ( !indexDir.exists() ) {
            throw new IOException("not found indexing folder");
        }

	this.caseFacade = caseFacade;
      
        // using stop analyzer
        this.writer = new IndexWriter(FSDirectory.open(indexDir), new StopAnalyzer(Version.LUCENE_30, 
                    new File(ApplicationConstants.STOP_WORD_FILE)),
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
    
    CaseFacade getCaseFacade() { return this.caseFacade; }
    IndexWriter getWriter () { return this.writer ; }
    
    int getIndexNumber () throws IOException {
        int numIndexed = writer.numDocs();
        return numIndexed ;
    }

    void closeIndex () throws IOException {
        writer.optimize();
	writer.close();
    }
    
    public boolean indexFile(File file, IndexingDialog dialog) throws UnsupportedOperationException{
        return indexFile(file, 0, dialog);
    }
    
    public boolean indexFile(File file, int parentId, IndexingDialog dialog) throws UnsupportedOperationException{ 
        try {
            Indexer indexType = IndexerFactory.getIndexer(this, file, parentId);
            
            if ( indexType == null )
                return false;
            
            if ( dialog !=  null) 
                indexType.setGUIDialog(dialog);
            
            return indexType.doIndexing();
        }
        catch(NullPointerException e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
        catch(UnsupportedOperationException e){
            //throw new UnsupportedOperationException(e.getMessage());
        }
        
        return false;
    }
}
