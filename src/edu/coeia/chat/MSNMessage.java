/* 
 * Copyright (C) 2014 Center of Excellence in Information Assurance
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

    @Override
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
