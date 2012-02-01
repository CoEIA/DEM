/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.cases.Case;
import edu.coeia.gutil.JTableUtil;
import edu.coeia.indexing.IndexingConstant;
import edu.coeia.items.EmailItem;
import edu.coeia.offlinemail.EmailBrowsingPanel;
import edu.coeia.util.DateUtil;
import edu.coeia.util.FilesPath;

import java.io.File;
import java.io.IOException;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author wajdyessam
 */
public class EmailLoadingTask  implements Task{
    private final TaskThread thread;
    private final Case aCase;
    private final EmailBrowsingPanel panel;
    
    public EmailLoadingTask(final Case aCase, final EmailBrowsingPanel panel) {
        this.thread = new TaskThread(this);
        this.aCase = aCase;
        this.panel = panel;
    }
    
    @Override
    public void startTask() {
        this.thread.execute();
    }
    
    @Override
    public void doTask() throws Exception {
        this.loadEmail();
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.thread.isCancelledThread();
    }
    
    private void loadEmail() throws Exception {
        if ( isOfflineEmailSelected() ) {
            String relativePath = String.valueOf(this.panel.getList().getSelectedValue());
            this.getAllMessaageInOfflineEmailPath(relativePath);
        }
        else if ( isOnlineEmailSelected() ) {
            String username = String.valueOf(this.panel.getList().getSelectedValue());
            this.getAllMessageInOnlineEmailPath(username);
        }
    }
    
