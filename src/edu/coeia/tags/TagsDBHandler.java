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

class TagsDBHandler {
    private String dbLocation ;
    
    /**
     * Get Instance of Tags DB Handler
     * @param location
     * @return
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
        
        this.dbLocation = location ;
        
        boolean isDBFound = FileUtil.isDirectoryExists(location);
        this.createDB(isDBFound, dbLocation);
    }
    
    /**
     * Get all Tags from case database
     * @return 
     */
    List<Tag> getTags() {
        List<Tag> tags = new ArrayList<Tag>();
        
        try {
            String select = "SELECT * FROM tags ";
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            
            while ( resultSet.next() ) {
                tags.add(Tag.newInstance(resultSet.getString(1), resultSet.getTimestamp(2), 
                        resultSet.getString(3)));
            }
        }
        catch (Exception e) {
        }
        
        return tags;
    }
    
    /**
     * Write New Tags to database
     * Remove database records and then add the new tags
     * @param tags 
     */
    void setTags(final List<Tag> tags) {
        try {
            this.removeRecords();
            
            for(Tag tag: tags) {
                this.insertRecord(tag);
            }
        }
        catch (Exception e) {
        }
    }
    
    private void createDB(boolean foundDB, String databasePath)
        throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException {

        databasePath = checkNull("database path must be not null", databasePath);
        
        DB_URL = databasePath;
        DB_NAME += DB_URL;
        
        if (!foundDB) {
            DB_NAME += ";create=true";
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
     private void insertRecord(final Tag tag)
            throws SQLException {
        
        String s = "insert into case_tags values(?,?,?)";
        PreparedStatement psInsert = connection.prepareStatement(s);
        
        psInsert.setString(1, tag.getName());
        psInsert.setTimestamp(2, new java.sql.Timestamp(tag.getDate().getTime()));
        psInsert.setString(3, tag.getMessage());
        
        psInsert.executeUpdate();
        psInsert.close();
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
        connection = DriverManager.getConnection(DB_NAME, DB_USER, DB_PASS);
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
    
    private static String DB_URL;
    private static String DB_NAME = "jdbc:derby:";
    private static String DB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static String DB_USER = "";
    private static String DB_PASS = "";
    private  Connection connection;
}
