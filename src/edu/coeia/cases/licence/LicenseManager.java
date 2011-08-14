/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases.licence;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar ;

import java.util.prefs.Preferences;


/**
 * Singleton Class For License Management
 * @author wajdyessam
 */

public enum LicenseManager {
    FULL_LICENSE(true), BETA_LICENSE(false);
    
    LicenseManager(boolean state) {
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
            Date expireDate = addDays(installDate, TRIAL_LENGTH);

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

        Date expireDate = addDays(installDate, TRIAL_LENGTH);

        int diff = subtractDays( expireDate, currentDate);
        
        return (diff);
    }
    
    private static Date addDays (Date date, int days) {
        GregorianCalendar now = new GregorianCalendar();
        now.setTime(date);
        now.add(Calendar.DATE, days);

        return (now.getTime());
    }
            
    // from: http://www.velocityreviews.com/forums/t139746-how-to-subtract-dates.html
    private int subtractDays(Date date1, Date date2) {
        GregorianCalendar gc1 = new GregorianCalendar();  gc1.setTime(date1);
        GregorianCalendar gc2 = new GregorianCalendar();  gc2.setTime(date2);

        int days1 = 0;
        int days2 = 0;
        int maxYear = Math.max(gc1.get(Calendar.YEAR), gc2.get(Calendar.YEAR));

        GregorianCalendar gctmp = (GregorianCalendar) gc1.clone();
        for (int f = gctmp.get(Calendar.YEAR);  f < maxYear;  f++)
            {days1 += gctmp.getActualMaximum(Calendar.DAY_OF_YEAR);  gctmp.add(Calendar.YEAR, 1);}

        gctmp = (GregorianCalendar) gc2.clone();
        for (int f = gctmp.get(Calendar.YEAR);  f < maxYear;  f++)
            {days2 += gctmp.getActualMaximum(Calendar.DAY_OF_YEAR);  gctmp.add(Calendar.YEAR, 1);}

        days1 += gc1.get(Calendar.DAY_OF_YEAR) - 1;
        days2 += gc2.get(Calendar.DAY_OF_YEAR) - 1;

        return (days1 - days2);
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
