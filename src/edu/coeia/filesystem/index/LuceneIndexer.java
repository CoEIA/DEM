/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.filesystem.index;

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
import edu.coeia.indexing.Indexer;
import edu.coeia.indexing.IndexerFactory;
import edu.coeia.main.util.FilesPath;

import java.io.FileNotFoundException ;
import java.io.File;
import java.io.IOException;

import java.util.List ;
import org.apache.lucene.analysis.StopAnalyzer;

import org.apache.tika.exception.TikaException;

// to be changed

class LuceneIndexer {

    private IndexWriter writer ;
    private List<String> imagesPath ;
    private Case caseObject; 
    
    // last parameter will create new index folder
    public LuceneIndexer (File indexDir, List<String> exts, Case caseObj) throws IOException {
       this(indexDir,exts,true, caseObj);
    }

    // add index to exists index folder if boolean value is false
    public LuceneIndexer (File indexDir, List<String> imagesPath,  boolean newIndex, Case caseObject) throws IOException {
        if ( !indexDir.exists() ) {
            throw new IOException("not found indexing folder");
        }

        this.imagesPath = imagesPath ;
	this.caseObject = caseObject; 
        
        // using standard analyzer
        //writer = new IndexWriter(FSDirectory.open(indexDir), new StandardAnalyzer(Version.LUCENE_20), true, IndexWriter.MaxFieldLength.LIMITED);
        
        // using stop analyzer
        writer = new IndexWriter(FSDirectory.open(indexDir), new StopAnalyzer(Version.LUCENE_20, new File(FilesPath.STOP_WORD_FILE)),
                true, IndexWriter.MaxFieldLength.UNLIMITED);

	writer.setUseCompoundFile(false);
    }

    public int getIndexNumber () throws IOException {
        int numIndexed = writer.numDocs();
        
        return numIndexed ;
    }

    public void closeIndex () throws IOException {
        writer.optimize();
	writer.close();
    }

//    public boolean indexFile (File f) throws IOException, FileNotFoundException, PSTException,
//    TikaException {
//        String ext = Utilities.getExtension(f).toLowerCase();
//        
//        if ( ext.equalsIgnoreCase("pst") || ext.equalsIgnoreCase("ost"))
//            return parsePST(f.getAbsolutePath());
//        
//        else {
//            Document doc = parseFile(f);
//
//            if ( doc != null) {
//                writer.addDocument(doc);    // index file
//            }
//            else {
//                System.out.println("Fail Parsing: " + f.getAbsolutePath());
//                return false;
//            }
//        }
//        
//        return true ;
//    }
    
    public boolean indexFile(File file)  
            throws IOException, FileNotFoundException, PSTException, TikaException {
   
        return indexContent(file);
    }
    
    private boolean indexContent(File file) 
            throws IOException, FileNotFoundException, PSTException, TikaException {
        
        try {
            Indexer indexType = IndexerFactory.getIndexer(file, true, this.caseObject.getIndexLocation());
            return indexType.doIndexing(writer);
        }
        catch(UnsupportedOperationException e){
            System.out.println(e.getMessage());
        }
        
        return false;
    }
    
//
//    // 0 = TXT , 1 = RTF , 2 = HTML , 3=HTM , 4 = MHT , 5 = DOC , 6 = PDF , 7 = XML
//    private Document parseFile (File f) throws IOException, PSTException, FileNotFoundException,
//    TikaException {
//        Document doc = null ;
//            
////            FileInputStream file = new FileInputStream(f);
////            if ( ext.equalsIgnoreCase("txt") )
////                doc =  new edu.coeia.parse.TXTParser(f.getAbsolutePath()).parseDocument(file);
////            else if ( ext.equalsIgnoreCase("rtf"))
////                doc =  new edu.coeia.parse.RTFParser(f.getAbsolutePath()).parseDocument(file);
////            else if ( ext.equalsIgnoreCase("html"))
////                doc =  new edu.coeia.parse.HTMLParser(f.getAbsolutePath()).parseDocument(file);
////            else if ( ext.equalsIgnoreCase("htm"))
////                doc =  new edu.coeia.parse.HTMLParser(f.getAbsolutePath()).parseDocument(file);
////            else if ( ext.equalsIgnoreCase("mht"))
////                doc =  new edu.coeia.parse.HTMLParser(f.getAbsolutePath()).parseDocument(file);
////            else if ( ext.equalsIgnoreCase("doc"))
////                doc =  new edu.coeia.parse.DOCParser(f.getAbsolutePath()).parseDocument(file);
////            else if ( ext.equalsIgnoreCase("pdf"))
////                 doc =  new edu.coeia.parse.PDFDocParser(f.getAbsolutePath()).parseDocument(file);
////            else if ( ext.equalsIgnoreCase("xml"))
////                doc =  new edu.coeia.parse.XMLParser(f.getAbsolutePath()).parseDocument(file);
//            
//        doc = new Document();
//        Tika tika = new Tika();
//        String mime = tika.detect(f);
//
//        if ( mime.startsWith("image")) {
//            imagesPath.add(f.getAbsolutePath());
//            //doc.add(Field.Keyword("mime", f.getAbsolutePath()));
//            doc = FileDocument.documentImage(f);
//        }
//        else {
//            String bodyText = tika.parseToString(f);
//            //doc.add(Field.UnStored("body", bodyText) );
//            //doc.add(Field.Keyword("filename", f.getAbsolutePath() ) );
//            doc = FileDocument.documentFile(f, bodyText);
//        }
//        
//        return (doc);
//    }
//
//    private boolean parsePST (String path) throws FileNotFoundException, PSTException, IOException{
//        PSTParser parser = new PSTParser();
//        return parser.parseIndexDocument(new PSTFile(path), path, writer);
//    }

}
