/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.filesystem.parse;

/**
 *
 * @author wajdyessam
 *
 */


import java.io.InputStream;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.pdfparser.PDFParser ;

public class PDFDocParser implements TypeParser {

    private String filePath ;

    public PDFDocParser (String fp) {
        filePath = fp ;
    }

    public Document parseDocument(InputStream is) throws Exception {
        COSDocument cosDoc = null;
        Document doc = null ;
        
        try {
            //System.out.println("Parse File: " + filePath );
            
            PDFParser parser = new PDFParser(is);
            parser.parse();
            cosDoc = parser.getDocument();

             if (cosDoc.isEncrypted()) {
                return null ;
            }

            // extract PDF document's textual content
            String docText = null;
            PDFTextStripper stripper = new PDFTextStripper();
            docText = stripper.getText(new PDDocument(cosDoc));

            doc = new Document();

            if (docText != null) {
//                doc.add(Field.UnStored("body", docText));
//                doc.add(Field.Keyword("filename", filePath ) );
            }
            
            closeCOSDocument(cosDoc);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }

    private void closeCOSDocument(COSDocument cosDoc) {
        if (cosDoc != null) {
            try {
                cosDoc.close();
            }
            catch (IOException e) {
               e.printStackTrace();
            }
        }
    }
}
