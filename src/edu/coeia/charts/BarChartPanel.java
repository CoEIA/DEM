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
import java.util.Map;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.CategoryLabelWidthType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.text.TextBlockAnchor;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

public class BarChartPanel {
    private Map<String,Double> map ;
    private String indexLocation ;
    private String indexName ;
    private int from, to;
    
    public BarChartPanel (String indexLocation, String indexName, int from, int to) {
        this.indexLocation = indexLocation ;
        this.indexName = indexName ;
        this.from = from;
        this.to = to;
    }

    public JPanel getBarChartPanel (Map<String,Double> map2) throws IOException {
        map = map2;

        // create the chart...
        final DefaultCategoryDataset dataset = createSampleDataset();    
        final JFreeChart chart = createChart(dataset, this.indexName + " " + this.indexLocation + " Visualization");

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

        map2= null;
        return chartPanel;
    }

    private JFreeChart createChart(final CategoryDataset dataset, String str) {
        final JFreeChart chart = ChartFactory.createBarChart3D(
            str,                            // chart title
            "Names",                        // domain axis label
            "Values(%)",                       // range axis label
            dataset,                        // data
            PlotOrientation.HORIZONTAL,     // orientation
            true,                           // include legend
            true,                           // tooltips
            false                           // urls
        );

        
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setForegroundAlpha(1.0f);

        // left align the category labels...
        final CategoryAxis axis = plot.getDomainAxis();
        final CategoryLabelPositions p = axis.getCategoryLabelPositions();

        final CategoryLabelPosition left = new CategoryLabelPosition(
            RectangleAnchor.LEFT, TextBlockAnchor.CENTER_LEFT,
            TextAnchor.CENTER_LEFT, 0.0,
            CategoryLabelWidthType.RANGE, 0.30f
        );
        axis.setCategoryLabelPositions(CategoryLabelPositions.replaceLeftPosition(p, left));

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(from, to);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;
    }

    private DefaultCategoryDataset createSampleDataset() {
        final DefaultCategoryDataset result = new DefaultCategoryDataset();

        double total = getTotal();
        
        for (Map.Entry<String,Double> aMap: map.entrySet()){

            String str = aMap.getValue().toString();
            double counts = Double.valueOf(str).doubleValue();
            double percentage = getPercentage(counts, total);
            
            result.addValue(percentage, "Number of Messages (%)", aMap.getKey());
        }

        return result;
    }

    private double getTotal () {
        double total = 0 ;

        for (Map.Entry<String,Double> aMap: map.entrySet()){
            total += aMap.getValue();
        }

        return total;
    }

    private double getPercentage (double value, double total) {
        double percentage = 0;

        percentage = value/total * 100 ;

        return percentage;
    }

    public void releaseMemory () {
        map.clear();
        map = null ;
    }
}
