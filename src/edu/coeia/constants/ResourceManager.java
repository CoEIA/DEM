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

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author wajdyessam
 */
public final class ResourceManager {
    private static Locale[] supportedLocale = { Locale.ENGLISH, new Locale("ar", "SA") };
    private static Locale currentLocale;
    private static ResourceBundle bundle;
    
    public static enum Language { ENGLISH, ARABIC } ;
    
    public static void setLanguage(Language language) {
        if ( language == Language.ARABIC ) {
            currentLocale = supportedLocale[1];
        }
        else {
            currentLocale = supportedLocale[0];
        }
    }
    
    public static Locale getLanguage() {
        return currentLocale;
    }
    
    public static String getText(final String bundlePath, final String key) {
        bundle = ResourceBundle.getBundle(bundlePath, currentLocale);
        return bundle.getString(key);
    }
        
    private ResourceManager() {
        throw new AssertionError("Cannot create object from this class");
    }
}
