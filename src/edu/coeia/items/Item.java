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
package edu.coeia.items;

/**
 * Base class of all Item classes (File, Online Email, Offline Email and Chat) items
 * this is mapping between Lucene document fields and our object
 * 
 * all items share common id, hash, parent-id fields
 * and they differ in their Lucene fields
 * 
 * the id for document is to differentiate it with other document
 * and the parent id is for show that this document have parent with this id
 * the hash is the hash value for document content
 * if the document is file then hash is file content
 * if the document is email then the hash is email body
 * if the document is chat then the file content is the hash 
 * 
 * two Item are equal when they share the same id
 * 
 * @author wajdyessam
 */

public abstract class Item {
    
    public Item(final int documentId, final int parentId, final String documentHash,
            final String documentDescription ) {
        this.documentId = documentId ;
        this.documentParentId = parentId ;
        this.documentHash = documentHash;
        this.documentDescription = documentDescription;
    }
    
    @Override
    public boolean equals (Object otherObject) {
        if ( this == otherObject )
            return true;
        
        if ( otherObject == null)
            return false;
        
        if ( this.getClass() != otherObject.getClass() )
            return false;
        
        Item item = (Item) otherObject;
        return this.documentId == item.getDocumentId();
    }
    
    @Override
    public int hashCode() {
        return 7 * this.getDocumentId();
    }
    
    public int getDocumentId() { return this.documentId; }
    public int getDocumentParentId () { return this.documentParentId ; }
    public String getDocumentHash() { return this.documentHash ; }
    public String getDocumentDescription() { return this.documentDescription; }
    
    public abstract Object[] getDisplayData();
    
    protected final int documentId ;
    protected final int documentParentId;
    protected final String documentHash ;
    protected final String documentDescription ;
}
