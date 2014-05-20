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
package edu.coeia.cases;

/**
 *
 * @author wajdyessam
 */

public final class CaseHistory {
    private final String caseName ;
    private final String lastModified ;
    private final boolean isCaseIndexed ;
    private final long numberOfItemsIndexed ;
    private final long caseSize ;

    public static CaseHistory newInstance(String cName, String lm, boolean isIndexed, 
            long itemsCount, long size) {

        return new CaseHistory(cName, lm, isIndexed, itemsCount, size);
    }

    private CaseHistory(String cName, String lm, boolean isIndexed, long itemsCount, long size) {
        this.lastModified = lm;
        this.caseName = cName ;
        this.isCaseIndexed = isIndexed; 
        this.numberOfItemsIndexed = itemsCount ;
        this.caseSize = size; 
    }

    public String getLastModified() { return this.lastModified ;}
    public String getCaseName() { return this.caseName ; }
    public boolean getIsCaseIndexed() { return this.isCaseIndexed ; }
    public long getNumberOfItemsIndexed() { return this.numberOfItemsIndexed ; }
    public long getCaseSize() { return this.caseSize ;}
}
