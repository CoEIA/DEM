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
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;

import java.io.Serializable;

public final class Case implements Serializable {

    // Requaired paramters for case
    private final String investigatorName;
    private final String description;
    private final Date createTime;

    private String caseName;
    private String caseLocation;
    private final List<String> evidenceSourceLocation;
    
    // Optional Paramaters for case

    private boolean isIndex;
    private boolean isHash;
    private boolean isExportLibrary;
    private boolean isClusterWithCase;
    private boolean isClusterWithLibrary;
    private boolean isIndexArchive;
    private boolean isIndexEmbedded;
    private boolean isCacheImages;
    private boolean isExcludeFileSystems;
    private boolean isIndexChatSessions;
    private boolean isDetectBrowserRessions;
    
    // index history information
    private boolean indexStatus;
    private String lastIndexDate;
    private String indexingTime;
    private String caseSource;
    //private int dataIndexedCount, dataIndexedSize ;

    private final boolean doIndexingAfterCaseCreating;
    private final boolean computeHashForEveryItem;
    private final boolean exportCopyOfHashes;
    private final boolean detectDuplicationInCase;
    private final boolean detectDuplicationWithHashLibrary;
    private final boolean indexArchiveFiles;
 

    // Email Configuation
    private final List<EmailConfiguration> emailConfig;

    private Case(Builder builder) {
        this.caseName = builder.caseName;
        this.caseLocation = builder.caseLocation;
        this.investigatorName = builder.investigatorName;
        this.description = builder.description;
        this.createTime = builder.createTime;
        this.evidenceSourceLocation = builder.caseSource;
        this.emailConfig = builder.emailInfo;
        this.doIndexingAfterCaseCreating = builder.isIndexingCaseAfterFinishing;
        this.computeHashForEveryItem = builder.isHash;
        this.exportCopyOfHashes = builder.isExportLibrary;
        this.detectDuplicationInCase = builder.isClusterWithCase;
        this.detectDuplicationWithHashLibrary = builder.isClusterWithLibrary;
        this.indexArchiveFiles = builder.isIndexArchive;
        this.isIndexEmbedded = builder.isIndexEmbedded;
        this.isCacheImages = builder.isCacheImages;
        this.isExcludeFileSystems = builder.isExcludeFileSystems;
        this.isIndexChatSessions  = builder.isIndexChatSessions;
        this.isDetectBrowserRessions  = builder.isDetectBrowserRessions;
    }

    public static class Builder {
        // Requaired Paramaters
        private final String caseName;
        private final String caseLocation;
        private final String investigatorName;
        private final String description;
        private final Date createTime;
        private final List<EmailConfiguration> emailInfo;
        private final List<String> caseSource;
        // optional Params
        private boolean isIndexingCaseAfterFinishing = false;
        private boolean isHash = false;
        private boolean isExportLibrary = false;
        private boolean isClusterWithCase = false;
        private boolean isClusterWithLibrary = false;
        private boolean isIndexArchive = false;
        private boolean isIndexEmbedded = false;
        private boolean isCacheImages = false;
        private boolean isExcludeFileSystems = false;

        private boolean isIndexChatSessions = false;
        private boolean isDetectBrowserRessions = false;
    
        // index history information
        private boolean indexStatus = false;
        private String lastIndexDate = "";
        private String indexingTime = "";
        //private int dataIndexedCount, dataIndexedSize ;

        public Builder(String indexName, String indexLocation, String investigatorName,
                String description, List<String> caseSource, Date createTime, long caseSize) {
            this.caseName = indexName;
            this.caseLocation = indexLocation;
            this.investigatorName = investigatorName;
            this.description = description;
            this.caseSource = caseSource;
            this.createTime = createTime;
            this.emailInfo = new ArrayList<EmailConfiguration>();
        }

        public Case build() {
            return new Case(this);
        }

        public Builder isIndex(boolean val) {
            isIndexingCaseAfterFinishing = val;
            return this;
        }

        public Builder isHash(boolean val) {
            isHash = val;
            return this;
        }

        public Builder isExportLibrary(boolean val) {
            isExportLibrary = val;
            return this;
        }

        public Builder isClusterWithCase(boolean val) {
            isClusterWithCase = val;
            return this;
        }

        public Builder isClusterWithLibrary(boolean val) {
            isClusterWithLibrary = val;
            return this;
        }

        public Builder isIndexArchive(boolean val) {
            isIndexArchive = val;
            return this;
        }

        public Builder isIndexEmbedded(boolean val) {
            isIndexEmbedded = val;
            return this;
        }

        public Builder isCacheImages(boolean val) {
            isCacheImages = val;
            return this;
        }

        public Builder isExcludeFileSystems(boolean val) {
            isExcludeFileSystems = val;
            return this;
        }

        public Builder createEmailConfig(List<EmailConfiguration> configList) {
            emailInfo.addAll(configList);
            return this;
        }
        public Builder isIndexChatSessions(boolean val)
        {
            isIndexChatSessions = val; 
            return this;
        }
        public Builder isDetectBrowserSessiond(boolean val)
        {
            isDetectBrowserRessions = val;
            return this;
        }
    }

    public boolean GetisHash() {
        return this.computeHashForEveryItem;
    }

    public boolean GetisIndex() {
        return this.doIndexingAfterCaseCreating;
    }

    public boolean GetisExportLibrary() {
        return this.exportCopyOfHashes;
    }

    public boolean GetisClusterWithCase() {
        return this.detectDuplicationInCase;
    }


   public boolean isIndexChatSessions() {
       return this.isIndexChatSessions;
   }
   public boolean isDectetBroswerSessions()
   {
       return this.isDetectBrowserRessions;
   }


    public boolean GetisExcludeFileSystems() {
        return this.isExcludeFileSystems;
    }

    public List<EmailConfiguration> getEmailConfig() {
        return Collections.unmodifiableList(this.emailConfig);
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
        List<String> list = new ArrayList<String>();
        for(String s : evidenceSourceLocation)
        {
        list.add(s);
        }
        return list;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public boolean getCacheImages() {
        return isCacheImages;
    }

    public boolean getCheckCompressed() {
        return indexArchiveFiles;
    }
    
    public boolean getCheckEmbedded() {
        return isIndexEmbedded;
    }
    
    // resetting case information
    public void setCaseName(final String name) {
        this.caseName = name;
    }
    
    public void setCaseLocation(final String location) {
        this.caseLocation = location;
    }
    
    public void setEveidenceSourceLocation(final String location) {
        this.evidenceSourceLocation.add(location);
    }
}
