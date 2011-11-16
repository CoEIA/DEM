/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tags;

/**
 * Tag class
 * Represent Tag in DEM that contain creator name and the date of creation
 * and contain the message of this tags
 * 
 * @author wajdyessam
 */

import java.util.Date ;

public final class Tag {
    
    /**
     * static factory method
     * @param name the name tag creator
     * @param date the date of tag creation
     * @param msg the message for this tag
     * @return new Tag object
     */
    public static Tag newInstance(String name, Date date, String msg) {
        return new Tag(name, new Date(date.getTime()), msg);
    }
    
    /**
     * private constructor 
     */
    private Tag(String name, Date date, String msg) {
        this.creationDate = date; 
        this.creatorName = name ;
        this.message = msg;
    }
    
    /**
     * toString implementation, for debugging purpose
     * @return 
     */
    @Override
    public String toString() {
        return String.format("%s[name=%s, date=%s, message=%s]", this.getClass().getName(),
                this.getName(), this.getDate().toString(), this.getMessage());
    }
    
    public String getName() { return this.creatorName ; }
    public String getMessage() { return this.message ; }
    public Date getDate() { return new Date(this.creationDate.getTime()) ; }
    
    private final Date creationDate ;
    private final String creatorName ;
    private final String message ;
}
