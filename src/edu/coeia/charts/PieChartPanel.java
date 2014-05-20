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
