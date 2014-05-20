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
package edu.coeia.internet;

/**
 *
 * @author wajdyessam
 *
 */

import java.sql.Connection ;
import java.sql.DriverManager ;
import java.sql.Statement ;
import java.sql.ResultSet ;
import java.sql.SQLException ;

import java.util.ArrayList ;

public class MozillaHandler {
    
    public void connectMozillaDB (String db) throws SQLException, ClassNotFoundException, InstantiationException,
    IllegalAccessException {
        String tmp = DB_NAME + db ;
        Class.forName(DB_DRIVER).newInstance();
        connection = DriverManager.getConnection(tmp,DB_USER,DB_PASS);
    }

    public ArrayList<ArrayList<String>> getSummary () throws SQLException {
      Statement statement = connection.createStatement();

        String select =
            "SELECT url, datetime(moz_historyvisits.visit_date/1000000,'unixepoch'), moz_places.title , moz_places.typed "+
            "FROM moz_places, moz_historyvisits "+
            "WHERE moz_places.id = moz_historyvisits.place_id "+
            "ORDER by visit_date ";

        ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>() ;

        ResultSet resultSet = statement.executeQuery(select);

	while ( resultSet.next() ) {
            ArrayList<String> row = new ArrayList<String>();

            for (int i=1 ; i<=4 ; i++)
                row.add(resultSet.getString(i));

            rows.add(row);
         }

        statement.close();
        return (rows);
    }
    
    public ArrayList<ArrayList<String>> getSearching () throws SQLException {
      Statement statement = connection.createStatement();

        String select =
            "SELECT url, datetime(moz_historyvisits.visit_date/1000000,'unixepoch') "+
            "FROM moz_places, moz_historyvisits "+
            "WHERE moz_places.id = moz_historyvisits.place_id AND (url LIKE \"%search?q=%\" OR url LIKE \"%search?p=%\" OR url LIKE \"%results.aspx?q=%\" OR url LIKE \"%web?q=%\") "+
            "ORDER by visit_date ";

        ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>() ;

        ResultSet resultSet = statement.executeQuery(select);

	while ( resultSet.next() ) {
            ArrayList<String> row = new ArrayList<String>();

            for (int i=1 ; i<=2 ; i++)
                row.add(resultSet.getString(i));

            rows.add(row);
         }

        statement.close();
        return (rows);
    }
    
    public ArrayList<ArrayList<String>> getMostVisitiedURL () throws SQLException {
      Statement statement = connection.createStatement();

        String select =
            "SELECT url, moz_places.title  , moz_places.visit_count, moz_places.typed "+
            "FROM moz_places "+
            "ORDER by visit_count DESC "+
            "LIMIT 20 ";

        ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>() ;

        ResultSet resultSet = statement.executeQuery(select);

	while ( resultSet.next() ) {
            ArrayList<String> row = new ArrayList<String>();

            for (int i=1 ; i<=3 ; i++)
                row.add(resultSet.getString(i));

            rows.add(row);
         }

        statement.close();
        return (rows);
    }
    
    public ArrayList<ArrayList<String>> getMostVisitiedHost () throws SQLException {
      Statement statement = connection.createStatement();

        String select =
            "SELECT rev_host , sum(visit_count)" +
            "FROM moz_places "+
            "WHERE rev_host <> \".\" AND rev_host <> \"\" " +
            "group by rev_host "+
            "ORDER by sum(visit_count) DESC "+
            "LIMIT 20 ";

        ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>() ;

        ResultSet resultSet = statement.executeQuery(select);

	while ( resultSet.next() ) {
            ArrayList<String> row = new ArrayList<String>();

            for (int i=1 ; i<=2 ; i++)
                row.add(resultSet.getString(i));

            rows.add(row);
         }

        statement.close();
        return (rows);
    }
    
