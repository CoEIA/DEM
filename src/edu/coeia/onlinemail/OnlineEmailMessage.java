
package edu.coeia.onlinemail ;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.Normalizer;
import java.util.ArrayList;

import java.util.List;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeUtility;

/**
 * Email Message - Represent Email Message
 * with from, subject, body, sent and receive date
 * @Author: Wajdy Essam
 * @Author: Ahmed Tolba
 * @version 2.0 19-10-2011
 */

public class OnlineEmailMessage {
	
	/**
	 * static factory that return new instance of email message
	 * @param id message id
	 * @param from the person who send the message
	 * @param subjet the subject of the message
	 * @param body the body of the message
	 * @param send the date that the message was sending
	 * @param receive the date that the message was receiving to distination
	 * @return new instance of EmailMessage
	 */
	public static OnlineEmailMessage newInstance(int id, String from,List<String> bcc, List<String> cc, String subject, String body, 
		String sent, String receive, List<String> path) {
			
		return new OnlineEmailMessage(id, from, bcc,cc,subject, body, sent, receive,path);
	}
	
	/**
	 * private constructor to construct email message
	 */
	public OnlineEmailMessage (int id, String from,List<String> bcc, List<String> cc,String subject, String body, String sent, String receive, List<String> path) {
		this.id = id;
		this.from = from;
                this.bcc  = bcc;
                this.cc   = cc;
		this.subject = subject ;
		this.body = body;
		this.sentDate = sent;
		this.receiveDate = receive;
                this.attachments = path;
	}
        
        
  
	/**
	 * Return brief description of email message, the exact detials of this representation
	 * are unspecified and subject to change.
	 */
	 
	@Override
	public String toString() {
		return this.getClass().getName() + "[id=" + id + ", form=" + from + ", subject=" + subject +
			", body=" + body + ", sentDate=" + sentDate + ", receiveDate=" + receiveDate;
	}
	
	public int getId() { return this.id; }
	public String getFrom() { return this.from; }
	public String getSubject() { return this.subject; }
	public String getBody() { return this.body; }
	public String getSentDate() { return this.sentDate ; }
	public String getReceiveDate() { return this.receiveDate; }
        public List<String> getCC() { return this.cc; }
        public List<String> getBCC() { return this.bcc; }
        public List<String> getAttachments() { return this.attachments; }
     
	private final int id;
	private final String from;
	private final String subject;
	private final String body;
        private final List<String> cc; 
        private final List<String> bcc; 
        private final List<String> attachments;
     	private final String sentDate, receiveDate;
}