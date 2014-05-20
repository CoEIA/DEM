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
