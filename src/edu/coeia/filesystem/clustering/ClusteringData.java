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

import java.io.File;
import java.io.IOException;

import java.util.Arrays;
import java.util.Map ;
import java.util.HashMap ;
import java.util.ArrayList;

import org.apache.lucene.store.FSDirectory;

import org.carrot2.core.Controller ;
import org.carrot2.core.ControllerFactory ;
import org.carrot2.core.ProcessingComponentConfiguration ;
import org.carrot2.core.ProcessingResult ;
import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.core.attribute.AttributeNames;
import org.carrot2.source.lucene.LuceneDocumentSource;
import org.carrot2.source.lucene.SimpleFieldMapper;
import org.carrot2.util.attribute.AttributeUtils;

public class ClusteringData {
    
    public static ArrayList<Tuple<String, ArrayList<String>>> clustetringData( File indexPath, String query,
            String titleFieldName, String contentFieldName ) throws IOException {
        final Controller controller = ControllerFactory.createPooling();
        final Map<String, Object> luceneGlobalAttributes = new HashMap<String, Object>();

        luceneGlobalAttributes.put(AttributeUtils.getKey(LuceneDocumentSource.class,
            "directory"), FSDirectory.open(indexPath));
        luceneGlobalAttributes.put(AttributeUtils.getKey(SimpleFieldMapper.class,
            "titleField"), titleFieldName);
        luceneGlobalAttributes.put(AttributeUtils.getKey(SimpleFieldMapper.class,
            "contentField"), contentFieldName);
        luceneGlobalAttributes.put(AttributeUtils.getKey(SimpleFieldMapper.class,
            "searchFields"), Arrays.asList(new String [] {titleFieldName, contentFieldName}));

        controller.init(new HashMap<String, Object>(),
            new ProcessingComponentConfiguration(LuceneDocumentSource.class, "lucene",
                luceneGlobalAttributes));
        
        final Map<String, Object> processingAttributes = new HashMap<String, Object>();
        processingAttributes.put(AttributeNames.QUERY, query);
        ProcessingResult process = controller.process(processingAttributes, "lucene",
            LingoClusteringAlgorithm.class.getName());

        return DisplayClusterData.getResult(process);
    }
}
