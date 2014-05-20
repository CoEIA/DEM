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
package edu.coeia.items;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author wajdyessam
 */
public final class ChatItem extends Item {
    public ChatItem (final int documentId, final int parentId, final String documentHash, final String description,
            final String auther, final String partner, final String date, final String messageText,
            final String agent, final String path, final String length, final String chatMessagePath) {
        
        super(documentId, parentId, documentHash, description);
        
        this.from = auther ;
        this.to = partner ;
        this.date = date ;
        this.messageText = messageText ;
        this.chatAgent = agent;
        this.chatFilePath = path;
        this.chatLength = length;
        this.chatMessagePath = chatMessagePath;
    }
                
    @Override
    public Object[] getDisplayData() {
        Object[] object = new Object[] {this.documentId, this.from, this.date, 
            new JLabel(this.documentDescription, new javax.swing.ImageIcon(getClass().getResource("/edu/coeia/main/resources/chat_16.png")),
                    SwingConstants.LEFT),
            this.to};
        return object;
    }
       
    public String getFrom() { return this.from ; }
    public String getTo() { return this.to ; }
    public String getDate() { return this.date ; }
    public String getMessageText() { return this.messageText ;}
    public String getChatAgent() { return this.chatAgent ; }
    public String getChatFilePath() { return this.chatFilePath; }
    public String getChatLength() { return this.chatLength; }
    public String getChatMessagePath() { return this.chatMessagePath ; }
    
    private final String from;
    private final String to;
    private final String date;
    private final String messageText ;
    private final String chatAgent;
    private final String chatFilePath;
    private final String chatLength;
    private final String chatMessagePath;
}
