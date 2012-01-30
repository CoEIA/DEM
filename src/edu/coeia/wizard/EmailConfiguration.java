/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.wizard;

import java.io.Serializable;

/**
 *
 * @author Ahmed
 */

public final class EmailConfiguration implements Serializable{
    
    public enum ONLINE_EMAIL_AGENT {HOTMAIL, GMAIL, YAHOO };
    
    private final String userName;
    private final String password;
    private final ONLINE_EMAIL_AGENT source ;

    public static EmailConfiguration newInstance(final String username,
            final String password, final ONLINE_EMAIL_AGENT source) {
        return new EmailConfiguration(username, password, source);
    }
    
    private EmailConfiguration(final String username, final String password,
            final ONLINE_EMAIL_AGENT source) {
        this.userName = username;
        this.password = password;
        this.source = source ;
    }
    
    public String getUserName()
    { return this.userName;}
    
    public String getPassword()
    { return this.password;}
    
    public ONLINE_EMAIL_AGENT getSource() { return this.source ;}
}
