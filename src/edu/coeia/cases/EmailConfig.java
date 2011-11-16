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
public final class EmailConfig implements Serializable{
    
    public enum SOURCE {HOTMAIL, GMAIL };
    
    private final String userName;
    private final String password;
    private final SOURCE source ;

    public static EmailConfig newInstance(final String username, final String password, final SOURCE source) {
        return new EmailConfig(username, password, source);
    }
    
    private EmailConfig(final String username, final String password, final SOURCE source) {
        this.userName = username;
        this.password = password;
        this.source = source ;
    }
    
    public String getUserName()
    { return this.userName;}
    
    public String getPassword()
    { return this.password;}
    
    public SOURCE getSource() { return this.source ;}

}
