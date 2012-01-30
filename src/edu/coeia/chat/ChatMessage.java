/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.chat;

/**
 *
 * @author wajdyessam
 */
public class ChatMessage {
    private final String from;
    private final String to;
    private final String date; 
    private final String message ;

    public ChatMessage (final String from, final String to,
            final String date, final String message) {
        this.from = from ;
        this.to = to ;
        this.date =  date ;
        this.message = message ;
    }

    public String getFrom () { return this.from ; }
    public String getTo () { return this.to ; }
    public String getDate () { return this.date ; }
    public String getMessage () { return this.message ;}
}
