/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.chat;

import edu.coeia.chat.YahooMessageReader.YahooConversation;
import edu.coeia.chat.YahooMessageReader.YahooChatSession;

import edu.coeia.detector.YahooDetector;

import java.util.ArrayList;
import java.util.List ;

import org.junit.Test;
import org.junit.Ignore ;


/**
 *
 * @author wajdyessam
 */
public class ChatSessionDetectionTest {
    
    @Test
    @Ignore
    public void printYahooChatMethod() {
        List<String> casePath = new YahooDetector().getFilesInCurrentSystem();
        
        for(String path: casePath) {
            List<YahooChatSession> sessions = YahooMessageReader.getAllYahooChatSession(path);
            
            for(YahooChatSession session: sessions) {
                System.out.println("Conversation Between : " + session.userName + " and: " + session.otherName);
                
                for(YahooConversation conversation: session.conversations) {
                    System.out.println("Conversation #");
                    
                    for(YahooMessage msg: conversation.messages) {
                        System.out.println(msg);
                    }
                }
            }
        }
    }
    
    @Test
    public void yahooChatDetectionTest1() {
        List<String> casePath = new ArrayList<String>();
        casePath.add("C:\\Program Files\\Yahoo!");
        
        for(String path: casePath) {
            List<YahooChatSession> sessions = YahooMessageReader.getAllYahooChatSession(path);
            
            for(YahooChatSession session: sessions) {
                System.out.println("Conversation Between : " + session.userName + " and: " + session.otherName);
                
                for(YahooConversation conversation: session.conversations) {
                    System.out.println("Conversation #");
                    
                    for(YahooMessage msg: conversation.messages) {
                        System.out.println(msg);
                    }
                }
            }
        }
    }
}
