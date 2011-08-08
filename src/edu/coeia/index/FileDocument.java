/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.index;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.email.MessageHeader ;

import java.io.File ;
import java.io.FileNotFoundException ;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field ;
import org.apache.lucene.document.DateTools ;

import com.pff.PSTMessage;

public class FileDocument {
    
    // provide lucene document for File formats (PDF, XML, ... etc)
    public static Document documentFile (File file, String text) throws FileNotFoundException{
        Document document = new Document();

        document.add(new Field("filename", file.getPath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field("filetitle", file.getName() , Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field("Modified", DateTools.timeToString(file.lastModified(), DateTools.Resolution.MINUTE),
                Field.Store.YES, Field.Index.NOT_ANALYZED));
        document.add(new Field("body", text, Field.Store.NO, Field.Index.ANALYZED));

        return (document);
    }

    // provide lucene document for images format (JPEG, PNG.. etc)
    public static Document documentImage (File file) {
        Document document = new Document();

        document.add(new Field("mime", file.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED) );

        return (document);
    }

    // provide lucene document for PST message (.pst file)
    public static Document documentMessage (MessageHeader messageHeader, PSTMessage pstMessage, String content) {
        Document doucment = new Document();

        // message header field into index
        doucment.add(new Field("mailid", String.valueOf(messageHeader.getID()), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doucment.add(new Field("location", messageHeader.getLocation(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doucment.add(new Field("mailtitle", pstMessage.getSubject(), Field.Store.YES, Field.Index.NOT_ANALYZED ));
        doucment.add(new Field("mailDate", messageHeader.getDate(), Field.Store.YES, Field.Index.NOT_ANALYZED ));
        doucment.add(new Field("mailTo", messageHeader.getTo(), Field.Store.YES, Field.Index.NOT_ANALYZED ));
        doucment.add(new Field("mailFrom", messageHeader.getFrom(), Field.Store.YES, Field.Index.NOT_ANALYZED ));
        doucment.add(new Field("mailHasAttachment", String.valueOf(messageHeader.hasAttachment()), Field.Store.YES, Field.Index.NOT_ANALYZED ));
        
        doucment.add(new Field("mailcontent", content, Field.Store.NO, Field.Index.ANALYZED));

        return doucment;
    }

    // prevent make object from this class
    private FileDocument () { }
}
