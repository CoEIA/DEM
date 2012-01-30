/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.gutil;

import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author wajdyessam
 */
public class JListUtil {
    // TODO: General List Model Methods, Move it to Utilties
    public static void AddFromModelToList(DefaultListModel model, List<String> list) {
        for (int i = 0; i < model.size(); i++) {
            list.add((String) model.getElementAt(i));
        }
    }

    public static void addToList(String path, DefaultListModel model, JList list) {
        if ((path != null || !path.startsWith("null")) && !existsInModel(path, model)) {
            model.addElement(path);
            list.setModel(model);
        }
    }

    public static boolean existsInModel(String path, DefaultListModel model) {
        return model.contains(path);
    }

    public static void removeFromList(String path, DefaultListModel model, JList list) {
        if (path != null) {
            model.removeElement(path);
            list.setModel(model);
        }
    }
}
