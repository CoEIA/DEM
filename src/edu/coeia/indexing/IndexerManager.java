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
import edu.coeia.cases.CaseFacade;
import edu.coeia.constants.ApplicationConstants;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory ;
import org.apache.lucene.util.Version ;

public final class IndexerManager {

    private final IndexWriter writer ;
    private final CaseFacade caseFacade;
    private final List<IndexerListener> listeners = new ArrayList<IndexerListener>();
    
    /*
     * Static Factory Method 
     * Create New Instance of Lucene Indexer
     */
    public static IndexerManager newInstance(final CaseFacade caseFacade) throws IOException{ 
        return new IndexerManager(caseFacade);
    }
    
    public boolean indexFile(File file, CrawlerIndexerThread crawler) throws UnsupportedOperationException{
        return indexFile(file, 0, crawler);
    }
    
    public boolean indexFile(File file, int parentId, CrawlerIndexerThread crawler) throws UnsupportedOperationException{ 
        boolean result = false;
        fireIndexerStarted(file.getAbsolutePath());
        
        try {
            Indexer indexType = IndexerFactory.getIndexer(this, file, parentId);
            
            if ( indexType == null )
                return false;
            
            if ( crawler !=  null) 
                indexType.setCrawler(crawler);
            
            result = indexType.doIndexing();
            // here if true, increase indexed item by one
            // else increase error by one
        }
        catch(NullPointerException e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
        catch(UnsupportedOperationException e){
            // here add error item , incease by one
            throw new UnsupportedOperationException(e.getMessage());
        }
        
        fireIndexerCompleted(file.getAbsolutePath(), result);
        return result;
    }

    public void writeEvidenceLocation(final List<String> paths) throws CorruptIndexException,
            IOException {
        Document document = new Document();
        
        for (String path: paths)
            document.add( new Field(IndexingConstant.EVIDENCE_PATH, path, 
                    Field.Store.YES, Field.Index.NO));
        
        this.addDocument(document);
    }
    
    public CaseFacade getCaseFacade() { return this.caseFacade; }
    
    public void addDocument(final Document document) throws CorruptIndexException, IOException {
        this.writer.addDocument(document);
    }

    public void closeIndex () throws IOException {
        writer.commit();
        writer.optimize();
	writer.close();
    }
    
    public void addListener(final IndexerListener listener) {
        listeners.add(listener);
    }

    public void removeListner(final IndexerListener listener) {
        listeners.remove(listener);
    }

    private void fireIndexerStarted(final String path) {
        IndexerEvent event = new IndexerEvent(path);

        for (IndexerListener listener : this.listeners) {
            listener.indexerStarted(event);
        }
    }

    private void fireIndexerCompleted(final String path, final boolean result) {
        IndexerEvent event = new IndexerEvent(path, result);

        for (IndexerListener listener : this.listeners) {
            listener.indexerCompleted(event);
        }
    }
        
    private IndexerManager (final CaseFacade caseFacade) throws IOException {
        File indexDir = new File(caseFacade.getCaseIndexFolderLocation());
        
        if ( !indexDir.exists() ) {
            throw new IOException("not found indexing folder");
        }

	this.caseFacade = caseFacade;
      
        // using stop analyzer
        this.writer = new IndexWriter(
            FSDirectory.open(indexDir),
            new StopAnalyzer(Version.LUCENE_30, new File(ApplicationConstants.STOP_WORD_FILE)),
            true,
            IndexWriter.MaxFieldLength.UNLIMITED
        );

	this.writer.setUseCompoundFile(false);
    }
}
