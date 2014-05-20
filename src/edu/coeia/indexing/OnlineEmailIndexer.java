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
package edu.coeia.indexing;

import edu.coeia.indexing.dialogs.EmailCrawlingProgressPanel;
import edu.coeia.extractors.ImageExtractor;
import edu.coeia.onlinemail.OnlineEmailMessage;
import edu.coeia.onlinemail.OnlineEmailDBHandler;
import edu.coeia.util.Utilities;

import java.io.File;

import java.sql.SQLException;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;


/**
 *
 * @author Ahmed
 */
final class OnlineEmailIndexer extends Indexer {

    public OnlineEmailIndexer(IndexerManager luceneIndex, File file, String mimeType,
            ImageExtractor imageExtractor) {
        super(luceneIndex, file, mimeType, imageExtractor);
        this.setParentId(0);
    }

    @Override
    public boolean doIndexing() {
        boolean status = false;
        OnlineEmailDBHandler db = null;
        
        try {
            db = new OnlineEmailDBHandler(this.getFile().getAbsolutePath());
            List<OnlineEmailMessage> AllMsgs = db.getAllMessages();

            for (OnlineEmailMessage msg : AllMsgs) {
                try {
                    Document doc = LuceneDocumentBuilder.getDocumentForOnlineEmailMessage(this, msg);

                    if (doc != null) {
                        //this.updateGUI(msg);
                        
                        int currentId = this.getId();
                        this.indexDocument(doc);

                        for (String sAttachments : msg.getAttachments()) {
                            File attachmentPath = new File(this.getCaseFacade().getCaseOnlineEmailAttachmentLocation() + File.separator + sAttachments);
                            this.getIndexerManager().indexFile(attachmentPath, currentId);
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
        finally {
            try {
                db.closeDB();
            } catch (SQLException ex) {
                Logger.getLogger(OnlineEmailIndexer.class.getName()).log(Level.SEVERE, null, ex);
            }
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
    }
}
