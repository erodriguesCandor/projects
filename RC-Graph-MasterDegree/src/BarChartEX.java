import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class BarChartEX extends Application {

    private static Double[] info;

    public void setdata(Double[] data){
        info=data;
        launch();
    }
 
    @Override public void start(Stage stage) {
        stage.setTitle("Degree Distribution");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = 
            new BarChart<>(xAxis,yAxis);
        bc.setTitle("DegreeDistribution");
        xAxis.setLabel("DegreeDistribution");
        yAxis.setLabel("DegreeDistribution");
 
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Degree");
        for(int i = 0; i< info.length;i++){
        	series1.getData().add(new XYChart.Data("D"+i,info[i]));
        }
               
        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(series1);
        stage.setScene(scene);
        stage.show();
    }
}
