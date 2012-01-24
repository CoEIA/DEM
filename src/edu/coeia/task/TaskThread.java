/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import java.util.List;

import javax.swing.SwingWorker;

/**
 *
 * @author wajdyessam
 */
public class TaskThread extends SwingWorker<Void, String> {
    private final Task task;
    private final ProgressDialog dialog;
    private boolean isCancelledThread ;
    
    public TaskThread (final Task task) {
        this.task = task;
        this.dialog = new ProgressDialog(null, false, this);
    }
    
    public void stopThread() {
        this.cancel(true);
        this.isCancelledThread = true;
        this.dialog.dispose();
    }
        
    @Override
    public Void doInBackground() throws Exception {
        this.dialog.setVisible(true);
        this.task.doTask();
        return null;
    }
        
    @Override
    public void process(List<String> data) {
//        if ( isCancelled() ) {
//            return; 
//        }
//        else {
//            for(String item: data) {
//                this.dialog.appendMessage(item);
//            }
//        }
    }
    
    @Override
    public void done() {
        this.dialog.dispose();
        return;
    }
    
    public boolean isCancelledThread() { 
        return this.isCancelledThread;
    }
}
