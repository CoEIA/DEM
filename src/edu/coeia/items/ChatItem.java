/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.items;

/**
 *
 * @author wajdyessam
 */
public final class ChatItem extends Item {
    public ChatItem (final int documentId, final int parentId, final String documentHash, final String description,
            final String auther, final String partner, final String date, final String messageText,
            final String agent, final String path, final String length, final String chatMessagePath) {
        
        super(documentId, parentId, documentHash, description);
        
        this.from = auther ;
        this.to = partner ;
        this.date = date ;
        this.messageText = messageText ;
        this.chatAgent = agent;
        this.chatFilePath = path;
        this.chatLength = length;
        this.chatMessagePath = chatMessagePath;
    }
                
    @Override
    public Object[] getDisplayData() {
        Object[] object = new Object[] {this.documentId, this.from, this.date, this.documentDescription, this.to};
        return object;
    }
       
    public String getFrom() { return this.from ; }
    public String getTo() { return this.to ; }
    public String getDate() { return this.date ; }
    public String getMessageText() { return this.messageText ;}
    public String getChatAgent() { return this.chatAgent ; }
    public String getChatFilePath() { return this.chatFilePath; }
    public String getChatLength() { return this.chatLength; }
    public String getChatMessagePath() { return this.chatMessagePath ; }
    
    private final String from;
    private final String to;
    private final String date;
    private final String messageText ;
    private final String chatAgent;
    private final String chatFilePath;
    private final String chatLength;
    private final String chatMessagePath;
}
