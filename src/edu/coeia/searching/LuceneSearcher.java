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

import edu.coeia.cases.Case;
import edu.coeia.constants.IndexingConstant;
import edu.coeia.constants.ApplicationConstants ;

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
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.TermQuery;

public class LuceneSearcher {

    protected Directory fsDir ;
    protected IndexReader indexReader ;
    protected IndexSearcher searcher ;
    protected TopDocs results ;
    
    private final int MAX_RESULT = 5000;
    
    public LuceneSearcher (final Case currentCase) throws Exception {
        File caseLocation = new File (currentCase.getCaseLocation() + "\\" + ApplicationConstants.CASE_INDEX_FOLDER);
        
        fsDir = FSDirectory.open(caseLocation);
        indexReader = IndexReader.open(fsDir, true);
        searcher = new IndexSearcher(indexReader);
    }
    
    public int search (String queryString, SearchScope luceneFields) throws Exception {
        // using stop analyzer in search
        Analyzer analyzer = new StopAnalyzer(Version.LUCENE_30,  new File(ApplicationConstants.STOP_WORD_FILE));
        
        String[] fields = getSupportedFileds(luceneFields);
        
        MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_30, fields, analyzer);
        
        // scpaing special characters
        queryString = QueryParser.escape(queryString);
        
        Query query = parser.parse(queryString);
        results = searcher.search(query, MAX_RESULT);

        int filterTotalHist = results.totalHits > MAX_RESULT ? MAX_RESULT: results.totalHits;
        return filterTotalHist;
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
            if ( !field.startsWith("FILE_") && // not file system fields
                 !field.startsWith("CHAT_") && // not chat fields
                 !field.startsWith("OFFLINE_EMAIL") && // not email fields
                 !field.startsWith("ONLINE_EMAIL_"))  // not online emial fields
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
        
        fields.add(IndexingConstant.ONLINE_EMAIL_BODY);
        fields.add(IndexingConstant.OFFLINE_EMAIL_HTML_CONTENT);
        fields.add(IndexingConstant.OFFLINE_EMAIL_PLAIN_CONTENT);
        
        return fields;        
    }
    
    private Collection<String> getEmailHeaderFields() {
        Collection<String> fields = new ArrayList<String>();
        
        fields.add(IndexingConstant.ONLINE_EMAIL_ATTACHMENT_PATH);
        fields.add(IndexingConstant.ONLINE_EMAIL_BCC);
        fields.add(IndexingConstant.ONLINE_EMAIL_CC);
        fields.add(IndexingConstant.ONLINE_EMAIL_FOLDER_NAME);
        fields.add(IndexingConstant.ONLINE_EMAIL_FROM);
        fields.add(IndexingConstant.ONLINE_EMAIL_RECIEVED_DATE);
        fields.add(IndexingConstant.ONLINE_EMAIL_SENT_DATE);
        fields.add(IndexingConstant.ONLINE_EMAIL_SUBJECT);
        fields.add(IndexingConstant.ONLINE_EMAIL_TO);
        
        fields.add(IndexingConstant.OFFLINE_EMAIL_SUBJECT);
        fields.add(IndexingConstant.OFFLINE_EMAIL_ADDRESS);
        fields.add(IndexingConstant.OFFLINE_EMAIL_HEADER);
        fields.add(IndexingConstant.OFFLINE_EMAIL_SIZE);
        fields.add(IndexingConstant.OFFLINE_EMAIL_SENT_REPRESENTING_NAME);
        fields.add(IndexingConstant.OFFLINE_EMAIL_SENDER_ADDRESS_TYPE);
        fields.add(IndexingConstant.OFFLINE_EMAIL_SENDER_EMAIL_ADDRESS);
        fields.add(IndexingConstant.OFFLINE_EMAIL_SENDER_NAME);
        fields.add(IndexingConstant.OFFLINE_EMAIL_RECIEVED_BY_ADDRESS);
        fields.add(IndexingConstant.OFFLINE_EMAIL_RECIEVED_BY_ADDRESS_TYPE);
        fields.add(IndexingConstant.OFFLINE_EMAIL_RECIEVED_BY_NAME);
        fields.add(IndexingConstant.OFFLINE_EMAIL_DISPLAY_TO);
        fields.add(IndexingConstant.OFFLINE_EMAIL_DISPLAY_CC);
        fields.add(IndexingConstant.OFFLINE_EMAIL_DISPLAY_BCC);
        fields.add(IndexingConstant.OFFLINE_EMAIL_HAS_REPLIED);
        fields.add(IndexingConstant.OFFLINE_EMAIL_HAS_FORWARDED);
        fields.add(IndexingConstant.OFFLINE_EMAIL_RECIPENT_NAME);
        fields.add(IndexingConstant.OFFLINE_EMAIL_RECIPENT_ADDRESS);
        fields.add(IndexingConstant.OFFLINE_EMAIL_RECIPENT_SMPT);
        
        return fields;        
    }
    
    private Collection<String> getChatContentFields() {
        Collection<String> fields = new ArrayList<String>();
        
        fields.add(IndexingConstant.CHAT_MESSAGE);
        fields.add(IndexingConstant.CHAT_AGENT);
        fields.add(IndexingConstant.CHAT_FROM);
        fields.add(IndexingConstant.CHAT_TIME);
        fields.add(IndexingConstant.CHAT_TO);
        
        return fields;        
    }
    
    public int searchById (String fileId) throws Exception{
        Term term = new Term(IndexingConstant.DOCUMENT_ID, fileId);
        Query query = new TermQuery(term);
        
        results = searcher.search(query, MAX_RESULT);
        return results.totalHits ;
    }

    public int searchParentById (String fileId) throws Exception{
        Term term = new Term(IndexingConstant.DOCUMENT_PARENT_ID, fileId);
        Query query = new TermQuery(term);
        
        results = searcher.search(query, MAX_RESULT);
        return results.totalHits;
    }
    
    public int searchForHash(final String hashValue) throws Exception {        
        Term term = new Term(IndexingConstant.DOCUMENT_HASH, hashValue);
        Query query = new TermQuery(term);
        
        results = searcher.search(query, MAX_RESULT);
        return results.totalHits;
    }

    public Document getDocHits (int index) throws Exception{
        ScoreDoc[] hits = results.scoreDocs;
        int id = hits[index].doc;
        Document doc = searcher.doc(id);

        return doc ;
    }
    
    public Document getLuceneDocumentById(final String id) {
        Document document = null ;
        try {
            int count = searchById(id);
            
            if ( count > 0 )
                document = getDocHits(0);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Id is not valid lucene document");
        }
        
        return document ;
    }
    
    public Document getParentDocument(final String id) {
        Document document = null ; 
        
        try {
            int count = searchParentById(id);
            
            if ( count > 0 )
                document = getDocHits(0);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Id is not valid lucene document");
        }
        
        return document ;
    }
    
    public void closeSearcher () throws Exception {
        fsDir.close();
        indexReader.close();
        searcher.close();
    }
}
