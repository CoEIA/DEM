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

import java.io.Serializable;

/**
 *
 * @author Farhan
 */
public class ResumeState implements Serializable{
    
    private String strFolderName;
    private String strMessageId;
    private boolean bFoundState;
    private boolean bActivate; 
   
    ResumeState()
    {
        bActivate = bFoundState = false;
        //debuging purpose
   //     strFolderName="INBOX";
   //     strMessageId="4";
    }
    
    public String getFolderName()
    {
        return strFolderName;
    }
    
    public String getMessageId()
    {
        return strMessageId;
    }
    
    public void setFolderName(String vstrFolderName)
    {
        strFolderName = vstrFolderName;
    }
    
    public void setMessageId(String vstrMsgId)
    {
        strMessageId = vstrMsgId;
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
    
    public void resetState()
    {
        bActivate = bFoundState = false;
        //debuging purpose
        strFolderName="";
        strMessageId="";
    }
}
