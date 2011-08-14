/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.chat;

import edu.coeia.main.util.FilesPath;
import edu.coeia.main.util.Tuple ;

import java.io.File;

import java.sql.SQLException ;
import java.sql.DriverManager ;
import java.sql.Connection ;
import java.sql.Statement ;
import java.sql.ResultSet ;

import java.util.ArrayList ;

/**
 *
 * @author wajdyessam
 *
 */

public class SkypeParser {
    private String path ;
    
    private static String DB_NAME = "jdbc:sqlite:" ;
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    private static final String DB_USER = "" ;
    private static final String DB_PASS = "" ;
    private Connection connection ;
    
    public SkypeParser() {
    }
    
    public SkypeParser (String path) {
        this.path = FilesPath.getSkypePath(path);
    }

    public String getPath () { return path ; }
    
    public ArrayList<Tuple<String, ArrayList<SkypeMessage>>> parse () throws SQLException, ClassNotFoundException, InstantiationException,
    IllegalAccessException {
        File file = new File(path);
        ArrayList<Tuple<String, ArrayList<SkypeMessage>>> userChats = new ArrayList<Tuple<String, ArrayList<SkypeMessage>>>();
        
        if ( file.exists() ) {
            File[] files = file.listFiles();

            for (File skypePath: files) {
                if ( isSkypeUserPath(skypePath) ) {
                    Tuple<String,ArrayList<SkypeMessage>> userChat = new Tuple<String,ArrayList<SkypeMessage>>();
                    connectMozillaDB(skypePath.getAbsolutePath());

                    ArrayList<SkypeMessage> msg = getMessages();
                    userChat.setA(skypePath.getName());
                    userChat.setB(msg);

                    userChats.add(userChat);
                    closeDB();
                }
            }
        }

        return userChats;
    }

    public ArrayList<Tuple<String, ArrayList<SkypeMessage>>> parseSkypeFile (String p) throws SQLException, ClassNotFoundException, InstantiationException,
    IllegalAccessException {
        File skypePath = new File(p);
        
        ArrayList<Tuple<String, ArrayList<SkypeMessage>>> userChats = new ArrayList<Tuple<String, ArrayList<SkypeMessage>>>();

        Tuple<String,ArrayList<SkypeMessage>> userChat = new Tuple<String,ArrayList<SkypeMessage>>();
        connectMozillaDB(skypePath.getAbsolutePath());

        ArrayList<SkypeMessage> msg = getMessages();
        userChat.setA(skypePath.getName());
        userChat.setB(msg);

        userChats.add(userChat);
        closeDB();

        return userChats;
     }

    private ArrayList<SkypeMessage> getMessages ()  throws SQLException {
        ArrayList<SkypeMessage> mList = new ArrayList<SkypeMessage>();

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
