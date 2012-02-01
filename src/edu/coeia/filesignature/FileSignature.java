/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.filesignature;

import java.util.List;

/**
 *
 * @author Ahmed
 */

final class FileSignature {
    private final String id;
    private final String signature;
    private final List<String> extension;
    private final String type;

    public FileSignature(final String Id, final String Signature,
            final List<String> Extension, final String Type) {

        this.id = Id;
        this.signature = Signature;
        this.extension = Extension;
        this.type = Type;
    }

    public String getID() {
        return this.id;
    }

    public String getSignature() {
        return this.signature;
    }

    public List<String> getExtension() {
        return this.extension;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return id + "\n" + signature + "\n" + extension + "\n" + type + "\n";
    }
}
