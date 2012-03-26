/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

import edu.coeia.wizard.EmailConfiguration;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;

import java.io.Serializable;

/**
 * Case object have collections of attribute that define 
 * what user options he need when defining case
 * 
 * when saved case object it will be saving the whole state
 * so we make the case Serializable for easy saving and retrieving
 * the attributes
 * 
 * some of these attributes cannot be changed when the case object is 
 * constructed ( Immutable attributes) and some of them will be modified
 * later on, even after serializable and de-serializable it
 * 
 * also some attributes is needed when constructing the object
 * and its marked as final attributes, and some attributes the Case
 * object can constructed with the default value of it (non-final) fields
 * 
 * because the case non-final fields and the finality of Case object
 * the case object will be build from inner CaseBuilder class
 * that will set default value for non-final fields and set these fields
 * in the Case constructor
 * 
 * @author wajdyessam
 */
public final class Case implements Serializable {

    // Required (Immutable fields) paramters for case
    private final String investigatorName;
    private final String description;
    private final Date createTime;

    // Required (can be changed) paramaters for case
    private final List<String> evidenceSourceLocation = new ArrayList<String>();
    private String caseName;
    private String caseLocation;
    
    // Optional Paramaters for case, cannot be changed
    private final List<EmailConfiguration> emaiConfigurations;
    
    private final boolean computeHashForEveryItem;
    private final boolean detectDuplicationInCase;
    private final boolean detectDuplicationWithHashLibrary;
    
    private final boolean indexArchiveFiles;
    private final boolean indexChatSessions;
    private final boolean indexEmbeddedDocuments;
    private final boolean indexBroswers;
    private final boolean cacheImage;

    private Case(final CaseBuilder builder) {
        this.caseName = builder.caseName;
        this.caseLocation = builder.caseLocation;
        this.investigatorName = builder.investigatorName;
        this.description = builder.description;
        this.createTime = builder.createTime;
        this.evidenceSourceLocation.addAll(builder.evidenceSourceLocation);
        this.emaiConfigurations = builder.emaiConfigurations;
        this.computeHashForEveryItem = builder.computeHashForEveryItem;
        this.detectDuplicationInCase = builder.detectDuplicationInCase;
        this.detectDuplicationWithHashLibrary = builder.detectDuplicationWithHashLibrary;
        this.indexArchiveFiles = builder.indexArchiveFiles;
        this.indexEmbeddedDocuments = builder.indexEmbeddedDocuments;
        this.cacheImage = builder.cacheImage;
        this.indexChatSessions  = builder.indexChatSessions;
        this.indexBroswers  = builder.indexBroswers;
    }

    public boolean computeHashForEveryItem() {
        return this.computeHashForEveryItem;
    }

    public boolean detectDuplicationWithinCase() {
        return this.detectDuplicationInCase;
    }

    public boolean detectDuplicationWithHashLibrary() {
        return this.detectDuplicationWithHashLibrary;
    }
    
    public boolean isIndexChatSessions() {
       return this.indexChatSessions;
    }
   
    public boolean detectInternetBrowsers() {
       return this.indexBroswers;
    }

    public List<EmailConfiguration> getEmailConfigurations() {
        return Collections.unmodifiableList(this.emaiConfigurations);
    }

    public String getCaseName() {
        return this.caseName;
    }

    public String getCaseLocation() {
        return this.caseLocation;
    }

    public String getInvestigatorName() {
        return this.investigatorName;
    }

    public String getDescription() {
        return this.description;
    }

    public List<String> getEvidenceSourceLocation() {
        return Collections.unmodifiableList(this.evidenceSourceLocation);
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public boolean getCacheImages() {
        return cacheImage;
    }

    public boolean getCheckCompressed() {
        return indexArchiveFiles;
    }
    
    public boolean getCheckEmbedded() {
        return indexEmbeddedDocuments;
    }
    
    // resetting case information
    public void setCaseName(final String name) {
        this.caseName = name;
    }
    
    public void setCaseLocation(final String location) {
        this.caseLocation = location;
    }
    
    public void addEvidenceSourceLocation(final String location) {
        this.evidenceSourceLocation.add(location);
    }
    
    public void removeEvidenceSourceLocation(final String location) {
        this.evidenceSourceLocation.remove(location);
    }
    
    public static class CaseBuilder {
        // Requaired (constant) paramters for case
        private final String investigatorName;
        private final String description;
        private final Date createTime;

        // Required (can be changed) paramaters for case
        private final List<String> evidenceSourceLocation;
        private final String caseName;
        private final String caseLocation;

        // Optional Paramaters for case
        private List<EmailConfiguration> emaiConfigurations;
        
        private boolean computeHashForEveryItem;
        private boolean detectDuplicationInCase;
        private boolean detectDuplicationWithHashLibrary;
        
        private boolean indexArchiveFiles;
        private boolean indexChatSessions;
        private boolean indexEmbeddedDocuments;
        private boolean indexBroswers;
        private boolean cacheImage;
    
         public CaseBuilder(final String indexName, final String indexLocation, 
                 final String investigatorName, final String description,
                 final List<String> evidenceFolders,
                 final Date createTime) {   
            this.caseName = indexName;
            this.caseLocation = indexLocation;
            this.investigatorName = investigatorName;
            this.description = description;
            this.evidenceSourceLocation = new ArrayList<String>();
            this.evidenceSourceLocation.addAll(evidenceFolders);
            this.createTime = new Date(createTime.getTime());
            this.emaiConfigurations = new ArrayList<EmailConfiguration>();
        }

        public Case build() {
            return new Case(this);
        }

        public CaseBuilder indexArchiveFiles(boolean value) {
            this.indexArchiveFiles = value;
            return this;
        }
        
        public CaseBuilder indexEmbeddedDocuments(boolean value) {
            this.indexEmbeddedDocuments = value;
            return this;
        }
                
        public CaseBuilder computeHashForEveryItem(boolean value) {
            this.computeHashForEveryItem = value;
            return this;
        }

        public CaseBuilder detectDuplicationWithinCase(boolean val) {
            this.detectDuplicationInCase = val;
            return this;
        }

        public CaseBuilder detectDuplicationWithHashLibrary(boolean val) {
            this.detectDuplicationWithHashLibrary = val;
            return this;
        }

        public CaseBuilder isIndexChatSessions(boolean val) {
           this.indexChatSessions = val;
           return this;
        }

        public CaseBuilder detectInternetBrowsers(boolean val) {
           this.indexBroswers = val;
           return this;
        }

        public CaseBuilder getCacheImages(boolean val) {
            this.cacheImage = val;
            return this;
        }

        public CaseBuilder getCheckCompressed(boolean val) {
            this.indexArchiveFiles = val;
            return this;
        }

        public CaseBuilder getCheckEmbedded(boolean val) {
            this.indexEmbeddedDocuments = val;
            return this;
        }
    
        public CaseBuilder addEmailConfiguration(final List<EmailConfiguration> emails) {
            this.emaiConfigurations = Collections.unmodifiableList(emails);
            return this;
        }
    }
}
