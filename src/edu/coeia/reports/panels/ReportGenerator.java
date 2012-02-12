/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.reports.panels;

import edu.coeia.reports.DatasourceXml;

import java.io.IOException;

/**
 *
 * @author wajdyessam
 */

public interface ReportGenerator {
    public DatasourceXml generateReport() throws IOException;
}
