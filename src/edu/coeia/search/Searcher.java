/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.search;

/**
 *
 * @author wajdyessam
 *
 */

import edu.coeia.utility.FilesPath ;

import java.io.File ;

import org.apache.lucene.search.TopDocs ;
import org.apache.lucene.search.IndexSearcher ;
import org.apache.lucene.search.Query ;
import org.apache.lucene.search.ScoreDoc;

import org.apache.lucene.store.Directory ;
import org.apache.lucene.store.FSDirectory ;

import org.apache.lucene.analysis.Analyzer ;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer ;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.document.Document ;
import org.apache.lucene.util.Version ;
import org.apache.lucene.queryParser.QueryParser ;

public class Searcher {

    protected Directory fsDir ;
    protected IndexReader indexReader ;
    protected IndexSearcher searcher ;
    protected TopDocs results ;
    
    public Searcher (File indexDir ) throws Exception {
        fsDir = FSDirectory.open(indexDir);
        indexReader = IndexReader.open(fsDir, true);
        searcher = new IndexSearcher(indexReader);
    }
    
    public int search (String queryString) throws Exception {

        //Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_20);

        // using stop analyzer in search
        Analyzer analyzer = new StopAnalyzer(Version.LUCENE_20,  new File(FilesPath.STOP_WORD_FILE));

        QueryParser parser = new QueryParser(Version.LUCENE_20, "body", analyzer);
        Query query = parser.parse(queryString) ;
        
	//Query query = QueryParser.parse(queryString,"body", new StandardAnalyzer());
	//hits  = is.search(query);
        //return hits.length();

        results = searcher.search(query,1000);
        
        return results.totalHits ;

//        ScoreDoc[] hits = searcher.search(query, 100).scoreDocs ;
//
//        for (ScoreDoc hit: hits) {
//            Document doc = searcher.doc(hit.doc);
//            System.out.println("PATH: " + doc.get("path"));
//        }
    }

    public String getHits (int index) throws Exception  {
        ScoreDoc[] hits = results.scoreDocs;
        int id = hits[index].doc;
        Document doc = searcher.doc(id);
        return doc.get("filename") ;
    }

    public Document getDocHits (int index) throws Exception {
        ScoreDoc[] hits = results.scoreDocs;
        int id = hits[index].doc;
        Document doc = searcher.doc(id);

        return doc ;
    }
    
    public void closeSearcher () throws Exception {
        fsDir.close();
        indexReader.close();
        searcher.close();
    }
}
