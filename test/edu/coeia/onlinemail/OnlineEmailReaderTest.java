/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.onlinemail;

import edu.coeia.util.Utilities;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 *
 * @author Ahmed
 */
public class OnlineEmailReaderTest {

    private OnlineEmailDownloader reader = null;

    @Test
    public void getFormattedStringTest() throws SQLException {
        List<String> list = new ArrayList<String>();
        list.add("ahmed");
        list.add("mahmad");
        assertEquals("ahmed,mahmad", Utilities.getFormattedString(list));
    }

    @Test
    @Ignore
    public void getFormattedStringTest1() throws SQLException {
        List<String> list = new ArrayList<String>();
        assertEquals("", Utilities.getFormattedString(list));

    }

    @Test
    @Ignore
    public void getFormattedStringTest3() throws SQLException {
        List<String> list = new ArrayList<String>();

        list.add("ahmed");

        assertEquals("ahmed", Utilities.getFormattedString(list));
    }

    @Test
    public void readMessagesTest() throws SQLException, MessagingException, NoSuchProviderException, IOException {
//        reader.Connect();
//        reader.readMessages();

    }
}
