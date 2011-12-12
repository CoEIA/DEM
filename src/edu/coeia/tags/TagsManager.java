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
 * the tags manager contain list of unchanged tags that is reading when accessing db
 * this list is using to verify the that the tags list is changed or not
 * so the user can know their is modification in tags list
 * and ask application user to save it
 * 
 * @author wajdyessam
 */
final public class TagsManager {
    
    /**
     * Get New Instance of Tags Manager for this case
     * its will be calling one time during case opening, but there is no harm
     * to re-call to with multiple time during case, since its will open the location
     * if there is database in this location
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
    public List<Tag> getTags() {
        return Collections.unmodifiableList(this.tags);
    }
    
    /**
     * write all the current tags to the database
     */
    public boolean setTags() {
        return this.tagsDataBase.setTags(this.tags);
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
     * check if monitor tags is different from database state
     */
    public boolean isTagsDbModified() {
        boolean result = false;
        
        if ( this.tags.size() != this.tagsCopy.size() )
            return !result ;
        
        for(int i=0; i<this.tags.size(); i++) {
            Tag tag1 = this.tags.get(i);
            Tag tag2 = this.tagsCopy.get(i);
            
            if ( ! tag1.equals(tag2) ) {
                result = true;
                break ;
            }
        }
        
        return result ;
    }
    
    /**
     * private constructor
     * create new database and then read tags to tags list
     */
    private TagsManager(String dbLocation) {
        this.tags = new ArrayList<Tag>();
        this.tagsCopy = new ArrayList<Tag>();
        
        try {
            this.tagsDataBase = TagsDBHandler.newInstance(dbLocation);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        this.tags.addAll(this.tagsDataBase.getTags());
        this.updateMonitorChangingList();
    }
    
    /**
     * update monitor tags
     * when saving new tags we should update the list
     */
    public void updateMonitorChangingList() {
        this.tagsCopy.clear();
        this.tagsCopy.addAll(this.getTags());
    }
    
    private final List<Tag> tags, tagsCopy;
    private TagsDBHandler tagsDataBase;
}
