/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    
    public static String getText(final String key) {
        bundle = ResourceBundle.getBundle("dem_messages", currentLocale);
        return bundle.getString(key);
    }
}
