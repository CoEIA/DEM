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

public class SkypeMessageReader implements ChatReader {
    private final String DB_NAME = "jdbc:sqlite:" ;
    private final String DB_DRIVER = "org.sqlite.JDBC";
    private final String DB_USER = "" ;
    private final String DB_PASS = "" ;
    private Connection connection ;

    @Override
    public ChatSession processFile(final File path) throws Exception {
        connectMozillaDB(path.getAbsolutePath());
        List<ChatMessage> msgs = getMessages();
        closeDB();
        
        ChatSession chat = ChatSession.newInstance(path.getName(), "", path.getAbsolutePath(), msgs);
        return chat;
    }
    
    // take path for the folder contain the main.db skype database file
    // C:\\Documents and Settings\\wajdyessam\\Application Data\\Skype\\wajdyessam
//    public List<Tuple<String, List<ChatMessage>>> parseSkypeFile (String p) throws SQLException, ClassNotFoundException, InstantiationException,
//    IllegalAccessException {
//        File skypePath = new File(p);
//        
//        List<Tuple<String, List<ChatMessage>>> userChats = new ArrayList<Tuple<String, List<ChatMessage>>>();
//
//        Tuple<String,List<ChatMessage>> userChat = new Tuple<String,List<ChatMessage>>();
//        connectMozillaDB(skypePath.getAbsolutePath());
//
//        List<ChatMessage> msg = getMessages();
//        userChat.setA(skypePath.getName());
//        userChat.setB(msg);
//
//        userChats.add(userChat);
//        closeDB();
//
//        return userChats;
//     }

    private List<ChatMessage> getMessages ()  throws SQLException {
        List<ChatMessage> mList = new ArrayList<ChatMessage>();

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
}
