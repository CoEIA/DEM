/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.constants;

/**
 *
 * @author wajdyessam
 */

public enum AuditingMessages {
    OPEN_CASE("User is Open the case"),
    CLOSE_CASE("User is closed the case"),
    NEW_CASE("User Make New Case"),
    DELETE_CASE("User Delete the Case"),
    IMPORT_CASE("User Import Case"),
    EXPORT_CASE("User Export Case");

    AuditingMessages (final String desc) {
        this.description = desc;
    }

    @Override
    public String toString() {
        return this.description;
    }

    private final String description;
}
