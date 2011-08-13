/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.email;

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

import java.io.FileNotFoundException;
import java.io.IOException ;
import java.util.Collections;

import edu.coeia.gui.utilties.InfiniteProgressPanel ;

public class EmailReaderWithGUI {

    private static PSTFile pstFile ;
    private static ArrayList<MessageHeader> map ;
    private static String path ;

    private static InfiniteProgressPanel panel ;
    private static int counter = 0 ;
    
    public static ArrayList<MessageHeader> getInstance(PSTFile pst, String p,InfiniteProgressPanel pnl ) {
        
        if ( map == null || ! path.equals(p)) {
            map = new ArrayList<MessageHeader> ();
            pstFile = pst ;
            path = p ;
            panel = pnl ;
            counter = 0;
            caching();
        }
        
        return map ;
    }
    
    private static void caching () {
        try {
            PSTFolder root = pstFile.getRootFolder() ;
            
            writeToCache(root);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (PSTException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void writeToCache (PSTFolder mainFolder) throws PSTException,IOException {
        Iterator<PSTFolder> itr = mainFolder.getSubFolders().iterator();
        while ( itr.hasNext() ){
            PSTFolder folder = itr.next();

            if ( folder.hasSubfolders() ) {
                writeToCache(folder);
            }
            else {     
                String name =  folder.getDisplayName() ;

                for (int i=0 ; i<folder.getEmailCount() ; i++) {
                    folder.moveChildCursorTo(i);

                    PSTMessage msg = (PSTMessage) folder.getNextChild();

                    MessageHeader mh = new MessageHeader(msg.getDescriptorNode().descriptorIdentifier, msg.getSubject(), msg.getSentRepresentingName(), msg.getReceivedByName(), msg.getClientSubmitTime().toString(), msg.hasAttachments());
                    map.add(mh);

                    
                    if ( panel != null ) {
                        counter++;
                        panel.setText("Load Message: " + counter);
                    }
                }
                
            }
        }
    }
}
