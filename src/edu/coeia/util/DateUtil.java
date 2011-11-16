/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.util;

/**
 *
 * @author wajdyessam
 */

import java.util.Calendar ;
import java.util.Date ;
import java.util.GregorianCalendar;

import java.text.SimpleDateFormat ;
import java.text.DateFormat ;

public class DateUtil {
    
    /*
     * Suppress default constructor for noninstantiability
     */    
    private DateUtil() {
        throw new AssertionError();
    }
    
    public static String getTimeFromMilliseconds (long ms) {
	int time = (int) ms / 1000 ;
	int seconds = time %  60 ;
	int mintues = (time % 3600 ) / 60 ;
	int hours = time / 3600 ;

	String format = String.format("%%0%dd", 2);
	String secondsString = String.format(format, seconds);
	String mintuesString = String.format(format, mintues);
	String hoursString   = String.format(format, hours  );

	return hoursString + ":" + mintuesString + ":" + secondsString ;
    }
        
    public static String getCurrentDate () {
        Date d = new Date();
        return formatDate(d);
    }

    public static String getFirstDate () {
        Date d = new Date(0);
        return formatDate(d);
    }

    public static String formatDate (Date d) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	Calendar c = Calendar.getInstance();
	c.setTime(d);
	return df.format( c.getTime() );
    }
    
    public static String formatDateForLogFileName(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return dateFormat.format(calendar.getTime());
    }

    public static String formatDateTime (Date d) {
        DateFormat df = new SimpleDateFormat("hh:mm:ss - dd/MM/yyyy");
	Calendar c = Calendar.getInstance();
	c.setTime(d);
        
	return df.format( c.getTime() );
    }
    
    public static Date addDays (Date date, int days) {
        GregorianCalendar now = new GregorianCalendar();
        now.setTime(date);
        now.add(Calendar.DATE, days);

        return (now.getTime());
    }
            
    // from: http://www.velocityreviews.com/forums/t139746-how-to-subtract-dates.html
    public static int subtractDays(Date date1, Date date2) {
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
}
