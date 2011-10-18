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
import java.io.FileInputStream ;
import java.io.DataInputStream ;
import java.io.IOException ;
import java.io.EOFException ;

import java.util.Date ;
import java.util.ArrayList ;
import java.util.Collections;
import java.util.HashMap ;
import java.util.List;
import java.util.Map ;

public final class MSNMessageReader {
    
    /**
     * List of all MSNChatSession founded will be stored here
     */
    private static List<MSNChatSession> sessions;
    
    /*
     * MSN Chat Sessions
     * contain list of all conversation between 2 user
     */
    public static class MSNChatSession {
        public String userName ;
        public String otherName ;
        
        public List<MSNConversation> conversations = new ArrayList<MSNConversation>();
    }
    
    /**
     *  MSN Message
     */
    public final static class MSNMessage {
        
    }
    
    /*
     * MSNConversation - list of all MSN message between 2 accounts
     * any conversation between 2 account will be consists of multi MSNMessage
     * each MSNMessage represent one record in the chat (from, to , time, txt)
     */
    public static class MSNConversation {
        public String path ;
        public List<MSNMessage> messages = new ArrayList<MSNMessage>();
    }
    
    /*
     * Gel all chat Session in MSN folder
     *
     * @param path the path to some folder that may contain MSN chat
     * @throws NullPointerException when path is null 
     * @return List of MSNChatSession contain all chat sessions in path
     */
    public static List<MSNChatSession> getAllMSNChatSession(String path) {
        if ( ! isValidMSNPath(path) )
            return Collections.emptyList();
        
        sessions = new ArrayList<MSNChatSession>();
        
        try {
            File dir = new File(path);
            traverseDir(dir);
        }
        catch(IOException e) {
            
        }
        
        return sessions;
    }
        
    /**
     * Test if the path is valid MSN path
     * @param path to chat profile
     * @return true if path is correct and false if not
     */
    public static boolean isValidMSNPath(String path) {
        if ( path.contains("My Documents\\My Received Files") )
            return true;
        
        return false;
    }
    
    /*
     * Recursive Method to traverse Dir to extract MSN chat message
     */
    private static void traverseDir (File dir) throws IOException {
        if ( dir.isDirectory() ) {
            File[] files = dir.listFiles();

            for ( File file: files)
                traverseDir( file );
        }
        else {
             extractMSNMessage(dir);
        }
    }

    /*
     * extract all MSN messages in .DAT file
     */
    private static void extractMSNMessage (File path) throws IOException {
        String currentUserName = path.getParentFile().getParentFile().getName() ;
        String otherUserName   = path.getName();
        
        MSNConversation msg = getMSNMessages(path.getAbsolutePath(), currentUserName, otherUserName);
        
        MSNChatSession chat = new MSNChatSession();
        chat.conversations.add(msg);
        chat.userName = currentUserName;
        chat.otherName = otherUserName;
        
        sessions.add(chat);
    }
    
    private static MSNConversation getMSNMessages(String path, String userName, String otherName) {
        MSNConversation conversation = new MSNConversation();
        
        
        
        return conversation;
    }
}
