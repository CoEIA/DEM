/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.onlinemail;

/**
 *
 * @author Farhan
 */
public class ResumeState {
    
    private String strFolderName;
    private String strMessageId;
    private boolean bFoundState;
    private boolean bActivate; 
   
    ResumeState()
    {
        bActivate = bFoundState = false;
        //debuging purpose
        strFolderName="INBOX";
        strMessageId="4";
    }
    
    public String getFolderName()
    {
        return strFolderName;
    }
    
    public String getMessageId()
    {
        return strMessageId;
    }
    
    public void Activate()
    {
        bActivate = true; 
    }
    
    public void Deactivate()
    {
         bActivate = false; 
    }
    
    public boolean isActive()
    {
        return bActivate;
    }
    
    public void setFoundState(boolean vbfound)
    {
        bFoundState = vbfound;
    }
    
    public boolean getFoundState()
    {
        return bFoundState;
    }
}
