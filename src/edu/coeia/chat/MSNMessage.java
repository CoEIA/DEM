/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.chat;

/**
 *
 * MSN Message - represent the structure of MSN history file
 * 
 * @author wajdyessam
 */

public class MSNMessage extends ChatMessage{
    public static MSNMessage newInstance(final String date, final String time,
            final String dateTime, final String sessionId, final String from,
            final String to, final String message) {

        return new MSNMessage(date, time, dateTime, sessionId,
                from, to, message);
    }

    private MSNMessage(final String date, final String time,
            final String dateTime, final String sessionId, final String from,
            final String to, final String message) {

        super(from, to, dateTime, message);
        
        this.date = date;
        this.time = time;
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return String.format("%s[date=%s, time=%s, sessionId=%s, from=%s, to=%s, message=%s]",
                this.getClass().getName(), this.date, this.time, this.sessionId,
                this.getFrom(), this.getTo(), this.getMessage());
    }

    public String getDate() {
        return this.date;
    }

    public String getTime() {
        return this.time;
    }

    public String sessionId() {
        return this.sessionId;
    }

    
    private final String date;
    private final String time;
    private final String sessionId;
}
