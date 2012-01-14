/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.reports;

import edu.coeia.cases.Case;
import edu.coeia.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Farhan
 */
public class RawResultFile {
    
    public static String getFileSystemXmlFile(List<String> list,Case cases)
    {
        String mainRawfilePath = cases.getCaseLocation()+"\\RAW";
        String cookedReportFolder = cases.getCaseLocation()+"\\Reports";
        
        if(!FileUtil.isDirectoryExists(mainRawfilePath))
        {
            FileUtil.createFolder(mainRawfilePath);
        }
        
        if(!FileUtil.isDirectoryExists(cookedReportFolder))
        {
            FileUtil.createFolder(cookedReportFolder);
        }
        
        String strOutputPath =mainRawfilePath+"\\filesystem.xml";
        String retOutput="";
        
        String strLocation = cases.getCaseLocation();
        strLocation.replace('C','/');
        strLocation.replace(':','/');
        System.out.println(strLocation);
        String strCaseXml ="<dem><case>"
                        +"<name>"+cases.getCaseName()+"</name>"
                        +"<author>"+cases.getDescription()+"</author>"
                        +"<source> "+strLocation+"</source>"
                        +"</case>";

        String files ="";
        
       Iterator<String> iterator = list.iterator();
	while (iterator.hasNext()) {
            String strPath = iterator.next();
            File file = new File(strPath);
            strPath.replace(':','/');
                        
            long filesize = file.length();
            long filesizeInKB = filesize / 1024;

            Date date = new Date(file.lastModified());
            int mid= strPath.lastIndexOf(".");
            String ext=strPath.substring(mid+1,strPath.length());  
		files+=
                         "<file>"
                        + "<path>"+strPath+"</path>"
                        + "<size>"+filesizeInKB+"kb</size>"
                        + "<moddate>"+date+"</moddate>"
                        + "<extension>"+ext+"</extension>"
                        + "</file>";
	}
        
       retOutput  =strCaseXml
                +"<detail>"
                      +"<effectivefiles>"
                      +files
                      +"</effectivefiles>" 
                     +"</detail>"
                + "</dem>";
       
       try
       {

           File file = new File(strOutputPath);      
           FileUtils.writeStringToFile(file, retOutput);
       }
       catch (IOException e)
       {
          e.printStackTrace();
          strOutputPath="";
       }

        return strOutputPath;
    }
}
