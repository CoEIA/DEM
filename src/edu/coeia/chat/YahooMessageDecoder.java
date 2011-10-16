/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.chat;

/**
 *
 * @author wajdyessam
 */

class YahooMessageDecoder {
    public static byte[] decode (byte[] cipher, String key) {
        byte[] plain = new byte[cipher.length];

        for (int i=0 ; i<cipher.length ; i++){
            int cipherInt = cipher[i];
            int keyInt = key.charAt(i % key.length());
            int resultInt = cipherInt ^ keyInt ;

            plain[i] = (byte) resultInt ;
        }

        return plain ;
    }
}