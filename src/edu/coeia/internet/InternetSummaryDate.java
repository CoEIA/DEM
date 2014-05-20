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
