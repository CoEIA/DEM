/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.chat;

/**
 *
 * @author wajdyessam
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.io.IOException;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element; 
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

public final class MSNMessageReader {

    /*
     * Gel all chat Session in MSN folder
     * extract all MSN messages in xml file
     *
     * @param path the path to XML that contain MSN chat
     * @return MSNChatSession contain all chat sessions in path
     */
    public MSNChatSession processFile(final File path) throws ParserConfigurationException,
            SAXException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(path.getAbsolutePath());

        String fromUser = path.getParentFile().getParentFile().getName();
        String toUser = path.getName();

        return MSNChatSession.newInstance(fromUser, toUser, path.getAbsolutePath(), this.parseFile(document));
    }

    private List<MSNMessage> parseFile(final Document document) {
        List<MSNMessage> messages = new ArrayList<MSNMessage>();
        Element documentElement = document.getDocumentElement();

        NodeList nodeList = documentElement.getElementsByTagName(MSN_ROOT_ELEMENT);
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                MSNMessage msg = getMessage(element);
                messages.add(msg);
            }
        }

        return messages;
    }

    private MSNMessage getMessage(final Element element) {
        String date = element.getAttribute(MSN_DATE_ATTRIBUTE);
        String time = element.getAttribute(MSN_TIME_ATTRIBUTE);
        String dateTime = element.getAttribute(MSN_DATE_TIME_ATTRIBUTE);
        String sessionId = element.getAttribute(MSN_SESSION_ID_ATTRIBUTE);
        String from = getUser(element, MSN_FROM_ELEMENT);
        String to = getUser(element, MSN_TO_ELEMENT);
        String message = getTextMessage(element);

        return MSNMessage.newInstance(date, time, dateTime, sessionId, from, to, message);
    }

    private String getUser(final Element element, final String destinationTag) {
        StringBuilder from = new StringBuilder();

        NodeList nodeList = element.getElementsByTagName(destinationTag);
        Element nodeElement = (Element) nodeList.item(0);

        NodeList userList = nodeElement.getElementsByTagName(MSN_USER_ELEMENT);
        if (userList != null && userList.getLength() > 0) {
            Element userElement = (Element) userList.item(0);
            from.append(userElement.getAttribute(MSN_FRIENDLY_NAME_ATTRIBUTE));
        }

        return from.toString();
    }

    private String getTextMessage(final Element element) {
        StringBuilder textMessage = new StringBuilder();

        NodeList nodeList = element.getElementsByTagName(MSN_TEXT_ELEMENT);
        if (nodeList != null && nodeList.getLength() > 0) {
            Element textElement = (Element) nodeList.item(0);
            textMessage.append(textElement.getFirstChild().getNodeValue());
        }

        return textMessage.toString();
    }

    /*
     * MSN Chat Sessions
     * contain list of all conversation between 2 user
     */
    public final static class MSNChatSession {

        public static MSNChatSession newInstance(final String fromName, final String toName,
                final String chatFilePath, final List<MSNMessage> msgs) {

            return new MSNChatSession(fromName, toName, chatFilePath, msgs);
        }

        private MSNChatSession(final String fromName, final String toName,
                final String chatFilePath, final List<MSNMessage> msgs) {

            this.userName = fromName;
            this.otherName = toName;
            this.path = chatFilePath;
            this.conversations = new ArrayList<MSNMessage>();
            this.conversations.addAll(Collections.unmodifiableList(msgs));
        }

        @Override
        public String toString() {
            return String.format("%s\n[There are %d Conversation Between: %s and %s]\nStored in this location: %s",
                    this.getClass().getName(), this.conversations.size(), this.userName, this.otherName, this.path);
        }

        public String getUserName() {
            return this.userName;
        }

        public String getOtherName() {
            return this.otherName;
        }

        public String getPath() {
            return this.path;
        }

        public List<MSNMessage> getConversations() {
            return Collections.unmodifiableList(this.conversations);
        }
        private final String userName;
        private final String otherName;
        private final String path;
        private final List<MSNMessage> conversations;
    }

    /**
     *  MSN Message - represent the structure of MSN history file
     */
    public final static class MSNMessage {

        public static MSNMessage newInstance(final String date, final String time,
                final String dateTime, final String sessionId, final String from,
                final String to, final String message) {

            return new MSNMessage(date, time, dateTime, sessionId,
                    from, to, message);
        }

        private MSNMessage(final String date, final String time,
                final String dateTime, final String sessionId, final String from,
                final String to, final String message) {

            this.date = date;
            this.time = time;
            this.dateTime = dateTime;
            this.sessionId = sessionId;
            this.from = from;
            this.to = to;
            this.message = message;
        }

        @Override
        public String toString() {
            return String.format("%s[date=%s, time=%s, sessionId=%s, from=%s, to=%s, message=%s]",
                    this.getClass().getName(), this.date, this.time, this.sessionId,
                    this.from, this.to, this.message);
        }

        public String getDate() {
            return this.date;
        }

        public String getTime() {
            return this.time;
        }

        public String getDateTime() {
            return this.dateTime;
        }

        public String sessionId() {
            return this.sessionId;
        }

        public String getTo() {
            return this.to;
        }

        public String getFrom() {
            return this.from;
        }

        public String getMessage() {
            return this.message;
        }
        private final String date;
        private final String time;
        private final String dateTime;
        private final String sessionId;
        private final String from;
        private final String to;
        private final String message;
    }
    
    // MSN Specific XML element and attributes
    private final String MSN_ROOT_ELEMENT = "Message";
    private final String MSN_DATE_ATTRIBUTE = "Date";
    private final String MSN_TIME_ATTRIBUTE = "Time";
    private final String MSN_DATE_TIME_ATTRIBUTE = "DateTime";
    private final String MSN_SESSION_ID_ATTRIBUTE = "SessionID";
    private final String MSN_FROM_ELEMENT = "From";
    private final String MSN_TO_ELEMENT = "To";
    private final String MSN_USER_ELEMENT = "User";
    private final String MSN_FRIENDLY_NAME_ATTRIBUTE = "FriendlyName";
    private final String MSN_TEXT_ELEMENT = "Text";
}
