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
package edu.coeia.tasks;

import javax.swing.SwingWorker;

/**
 *
 * @author wajdyessam
 */
final class BackgroundTask extends SwingWorker<Void, String> {
    private final Task task;
    private final BackgroundProgressDialog dialog;
    
    public BackgroundTask (final BackgroundProgressDialog dialog) {
        this.dialog = dialog;
        this.task = dialog.getTask();
    }
        
    @Override
    public Void doInBackground() throws Exception {
        this.task.doTask();
        return null;
    }
    
    @Override
    public void done() {
        this.dialog.setVisible(false);
        this.dialog.dispose();
        return;
    }
}
