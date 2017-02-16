import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;

public class LineChart {
    private TimeSeries ts = new TimeSeries("data", Millisecond.class);


    public LineChart(String title, String x, String y) throws InterruptedException {

        TimeSeriesCollection dataset = new TimeSeriesCollection(ts);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title,
                x,
                y,
                dataset,
                true,
                true,
                false
        );
        final XYPlot plot = chart.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChartPanel label = new ChartPanel(chart);
        frame.getContentPane().add(label);
        //Suppose I add combo boxes and buttons here later

        frame.pack();
        frame.setVisible(true);
    }

    public void addElement(double value) {
        ts.addOrUpdate(new Millisecond(), value);
    }

}