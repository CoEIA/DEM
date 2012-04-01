/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.extractors;

import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.language.LanguageProfile;
import org.junit.Test;

/**
 *
 * @author wajdyessam
 */
public class NewEmptyJUnitTest {
    
    public NewEmptyJUnitTest() {
    }

    @Test
    public void detectTest() {
        final String text = "this is text";
        LanguageProfile profile = new LanguageProfile(text);
        LanguageIdentifier identifier = new LanguageIdentifier(profile);
        System.out.println("Language:  " + identifier.getLanguage());
    }
}
