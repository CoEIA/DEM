/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.filesystem.clustering;

/**
 *
 * @author wajdyessam
 */

import edu.coeia.main.util.Tuple;

import java.util.Collection;
import java.util.ArrayList ;

import org.carrot2.core.ProcessingResult;
import org.carrot2.core.Cluster;
import org.carrot2.core.Document;

public class DisplayClusterData {

    public static ArrayList<Tuple<String, ArrayList<String>>> getResult (ProcessingResult processingResult) {
        final Collection<Cluster> clusters = processingResult.getClusters();
        final ArrayList<Tuple<String, ArrayList<String>>> result = new ArrayList<Tuple<String, ArrayList<String>>>();
        
        for (final Cluster cluster: clusters){
            Tuple<String,ArrayList<String>> oneCluster = new Tuple<String,ArrayList<String>>();
            ArrayList<String> filesPath = new ArrayList<String>();
            
            String name = cluster.getLabel() + " (" + cluster.getAllDocuments().size() + ") " ;
            oneCluster.setA(name);
            
            for (Document doc: cluster.getAllDocuments()) {
                filesPath.add( (String) doc.getField(Document.TITLE));
//                System.out.println("doc title: " + doc.getField(Document.TITLE));
//                System.out.println("doc url: " + doc.getField(Document.CONTENT_URL));
//                System.out.println("doc size: " + doc.getField(Document.SIZE));
//                System.out.println("doc source: " + doc.getField(Document.SOURCES));
//                System.out.println("doc click: " + doc.getField(Document.CLICK_URL));
//                System.out.println("doc language: " + doc.getField(Document.LANGUAGE));
//                System.out.println("doc partition: " + doc.getField(Document.PARTITIONS));
//                System.out.println("doc sumary: " + doc.getField(Document.SUMMARY));
//                System.out.println("doc turl: " + doc.getField(Document.THUMBNAIL_URL));
            }

            oneCluster.setB(filesPath);

            result.add(oneCluster);
        }

        return result;
    }
}
