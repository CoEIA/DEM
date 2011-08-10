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

import java.util.List ;
import java.util.ArrayList ;
import java.util.Date ;

import java.io.Serializable ;

public class Case implements Serializable {
    private String indexName ;
    private String indexLocation ;
    private String investigatorName ;
    private String description ;
    
    private ArrayList<String> documentInIndex ;
    private List<String> extensionAllowed ;

    private ArrayList<String> pstPath ;
    private ArrayList<String> msnPath,yahooPath,skypePath;
    private ArrayList<String> iePath, ffPath ;
    
    private Date createTime ;
    private long indexSize, dataIndexedSize ;
    private int dataIndexedCount ;

    private boolean cacheImages ;
    private boolean checkCompressed ;

    // index history information
    private boolean indexStatus ;
    private String lastIndexDate ;
    private String indexingTime ;
    
    private Case () { }

    public Case (String indexName, String indexLocation, String investigatorName, String description,
            ArrayList<String> docInIndex, List<String> ext, ArrayList<String> pstPath,
            ArrayList<String> iePath, ArrayList<String> ffPath, ArrayList<String> msnPath, ArrayList<String> yahooPath,
            ArrayList<String> skypePath, Date ct, long iz , long diz, int dic ,
            boolean cacheImages, boolean checkCompressed, boolean indexStatus, String lastIndexDate, String indexingTime
            ) {
            
        this.indexName = indexName ;
        this.indexLocation = indexLocation ;
        this.investigatorName = investigatorName ;
        this.description = description ;
        this.indexStatus = indexStatus ;
        
        this.documentInIndex = docInIndex ;
        this.extensionAllowed = ext ;

        this.pstPath = pstPath ;

        this.iePath = iePath ;
        this.ffPath = ffPath ;

        this.msnPath = msnPath ;
        this.yahooPath = yahooPath; 
        this.skypePath = skypePath;
        
        this.createTime = ct ;
        this.indexSize = iz ;
        this.dataIndexedSize = diz ;
        this.dataIndexedCount = dic ;

        this.cacheImages = cacheImages ;
        this.checkCompressed = checkCompressed ;

        this.lastIndexDate = lastIndexDate ;
        this.indexingTime = indexingTime ;
    }
    
    public void setIndexName (String indexName) { this.indexName = indexName ; }
    public void setIndexLocation (String indexLocation) { this.indexLocation = indexLocation ; }
    public void setInvestigatorName (String name) { this.investigatorName = name; }
    public void setDescription(String desc) { this.description = desc; }
    public void setIndexStatus (boolean status) { this.indexStatus = status ; }
    
    public void setDocumentInIndex (ArrayList<String> doc) { this.documentInIndex = doc ; }
    public void setExtensionAllowed (ArrayList<String> ext) { this.extensionAllowed = ext ; }

    public void setPstPath (ArrayList<String> pst) { this.pstPath = pst ; }

    public void setIePath (ArrayList<String> ie) { this.iePath = ie; }
    public void setFFPath (ArrayList<String> ff) { this.ffPath = ff ;}
    
    public void setMsnPath (ArrayList<String> msn) { this.msnPath = msn ; }
    public void setYahooPath (ArrayList<String> yahoo) { this.yahooPath = yahoo ; }
    public void setSkypePath (ArrayList<String> skype) { this.skypePath= skype ; }

    public void setCreateTime (Date d) { this.createTime = d ; }
    public void setIndexSize (long size) { this.indexSize = size; }
    public void setDataIndexedSize (long size) { this.dataIndexedSize = size; }
    public void setDataIndexedCount (int count) { this.dataIndexedCount = count ; }

    public void setCacheImages (boolean value) { this.cacheImages = value ; }
    public void setCheckCompressed (boolean value) { this.checkCompressed = value ; }
    public void setLastIndexDate (String value) { this.lastIndexDate = value ; }
    public void setIndexingTime (String value) { this.indexingTime = value ; }
    
    public String getIndexName () { return this.indexName ; }
    public String getIndexLocation () { return this.indexLocation ; }
    public String getInvestigatorName () { return this.investigatorName ; }
    public String getDescription () { return this.description ; }
    public boolean getIndexStatus () { return this.indexStatus ;}
    
    public ArrayList<String> getDocumentInIndex() { return this.documentInIndex ;}
    public List<String> getExtensionAllowed () { return this.extensionAllowed ;}

    public ArrayList<String> getPstPath() { return this.pstPath ; }

    public ArrayList<String> getIePath() { return this.iePath ; }
    public ArrayList<String> getFFPath() { return this.ffPath ; }

    public ArrayList<String> getMsnPath () { return this.msnPath ;}
    public ArrayList<String> getYahooPath() { return this.yahooPath ;}
    public ArrayList<String> getSkypePath () { return this.skypePath ; }
    
    public Date getCreateTime () { return this.createTime ;}
    public long getIndexSize () { return this.indexSize ; }
    public long getDataIndexedSize() { return this.dataIndexedSize ;}
    public int getDataIndexedCount () { return this.dataIndexedCount ;}

    public boolean getCacheImages () { return this.cacheImages ; }
    public boolean getCheckCompressed () { return this.checkCompressed ; }

    public String getLastIndexDate () { return this.lastIndexDate ; }
    public String getIndexingTime () { return this.indexingTime ;}
}
