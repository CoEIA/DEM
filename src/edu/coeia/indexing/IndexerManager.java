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
import org.apache.tika.Tika;

public final class IndexerManager {

    private final IndexWriter writer ;
    private final CaseFacade caseFacade;
    private final List<IndexerListener> indexerListeners = new ArrayList<IndexerListener>();
    private final List<ScanningListener> scanningListeners = new ArrayList<ScanningListener>();
    private final Tika tika = new Tika();
    
    /*
     * Static Factory Method 
     * Create New Instance of Lucene Indexer
     */
    public static IndexerManager newInstance(final CaseFacade caseFacade) throws IOException{ 
        return new IndexerManager(caseFacade);
    }
    
    public boolean indexFile(File file) throws UnsupportedOperationException{
        return indexFile(file, 0);
    }
    
    public boolean indexFile(File file, int parentId) throws UnsupportedOperationException{ 
        boolean result = false;
        fireItemIndexerStarted(file.getAbsolutePath());
        
        try {
            Indexer indexType = IndexerFactory.getIndexer(this, tika, file, parentId);
            
            if ( indexType != null ) {
                result = indexType.doIndexing();
            }
        }
        catch(NullPointerException e) {
            result = false;
        }
        catch(UnsupportedOperationException e){
            result = false;
        }
        
        fireItemIndexerCompleted(file.getAbsolutePath(), result);
        return result;
    }
    
    public Tika getTika() { return this.tika; }

    public void fireItemIndexerStarted(final String msg) {
        fireIndexerStarted(msg);
    }
    
    public void fireItemIndexerCompleted(final String msg, final boolean result) {
        fireIndexerCompleted(msg, result);
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
        //this.writer.commit();
    }

    public void closeIndex () throws IOException {
        System.out.println("closing index righ now");
        writer.commit();
        writer.optimize();
	writer.close();
    }
    
    public void addScanningListener(final ScanningListener listener) {
        this.scanningListeners.add(listener);
    }
    
    public void removeScanningListener(final ScanningListener listener) {
        this.scanningListeners.add(listener);
    }
    
    public void fireScanningStarted(final String path, final long size) {
       ScanningEvent event = new ScanningEvent(path, size);
       for(ScanningListener listener: this.scanningListeners) {
           listener.scanningStarted(event);
       }
    }
    
    public void addIndexerListener(final IndexerListener listener) {
        indexerListeners.add(listener);
    }

    public void removeIndexerListener(final IndexerListener listener) {
        indexerListeners.remove(listener);
    }

    private void fireIndexerStarted(final String path) {
        IndexerEvent event = new IndexerEvent(path);

        for (IndexerListener listener : this.indexerListeners) {
            listener.indexerStarted(event);
        }
    }

    private void fireIndexerCompleted(final String path, final boolean result) {
        IndexerEvent event = new IndexerEvent(path, result);

        for (IndexerListener listener : this.indexerListeners) {
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
        this.writer.setRAMBufferSizeMB(500);
    }
}
