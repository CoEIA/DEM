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
package edu.coeia.offlinemail;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.gutil.InfiniteProgressPanel ;

import javax.swing.SwingWorker ;

import com.pff.PSTFile ;
import javax.swing.JOptionPane;

public class EmailReaderThread extends SwingWorker<Integer,Void>{

    private String path ;
    private PSTFile pstFile ;
    private InfiniteProgressPanel panel ;
    private boolean status;

    public EmailReaderThread (String path, PSTFile pstFile, InfiniteProgressPanel i) {
        this.path = path ;
        this.pstFile = pstFile ;
        this.panel = i ;
    }
    
    @Override
    protected Integer doInBackground() throws Exception {
        if ( EmailReader.getInstance(pstFile, path, panel) == null )
            status = false;
        else
            status = true;
        
        return 0;
    }

    @Override
    public void done() {
        if ( status == false) {
            JOptionPane.showMessageDialog(null, "Cannot handling this email", "there is problem with this email type", JOptionPane.ERROR_MESSAGE);
        }
        
        panel.interrupt();
        panel.stop();
    }
}
