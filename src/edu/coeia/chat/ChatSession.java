/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
