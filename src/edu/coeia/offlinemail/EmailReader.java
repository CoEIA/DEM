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
package edu.coeia.offlinemail;

/**
 *
 * @author wajdyessam
 */

import java.util.Iterator ;
import java.util.ArrayList ;

import com.pff.PSTFolder ;
import com.pff.PSTMessage ;
import com.pff.PSTFile ;
import com.pff.PSTException;

import java.io.IOException ;

import edu.coeia.gutil.InfiniteProgressPanel ;

import edu.coeia.util.ApplicationLogging;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailReader {

    private static PSTFile pstFile ;
    private static ArrayList<MessageHeader> messageHeaderList ;
    private static String path ;

    private static InfiniteProgressPanel panel ;
    private static int counter = 0 ;

    private final static Logger logger = ApplicationLogging.getLogger();
    
    private static boolean status = false;
    
    public static ArrayList<MessageHeader> getInstance(PSTFile pst, String p, InfiniteProgressPanel pnl) {    
        if ( messageHeaderList == null || ! path.equals(p)) {
            messageHeaderList = new ArrayList<MessageHeader> ();
            pstFile = pst ;
            path = p ;
            panel = pnl ;
            counter = 0;
            
            logger.info("EmailReader Constructor");

            caching();
        }

        if ( status == false) {
            logger.log(Level.INFO, "Cannot caching email");
            return null;
        }

        logger.log(Level.INFO, "Messaege Header List size is: " + messageHeaderList.size());
        return messageHeaderList ;
    }
    
    private static void caching () {
        try {
            PSTFolder root = pstFile.getRootFolder() ;
            status = writeToCache(root);
            logger.log(Level.INFO, "Finisch Caching Email");
        }
        catch (PSTException e) {
            logger.log(Level.SEVERE, "Uncaught exception", e);
            status = false;
        }
        catch (IOException ex){
            logger.log(Level.SEVERE, "Uncaught exception", ex);
            status = false;
        }
    }

    private static boolean writeToCache (PSTFolder mainFolder) {
        boolean statusTmp = false;
        
        try{
            Iterator<PSTFolder> itr = mainFolder.getSubFolders().iterator();
            while ( itr.hasNext() ){
                PSTFolder folder = itr.next();

                if ( folder.getSubFolders().size() > 0 && folder.hasSubfolders() ) {
                    if ( folder.getContentCount() > 0 )
                        messageHeaderList.addAll(getFolderContent(folder));
                    
                    writeToCache(folder);
                }
                else {
                    messageHeaderList.addAll(getFolderContent(folder));
                }
            }

            statusTmp = true;
        }
        catch (PSTException e) {
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }
        catch (IOException ex){
            logger.log(Level.SEVERE, "Uncaught exception", ex);
        }

        return (statusTmp);
    }

    private static ArrayList<MessageHeader> getFolderContent (PSTFolder folder){
        ArrayList<MessageHeader> headers = new ArrayList<MessageHeader>();

        String name =  folder.getDisplayName() ;

        try {
            if ( folder.getContentCount() > 0) {
                try {
                    for (int i=0 ; i<folder.getContentCount() ; i++) {
                        folder.moveChildCursorTo(i);

                        PSTMessage msg = (PSTMessage) folder.getNextChild();

                        Date submitTime = msg.getClientSubmitTime();
                        String time = "";

                        if ( submitTime != null )
                            time = submitTime.toString();

                        MessageHeader mh = new MessageHeader(msg.getDescriptorNode().descriptorIdentifier, msg.getSubject(), 
                                msg.getSentRepresentingName(),  msg.getDisplayTo(), time, msg.hasAttachments());

                        mh.setLocation(name);
                        headers.add(mh);

                        if ( panel != null ) {
                            counter++;
                            panel.setText("Load Message: " + counter);
                        }
                    }
                }
                catch(PSTException e) {
                    logger.log(Level.SEVERE, "Uncaught exception", e);
                }
            }
        }
        catch (IOException e) {
            logger.log(Level.SEVERE, "Uncaught exception", e);
        }

        return headers;
    }
}
