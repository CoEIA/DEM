/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.cases;

/**
 *
 * @author wajdyessam
 *
 */

import edu.coeia.wizard.EmailConfiguration;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;

import java.io.Serializable;

public final class Case implements Serializable {

    // Requaired (constant) paramters for case
    private final String investigatorName;
    private final String description;
    private final Date createTime;

    // Required (can be changed) paramaters for case
    private final List<String> evidenceSourceLocation = new ArrayList<String>();
    private String caseName;
    private String caseLocation;
    
    // Optional Paramaters for case
    private final List<EmailConfiguration> emaiConfigurations;
    private final boolean doIndexingAfterCaseCreating;
    private final boolean computeHashForEveryItem;
    private final boolean detectDuplicationInCase;
    private final boolean detectDuplicationWithHashLibrary;
    private final boolean indexArchiveFiles;
    private final boolean indexChatSessions;
    private final boolean indexEmbeddedDocuments;
    private final boolean indexBroswers;
    private final boolean cacheImage;
    private final boolean execludeFileSystem;

    private Case(final CaseBuilder builder) {
        this.caseName = builder.caseName;
        this.caseLocation = builder.caseLocation;
        this.investigatorName = builder.investigatorName;
        this.description = builder.description;
        this.createTime = builder.createTime;
        this.evidenceSourceLocation.addAll(builder.evidenceSourceLocation);
        this.emaiConfigurations = builder.emaiConfigurations;
        this.doIndexingAfterCaseCreating = builder.doIndexingAfterCaseCreating;
        this.computeHashForEveryItem = builder.computeHashForEveryItem;
        this.detectDuplicationInCase = builder.detectDuplicationInCase;
        this.detectDuplicationWithHashLibrary = builder.detectDuplicationWithHashLibrary;
        this.indexArchiveFiles = builder.indexArchiveFiles;
        this.indexEmbeddedDocuments = builder.indexEmbeddedDocuments;
        this.cacheImage = builder.cacheImage;
        this.execludeFileSystem = builder.execludeFileSystem;
        this.indexChatSessions  = builder.indexChatSessions;
        this.indexBroswers  = builder.indexBroswers;
    }

    public boolean computeHashForEveryItem() {
        return this.computeHashForEveryItem;
    }

    public boolean doIndexingAfterCaseCreation() {
        return this.doIndexingAfterCaseCreating;
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

    public boolean execludeFileSystem() {
        return this.execludeFileSystem;
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
        return (this.evidenceSourceLocation);
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
        private boolean doIndexingAfterCaseCreating;
        private boolean computeHashForEveryItem;
        private boolean detectDuplicationInCase;
        private boolean detectDuplicationWithHashLibrary;
        private boolean indexArchiveFiles;
        private boolean indexChatSessions;
        private boolean indexEmbeddedDocuments;
        private boolean indexBroswers;
        private boolean cacheImage;
        private boolean execludeFileSystem;
    
         public CaseBuilder(final String indexName, final String indexLocation, 
                 final String investigatorName, final String description,
                 final List<String> evidenceFolders,
                 final Date createTime, final long caseSize) {
             
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

        public CaseBuilder doIndexingAfterCaseCreation(boolean val) {
            this.doIndexingAfterCaseCreating = val;
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

        public CaseBuilder execludeFileSystem(boolean val) {
            this.execludeFileSystem = val;
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
