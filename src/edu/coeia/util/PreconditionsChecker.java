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
 * Utility class for common argument validations
 * 
 * @author wajdyessam
 */

public final class PreconditionsChecker {
    
    /**
     * prevent making object from this class
     */
    private PreconditionsChecker() {
        // prevent even from reflection code
        throw new AssertionError();
    }
    
    /**
     * if object is null then throws NullPointerException
     */
    public static <T> T checkNull(String message, T object) {
        if ( object == null ) {
            throw new NullPointerException(message);
        }
        
        return object;
    }
    
    /*
     * if string must be not-empty string
     * if empty string then throws IllegalArgumentException
     */
    public static String checkNotEmptyString(String message, String object) {
        if ( object.isEmpty() ) {
            throw new IllegalArgumentException(message);
        }
        
        return object;
    }
}
