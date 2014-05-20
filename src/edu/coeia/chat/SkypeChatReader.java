/* 
 * Copyright (C) 2014 Center of Excellence in Information Assurance
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.coeia.chat;

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
        String tmp = DB_NAME + path ;
        Class.forName(DB_DRIVER).newInstance();
        connection = DriverManager.getConnection(tmp,DB_USER,DB_PASS);
    }

    private void closeDB () throws SQLException {
        connection.close();
    }
}
