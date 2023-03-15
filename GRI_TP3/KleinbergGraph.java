import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
// import java.util.ArrayList;
import java.util.HashMap;
// import java.util.LinkedList;
import java.util.Map;
// import java.util.Queue;
// import java.util.Stack;
import java.awt.Point;


public class KleinbergGraph {
    private ArrayList<Node> nodeList; // list of nodes
    Map<Integer, ArrayList<Integer>> adjList; // adjacency list ID
    private int n;

    KleinbergGraph(String output, String c){
        int id = 0; // node id from 0 to nbNode^2 excluded
        this.nodeList = new ArrayList<Node>();
        this.adjList = new HashMap<Integer, ArrayList<Integer>>();
        try{
            this.n = Integer.parseInt(c);
        } catch (NumberFormatException e){
            System.err.println("c value is not a number");
        }

        // Create all nodes
        for (int x = 0; x < this.n; x++){
            for (int y = 0; y < this.n; y++){
                this.nodeList.add(new Node(id, x, y));
                id++;
            }
        }

        // Create adjList
        for (Node node : nodeList) {
            id = node.getID();
            ArrayList<Integer> neighbors = new ArrayList<>(); // Neighbors of the current node
    
            int x = id / n;
            int y = id % n;
    
            // Compute the ids of the neighbors
            // left neighbor
            if (x > 0) { 
                neighbors.add(id - 1);
            }
            
            // right neighbor
            if (x < n-1) {
                neighbors.add(id + 1);
            }

            // top neighbor
            if (y > 0) {
                neighbors.add(id - n);
            }

            //bottom neighbor
            if (y < n-1) {
                neighbors.add(id + n);
            }
    
            adjList.put(id, neighbors);
        }

        // Compute long edge (A,B)
        for (Node nodeA : nodeList) {
            double threshold = Math.random(); // threshold value to select nodeB
            double probaSum = 0; // sum of nodeB proba
            // Compute S coefficient
            double s = 0.0;
            for (Node nodeB : nodeList) {
                if (nodeA != nodeB && !adjList.get(nodeA.getID()).contains(nodeB.getID())) { // nodeB is not nodeA and is not its neighbor already
                    double distance = distance(nodeA, nodeB);
                    s += 1.0 / Math.pow(distance, 2);
                }
            }

            // for each nodeB different of nodeA and not one of its neighbors
            for (Node nodeB : nodeList) {
                if (nodeA != nodeB && !adjList.get(nodeA.getID()).contains(nodeB.getID())) {
                    double distance = distance(nodeA, nodeB);
                    probaSum += 1.0 / (s * Math.pow(distance, 2));

                    // nodeB selected for the long edge
                    if (probaSum > threshold) {
                        // add the node ids to the adjList
                        int idA = nodeA.getID();
                        int idB = nodeB.getID();
                        adjList.get(idA).add(idB);
                        adjList.get(idB).add(idA);
                        break; // go to next nodeA
                    }
                }
            }
        }

        try{
            BufferedWriter writerTxt = new BufferedWriter(new FileWriter(output+".txt")); // writer for .txt file
            BufferedWriter writerDot = new BufferedWriter(new FileWriter(output+".dot")); // writer for .dot file
        } catch(IOException e){
            System.err.println("Error while writing in file");
            e.printStackTrace();
        }

    }

    // Compute euclidean distance between two nodes
    private double distance(Node a, Node b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return Math.sqrt(dx*dx + dy*dy);
    }
}
