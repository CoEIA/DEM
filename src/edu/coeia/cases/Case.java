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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;


public class Case implements Serializable {

    private final String indexName;
    private final String indexLocation;
    private final String investigatorName;
    private final String description;
    private final Date createTime;
    private final long caseSize;
    private boolean isIndex;
    private boolean isHash;
    private boolean isExportLibrary;
    private boolean isClusterWithCase;
    private boolean isClusterWithLibrary;
    private boolean isIndexArchive;
    private boolean isIndexEmbedded;
    private boolean isCacheImages;
    private boolean isExcludeFileSystems;
    // index history information
    private boolean indexStatus;
    private String lastIndexDate;
    private String indexingTime;
    private String caseSource;
    //private int dataIndexedCount, dataIndexedSize ;
    private List<EmailConfig> emailInfo;

    private Case(Builder builder) {

        this.indexName = builder.indexName;
        this.indexLocation = builder.indexLocation;
        this.investigatorName = builder.investigatorName;
        this.description = builder.description;
        this.createTime = builder.createTime;
        this.caseSize = builder.caseSize;
        this.indexStatus = builder.indexStatus;
        this.lastIndexDate = builder.lastIndexDate;
        this.indexingTime = builder.indexingTime;
        this.caseSource = builder.caseSource;
        this.emailInfo = builder.emailInfo;

        this.isIndex = builder.isIndex;
        this.isHash = builder.isHash;
        this.isExportLibrary = builder.isExportLibrary;
        this.isClusterWithCase = builder.isClusterWithCase;
        this.isClusterWithLibrary = builder.isClusterWithLibrary;
        this.isIndexArchive = builder.isIndexArchive;
        this.isIndexEmbedded = builder.isIndexEmbedded;
        this.isCacheImages = builder.isCacheImages;
        this.isExcludeFileSystems = builder.isExcludeFileSystems;


    }

    public static class Builder {

        private final String indexName;
        private final String indexLocation;
        private final String investigatorName;
        private final String description;
        private final String caseSource;
        private final Date createTime;
        private final long caseSize;
        private boolean isIndex = false;
        private boolean isHash = false;
        private boolean isExportLibrary = false;
        private boolean isClusterWithCase = false;
        private boolean isClusterWithLibrary = false;
        private boolean isIndexArchive = false;
        private boolean isIndexEmbedded = false;
        private boolean isCacheImages = false;
        private boolean isExcludeFileSystems = false;
        private List<EmailConfig> emailInfo;
        // index history information
        private boolean indexStatus = false;
        private String lastIndexDate = "";
        private String indexingTime = "";
        //private int dataIndexedCount, dataIndexedSize ;
        private boolean cacheImages = false;
        private boolean checkCompressed = false;
        private ArrayList<String> documentInIndex = new ArrayList<String>();
        private List<String> extensionAllowed = new ArrayList<String>();
        private ArrayList<String> pstPath = new ArrayList<String>();
        private ArrayList<String> msnPath = new ArrayList<String>();
        private ArrayList<String> yahooPath = new ArrayList<String>();
        private ArrayList<String> skypePath  = new ArrayList<String>();
        private ArrayList<String> iePath  = new ArrayList<String>();
        private ArrayList<String> ffPath  = new ArrayList<String>();

        public Builder(String indexName, String indexLocation, String investigatorName,
                String description, String caseSource, Date createTime, long caseSize) {
            this.indexName = indexName;
            this.indexLocation = indexLocation;
            this.investigatorName = investigatorName;
            this.description = description;
            this.caseSource = caseSource;
            this.createTime = createTime;
            this.caseSize = caseSize;


        }

        public Case build() {

            return new Case(this);
        }

        public Builder(String indexName, String indexLocation, String investigatorName, String description,
                String caseSource, long caseSize, Date creationDate) {
            this.indexName = indexName;
            this.indexLocation = indexLocation;
            this.investigatorName = investigatorName;
            this.description = description;
            this.caseSource = caseSource;
            this.caseSize = caseSize;
            this.createTime = creationDate;
        }

        public Builder isIndex(boolean val) {
            isIndex = val;
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

        public Builder createEmailConfig(List<EmailConfig> configList) {
            emailInfo = configList;
            return this;
        }
    }

//    public void setIndexName (String indexName) { this.indexName = indexName ; }
//    public void setIndexLocation (String indexLocation) { this.indexLocation = indexLocation ; }
//    public void setInvestigatorName (String name) { this.investigatorName = name; }
//    public void setDescription(String desc) { this.description = desc; }
    public void setIndexStatus(boolean status) {
    }

//    public void setDocumentInIndex (ArrayList<String> doc) { this.documentInIndex = doc ; }
//    public void setExtensionAllowed (ArrayList<String> ext) { this.extensionAllowed = ext ; }
//
//    public void setPstPath (ArrayList<String> pst) { this.pstPath = pst ; }
//
//    public void setIePath (ArrayList<String> ie) { this.iePath = ie; }
//    public void setFFPath (ArrayList<String> ff) { this.ffPath = ff ;}
//    
//    public void setMsnPath (ArrayList<String> msn) { this.msnPath = msn ; }
//    public void setYahooPath (ArrayList<String> yahoo) { this.yahooPath = yahoo ; }
//    public void setSkypePath (ArrayList<String> skype) { this.skypePath= skype ; }
//    public void setCreateTime (Date d) { this.createTime = d ; }
//    public void setCaseSize (long size) { this.caseSize = size; }
//    public void setDataIndexedSize (long size) { this.dataIndexedSize = size; }
//    public void setDataIndexedCount (int count) { this.dataIndexedCount = count ; }
//    public void setCacheImages (boolean value) { this.cacheImages = value ; }
//    public void setCheckCompressed (boolean value) { this.checkCompressed = value ; }
    public void setLastIndexDate(String value) {
    }

    public void setIndexingTime(String value) {
    }

    public String getIndexName() {
        return this.indexName;
    }
    
    public List<EmailConfig> getEmailConfig() {
        return this.emailInfo;
    }

    public String getIndexLocation() {
        return this.indexLocation;
    }

    public String getInvestigatorName() {
        return this.investigatorName;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean getIndexStatus() {
        return this.indexStatus;
    }

    public List<String> getDocumentInIndex() {
        return Collections.emptyList();
    }

    public List<String> getExtensionAllowed() {
        return Collections.emptyList();
    }

    public  List<String> getPstPath() {
        return Collections.emptyList();
    }

    public  List<String> getIePath() {
        return Collections.emptyList();
    }

    public  List<String> getFFPath() {
        return Collections.emptyList();
    }

    public List<String> getMsnPath() {
        return Collections.emptyList();
    }

    public List<String> getYahooPath() {
        return Collections.emptyList();
    }

    public List<String> getSkypePath() {
        return Collections.emptyList();
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public long getCaseSize() {
        return this.caseSize;
    }
    //public long getDataIndexedSize() { return this.dataIndexedSize ;}
    //public int getDataIndexedCount () { return this.dataIndexedCount ;}

    public boolean getCacheImages() {
        return false;
    }

    public boolean getCheckCompressed() {
        return false;
    }

    public String getLastIndexDate() {
        return this.lastIndexDate;
    }

    public String getIndexingTime() {
        return this.indexingTime;
    }
}
