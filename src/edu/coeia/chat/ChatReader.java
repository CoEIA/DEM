/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.chat;

import java.io.File;

/**
 *
 * @author wajdyessam
 */
public interface ChatReader {
    ChatSession processFile(final File path) throws Exception;
}
