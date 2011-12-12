package edu.coeia.filesignature;


import edu.coeia.util.FileUtil;
import edu.coeia.util.PreconditionsChecker;
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


/**
 * EmailDatabase write EmailMessage to Email Database
 * @auther Wajdy Essam
 * @version 1.0
 */
public class FileSignatureDBHandler {

    public FileSignatureDBHandler(String databasePath) throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException {
        boolean isDBFound = FileUtil.isDirectoryExists(databasePath);
        this.createDB(isDBFound, databasePath);

    }

    public void createDB(boolean nDB, String databasePath)
            throws ClassNotFoundException, InstantiationException, SQLException, IllegalAccessException {

        databasePath = PreconditionsChecker.checkNull("database path must be not null", databasePath);

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

    public List<FileSignature> getAllSignatures() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {


        String select = "SELECT * FROM signatures ";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(select);
        List<FileSignature> FileSignaturesList = new ArrayList<FileSignature>();
     

        while (resultSet.next()) {

            String ext = resultSet.getString("Extenstion");
            String[] extArray = null;
            if (!ext.isEmpty()) {
                extArray = ext.split(",");
            }
            FileSignature fs = new FileSignature(resultSet.getString("Id"), resultSet.getString("Signature"), extArray, resultSet.getString("type"));
            FileSignaturesList.add(fs);
        }
        resultSet.close();
        statement.close();

        return FileSignaturesList;

    }

    public void inserteSignature(FileSignature fs)
            throws SQLException, UnsupportedEncodingException {

        String s = "insert into signatures values(?,?,?,?)";
        PreparedStatement psInsert = connection.prepareStatement(s);
        psInsert.setString(1, fs.getID());
        psInsert.setString(1, fs.getSignature());
        psInsert.setString(1, fs.getFormattedExtension(fs.getExtension()));
        psInsert.setString(1, fs.getType());
        
       

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

        String signatureTables =
                "CREATE TABLE singatures ("
                + "Id VARCHAR(500)"
                + "Signature VARCHAR (500)"
                + "Extension VARCHAR (500)"
                + "Type      VARCHAR (500)"
                +")";


        statement_.execute(signatureTables);
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
    private String DB_URL;
    private static String DB_NAME = "jdbc:derby:";
    private static String DB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static String DB_USER = "";
    private static String DB_PASS = "";
    private Connection connection;
}
