/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.gutil;

/**
 *
 * @author wajdyessam
 */

import javax.swing.JLabel ;
import javax.swing.ImageIcon ;

public class ImageLabel extends JLabel {
    private String path ;

    public ImageLabel (String path, ImageIcon icon) {
        super(icon);

        this.path = path ;
    }

    public String getPath () { return path ; }
}
