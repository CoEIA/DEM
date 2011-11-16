/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.searching;

/**
 * Builder Pattern
 * @author wajdyessam
 */

final class SearchScope {
    
    private final boolean fileSystemContent ;
    private final boolean fileSystemMetadata ;
    private final boolean emailContent ;
    private final boolean emailHeader ;
    private final boolean chatContent ;
    
    static class Builder {
        // Optional paramters - initialized to default values
        private boolean fileSystemContent  = false ;
        private boolean fileSystemMetadata = false ;
        private boolean emailContent = false ;
        private boolean emailHeader = false ;
        private boolean chatContent = false ;
        
        public Builder fileSystemContent(boolean support) {
            fileSystemContent = support ;
            return this;
        }
        
        public Builder fileSystemMetadata(boolean support) {
            fileSystemMetadata = support ;
            return this;
        }
        
        public Builder emailContent (boolean support) {
            emailContent = support ;
            return this;
        }
        
        public Builder emailHeader (boolean support) {
            emailHeader = support ;
            return this;
        }
        
        public Builder chatContent (boolean support) {
            chatContent = support; 
            return this;
        }
        
        public SearchScope build() {
            return new SearchScope(this);
        }
    }
    
    private SearchScope (Builder builder) {
        this.fileSystemContent = builder.fileSystemContent; 
        this.fileSystemMetadata = builder.fileSystemMetadata ;
        this.emailContent = builder.emailContent ;
        this.emailHeader = builder.emailHeader ;
        this.chatContent = builder.chatContent ;
    }
    
    // getter method
    boolean haveFileSystemContent() { return this.fileSystemContent ; }
    boolean haveFileSystemMetadata() { return this.fileSystemMetadata ; }
    boolean haveEmailContent() { return this.emailContent ; }
    boolean haveEmailHeader() { return this.emailHeader ; }
    boolean haveChatContent() { return this.chatContent ; }
}
