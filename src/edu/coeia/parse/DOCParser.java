/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.parse;

/**
 *
 * @author wajdyessam
 *
 */

import java.io.InputStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.File ;

import org.apache.poi.hdf.extractor.WordDocument;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field ;
import org.apache.lucene.document.DateTools ;

public class DOCParser implements TypeParser {
    private String filePath ;

    public DOCParser (String fp) {
        filePath = fp ;
    }
    
    public Document parseDocument (InputStream is) throws Exception {
        String bodyText = null;
        //System.out.println("Parse File: " + filePath );
        
        try {
            WordDocument wd = new WordDocument(is);
            StringWriter docTextWriter = new StringWriter();
            wd.writeAllText(new PrintWriter(docTextWriter));
            bodyText = docTextWriter.toString();
            docTextWriter.close();
        }
        catch (Exception e) {
            throw new Exception("Cannot extract text from a Word document", e);
        }

        if ((bodyText != null) && (bodyText.trim().length() > 0)) {
            Document doc = new Document();
            
//            doc.add(Field.UnStored("body", bodyText));
//            doc.add(Field.Keyword("filename", filePath ) );

            doc.add(new Field("filename", filePath, Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field("Modified", DateTools.timeToString(new File(filePath).lastModified(), DateTools.Resolution.MINUTE),
                Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field("body", bodyText, Field.Store.NO, Field.Index.ANALYZED));

            return doc;
        }
        
        return null;
    }
}
