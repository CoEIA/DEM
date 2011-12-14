/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.hash;

import java.util.List ;
import java.util.ArrayList ;
import java.util.Collections;

/**
 *
 * @author wajdyessam
 */
public class HashCategory {

    public HashCategory (final String hashSetName) {
        this.name = hashSetName ;
        this.items = new ArrayList<HashItem>();
    }
    
    public boolean addItem(final HashItem item) {
        if (! this.isContain(item) )  {
            this.items.add(item);
            return true;
        }
        
        return false;
    }
    
    public boolean isContain (final HashItem item) {
        return this.items.contains(item);
    }
    
    public String getName() { return this.name ;}
    public List<HashItem> getItems() { return Collections.unmodifiableList(this.items); }
    
    
    @Override
    public boolean equals(Object object) {
        if ( this == object )
            return true;
        
        if ( object == null )
            return false;
        
        if ( this.getClass() != object.getClass() )
            return false;
        
        HashCategory hashCategory = (HashCategory) object;
        return hashCategory.getName().equals(this.getName());
    }
    
    @Override
    public int hashCode() {
        return this.getName().hashCode() * 7 ;
    }
    
    private final String name; 
    private final List<HashItem> items;
}
