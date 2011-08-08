/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.chat;

/**
 *
 * @author wajdyessam
 */


import java.util.Date ;

public class YahooMessage {

    public static enum MESSAGE_PATH {
        SOURCE_TO_DEST, DEST_TO_SOURCE
    }

    private String profileName ;
    private String destinationName ;

    private Date timeStamp ;
    private int unknown1 ;
    private MESSAGE_PATH path ;
    private int messageLength ;
    private byte[] cipherText ;
    private int unknown2 ;

    private String plainText ;

    public YahooMessage (String pn, String dn, Date ts, int un1, MESSAGE_PATH path, int len, byte[] ct, int un2) {
        this.profileName = pn;
        this.destinationName = dn;
        this.timeStamp = ts ;
        this.unknown1 = un1 ;
        this.path = path ;
        this.messageLength = len ;
        this.cipherText = ct ;
        this.unknown2 = un2 ;
    }

    public String getProfileName () { return this.profileName ; }
    public String getDestinationName () { return this.destinationName ; }

    public Date getTimeStamp () { return this.timeStamp ; }
    public int getUnkown1 () { return this.unknown1 ; }
    public MESSAGE_PATH getMessagePath () { return this.path ; }
    public int getMessageLength () { return this.messageLength ; }
    public byte[] getCipherText() { return this.cipherText; }
    public int getUnknown2 () { return this.unknown2; }

    public String getPlainText() { return this.plainText ; }
    public void setPlainText(String text) { this.plainText = text ; }
}
