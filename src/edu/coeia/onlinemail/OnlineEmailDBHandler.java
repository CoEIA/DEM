package edu.coeia.onlinemail;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.File;

import static edu.coeia.util.PreconditionsChecker.* ;

/**
 * EmailDatabase write EmailMessage to Email Database
 * @auther Wajdy Essam
 * @version 1.0
 */

final class OnlineEmailDBHandler {

    public OnlineEmailDBHandler(boolean nDB, String databasePath)
            throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException {

        databasePath = checkNull("database path must be not null", databasePath);
        
        DB_URL = databasePath;
        DB_NAME += DB_URL;
        if (nDB) {
           
            DB_NAME += ";create=true";
        }

        connectDB();

        if (nDB) {
            makeDBStructure();
        }
    }

    public void inserteEmail(int id,String From, String Subject, String Body,
            String Created_Date, String Sent_Date, String CC, String BCC, String Path, String FolderName)
            throws SQLException {

        String s = "insert into emails values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement psInsert = connection.prepareStatement(s);
        psInsert.setInt(1, id);
        psInsert.setString(2, From);
        psInsert.setString(3, Subject);
        psInsert.setString(4, Body);
        psInsert.setString(5, Created_Date);
        psInsert.setString(6, Sent_Date);
        psInsert.setString(7, CC);
        psInsert.setString(8, BCC);
        psInsert.setString(9, Path);
        psInsert.setString(10, FolderName);

        psInsert.executeUpdate();
        psInsert.close();
    }

    public void closeDB() throws SQLException {
        connection.close();
        DriverManager.getConnection("jdbc:derby:;shutdown=true");
    }

    private void connectDB() throws ClassNotFoundException, InstantiationException,
            SQLException, IllegalAccessException {
        Class.forName(DB_DRIVER).newInstance();
        connection = DriverManager.getConnection(DB_NAME, DB_USER, DB_PASS);
    }

    private void makeDBStructure() throws SQLException {
        Statement statement_ = connection.createStatement();

      

        String emailTables =
                "CREATE TABLE emails ("
                + "EMAILID DECIMAL(20) NOT NULL, "
                + "FROM_ADDRESS VARCHAR(500), "
                + "SUBJECT VARCHAR(500),"
                + "BODY_MESSAGE VARCHAR(32000),"
                + "CREATED_DATE VARCHAR(100),"
                + "SENT_DATE VARCHAR(100),"
                + "CC VARCHAR(5000),"
                + "BCC VARCHAR(5000),"
                + "PATH VARCHAR(5000),"
                + "FOLDER_NAME VARCHAR(400)"
                + ")";


        statement_.execute(emailTables);
        statement_.close();
    }

    public static boolean isDBExists(String Path) {
        File file = new File(Path);
        boolean isDB = false;

        if (file.exists()) {
            if (file.isDirectory()) {
                isDB = true;
            }
        }

        return isDB;
    }
    private static String DB_URL;
    private String DB_NAME = "jdbc:derby:";
    private final String DB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private final String DB_USER = "";
    private final String DB_PASS = "";
    public static Connection connection;
}
