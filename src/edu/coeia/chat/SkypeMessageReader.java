/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.chat;

import edu.coeia.util.FilesPath;
import edu.coeia.util.Tuple ;

import java.io.File;

import java.sql.SQLException ;
import java.sql.DriverManager ;
import java.sql.Connection ;
import java.sql.Statement ;
import java.sql.ResultSet ;

import java.util.ArrayList ;
import java.util.List;

/**
 *
 * @author wajdyessam
 *
 */

public class SkypeMessageReader {
    private String path ;
    
    private final String DB_NAME = "jdbc:sqlite:" ;
    private final String DB_DRIVER = "org.sqlite.JDBC";
    private final String DB_USER = "" ;
    private final String DB_PASS = "" ;
    private Connection connection ;
    
    public SkypeMessageReader() {
    }
    
    public SkypeMessageReader (String path) {
        this.path = FilesPath.getSkypePath(path);
    }

    public String getPath () { return path ; }

    // take path for the folder contain the main.db skype database file
    // C:\\Documents and Settings\\wajdyessam\\Application Data\\Skype\\wajdyessam
    public List<Tuple<String, List<SkypeMessage>>> parseSkypeFile (String p) throws SQLException, ClassNotFoundException, InstantiationException,
    IllegalAccessException {
        File skypePath = new File(p);
        
        List<Tuple<String, List<SkypeMessage>>> userChats = new ArrayList<Tuple<String, List<SkypeMessage>>>();

        Tuple<String,List<SkypeMessage>> userChat = new Tuple<String,List<SkypeMessage>>();
        connectMozillaDB(skypePath.getAbsolutePath());

        List<SkypeMessage> msg = getMessages();
        userChat.setA(skypePath.getName());
        userChat.setB(msg);

        userChats.add(userChat);
        closeDB();

        return userChats;
     }

    private List<SkypeMessage> getMessages ()  throws SQLException {
        List<SkypeMessage> mList = new ArrayList<SkypeMessage>();

        String select =
            "select Messages.author,Messages.dialog_partner,datetime(Messages.timestamp,'unixepoch'),Messages.body_xml " +
            "from messages" ;

        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(select);

        while ( resultSet.next() ) {
            SkypeMessage msg = new SkypeMessage(resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4));
            
            mList.add(msg);
        }

        statement.close();
        return mList ;
    }
    
    private void connectMozillaDB (String path) throws SQLException, ClassNotFoundException, InstantiationException,
    IllegalAccessException {
        String tmp = DB_NAME + path + "\\" + FilesPath.SKYPE_DB ;
        Class.forName(DB_DRIVER).newInstance();
        connection = DriverManager.getConnection(tmp,DB_USER,DB_PASS);
    }

    private void closeDB () throws SQLException {
        connection.close();
    }

    private boolean isSkypeUserPath (File filePath) {
        File[] files = filePath.listFiles() ;

        if ( files != null ) {
            for (File file : files) {
                if ( file.isFile() && file.getName().equalsIgnoreCase(FilesPath.SKYPE_DB) )
                    return (true);
            }
        }

        return false ;
    }
}
