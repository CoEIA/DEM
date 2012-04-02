/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.reports;

import edu.coeia.cases.Case;
import edu.coeia.cases.CaseFacade;
import edu.coeia.constants.ApplicationConstants;
import edu.coeia.tags.Tag;
import edu.coeia.util.DateUtil;
import edu.coeia.util.FileUtil;
import edu.coeia.util.SizeUtil;

import java.io.File;
import java.io.IOException;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Farhan
 */
public class RawResultFile {

    public static DatasourceXml getExtensionFrequencyXmlFile(final Map<String, Double> map, 
            final CaseFacade caseFacade) throws IOException {
        
        DatasourceXml sourceXml = new DatasourceXml();
        sourceXml.m_strJasperFile = ApplicationConstants.FILE_EXTENSION_JASPER_FILE;
        sourceXml.m_strXPath = ApplicationConstants.FILE_EXTENSION_X_PATH;
        sourceXml.m_strReportName = ApplicationConstants.FILE_EXTENSION_REPORT_NAME;
        sourceXml.m_strXmlPath =  caseFacade.getCaseRawReportFolderLocation() + File.separator + ApplicationConstants.FILE_EXTENSION_XML_FILE;

        StringBuilder filesdata = new StringBuilder();
        int icounter = 1;
        
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            String extension = entry.getKey();
            double frequecy = entry.getValue();

            // operations here...
            filesdata.append("<ext>" + "<sno>")
                .append(icounter)
                .append("</sno>" + "<extension>")
                .append(extension)
                .append("</extension>" + "<frequency>")
                .append(frequecy)
                .append("</frequency>"+ "</ext>");
            
            icounter++;
        }

        String retOutput = getFullXMLTags(getGenericCaseInformation(caseFacade.getCase()), filesdata.toString());
        File file = new File(sourceXml.m_strXmlPath);
        FileUtils.writeStringToFile(file, retOutput);
        
