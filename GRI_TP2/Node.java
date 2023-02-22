class Node {
    private int id; // Node label in the txt filed remmaped to [0, n[] 
    private int label; // Node label in the txt filed
    private int inDegree; // Number of incoming edge
    private int outDegree; // Number of outgoing edge
    private int adjListIndex; // Index of the beginning of its outgoing edge list in the adjacent list of all nodes
    private boolean visited; // true if all of its edge has been examined
    private int dist; // distance to the considered node
    private boolean done;
    public int npcc;
    private boolean deleted;

    public Node(){
        this.inDegree = 0;
        this.outDegree = 0;
        this.visited = false;
        this.done = false;
        this.deleted = false;
    }
    
    public Node(int id, int label){
        this.id = id;
        this.label = label;
        this.inDegree = 0;
        this.outDegree = 0;
        this.visited = false;
        this.done = false;
        this.deleted = false;
    }

    public void setID(int id){
        this.id = id;
    }

    public int getID(){
        return this.id;
    }

    public void setLabel(int label){
        this.label = label;
    }

    public int getLabel(){
        return this.label;
    }

    public int getInDegree(){
        return this.inDegree;
    }

    public void incInDegree(){
        this.inDegree++;
    }

    public void decInDegree(){
        this.inDegree--;
    }

    public int getOutDegree(){
        return this.outDegree;
    }

    public void incOutDegree(){
        this.outDegree++;
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

    public void resetVisited(){
        this.visited = false;
    }
    public int getDist(){
        return this.dist;
    }
    
    public void setDist(int dist){
        this.dist = dist;
    }

    public boolean isDone(){
        return this.done;
    }

    public void changeDone(){
        this.done = !this.done;
    }

    public boolean isDeleted(){
        return this.deleted;
    }

    public void setDeleted(){
        this.deleted = true;
    }
} 