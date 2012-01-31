/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.onlinemail;

import edu.coeia.cases.Case;
import edu.coeia.util.FilesPath;
import edu.coeia.wizard.EmailConfiguration;
import edu.coeia.wizard.EmailConfiguration.ONLINE_EMAIL_AGENT;

import javax.swing.JFrame;

/**
 *
 * @author wajdyessam
 */
public class DownloadEmail {
    private final Case aCase;
    private final EmailConfiguration emailConsiguration;
    
    public DownloadEmail(final Case aCase, final EmailConfiguration emailConfig) {
        this.aCase = aCase;
        this.emailConsiguration = emailConfig;
    }
    
    public void download(final JFrame frame) throws Exception {
        EmailDownloaderDialog dialogue = new EmailDownloaderDialog(frame, true, this.aCase);
        dialogue.m_ObjDownloader = new OnlineEmailDownloader(dialogue,
                this.aCase.getCaseLocation() + "\\" + FilesPath.ATTACHMENTS,
                this.aCase.getCaseLocation() + "\\" + FilesPath.EMAIL_DB,
                this.aCase.getCaseLocation() + "\\TMP\\" 
                );
        
        // if hotmail
        if (this.emailConsiguration.getSource() == ONLINE_EMAIL_AGENT.HOTMAIL) {
            if (dialogue.m_ObjDownloader.ConnectPop3Hotmail(this.emailConsiguration.getUserName(), this.emailConsiguration.getPassword())) {
                dialogue.m_ObjDownloader.execute();
                dialogue.setVisible(true);
            }
        }
        
        if (this.emailConsiguration.getSource() == ONLINE_EMAIL_AGENT.YAHOO) {
            if (dialogue.m_ObjDownloader.ConnectPop3Yahoo(this.emailConsiguration.getUserName(), this.emailConsiguration.getPassword())) {
                dialogue.m_ObjDownloader.execute();
                dialogue.setVisible(true);
            }
        }
        
       if (this.emailConsiguration.getSource() == ONLINE_EMAIL_AGENT.GMAIL) {
            if (dialogue.m_ObjDownloader.ConnectIMAP(this.emailConsiguration.getUserName(), this.emailConsiguration.getPassword())) {
                dialogue.m_ObjDownloader.execute();
                dialogue.setVisible(true);
            }
        }
    }
}
