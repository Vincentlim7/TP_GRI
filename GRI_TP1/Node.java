class Node {
    private int id; // Node label in the txt filed remmaped to [0, n[] 
    private int label; // Node label in the txt filed
    private int degree; // Number of outgoing edge
    private int adjListIndex; // Index of the beginning of its outgoing edge list in the adjacent list of all nodes
    private boolean visited; // true if all of its edge has been examined
    private int dist; // distance to the considered node

    public Node(int id, int label){
        this.id = id;
        this.label = label;
        this.degree = 0;
        this.visited = false;
    }

    public int getID(){
        return this.id;
    }

    public int getLabel(){
        return this.label;
    }

    public int getDegree(){
        return this.degree;
    }

    public void inc_degree(){
        this.degree++;
    }

    public int getAdjListIndex(){
        return this.adjListIndex;
    }

    public void setAdjListIndex(int index){
        this.adjListIndex = index;
    }

    public boolean getVisited(){
        return this.visited;
    }
    
    public void setVisited(){
        this.visited = true;
    }

    public int getDist(){
        return this.dist;
    }
    
    public void setDist(int dist){
        this.dist = dist;
    }
} 