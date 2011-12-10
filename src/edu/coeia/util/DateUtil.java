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
import org.joda.time.DateTime;
import org.joda.time.Days;

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
            
    // Joda Implementation
    public static int subtractDays(Date date1, Date date2) {
        DateTime install = new DateTime(date1);
        DateTime current = new DateTime(date2);
        
        return Days.daysBetween(current, install).getDays();
    }
}
