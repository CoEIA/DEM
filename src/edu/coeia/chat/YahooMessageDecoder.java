/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.chat;

/**
 *
 * @author wajdyessam
 */

public final class YahooMessageDecoder {
    /*
     * decode yahoo chat message with the key
     * 
     * @param cipher is the cipher text
     * @parem key is encryption key
     * @return byte array contain the clear text
     */
    public static byte[] decode (final byte[] cipher, final String key) {
        assert ( key != null ) : "key must not be null value" ;
        
        final byte[] plain = new byte[cipher.length];

        for (int i=0 ; i<cipher.length ; i++){
            int cipherInt = cipher[i];
            int keyInt = key.charAt(i % key.length());
            int resultInt = cipherInt ^ keyInt ;

            plain[i] = (byte) resultInt ;
        }

        return plain ;
    }
}