    public ArrayList<ArrayList<String>> getPlcaseHistory () throws SQLException {
        Statement statement = connection.createStatement();

        String select =
            "SELECT datetime(moz_historyvisits.visit_date/1000000,'unixepoch'), moz_places.url , moz_places.title  , moz_places.visit_count , moz_places.typed "
            + "FROM moz_places, moz_historyvisits "
            + "WHERE moz_places.id = moz_historyvisits.place_id" ;
        
        ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>() ;

        ResultSet resultSet = statement.executeQuery(select);

	while ( resultSet.next() ) {
            ArrayList<String> row = new ArrayList<String>();

            for (int i=1 ; i<=5 ; i++)
                row.add(resultSet.getString(i));
            
            rows.add(row);
         }

        statement.close();
        return (rows);
    }

    public ArrayList<ArrayList<String>> getBookmark () throws SQLException {
        Statement statement = connection.createStatement();

        String select =
            "SELECT moz_bookmarks.title,moz_places.url,datetime(moz_bookmarks.dateAdded/1000000,'unixepoch') " +
            "FROM moz_bookmarks " +
            "LEFT JOIN moz_places " +
            "WHERE moz_bookmarks.fk = moz_places.id AND moz_bookmarks.title != 'null' AND moz_places.url LIKE '%http%'" ;

        ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>() ;

        ResultSet resultSet = statement.executeQuery(select);

	while ( resultSet.next() ) {
            ArrayList<String> row = new ArrayList<String>();

            for (int i=1 ; i<=3 ; i++)
                row.add(resultSet.getString(i));

            rows.add(row);
         }

        statement.close();
        return (rows);
    }

    public ArrayList<ArrayList<String>> getCookies () throws SQLException {
        Statement statement = connection.createStatement();

        String select =
            "SELECT moz_cookies.host,moz_cookies.path,moz_cookies.name,moz_cookies.value," +
            "datetime(moz_cookies.lastAccessed/1000000,'unixepoch'),datetime(moz_cookies.expiry/1000000,'unixepoch')" +
            ",moz_cookies.isSecure,moz_cookies.isHttpOnly " +
            "FROM moz_cookies" ;

        ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>() ;

        ResultSet resultSet = statement.executeQuery(select);

	while ( resultSet.next() ) {
            ArrayList<String> row = new ArrayList<String>();

            for (int i=1 ; i<=8 ; i++)
                row.add(resultSet.getString(i));

            rows.add(row);
         }

        statement.close();
        return (rows);
    }

    public ArrayList<ArrayList<String>> getDownloads () throws SQLException {
        Statement statement = connection.createStatement();

        String select =
            "SELECT name,source,target,datetime(moz_downloads.startTime/1000000,'unixepoch'),datetime(moz_downloads.endTime/1000000,'unixepoch') "
            + "FROM moz_downloads" ;

        ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>() ;

        ResultSet resultSet = statement.executeQuery(select);

	while ( resultSet.next() ) {
            ArrayList<String> row = new ArrayList<String>();

            for (int i=1 ; i<=5 ; i++)
                row.add(resultSet.getString(i));

            rows.add(row);
         }

        statement.close();
        return (rows);
    }

    public ArrayList<ArrayList<String>> getLoggins () throws SQLException {
        Statement statement = connection.createStatement();

        String select =
            "SELECT hostname,usernameField,passwordField,encryptedUsername,encryptedPassword " +
            "FROM moz_logins" ;

        ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>() ;

        ResultSet resultSet = statement.executeQuery(select);

	while ( resultSet.next() ) {
            ArrayList<String> row = new ArrayList<String>();

            for (int i=1 ; i<=5 ; i++)
                row.add(resultSet.getString(i));

            rows.add(row);
         }

        statement.close();
        return (rows);
    }
    
    public void closeDB () throws SQLException {
        connection.close();
    }

    private Connection connection ;

    public static final String PLCASES_DB =  "places.sqlite" ;
    public static final String COOKIES_DB =  "cookies.sqlite" ;
    public static final String DOWNLOAD_DB = "downloads.sqlite" ;
    public static final String LOGGIN_DB  =  "signons.sqlite" ;
    
    private static String DB_NAME = "jdbc:sqlite:" ;
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    private static final String DB_USER = "" ;
    private static final String DB_PASS = "" ;
}
