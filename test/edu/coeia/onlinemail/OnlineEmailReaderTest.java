    /*
    * To change this template, choose Tools | Templates
    * and open the template in the editor.
    */
    package edu.coeia.onlinemail;

    import java.io.IOException;
    import java.sql.SQLException;
    import java.util.List;
    import java.util.ArrayList;
    import javax.mail.MessagingException;
    import javax.mail.NoSuchProviderException;
    import org.junit.Test ;
    import org.junit.Before; 
    import static org.junit.Assert.* ;
    /**
    *
    * @author Ahmed
    */
    public class OnlineEmailReaderTest {

     @Test
    public void getFormattedStringTest () throws SQLException  {
     OnlineEmailReader reader = OnlineEmailReader.newInstance("xgameprogrammer@gmail.com", "windows98","C:\\Attachments","C:\\SECURE_DB");

         List<String> list = new ArrayList<String>();
         list.add("ahmed");
         list.add("mahmad");
         assertEquals("ahmed,mahmad", reader.getFormattedString(list));
    }

    @Test
    public void getFormattedStringTest1 () throws SQLException  {
     OnlineEmailReader reader = OnlineEmailReader.newInstance("xgameprogrammer@gmail.com", "windows98","C:\\Attachments","C:\\SECURE_DB");
     List<String> list = new ArrayList<String>();
     assertEquals("", reader.getFormattedString(list));

    }
    @Test
    public void getFormattedStringTest3 () throws SQLException  {
    OnlineEmailReader reader = OnlineEmailReader.newInstance("xgameprogrammer@gmail.com", "windows98","C:\\Attachments","C:\\SECURE_DB");
  
     List<String> list = new ArrayList<String>();
     
     list.add("ahmed");
     
     assertEquals("ahmed", reader.getFormattedString(list));
     }
    @Test
    
    public void readMessagesTest() throws SQLException, MessagingException, NoSuchProviderException, IOException
    {
     OnlineEmailReader reader = OnlineEmailReader.newInstance("xgameprogrammer@gmail.com", "windows98","C:\\Attachments","C:\\SECURE_DB");
     reader.Connect();
     reader.readMessages();

    }
    }
