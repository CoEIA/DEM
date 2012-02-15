/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
