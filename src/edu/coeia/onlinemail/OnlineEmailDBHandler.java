package edu.coeia.onlinemail;

import edu.coeia.util.FileUtil;
import edu.coeia.util.Utilities;
import static edu.coeia.util.PreconditionsChecker.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.List;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;


/**
 * EmailDatabase write EmailMessage to Email Database
 * @auther Wajdy Essam
 * @version 1.0
 */

public final class OnlineEmailDBHandler {

    public OnlineEmailDBHandler (final String databasePath) throws Exception{
        boolean isDBFound = FileUtil.isDirectoryExists(databasePath);
        this.createDB(isDBFound, databasePath);
    }

    private void createDB( boolean isFoundDatabase, String databasePath) throws Exception {
        databasePath = checkNull("database path must be not null", databasePath);
        DB_URL = DB_NAME + databasePath;

        if (!isFoundDatabase)
            DB_URL += ";create=true";
        
        connectDB();
        
        if (!isFoundDatabase) {
            makeDBStructure();
        }
    }

    public List<OnlineEmailMessage> getAllMessages() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String select = "SELECT * FROM emails ";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(select);
        
        List<OnlineEmailMessage> mEmails = new ArrayList<OnlineEmailMessage>();

        while (resultSet.next()) {
            List<String> listPaths = Utilities.getStringListFromCommaSeparatedString(resultSet.getString("PATH"));
            List<String> bccList = Utilities.getStringListFromCommaSeparatedString(resultSet.getString("BCC"));
            List<String> ccList = Utilities.getStringListFromCommaSeparatedString(resultSet.getString("CC"));
            List<String> toList = Utilities.getStringListFromCommaSeparatedString(resultSet.getString("TO_ADDRESS"));
            
            String subject = resultSet.getString("SUBJECT");
            String from = resultSet.getString("FROM_ADDRESS");
            String body = resultSet.getString("BODY_MESSAGE");
            int id = resultSet.getInt("EMAILID");
            String userName = resultSet.getString("USERNAME");
            String sendDate = resultSet.getString("SENT_DATE");
            String createdDate = resultSet.getString("CREATED_DATE");
            String folderName = resultSet.getString("Folder_Name");
            
            OnlineEmailMessage message = OnlineEmailMessage.newInstance(id,
                    userName,
                    from,
                    toList,
                    bccList,
                    ccList,
                    subject,
                    body,
                    sendDate,
                    createdDate,
                    listPaths, 
                    folderName);
            mEmails.add(message);
        }

        resultSet.close();
        statement.close();

        return mEmails;
    }
 
    public void inserteEmail(final OnlineEmailMessage msg) throws SQLException, UnsupportedEncodingException,
             IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String ccBuilder = Utilities.getCommaSeparatedStringFromCollection(msg.getCC());
        String bccBuilder = Utilities.getCommaSeparatedStringFromCollection(msg.getBCC());
        String toBuilder = Utilities.getCommaSeparatedStringFromCollection(msg.getTo());
        String attachments = Utilities.getCommaSeparatedStringFromCollection(msg.getAttachments());

        String s = "insert into emails values(?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement psInsert = connection.prepareStatement(s);
        
        psInsert.setString(1, msg.getUsername());
        psInsert.setInt(2, msg.getId());
        psInsert.setString(3,msg.getFrom());
        psInsert.setString(4, toBuilder);
        psInsert.setString(5, msg.getSubject());
        psInsert.setString(6, msg.getBody());
        psInsert.setString(7, msg.getReceiveDate());
        psInsert.setString(8, msg.getSentDate());
        psInsert.setString(9, ccBuilder);
        psInsert.setString(10, bccBuilder);
        psInsert.setString(11, attachments);
        psInsert.setString(12, msg.getFolderName());

        psInsert.executeUpdate();
        psInsert.close();
    }

    private void makeDBStructure() throws Exception {
        Statement statement_ = connection.createStatement();

        String emailTables =
                "CREATE TABLE emails ("
                + "USERNAME VARCHAR(500),"
                + "EMAILID DECIMAL(20) NOT NULL, "
                + "FROM_ADDRESS CLOB(1 M), "
                + "TO_ADDRESS CLOB(1 M),"
                + "SUBJECT CLOB(1 M),"
                + "BODY_MESSAGE CLOB(10 M),"
                + "CREATED_DATE VARCHAR(100),"
                + "SENT_DATE VARCHAR(100),"
                + "CC VARCHAR(10000),"
                + "BCC VARCHAR(10000),"
                + "PATH VARCHAR(10000),"
                + "FOLDER_NAME VARCHAR(500)"
                + ")";


        statement_.execute(emailTables);
        statement_.close();
    }

    public void commit() throws SQLException{
        connection.commit();
    }
    
    private void connectDB() throws ClassNotFoundException, InstantiationException,
            SQLException, IllegalAccessException {
        Class.forName(DB_DRIVER).newInstance();
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
       
    public void closeDB() throws SQLException {
        connection.close();
        DriverManager.getConnection("jdbc:derby:;shutdown=true");
    }
    
    private String DB_URL;
    private static String DB_NAME = "jdbc:derby:";
    private static String DB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static String DB_USER = "";
    private static String DB_PASS = "";
    private Connection connection;
}
