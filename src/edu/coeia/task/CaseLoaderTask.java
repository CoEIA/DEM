/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.cases.Case;
import edu.coeia.cases.CaseManager;
import edu.coeia.cases.CaseManagerFrame;

/**
 *
 * @author wajdyessam
 */
public class CaseLoaderTask implements Task {
    private final TaskThread thread;
    private final CaseManagerFrame frame;
    private final String caseName ;
    
    public CaseLoaderTask(final CaseManagerFrame frame, final String caseName) {
        this.thread = new TaskThread(this);
        this.frame = frame;
        this.caseName = caseName;
    }
    
    @Override
    public void startTask() {
        this.thread.execute();
    }
    
    @Override
    public void doTask() throws Exception {
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.thread.isCancelledThread();
    }
}
