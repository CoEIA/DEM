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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class TXTParser implements TypeParser {
    private String filePath ;

    public TXTParser (String fp) {
        filePath = fp ;
    }
    
    public Document parseDocument(InputStream is) throws Exception {
        String bodyText = "";
        //System.out.println("Parse File: " + filePath );
        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
        
            String line = null;
            while ((line = br.readLine()) != null) {
                bodyText += line;
            }

            br.close();
        }
        catch(IOException e) {
            throw new Exception("Cannot read the text document", e);
        }

        if ( !bodyText.equals("")) {
            Document doc = new Document();
//            doc.add(Field.UnStored("body", bodyText) );
//            doc.add(Field.Keyword("filename", filePath ) );
            return doc;
        }

        return null;
    }
}
