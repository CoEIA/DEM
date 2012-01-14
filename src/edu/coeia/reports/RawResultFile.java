/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.reports;

import edu.coeia.cases.Case;
import edu.coeia.util.DateUtil;
import edu.coeia.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Farhan
 */
public class RawResultFile {
    
    public static String getExtensionFrequencyXmlFile(Map<String, Double> map, Case currentCase) {
        // iterator over map
        for(Map.Entry<String, Double> entry: map.entrySet()) {
            String extension = entry.getKey();
            double frequecy = entry.getValue();
            
            // operations here...
        }
        
        return "";
    }
    
    public static String getCasesXmlFile(List<Case> cases, Case currentCase) {
        for(Case aCase: cases) {
            String caseName = aCase.getCaseName();
            String caseLocation = aCase.getCaseLocation();
            String caseCreatingTime = DateUtil.formatedDateWithTime(aCase.getCreateTime());
            String caseAuther = aCase.getInvestigatorName();
            
            // operations here
        }
        
        return "";
    }
    
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
        
        String strCaseXml ="<dem><case>"
                        +"<name>"+cases.getCaseName()+"</name>"
                        +"<date>1-1-2012</date>"
                        +"<size>321</size>"
                        +"<author>"+cases.getDescription()+"</author>"
                        +"<source> "+cases.getCaseLocation()+"</source>"
                        +"</case>";

        String files ="";
        
       Iterator<String> iterator = list.iterator();
	while (iterator.hasNext()) {
            String str = iterator.next();
            str.replace(':','/');
		files+=
                         "<file>"+str+"</file>";
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
