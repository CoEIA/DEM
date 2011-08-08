/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.index;

/**
 *
 * @author wajdyessam
 *
 */

import java.util.ArrayList ;
import java.util.Date ;

import java.io.Serializable ;

public class Index implements Serializable {
    private String indexName ;
    private String indexLocation ;
    private ArrayList<String> documentInIndex ;
    private ArrayList<String> extensionAllowed ;
    private Date createTime ;
    private long indexSize, dataIndexedSize ;
    private int dataIndexedCount ;
    
    public Index () { }
    
    public Index (String indexName, String indexLocation, ArrayList<String> docInIndex, ArrayList<String> ext,
            Date ct, long iz , long diz, int dic ) {
        this.indexName = indexName ;
        this.indexLocation = indexLocation ;
        this.documentInIndex = docInIndex ;
        this.extensionAllowed = ext ;
        this.createTime = ct ;
        this.indexSize = iz ;
        this.dataIndexedSize = diz ;
        this.dataIndexedCount = dic ;
    }

    public void setIndexName (String indexName) { this.indexName = indexName ; }
    public void setIndexLocation (String indexLocation) { this.indexLocation = indexLocation ; }
    public void setDocumentInIndex (ArrayList<String> doc) { this.documentInIndex = doc ; }
    public void setExtensionAllowed (ArrayList<String> ext) { this.extensionAllowed = ext ; }
    public void setCreateTime (Date d) { this.createTime = d ; }
    public void setIndexSize (long size) { this.indexSize = size; }
    public void setDataIndexedSize (long size) { this.dataIndexedSize = size; }
    public void setDataIndexedCount (int count) { this.dataIndexedCount = count ; }
    
    public String getIndexName () { return this.indexName ; }
    public String getIndexLocation () { return this.indexLocation ; }
    public ArrayList<String> getDocumentInIndex() { return this.documentInIndex ;}
    public ArrayList<String> getExtensionAllowed () { return this.extensionAllowed ;}
    public Date getCreateTime () { return this.createTime ;}
    public long getIndexSize () { return this.indexSize ; }
    public long getDataIndexedSize() { return this.dataIndexedSize ;}
    public int getDataIndexedCount () { return this.dataIndexedCount ;}
}
