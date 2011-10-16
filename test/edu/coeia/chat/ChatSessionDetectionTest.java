/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.chat;

import edu.coeia.detector.YahooDetector;
import java.util.List ;
import org.junit.Test;

/**
 *
 * @author wajdyessam
 */
public class ChatSessionDetectionTest {
    
    @Test
    public void yahooChatDetectionTest() {
        List<String> casePath = new YahooDetector().getFiles();
        
        for(String path: casePath) {
            System.out.println("casepath: " + path);
        }
        
//        
//        try {
//            assertEquals(null, YahooMessageReader.getInstance(casePath));
//        }
//        catch(Exception e) {
//            
//        }
    }
}
