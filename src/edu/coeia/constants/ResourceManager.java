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
    private Locale[] supportedLocale = { Locale.ENGLISH, new Locale("ar", "SA") };
    private Locale currentLocale;
    private ResourceBundle bundle;
    
    public enum Language { ENGLISH, ARABIC } ;
    
    public void setLanguage(Language language) {
        if ( language == Language.ARABIC ) {
            currentLocale = supportedLocale[1];
        }
        else {
            currentLocale = supportedLocale[0];
        }
    }
    
    public String getText(final String key) {
        this.bundle = ResourceBundle.getBundle("dem_messages", currentLocale);
        return this.bundle.getString(key);
    }
}
