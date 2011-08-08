/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.index;

/**
 *
 * @author wajdyessam
 *
 */

import edu.coeia.utility.FilesPath ;

import java.io.File ;
import java.io.FileInputStream ;
import java.io.FileOutputStream ;
import java.io.ObjectInputStream ;
import java.io.ObjectOutputStream ;
import java.io.IOException ;

public class IndexOperation {
    public static void writeIndex (IndexInformation index, File file) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(index);
        out.close();
    }

    public static IndexInformation readIndex (File file) throws IOException,ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        IndexInformation index = (IndexInformation) in.readObject();
        in.close();

        return index; 
    }

    public static void writeNewIndex (IndexInformation index) throws IOException {
        // create index folder
        File dir = new File( index.getIndexLocation());
        dir.mkdir();

        // create THE_INDEX that hold index data used by lucene engine
        File dir2 = new File( index.getIndexLocation() + "\\" + FilesPath.INDEX_PATH );
        dir2.mkdir();

        // create index information file & write the index on it
        String info = index.getIndexLocation() + "\\" + index.getIndexName() + ".DAT" ;
        File infoFile = new File(info);
        infoFile.createNewFile();
        IndexOperation.writeIndex(index, infoFile);

        // create log file
        String log = index.getIndexLocation() + "\\" + index.getIndexName() + ".LOG" ;
        new File(log).createNewFile();
    }
}
