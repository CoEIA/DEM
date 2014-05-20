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

import java.util.Date ;
import java.io.Serializable; 

/**
 *
 * Hash Item Represent any item that we will adding
 * to the hash set in the hash library
 * 
 * HashItem will equal another HashItem when they have the same hash value
 * regardless of other informations
 * 
 * @author wajdyessam
 */
public final class HashItem implements Serializable{
    
    /**
     * create new instance of HashItem class
     * @param fileName the file name that we want to put in hash set
     * @param filePath the absolute file path 
     * @param caseName the case name that the fileName exist on
     * @param casePath the absolute to the case
     * @param investigatorName the name of person who want to add this item
     * @param time the current time of adding item
     * @param hashValue the MD5 hash value for the file that pointed by the filePath
     * @return New Instance of HashItem
     */
    public static HashItem newInstance(final String fileName, final String filePath,
            final String caseName, final String casePath, final String investigatorName, 
            final Date time, final String hashValue) {
        
        return new HashItem(fileName, filePath, caseName, casePath, investigatorName,
                time, hashValue);
    }
    
    /**
     * Prevent Construction outside this class
     */
    private HashItem(final String fileName, final String filePath, 
            final String caseName, final String casePath, final String investigatorName, 
            final Date time, final String hashValue) {
        
        this.fileName = fileName ;
        this.filePath = filePath ;
        this.caseName = caseName ;
        this.casePath = casePath ;
        this.invesitgatorName = investigatorName;
        this.time = new Date( time.getTime() ); // defense copy of time
        this.hashValue = hashValue;
    }
    
    /**
     *  Getter Methods
     */
    public String getFileName() { return this.fileName ; }
    public String getFilePath() { return this.filePath ; }
    public String getCaseName() { return this.caseName ; }
    public String getCasePath() { return this.casePath ; }
    public String getInvestigatorName() { return this.invesitgatorName ;}
    public Date getTime() { return new Date(this.time.getTime()); } // Defense Copy
    public String getHashValue() { return this.hashValue ; }
    
    @Override
    public boolean equals(Object object) {
        if ( this == object ) 
            return true;
        
        if ( object == null)
            return false;
        
        if ( this.getClass() != object.getClass() )
            return false; 
        
        HashItem item = (HashItem) object;
        return this.getHashValue().equals(item.getHashValue());
    }
    
    @Override 
    public int hashCode() {
        return 7 * this.getHashValue().hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("%s[file: %s, hash: %s, case: %s]", this.getClass().getName(), this.fileName, this.hashValue, this.caseName);
    }
    
    private final String fileName ;
    private final String filePath ;
    private final String caseName ;
    private final String casePath ;
    private final String invesitgatorName; 
    private final Date time ;
    private final String hashValue ;
}
