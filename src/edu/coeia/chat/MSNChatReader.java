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

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element; 
import org.w3c.dom.NodeList;

public final class MSNChatReader implements ChatReader{

    /*
     * Gel all chat Session in MSN folder
     * extract all MSN messages in xml file
     *
     * @param path the path to XML that contain MSN chat
     * @return MSNChatSession contain all chat sessions in path
     */
    @Override
    public ChatSession processFile(final File path) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(path.getAbsolutePath());

        String fromUser = path.getParentFile().getParentFile().getName();
        String toUser = path.getName();

        return ChatSession.newInstance(fromUser, toUser, path.getAbsolutePath(), this.parseFile(document));
    }

    private List<ChatMessage> parseFile(final Document document) {
        List<ChatMessage> messages = new ArrayList<ChatMessage>();
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
