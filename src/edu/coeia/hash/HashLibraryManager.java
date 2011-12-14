/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.hash;

import edu.coeia.util.FileUtil;
import java.io.File;
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
    
    public void saveHashCategory() { 
       try {
           for(HashCategory hashCategory: hashCategories) {
                FileUtil.writeObject(hashCategory, new File("C:\\" + hashCategory.getName() + ".hash"));
                System.out.println(hashCategory.toString());
           }
       }
       catch(Exception e) {
           e.printStackTrace();
       }
    }
    
    private List<HashCategory> hashCategories;
}
