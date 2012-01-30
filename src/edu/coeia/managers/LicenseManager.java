/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.managers;

import edu.coeia.util.DateUtil ;

import java.util.Date;
import java.util.prefs.Preferences;


/**
 * Singleton Class For License Management
 * @author wajdyessam
 */

public enum LicenseManager {
    FULL_LICENSE(true), BETA_LICENSE(false);
    
    private LicenseManager(boolean state) {
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
