/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.items;

/**
 *
 * @author wajdyessam
 */
public final class FileItem extends Item{
    
    public FileItem(final int documentId, final int documentParentId, final String hash,
            final String fileName, final String fileTitle, final String fileContent, 
            final String fileDate, final String fileMimeType) {
   
        super(documentId, documentParentId, hash);
        
        this.fileName = fileName ;
        this.fileTitle = fileTitle; 
        this.fileContent = fileContent;
        this.fileDate = fileDate;
        this.fileMimeType = fileMimeType;
    }
    
    public String getFileName()  { return this.fileName ; }
    public String getFileTitle() { return this.fileTitle; }
    public String getFileContent() { return this.fileContent ;}
    public String getFileDate() { return this.fileDate ;}
    public String getFileMimeType() { return this.fileMimeType ;}
    
    private final String fileName ;
    private final String fileTitle;
    private final String fileContent; 
    private final String fileDate ;
    private final String fileMimeType;
}
