/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.indexing.dialogs.EmailCrawlingProgressPanel;
import edu.coeia.extractors.ImageExtractor;
import edu.coeia.util.ApplicationConstants;
import edu.coeia.util.FileUtil;
import edu.coeia.util.Utilities;
import edu.coeia.util.Tuple;
import edu.coeia.extractors.NoneImageExtractor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.document.Document;

import com.pff.PSTAttachment;
import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;

import java.util.ArrayList;
import java.util.List;

final class OutlookIndexer extends Indexer{
    
    private int depth = -1;
    private int outlookId = 0;
    
    /*
     * static factory method to get an instance of outlook indexer
     */
    public static OutlookIndexer newInstance (LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor) {
        return new OutlookIndexer(luceneIndex, file, mimeType, imageExtractor, 0);
    }

    public static OutlookIndexer newInstance (LuceneIndex luceneIndex, File file, String mimeType, 
            ImageExtractor imageExtractor, int parentId) {
        return new OutlookIndexer(luceneIndex, file, mimeType, imageExtractor, parentId);
    }
    
    private OutlookIndexer(LuceneIndex luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor, int parentId) {
        super(luceneIndex, file, mimeType, imageExtractor);
        this.setParentId(parentId);
    }
   
    @Override
    public boolean doIndexing() {
       boolean result = false;
       
        try {
            PSTFile pstFile = new PSTFile(this.getFile().getAbsolutePath());
            
            this.outlookId = this.getId();
            
            NonDocumentIndexer.newInstance(this.getLuceneIndex(), this.getFile(), this.getMimeType(),
                new NoneImageExtractor(), this.getParentId()).doIndexing();
            
            this.processOutlookFolder(pstFile.getRootFolder());
            result = true;
            
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return result;
    }
   
    private void processOutlookFolder(final PSTFolder folder) throws PSTException, IOException {
        depth++;
        String folderName = ""; // root folder has no name
        
        if (depth > 0) {
            folderName = folder.getDisplayName();
        }

        // get folders
        if (folder.hasSubfolders()) {
            List<PSTFolder> childFolders = folder.getSubFolders();
            for (PSTFolder childFolder : childFolders) {
                processOutlookFolder(childFolder);
            }
        }

        // get email inside folders
        if (folder.getContentCount() > 0) {
            depth++;
            PSTMessage email = (PSTMessage) folder.getNextChild();
            while (email != null) {
                processEmail(email, folderName);
                email = (PSTMessage) folder.getNextChild();
            }
            depth--;
        }
        depth--;
    }
    
    private void processEmail(final PSTMessage email, final String folderName) {
        try {
            List<Tuple<String, PSTAttachment>> attachmentsName = this.getAttachments(email);
            this.updateGuiWithAttachmentsName(email, folderName, attachmentsName);
            List<String> filePaths = this.saveAndGetEmailAttachmentsPath(attachmentsName);
            
            // index the email message
            int emailId = this.getId();
            Document document = LuceneDocumentBuilder.getDocumentForOfflineEmailMessage(this, email, folderName, this.outlookId, filePaths);
            this.indexDocument(document);
            
            // index the attachments paths, with email id as the parent
            for(String path: filePaths) {
                try {
                    File file = new File(path);
                    boolean status = this.getLuceneIndex().indexFile(file, emailId, null);
                }
                catch(Exception e) {
                    //TODO: update gui with these error file
                    System.out.println("cannot index the attachment: " + path);
                }
            }
        } catch (PSTException ex) {
            ex.printStackTrace();
            Logger.getLogger(OutlookIndexer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(OutlookIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void updateGuiWithAttachmentsName(final PSTMessage email,
            final String folderName, final List<Tuple<String, PSTAttachment>> attachmentsName) {
        String subject = "";
        String date = "";
        String sentRepresentingName = "";
        String displayTo = "";
        boolean hasAttachment = false;

        try {
            subject = email.getSubject();
            date  = Utilities.getEmptyStringWhenNullDate(email.getClientSubmitTime());
            hasAttachment = email.hasAttachments();
            sentRepresentingName = email.getSentRepresentingName();   
            displayTo = Utilities.getEmptyStringWhenNullString(email.getDisplayTo());
        }
        catch(Exception e ) {
            System.out.println("Exception in outlook gui update!!!");
        }

        EmailCrawlingProgressPanel panel = new EmailCrawlingProgressPanel();
        panel.setAgentType("Outlook: " + getFile().getAbsolutePath());
        panel.setCurrentFolder(folderName);
        panel.setCurrentMessageSubject(subject);
        panel.setMessageDate(date);
        panel.setHasAttachment(String.valueOf(hasAttachment));
        panel.setFrom(sentRepresentingName);
        panel.setTo(displayTo);

        List<String> names = new ArrayList<String>();
        for(Tuple<String, PSTAttachment> pair: attachmentsName) {
            names.add(pair.getA());
        }
        panel.setAttachment(names);

        getDialog().changeProgressPanel(panel);
    }
    
    private List<String> saveAndGetEmailAttachmentsPath(final List<Tuple<String, PSTAttachment>> attachmentsName) 
            throws PSTException, IOException {
        File storingPath = new File(this.getCaseLocation() + "\\" + ApplicationConstants.OFFLINE_EMAIL_ATTACHMENTS);
        
        List<String> filePaths = new ArrayList<String>();
        
        for(Tuple<String, PSTAttachment> pair: attachmentsName) {
            File attachmentPath = new File(storingPath, pair.getA());
            InputStream stream = pair.getB().getFileInputStream();
            FileUtil.saveObject(stream,attachmentPath.getAbsolutePath());
            stream.close();
            
            filePaths.add(attachmentPath.getAbsolutePath());
        }
        
        return filePaths;
    }
  
    private List<Tuple<String, PSTAttachment>> getAttachments(final PSTMessage email) throws PSTException, IOException {
        int id = email.getDescriptorNode().descriptorIdentifier;
        int numberOfAttachments = email.getNumberOfAttachments();

        List<Tuple<String, PSTAttachment>> attachmentsName = new ArrayList<Tuple<String, PSTAttachment>>();
        
        for(int i=0; i<numberOfAttachments; i++) {
            PSTAttachment attchment = email.getAttachment(i);
            String fileName = this.getId() + "-" + id + "-" + attchment.getLongFilename();
            
            if ( fileName.isEmpty() ) {
                String name = attchment.getFilename();
                if ( name.trim().isEmpty()) continue;
                
                fileName =  this.getId() + "-" + id + "-" + name;
            }
            
            Tuple<String, PSTAttachment> pair = new Tuple<String, PSTAttachment>(fileName, attchment);
            attachmentsName.add(pair);
        }
        
        return attachmentsName;
    }
}
