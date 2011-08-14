/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.detector;

import edu.coeia.cases.detector.OutlookDetector;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List; 

/**
 *
 * @author wajdyessam
 */
public class OutlookDetectorTest {
    private OutlookDetector detector ;
    
    @Before
    public void init() {
        detector = new OutlookDetector();
    }
    
    @Test
    public void getNumberOfOutlookFiles() {
        List<String> files = detector.getFiles();
        assertEquals(files.size(), 3);
    }
}
