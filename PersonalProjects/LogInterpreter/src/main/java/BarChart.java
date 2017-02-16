import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.util.Random;

public class BarChart extends ApplicationFrame
{
    public BarChart(String applicationTitle , String chartTitle )
    {
        super( applicationTitle );
        DefaultCategoryDataset dataset = createDataset();
        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "Category",
                "Score",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        gen myGen = new gen(dataset);
        new Thread(myGen).start();

        ChartPanel chartPanel = new ChartPanel( barChart );
        chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );
    }
    private DefaultCategoryDataset createDataset( )
    {
        final String fiat = "FIAT";
        final String audi = "AUDI";
        final String ford = "FORD";
        final String speed = "Speed";

        final DefaultCategoryDataset dataset =
                new DefaultCategoryDataset( );

        dataset.addValue( 1.0 , fiat , speed );
        dataset.addValue( 5.0 , audi , speed );
        dataset.addValue( 4.0 , ford , speed );

        return dataset;
    }
    public static void main( String[ ] args )
    {
        BarChart chart = new BarChart("BarChart", "Char");
                chart.pack( );
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );
    }

    static class gen implements Runnable {
        private Random randGen = new Random();
        private DefaultCategoryDataset data;
        public gen(DefaultCategoryDataset result){
            data = result;
        }

        public void run() {
            while(true) {
                int num = randGen.nextInt(30);
                int num2 = randGen.nextInt(30);
                int num3 = randGen.nextInt(30);
                System.out.println(num);
                final String fiat = "FIAT";
                final String audi = "AUDI";
                final String ford = "FORD";
                final String speed = "Speed";
                data.setValue( num , fiat , speed );
                data.setValue( num2 , audi , speed );
                data.setValue( num3 , ford , speed );
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            }
        }
    }
}