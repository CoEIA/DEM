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
package edu.coeia.tasks;

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
    private final BackgroundProgressDialog dialog ;
    
     public ReportGeneratorTask(final CaseFacade caseFacade, final ReportOptionDialog dialog,
            final ReportGenerator report) {
        this.dialog = new BackgroundProgressDialog(null, true, this);
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
