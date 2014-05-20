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
package edu.coeia.licence;

import edu.coeia.util.DateUtil ;

import java.util.Date;
import java.util.prefs.Preferences;


/**
 * Singleton Class For License Management
 * @author wajdyessam
 */

public enum LicenceManager {
    FULL_LICENSE(true), BETA_LICENSE(false);
    
    private LicenceManager(boolean state) {
        isFullVersion = state;
    }
    
    public boolean isFullVersion() {
        return isFullVersion;
    }
    
    public boolean isExpireNow() {
        boolean state = false;

        Date currentDate = new Date();
        
        Preferences root = Preferences.userRoot();
        Preferences node = root.node("/com/coeia/dem");

        String installTime = node.get("installTime",String.valueOf(currentDate.getTime()));
        String lastUsage = node.get("LastUsage", String.valueOf(currentDate.getTime()));

        Date installDate = new Date(Long.valueOf(installTime));
        Date lastDate = new Date(Long.valueOf(lastUsage));

        if ( lastDate.after(currentDate) ) {
            state = true;
        }
        else if ( lastDate.before(currentDate) ) {
            Date expireDate = DateUtil.addDays(installDate, TRIAL_LENGTH);

            if ( expireDate.before(currentDate))
                state = true;
            else
                state = false;
        }
        else {
            node.put("installTime", String.valueOf(currentDate.getTime()));
            node.put("LastUsage", String.valueOf(currentDate.getTime()));
            state = false;
        }

        return state;
    }
    
    public void saveUsage () {
        Date currentDate = new Date();

        Preferences root = Preferences.userRoot();
        Preferences node = root.node("/com/coeia/dem");
        
        node.put("LastUsage", String.valueOf(currentDate.getTime()));
    }

    public int getRemainingDays() {
        Date currentDate = new Date();

        Preferences root = Preferences.userRoot();
        Preferences node = root.node("/com/coeia/dem");

        String installTime = node.get("installTime",String.valueOf(currentDate.getTime()));
        Date installDate = new Date(Long.valueOf(installTime));

        Date expireDate = DateUtil.addDays(installDate, TRIAL_LENGTH);

        int diff = DateUtil.subtractDays( expireDate, currentDate);
        
        return (diff);
    }
    
    /**
     * 
     * determine full version or beta version
     * true  - full version should work using smart card license
     * false - beta version should be trial version for 60 days
     */
    private boolean isFullVersion;
    private static final int TRIAL_LENGTH = 60;
}
