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

public final class YahooMessageReader {

    /**
     * List of all YahooChatSession founded will be stored here
     */
    private List<YahooChatSession> sessions;
    
    /**
     * contain the path of .DAT file
     */
    private String chatPath ;
    
    /*
     * Yahoo Chat Sessions
     * contain list of all conversation between 2 user
     */
    public static class YahooChatSession {
        public String userName ;
        public String otherName ;
        
        public List<YahooConversation> conversations = new ArrayList<YahooConversation>();
    }
    
    /*
     * YahooConversation - list of all yahoo message between 2 accounts
     * any conversation between 2 account will be consists of multi YahooMessage
     * each YahooMessage represent one record in the chat (from, to , time, txt)
     */
    public static class YahooConversation {
        public String path ;
        public List<YahooMessage> messages = new ArrayList<YahooMessage>();
    }
    
    /*
     * Gel all chat Session in yahoo folder
     *
     * @param path the path to some folder that may contain yahoo chat
     * @throws NullPointerException when path is null 
     * @return List of YahooChatSession contain all chat sessions in path
     */
    public List<YahooChatSession> getAllYahooChatSession(String path) throws IOException{
        if ( ! isValidYahooPath(path) )
            return Collections.emptyList();
        
        sessions = new ArrayList<YahooChatSession>();
        
        File dir = new File(path);
        
        traverseDir(dir);

        return sessions;
    }
    
    /*
     * Return .DAT chat path
     */
    public String getChatPath () {
        return this.chatPath ;
    }
        
    /**
     * Test if the path is valid Yahoo path
     * @param path to chat profile
     * @return true if path is correct and false if not
     */
    public static boolean isValidYahooPath(String path) {
        if ( path.contains("Program Files\\Yahoo!\\Messenger\\Profiles") )
            return true;
        
        return false;
    }
    
    /*
     * Recursive Method to traverse Dir to extract yahoo chat message
     */
    private void traverseDir (File dir) throws IOException {
        if ( dir.isDirectory() ) {
            File[] files = dir.listFiles();

            for ( File file: files)
                traverseDir( file );
        }
        else {
            this.chatPath = dir.getAbsolutePath();
             extractYahooMessage(dir);
        }
    }

    /*
     * extract all yahoo messages in .DAT file
     */
    private void extractYahooMessage (File path) throws IOException {
        String currentUserName = path.getParentFile().getParentFile().getParentFile().getParentFile().getName() ;
        String otherUserName   = path.getParentFile().getName();
        YahooConversation msg = getYahooMessages(path.getAbsolutePath(), currentUserName, otherUserName);
        
        YahooChatSession chat = new YahooChatSession();
        chat.conversations.add(msg);
        chat.userName = currentUserName;
        chat.otherName = otherUserName;
        
        sessions.add(chat);
    }

    /*
     * Get all yahoo chat messages between profileName user and otherName user
     * 
     * @param path is the path to yahoo chat file .DAT file
     * @param profileName is the folder name for current account
     * @param otherName is subfolder of profileName that contain chat between current and other
     * @return List of YahooMesssage that contain yahoo messages
     * @throws IOException if the path/.DAT file is not found
     */
     private YahooConversation getYahooMessages (String path, String profileName,
        String otherName ) throws IOException {

        DataInputStream input = new DataInputStream(new FileInputStream(new File(path)) );
        List<YahooMessage> msgs = new ArrayList<YahooMessage>();

        try {
            while ( true ) {
                Date timeStamp = new Date( 1000 * ( (long) convertFromLEToBE(input.readInt()) ));
                int unknown1   = convertFromLEToBE(input.readInt());
                int sendFlag   = convertFromLEToBE(input.readInt());
                int msgLength  = convertFromLEToBE(input.readInt());
                
                byte[] cipherText = new byte[msgLength];
                input.readFully(cipherText);
                
                int unknown2   = convertFromLEToBE(input.readInt());

                YahooMessage yahooMsg = null ;
                if ( sendFlag == 0 ) {
                    yahooMsg = new YahooMessage(
                        profileName, otherName, timeStamp, unknown1, YahooMessage.MESSAGE_PATH.SOURCE_TO_DEST,
                        msgLength, cipherText, unknown2);
                }
                else {
                    yahooMsg = new YahooMessage(
                        profileName, otherName, timeStamp, unknown1, YahooMessage.MESSAGE_PATH.DEST_TO_SOURCE,
                        msgLength, cipherText, unknown2);
                }

                msgs.add( yahooMsg );
            }
        }
        catch (EOFException e) {
        }
        finally{
            input.close();
        }

        YahooConversation con = new YahooConversation();
        con.messages.addAll(msgs);
        con.path = path;
        
        return con;
    }

    // from http://www.devx.com/tips/Tip/34353
    // convert little endian to big endain
    private static int convertFromLEToBE (int i) {
        return((i&0xff)<<24)+((i&0xff00)<<8)+((i&0xff0000)>>8)+((i>>24)&0xff);
    }
}
