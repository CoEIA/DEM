/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coeia.main;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.JPanel ;

import java.text.ParseException ;
import java.text.SimpleDateFormat ;
import java.text.DateFormat ;

import java.util.ArrayList ;
import java.util.Date ;
import java.util.HashMap ;
import java.util.Map ;

import edu.coeia.email.Message ;

/**
 *
 * @author wajdyessam
 */

public class LineChartPanel {
    
    public static JPanel getLineChartPanel (String userName, String otherName,
            ArrayList<Message> data, ArrayList<Message> data2) throws ParseException {

        CategoryDataset categoryDataset = createCategoryDataset(userName, otherName, data, data2);

        JFreeChart chart = createChart(categoryDataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setSize(400, 400);

        return chartPanel;
    }

    private static CategoryDataset createCategoryDataset(String userName, String otherName,
            ArrayList<Message> data, ArrayList<Message> data2) throws ParseException{
        
        // row keys...
        final String series1 = userName;
        final String series2 = otherName ;

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

  
        HashMap<String,Integer> sentMap = getDateMap(data,userName,otherName);

        for (Map.Entry<String,Integer> mapEntry: sentMap.entrySet()) {
            dataset.addValue( mapEntry.getValue(), series2, mapEntry.getKey());
        }

        HashMap<String,Integer> recMap = getDateMap(data2,otherName,userName);

        for (Map.Entry<String,Integer> mapEntry: recMap.entrySet()) {
            dataset.addValue( mapEntry.getValue(), series1, mapEntry.getKey());
        }

        return dataset;
    }

    private static HashMap<String,Integer> getDateMap (ArrayList<Message> data, String first, String second) throws ParseException {
        HashMap<String,Integer> map = new HashMap<String,Integer>();

        for (Message msg: data) {
            if ( msg.getReceiverName().equalsIgnoreCase(first) && msg.getSenderName().equalsIgnoreCase(second)) {
                if ( map.get(getMonthName(msg.getDate())) == null ) {
                    map.put(getMonthName(msg.getDate()), 1);
                }
                else {
                    map.put(getMonthName(msg.getDate()), map.get(getMonthName(msg.getDate())) + 1 );
                }
            }
        }

        return (map);
    }

    private static String getMonthName (String date) throws ParseException {
        DateFormat fullDate = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat month = new SimpleDateFormat("MMMM");
        Date d = fullDate.parse(date);

        String m = month.format(d) ;
        return (m);
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  a dataset.
     *
     * @return The chart.
     */
    private static JFreeChart createChart(final CategoryDataset dataset) {

        // create the chart...
        final JFreeChart chart = ChartFactory.createLineChart(
            "Frequency of Messages",       // chart title
            "Time of Messages",            // domain axis label
            "Number of Messages",          // range axis label
            dataset,                   // data
            PlotOrientation.VERTICAL,  // orientation
            true,                      // include legend
            true,                      // tooltips
            false                      // urls
        );

        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);

        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);


        // customise the renderer...
        final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesStroke(
            0, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {10.0f, 6.0f}, 0.0f
            )
        );

        renderer.setSeriesStroke(
            1, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {6.0f, 6.0f}, 0.0f
            )
        );

        renderer.setSeriesStroke(
            2, new BasicStroke(
                2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[] {2.0f, 6.0f}, 0.0f
            )
        );

        return chart;
    }
}
