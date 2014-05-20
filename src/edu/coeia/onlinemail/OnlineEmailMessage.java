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
package edu.coeia.onlinemail;

import java.util.Collections;
import java.util.List;

/**
 * Email Message - Represent Email Message
 * with from, subject, body, sent and receive date
 * @Author: Wajdy Essam
 * @Author: Ahmed Tolba
 * @version 2.0 19-10-2011
 */

public final class OnlineEmailMessage {

    /**
     * static factory that return new instance of email message
     * @param id message id
     * @param from the person who send the message
     * @param subjet the subject of the message
     * @param body the body of the message
     * @param send the date that the message was sending
     * @param receive the date that the message was receiving to destination
     * @return new instance of EmailMessage
     */
    public static OnlineEmailMessage newInstance(int id, String Username, String from, List<String> to, 
            List<String> bcc, List<String> cc, String subject, String body,
            String sent, String receive, List<String> path,String FolderName) {

        return new OnlineEmailMessage(id,Username, from,to, bcc, cc, subject, body, sent, receive, path,FolderName);
    }

    /**
     * private constructor to construct email message
     */
    private OnlineEmailMessage(int id,String username, String from, List<String> to, 
            List<String> bcc, List<String> cc,
            String subject, String body, String sent, String receive, List<String> path,String FolderName) {
        
        this.id = id;
        this.username = username;
        this.from = from;
        this.bcc = bcc;
        this.cc = cc;
        this.subject = subject;
        this.body = body;
        this.sentDate = sent;
        this.receiveDate = receive;
        this.attachments = path;
        this.FolderName  = FolderName;
        this.to          = to;
    }

    /**
     * Return brief description of email message, the exact detials of this representation
     * are unspecified and subject to change.
     */
    @Override
    public String toString() {
        return this.getClass().getName() + "[id=" + id + ", form=" + from + ", subject=" + subject
                + ", body=" + body + ", sentDate=" + sentDate + ", receiveDate=" + receiveDate 
                + ", cc=" + cc.toString() + ", bcc=" + bcc.toString() + " ]\n" ;
    }

    public int getId() {
        return this.id;
    }
    public String getUsername()
    {
        return this.username;
    }

    public String getFrom() {
        return this.from;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getBody() {
        return this.body;
    }
    public  List<String> getTo()
    {
        return this.to;
    }

    public String getSentDate() {
        return this.sentDate;
    }

    public String getReceiveDate() {
        return this.receiveDate;
    }
    public String getFolderName(){
        return this.FolderName;
    }

    public List<String> getCC() {
        return Collections.unmodifiableList(this.cc);
    }

    public List<String> getBCC() {
        return Collections.unmodifiableList(this.bcc);
    }

    public List<String> getAttachments() {
        return Collections.unmodifiableList(this.attachments);
    }
    
    private final int id;
    private final String from;
    private final String username;
    private final String subject;
    private final String body;
    private final List<String> cc;
    private final List<String> bcc;
    private final List<String> attachments,to;
    private final String sentDate, receiveDate,FolderName;
}