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

public class EmailConfiguration implements Serializable{
    
    public enum SOURCE {HOTMAIL, GMAIL, Yahoo };
    
    private final String userName;
    private final String password;
    private final SOURCE source ;

    public static EmailConfiguration newInstance(final String username, final String password, final SOURCE source) {
        return new EmailConfiguration(username, password, source);
    }
    
    private EmailConfiguration(final String username, final String password, final SOURCE source) {
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
