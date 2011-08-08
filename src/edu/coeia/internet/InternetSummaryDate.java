/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.internet;

/**
 *
 * @author wajdyessam
 */

public class InternetSummaryDate {
    private String url ;
    private String date ;
    private int numberOfVisit ;

    public InternetSummaryDate (String url, String date, int numberOfVisit) {
        this.url = url ;
        this.date = date ;
        this.numberOfVisit = numberOfVisit ;
    }

    public int getNumberOfVisit() { return this.numberOfVisit; }
    public String getDate () { return this.date ; }
    public String getURL () { return this.url ; }

    @Override
    public String toString () {
        return " " + this.getURL() + " " + this.getDate() ;
    }
}
