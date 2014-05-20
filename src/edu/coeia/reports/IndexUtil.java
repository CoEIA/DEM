/* 
 * Copyright (C) 2014 Center of Excellence in Information Assurance
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.coeia.reports;

import edu.coeia.cases.Case;
import edu.coeia.cases.management.ApplicationManager;
import edu.coeia.cases.CaseFacade;
import edu.coeia.constants.IndexingConstant;
import edu.coeia.util.FileUtil;
import edu.coeia.util.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import org.apache.lucene.index.TermEnum ;
import org.apache.lucene.index.Term ;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author wajdyessam
 */
public final class IndexUtil {
    
    public static List<Case> getAllCases() throws FileNotFoundException, IOException, ClassNotFoundException {
        return Collections.unmodifiableList(ApplicationManager.Manager.getCases());
    }
    
    public static Map<String, Double> getAllFilesFrequency(final CaseFacade caseFacade)
            throws IOException{
        
        Map<String,Double> map = new HashMap<String,Double>();
        
        String indexDir = caseFacade.getCaseIndexFolderLocation();
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        
        TermEnum te = indexReader.terms(new Term(IndexingConstant.FILE_PATH,"") );
        while ( te.next() ) {
            Term currentTerm = te.term();

            if ( ! currentTerm.field().equals(IndexingConstant.FILE_PATH))
                continue ;

            String file = currentTerm.text();
            String fullPath = caseFacade.getFullPath(file);
            String ext = FileUtil.getExtension(fullPath);
            
            if ( ext == null || ext.length() > 6) // no more extension than 5 character!
                continue;

            ext = ext.toLowerCase();

            if ( map.get(ext) == null ){
                map.put(ext, 1.0);
            }
            else
                map.put(ext, map.get(ext) + 1);
        }

        te.close();
        indexReader.close();
                
        return map ;
    }
    
    public static List<String> getAllFilesHaveAuthers(final CaseFacade caseFacade,
            List<String> authers) throws IOException{
        List<String> files = new ArrayList<String>();
        files.addAll(getAllFilePathsHaveAuther(caseFacade, authers));
        return files;
    }
    
    public static List<String> getAllFilesBetweenSize(final CaseFacade caseFacade, 
            final long from, final long to) throws IOException{
        List<String> files = new ArrayList<String>();
        
         for(String fileName: getAllFilePaths(caseFacade)) {
            File file = new File(fileName);
            long length = file.length();
            
            if ( length >= from && length <= to) {
                files.add(fileName);
            }
        }
         
        return files;
    }
    
    public static List<String> getAllFilesBetweenDates(final CaseFacade caseFacade, 
            final Date from, final Date to) throws IOException{
        List<String> files = new ArrayList<String>();
        
        for(String fileName: getAllFilePaths(caseFacade)) {
            File file = new File(fileName);
            if ( FileUtils.isFileNewer(file, from) && FileUtils.isFileOlder(file, to) ) {
                files.add(fileName);
            }
        }
        
        return files;
    }
    
    public static List<String> getAllFilePaths(final CaseFacade caseFacade) 
            throws IOException {
        
        List<String> files = new ArrayList<String>();
        
        String indexDir = caseFacade.getCaseIndexFolderLocation();
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        
        for (int i=0; i<indexReader.maxDoc(); i++) {
            Document document = indexReader.document(i);
            if ( document != null ) {
                Field field = document.getField(IndexingConstant.DOCUMENT_TYPE);
                if ( field != null && field.stringValue() != null) {
                    String path = field.stringValue();
                   
                    if ( path.equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_GENERAL_TYPE.FILE)) ) {
                        String relativePath = document.get(IndexingConstant.FILE_PATH);
                        
                        if ( !relativePath.isEmpty() ) {
                            String fullpath = caseFacade.getFullPath(relativePath);
                            files.add(fullpath);
                        }
                    }
                }
            }
        }
        
        indexReader.close();
        return files;
    }
    
    private static List<String> getAllFilePathsHaveAuther(final CaseFacade caseFacade, 
            final List<String> authers) 
            throws IOException {
        
        List<String> files = new ArrayList<String>();
        
        String indexDir = caseFacade.getCaseIndexFolderLocation();
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        
        for (int i=0; i<indexReader.maxDoc(); i++) {
            Document document = indexReader.document(i);
            if ( document != null ) {
                Field field = document.getField(IndexingConstant.DOCUMENT_TYPE);
                if ( field != null && field.stringValue() != null) {
                    String path = field.stringValue();
                   
                    if ( path.equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_GENERAL_TYPE.FILE)) ) {
                        String relativePath = document.get(IndexingConstant.FILE_PATH);
                        String auther = document.get("Author");
                        
                        if ( !relativePath.isEmpty() && auther != null && !auther.trim().isEmpty() 
                                && Utilities.isFound(authers, auther) )  {
                            String fullpath = caseFacade.getFullPath(relativePath);
                            files.add(fullpath);
                        }
                    }
                }
            }
        }
        
        indexReader.close();
        return files;
    }
    
    public static List<String> getAllAuthers(final CaseFacade caseFacade) 
            throws IOException {
        
        List<String> files = new ArrayList<String>();
        
        String indexDir = caseFacade.getCaseIndexFolderLocation();
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        
        for (int i=0; i<indexReader.maxDoc(); i++) {
            Document document = indexReader.document(i);
            if ( document != null ) {
                Field field = document.getField(IndexingConstant.DOCUMENT_TYPE);
                if ( field != null && field.stringValue() != null) {
                    String path = field.stringValue();
                   
                    if ( path.equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_GENERAL_TYPE.FILE)) ) {
                        String relativePath = document.get(IndexingConstant.FILE_PATH);
                        String auther = document.get("Author");
                        
                        if ( !relativePath.isEmpty() && auther != null && !auther.trim().isEmpty()) {
                            files.add(auther);
                        }
                    }
                }
            }
        }
        
        indexReader.close();
        return files;
    }
}
