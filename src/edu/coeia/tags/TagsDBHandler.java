/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tags;

/**
 *
 * @author wajdyessam
 */

import static edu.coeia.util.PreconditionsChecker.* ;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public final class TagsDBHandler {
    
    /**
     * Get New Instance of Tags DB Handler
     * @param location
     * @return TagsDBHandler
     */
    public static TagsDBHandler newInstance(String location, boolean newDb) throws ClassNotFoundException,
            InstantiationException, SQLException, IllegalAccessException {
        checkNull("Location Mush have a value", location);
        checkNotEmptyString("location must be not empty string", location);
        
        return new TagsDBHandler(location, newDb);
    }
    
    private TagsDBHandler(String location, boolean newDb) throws ClassNotFoundException,
            InstantiationException, SQLException, IllegalAccessException {
        this.createDB(newDb, location);
    }
    
    /**
     * Get all Tags from case database
     * @return 
     */
    public List<Tag> readTagsFromDataBase() throws SQLException {
        List<Tag> tags = new ArrayList<Tag>();
        
        String select = "SELECT * FROM case_tags";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(select);

        while ( resultSet.next() ) {
            tags.add(Tag.newInstance(resultSet.getString(1), resultSet.getTimestamp(2), 
                    resultSet.getString(3)));
        }
        
        return tags;
    }
    
    /**
     * Write New Tags to database
     * Remove database records and then add the new tags
     * @param tags 
     */
    public boolean writeTagsToDatabase(final List<Tag> tags) throws Exception {
        boolean status = false; 
        
        this.removeTagRecords();

        for(Tag tag: tags) {
            this.insertTagRecord(tag);
        }

        status = true;
        
        return (status);
    }
    
    private void createDB(boolean newDb, String databasePath) throws ClassNotFoundException, 
            InstantiationException, SQLException, IllegalAccessException{

        databasePath = checkNull("database path must be not null", databasePath);
        DB_URL = DB_NAME + databasePath;
        
        this.connection = this.getConnection();
        
        if ( newDb ) {
            makeDBStructure();
        }
    }

    /**
     * Write Tag to database 
     * @param tag
     * @throws SQLException 
     */
     private boolean insertTagRecord(final Tag tag)
            throws Exception {
        
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
    private void removeTagRecords() throws Exception{
        String command = "DELETE FROM case_tags";
        PreparedStatement update = connection.prepareStatement(command);
        update.executeUpdate();
        update.close();
    }
    
    public void closeConnection() throws SQLException {
        connection.close();
    }

    private void makeDBStructure() throws ClassNotFoundException,
            InstantiationException, SQLException, IllegalAccessException  {
        Statement statement = connection.createStatement();

        String tagsTable =
                "CREATE TABLE case_tags ("
                + "NAME VARCHAR(1024), "
                + "WHEN_TIME TIMESTAMP,"
                + "CONTENT VARCHAR(2048)"
                + ")";

        statement.execute(tagsTable);
        statement.close();
    }
    
    private Connection getConnection() throws ClassNotFoundException, InstantiationException,
            SQLException, IllegalAccessException {
        Class.forName(DB_DRIVER).newInstance();
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
    
    private String DB_URL;
    private Connection connection;
    private static final String DB_NAME = "jdbc:sqlite:" ;
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    private static final String DB_USER = "";
    private static final String DB_PASS = "";
}
