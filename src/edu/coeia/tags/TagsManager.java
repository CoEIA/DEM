/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tags;

/**
 *
 * @author wajdyessam
 */

import static edu.coeia.util.PreconditionsChecker.* ;

import java.util.List; 
import java.util.ArrayList ;
import java.util.Collections;

public class TagsManager {
    
    public static TagsManager getTagsManager(String dbLocation) {
        checkNull("Case location must have a value", dbLocation);
        checkNotEmptyString("Case Location must not be empty string", dbLocation);
        
        return new TagsManager(dbLocation);
    }
    
    private TagsManager(String dbLocation) {
        this.tags = new ArrayList<Tag>();
        this.tagsDataBase = new TagsDBHandler(dbLocation);
        
        this.tags.addAll(this.tagsDataBase.getTags());
    }
    
    public void addTag(Tag tag) {
        this.tags.add(tag);
    }
    
    public void removeTag(int index) {
        this.tags.remove(index);
    }
    
    public List<Tag> readTags() {
        return Collections.unmodifiableList(this.tags);
    }
    
    public void saveTags() {
        this.tagsDataBase.setTags(this.tags);
    }
    
    private List<Tag> tags;
    private TagsDBHandler tagsDataBase;
}
