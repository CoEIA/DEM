/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

import java.io.Serializable;

/**
 *
 * @author Ahmed
 */
public class EmailConfig implements Serializable{
    
    private String userName;
    private String password;
    private boolean isGmail;
    private boolean isHotmail;

    public EmailConfig(String username, String password, boolean isGmail, boolean isHotmail) {

        this.userName = username;
        this.password = password;
        this.isGmail = isGmail;
        this.isHotmail = isHotmail;


    }
    
    public String getUserName()
    { return this.userName;}
    public String getPassword()
    { return this.password;}
    public boolean isGmail()
    { return this.isGmail; }
    public boolean isHotmail()
    { return this.isHotmail;}
}
