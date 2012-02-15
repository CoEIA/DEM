/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.task;

import edu.coeia.cases.CaseFacade;
import edu.coeia.constants.ApplicationConstants;
import edu.coeia.reports.DatasourceXml;
import edu.coeia.reports.DisclosureReport;
import edu.coeia.reports.ReportOptionDialog;
import edu.coeia.reports.panels.ReportGenerator;

import java.awt.Desktop;
import java.awt.EventQueue;

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wajdyessam
 */
public class ReportGeneratorTask implements Task{
    private final CaseFacade caseFacade ;
    private final ReportOptionDialog reportDialog;
    private final ReportGenerator report;
    private final ProgressDialog dialog ;
    
     public ReportGeneratorTask(final CaseFacade caseFacade, final ReportOptionDialog dialog,
            final ReportGenerator report) {
        this.dialog = new ProgressDialog(null, true, this);
        this.caseFacade = caseFacade ;
        this.reportDialog = dialog;
        this.report = report;
    }
    
    @Override
    public void startTask() {
        this.dialog.startThread();
    }
    
    @Override
    public void doTask() throws Exception {
        this.generateReport(this.report.generateReport());
    }
    
    @Override
    public boolean isCancelledTask() {
        return this.dialog.isCancelledThread();
    }
    
    private void generateReport(final DatasourceXml objXmlSource) throws Exception {
        File file = new File(ApplicationConstants.TEMPLATES_FOLDER 
                + File.separator + objXmlSource.m_strJasperFile);

        String strJasperFile = file.getAbsolutePath();
        String strReportOutputPath = this.caseFacade.getCaseReportFolderLocation();

        DisclosureReport disReport = new DisclosureReport(
                strJasperFile,
                objXmlSource.m_strXmlPath,
                strReportOutputPath,objXmlSource.m_strReportName);

        disReport.setOutputFileExtension(DisclosureReport.REPORT_TYPE.PDF);
        disReport.setRootXPath(objXmlSource.m_strXPath);
        disReport.Generate();

        final File pdf = new File(disReport.getFinalFile());
        
        if (pdf.exists()){
            EventQueue.invokeLater(new Runnable() { 
                @Override
                public void run() {
                    try {
                        Desktop.getDesktop().open(pdf);
                    } catch (IOException ex) {
                        Logger.getLogger(ReportGeneratorTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            System.out.println(disReport.getFinalFile());
        } 
        else
            System.out.println("File is not exists");
    }
}
