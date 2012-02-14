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

public final class TagsDBHandler {
    
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
    public List<Tag> readTagsFromDataBase() {
        List<Tag> tags = new ArrayList<Tag>();
        
        try {
            String select = "SELECT * FROM case_tags";
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
        
        return tags;
    }
    
    /**
     * Write New Tags to database
     * Remove database records and then add the new tags
     * @param tags 
     */
    public boolean writeTagsToDatabase(final List<Tag> tags) {
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
        DATABASE_NAME_PATH = DB_URL;
        
        if (!foundDB) {
            DB_URL += ";create=true";
        }

        this.connection = this.getConnection();
        
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
    }
    
    public void closeConnection() {
        try {
            connection.close();
            DriverManager.getConnection(String.format("jdbc:derby:;shutdown=true"));
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
        Statement statement = connection.createStatement();

        String tagsTable =
                "CREATE TABLE case_tags ("
                + "NAME VARCHAR(1024), "
                + "WHEN TIMESTAMP,"
                + "CONTENT VARCHAR(2048)"
                + ")";

        statement.execute(tagsTable);
        statement.close();
    }
    
    private Connection getConnection() throws ClassNotFoundException, InstantiationException,
            SQLException, IllegalAccessException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
    
    private String DB_URL;
    private Connection connection;
    private String DATABASE_NAME_PATH = "";
    private final String DB_NAME = "jdbc:derby:";
    private final String DB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private final String DB_USER = "";
    private final String DB_PASS = "";
}
