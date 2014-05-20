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
package edu.coeia.util;

/**
 *
 * @author wajdyessam
 */

import java.io.File ;
import javax.swing.filechooser.FileFilter;

public class GUIFileFilter extends FileFilter {

    private String[] ext;
    private String desc ;

    public GUIFileFilter (String desc, String ... ext ) {
        this.ext = ext ;
        this.desc = desc ;
    }

    public boolean accept(File file) {
        for (String ex: ext)
            if ( file.getName().endsWith(ex) )
                return true;

        return false ;
    }

    public String getDescription() {
        return desc ;
    }
}
