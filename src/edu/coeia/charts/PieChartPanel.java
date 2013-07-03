/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.charts;

/**
 *
 * @author wajdyessam
 */

import java.io.IOException ;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

public class PieChartPanel {

    public static JPanel newInstance (final Map<String,Double> map, String title,
            int factor) throws IOException {
        final PieDataset dataset = createSampleDataset(map, factor);
        final JFreeChart chart = createChart(dataset,title);
        final ChartPanel chartPanel = new ChartPanel(chart);
        
        chartPanel.setSize(400, 400);
        return chartPanel ;
    }
    
    private static PieDataset createSampleDataset(final Map<String,Double> map, int factor) {
        final DefaultPieDataset result = new DefaultPieDataset();

        Set set = map.entrySet();
        Iterator itr = set.iterator();

        while ( itr.hasNext() ) {
            Map.Entry me = (Map.Entry) itr.next();

            String str = me.getValue().toString();
            double counts = Double.valueOf(str).doubleValue();
            
            if ( counts < factor)
                continue;
            
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
