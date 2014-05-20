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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author wajdyessam
 */
public class ChatSession {
    public static ChatSession newInstance(final String fromName, final String toName,
            final String chatFilePath, final List<ChatMessage> msgs) {

        return new ChatSession(fromName, toName, chatFilePath, msgs);
    }

    private ChatSession(final String fromName, final String toName,
            final String chatFilePath, final List<ChatMessage> msgs) {

        this.fromUser = fromName;
        this.toUser = toName;
        this.filePath = chatFilePath;
        this.conversations = new ArrayList<ChatMessage>();
        this.conversations.addAll(Collections.unmodifiableList(msgs));
    }

    @Override
    public String toString() {
        return String.format("%s\n[There are %d Conversation Between: %s and %s]\nStored in this location: %s",
                this.getClass().getName(), this.conversations.size(), this.fromUser, this.toUser, this.filePath);
    }

    public String getUserName() {
        return this.fromUser;
    }

    public String getOtherName() {
        return this.toUser;
    }

    public String getPath() {
        return this.filePath;
    }

    public List<ChatMessage> getConversations() {
        return Collections.unmodifiableList(this.conversations);
    }

    private final String fromUser;
    private final String toUser;
    private final String filePath;
    private final List<ChatMessage> conversations;
}
