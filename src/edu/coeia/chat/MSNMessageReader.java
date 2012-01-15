/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.chat;

/**
 *
 * @author wajdyessam
 */

import java.io.File ;

import java.util.ArrayList ;
import java.util.List;

public final class MSNMessageReader {
    
    /*
     * MSN Chat Sessions
     * contain list of all conversation between 2 user
     */
    public static class MSNChatSession {
        public String userName ;
        public String otherName ;
        public String path ;
        public List<MSNMessage> conversations = new ArrayList<MSNMessage>();
    }
    
    /**
     *  MSN Message
     */
    public final static class MSNMessage {
        
    }
    

    /*
     * Gel all chat Session in MSN folder
     * extract all MSN messages in .DAT file
     *
     * @param path the path to some folder that may contain MSN chat
     * @throws NullPointerException when path is null 
     * @return List of MSNChatSession contain all chat sessions in path
     */
    public static MSNChatSession getMSNChatSession(File path) {
        String currentUserName = path.getParentFile().getParentFile().getName() ;
        String otherUserName   = path.getName();
        
        List<MSNMessage> msgs = getMSNMessages(path.getAbsolutePath(), currentUserName, otherUserName);
        
        MSNChatSession chat = new MSNChatSession();
        chat.conversations.addAll(msgs);
        chat.userName = currentUserName;
        chat.otherName = otherUserName;
        
        return chat;
    }
    
    private static List<MSNMessage> getMSNMessages(String path, String userName, String otherName) {
        List<MSNMessage> conversation = new ArrayList<MSNMessage>();
        return conversation;
    }
}
