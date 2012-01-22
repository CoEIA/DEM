/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.chat;

import edu.coeia.util.Tuple;

import java.util.List ;

import org.junit.Test;


/**
 *
 * @author wajdyessam
 */
public class ChatSessionDetectionTest {
    
    @Test
    public void skypeChatDetectionTest() throws Exception{
        SkypeMessageReader parser = new SkypeMessageReader();
        String path = "C:\\Documents and Settings\\wajdyessam\\Application Data\\Skype\\wajdyessam";
        List<Tuple<String, List<SkypeMessage>>> msgs = parser.parseSkypeFile(path);
        
        for (Tuple<String, List<SkypeMessage>> user: msgs) {
            System.out.println("User: " + user.getA());
            for (SkypeMessage msg: user.getB()) {
                System.out.println("Message: " + msg.getMessageText() + " with: " + msg.getPartner());
            }
        }
    }
}
