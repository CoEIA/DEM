/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.offlinemail;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.util.FilesPath ;

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
import org.apache.lucene.index.Term;
import org.apache.lucene.util.Version ;
import org.apache.lucene.queryParser.QueryParser ;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TermQuery;

public class PSTSearcher {

    protected Directory fsDir ;
    protected IndexReader indexReader ;
    protected IndexSearcher searcher ;
    protected TopDocs results ;

    public enum SearchField {
        ID("mailid"), LOCATION("location"), TITLE("mailtitle"), DATE("mailDate"),
        TO("mailTo"), FROM("mailFrom"), HAS_ATTACGMENT("mailHasAttachment"),
        CONTENT("mailcontent");

        SearchField(String value) {
            this.value = value ;
        }

        public String Value() { return this.value; }

        private String value ;
    }

    public PSTSearcher (File indexDir ) throws Exception {
        fsDir = FSDirectory.open(indexDir);
        indexReader = IndexReader.open(fsDir, true);
        searcher = new IndexSearcher(indexReader);
    }

    public int search (String queryString, SearchField field) throws Exception {
//	Query query = QueryParser.parse(queryString,"mailcontent", new StandardAnalyzer());
//	hits  = is.search(query);
//        return hits.length();

        //Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_20);
        Analyzer analyzer = new StopAnalyzer(Version.LUCENE_20,  new File(FilesPath.STOP_WORD_FILE));

        QueryParser parser = new QueryParser(Version.LUCENE_20, field.Value(), analyzer);
        Query query = parser.parse(queryString) ;

	//Query query = QueryParser.parse(queryString,"body", new StandardAnalyzer());
	//hits  = is.search(query);
        //return hits.length();

        results = searcher.search(query,100);

        return results.totalHits ;
    }

    public int searchInbox() throws Exception {
//        Analyzer analyzer = new StopAnalyzer(Version.LUCENE_20,  new File(FilesPath.STOP_WORD_FILE));

//        QueryParser parser = new QueryParser(Version.LUCENE_20,"", analyzer);
//        Query query = parser.parse(queryString) ;

//        BooleanQuery query = new BooleanQuery();
//        query.add(new TermQuery(new Term(SearchField.LOCATION.Value(), "Inbox")), BooleanClause.Occur.MUST);
        //query.add(new TermQuery(new Term(SearchField.TO.Value(), queryString)), BooleanClause.Occur.MUST);

        //Query query = new TermQuery(new Term(SearchField.LOCATION.Value(), "Inbox"));
        Query query = new MatchAllDocsQuery(SearchField.LOCATION.Value());
        
        results = searcher.search(query,100);

        return results.totalHits ;
    }

     public String getHits (int index) throws Exception {
        ScoreDoc[] hits = results.scoreDocs;
        int id = hits[index].doc;
        Document doc = searcher.doc(id);
        return doc.get("mailid") ;
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
