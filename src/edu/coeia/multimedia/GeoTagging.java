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
        GPSData data = new GPSData();
        
//        Metadata metadata = new ExifReader(new File(fileName)).extract();
//        
//        Directory gpsDir = metadata.getDirectory(GpsDirectory.class);
//        GpsDescriptor gps = new GpsDescriptor(gpsDir);        
//        String tmp =
//            gps.getDescription(GpsDirectory.TAG_GPS_LATITUDE_REF) + " " + gps.getDescription(GpsDirectory.TAG_GPS_LATITUDE) + " "
//            + gps.getDescription(GpsDirectory.TAG_GPS_LONGITUDE_REF) + " " + gps.getDescription(GpsDirectory.TAG_GPS_LONGITUDE);
//
//        tmp = tmp.replaceAll("\"", " ");
//        tmp = tmp.replaceAll("\'", "%60");
//        tmp = tmp.replaceAll(" ", "%20");
//
//        data.location = tmp;
//        data.time = gps.getDescription(GpsDirectory.TAG_GPS_TIME_STAMP);
        
        return data;
    }

    public static boolean hasGoeTag (String fileName)  {
        boolean result = false;
//        try {
//            Metadata metadata = new ExifReader(new File(fileName)).extract();
//            Directory gpsDir = metadata.getDirectory(GpsDirectory.class);
//            GpsDescriptor gps = new GpsDescriptor(gpsDir);
//            
//            if (  gpsDir.getString(GpsDirectory.TAG_GPS_VERSION_ID) == null ||
//              gpsDir.getString(GpsDirectory.TAG_GPS_LONGITUDE)  == null ||
//              gpsDir.getString(GpsDirectory.TAG_GPS_LATITUDE)   == null )
//                return false ;
//            
//            result = true;
//        }
//        catch (Exception e) {
//            result = false;
//        }

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