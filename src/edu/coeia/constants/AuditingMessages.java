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
package edu.coeia.constants;

/**
 * Containing Auditing Message that will 
 * record the interactions with the application
 * 
 * @author wajdyessam
 */

public enum AuditingMessages {
    OPEN_CASE("User is Open the case"),
    CLOSE_CASE("User is closed the case"),
    NEW_CASE("User Make New Case"),
    DELETE_CASE("User Delete the Case"),
    IMPORT_CASE("User Import Case"),
    EXPORT_CASE("User Export Case"),
    
    SAVE_TAGS("User Save Tags"),
    
    REFRESHING_CHAT("User press chat refresh button"),
    LOADING_CHAT("User load chat sessions to view");
    
    AuditingMessages (final String desc) {
        this.description = desc;
    }

    @Override
    public String toString() {
        return this.description;
    }

    private final String description;
}
