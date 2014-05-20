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
