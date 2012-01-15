/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.reports;

import edu.coeia.cases.Case;
import edu.coeia.cases.CaseHistoryHandler;
import edu.coeia.util.DateUtil;
import edu.coeia.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Farhan
 */
public class RawResultFile {
    
    public static DatasourceXml getExtensionFrequencyXmlFile(Map<String, Double> map, Case currentCase) {
        // iterator over map
        String mainRawfilePath = currentCase.getCaseLocation()+"\\RAW";
        String cookedReportFolder = currentCase.getCaseLocation()+"\\Reports";
        DatasourceXml sourceXml = new DatasourceXml();
        if(!FileUtil.isDirectoryExists(mainRawfilePath))
        {
            FileUtil.createFolder(mainRawfilePath);
        }
        
        if(!FileUtil.isDirectoryExists(cookedReportFolder))
        {
            FileUtil.createFolder(cookedReportFolder);
        }
        
        sourceXml.m_strJasperFile = "\\fileextension_report.jasper";
        sourceXml.m_strXPath = "/dem/detail/effectiveextensions/ext";
        sourceXml.m_strReportName =  "fileextension";
        
        String strOutputPath =mainRawfilePath+"\\fileextension.xml";
        String retOutput="";
        String strLocation = currentCase.getCaseLocation();
        String strCaseXml ="<dem><case>"
                        +"<name>"+currentCase.getCaseName()+"</name>"
                        +"<author>"+currentCase.getDescription()+"</author>"
                        +"<source> "+strLocation+"</source>"
                        +"</case>";
        
        String filesdata="";
        int icounter = 0 ;
        for(Map.Entry<String, Double> entry: map.entrySet()) 
        {
            String extension = entry.getKey();
            double frequecy = entry.getValue();
            
            // operations here...
            filesdata+=
                         "<ext>"
                        + "<sno>"+icounter+"</sno>"
                        + "<extension>"+extension+"</extension>"
                        + "<frequency>"+frequecy+"</frequency>"
                        + "</ext>";
            icounter++;
	}
        
       retOutput  =strCaseXml
                +"<detail>"
                      +"<effectiveextensions>"
                      +filesdata
                      +"</effectiveextensions>" 
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
       
        sourceXml.m_strXmlPath =  strOutputPath;

        return sourceXml;
    }
    
    public static DatasourceXml getCasesXmlFile(List<Case> cases, Case currentCase) {
        
        String mainRawfilePath = currentCase.getCaseLocation()+"\\RAW";
        String cookedReportFolder = currentCase.getCaseLocation()+"\\Reports";
        DatasourceXml sourceXml = new DatasourceXml();
        sourceXml.m_strJasperFile = "\\cases_report.jasper";
        sourceXml.m_strXPath = "/dem/cases/case";
        sourceXml.m_strReportName =  "cases";
        
        if(!FileUtil.isDirectoryExists(mainRawfilePath))
        {
            FileUtil.createFolder(mainRawfilePath);
        }
        
        if(!FileUtil.isDirectoryExists(cookedReportFolder))
        {
            FileUtil.createFolder(cookedReportFolder);
        }
        
        String strOutputPath =mainRawfilePath+"\\cases.xml";
        String retOutput="";
        String strLocation = currentCase.getCaseLocation();
        String strCaseXml ="<dem><cases>";
        String casess ="";
        
        for(Case aCase: cases) {
            String caseName = aCase.getCaseName();
            String caseLocation = aCase.getCaseLocation();
            caseLocation = caseLocation.replace(':','\\');
            String caseCreatingTime = DateUtil.formatedDateWithTime(aCase.getCreateTime());
            String caseAuther = aCase.getInvestigatorName();
            String caseDescription  = aCase.getDescription();
            long caseSize = CaseHistoryHandler.get(caseName).getCaseSize();
            casess += "<case>"
                      +"<path>"+caseLocation+"</path>"
                       +"<creator>"+caseAuther+"</creator>"
                      +"<name>"+ caseName+"</name>"
                      +"<description>"+ caseDescription+"</description>"
                      +"<size>"+ caseSize+"</size>"                  
                      +"<date>"+caseCreatingTime+"</date>"
                    + "</case>";
            // operations here
        }
        
       strCaseXml+= casess +"</cases></dem>";
                
       try
       {
           File file = new File(strOutputPath);      
           FileUtils.writeStringToFile(file, strCaseXml);
       }
       catch (IOException e)
       {
          e.printStackTrace();
          strOutputPath="";
       }

        sourceXml.m_strXmlPath =  strOutputPath;
       
      return sourceXml; 
    }
    
    public static DatasourceXml getFileSystemXmlFile(List<String> list,Case cases)
    {
        String mainRawfilePath = cases.getCaseLocation()+"\\RAW";
        String cookedReportFolder = cases.getCaseLocation()+"\\Reports";
        DatasourceXml sourceXml = new DatasourceXml();
        
        if(!FileUtil.isDirectoryExists(mainRawfilePath))
        {
            FileUtil.createFolder(mainRawfilePath);
        }
        
        if(!FileUtil.isDirectoryExists(cookedReportFolder))
        {
            FileUtil.createFolder(cookedReportFolder);
        }
        
        sourceXml.m_strJasperFile = "\\filesystem_report.jasper";
        sourceXml.m_strXPath = "/dem/detail/effectivefiles/file";
        sourceXml.m_strReportName =  "filesystem";
                
        String strOutputPath =mainRawfilePath+"\\filesystem.xml";
        String retOutput="";
        
        String strLocation = cases.getCaseLocation();
        strLocation = strLocation.replace(':','\\');

        String strCaseXml ="<dem><case>"
                        +"<name>"+cases.getCaseName()+"</name>"
                        +"<author>"+cases.getDescription()+"</author>"
                        +"<source> "+strLocation+"</source>"
                        +"</case>";

        String files ="";
        
       Iterator<String> iterator = list.iterator();
       int icounter = 0;
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
                        + "<sno>"+icounter+"</sno>"
                        + "<path>"+strPath+"</path>"
                        + "<size>"+filesizeInKB+"kb</size>"
                        + "<moddate>"+date+"</moddate>"
                        + "<extension>"+ext+"</extension>"
                        + "</file>";
            icounter++;
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

        sourceXml.m_strXmlPath =  strOutputPath;
       
        return sourceXml;
    }
}
