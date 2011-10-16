/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.internet;

/**
 *
 * @author wajdyessam
 */

public class FireFoxHTMLReportGenerator {
    public static String getHeader () {
        String str =
        "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">" +
        "<HTML lang=\"en\">" +
        "<head>" +
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"+
        "<title>FireFox Internet Usage Report</title>" +
        "</head>"+
        "<body>" +
        "<big style=\"font-weight: bold; color: rgb(153, 153, 153);\"><big>" +
        "<span style=\"font-family: Arial;\"> FireFox Internet Usage Report</span></big></big>"+
        "<br style=\"font-family: Arial;\">";

        return str;
    }

    public static String getName (String date, String name, String user) {
        String str =
        "<span style=\"font-family: Arial;\"> " + date + " </span><br><br>"+
        "<small><span style=\"font-family: Arial;\">Produced by: " + name + " </span></small> " +
        "<small><span style=\"font-family: Arial;\">For User : " + user + " </span></small><br><br>" ;

        return str ;
    }

    public static String getTopHostTable () {
        String str =
        "<font size=\"3\" face=\"Arial\"><strong>20 Most visited hosts</strong></font><br><TABLE border=\"0\" cellpadding=\"2\" cellspacing=\"2\">" +
        "<TR style=\"background-color: rgb(150, 150, 150);\">" +
        "<TD style=\"font-family: Arial; font-weight: bold;\"><font size=\"2\">URL</TD>"+
        "<TD style=\"font-family: Arial; font-weight: bold;\"><font size=\"2\">Visit Count</TD>" +
        "</TR>";
        
        return str; 
    }

    public static String getTopHostRow (String name, int size) {
        String str =
        "<TR style=\"background-color: rgb(175, 175, 175);\">"+
        "<TD style=\"font-family: Arial;\"><font size=\"2\"> " + name + " </TD>"+
        "<TD style=\"font-family: Arial;\" align=\"center\"><font size=\"2\">"  + size + " </TD>"+
        "</TR>";

        return str; 
    }

    public static String getVisitedURLTable () {
        String str =
        "</TABLE><BR><BR>" +
        "<TABLE border=\"0\" cellpadding=\"2\" cellspacing=\"2\">" +
        "<TR>" +
          "<TD></TD>" +
          "<TD style=\"font-family: Arial; font-weight: bold;\"><font size=\"1\">URL</TD>" +
          "<TD style=\"font-family: Arial; font-weight: bold;\"><font size=\"1\">Visit Date</TD>" +
          "<TD style=\"font-family: Arial; font-weight: bold;\"><font size=\"1\">Title</TD>" +
          "<TD style=\"font-family: Arial; font-weight: bold;\"><font size=\"1\">Typed</TD>" +
        "</TR>" ;

        return str ;
    }

    public static String getVisitedBlackRow (String date, String url, String title, boolean typed) {
        String str =
        "<TR style=\"background-color: rgb(175, 175, 175);\">" +
        "<TD></TD><TD valign=\"top\" style=\"font-family: Arial;\"><font size=\"2\">" + date + "</font></TD>"+
        "<TD valign=\"top\" style=\"font-family: Courier New,Courier,monospace;\"><font size=\"2\">" + url + "</font></TD>"+
        "<TD valign=\"top\" style=\"font-family: Courier New,Courier,monospace;\"><font size=\"2\">" + title + "</font></TD>"+
        "<TD valign=\"top\" style=\"font-family: Arial;\"><font size=\"2\">" + typed + "</font></TD>"+
        "</TR>";

        return str; 
    }

    public static String getVisitedWhiteRow (String date, String url, String title, boolean typed ) {
        String str =
        "<TR>"+
        "<TD></TD><TD valign=\"top\" style=\"font-family: Arial;\"><font size=\"2\">" + date + "</font></TD>"+
        "<TD valign=\"top\" style=\"font-family: Courier New,Courier,monospace;\"><font size=\"2\">" + url + "</font></TD>"+
        "<TD valign=\"top\" style=\"font-family: Courier New,Courier,monospace;\"><font size=\"2\">" + title + "</font></TD>"+
        "<TD valign=\"top\" style=\"font-family: Arial;\"><font size=\"2\">" + typed + "</font></TD>"+
        "</TR>";

        return str; 
    }

   public static String getFooter() {
        String str =
        "</table><br><br><div style=\"text-align: center;\"><small><span style=\"font-family: Arial;\">Report created by CoEIA Forensics Toolkit</span></small></div></body></html>";

        return str ;
    }
}