        return sourceXml;
    }
    
    public static DatasourceXml getFileSystemXmlFile(final List<String> dataList,
            final CaseFacade caseFacade) throws IOException {

        DatasourceXml sourceXml = new DatasourceXml();
        sourceXml.m_strJasperFile = ApplicationConstants.FILE_SYSTEM_JASPER_FILE;
        sourceXml.m_strXPath = ApplicationConstants.FILE_SYSTEM_X_PATH;
        sourceXml.m_strReportName = ApplicationConstants.FILE_SYSTEM_REPORT_NAME;
        sourceXml.m_strXmlPath = caseFacade.getCaseRawReportFolderLocation() + File.separator + ApplicationConstants.FILE_SYSTEM_XML_FILE;
                
        StringBuilder files = new StringBuilder();
        int icounter = 1;
        
        for(String strPath: dataList) {
            File file = new File(strPath);
            strPath = StringEscapeUtils.escapeXml(strPath);

            long filesizeInKB = (long) SizeUtil.toKB((double)file.length());
            Date date = new Date(file.lastModified());
            String ext = FileUtil.getExtension(file);
             
            files.append("<file>")
                .append("<sno>")
                .append(icounter)
                .append("</sno>")
                .append("<path>")
                .append(strPath)
                .append("</path>")
                .append("<size>")
                .append(filesizeInKB)
                .append("kb</size>")
                .append("<moddate>")
                .append(date)
                .append("</moddate>")
                .append("<extension>")
                .append(ext)
                .append("</extension>")
                .append("</file>");
            
            icounter++;
        }

        String retOutput = getFullXMLTags(getGenericCaseInformation(caseFacade.getCase()), files.toString());
        File file = new File(sourceXml.m_strXmlPath);
        FileUtils.writeStringToFile(file, retOutput);

        return sourceXml;
    }
        
    public static DatasourceXml getCasesXmlFile(final List<Case> cases, final CaseFacade caseFacade) throws Exception {
        DatasourceXml sourceXml = new DatasourceXml();
        
        sourceXml.m_strJasperFile = ApplicationConstants.CASES_JASPER_FILE;
        sourceXml.m_strXPath = ApplicationConstants.CASES_X_PATH;
        sourceXml.m_strReportName = ApplicationConstants.CASES_REPORT_NAME;
        sourceXml.m_strXmlPath = caseFacade.getCaseRawReportFolderLocation() + File.separator + ApplicationConstants.CASES_XML_FILE;
        
        StringBuilder casesBuffer = new StringBuilder();
        casesBuffer.append("<dem><cases>");
        
        for (Case aCase : cases) {
            String caseLocation = aCase.getCaseLocation(); //.replace(':', '\\');
            caseLocation = StringEscapeUtils.escapeXml(caseLocation);
            String caseCreatingTime = DateUtil.formatedDateWithTime(aCase.getCreateTime()); 
            long caseSize = caseFacade.getCaseHistory().getCaseSize();
            
            casesBuffer.append("<case>" + "<path>")
                    .append(StringEscapeUtils.escapeXml(caseLocation))
                    .append("</path>" + "<creator>")
                    .append(StringEscapeUtils.escapeXml(aCase.getInvestigatorName()))
                    .append("</creator>" + "<name>")
                    .append( StringEscapeUtils.escapeXml(aCase.getCaseName()))
                    .append("</name>" + "<description>")
                    .append(StringEscapeUtils.escapeXml(aCase.getDescription()))
                    .append("</description>" + "<size>")
                    .append(caseSize)
                    .append("</size>" + "<date>")
                    .append(caseCreatingTime)
                    .append("</date>" + "</case>");
        }
        
        casesBuffer.append("</cases></dem>");
        
        File file = new File(sourceXml.m_strXmlPath);
        FileUtils.writeStringToFile(file, casesBuffer.toString());

        return sourceXml;
    }

    public static DatasourceXml getTaggedItems(final CaseFacade caseFacade) throws IOException {
        DatasourceXml sourceXml = new DatasourceXml();
        
        sourceXml.m_strJasperFile = ApplicationConstants.TAGS_JASPER_FILE;
        sourceXml.m_strXPath = ApplicationConstants.TAGS_X_PATH;
        sourceXml.m_strReportName = ApplicationConstants.TAGS_REPORT_NAME;
        sourceXml.m_strXmlPath = caseFacade.getCaseRawReportFolderLocation() + File.separator + ApplicationConstants.TAGS_XML_FILE;
        
        List<Tag> taggeditems = caseFacade.getTags();

        StringBuilder result = new StringBuilder();
        
        result.append(getGenericCaseInformation(caseFacade.getCase()));
        result.append("<filetags>");
        
        for (Tag tag : taggeditems) {
            result.append("<tag><name>")
                    .append(StringEscapeUtils.escapeXml(tag.getName()))
                    .append("</name>" + "<moddate>")
                    .append(StringEscapeUtils.escapeXml(tag.getDate().toString()))
                    .append("</moddate>" + "<message>")
                    .append(StringEscapeUtils.escapeXml(tag.getMessage()))
                    .append("</message>" + "</tag>");
        }

        result.append("</filetags></dem>");
        
        File file = new File(sourceXml.m_strXmlPath);
        FileUtils.writeStringToFile(file, result.toString());

        return sourceXml;
    }