    private void getAllMessaageInOfflineEmailPath(final String path) throws IOException, ParseException {
        List<Integer> ids = new ArrayList<Integer>();
        
        String indexDir = this.aCase.getCaseLocation() + "\\" + FilesPath.INDEX_PATH;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        
        for (int i=0; i<indexReader.maxDoc(); i++) {
            if ( this.isCancelledTask() )
                return ;
                     
            Document document = indexReader.document(i);
            if ( document != null ) {
                Field field = document.getField(IndexingConstant.OFFLINE_EMAIL_PATH);
                if ( field != null && field.stringValue() != null) {
                   String tmp = field.stringValue();
                   
                   if ( tmp.endsWith(path)) {
                         // show file properities
                        String desciption = document.get(IndexingConstant.DOCUMENT_DESCRIPTION);
                        String emailAgent = document.get(IndexingConstant.OFFLINE_EMAIL_FOLDER_NAME);
                        String emailSource = document.get(IndexingConstant.OFFLINE_EMAIL_NAME);
                        String emailDate = document.get(IndexingConstant.OFFLINE_EMAIL_CLIENT_SUBMIT_TIME);
                        String emailMessage = document.get(IndexingConstant.OFFLINE_EMAIL_HTML_CONTENT);
                        if ( emailMessage.trim().isEmpty() ) 
                            emailMessage = document.get(IndexingConstant.OFFLINE_EMAIL_PLAIN_CONTENT);

                        String emailSubject = document.get(IndexingConstant.OFFLINE_EMAIL_SUBJECT);

                        String emailTo = document.get(IndexingConstant.OFFLINE_EMAIL_DISPLAY_TO);
                        String emailFrom = document.get(IndexingConstant.OFFLINE_EMAIL_SENT_REPRESENTING_NAME);
                        String emailCC = document.get(IndexingConstant.OFFLINE_EMAIL_DISPLAY_CC);
                        String emailBCC = document.get(IndexingConstant.OFFLINE_EMAIL_DISPLAY_BCC);
                        String id = document.get(IndexingConstant.DOCUMENT_ID);
                        String parentId = document.get(IndexingConstant.DOCUMENT_PARENT_ID);
                        String hash = document.get(IndexingConstant.DOCUMENT_HASH);
                        String folderName = document.get(IndexingConstant.OFFLINE_EMAIL_FOLDER_NAME);
                        boolean hasAttachment = Boolean.valueOf(document.get(IndexingConstant.OFFLINE_EMAIL_HAS_ATTACHMENT));
                        String user = document.get(IndexingConstant.OFFLINE_EMAIL_PATH);
                        
                        EmailItem item = new EmailItem(Integer.valueOf(id), 
                                Integer.valueOf(parentId), hash,  desciption, emailFrom,
                                emailTo, emailSubject, DateUtil.formatDate(emailDate), folderName, hasAttachment,user);
                        
                        // display
                        Object[] data = {item.getID(), item.getFolder(), item.getFrom(),
                            item.getTo(), item.getSubject(), item.getTime(), item.hasAttachment()};

                        JTableUtil.addRowToJTable(panel.getTable(), data);
                
                        ids.add(Integer.valueOf(id));
                   }
                }
            }
        }
        
        this.panel.setResultIds(ids);
        indexReader.close();
    }
        
    
    private void getAllMessageInOnlineEmailPath(final String username) throws IOException, ParseException {
        List<Integer> ids = new ArrayList<Integer>();
        
        String indexDir = this.aCase.getCaseLocation() + "\\" + FilesPath.INDEX_PATH;
        Directory dir = FSDirectory.open(new File(indexDir));
        IndexReader indexReader = IndexReader.open(dir);
        
        for (int i=0; i<indexReader.maxDoc(); i++) {
            if ( this.isCancelledTask() )
                return ;
                        
            Document document = indexReader.document(i);
            if ( document != null ) {
                Field field = document.getField(IndexingConstant.ONLINE_EMAIL_USER_NAME);
                if ( field != null && field.stringValue() != null) {
                   String tmp = field.stringValue();
                   
                   if ( tmp.equals(username)) {
                         // show file properities
                        String desciption = document.get(IndexingConstant.DOCUMENT_DESCRIPTION);
                        String emailDate = document.get(IndexingConstant.ONLINE_EMAIL_RECIEVED_DATE);
                        String emailMessage = document.get(IndexingConstant.ONLINE_EMAIL_BODY);
                        String emailSubject = document.get(IndexingConstant.ONLINE_EMAIL_SUBJECT);

                        String emailTo = document.get(IndexingConstant.ONLINE_EMAIL_TO);
                        String emailFrom = document.get(IndexingConstant.ONLINE_EMAIL_FROM);
                        String emailCC = document.get(IndexingConstant.ONLINE_EMAIL_CC);
                        String emailBCC = document.get(IndexingConstant.ONLINE_EMAIL_BCC);
                        String id = document.get(IndexingConstant.DOCUMENT_ID);
                        String hash = document.get(IndexingConstant.DOCUMENT_HASH);
                        String folderName = document.get(IndexingConstant.ONLINE_EMAIL_FOLDER_NAME);
                        boolean hasAttachment = Boolean.valueOf(document.get(IndexingConstant.ONLINE_EMAIL_ATTACHMENT_PATH));
                        String user = document.get(IndexingConstant.ONLINE_EMAIL_USER_NAME);
                        
                        EmailItem item = new EmailItem(Integer.valueOf(id), 
                                Integer.valueOf("0"), hash, desciption, emailFrom,
                                emailTo, emailSubject, DateUtil.formatDate(emailDate), folderName, hasAttachment,user);

                        // display data
                        Object[] data = {item.getID(), item.getFolder(), item.getFrom(),
                            item.getTo(), item.getSubject(), item.getTime(), item.hasAttachment()};

                        JTableUtil.addRowToJTable(panel.getTable(), data);
                        
                        ids.add(Integer.valueOf(id));
                   }
                }
            }
        }
        
        this.panel.setResultIds(ids);
        indexReader.close();
    }
        
    private boolean isOfflineEmailSelected() {
        Object selectedValue = this.panel.getList().getSelectedValue();
        if ( selectedValue == null )
            return false;
        
        return String.valueOf(this.panel.getList().getSelectedValue()).endsWith(".pst") ||
               String.valueOf(this.panel.getList().getSelectedValue()).endsWith("ost");
        
    }
    
    private boolean isOnlineEmailSelected() {
        Object selectedValue = this.panel.getList().getSelectedValue();
        if ( selectedValue == null )
            return false;
        
        String value = String.valueOf(selectedValue);
        return !value.endsWith(".pst") && !value.endsWith("ost");
    }
}
