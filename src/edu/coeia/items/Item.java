/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.items;

/**
 * Base class of all Item classes (File, Online Email, Offline Email and Chat) items
 * this is mapping between lucene document fields and our object
 * 
 * all items share common id, hash, parent-id fields
 * and they differ in their lucene fields
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
