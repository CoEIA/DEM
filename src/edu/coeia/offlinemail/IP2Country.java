/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.offlinemail;

/**
 *
 * @author wajdyessam
 *
 */

/*
 * IP2Country
 * Goal: to get country name from ip
 * Written by: Wajdy Essam , 11/6/2010
 * FeedBack: wajdyessam[AT]hotmail[DOT]com
 * feel free to use this source code in your programs
 * database for ip number from: http://ip-to-country.webhosting.info
*/

import edu.coeia.constants.ApplicationConstants;
import java.sql.Connection ;
import java.sql.DriverManager ;
import java.sql.Statement ;
import java.sql.SQLException ;
import java.sql.ResultSet ;

import java.util.regex.Pattern ;

public class IP2Country {
    public static Country getCountryFromIP (String ip) throws Exception {
        Long ipLong = ipToLong(ip);
        Country c = getCountry(ipLong);
        
        return c ;
    }

    private static long ipToLong (String ip) {
        Pattern pattern = Pattern.compile("\\.");
        String[] nums = pattern.split(ip);

        int x = 3 ;
        long product = 0 ;

        for (String s: nums) {
            product += Integer.parseInt(s) * Math.pow( 256 , x--);
        }

        return (product);
    }

    private static Country getCountry ( long ip ) throws ClassNotFoundException, InstantiationException,
            SQLException, IllegalAccessException  {
        
        connectDB();
    
        String select = "SELECT * from COUNTRIES where FROM_IP <=  " + ip + " AND TO_IP >=  " + ip;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(select);

        Country item = null;

        while ( resultSet.next() ) {
            item = new Country(resultSet.getLong(1),resultSet.getLong(2),resultSet.getString(3),resultSet.getString(4),
            resultSet.getString(5));
        }

        closeDB();
        
        return (item);
    }

    private static void connectDB () throws ClassNotFoundException, InstantiationException,
    SQLException, IllegalAccessException {
        Class.forName(DB_DRIVER).newInstance();
        connection = DriverManager.getConnection(DB_NAME,DB_USER,DB_PASS);
        connection.setAutoCommit(false);
    }

    private static void closeDB () throws SQLException {
	connection.close();
    }


    //public static final String DB_URL = "countries.db" ;

    private static final String DB_NAME = "jdbc:sqlite:" + ApplicationConstants.IP_DB ;
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    private static final String DB_USER = "" ;
    private static final String DB_PASS = "" ;

    private static Connection connection ;
}