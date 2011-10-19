/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.util;


/**
 * Utility class for common argument validations
 * 
 * @author wajdyessam
 */

public final class PreconditionsChecker {
    
    /**
     * prevent making object from this class
     */
    private PreconditionsChecker() {
        // prevent even from reflection code
        throw new AssertionError();
    }
    
    
    /**
     * if object is null then throws NullPointerException
     */
    public static <T> T checkNull(String message, T object) {
        if ( object == null ) {
            throw new NullPointerException(message);
        }
        
        return object;
    }
    
    /*
     * if string must be not-empty string
     * if empty string then throws IllegalArgumentException
     */
    public static String checkNotEmptyString(String message, String object) {
        if ( object.isEmpty() ) {
            throw new IllegalArgumentException(message);
        }
        
        return object;
    }
}
