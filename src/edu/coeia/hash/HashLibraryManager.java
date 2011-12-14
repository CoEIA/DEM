/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.hash;

import java.util.List ;
import java.util.ArrayList ;

/**
 *
 * @author wajdyessam
 */
public class HashLibraryManager {
    
    public HashLibraryManager(){ 
        this.hashCategories = new ArrayList<HashCategory>();
    }
    
    public boolean add(final HashCategory hashCategory) {
        if ( ! this.isContain(hashCategory) ) {
            this.hashCategories.add(hashCategory);
            return true;
        }
        
        return false;
    }
    
    public boolean isContain(final HashCategory hashCategory) {
        return this.hashCategories.contains(hashCategory);
    }
    
    public void update(final HashCategory hashCategory) {
        
    }
    
    private List<HashCategory> hashCategories;
}
