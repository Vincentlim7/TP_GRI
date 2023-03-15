import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    private int n, x1, y1, x2, y2;

    KleinbergGraph(String output, String c, String origine_x, String origine_y, String cible_x, String cible_y){
        int id = 0; // node id from 0 to nbNode^2 excluded
        this.nodeList = new ArrayList<Node>();
        this.adjList = new HashMap<Integer, ArrayList<Integer>>();
        try{
            this.n = Integer.parseInt(c);
            this.x1 = Integer.parseInt(origine_x);
            this.y1 = Integer.parseInt(origine_y);
            this.x2 = Integer.parseInt(cible_x);
            this.y2= Integer.parseInt(cible_y);
        } catch (NumberFormatException e){
            System.err.println("grid size or one of the nodes' coordinates value is not a number");
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
            // top neighbor
            if (x > 0) { 
                neighbors.add(id - n);
            }
            
            // bottom neighbor
            if (x < n-1) {
                neighbors.add(id + n);
            }

            // left neighbor
            if (y > 0) {
                neighbors.add(id - 1);
            }

            // right neighbor
            if (y < n-1) {
                neighbors.add(id + 1);
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
            writerDot.write("graph Kleinberg {\n");
            for (Node node : nodeList) {
                writerDot.write("\t" + node.getID() + " [ label=\"("+ node.getX() + "," + node.getY() + ")\"" + " ];" + "\n");
            }

            for (int i : adjList.keySet()) {
                ArrayList<Integer> successors = adjList.get(i);
                Collections.sort(successors); // Trie la liste de successeurs
                for (int j : successors) {
                    writerTxt.write(i + "\t" + j +"\n");
                    if(i<j){
                        writerDot.write("\t" + i + " -- " + j + " ;" + "\n");
                    }
                    
                }
            }
            writerDot.write("}");
            writerTxt.close();
            writerDot.close();
        } catch(IOException e){
            System.err.println("Error while writing in file");
            e.printStackTrace();
        }
        this.computePath(x1, y1, x2, y2);

    }

    // Compute euclidean distance between two nodes
    private double distance(Node a, Node b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return Math.sqrt(dx*dx + dy*dy);
    }

    public void computePath(int x1, int y1, int x2, int y2) {
        Node currentNode;
        Node nextNode;
        Node nodeA = this.nodeList.get(x1*this.n + y1);
        Node nodeB = this.nodeList.get(x2*this.n + y2);
        // System.out.println("nodeA : " + nodeA.getID());
        // System.out.println("nodeB : " + nodeB.getID());
        
        ArrayList<Node> path = new ArrayList<>();
        path.add(nodeA);
    
        do {
            // get last node in path
            currentNode = path.get(path.size() - 1);

            // find the closest neighbor to the target node
            double minDistance = Double.POSITIVE_INFINITY;
            nextNode = null;
            for (int neighborId : this.adjList.get(currentNode.getID())) {
                Node neighbor = this.nodeList.get(neighborId);
                double distance = distance(neighbor, nodeB);
                if (distance < minDistance) {
                    minDistance = distance;
                    nextNode = neighbor;
                }
                
            }
    
            if (nextNode == null) {
                System.out.println("Should not happen : No next node found");
                return;
            }
            path.add(nextNode);
        } while(nextNode.getID() != nodeB.getID());
    
        // print path
        StringBuilder sb = new StringBuilder();
        for (Node node : path) {
            sb.append(node.getID());
            sb.append(" (");
            sb.append(node.getX());
            sb.append(",");
            sb.append(node.getY());
            sb.append(") ; ");
        }
        System.out.println(sb);
        System.out.println("longueur chemin : " + (path.size()-1));
    }

}
