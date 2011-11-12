/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.tags;

/**
 *
 * @author wajdyessam
 */

import java.util.List ;
import java.util.ArrayList ;
import java.util.Collections;

class TagsDBHandler {
    private String dbLocation ;
    
    public TagsDBHandler(String location) {
        this.dbLocation = location ;
    }
    
    public List<Tag> getTags() {
        return Collections.emptyList();
    }
    
    public void setTags(List<Tag> tags) {
    }
}
