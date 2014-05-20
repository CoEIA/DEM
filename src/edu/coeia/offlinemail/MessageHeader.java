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
