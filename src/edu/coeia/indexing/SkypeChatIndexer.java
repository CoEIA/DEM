/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.chat.SkypeMessage;
import edu.coeia.chat.SkypeParser;
import edu.coeia.extractors.ImageExtractor;
import edu.coeia.util.Tuple;
import groovy.model.DefaultTableModel;
import java.io.File;
import java.util.List;

/**
 *
 * @author wajdyessam
 */
public class SkypeChatIndexer extends Indexer{
    
    /**
     *  chat type
     */
    private static final String CHAT_AGENT = "SKYPE" ;
    private int parentId ;
    
    public static SkypeChatIndexer newInstance(LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor) {
            
        return new SkypeChatIndexer(luceneIndex, file, mimeType, imageExtractor, 0);
    }
        
    private SkypeChatIndexer(LuceneIndex luceneIndex, File file, String mimeType, ImageExtractor imageExtractor,
            int parentId) {
        
        super(luceneIndex, file, mimeType, imageExtractor);
        this.parentId = parentId ;
    }
        
    @Override
    public boolean doIndexing() {
        boolean status = false;
        
        try {     
            SkypeParser parser = new SkypeParser();
            List<Tuple<String, List<SkypeMessage>>> msgs = parser.parseSkypeFile(this.getFile().getAbsolutePath());

            for (Tuple<String, List<SkypeMessage>> user: msgs) {
                System.out.println("User: " + user.getA());
                for (SkypeMessage msg: user.getB()) {
                    System.out.println("Message: " + msg.getMessageText() + " with: " + msg.getPartner());
                }
            }
            
            status = true;
        }
        catch(Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
        
        return status;
    }
}
