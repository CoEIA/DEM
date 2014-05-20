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
package edu.coeia.viewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author wajdyessam
 */
public class SearchResultParamter {
    
    public SearchResultParamter(final String keyword,final int documentId, final List<Integer> ids) {
        this.keyword = keyword;
        this.currentDocumentId = documentId;
        this.documentsIdNumbers = new ArrayList<Integer>();
        this.documentsIdNumbers.addAll(Collections.unmodifiableList(ids));
    }
    
    public String getKeyword() { return this.keyword ; }
    public int getCurrentDocumentId() { return this.currentDocumentId; }
    public List<Integer> getDocumentIds() { return Collections.unmodifiableList(this.documentsIdNumbers); }
    
    private final String keyword;
    private final int currentDocumentId; 
    private final List<Integer> documentsIdNumbers;
}
