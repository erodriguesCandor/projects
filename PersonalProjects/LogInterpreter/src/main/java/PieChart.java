import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Rotation;

import java.util.Random;

public class PieChart extends ApplicationFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public PieChart(String title) {

        super(title);

        // create a dataset...
        final DefaultPieDataset dataset = createSampleDataset();
        gen myGen = new gen(dataset);
        new Thread(myGen).start();

        // create the chart...
        final JFreeChart chart = createChart(dataset);

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }

    /**
     * Creates a sample dataset for the demo.
     *
     * @return A sample dataset.
     */
    private DefaultPieDataset createSampleDataset() {

        final DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Java", new Double(43.2));
        result.setValue("Visual Basic", new Double(10.0));
        result.setValue("C/C++", new Double(17.5));
        result.setValue("PHP", new Double(32.5));
        result.setValue("Perl", new Double(1.0));
        return result;

    }


    private JFreeChart createChart(final PieDataset dataset) {

        final JFreeChart chart = ChartFactory.createPieChart3D(
                "PieChart",  // chart title
                dataset,                // data
                true,                   // include legend
                true,
                false
        );

        final PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        plot.setNoDataMessage("No data to display");
        return chart;

    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {

        final PieChart demo = new PieChart("PieChart");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

    static class gen implements Runnable {
        private Random randGen = new Random();
        private DefaultPieDataset data;
        public gen(DefaultPieDataset result){
            data = result;
        }

        public void run() {
            while(true) {
                int num = randGen.nextInt(1000);
                System.out.println(num);
                data.setValue("Perl", new Double(num));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

}


