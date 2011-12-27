package edu.coeia.onlinemail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import edu.coeia.util.FileUtil;
import edu.coeia.util.Utilities;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.List;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.File;

import static edu.coeia.util.PreconditionsChecker.*;

/**
 * EmailDatabase write EmailMessage to Email Database
 * @auther Wajdy Essam
 * @version 1.0
 */
public class OnlineEmailDBHandler {

    public OnlineEmailDBHandler(String databasePath) throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException {
        boolean isDBFound = FileUtil.isDirectoryExists(databasePath);
        this.createDB(isDBFound, databasePath);

    }

    public void createDB(boolean nDB, String databasePath)
            throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException {

        databasePath = checkNull("database path must be not null", databasePath);

        DB_URL = DB_NAME + databasePath;

        if (!nDB) {
            DB_URL += ";create=true";
            System.out.println("Creating Database ");
        } else {
            System.out.println("Opening Existing Database ");
        }

        connectDB();

        if (!nDB) {
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

            InputStream subjectStream = resultSet.getAsciiStream("SUBJECT");
            String subject = Utilities.convertStreamToString(subjectStream);
            
            InputStream toStream = resultSet.getAsciiStream("TO_ADDRESS");
            List<String> toList = Utilities.getStringListFromCommaSeparatedString(Utilities.convertStreamToString(toStream));

            InputStream fromStream = resultSet.getAsciiStream("FROM_ADDRESS");
            String from = Utilities.convertStreamToString(fromStream);

            InputStream bodyStream = resultSet.getAsciiStream("BODY_MESSAGE");
            String body = Utilities.convertStreamToString(bodyStream);

            OnlineEmailMessage message = OnlineEmailMessage.newInstance(resultSet.getInt("EMAILID"),
                    resultSet.getString("USERNAME"),
                    from,
                    toList,
                    bccList,
                    ccList,
                    subject,
                    body,
                    resultSet.getString("SENT_DATE"),
                    resultSet.getString("CREATED_DATE"),
                    listPaths, resultSet.getString("Folder_Name"));
            System.out.println("added");
            mEmails.add(message);
        }

        resultSet.close();
        statement.close();

        return mEmails;

    }
 
    
    public void inserteEmail(OnlineEmailMessage msg)throws SQLException, UnsupportedEncodingException, IOException 
    {
        String ccBuilder = Utilities.getCommaSeparatedStringFromCollection(msg.getCC());
        String bccBuilder = Utilities.getCommaSeparatedStringFromCollection(msg.getBCC());
        String toBuilder = Utilities.getCommaSeparatedStringFromCollection(msg.getTo());
        String attachments = Utilities.getCommaSeparatedStringFromCollection(msg.getAttachments());

        String s = "insert into emails values(?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement psInsert = connection.prepareStatement(s);
        
        psInsert.setString(1, msg.getUsername());
        psInsert.setInt(2, msg.getId());
        
        InputStream fromStream = new ByteArrayInputStream(msg.getFrom().getBytes());
        psInsert.setAsciiStream(3,fromStream);
               
        InputStream toStream = new ByteArrayInputStream(toBuilder.getBytes());
        psInsert.setAsciiStream(4, toStream);
        
        InputStream subjectStream = new ByteArrayInputStream(msg.getSubject().getBytes());
        psInsert.setAsciiStream(5, subjectStream);
        
        InputStream bodyStream = new ByteArrayInputStream(msg.getBody().getBytes());
        psInsert.setAsciiStream(6, bodyStream);
        
        psInsert.setString(7, msg.getReceiveDate());
        psInsert.setString(8, msg.getSentDate());
        psInsert.setString(9, ccBuilder);
        psInsert.setString(10, bccBuilder);
        psInsert.setString(11, attachments);
        psInsert.setString(12, msg.getFolderName());

        psInsert.executeUpdate();
        psInsert.close();
    }

    public void closeDB() throws SQLException {
        connection.close();
        DriverManager.getConnection("jdbc:derby:;shutdown=true");
    }

    public void connectDB() throws ClassNotFoundException, InstantiationException,
            SQLException, IllegalAccessException {
        Class.forName(DB_DRIVER).newInstance();
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    private void makeDBStructure() throws SQLException {
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

 
    private String DB_URL;
    private static String DB_NAME = "jdbc:derby:";
    private static String DB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static String DB_USER = "";
    private static String DB_PASS = "";
    private Connection connection;
}
