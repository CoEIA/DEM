/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.onlinemail;

import edu.coeia.cases.Case;
import edu.coeia.wizard.EmailConfiguration;

import javax.swing.JFrame;

/**
 *
 * @author wajdyessam
 */
public class DownloadEmail {
    private final Case aCase;
    private final EmailConfiguration emailConfiguration;
    
    public DownloadEmail(final Case aCase, final EmailConfiguration emailConfig) {
        this.aCase = aCase;
        this.emailConfiguration = emailConfig;
    }
    
    public void download(final JFrame frame) throws Exception {
        EmailDownloaderDialog dialogue = new EmailDownloaderDialog(frame, true, this.aCase, 
                this.emailConfiguration);
        dialogue.start();
    }
}
