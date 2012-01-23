/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.multimedia;

/**
 *
 * @author wajdyessam
 */

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifReader;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.exif.GpsDescriptor;

import java.io.File ;

public class GeoTagging {

    public static GPSData getGPS(String fileName) throws Exception {
        Metadata metadata = new ExifReader(new File(fileName)).extract();
        Directory gpsDir = metadata.getDirectory(GpsDirectory.class);
        GpsDescriptor gps = new GpsDescriptor(gpsDir);
        GPSData data = new GPSData();
        String tmp =
            gps.getDescription(GpsDirectory.TAG_GPS_LATITUDE_REF) + " " + gps.getDescription(GpsDirectory.TAG_GPS_LATITUDE) + " "
            + gps.getDescription(GpsDirectory.TAG_GPS_LONGITUDE_REF) + " " + gps.getDescription(GpsDirectory.TAG_GPS_LONGITUDE);

        tmp = tmp.replaceAll("\"", " ");
        tmp = tmp.replaceAll("\'", "%60");
        tmp = tmp.replaceAll(" ", "%20");

        data.location = tmp;
        data.time = gps.getDescription(GpsDirectory.TAG_GPS_TIME_STAMP);
        
        return data;
    }

    public static boolean hasGoeTag (String fileName)  {
        boolean result = false;
        try {
            Metadata metadata = new ExifReader(new File(fileName)).extract();
            Directory gpsDir = metadata.getDirectory(GpsDirectory.class);
            GpsDescriptor gps = new GpsDescriptor(gpsDir);
            
            if (  gpsDir.getString(GpsDirectory.TAG_GPS_VERSION_ID) == null ||
              gpsDir.getString(GpsDirectory.TAG_GPS_LONGITUDE)  == null ||
              gpsDir.getString(GpsDirectory.TAG_GPS_LATITUDE)   == null )
                return false ;
            
            result = true;
        }
        catch (Exception e) {
            result = false;
        }

        return result;
    }
    
    public static class GPSData {
        public long lat;
        public String latR ;
        public long lon;
        public String lonR ;

        public String location;
        public String time ;
    }
}