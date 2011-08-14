/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.email;

/**
 *
 * @author wajdyessam
 *
 */

import edu.coeia.utility.Utilities;
import edu.coeia.utility.IP2Country;
import edu.coeia.utility.Tuple;
import edu.coeia.email.util.Message ;

import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.regex.Pattern ;
import java.util.regex.Matcher ;
import java.util.Date ;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.text.SimpleDateFormat;
import java.text.DateFormat ;
import java.text.ParseException ;

import com.pff.PSTMessage ;
import com.pff.PSTFile ;
import com.pff.PSTException ;
import com.pff.PSTObject ;

import java.io.IOException ;

public class EmailHandler {

    private PSTFile pstFile ;
    private String path ;

    private static Logger logger = Logger.getLogger("EmailHandler");
    private static FileHandler handler ;

    public EmailHandler (PSTFile pst, String path) {
        pstFile = pst ;
        this.path = path ;

        try {
            handler = new FileHandler("EmailHandler.log");
            logger.addHandler(handler);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Uncaught exception", ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, "Uncaught exception", ex);
        }
    }

    public ArrayList<Message> getInboxMessagesDate (String fromDate , String toDate) throws ParseException {
        return getFolderDate("Inbox",fromDate,toDate);
    }

    public ArrayList<Message> getOutboxMessagesDate (String fromDate , String toDate) throws ParseException {
        return getFolderDate("Sent Items",fromDate,toDate);
    }

    public ArrayList<Message> getSenderName (String fromDate , String toDate) throws ParseException {
        return getFolders("Inbox",fromDate,toDate);
    }

    public ArrayList<Message> getReceiverName (String fromDate , String toDate) throws ParseException {
        return getFolders("Sent Items",fromDate,toDate);
    }
    
    public HashMap<String,Integer> getSendersName (String fromDate, String toDate) throws ParseException {
        return getFolder("Inbox",fromDate,toDate);
    }
    
    public HashMap<String,Integer> getReceiversName (String fromDate, String toDate) throws ParseException {
        return getFolder("Sent Items",fromDate,toDate);
    }
    
    public String getUserName () {
        String userName = null ;

        ArrayList<MessageHeader> map = EmailReader.getInstance(pstFile,path,null);
         if ( map == null)
            return null;

        map = filterList("Sent Items", map);
        
        if ( map == null ) {
            System.out.println("map is null");
            logger.log(Level.INFO, "Map is Null ( user name will be empty");
            return null ;
        }

        try {
            String tmp = "";
            //System.out.println("Map size: " + map.size());
            
            for (int i=0; i<map.size() ; i++) {
                tmp = map.get(i).getFrom().trim();

                //System.out.println("tmp: " + tmp);
                
                if ( ! tmp.equals(" ") || ! tmp.isEmpty() ) {
                    //System.out.println("user name: [" + tmp + "].");
                    break;
                }
            }

            userName = tmp ;
            logger.log(Level.INFO, "User Name For OST: " + userName);
       }
       catch (Exception e){
           e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }
        
        return userName;
    }

    public HashMap<String,Integer> getEspName (String from, String to) throws ParseException {
        return getESPFolder("Inbox",from,to);
    }

    public HashMap<String,Integer> getLocations(String from, String to) throws ParseException {
        return (getLocationsFolder("Inbox",from,to));
    }

