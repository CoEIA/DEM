/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.offlinemail;

/**
 *
 * @author wajdyessam
 */
public class MessageHeader {
    public MessageHeader (long id, String subject, String from, String to, String date, boolean attachment) {
        this.id = id;
        this.subject = subject ;
        this.from = from ;
        this.to = to ;
        this.date = date ;
        this.attachment = attachment ;
    }

    public long getID () { return this.id ; }
    public String getSubject () { return this.subject ; }
    public String getFrom () { return this.from ; }
    public String getTo () { return this.to; }
    public String getDate() { return this.date ; }
    public boolean hasAttachment () { return this.attachment ;}

    public String getLocation () { return this.location ; }
    public void setLocation (String location) { this.location = location ;}
    
    private long id;
    private String subject, from, to, date;
    private boolean attachment ;
    private String location ;
}
