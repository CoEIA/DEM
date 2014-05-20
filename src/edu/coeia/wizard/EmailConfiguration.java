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
