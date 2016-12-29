import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Graph {
    private HashMap<Integer, LinkedList<Integer>> graph = new HashMap<>();
    private int nodeCount = 0;
    private int edgesCount = 0;
    private HashMap<Integer, Double> cb = new HashMap<>();
    private boolean hasGraphChanges = false;

    public int getNodeCount() {
        return nodeCount;
    }

    private int getEdgesCount() {
        return edgesCount;
    }

    private ArrayList<Integer> getNodes() {
        return new ArrayList<>(graph.keySet());
    }	
	
	/* --------------- Edges --------------- */

    private void addEdge(int v1, int v2) {
        graph.get(v1).add(v2);
        graph.get(v2).add(v1);
        edgesCount++;
        hasGraphChanges = true;
    }

    private void removeEdge(int v1, int v2) {
        graph.get(v1).remove(v2);
        graph.get(v2).remove(v1);
        hasGraphChanges = true;
    }

    public LinkedList<Integer> getEdges(int v) {
        return graph.get(v);
    }
	
	
	/* ------------- Vertices ------------- */

    private void addVertex(int v) {
        graph.put(v, new LinkedList<>());
        nodeCount++;
        hasGraphChanges = true;
    }

    private void removeVertex(int v) {
        for (int v2 : graph.get(v)) {
            graph.get(v2).remove(v);
        }
        graph.remove(v);
        nodeCount--;
        hasGraphChanges = true;
    }


    /* ---------- AUX -------- */



    private int bfs(int from, int to) {
        ArrayList<Integer> distTo = new ArrayList<>();
        ArrayList<Boolean> marked = new ArrayList<>();

        Queue<Integer> q = new LinkedList<>();

        for (int v = 0; v < getNodeCount(); v++) {
            distTo.add(v, Integer.MAX_VALUE);
            marked.add(v, false);
        }

        distTo.set(from, 0); // set
        marked.set(from, true);

        q.add(from);

        while (!q.isEmpty()) {
            int v = q.poll();
            for (int child: graph.get(v)) {
                if(!marked.get(child)) {
                    distTo.set(child, distTo.get(v) + 1); //set
                    marked.set(child, true);
                    q.add(child);
                }
            }
        }

        return distTo.get(to);
    }
	
	
	/* ---------- Graph metrics ---------- */

    private double getAveragePathLength() {
        int dist = 0, aux = 0, count = 0, max = 0;
        ArrayList<Integer> nodes = getNodes();
        for (int i = 0; i < nodeCount; i++){
            for (int j = i + 1; j < nodeCount; j++) {
                aux = nodeDistance(nodes.get(i), nodes.get(j));
                if(aux>max){
                    max=aux;
                }
                if(aux==-1){
                    count++;
                }
                else
                    dist+=aux;
            }

        }

        for(int i = 0; i<count; i++)
            dist+=max;
        
        return (double) (dist * 2) / (double) (nodeCount * (nodeCount - 1));
    }

    private void getDegreeDistribution() {
        Double[] degreeDist = new Double[nodeCount];

        for (int e = 0; e < nodeCount; e++) {
            degreeDist[e] = 0.0;
            for (int j = 0; j < nodeCount; j++)
                if (getNodeDegree(j) == e)
                    degreeDist[e]++;
        }
        for (int i = 0; i < degreeDist.length; i++)
            degreeDist[i] = degreeDist[i] / nodeCount;

        BarChartEX chart = new BarChartEX();
        chart.setdata(degreeDist);
    }

    private double getClusteringCoeficient(int node) {
        List<Integer> neighbors = new LinkedList<>();
        int connects = 0, degree;
        for (int i = 0; i < nodeCount; i++)
            if (hasConnection(i, node))
                neighbors.add(i);

        degree = neighbors.size();

        for (int i = 0; i < neighbors.size(); i++)
            for (int j = i; j < neighbors.size(); j++)
                if (i != j)
                    if (hasConnection(neighbors.get(i), neighbors.get(j)))
                        connects++;

        if (connects == 0 || degree == 0)
            return 0.0;
        return (double) (2 * connects) / (double) (degree * (degree - 1));
    }

    private boolean hasConnection(int i, int node) {
        return graph.get(i).contains(node);
    }

    private double getAvgClusteringCoeficient() {
        double res = 0;
        ArrayList<Integer> nodes = getNodes();
        for (Integer l : nodes) {
            res += getClusteringCoeficient(l);
        }
        return res / (double) (nodeCount - 1);
    }

    private void calculateBetweenessCentrality() {
        hasGraphChanges = false;
        for (Integer v : graph.keySet()) cb.put(v, 0.0);

        for (Integer s : graph.keySet()) {
            Stack<Integer> S = new Stack<>();
            HashMap<Integer, LinkedList<Integer>> P = new HashMap<>(graph.size());
            HashMap<Integer, Integer> sigma = new HashMap<>(graph.size());
            HashMap<Integer, Integer> d = new HashMap<>(graph.size());
            Queue<Integer> Q = new LinkedList<>();

            for (int t : graph.keySet()) {
                P.put(t, new LinkedList<>());
                sigma.put(t, 0);
                d.put(t, -1);
            }
            sigma.put(s, 1);
            d.put(s, 0);

            Q.add(s);
            while (!Q.isEmpty()) {
                int v = Q.poll();
                S.push(v);
                for (int w : graph.get(v)) {
                    if (d.get(w) < 0) {
                        Q.add(w);
                        d.put(w, d.get(v) + 1);
                    }
                    if (d.get(w) == d.get(v) + 1) {
                        sigma.put(w, sigma.get(w) + sigma.get(v));
                        P.get(w).add(v);
                    }
                }
            }

            HashMap<Integer, Double> delta = new HashMap<>(graph.size());
            for (int t : graph.keySet()) delta.put(t, 0.0);
            while (!S.isEmpty()) {
                int w = S.pop();
                for (int v : P.get(w)) {
                    delta.put(v, delta.get(v) + ((double) sigma.get(v) / (double) sigma.get(w)) * (1.0 + delta.get(w)));
                }
                if (w != s) {
                    cb.put(w, cb.get(w) + delta.get(w));
                }
            }
        }

    }
	
	/* ----------- Node metrics ----------- */

    private int getNodeDegree(int v) {
        return graph.get(v).size();
    }

    private int nodeDistance(int v1, int v2) {
        if (v1 == v2)
            return 0;
        
        if (graph.get(v1).contains(v2))
            return 1;

        int res=bfs(v1,v2);
        if(res==Integer.MAX_VALUE)
            return -1;

        return res;
    }

    private double getBetweenessCentrality(Integer v) {
        if (hasGraphChanges)
            calculateBetweenessCentrality();
        return cb.get(v) / 2d;
    }

    /* ------------ main ----------- */
    private void printMenu() {
        System.out.println("Options:");
        System.out.println("i - Import GML File");
        System.out.println("c - Create a Graph");
        System.out.println("r - Create a random netword with Barabási-Albert Model");
        System.out.println("exit - Close Graph");
    }
    
    private static String chooseFile(String description, String extension) {
    	JFileChooser chooser = new JFileChooser();
    	chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    	chooser.setFileFilter(new FileNameExtensionFilter(description, extension));
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
        	return chooser.getSelectedFile().getAbsolutePath();
        }
        return "-1";
    }

    private String readInput() {
    	try{
	        System.out.print(">>> ");
	        Scanner scanner = new Scanner(System.in);
	        return scanner.nextLine();
    	} catch(NoSuchElementException e){
    		try {
    			System.out.println("\n");
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    	return null;
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("Thanks for using out APP");
            }
         });
        
        Graph g = new Graph();
        String input = "";
        System.out.println("_________________________________________");
        System.out.println("|           Welcome to Graph!            |");
        System.out.println("|_______Instituto Superior Técnico_______|\n\n");
        while (!input.equals("exit")) {
            g.printMenu();
            while((input = g.readInput()).length() == 0);
            
            //Import Graph
            if (input.charAt(0) == 'i') {
            	String inputFile;
            	String[] iargs = input.split(" ");
            	
            	if(iargs.length < 2)
            		inputFile = chooseFile("GML Files", "gml");
            	else
            		inputFile = iargs[1];
            	
            	if(inputFile != "-1"){
	                try {
	                   	BufferedReader br = new BufferedReader(new FileReader(inputFile));
	                    for (int i = 0; i < 4; i++) br.readLine();
	                    String line = br.readLine().trim();
	                    String line2;
	                    while (line.charAt(0) != ']') {
	
	
	                        //processNode
	                        if (line.charAt(0) == 'n') {
	                            br.readLine();//skip [
	                            line = br.readLine().trim(); //id
	                            g.addVertex(Integer.parseInt(line.substring(3)));
	
	                            if (br.readLine().trim().charAt(0) == 'l') //skip l
	                                br.readLine();
	                            line = br.readLine().trim();
	                        }
	
	                        //process edge
	                        if (line.charAt(0) == 'e') {
	                            br.readLine();
	                            line = br.readLine().trim();
	                            line2 = br.readLine().trim();
	                            g.addEdge(Integer.parseInt(line.substring(7)), Integer.parseInt(line2.substring(7)));
	                            line = br.readLine().trim();
	                            if (line.trim().charAt(0) == 'l')
	                                br.readLine();
	                            line = br.readLine().trim();
	                        }
	                    }
	                    br.close();
	                    System.out.println("Graph Imported with success!");
	                    g.processOperations();
	
	                } catch (IOException e) {
	                    System.out.println("Invalid file");
	                }
            	}
            }


            if(input.charAt(0)=='c'){
                int count = g.getNodeCount(), val1, val2;
                System.out.println("\nOptions:\nv - Add Vertex");
                System.out.println("e <v1> <v2>- Add Edge");
                System.out.println("d - Finish Graph");
                System.out.println("exit - Exit Graph");
                while((input = g.readInput()).length() == 0);
                while(!input.equals("exit")){
                    switch (input.charAt(0)){
                        case('v'):
                            g.addVertex(count);
                            System.out.println("Created Vertex "+count++);
                            break;

                        case('e') :
                            Scanner sc = new Scanner(input.substring(1));
                            val1=sc.nextInt();
                            val2=sc.nextInt();
                            g.addEdge(val1,val2);
                            break;

                        case('d') :
                            g.processOperations();
                            break;
                    }

                    input = g.readInput();
                }
            }

            if(input.charAt(0)=='r'){
                int v1, v2;
                g.createRandomNetwork(g);
                System.out.println("Graph Created with success");
                System.out.println("#Vertices: "+g.getNodeCount()+"\n#Edges: "+g.getEdgesCount());
                System.out.println("Insert the number of <vertex> and <connections> you want to add.");
                input=g.readInput();
                Scanner sc = new Scanner(input);
                v1=sc.nextInt();
                v2=sc.nextInt();
                g.createBarabasiAlbertModel(v1,v2);
                System.out.println("It's Done!");
                g.processOperations();
            }
        }
        if(input.equals("exit")) return;
    }

    private void createRandomNetwork(Graph g){
        g.addVertex(0);
        g.addVertex(1);
        g.addVertex(2);
        g.addEdge(0,1);
        g.addEdge(0,2);
    }

    private void createBarabasiAlbertModel(int nodes, int connections){

        int newNodes,degreeValue,sumDegree;
        HashMap<Integer,ArrayList<Integer>> nodesListByDegree = new HashMap<>();
        Random rn = new Random();
        int times = connections;

        if(nodes==0) {

            newNodes =  rn.nextInt(300) + 1;
            newNodes = newNodes*times;
        }
        else
            newNodes=nodes*times;

        List<Integer> keys = new ArrayList<>();


        while(newNodes!=0) {



            sumDegree = 0;
            nodesListByDegree.clear();

            //get vertex by degree
            for (int i = 0; i < getNodeCount(); i++) {
                degreeValue = getNodeDegree(i);
                if (!nodesListByDegree.containsKey(degreeValue)) {
                    nodesListByDegree.put(degreeValue, new ArrayList<>());
                    nodesListByDegree.get(degreeValue).add(i);
                } else
                    nodesListByDegree.get(degreeValue).add(i);

                sumDegree += degreeValue;
            }




            keys.clear();
            keys.addAll(nodesListByDegree.keySet());
            Collections.sort(keys);
            addVertex(getNodeCount());
            times=connections;

            while (times!=0) {
                for (Integer key : keys) {
                    if (times == 0) break;
                    for (int node : nodesListByDegree.get(key)) {
                        if (rn.nextInt((int) (1 / ((double) key / (double) sumDegree))) == 0) {
                            if (!hasConnection(node, getNodeCount() - 1)) {
                                newNodes--;
                                addEdge(node, getNodeCount() - 1);
                                times--;
                            }
                        }
                        if (times == 0) break;
                    }
                }
            }
        }
    }
    
    private void resetGraph(){
        graph.clear();
        nodeCount = 0;
        edgesCount = 0;
        cb.clear();
        hasGraphChanges = false;
        System.out.println("Graph reseted");
    }


    private void processOperations() {
        System.out.println("\nOptions:\na - Average Path Length");
        System.out.println("b - Betweeness Centrality");
        System.out.println("c - Average Clustering coefficient");
        System.out.println("d - Degree Distribution");
        System.out.println("f - Number of Edges");
        System.out.println("n - Number of Nodes");
        System.out.println("g - Draw Graph");
        System.out.println("m <nodes> <connections> - Apply Barabási-Albert model");
        System.out.println("exit - Close Graph");
        String input = readInput();
        while (!input.equals("exit")) {
            switch (input.charAt(0)) {
                case ('a'):
                    System.out.println(getAveragePathLength());
                    break;
                case ('b'):
                    for (int node : getNodes()) {
                        System.out.println("#" + node + " - " + getBetweenessCentrality(node));
                    }
                    break;
                case ('c'):
                    System.out.println(getAvgClusteringCoeficient());
                    break;
                case ('d'):
                    getDegreeDistribution();
                    break;
                case ('g'):
                    GraphPanel.drawGraph(this);
                    break;
                case('f') :
                    System.out.println(getEdgesCount());
                    break;
                case('n'):
                    System.out.println(getNodeCount());
                    break;
                case('m'):
                	String[] args = input.split(" ");
                	int nodes = Integer.parseInt(args[1]);
                	int connections = Integer.parseInt(args[2]);
                	if(connections > 0 && connections < nodeCount){        
                		createBarabasiAlbertModel(nodes, connections);
                		System.out.println("Barabasi-Model applied to the Graph");
                	}
                	else{
                		System.out.println("Number of connections cannot be bigger than number of nodes");
                	}
                    break;
                case ('e'):
                    break;
            }

            input = readInput();
        }
        if(input.equals("exit"))
        	resetGraph();
    }
}
