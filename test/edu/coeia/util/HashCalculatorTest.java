/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.util;

/**
 *
 * @author wajdyessam
 */

import org.junit.Test;
import static org.junit.Assert.* ;

public class HashCalculatorTest{
    protected static final String SOURCE_PATH = "C:\\data\\" ;
        
    @Test
    public void fileHashTest() {
        assertEquals("a9206cd7ce05334bbc2a39205a5ad51d".toUpperCase(), HashCalculator.calculateFileHash(SOURCE_PATH + "data.zip"));
        assertEquals("01cb3a9dd21d0cb17f3c97f5478e1678".toUpperCase(), HashCalculator.calculateFileHash(SOURCE_PATH + "a.pdf"));
    }
    
    @Test(expected=NullPointerException.class)
    public void notValidFileHashTest() {
        assertEquals("a9206cd7ce05334bbc2a39205a5ad51d", HashCalculator.calculateFileHash(null));
    }
    
    @Test
    public void directoryHashTest() {
    }
    
    @Test
    public void notValidDirectoryHashTest() {
    }
}
