/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.parse;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.email.EmailReader;
import edu.coeia.email.MessageHeader ;
import edu.coeia.filesystem.index.FileDocument;

import java.util.ArrayList ;

import com.pff.PSTFile ;
import com.pff.PSTMessage;
import com.pff.PSTException ;
import com.pff.PSTObject;

import org.apache.lucene.document.Document;

import java.io.FileNotFoundException ;
import java.io.IOException ;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.index.IndexWriter;

public class PSTParser {
    private PSTFile pstFile ;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private FileHandler handler ;

    public PSTParser () {
        try {
            handler = new FileHandler("emailparsing.log");
            logger.addHandler(handler);
            logger.log(Level.INFO, "this is first line in indexing");
        } catch (IOException ex) {
           logger.log(Level.SEVERE, "Uncaught exception", ex);
        }

        logger.log(Level.INFO, "Start Email Parsing");
    }

//    public ArrayList<Document> parseDocument (PSTFile pstFile , String path)  throws
//    FileNotFoundException, PSTException, IOException{
//        ArrayList<Document> docs = new ArrayList<Document>();
//        this.pstFile = pstFile ;
//
//        ArrayList<MessageHeader> messageHeaderLists = EmailReader.getInstance(pstFile,path,null);
//        int row = messageHeaderLists.size();
//        logger.log(Level.INFO, "message header size: " + row);
//
//        System.out.println("before writing message header");
//        for (int i=0 ; i<row ; i++ ) {
//            MessageHeader m = messageHeaderLists.get(i);
//            PSTMessage mapValue = getMessage(m.getID());
//            String content = mapValue.getBodyHTML();
//
//            System.out.println("mHeader: " + m.getSubject() + " inside: " + m.getLocation() + " id: " + i);
//
//            if ( ! content.isEmpty() ) {
//                Document doc = FileDocument.documentMessage(m, mapValue, content);
//                docs.add(doc);
//            }
//        }
//        System.out.println("after writing message header");
//
//
//        return (docs);
//    }

    public boolean parseIndexDocument (PSTFile pstFile , String path, IndexWriter writer)  throws
    FileNotFoundException, PSTException, IOException {
        this.pstFile = pstFile ;

        ArrayList<MessageHeader> messageHeaderLists = EmailReader.getInstance(pstFile,path,null);
        if ( messageHeaderLists == null ) {
            logger.log(Level.INFO, "Message header list is null");
            return false;
        }

        int row = messageHeaderLists.size();
        logger.log(Level.INFO, "message header size: " + row);

        for (int i=0 ; i<row ; i++ ) {
            MessageHeader m = messageHeaderLists.get(i);
            PSTMessage mapValue = getMessage(m.getID());
            String content = mapValue.getBodyHTML();

            Document doc = FileDocument.documentMessage(m, mapValue, content);
            writer.addDocument(doc);
        }

        logger.log(Level.INFO, "Finish parseIndex" + row);
        return true;
    }

    private PSTMessage getMessage (long id)throws IOException, PSTException {
        return (PSTMessage) PSTObject.detectAndLoadPSTObject(pstFile, id);
    }
}
