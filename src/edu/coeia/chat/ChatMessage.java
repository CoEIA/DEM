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
 * @author wajdyessam
 */

public abstract class ChatMessage {
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
