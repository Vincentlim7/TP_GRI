import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WattsStrogatzGraph{
    private ArrayList<Node> nodeList; // list of nodes

    Map<Integer, ArrayList<Integer>> adjList; // adjacency list ID
    Map<Integer, ArrayList<Integer>> RebrancheList; // Rebranche list ID
    int n; // nb_nodes
    int k; 
    double p; 
    int origine;
    int cible;
    ArrayList<Integer> routage;
    boolean flag = false;

    
    public WattsStrogatzGraph(String output, String n_, String k_, String p_, String ori, String cib){
        this.routage = new ArrayList<>();
        this.nodeList = new ArrayList<Node>(); 
        this.adjList = new HashMap<Integer, ArrayList<Integer>>();
        this.RebrancheList = new HashMap<Integer, ArrayList<Integer>>();
        try{
            this.n = Integer.parseInt(n_);
            this.k = Integer.parseInt(k_);
            this.p = Double.parseDouble(p_);
            this.origine = Integer.parseInt(ori);
            this.cible= Integer.parseInt(cib);
        } catch (NumberFormatException e){
            System.err.println("grid size or one of the nodes' coordinates value is not a number");
        }

        // Create all nodes
        int id = 0; 
        for (int x = 0; x < this.n; x++){
            this.nodeList.add(new Node(id));
            id++;
            RebrancheList.putIfAbsent(x, new ArrayList<>());
        }
        
        int voisin;
        for (int i = 0; i < n; i++) {
            ArrayList<Integer> neighbors = new ArrayList<>();
            for(int voisinSup=0; voisinSup<k; voisinSup++){
                voisin = i+voisinSup+1;
                if(voisin >= n){
                    neighbors.add(voisin%n);
                }
                else{                
                    neighbors.add(voisin);
                }
            }

            for(int voisinInf=0; voisinInf<k; voisinInf++){
                voisin = i-voisinInf-1;
                if(voisin < 0){
                    neighbors.add(voisin%n+n);
                }
                else{                
                    neighbors.add(voisin);
                }
            }
            adjList.put(i, neighbors);
        }
        //System.out.println(adjList);
        rebranchement();
        greedyRouting();
        createTxt(output);
        createDot(output);


    }


    public void rebranchement() {
        Random random = new Random();
        double a;
        int z;
        int voisinOri;
    
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < adjList.get(x).size(); y++) {
                voisinOri = adjList.get(x).get(y);
                a = random.nextDouble();
                if (a < p && voisinOri > x) {
                    z = random.nextInt(n);
                    while (!checkVoisin(x, y, z)) {
                        z = random.nextInt(n);
                    }
                    adjList.get(x).remove(Integer.valueOf(voisinOri));
                    adjList.get(x).add(y, z);
                    adjList.get(voisinOri).remove(Integer.valueOf(x));
                    adjList.get(z).add(x);
                    RebrancheList.get(x).add(z);
                }
            }
        }
    }


    public boolean checkVoisin(int x, int y, int z) {
        // Ensure no self-loop on x
        if (z == x) {
            return false;
        }
    
        // Check if new edge (x,z) forms a cycle with existing edges (x,y) and (y,z)
        for (int neighborId : adjList.get(x)) {
            if (neighborId == z && y != z) {
                return false;
            }
            if (adjList.get(z).contains(x)) {
                return false;
            }
        }
    
        // Check if adding edge (x,z) will disconnect the graph
        if (adjList.get(x).contains(z) || adjList.get(z).contains(x)) {
            return false;
        }
    
        // If all checks pass, return true to indicate edge can be added
        return true;
    }

    

    public void greedyRouting() {
        int dist;
        int minDist = Integer.MAX_VALUE;
        int current = origine;
    
        routage.add(current);
        nodeList.get(current).setVisited(true);
    
        while (current!=cible) {

            for(int v : adjList.get(current)){
                if(!nodeList.get(v).isVisited()){
                    dist = Math.min(Math.abs(v-cible), Math.abs(v+n-cible));
                    if (minDist > dist) {
                        minDist = dist;
                        current = v;
                        nodeList.get(v).setVisited(true);
                    }
                }
            }
            
            if (routage.contains(current)) {
                flag = true;
                break;
            }
            routage.add(current);
        }
    
        for(int i=0; i<routage.size(); i++){
            System.out.print(routage.get(i)+" ");
        }
        if (!flag) System.out.println("\nlongueur chemin : " + (routage.size()-1));
    
        else System.out.println("\nGlouton coincé, échec !"); 
    }

    public void createTxt(String output) {
        try {            
            FileWriter txt = new FileWriter(output + ".txt");
            txt.write("# " + output + ".txt\n");
            txt.write("# Nodes: "+ n + " Edges: " + (k*n)+ "\n");
            txt.write("# FromNodeId  ToNodeId\n");
            for (Node node : nodeList) {
                int nodeId = node.getID();
                // System.out.println(nodeId +" "+adjList.get(nodeId));
                for (int neighborId : adjList.get(nodeId)) {
                    if (nodeId < neighborId) {
                        txt.write(nodeId + " " + neighborId + "\n");
                    }
                }
            }
            txt.close();
        } catch (IOException e) {
            System.out.println("ERREUR output file ");
            e.printStackTrace();
        }
    }



    public void createDot(String output) {
        int penwidth = 10;
    
        try {            
            FileWriter dot = new FileWriter(output + ".dot");
    
            dot.write("graph " + output + " {\n");
            dot.write("layout=\"circo\";\n");
    
            for (Node node : nodeList) {
                int nodeId = node.getID();
                if (routage.contains(nodeId)) {
                    if (nodeId == origine || nodeId == cible) {
                        dot.write(nodeId + " [shape = triangle,style=filled,color=\"red\"] ;\n");
                    } else {
                        dot.write(nodeId + " [shape = circle, style=filled,color=\"red\"] ;\n");
                    }
                } else {
                    dot.write(nodeId + " ;\n");
                }
            }
            dot.write(cible + " [shape = triangle,style=filled,color=\"red\"] ;\n");
    
            for (Node node : nodeList) {
                int nodeId = node.getID();
                for (int neighborId : adjList.get(nodeId)) {
                    if (nodeId < neighborId) {
                        String edgeStyle = "";
                        // orange : routage if Rebranche
                        if (routage.contains(nodeId) && routage.contains(neighborId) &&
                                (RebrancheList.get(nodeId).contains(neighborId) || RebrancheList.get(neighborId).contains(nodeId))) {
                            edgeStyle = " [color=orange,penwidth=" + penwidth + "]";
                        }
                        // red : routage 
                        else if (routage.contains(nodeId) && routage.contains(neighborId)) {
                            edgeStyle = " [color=red,penwidth=" + penwidth + "]";
                        }
                        // green : Rebranche
                        else if (RebrancheList.get(nodeId).contains(neighborId) || RebrancheList.get(neighborId).contains(nodeId)) {
                            edgeStyle = " [color=green]";
                        }
                        dot.write(nodeId + " -- " + neighborId + edgeStyle + " ;\n");
                    }
                }
            }
    
            dot.write("}");
            dot.close();
    
        } catch (IOException e) {
            System.out.println("ERREUR sortie file ");
            e.printStackTrace();
        }
    }
    

}
