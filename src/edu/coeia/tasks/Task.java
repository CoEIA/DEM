/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tasks;

/**
 *
 * @author wajdyessam
 */
public interface Task {
    public void startTask();
    public boolean isCancelledTask();
    public void doTask() throws Exception;
}
