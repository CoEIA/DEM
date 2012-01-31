/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.indexing.dialogs.EmailCrawlingProgressPanel;
import edu.coeia.extractors.ImageExtractor;
import edu.coeia.indexing.dialogs.IndexingDialog;
import edu.coeia.onlinemail.OnlineEmailMessage;
import edu.coeia.util.FilesPath;
import edu.coeia.onlinemail.OnlineEmailDBHandler;
import edu.coeia.util.Utilities;

import java.io.File;

import java.util.List;

import org.apache.lucene.document.Document;


/**
 *
 * @author Ahmed
 */
final class OnlineEmailIndexer extends Indexer {

    public OnlineEmailIndexer(LuceneIndex luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor, final IndexingDialog dialog) {
        super(luceneIndex, file, mimeType, imageExtractor);
        this.setParentId(0);
        this.setGUIDialog(dialog);
    }

    @Override
    public boolean doIndexing() {
        boolean status = false;

        try {
            OnlineEmailDBHandler db = new OnlineEmailDBHandler(this.getFile().getAbsolutePath());
            List<OnlineEmailMessage> AllMsgs = db.getAllMessages();

            for (OnlineEmailMessage msg : AllMsgs) {
                try {
                    Document doc = LuceneDocumentBuilder.getDocument(this, msg);

                    if (doc != null) {
                        this.updateGUI(msg);
                        
                        int currentId = this.getId();
                        this.indexDocument(doc);

                        for (String sAttachments : msg.getAttachments()) {
                            File attachmentPath = new File(this.getCaseLocation() + "\\" + FilesPath.ATTACHMENTS + "\\" + sAttachments);
                            this.getLuceneIndex().indexFile(attachmentPath, currentId , this.getDialog());
                        }
                    }
                } 
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            
            status = true;
        } 
        catch (Exception ex) {
            throw new UnsupportedOperationException("Error in Email Reading");
        }

        return status;
    }
        
    private void updateGUI(final OnlineEmailMessage email) {
        String subject = email.getSubject();
        String date = email.getSentDate();
        boolean hasAttachment = !email.getAttachments().isEmpty();
        String folderName = email.getFolderName();
        String agent = email.getUsername();

        EmailCrawlingProgressPanel panel = new EmailCrawlingProgressPanel();
        panel.setAgentType(agent);
        panel.setEmailPath("");
        panel.setCurrentFolder(folderName);
        panel.setCurrentMessageSubject(subject);
        panel.setMessageDate(date);
        panel.setFrom(email.getFrom());
        panel.setTo(Utilities.getCommaSeparatedStringFromCollection(email.getTo()));
        panel.setHasAttachment(String.valueOf(hasAttachment));
        panel.setAttachment(email.getAttachments());
        this.getDialog().changeProgressPanel(panel);
    }
}
