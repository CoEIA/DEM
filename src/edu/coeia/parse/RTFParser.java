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
import java.io.IOException;
import java.io.InputStreamReader ;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;
import javax.swing.text.BadLocationException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class RTFParser implements TypeParser {
    private String filePath ;

    public RTFParser (String fp) {
        filePath = fp ;
    }
    
    public Document parseDocument (InputStream is) throws Exception {
        String bodyText = null;
        //System.out.println("Parse File: " + filePath );
        
        DefaultStyledDocument styledDoc = new DefaultStyledDocument();
        try {
            InputStreamReader file = new InputStreamReader(is,"UTF8");
            new RTFEditorKit().read(file, styledDoc, 0);
            bodyText = styledDoc.getText(0, styledDoc.getLength());
        }
        catch (IOException e) {
            throw new Exception("Cannot extract text from a RTF document", e);
        }
        catch (BadLocationException e) {
            throw new Exception("Cannot extract text from a RTF document", e);
        }

        if (bodyText != null) {
            Document doc = new Document();

//            doc.add(Field.UnStored("body", bodyText));
//            doc.add(Field.Keyword("filename", filePath ) );
            
            return doc;
        }
        
        return null;
    }
}
