import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;


public class Graph {
    private int nbNode; // number of nodes
    private int nbEdge; // number of edges
    private int maxDegree; // max degree
    private Node[] nodeList; // list of nodes
    private int[] adjList; // adjacency list
    private int[] adjListID; // adjacency list ID

    Graph(String fileName){
        nbNode = 0;
        nbEdge = 0;
        maxDegree = 0;

        // Loop to count number of nodes and edges
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            int previousID = -1;
            while ((line = br.readLine()) != null) {
                int nodeID = Integer.parseInt(line.split("\\s+")[0]); // origin node of the edge
                if(nodeID != previousID){
                    previousID = nodeID;
                    nbNode++;
                }
                nbEdge++;
            }
            br.close();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        nodeList = new Node[nbNode];
        adjList = new int[nbEdge];


        // Loop to create nodes, and populate nodeList and AdjList
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            int previousID = -1;
            int nodeIndex = -1; // remapped id
            int edgeIndex = 0;
            int nodeDegree;

            while ((line = br.readLine()) != null) {
                int nodeID = Integer.parseInt(line.split("\\s+")[0]); // origin node of the edge
                int nodeDestID = Integer.parseInt(line.split("\\s+")[1]); // dest node of the edge

                if(nodeID != previousID){ // if new node
                    previousID = nodeID;
                    nodeIndex++;
                    nodeList[nodeIndex] = new Node(nodeIndex, nodeID); // (remapped id, label)
                    nodeList[nodeIndex].setAdjListIndex(edgeIndex); // set the index value to get its sucessor in adjList
                    if(nodeIndex > 0){ // all of the previous node's edge have been taken into account already, so we can check its degree
                        nodeDegree = nodeList[nodeIndex-1].getDegree();
                        if(nodeDegree > this.maxDegree)
                            this.maxDegree = nodeDegree;
                    }
                }
                nodeList[nodeIndex].inc_degree();
                adjList[edgeIndex] = nodeDestID;
                edgeIndex++;
            }

            nodeDegree = nodeList[nbNode-1].getDegree(); // compute degree of the last node
            if(nodeDegree > this.maxDegree)
                this.maxDegree = nodeDegree;
            br.close();

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        adjListID = new int[nbEdge];
        for(int i=0; i < nbEdge; i++){
            for(Node n : nodeList){
                if(n.getLabel()==adjList[i]){
                    adjListID[i] = n.getID();
                    break;
                }
            }

        }
        

        // // Printing adj list
        // for(int i = 0; i < nbEdge; i++)
        //     System.out.println(adjList[i]);

        // System.out.println("-----------");

        // // Printing node list
        // for(int i = 0; i < nbNode; i++)
        //     System.out.println(nodeList[i].getLabel() + ", " + nodeList[i].getID() + ", " + nodeList[i].getAdjListIndex());
    }

    public int getNbNode(){
        return this.nbNode;
    }

    public int getNbEdge(){
        return this.nbEdge;
    }

    public int getMaxDegree(){
        return this.maxDegree;
    }

    public int[] getNB_BFS(int label_ori){
        // System.out.println("label"+nodeList[4].getLabel());
        // for(int x : adjList){
        //     System.out.println("*"+x);
        // }
        int[] res = new int[2];
        int nb = 0;
        int max_dist = 0;
        ArrayDeque<Integer> f = new ArrayDeque<Integer>();
        for(Node n:nodeList){
            if(n.getLabel()==label_ori){
                f.add(n.getID());
                nodeList[n.getID()].setVisited();
                nodeList[n.getID()].setDist(0);
                nb++;
                break;
            }
        }

        while(!f.isEmpty()){
            int x = f.poll();
            for(int i=0;i<nodeList[x].getDegree();i++) {
                int y = adjListID[(nodeList[x].getAdjListIndex()+i)%this.adjList.length];
                if(!nodeList[y].getVisited()){
                    nodeList[y].setVisited();
                    f.add(y);
                    nodeList[y].setDist(nodeList[x].getDist()+1);
                    max_dist = Math.max(max_dist,  nodeList[y].getDist());
                    nb++;
                }
            }
        }
        res[0] = nb;
        res[1] = max_dist;
        return res;
    }

    public int countComponents() {
        
        int len = this.nbNode;
        int count = 0;
        for (int i = 0; i < len; i++) {
            nodeList[i].resetVisited();
        }

        ArrayDeque<Integer> f = new ArrayDeque<Integer>();

        for (int i = 0; i < len; i++) {
            if (!nodeList[i].getVisited()) {
                count++;
                f.add(i);
                while(!f.isEmpty()){
                    int x = f.poll();
                    for(int j=0;j<nodeList[x].getDegree();j++) {
                        int y = adjListID[(nodeList[x].getAdjListIndex()+j)%this.adjList.length];
                        if(!nodeList[y].getVisited()){
                            nodeList[y].setVisited();
                            f.add(y);
                        }
                    }


                }
            }
        }
        return count;
    }
}
