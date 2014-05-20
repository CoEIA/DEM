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
package edu.coeia.searching;

/**
 * Builder Pattern
 * @author wajdyessam
 */

public final class SearchScope {
    
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
