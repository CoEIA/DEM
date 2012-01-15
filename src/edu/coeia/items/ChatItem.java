/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.items;

/**
 *
 * @author wajdyessam
 */
public class ChatItem extends Item {
    private final String from, to, date, messageText ;

    public ChatItem (final int documentId, final int parentId, final String documentHash, 
            final String auther, final String partner, final String date, final String messageText) {
        
        super(documentId, parentId, documentHash);
        this.from = auther ;
        this.to = partner ;
        this.date = date ;
        this.messageText = messageText ;
    }

    public String getFrom () { return this.from ; }
    public String getTo () { return this.to ; }
    public String getDate () { return this.date ; }
    public String getMessageText () { return this.messageText ;}
}
