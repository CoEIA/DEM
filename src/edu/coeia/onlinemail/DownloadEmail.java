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
