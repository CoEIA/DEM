/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.reports;

/**
 *
 * @author Farhan
 */
import java.io.File;

import java.util.HashMap;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

public class DisclosureReport {

    private String m_strJasperCompiledFile,
            m_strDataSourceFile, m_strOutputFilePath, m_strOutputFileName, m_strRootXPath;
    private String m_strFinalFile;

    public String getFinalFile() {
        return m_strFinalFile;
    }
    public static String REPORTFOLDER = "\\Reports\\";

    public enum REPORT_TYPE {

        PDF, WORD, HTML, RTF, CSV, XLS, XML
    };

    public enum DATASOURCE_TYPE {

        XML, MYSQL, CSV
    };
    private REPORT_TYPE m_ReportType = REPORT_TYPE.PDF;
    ;
   private DATASOURCE_TYPE m_DataSource = DATASOURCE_TYPE.XML;

    public void setRootXPath(String m_strRootXPath) {
        this.m_strRootXPath = m_strRootXPath;
    }

    public void setDataSource(DATASOURCE_TYPE m_DataSource) {
        this.m_DataSource = m_DataSource;
    }

    public void setDataSourceFile(String v_strDataXmlFile) {
        this.m_strDataSourceFile = v_strDataXmlFile;
    }

    public void setJasperCompiledFile(String v_strJasperCompiledFile) {
        this.m_strJasperCompiledFile = v_strJasperCompiledFile;
    }

    public void setOutputFileExtension(REPORT_TYPE v_OutputFileExtension) {
        m_ReportType = v_OutputFileExtension;
    }

    public void setOutputFileName(String v_strOutputFileName) {
        this.m_strOutputFileName = v_strOutputFileName;
    }

    public void setOutputFilePath(String v_strOutputFilePath) {
        this.m_strOutputFilePath = v_strOutputFilePath;
    }

    public String getDataSourceFile() {
        return m_strDataSourceFile;
    }

    public String getJasperCompiledFile() {
        return m_strJasperCompiledFile;
    }

    public REPORT_TYPE getOutputFileExtension() {
        return m_ReportType;
    }

    public String getOutputFileName() {
        return m_strOutputFileName;
    }

    public String getOutputFilePath() {
        return m_strOutputFilePath;
    }

    public DisclosureReport(String v_jasperCompiledFile, String v_XmlDataFile,
            String v_OutputFilePath, String v_OutputFileName) {
        this.m_strDataSourceFile = v_XmlDataFile;
        m_strJasperCompiledFile = v_jasperCompiledFile;
        m_strOutputFilePath = v_OutputFilePath;
        m_strOutputFileName = v_OutputFileName;
    }

    public void Generate() throws Exception {
        if (m_strOutputFileName.isEmpty()
                || m_strJasperCompiledFile.isEmpty()
                || m_strOutputFilePath.isEmpty()
                || m_strDataSourceFile.isEmpty()) {
            throw new Exception("One of the parameter for file output is empty.");
        }

        switch (m_DataSource) {
            case XML:
                GenerateByXML();
                break;

            case CSV:
                break;

            case MYSQL:
                break;

            default:
                GenerateByXML();
        }

    }

    private void GenerateByXML() throws Exception {
        if (this.m_strRootXPath.isEmpty()) {
            throw new Exception("Root XPath is required.");
        }

        try {
            HashMap hm = new HashMap();
            File file = new File(m_strDataSourceFile);

            if (!file.canRead()) {
                throw new Exception("Unable to read XML data source file.");
            }

            JRXmlDataSource jrxmlds = new JRXmlDataSource(file, m_strRootXPath);
            exportOutputFile(JasperFillManager.fillReport(m_strJasperCompiledFile, hm, jrxmlds));
        } catch (JRException ex) {
            System.out.println("CAUSE: " + ex.getCause());
            System.out.println("MESSAGE" + ex.getMessage());
            System.out.println("LOCAL MESSAGE" + ex.getLocalizedMessage());
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }

    }

    private void exportOutputFile(JasperPrint v_jsPrint) throws Exception {
        String ext;

        JRExporter exporter = null;

        try {
            switch (m_ReportType) {
                case PDF:
                    ext = ".pdf";
                    exporter = new JRPdfExporter();
                    break;
                case CSV:
                    ext = ".csv";
                    exporter = new JRCsvExporter();
                    break;
                case XML:
                    ext = ".xml";
                    exporter = new JRXmlExporter();
                    break;
                case HTML:
                    ext = ".html";
                    exporter = new JRHtmlExporter();
                    break;
                case WORD:
                    ext = ".docx";
                    exporter = new JRDocxExporter();
                    break;
                case RTF:
                    ext = ".rtf";
                    exporter = new JRRtfExporter();
                    break;
                default:
                    ext = ".pdf";
                    exporter = new JRPdfExporter();
            }

            String strFinalOutput = m_strOutputFilePath + "\\" + m_strOutputFileName + ext;
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, strFinalOutput);
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, v_jsPrint);
            exporter.exportReport();
            System.out.println("Created file: " + strFinalOutput);
            m_strFinalFile = strFinalOutput;
        } catch (JRException ex) {
            System.out.println("CAUSE: " + ex.getCause());
            System.out.println("MESSAGE" + ex.getMessage());
            System.out.println("LOCAL MESSAGE" + ex.getLocalizedMessage());
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }
}
