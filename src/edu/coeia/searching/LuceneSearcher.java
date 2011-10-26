/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.searching;

/**
 *
 * @author wajdyessam
 *
 */

import edu.coeia.indexing.IndexingConstant;
import edu.coeia.util.FilesPath ;

import java.io.File ;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.search.TopDocs ;
import org.apache.lucene.search.IndexSearcher ;
import org.apache.lucene.search.Query ;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory ;
import org.apache.lucene.store.FSDirectory ;
import org.apache.lucene.analysis.Analyzer ;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.document.Document ;
import org.apache.lucene.util.Version ;
import org.apache.lucene.index.Term ;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.TermQuery;

class LuceneSearcher {

    protected Directory fsDir ;
    protected IndexReader indexReader ;
    protected IndexSearcher searcher ;
    protected TopDocs results ;
    
    public LuceneSearcher (File indexDir ) throws Exception {
        fsDir = FSDirectory.open(indexDir);
        indexReader = IndexReader.open(fsDir, true);
        searcher = new IndexSearcher(indexReader);
    }
    
    public int search (String queryString, SearchScope luceneFields) throws Exception {
        // using stop analyzer in search
        Analyzer analyzer = new StopAnalyzer(Version.LUCENE_20,  new File(FilesPath.STOP_WORD_FILE));
        
        String[] fields = getSupportedFileds(luceneFields);
        
        MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_20, fields, analyzer);
        
        Query query = parser.parse(queryString);
        results = searcher.search(query, 1000);

        return results.totalHits ;
    }
    
    private String[] getSupportedFileds(SearchScope luceneFields) {
        Collection<String> fields = new ArrayList<String>();
        
        if ( luceneFields.haveFileSystemContent() )
            fields.addAll(getFileSystemContentFields());
        
        if ( luceneFields.haveFileSystemMetadata() )
            fields.addAll(getFileSystemMetadataFields());
        
        if ( luceneFields.haveEmailContent() )
            fields.addAll(getEmailContentFields());
        
        if ( luceneFields.haveEmailHeader() )
            fields.addAll(getEmailHeaderFields());
        
        if ( luceneFields.haveChatContent() )
            fields.addAll(getChatContentFields());
        
        // converting to array 
        String[] fieldsArray = new String[fields.size()];
        fieldsArray = fields.toArray(fieldsArray);
        
        return fieldsArray;
    }
    
    private Collection<String> getFileSystemContentFields () {
        Collection<String> fields = new ArrayList<String>();
        
        fields.add(IndexingConstant.FILE_CONTENT);
        
        return fields;
    }
    
    private Collection<String> getFileSystemMetadataFields() {
        Collection<String> fields = new ArrayList<String>();
        
        Collection<String> allFileds = this.indexReader.getFieldNames(IndexReader.FieldOption.ALL);
        
        for(String field: allFileds) {
            if ( !field.startsWith("file_") && // not file system fields
                 !field.startsWith("chat_") && // not chat fields
                 !field.startsWith("email_") && // not email fields
                 !field.startsWith("online_"))  // not online emial fields
            {
                // this will file system metadata , extracted by tika library
                // since we don't know the name of this fields
                fields.add(field);
            }
        }
        
        return fields;        
    }
    
    private Collection<String> getEmailContentFields() { 
        Collection<String> fields = new ArrayList<String>();
        
        fields.add(IndexingConstant.OnlineEmail_Body);
        
        return fields;        
    }
    
    private Collection<String> getEmailHeaderFields() {
        Collection<String> fields = new ArrayList<String>();
        
        //TODO: change this with the email header
        // or adding or the fields in email
        fields.add(IndexingConstant.OnlineEmail_Subject);
        
        return fields;        
    }
    
    private Collection<String> getChatContentFields() {
        Collection<String> fields = new ArrayList<String>();
        
        // TODO: add chat metadata , and add the the fields as metadata search
        fields.add(IndexingConstant.CHAT_MESSAGE);
        
        return fields;        
    }
    
    public Document searchById (String fileId) throws Exception{
        Term term = new Term(IndexingConstant.FILE_ID, fileId);
        Query query = new TermQuery(term);
        results = searcher.search(query, 10);
        
        return getDocHits(0);
    }

    public String getHits (int index) throws Exception  {
        ScoreDoc[] hits = results.scoreDocs;
        int id = hits[index].doc;
        Document doc = searcher.doc(id);
        return doc.get(IndexingConstant.FILE_NAME) ;
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
