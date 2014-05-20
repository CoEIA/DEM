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
package edu.coeia.cases;

import edu.coeia.tags.Tag;
import edu.coeia.tags.TagsDBHandler;
import static edu.coeia.util.PreconditionsChecker.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handle Tags Management by create new tags database
 * and read/write tags to the database
 * CaseTags contain list of current tags
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
final class CaseTags {
    
    public CaseTags(final String dbLocation){
        checkNull("Case location must have a value", dbLocation);
        checkNotEmptyString("Case Location must not be empty string", dbLocation);
        
        this.tags = new ArrayList<Tag>();
        this.tagsCopy = new ArrayList<Tag>();
        this.dataBaseLocation = dbLocation;
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
    public boolean saveTags() {
        try {
            return this.tagsDataBase.writeTagsToDatabase(this.tags);
        } catch (Exception ex) {
            Logger.getLogger(CaseTags.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * check if monitor tags is different from database state
     */
    public boolean isTagsDbModified() {
        boolean result = false;
        
        if ( this.tags.size() != this.tagsCopy.size() ) {
            return !result ;
        }
        
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
     * open connections to existing database
     * the connection should be closed after finishing the 
     * database operation
     */
    public void openDatabase(){
        try {
            this.tagsDataBase = new TagsDBHandler(this.dataBaseLocation, false);
            this.tags.addAll(this.tagsDataBase.readTagsFromDataBase());
            this.updateMonitorChangingList();
        } catch (Exception ex) {
            Logger.getLogger(CaseTags.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
     * create new tags database (make table structure)
     * no connection is created after calling this method,
     * user should not call close database after this method
     * because it will already closed after creating the database 
     * file
     */
    public void createDatabase() {
        try {
            this.tagsDataBase = new TagsDBHandler(this.dataBaseLocation, true);
        } catch (Exception ex) {
            Logger.getLogger(CaseTags.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
     * update monitor tags
     * when saving new tags we should update the list
     */
    public void updateMonitorChangingList() {
        this.tagsCopy.clear();
        this.tagsCopy.addAll(this.getTags());
    }
    
    public void close() {
        try {
            this.tagsDataBase.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(CaseTags.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private final List<Tag> tags, tagsCopy;
    private TagsDBHandler tagsDataBase;
    private final String dataBaseLocation;
}
