/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.email.util;

/**
 *
 * @author wajdyessam
 * 
 */


public class Message implements Comparable<Message> {

    public void setSenderName (String name) { senderName = name; }
    public void setReceiverName (String name) { receiverName = name ; }
    public void setDate (String d) { date = d ; }
    public void setNumberOfMessage(int num) { numberOfMessage = num ; }

    public String getSenderName () { return senderName ; }
    public String getReceiverName () { return receiverName ; }
    public String getDate () { return date ; }
    public int getNumberOfMessage () { return numberOfMessage ;}

    public int compareTo (Message msg) {
        return senderName.compareTo(msg.senderName);
    }

    private String senderName ;
    private String receiverName ;
    private String date ;
    private int numberOfMessage ;
}
