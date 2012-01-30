/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.chat;

/**
 *
 * @author wajdyessam
 *
 */

public class SkypeMessage extends ChatMessage {
    private String auther, partner, date, messageText ;

    public SkypeMessage (String a, String p, String d, String m) {
        this.auther = a ;
        this.partner = p ;
        this.date =  d ;
        this.messageText = m ;
    }

    public void setAuther (String a){ this.auther = a ; }
    public void setPartner (String p) { this.partner = p ; }
    public void setDate (String d) { this.date = d ; }
    public void setMessageText (String m) { this.messageText = m ; }

    public String getAuther () { return this.auther ; }
    public String getPartner () { return this.partner ; }
    public String getDate () { return this.date ; }
    public String getMessageText () { return this.messageText ;}
    
}
