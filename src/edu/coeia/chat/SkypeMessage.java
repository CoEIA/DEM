/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.chat;

/**
 *
 * @author wajdyessam
 *
 */

public class SkypeMessage extends ChatMessage {
    public SkypeMessage (final String from, final String to,
            final String date, final String message) {
        super(from, to, date, message);
    }
}