    private ArrayList<Message>  getFolderDate (String folderName, String from, String to) throws ParseException {
        ArrayList<MessageHeader> map = EmailReader.getInstance(pstFile,path,null);

         if ( map == null)
            return null;

        map = filterList(folderName, map);
        
        String user = getUserName() ;

         if ( user == null) {
            logger.log(Level.INFO, "User Name For OST: " + path + " is null");
            return null ;
        }
        
        ArrayList<Message> data = new ArrayList<Message>();

        DateFormat df = new SimpleDateFormat("d/M/yyyy");
        Date fromDate = df.parse(from);
        Date toDate   = df.parse(to);

       try {
            int row = map.size();
            for (int i=0 ; i<row ; i++ ) {
                MessageHeader m = map.get(i);
                PSTMessage msg = getMessage(m.getID());
                
                String col2 = (String) getValueAt(2,msg);
                String col3 = (String) getValueAt(3,msg);
                Date messageDate = (Date) getValueAt(4,msg);

                if ( ! messageDate.after(fromDate) || ! messageDate.before(toDate) ) {
                    continue ;
                }

                Tuple<String,ArrayList<String>> tuple = null;

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String date = formatter.format(messageDate);
                if ( folderName.equals("Inbox") ) {

                    tuple =  getSendingMessages(col2,col3);
                    String senderName = tuple.getA();

                    if ( Utilities.isFound(tuple.getB(),user)) {
                        Message message = new Message();
                        message.setSenderName(senderName);
                        message.setReceiverName(user);
                        message.setDate(date);
                        message.setNumberOfMessage(1);

                        data.add(message);
                    }
                }
                else if ( folderName.equals("Sent Items")) {
                    tuple = getReceiverNames(col2,col3);

                    for (String s: tuple.getB()) {
                        Message message = new Message();
                        message.setSenderName(user);
                        message.setReceiverName(s);
                        message.setDate(date);
                        message.setNumberOfMessage(1);

                        data.add(message);
                    }
                }
            }
       }
       catch (Exception e){
           e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }

       return ( data );
    }

    private ArrayList<Message>  getFolders (String folderName, String from, String to) throws ParseException {
        ArrayList<MessageHeader> messageHeaderList = EmailReader.getInstance(pstFile,path,null);
        if ( messageHeaderList == null)
            return null;
        
        messageHeaderList = filterList(folderName, messageHeaderList);

        String user = getUserName() ;

        if ( user == null) {
            logger.log(Level.INFO, "User Name For OST: " + path + " is null");
            return null ;
        }

        ArrayList<Message> data = new ArrayList<Message>();

        DateFormat df = new SimpleDateFormat("d/M/yyyy");
        Date fromDate = df.parse(from);
        Date toDate   = df.parse(to);

       try {
            int row = messageHeaderList.size();

            for (int i=0 ; i<row ; i++ ) {
                MessageHeader m = messageHeaderList.get(i);
                PSTMessage msg = getMessage(m.getID());

                String col2 = (String) getValueAt(2,msg);
                String col3 = (String) getValueAt(3,msg);
                Date messageDate = (Date) getValueAt(4,msg);

                if ( ! messageDate.after(fromDate) || ! messageDate.before(toDate) ) {
                    continue ;
                }

                Tuple<String,ArrayList<String>> tuple = null;

                if ( folderName.equals("Inbox") ) {

                    tuple =  getSendingMessages(col2,col3);
                    String senderName = tuple.getA();

                    if ( Utilities.isFound(tuple.getB(),user)) {
                        Message message = new Message();
                        message.setSenderName(senderName);
                        message.setReceiverName(user);
                        message.setDate(Utilities.formatDate(fromDate) + "  to  " + Utilities.formatDate(toDate));
                        message.setNumberOfMessage(1);

                        addData(data,message);
                    }
                }
                else if ( folderName.equals("Sent Items")) {
                    tuple = getReceiverNames(col2,col3);

                    for (String s: tuple.getB()) {
                        Message message = new Message();
                        message.setSenderName(s);
                        message.setReceiverName(user);
                        message.setDate(Utilities.formatDate(fromDate) + "  to  " + Utilities.formatDate(toDate));
                        message.setNumberOfMessage(1);

                        addData(data,message);
                    }
                }
            }
       }
       catch (Exception e){
           e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }

       return ( data );
    }
        
