/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.utility;

/**
 *
 * @author wajdyessam
 *
 */

/*
 * IP2Country
 * Goal: to get country name from ip
 * Written by: Wajdy Essam , 11/6/2010
 * FeedBack: wajdyessam[AT]hotmail[DOT]com
 * feel free to use this source code in your programs
 * database for ip number from: http://ip-to-country.webhosting.info
*/

import java.sql.Connection ;
import java.sql.DriverManager ;
import java.sql.Statement ;
import java.sql.SQLException ;
import java.sql.ResultSet ;

import java.util.regex.Pattern ;

public class IP2Country {

//    public IP2Country () throws FileNotFoundException,IOException, URISyntaxException {
//        filePath = new File(FilesPath.IP_DB);
//
//        compareCountry = new Comparator<Country>() {
//            public int compare (Country c1, Country c2) {
//                if ( c2.getFrom() >= c1.getFrom() && c2.getTo() <= c1.getTo() )
//                    return 0 ;	// c2 equals c1
//                else if ( c2.getFrom() > c1.getFrom() && c2.getTo() > c1.getTo() )
//                    return -1 ;	// c2 less than c1
//                else
//                    return 1 ;	// c2 graeter than c1
//            }
//        };
//
//        countryList = new ArrayList<Country>();
//        loadFile();
//        Collections.sort(countryList, compareCountry);
//    }
//
//    public Country getCountry (String ip) {
//        Long ipLong = ipToLong(ip);
//        int index = Collections.binarySearch(countryList, new Country(ipLong), compareCountry);
//
//        if ( index < 0 )
//            return null ;
//        else
//            return countryList.get(index);
//    }
//
//    // to convert integer to ip - assume x is the integer:
//    // A: x/(256*256*256) % 256
//    // B: x/(256*256) % 256
//    // C: x/256 % 256
//    // D: x % 256
//    private static String longToIp (long longIP ) {
//        StringBuilder ip = new StringBuilder("");
//
//        int x = 3 ;
//        for (int i=0 ; i<4 ; i++ ){
//            ip.append ( (int) ( (longIP / Math.pow( 256 , x--)) % 256 ) );
//
//            if ( i < 3 )
//                ip.append(".");
//        }
//
//        return (ip.toString());
//    }
//
//    // to convert ip to integer:
//    // A.B.C.D = A*256*256*256 + B*256*256 + C*256 + D
//    private long ipToLong (String ip) {
//        Pattern pattern = Pattern.compile("\\.");
//        String[] nums = pattern.split(ip);
//
//        int x = 3 ;
//        long product = 0 ;
//
//        for (String s: nums) {
//            product += Integer.parseInt(s) * Math.pow( 256 , x--);
//        }
//
//        return (product);
//    }
//
//    // raed using memory mapped method
//    private void loadFile () throws FileNotFoundException,IOException {
//        FileInputStream input = new FileInputStream( filePath );
//        FileChannel channel = input.getChannel();
//        int length = (int) channel.size();
//        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);
//
//        StringBuilder line = new StringBuilder("");
//        int ch = 0 ;
//
//        for (int i=0; i<length ; i++) {
//            ch = buffer.get(i);
//
//            if ( ch == '\n' ) {
//                Country c = getCountryFromLine(line.toString());
//                countryList.add(c);
//                line.delete( 0, line.length() );
//            }
//            else {
//                line.append((char) ch);
//            }
//        }
//    }
//
//    // read CSV file
//    private Country getCountryFromLine (String line) {
//        Pattern pattern = Pattern.compile(",");
//        String texts[] = pattern.split(line);
//        String[] result =  new String[texts.length];
//
//        for (int i=0 ; i<texts.length ; i++)
//            result[i] = texts[i].split("\"")[1];
//
//        Country country = new Country(Long.parseLong(result[0]), Long.parseLong(result[1]), result[2], result[3], result[4] );
//        return (country) ;
//    }
//
//    public void freeMemory () {
//        for (int i=0; i<countryList.size(); i++) {
//            countryList.get(i).releaseMemory();
//        }
//
//        countryList.clear();
//        countryList = null;
//        compareCountry = null ;
//    }
//
//    private File filePath ;
//    private ArrayList<Country> countryList ;
//    private Comparator<Country> compareCountry ;


    public static Country getCountryFromIP (String ip) throws Exception {
        Long ipLong = ipToLong(ip);

        Country c = getCountry(ipLong);
        return c ;
    }

    private static long ipToLong (String ip) {
        Pattern pattern = Pattern.compile("\\.");
        String[] nums = pattern.split(ip);

        int x = 3 ;
        long product = 0 ;

        for (String s: nums) {
            product += Integer.parseInt(s) * Math.pow( 256 , x--);
        }

        return (product);
    }

    private static Country getCountry ( long ip ) throws ClassNotFoundException, InstantiationException,
    SQLException, IllegalAccessException  {
    connectDB();
    
    String select = "SELECT * from COUNTRIES where FROM_IP <=  " + ip + " AND TO_IP >=  " + ip;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(select);

        Country item = null;

        while ( resultSet.next() ) {
            item = new Country(resultSet.getLong(1),resultSet.getLong(2),resultSet.getString(3),resultSet.getString(4),
            resultSet.getString(5));
        }

        closeDB();
        
        return (item);
    }

    private static void connectDB () throws ClassNotFoundException, InstantiationException,
    SQLException, IllegalAccessException {
        Class.forName(DB_DRIVER).newInstance();
        connection = DriverManager.getConnection(DB_NAME,DB_USER,DB_PASS);
        connection.setAutoCommit(false);
    }

    private static void closeDB () throws SQLException {
	connection.close();
    }


    //public static final String DB_URL = "countries.db" ;

    private static String DB_NAME = "jdbc:sqlite:" + FilesPath.IP_DB ;
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    private static final String DB_USER = "" ;
    private static final String DB_PASS = "" ;

    private static Connection connection ;
}