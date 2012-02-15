/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import javax.swing.SwingWorker;

/**
 *
 * @author wajdyessam
 */
final class TaskThread extends SwingWorker<Void, String> {
    private final Task task;
    private final ProgressDialog dialog;
    
    public TaskThread (final ProgressDialog dialog) {
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
