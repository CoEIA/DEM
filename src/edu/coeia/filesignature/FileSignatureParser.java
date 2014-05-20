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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Ahmed
 */

final class FileSignatureParser {
   
    public static List<FileSignature> paserFile() throws IOException {
        BufferedReader br = null;
        List<FileSignature> files = new ArrayList<FileSignature>();
        
        try {
            br = new BufferedReader(new FileReader("tools\\Signatures.dat"));
            String line = null;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",N/A");
                String[] firstPart = values[0].split(",");
                String[] secondPart = values[1].split(",");
                List<String> extensionPart = Arrays.asList(secondPart[1].split("\\|"));

                FileSignature fs = new FileSignature(firstPart[0], firstPart[1].replaceAll("\\ ", ""), extensionPart, secondPart[2]);
                files.add(fs);
            }
        }
        finally {
            if (br != null) {
                br.close();
            }
        }

        return files;
    }
}