//    public static DatasourceXml getSignatureItems(final JTable Table, final CaseFacade caseFacade) throws IOException {
//        DatasourceXml sourceXml = new DatasourceXml();
//        
//        sourceXml.m_strJasperFile = ApplicationConstants.TAGS_JASPER_FILE;
//        sourceXml.m_strXPath = ApplicationConstants.TAGS_X_PATH;
//        sourceXml.m_strReportName = ApplicationConstants.TAGS_REPORT_NAME;
//        sourceXml.m_strXmlPath = caseFacade.getCaseRawReportFolderLocation() + File.separator + ApplicationConstants.TAGS_XML_FILE;
//        
//
//        Object[][] data = JTableUtil.getTableData(Table);
//
//
//        int r = Table.getRowCount();
//        int c = Table.getColumnCount();
//
//        for (int i = 0; i < r; i++) {
//            for (int x = 0; x < c; x++) {
//                if (data[i][x] == null) {
//                    data[i][x] = new String();
//                }
//            }
//        }
//        String strCaseXml = "<filesign>";
//        for (int i = 0; i < r; i++) {
//            strCaseXml += "<sign><filename>" + data[i][0] + "</filename>"
//                    + "<status>" + data[i][2] + "</status>"
//                    + "<suspected>" + data[i][3] + "</suspected>"
//                    + "<fileextensions>" + data[i][4] + "</fileextensions>"
//                    + "<signatures>" + data[i][1] + "</signatures>"
//                    + "</sign>";
//        }
//        strCaseXml += "</filesign>";
//
//
//        File file = new File(sourceXml.m_strXmlPath);
//        FileUtils.writeStringToFile(file, strCaseXml);
//
//        return sourceXml;
//    }
//
//    public static DatasourceXml getDatabaseSignatures(JTable Table, Case currentCase) throws IOException {
//        String mainRawfilePath = currentCase.getCaseLocation() + File.separator + ApplicationConstants.CASE_ROW_REPORT_FOLDER;
//        
//        DatasourceXml sourceXml = new DatasourceXml();
//
//        sourceXml.m_strJasperFile = "\\databasesignature_report.jasper";
//        sourceXml.m_strXPath = "/filedb/db";
//        sourceXml.m_strReportName = "databasesignatures";
//
//        String strOutputPath = mainRawfilePath + "\\databasesignatures.xml";
//
//        Object[][] data = JTableUtil.getTableData(Table);
//
//
//        int r = Table.getRowCount();
//        int c = Table.getColumnCount();
//
//        for (int i = 0; i < r; i++) {
//            for (int x = 0; x < c; x++) {
//                if (data[i][x] == null) {
//                    data[i][x] = new String();
//                }
//            }
//        }
//
//
//        String strCaseXml = "<filedb>";
//        for (int i = 0; i < r; i++) {
//            strCaseXml += "<db><fileext>" + data[i][0] + "</fileext>"
//                    + "<filesign>" + data[i][1] + "</filesign>"
//                    + "<filetype>" + data[i][2] + "</filetype>"
//                    + "<filecatg>" + data[i][3] + "</filecatg>"
//                    + "</db>";
//        }
//        strCaseXml += "</filedb>";
//
//
//        File file = new File(strOutputPath);
//        FileUtils.writeStringToFile(file, strCaseXml);
//
//        sourceXml.m_strXmlPath = strOutputPath;
//
//        return sourceXml;
//    }
//
//    public static DatasourceXml getHashAnalysisHashLibrary(JTable Table, Case currentCase) throws IOException {
//        String mainRawfilePath = currentCase.getCaseLocation() + File.separator + ApplicationConstants.CASE_ROW_REPORT_FOLDER;
//        
//        DatasourceXml sourceXml = new DatasourceXml();
//
//        sourceXml.m_strJasperFile = "\\hashanalysis_report.jasper";
//        sourceXml.m_strXPath = "/hashfile/file";
//        sourceXml.m_strReportName = "hashanalysis_report";
//
//        String strOutputPath = mainRawfilePath + "\\hashanalysis_report.xml";
//
//        Object[][] data = JTableUtil.getTableData(Table);
//
//
//        int r = Table.getRowCount();
//        int c = Table.getColumnCount();
//
//        for (int i = 0; i < r; i++) {
//            for (int x = 0; x < c; x++) {
//                if (data[i][x] == null) {
//                    data[i][x] = new String();
//                }
//            }
//        }
//
//
//        String strCaseXml = "<hashfile>";
//        for (int i = 0; i < r; i++) {
//            strCaseXml += "<file><filename>" + data[i][0] + "</filename>"
//                    + "<filepath>" + data[i][1] + "</filepath>"
//                    + "<hashset>" + data[i][2] + "</hashset>"
//                    + "<hashvalue>" + data[i][3] + "</hashvalue>"
//                    + "</file>";
//        }
//        strCaseXml += "</hashfile>";
//
//
//        File file = new File(strOutputPath);
//        FileUtils.writeStringToFile(file, strCaseXml);
//        sourceXml.m_strXmlPath = strOutputPath;
//
//        return sourceXml;
//    }
//
//    public static DatasourceXml getHashAnalysisinCase(JTable Table, Case currentCase) throws IOException {
//        String mainRawfilePath = currentCase.getCaseLocation() + File.separator + ApplicationConstants.CASE_ROW_REPORT_FOLDER;
//        
//        DatasourceXml sourceXml = new DatasourceXml();
//
//        sourceXml.m_strJasperFile = "\\hashanalysis_incase_report.jasper";
//        sourceXml.m_strXPath = "/hashfile/file";
//        sourceXml.m_strReportName = "hashanalysis_incase_report";
//
//        String strOutputPath = mainRawfilePath + "\\hashanalysis_incase_report.xml";
//
//        Object[][] data = JTableUtil.getTableData(Table);
//
//
//        int r = Table.getRowCount();
//        int c = Table.getColumnCount();
//
//        for (int i = 0; i < r; i++) {
//            for (int x = 0; x < c; x++) {
//                if (data[i][x] == null) {
//                    data[i][x] = new String();
//                }
//            }
//        }
//
//
//        String strCaseXml = "<hashfile>";
//        for (int i = 0; i < r; i++) {
//            strCaseXml += "<file><filename>" + data[i][0] + "</filename>"
//                    + "<filepath>" + data[i][1] + "</filepath>"
//                    + "<date>" + data[i][2] + "</date>"
//                    + "<hashvalue>" + data[i][3] + "</hashvalue>"
//                    + "</file>";
//        }
//        strCaseXml += "</hashfile>";
//
//
//        File file = new File(strOutputPath);
//        FileUtils.writeStringToFile(file, strCaseXml);
//
//        sourceXml.m_strXmlPath = strOutputPath;
//
//        return sourceXml;
//    }
//
//    public static DatasourceXml getTextClouds(JTable Table, Case currentCase) throws IOException {
//        String mainRawfilePath = currentCase.getCaseLocation() + File.separator + ApplicationConstants.CASE_ROW_REPORT_FOLDER;
//        
//        DatasourceXml sourceXml = new DatasourceXml();
//
//        sourceXml.m_strJasperFile = "\\commonfrequency.jasper";
//        sourceXml.m_strXPath = "/wordclouds/cloud";
//        sourceXml.m_strReportName = "commonfrequency";
//
//        String strOutputPath = mainRawfilePath + "\\commonfrequency_report.xml";
//
//        Object[][] data = JTableUtil.getTableData(Table);
//
//
//        int r = Table.getRowCount();
//        int c = Table.getColumnCount();
//
//        for (int i = 0; i < r; i++) {
//            for (int x = 0; x < c; x++) {
//                if (data[i][x] == null) {
//                    data[i][x] = new String();
//                }
//            }
//        }
//
//
//        String strCaseXml = "<wordclouds>";
//        for (int i = 0; i < r; i++) {
//            strCaseXml += "<cloud><word>" + data[i][0] + "</word>"
//                    + "<frequency>" + data[i][1] + "</frequency>"
//                    + "</cloud>";
//
//        }
//        strCaseXml += "</wordclouds>";
//
//
//        File file = new File(strOutputPath);
//        FileUtils.writeStringToFile(file, strCaseXml);
//
//        sourceXml.m_strXmlPath = strOutputPath;
//
//        return sourceXml;
//    }
    
    private static String getGenericCaseInformation(final Case currentCase) {
        String result = "<dem><case>"
                + "<name>" + StringEscapeUtils.escapeXml(currentCase.getCaseName()) + "</name>"
                + "<author>" + StringEscapeUtils.escapeXml(currentCase.getInvestigatorName())  + "</author>"
                + "<source> " +  StringEscapeUtils.escapeXml(currentCase.getCaseLocation())  + "</source>"
                + "</case>";
        
        return result;
    }
    
    private static String getFullXMLTags(final String header, final String data) {
        StringBuilder result = new StringBuilder();
        
        result.append(header)
            .append("<detail>")
            .append("<effectivefiles>")
            .append(data)
            .append("</effectivefiles>")
            .append("</detail>")
            .append("</dem>");
            
        return result.toString();
    }
}