    public HashMap<String,Integer> getMessagesCountInFolder (String folderName, String from, String to) throws ParseException {
        ArrayList<MessageHeader> messageHeaderList = EmailReader.getInstance(pstFile,path,null);

        if ( messageHeaderList == null)
            return null;
        
        messageHeaderList = filterList(folderName, messageHeaderList);
        
        String user = getUserName() ;

        if ( user == null) {
            logger.log(Level.INFO, "User Name For OST: " + path + " is null");
            return null ;
        }

        HashMap<String,Integer> data2 = new HashMap<String,Integer>();
 
        DateFormat df = new SimpleDateFormat("d/M/yyyy");
        Date fromDate = df.parse(from);
        Date toDate   = df.parse(to);

       try {
            int row = messageHeaderList.size();
       
            for (int i=0 ; i<row ; i++ ) {
                MessageHeader m = messageHeaderList.get(i);
                PSTMessage msg = getMessage(m.getID());
                Date messageDate = (Date) getValueAt(4,msg);

                if ( ! messageDate.after(fromDate) || ! messageDate.before(toDate) ) {
                    continue ;
                }

                if (folderName.equals("Inbox") && m.getLocation().equals("Inbox")) {
                    String toName = m.getTo();
                    String fromName = m.getFrom();

                    //System.out.println("from: " + fromName + " to: " + toName);
                    
                    //if (toName.equals(user)) {
                        if ( data2.get(fromName) == null)
                            data2.put(fromName, 1);
                        else
                            data2.put(fromName, data2.get(fromName) + 1);
                    //}
                }
                else if ( folderName.equals("Sent Items") && m.getLocation().equals("Sent Items")) {
                    //String toName = m.getTo();
                    String fromName = m.getFrom();

                    String col2 = (String) getValueAt(2,msg);
                    String col3 = (String) getValueAt(3,msg);
                    Tuple<String,ArrayList<String>> tuple = getReceiverNames(col2,col3);

                    for (String s: tuple.getB()) {
                        if ( user.equals(fromName)) {
                            if ( data2.get(s) == null)
                                data2.put(s, 1);
                            else
                                data2.put(s, data2.get(s) + 1);
                        }
                    }
                }
            }
       }
       catch (Exception e){
           e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }

       return ( data2 );
    }
        
    private HashMap<String,Integer>  getFolder (String folderName, String from, String to) throws ParseException {
        ArrayList<MessageHeader> map = EmailReader.getInstance(pstFile,path,null);

        if ( map == null)
            return null;

        map = filterList(folderName, map);
        
        String user = getUserName() ;

        if ( user == null) {
            logger.log(Level.INFO, "User Name For OST: " + path + " is null");
            return null ;
        }
        
        HashMap<String,Integer> data = new HashMap<String,Integer>();

        DateFormat df = new SimpleDateFormat("d/M/yyyy");
        Date fromDate = df.parse(from);
        Date toDate   = df.parse(to);
        
       try {
            int row = map.size();

            for (int i=0 ; i<row ; i++ ) {
                MessageHeader m = map.get(i);
                PSTMessage msg = getMessage(m.getID());
                
                String col2 = (String) getValueAt(2,msg);
                String col3 = (String) getValueAt(3,msg);
                Date messageDate = (Date) getValueAt(4,msg);

                if ( ! messageDate.after(fromDate) || ! messageDate.before(toDate) ) {
                    continue ;
                }

                Tuple<String,ArrayList<String>> tuple = null;

                if ( folderName.equals("Inbox") ) {
                    tuple =  getSendingMessages(col2,col3);
                    String senderName = tuple.getA();

                    if ( Utilities.isFound(tuple.getB(),user)) {
                        if ( data.get(senderName) == null)
                            data.put(senderName, 1);
                        else
                            data.put(senderName, data.get(senderName) + 1);
                    }
                }
                else if ( folderName.equals("Sent Items")) {
                    tuple = getReceiverNames(col2,col3);

                    for (String s: tuple.getB()) {
                        if ( data.get(s) == null)
                            data.put(s,1);
                        else
                            data.put(s,data.get(s)+1);
                    }
                }
            }
       }
       catch (Exception e){
           e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }

       return ( data );
    }

