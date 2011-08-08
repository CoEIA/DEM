/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.gui;

/**
 *
 * @author wajdyessam
 */


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator ;
import org.jfree.util.Rotation;

import javax.swing.JPanel ;

import java.util.HashMap ;
import java.util.Set ;
import java.util.Iterator ;
import java.util.Map ;

import java.io.IOException ;

public class PieChartPanel {
    private static HashMap<String,Double> map ;

    public static JPanel getPieChartPanel (HashMap<String,Double> map2, String title) throws IOException {
        map = map2;
        
         // create a dataset...
        final PieDataset dataset = createSampleDataset();

        // create the chart...
        final JFreeChart chart = createChart(dataset,title);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setSize(400, 400);

        return chartPanel ;
    }
    
    private static PieDataset createSampleDataset() {
        final DefaultPieDataset result = new DefaultPieDataset();

        Set set = map.entrySet();
        Iterator itr = set.iterator();

        while ( itr.hasNext() ) {
            Map.Entry me = (Map.Entry) itr.next();

            String str = me.getValue().toString();
            double counts = Double.valueOf(str).doubleValue();
            double per = (counts/map.size()) * 100 ;

            result.setValue((String)me.getKey(),per);
        }

        return result;
    }

    private static JFreeChart createChart(final PieDataset dataset, String str) {

        final JFreeChart chart = ChartFactory.createPieChart3D(
            str,                    // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false
        );

        final PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        StandardPieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0} {2}");
        plot.setLabelGenerator(labelGenerator);
        plot.setNoDataMessage("No data to display");
        return chart;
    }
}
