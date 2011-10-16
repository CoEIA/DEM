/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.chat;

/**
 *
 * @author wajdyessam
 */

import java.io.File ;
import java.io.FileInputStream ;
import java.io.DataInputStream ;
import java.io.IOException ;
import java.io.EOFException ;

import java.util.Date ;
import java.util.ArrayList ;
import java.util.HashMap ;

class YahooMessageReader {

    private static HashMap<String, HashMap<String, ArrayList<ArrayList<YahooMessage>>>> map ;
    private static String path ;

    public static HashMap<String, HashMap<String, ArrayList<ArrayList<YahooMessage>>>> getInstance (String p)
    throws IOException {
        if ( map == null || ! path.equals(p)  ) {
            map = new HashMap<String, HashMap<String, ArrayList<ArrayList<YahooMessage>>>>();
            path = p ;
            readAllYahooMessages(path);
        }
        
        return map ;
    }

    private static void readAllYahooMessages (String p) throws IOException {
        File dir = new File(p);
        traverseDir(dir);
    }

    private static void traverseDir (File dir) throws IOException {
        if ( dir.isDirectory() ) {
            File[] files = dir.listFiles();

            for ( File file: files)
                traverseDir( file );
        }
        else {
            cacheYahooMessage(dir);
        }
    }

    private static void cacheYahooMessage( File path) throws IOException {
        String currentUserName = path.getParentFile().getParentFile().getParentFile().getParentFile().getName() ;
        String otherUserName   = path.getParentFile().getName();
        ArrayList<YahooMessage> msg = getYahooMessages(path.getAbsolutePath(), currentUserName, otherUserName);

        // update user profile
        if ( map.get(currentUserName) == null ) {
            ArrayList<ArrayList<YahooMessage>> msgs = new ArrayList<ArrayList<YahooMessage>>();
            msgs.add(msg);

            HashMap<String,ArrayList<ArrayList<YahooMessage> > > aMap = new
                HashMap<String,ArrayList<ArrayList<YahooMessage>>>() ;

            aMap.put(otherUserName, msgs);
            map.put(currentUserName, aMap);
        }
        else { // add new user profile

            // update other user chating list
            if (map.get(currentUserName).get(otherUserName) == null ) {
                ArrayList<ArrayList<YahooMessage>> msgs = new ArrayList<ArrayList<YahooMessage>>();
                msgs.add(msg);

                HashMap<String,ArrayList<ArrayList<YahooMessage>>> aMap = new
                    HashMap<String,ArrayList<ArrayList<YahooMessage>>>() ;

                aMap.put(otherUserName, msgs);
                map.put(currentUserName,aMap);
            }
            else { // add new user chatting list
                ArrayList<ArrayList<YahooMessage>> msgs = new ArrayList<ArrayList<YahooMessage>>();
                msgs.add(msg);

                HashMap<String,ArrayList<ArrayList<YahooMessage>>> aMap = new
                    HashMap<String,ArrayList<ArrayList<YahooMessage>>>() ;

                aMap.put(otherUserName, msgs);
                map.put(currentUserName,aMap);
            }
        }
    }

    private static ArrayList<YahooMessage> getYahooMessages (String path, String profileName,
        String otherName ) throws IOException {

        DataInputStream input = new DataInputStream(new FileInputStream(new File(path)) );
        ArrayList<YahooMessage> msgs = new ArrayList<YahooMessage>();

        try {
            while ( true ) {
                Date timeStamp = new Date( 1000 * ( (long) convertFromLEToBE(input.readInt()) ));
                int unknown1   = convertFromLEToBE(input.readInt());
                int sendFlag   = convertFromLEToBE(input.readInt());
                int msgLength  = convertFromLEToBE(input.readInt());
                
                byte[] cipherText = new byte[msgLength];
                input.readFully(cipherText);
                
                int unknown2   = convertFromLEToBE(input.readInt());

                YahooMessage yahooMsg = null ;
                if ( sendFlag == 0 ) {
                    yahooMsg = new YahooMessage(
                        profileName, otherName, timeStamp, unknown1, YahooMessage.MESSAGE_PATH.SOURCE_TO_DEST,
                        msgLength, cipherText, unknown2);
                }
                else {
                    yahooMsg = new YahooMessage(
                        profileName, otherName, timeStamp, unknown1, YahooMessage.MESSAGE_PATH.DEST_TO_SOURCE,
                        msgLength, cipherText, unknown2);
                }

                msgs.add( yahooMsg );
            }
        }
        catch (EOFException e) {
        }
        finally{
            input.close();
        }

        return msgs;
    }
    
    public static short twoBytesToChart(byte b1, byte b2) {
          return (short) ((b1 << 8) | (b2 & 0xFF));
    }

    // from http://www.devx.com/tips/Tip/34353
    // convert little endian to big endain
    private static int convertFromLEToBE (int i) {
        return((i&0xff)<<24)+((i&0xff00)<<8)+((i&0xff0000)>>8)+((i>>24)&0xff);
    }

    int convertFromLEToBE2Byte(int i) {
        return ((i>>8)&0xff)+((i << 8)&0xff00);
    }

    //http://www.exampledepot.com/egs/java.nio.charset/ConvertChar.html
}
