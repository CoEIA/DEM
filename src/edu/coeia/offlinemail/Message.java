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
