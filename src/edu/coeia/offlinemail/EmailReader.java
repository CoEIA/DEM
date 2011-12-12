/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailReader {

    private static PSTFile pstFile ;
    private static ArrayList<MessageHeader> messageHeaderList ;
    private static String path ;

    private static InfiniteProgressPanel panel ;
    private static int counter = 0 ;

    private static final Logger logger = Logger.getLogger(edu.coeia.util.FilesPath.LOG_NAMESPACE);
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
                    for (int i=0 ; i<folder.getEmailCount() ; i++) {
                        folder.moveChildCursorTo(i);

                        PSTMessage msg = (PSTMessage) folder.getNextChild();

                        Date submitTime = msg.getClientSubmitTime();
                        String time = "";

                        if ( submitTime != null )
                            time = submitTime.toString();

                        MessageHeader mh = new MessageHeader(msg.getDescriptorNode().descriptorIdentifier, msg.getSubject(), 
                                msg.getSentRepresentingName(),  msg.displayTo(), time, msg.hasAttachments());

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
