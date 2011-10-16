/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.filesystem.parse;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.email.EmailReader;
import edu.coeia.email.MessageHeader ;
import edu.coeia.indexing.FileDocument;

import java.util.ArrayList ;

import com.pff.PSTFile ;
import com.pff.PSTMessage;
import com.pff.PSTException ;
import com.pff.PSTObject;

import edu.coeia.util.FilesPath;
import org.apache.lucene.document.Document;

import java.io.IOException ;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.index.IndexWriter;

public class PSTParser {
    private PSTFile pstFile ;
    private static final Logger logger = Logger.getLogger(FilesPath.LOG_NAMESPACE);

    public PSTParser () {
        logger.info("PSTParser Constructor");
    }
    
    public boolean parseIndexDocument (PSTFile pstFile , String path, IndexWriter writer) {
        this.pstFile = pstFile ;

        logger.info("Parse PST File: " + path);
        
        ArrayList<MessageHeader> messageHeaderLists = EmailReader.getInstance(pstFile,path,null);
        if ( messageHeaderLists == null ) {
            logger.log(Level.INFO, "Message header list is null");
            return false;
        }

        int row = messageHeaderLists.size();
        logger.log(Level.INFO, "Number of Messages in PST: " + row);

        for (int i=0 ; i<row ; i++ ) {
            try {
                MessageHeader m = messageHeaderLists.get(i);
                PSTMessage mapValue = getMessage(m.getID());
                String content = mapValue.getBodyHTML();

                Document doc = FileDocument.documentMessage(m, mapValue, content);
                writer.addDocument(doc);
            }
            catch(PSTException e){
                logger.log(Level.SEVERE, "Uncaught exception", e);
            }
            catch(IOException e) {
                logger.log(Level.SEVERE, "Uncaught exception", e);
            }
        }

        logger.info("End of Parse PST File: " + path);
        return true;
    }

    private PSTMessage getMessage (long id)throws IOException, PSTException {
        return (PSTMessage) PSTObject.detectAndLoadPSTObject(pstFile, id);
    }
}
