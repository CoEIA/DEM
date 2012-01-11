/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.items;

/**
 *
 * @author wajdyessam
 */
public final class EmailItem extends Item{
    
    public EmailItem(final int documentId, final int documentParentId,
            final String from, final String to, final String subject, 
            final String time, final String folder, final boolean hasAttachment) {
        
        super(documentId, documentParentId, "");
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.time = time;
        this.id = documentId;
        this.folder = folder;
        this.hasAttachment = hasAttachment;
    }
    
    public String getFrom() { return this.from ; }
    public String getTo() { return to; }
    public String getSubject() { return this.subject; }
    public String getTime() { return this.time; }
    public String getFolder() { return this.folder;}
    public boolean hasAttachment() { return this.hasAttachment; }
    public int getID() { return this.id ; }
    
    private final String from;
    private final String to;
    private final String subject;
    private final String time;
    private final String folder;
    private final boolean hasAttachment;
    private final int id;
}