    private HashMap<String,Integer>  getLocationsFolder (String folderName, String from, String to) throws ParseException {
        ArrayList<MessageHeader> map = EmailReader.getInstance(pstFile,path,null);

        if ( map == null)
            return null;

        map = filterList(folderName, map);
        HashMap<String,Integer> data = new HashMap<String,Integer>();

        if ( map == null ) {
            logger.log(Level.INFO, "Map For OST: " + path + " is null");
            return null ;
        }
        
        DateFormat df = new SimpleDateFormat("d/M/yyyy");
        Date fromDate = df.parse(from);
        Date toDate   = df.parse(to);

        try {
            int row = map.size();

            for (int i=0 ; i<row ; i++ ) {
                MessageHeader m = map.get(i);
                PSTMessage msg = getMessage(m.getID());
                
                Date messageDate = (Date) getValueAt(4,msg);

                if ( ! messageDate.after(fromDate) || ! messageDate.before(toDate) ) {
                    continue ;
                }

                if ( folderName.equals("Inbox") ) {
                    if ( isLocal(msg.getSenderAddrtype()) ) {
                        String line = getLine(msg.getTransportMessageHeaders());

                        if ( ! line.startsWith("Received") )
                            continue ;

                        String domain = getLocalDomain(line);
                        String ip = getLocalIp(line);

                        if ( data.get(domain) == null)
                            data.put(domain, 1);
                        else
                            data.put(domain, data.get(domain) + 1);
                    }
                    else {
                        String country = getOutIp(msg.getTransportMessageHeaders());

                        if ( data.get(country) == null)
                            data.put(country, 1);
                        else
                            data.put(country, data.get(country) + 1);

                    }
                }
                else if ( folderName.equals("Sent Items")) {
                }
            }
       }
       catch (Exception e){
           e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }

       return ( data );
    }

    private HashMap<String,Integer>  getESPFolder (String folderName, String from, String to) throws ParseException {
        ArrayList<MessageHeader> map = EmailReader.getInstance(pstFile,path,null);

        if ( map == null)
            return null;

        map = filterList(folderName, map);
        
        HashMap<String,Integer> data = new HashMap<String,Integer>();

        if ( map == null) {
            logger.log(Level.INFO, "Map For OST: " + path + " is null");
            return null ;
        }
        
        DateFormat df = new SimpleDateFormat("d/M/yyyy");
        Date fromDate = df.parse(from);
        Date toDate   = df.parse(to);

        try {
            int row = map.size();

            for (int i=0 ; i<row ; i++ ) {
                MessageHeader m = map.get(i);
                PSTMessage msg = getMessage(m.getID());
                
                String col2 = (String) getValueAt(2,msg);
                Date messageDate = (Date) getValueAt(4,msg);

                if ( ! messageDate.after(fromDate) || ! messageDate.before(toDate) ) {
                    continue ;
                }

                String esp = getESP(col2);

                if ( folderName.equals("Inbox") ) {
                    if ( data.get(esp) == null )
                        data.put(esp,1);
                    else
                        data.put(esp, data.get(esp) + 1);
                }
                else if ( folderName.equals("Sent Items")) {
                    if ( data.get(esp) == null )
                        data.put(esp,1);
                    else
                        data.put(esp, data.get(esp) + 1);
                }
            }
       }
       catch (Exception e){
           e.printStackTrace();
           logger.log(Level.SEVERE, "Uncaught exception", e);
       }

       return ( data );
    }

    private ArrayList<MessageHeader> filterList (String name, ArrayList<MessageHeader> map) {
        ArrayList<MessageHeader> msgs = new ArrayList<MessageHeader>();

        for (MessageHeader m: map) {
            if ( m.getLocation().equals(name))
                msgs.add(m);
        }

        return msgs;
    }
    
    private PSTMessage getMessage (long id)throws IOException, PSTException {
        return (PSTMessage) PSTObject.detectAndLoadPSTObject(pstFile, id);
    }
    
    private String getOutIp (String header) throws Exception {
        if ( header.contains("X-Originating-IP"))
            return getCountryFromHeader(header);

        java.util.Scanner in = new java.util.Scanner(header);
        while ( in.hasNext() ) {
            String line = in.nextLine() ;

            if ( line.startsWith("Received:")) {
                String s = getOutIP(line) ;

                if ( ! s.isEmpty() ) {
                    String x = s.split("\\[|\\]")[1];
                    String country = getOutCountry(x);

                    return country ;
                }
            }
        }

        return "" ;
    }

    private String getCountryFromHeader (String header) throws Exception {
        java.util.Scanner in = new java.util.Scanner(header);
        while ( in.hasNext() ) {
            String line = in.nextLine() ;

            if ( line.startsWith("X-Originating-IP")) {
                String s = getOutIP(line) ;

                if ( ! s.isEmpty() ) {
                    String x = s.split("\\[|\\]")[1];
                    String country = getOutCountry(x);

                    return country ;
                }
            }
        }

        return "" ;
    }

