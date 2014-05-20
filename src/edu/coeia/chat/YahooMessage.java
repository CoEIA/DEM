/* 
 * Copyright (C) 2014 Center of Excellence in Information Assurance
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

    public YahooMessage (final String from, final String to, final Date date,
            final int un1, final MESSAGE_PATH path, final int len, final byte[] ct, final int un2) throws UnsupportedEncodingException {
        
        super(from, to, DateUtil.formatDateTime(date), new String(YahooMessageDecoder.decode(ct,from), "UTF-8"));
        
        this.profileName = from;
        this.destinationName = to;
        this.timeStamp = new Date(date.getTime());    // defense copy
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
