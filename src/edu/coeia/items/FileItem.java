/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.items;

import edu.coeia.constants.IndexingConstant;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author wajdyessam
 */
public final class FileItem extends Item{
    
    public FileItem(final int documentId, final int documentParentId, final String hash, final String description,
            final String fileName, final String filePath, final String fileContent, 
            final String fileDate, final String fileMimeType, final String metadata) {
   
        super(documentId, documentParentId, hash, description);
        
        this.fileName = fileName ;
        this.filePath = filePath; 
        this.fileContent = fileContent;
        this.fileDate = fileDate;
        this.fileMimeType = fileMimeType;
        this.metadata = metadata;
    }
    
    public String getFileName()  { return this.fileName ; }
    public String getFilePath() { return this.filePath; }
    public String getFileContent() { return this.fileContent ;}
    public String getFileDate() { return this.fileDate ;}
    public String getFileMimeType() { return this.fileMimeType ;}
    public String getMetadata() { return this.metadata; }
    
    @Override
    public Object[] getDisplayData() {
        Object[] object = new Object[] {this.documentId, this.fileName, this.fileDate,  this.getLabel() , this.filePath};
        return object;
    }
    
    private JLabel getLabel() {
        ImageIcon icon  = null;
        
        if ( this.documentDescription.equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_DESCRIPTION_TYPE.NORMAL_FILE)) )
            icon = new ImageIcon(getClass().getResource("/edu/coeia/main/resources/file_16.png"));
        else if ( this.documentDescription.equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_DESCRIPTION_TYPE.EMBEDDED_FILE)) ) 
            icon = new ImageIcon(getClass().getResource("/edu/coeia/main/resources/zip_16.png"));
        else if ( this.documentDescription.equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_DESCRIPTION_TYPE.IMAGE)) ) 
            icon = new ImageIcon(getClass().getResource("/edu/coeia/main/resources/image_16.png"));
        else if ( this.documentDescription.equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_DESCRIPTION_TYPE.EMBEDDED_IMAGE)) ) 
            icon = new ImageIcon(getClass().getResource("/edu/coeia/main/resources/embedded_image_16.png"));
        else if ( this.documentDescription.equals(IndexingConstant.fromDocumentTypeToString(IndexingConstant.DOCUMENT_DESCRIPTION_TYPE.EMAIL_ATTACHMENT))) 
            icon = new ImageIcon(getClass().getResource("/edu/coeia/main/resources/attachment_16.png"));
        
        return new JLabel(this.documentDescription, icon,SwingConstants.LEFT);
    }
    
    private final String fileName ;
    private final String filePath;
    private final String fileContent; 
    private final String fileDate ;
    private final String fileMimeType;
    private final String metadata;
}