    private void addData (ArrayList<Message> data, Message m) {
        if ( Utilities.getMessageIndex(data, m) == -1 ) {
            data.add(m);
        }
        else{
            int index = Utilities.getMessageIndex(data, m);
            data.get(index).setNumberOfMessage(data.get(index).getNumberOfMessage()+1);
        }
    }

    private String getOutIP (String line) {
        String p = "\\[\\d+\\.\\d+\\.\\d+\\.\\d+\\]" ;
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(line);

        String result = "" ;
        if (matcher.find()) {
            result = matcher.group(0) ;
        }

        if ( result.startsWith("[10.") )
            result = "" ;

        return result ;
    }

    private String getOutCountry (String ip) throws Exception {
        if ( ip.isEmpty() )
            return "" ;

        return IP2Country.getCountryFromIP(ip).getName();
    }

    private String getLocalDomain (String line) {
        String p = "\\w+\\.\\w+\\.\\w+" ;
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(line);

        String result = "" ;
        if (matcher.find()) {
            result = matcher.group(0) ;
        }

        return result ;
    }

    private String getLocalIp ( String line) {
        String p = "\\d+\\.\\d+\\.\\d+\\.\\d+" ;
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(line);

        String result = "" ;
        if (matcher.find()) {
            result = matcher.group(0) ;
        }

        return result ;
    }

    private boolean isLocal (String text) {
       if (text.equalsIgnoreCase("ex"))
           return true ;

       return false ;
    }

    private String getLine (String header) {
        java.util.Scanner in = new java.util.Scanner(header);
        String s = "" ;
        if ( in.hasNext() )
            s = in.nextLine() ;

        return s ;
    }
    
    private String getESP(String line) {
	Pattern pattern = Pattern.compile("<|>");
	String from = pattern.split(line)[1];

	pattern = Pattern.compile("/O|=|@");
	String esp = null ;

	if ( from.indexOf("/O") != -1 )
            esp = pattern.split(from)[2];
	else 
            esp = pattern.split(from)[1];
	
	return esp ;
    }
    
    private Tuple<String,ArrayList<String>> getReceiverNames (String line1, String line2) throws Exception {
	Tuple<String,ArrayList<String>> data = new Tuple<String,ArrayList<String>>();
	ArrayList<String> recList = new ArrayList<String>();

	Pattern pattern = Pattern.compile("<>");
	String from = pattern.split(line1)[0];
	data.setA(from);

	pattern = Pattern.compile("<>|'|;");
	String[] to = pattern.split(line2);
	for (int i=0; i<to.length ; i++)
            if (! to[i].trim().isEmpty() )
                recList.add( to[i].trim() );

	data.setB(recList);
	return (data);
    }

    private Tuple<String,ArrayList<String>> getSendingMessages (String sendLine, String receiveLine ) throws Exception{
        Tuple<String,ArrayList<String>> tuple = new Tuple<String,ArrayList<String>>();

	Pattern pattern = Pattern.compile("<|>");
	String sent = pattern.split(sendLine)[0] ;
	tuple.setA(sent.trim());

	pattern = Pattern.compile("<");
	String[] rec = pattern.split(receiveLine) ;
	ArrayList<String> names = new ArrayList<String>();

	if ( rec.length > 1 ) {
            pattern = Pattern.compile(";");
            String[] receive = pattern.split(rec[1]) ;

            pattern = Pattern.compile(">");
            String[] cc = pattern.split(rec[0]) ;
            names.add(cc[0]);

            for (int i=1 ; i<receive.length ; i++) {
                names.add(receive[i]);
            }
        }
        else {
            names.add(rec[0].trim());
        }

        tuple.setB(names);

        return tuple;
    }

    private Object getValueAt(int col, PSTMessage next) {
    	try {
            switch (col) {
                case 0:
                    return next.getDescriptorNode().descriptorIdentifier+"";
                case 1:
                    return next.getSubject();
                case 2:
                    return next.getSentRepresentingName() + " <"+ next.getSentRepresentingEmailAddress() +">";
                case 3:
                    return next.getReceivedByName() + " <"+next.getReceivedByAddress()+">" + next.displayTo();
                case 4:
                    return next.getClientSubmitTime();
                case 5:
                    return (next.hasAttachments() ? "Yes" : "No");
                case 10:
                   return next ;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }

        return "";
    }
}