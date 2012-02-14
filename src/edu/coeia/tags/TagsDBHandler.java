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

import java.io.IOException;
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
     */
    public static TagsDBHandler newInstance(String location) throws ClassNotFoundException,
            InstantiationException, SQLException, IllegalAccessException {
        checkNull("Location Mush have a value", location);
        checkNotEmptyString("location must be not empty string", location);
        
        return new TagsDBHandler(location);
    }
    
    private TagsDBHandler(String location) throws ClassNotFoundException,
            InstantiationException, SQLException, IllegalAccessException {
        boolean isDBFound = FileUtil.isDirectoryExists(location);
        this.createDB(isDBFound, location);
    }
    
    /**
     * Get all Tags from case database
     * @return 
     */
    List<Tag> readTagsFromDataBase() {
        List<Tag> tags = new ArrayList<Tag>();
        Connection connection = null;
        
        try {
            String select = "SELECT * FROM case_tags";
            connection = this.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            
            while ( resultSet.next() ) {
                tags.add(Tag.newInstance(resultSet.getString(1), resultSet.getTimestamp(2), 
                        resultSet.getString(3)));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.closeConnection(connection);
        }
        
        return tags;
    }
    
    /**
     * Write New Tags to database
     * Remove database records and then add the new tags
     * @param tags 
     */
    boolean writeTagsToDatabase(final List<Tag> tags) {
        boolean status = false; 
        
        try {
            this.removeTagRecords();
            
            for(Tag tag: tags) {
                this.insertTagRecord(tag);
            }
            
            status = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return (status);
    }
    
    private void createDB(boolean foundDB, String databasePath) throws ClassNotFoundException, 
            InstantiationException, SQLException, IllegalAccessException{

        databasePath = checkNull("database path must be not null", databasePath);
        DB_URL = DB_NAME + databasePath;
        
        if (!foundDB) {
            DB_URL += ";create=true";
        }

        if (!foundDB) {
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
        
        Connection connection = this.getConnection();
        
        String s = "INSERT into case_tags values(?,?,?)";
        PreparedStatement psInsert = connection.prepareStatement(s);
        
        psInsert.setString(1, tag.getName());
        psInsert.setTimestamp(2, new java.sql.Timestamp(tag.getDate().getTime()));
        psInsert.setString(3, tag.getMessage());
        
        int ret = psInsert.executeUpdate();
        psInsert.close();
        
        this.closeConnection(connection);
        return ret > 0 ;
    }

     /**
      * Remove Records from DataBase
      * @throws SQLException 
      */
    private void removeTagRecords() throws Exception{
        String command = "DELETE FROM case_tags";
        Connection connection = this.getConnection();
        PreparedStatement update = connection.prepareStatement(command);
        update.executeUpdate();
        this.closeConnection(connection);
    }
    
    private void closeConnection(final Connection connection) {
        try {
            connection.close();
        }
        catch (SQLException e){
            if ( e.getErrorCode() == 50000 && ("XJ015").equals(e.getSQLState()))
                System.out.println("Derby Shutdown normally");
            else {
                System.out.println("Derby Did not shutdown normally");
                e.printStackTrace();
            }
        }
    }

    private void makeDBStructure() throws ClassNotFoundException,
            InstantiationException, SQLException, IllegalAccessException  {
        Connection connection = this.getConnection();
        Statement statement = connection.createStatement();

        String tagsTable =
                "CREATE TABLE case_tags ("
                + "NAME VARCHAR(1024), "
                + "WHEN TIMESTAMP,"
                + "CONTENT VARCHAR(2048)"
                + ")";

        statement.execute(tagsTable);
        statement.close();
        this.closeConnection(connection);
    }
    
    private Connection getConnection() throws ClassNotFoundException, InstantiationException,
            SQLException, IllegalAccessException {
        Class.forName(DB_DRIVER).newInstance();
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
    
    private String DB_URL;
    private final static String DB_NAME = "jdbc:derby:";
    private final static String DB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private final static String DB_USER = "";
    private final static String DB_PASS = "";
}
