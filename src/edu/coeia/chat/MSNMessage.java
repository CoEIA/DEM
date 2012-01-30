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

        this.date = date;
        this.time = time;
        this.dateTime = dateTime;
        this.sessionId = sessionId;
        this.from = from;
        this.to = to;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("%s[date=%s, time=%s, sessionId=%s, from=%s, to=%s, message=%s]",
                this.getClass().getName(), this.date, this.time, this.sessionId,
                this.from, this.to, this.message);
    }

    public String getDate() {
        return this.date;
    }

    public String getTime() {
        return this.time;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public String sessionId() {
        return this.sessionId;
    }

    public String getTo() {
        return this.to;
    }

    public String getFrom() {
        return this.from;
    }

    public String getMessage() {
        return this.message;
    }
    private final String date;
    private final String time;
    private final String dateTime;
    private final String sessionId;
    private final String from;
    private final String to;
    private final String message;
}
