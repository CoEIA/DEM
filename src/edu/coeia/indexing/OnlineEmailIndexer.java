/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.indexing;

import edu.coeia.indexing.dialogs.EmailCrawlingProgressPanel;
import edu.coeia.extractors.ImageExtractor;
import edu.coeia.onlinemail.OnlineEmailMessage;
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
            ImageExtractor imageExtractor, CrawlerIndexerThread crawler) {
        super(luceneIndex, file, mimeType, imageExtractor);
        this.setParentId(0);
        this.setCrawler(crawler);
    }

    @Override
    public boolean doIndexing() {
        boolean status = false;

        try {
            OnlineEmailDBHandler db = new OnlineEmailDBHandler(this.getFile().getAbsolutePath());
            List<OnlineEmailMessage> AllMsgs = db.getAllMessages();

            for (OnlineEmailMessage msg : AllMsgs) {
                try {
                    Document doc = LuceneDocumentBuilder.getDocumentForOnlineEmailMessage(this, msg);

                    if (doc != null) {
                        this.updateGUI(msg);
                        
                        int currentId = this.getId();
                        this.indexDocument(doc);

                        for (String sAttachments : msg.getAttachments()) {
                            File attachmentPath = new File(this.getCaseFacade().getCaseOnlineEmailAttachmentLocation() + File.separator + sAttachments);
                            this.getLuceneIndex().indexFile(attachmentPath, currentId , this.getCrawler());
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

        EmailCrawlingProgressPanel.EmailCrawlingData data = new EmailCrawlingProgressPanel.EmailCrawlingData(
                agent, folderName, subject, date, String.valueOf(hasAttachment), 
                email.getFrom(), Utilities.getCommaSeparatedStringFromCollection(email.getTo()), email.getAttachments()
        );
        
        this.getCrawler().showEmailPanel(data);
    }
}
