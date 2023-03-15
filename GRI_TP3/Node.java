class Node {
    private int id; // Node label in the txt filed remmaped to [0, n[] 
    private int x;
    private int y;

    
    public Node(int id, int x, int y){
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void setID(int id){
        this.id = id;
    }

    public int getID(){
        return this.id;
    }


    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

} 