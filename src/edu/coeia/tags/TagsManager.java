/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tags;

import static edu.coeia.util.PreconditionsChecker.* ;

import java.sql.SQLException;
import java.util.List; 
import java.util.ArrayList ;
import java.util.Collections;

/**
 * Handle Tags Management by create new tags database
 * and read/write tags to the database
 * TagsManager contain list of current tags
 * and its written to database when user save the tags
 * using saveTags method
 * 
 * @author wajdyessam
 */
final public class TagsManager {
    
    /**
     * Get New Instance of Tags Manager for this case
     * @param dbLocation is the database location inside the case
     * @return TagsManager for this case
     */
    public static TagsManager getTagsManager(String dbLocation) {
        checkNull("Case location must have a value", dbLocation);
        checkNotEmptyString("Case Location must not be empty string", dbLocation);
        
        return new TagsManager(dbLocation);
    }
    
    /**
     * Add Tags to Tags List
     * @param tag 
     */
    public void addTag(Tag tag) {
        this.tags.add(tag);
    }
    
    /**
     * Remove Tag From tag List
     * @param index 
     */
    public void removeTag(int index) {
        this.tags.remove(index);
    }
    
    /**
     * Read all the current tags , not modifiable list
     * @return 
     */
    public List<Tag> readTags() {
        return Collections.unmodifiableList(this.tags);
    }
    
    /**
     * write all the current tags to the database
     */
    public void saveTags() {
        this.tagsDataBase.setTags(this.tags);
    }
    
    /**
     * shutdown tags manager
     */
    public void closeManager() {
        try {
            this.tagsDataBase.closeDB();
        }
        catch (SQLException e){
            if ( e.getErrorCode() == 50000 && ("XJ015").equals(e.getSQLState()))
                System.out.println("Derby Shutdown normally");
            else {
                System.out.println("Derby Did not shutdown normally");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * private constructor
     * create new database and then read tags to tags list
     */
    private TagsManager(String dbLocation) {
        this.tags = new ArrayList<Tag>();
        
        try {
            this.tagsDataBase = TagsDBHandler.newInstance(dbLocation);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        this.tags.addAll(this.tagsDataBase.getTags());
    }
    
    private List<Tag> tags;
    private TagsDBHandler tagsDataBase;
}
