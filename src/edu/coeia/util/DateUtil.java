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

import java.text.SimpleDateFormat ;
import java.text.DateFormat ;

public class DateUtil {
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
}
