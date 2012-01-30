/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.chat;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.util.DateUtil;

import java.io.UnsupportedEncodingException;

import java.util.Date ;

/**
 * YahooMessge to represent Yahoo Chat Message between two account
 * YahooMessage is Immutable class , since there is no way to change its after constructor
 */
public final class YahooMessage extends ChatMessage{

    public static enum MESSAGE_PATH { 
        SOURCE_TO_DEST, DEST_TO_SOURCE
    }

    private final String profileName ;
    private final String destinationName ;
    private final Date timeStamp ;
    private final int unknown1 ;
    private final MESSAGE_PATH path ;
    private final int messageLength ;
    private final byte[] cipherText ;
    private final int unknown2 ;

    public YahooMessage (final String pn, final String dn, final Date ts,
            final int un1, final MESSAGE_PATH path, final int len, final byte[] ct, final int un2) {
        
        this.profileName = pn;
        this.destinationName = dn;
        this.timeStamp = new Date(ts.getTime());    // defense copy
        this.unknown1 = un1 ;
        this.path = path ;
        this.messageLength = len ;
        this.cipherText = ct ;
        this.unknown2 = un2 ;
    }

    public String getProfileName () { return this.profileName ; }
    public String getDestinationName () { return this.destinationName ; }

    public Date getTimeStamp () { return new Date(this.timeStamp.getTime()) ; } // return defense copy of date field
    public int getUnkown1 () { return this.unknown1 ; }
    public MESSAGE_PATH getMessagePath () { return this.path ; }
    public int getMessageLength () { return this.messageLength ; }
    public byte[] getCipherText() { return this.cipherText; }
    public int getUnknown2 () { return this.unknown2; }
    
    /**
     * Intended only for debugging
     * @return string representation of YahooMessage class
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        
        String from = this.profileName ;
        String to   = this.destinationName ;
        
        if ( this.getMessagePath() == YahooMessage.MESSAGE_PATH.SOURCE_TO_DEST) {
            from = this.profileName;
            to = this.destinationName;
        }
        else {
            from = this.destinationName;
            to = this.profileName;
        }
                
        result.append("YahooMessage").append("[")
                .append("from: ").append(from)
                .append(", To:").append(to)
                .append(", Time: ").append(DateUtil.formatDateTime(this.getTimeStamp()));
        
        try {
            byte[] plainText  = YahooMessageDecoder.decode(this.cipherText,this.profileName);
            String plain = new String(plainText, "UTF-8");
            result.append(", Plain: ").append(plain);
        }
        catch(UnsupportedEncodingException e) {
        }
                        
        return result.toString();
    }
}
