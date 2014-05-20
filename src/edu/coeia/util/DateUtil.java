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
package edu.coeia.util;

/**
 *
 * @author wajdyessam
 */

import java.text.ParseException;
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

    public static String formatDate (final String date) throws ParseException {
        if ( date.isEmpty() )
            return date;
        
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        Date formattedDate = df.parse(date);
        return formatedDateWithTime(formattedDate);
    }
    
    public static String formatedDateWithTime(final Date date) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	Calendar c = Calendar.getInstance();
	c.setTime(date);
	return df.format( c.getTime() );
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
