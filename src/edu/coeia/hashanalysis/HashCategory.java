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
package edu.coeia.hashanalysis;

import java.util.List ;
import java.util.ArrayList ;
import java.util.Collections;

import java.io.Serializable; 

/**
 * Hash Category (sometimes we refer it as Hash Set in GUI implementation code)
 * is a collection of HashItem with name and note specify this hash category
 * 
 * hash category will be equal to other category
 * when they have the same name regardless of other informations
 * 
 * @author wajdyessam
 */
public final class HashCategory implements Serializable{

    public HashCategory (final String hashSetName, final String note) {
        this.name = hashSetName ;
        this.note = note;
        this.items = new ArrayList<HashItem>();
    }
    
    /**
     * Adding Items if its new to the current collections
     * @param item
     * @return false if the item is not new
     */
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
    
    // getter methods
    public String getName() { return this.name ;}
    public String getNote() { return this.note ;}
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
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName()).append("[\n");
        
        for(HashItem item: this.items ) {
            builder.append(item).append("\n");
        }
        
        builder.append("]\n");
        return builder.toString();
    }
    
    private final String name; 
    private final String note;
    private final List<HashItem> items;
}
