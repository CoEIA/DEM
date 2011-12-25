/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.items;

/**
 * Base class of all Item classes (File, Email, Chat) items
 * this is mapping between lucene fields and our object
 * 
 * all items share common id, hash, parent-id fields
 * and they differ in their lucene fields
 * 
 * @author wajdyessam
 */

public abstract class Item {
    
    public Item(final int documentId, final int parentId, final String documentHash) {
        this.documentId = documentId ;
        this.documentParentId = parentId ;
        this.documentHash = documentHash;
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
    
    protected int documentId ;
    protected int documentParentId;
    protected String documentHash ;
}
