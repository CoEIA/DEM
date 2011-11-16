/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tags;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.util.FileUtil;
import static edu.coeia.util.PreconditionsChecker.* ;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

final class TagsDBHandler {
    
    /**
     * Get New Instance of Tags DB Handler
     * @param location
     * @return TagsDBHandler
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws SQLException
     * @throws IllegalAccessException 
     */
    public static TagsDBHandler newInstance(String location) 
        throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException {
        
        checkNull("Location Mush have a value", location);
        checkNotEmptyString("location must be not empty string", location);
        
        return new TagsDBHandler(location);
    }
    
    private TagsDBHandler(String location) 
        throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException {

        boolean isDBFound = FileUtil.isDirectoryExists(location);
        this.createDB(isDBFound, location);
    }
    
    /**
     * Get all Tags from case database
     * @return 
     */
    List<Tag> getTags() {
        List<Tag> tags = new ArrayList<Tag>();
        
        try {
            String select = "SELECT * FROM case_tags";
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            
            while ( resultSet.next() ) {
                tags.add(Tag.newInstance(resultSet.getString(1), resultSet.getTimestamp(2), 
                        resultSet.getString(3)));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return tags;
    }
    
    /**
     * Write New Tags to database
     * Remove database records and then add the new tags
     * @param tags 
     */
    boolean setTags(final List<Tag> tags) {
        boolean status = false; 
        
        try {
            this.removeRecords();
            
            for(Tag tag: tags) {
                this.insertRecord(tag);
            }
            
            status = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return (status);
    }
    
    private void createDB(boolean foundDB, String databasePath)
        throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException {

        databasePath = checkNull("database path must be not null", databasePath);
        
        DB_URL = DB_NAME + databasePath;
        
        if (!foundDB) {
            DB_URL += ";create=true";
        }

        connectDB();

        if (!foundDB) {
            makeDBStructure();
        }
    }

    /**
     * Write Tag to database 
     * @param tag
     * @throws SQLException 
     */
     private boolean insertRecord(final Tag tag)
            throws SQLException {
        
        String s = "INSERT into case_tags values(?,?,?)";
        PreparedStatement psInsert = connection.prepareStatement(s);
        
        psInsert.setString(1, tag.getName());
        psInsert.setTimestamp(2, new java.sql.Timestamp(tag.getDate().getTime()));
        psInsert.setString(3, tag.getMessage());
        
        int ret = psInsert.executeUpdate();
        psInsert.close();
        
        return ret > 0 ;
    }

     /**
      * Remove Records from DataBase
      * @throws SQLException 
      */
    private void removeRecords() throws SQLException{
        String command = "DELETE FROM case_tags";
        PreparedStatement update = getConnection().prepareStatement(command);
        update.executeUpdate();
    }
    
    void closeDB() throws SQLException {
        connection.close();
        DriverManager.getConnection("jdbc:derby:;shutdown=true");

    }

    private void connectDB() throws ClassNotFoundException, InstantiationException,
            SQLException, IllegalAccessException {
        Class.forName(DB_DRIVER).newInstance();
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    private void makeDBStructure() throws SQLException {
        Statement statement = connection.createStatement();

        String tagsTable =
                "CREATE TABLE case_tags ("
                + "NAME VARCHAR(255), "
                + "WHEN TIMESTAMP,"
                + "CONTENT VARCHAR(255)"
                + ")";

        statement.execute(tagsTable);
        statement.close();
    }
    
    private Connection getConnection() {
        return connection;
    }
    
    private String DB_URL;
    private final static String DB_NAME = "jdbc:derby:";
    private final static String DB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private final static String DB_USER = "";
    private final static String DB_PASS = "";
    private Connection connection;
}
