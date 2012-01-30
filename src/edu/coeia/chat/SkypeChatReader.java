/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.chat;

import edu.coeia.util.FilesPath;

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

public class SkypeChatReader implements ChatReader {
    private final String DB_NAME = "jdbc:sqlite:" ;
    private final String DB_DRIVER = "org.sqlite.JDBC";
    private final String DB_USER = "" ;
    private final String DB_PASS = "" ;
    private Connection connection ;

    @Override
    public ChatSession processFile(final File path) throws Exception {
        this.connectMozillaDB(path.getAbsolutePath());
        List<ChatMessage> msgs = getMessages();
        this.closeDB();
        
        ChatSession chat = ChatSession.newInstance(path.getName(), "", path.getAbsolutePath(), msgs);
        return chat;
    }

    private List<ChatMessage> getMessages() throws SQLException {
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
