import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GraphPanel extends JPanel {

    private int width = 800;
    private int heigth = 400;
    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(44, 102, 230, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 4;
    private int numberYDivisions = 10;
    private List<Double> scores;
    private Graph graph;

    public GraphPanel(List<Double> scores, Graph graph) {
        this.scores = scores;
	this.graph = graph;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (scores.size() - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

        // create points
        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < graph.getNodeCount(); i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - scores.get(i)) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        Stroke oldStroke = g2.getStroke();

        // draw lines
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            for (Integer j : graph.getEdges(i)) {
            	int x2 = graphPoints.get(j).x;
            	int y2 = graphPoints.get(j).y;
            	g2.drawLine(x1, y1, x2, y2);
            }
        }

	// draw points
        g2.setStroke(oldStroke);
        for (int i = 0; i < graphPoints.size(); i++) {
	    g2.setColor(pointColor);
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);

	    g2.setColor(Color.BLACK);
	    String nodeLabel = i + "";
	    g2.drawString(nodeLabel, x, y);
        }
    }


    private double getMinScore() {
        double minScore = Double.MAX_VALUE;
        for (Double score : scores) {
            minScore = Math.min(minScore, score);
        }
        return minScore;
    }

    private double getMaxScore() {
        double maxScore = Double.MIN_VALUE;
        for (Double score : scores) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }
    
    private static void createAndShowGui(Graph graph) {
        List<Double> scores = new ArrayList<>();

        Random random = new Random();
        for (int i = 0; i < graph.getNodeCount(); i++) {
            scores.add((double) random.nextDouble() * 10);
        }

	GraphPanel mainPanel = new GraphPanel(scores, graph);
        mainPanel.setPreferredSize(new Dimension(800, 600));
        JFrame frame = new JFrame("GraphPanel");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void drawGraph(Graph graph) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGui(graph);
         }
      });
   }

}
