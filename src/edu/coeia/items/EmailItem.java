/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.items;

import edu.coeia.constants.IndexingConstant;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author wajdyessam
 */
public final class EmailItem extends Item{
    
    public EmailItem(final int documentId, final int documentParentId, final String hash,final String description,
            final String from, final String to, final String subject, 
            final String time, final String folder, final boolean hasAttachment,
            final String emailSource, final String content, final String header,
            final String cc, final String bcc, final String plainContent) {
        
        super(documentId, documentParentId, hash, description);
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.time = time;
        this.id = documentId;
        this.folder = folder;
        this.hasAttachment = hasAttachment;
        this.emailSource = emailSource;
        this.htmlContent = content;
        this.header = header;
        this.cc = cc;
        this.bcc = bcc;
        this.plainContent = plainContent;
    }
    
    public String getFrom() { return this.from ; }
    public String getTo() { return to; }
    public String getSubject() { return this.subject; }
    public String getTime() { return this.time; }
    public String getFolder() { return this.folder;}
    public boolean hasAttachment() { return this.hasAttachment; }
    public int getID() { return this.id ; }
    public String getEmailSource() { return this.emailSource ; }
    public String getHTMLContent() { return this.htmlContent ; }
    public String getPlainContent() { return this.plainContent; }
    public String getCC() { return this.cc ; }
    public String getBCC() { return this.bcc; }
    public String getHeader() { return this.header; }
          
    @Override
    public Object[] getDisplayData() {
        Object[] object = new Object[] {this.documentId, this.subject, this.time, this.getLabel(), this.folder};   
        return object;
    }
    
    public Object[] getFullDisplayData() {
        Object[] data = new Object[] { 
            this.documentId,
            this.subject,
            this.from,
            this.to,
            this.folder,
            this.time,
            this.hasAttachment
        };
        return data;
    }
    
    private JLabel getLabel() {
        ImageIcon icon  = null;
        
        if ( this.documentDescription.equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_DESCRIPTION_TYPE.EMAIL_MESSAGE)) )
            icon = new ImageIcon(getClass().getResource("/edu/coeia/main/resources/email_16.png"));
        else if ( this.documentDescription.equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_DESCRIPTION_TYPE.EMAIL_ATTACHMENT)) ) 
            icon = new ImageIcon(getClass().getResource("/edu/coeia/main/resources/attachment_16.png"));
        
        return new JLabel(this.documentDescription, icon,SwingConstants.LEFT);
    }
        
    private final String from;
    private final String to;
    private final String subject;
    private final String time;
    private final String folder;
    private final boolean hasAttachment;
    private final int id;
    private final String emailSource;
    private final String htmlContent;
    private final String plainContent;
    private final String cc;
    private final String bcc;
    private final String header;
}
