import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
// import java.util.ArrayList;
import java.util.HashMap;
// import java.util.LinkedList;
import java.util.Map;
// import java.util.Queue;
// import java.util.Stack;


public class Graph {
    private int nbNode; // number of nodes
    private int nbEdge; // number of edges
    private int maxOutDegree; // max degree
    private int maxInDegree;
    private Node[] nodeList; // list of nodes
    private int[] adjList; // adjacency list ID

    Graph(String fileName){
        nbNode = 0;
        nbEdge = 0;
        maxOutDegree = 0;
        maxInDegree = 0;
        
        Map<Integer, Integer> idMapping = new HashMap<>(); // map to remap node id from adjList to adjList

        // Loop to count number of nodes and edges
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            int numLigne = 0; 
            while ((line = br.readLine()) != null) {
                numLigne++;
                if(line.length()==0||line.charAt(0) == '#') // commentaire
                    continue;
                    int nodeLabel = 0;
                    for (int pos = 0; pos < line.length(); pos++){
                        // on converti char par char de ascii texte vers int
                        char c = line.charAt(pos);
                        if(c==' ' || c == '\t') { // on a fini le premier sommet
                            if(idMapping.get(nodeLabel)==null) // on rencontre ce sommet pour la premiere fois
                                idMapping.put(nodeLabel, nbNode++); // le voila numéroté n
                            
                            nodeLabel=0;
                            continue;
                            }
                        if(c < '0' || c > '9')
                            {
                            System.out.println("ERREUR format ligne "+numLigne+"c = "+c+" valeur "+(int)c);
                            System.exit(1);
                            }
                            nodeLabel = 10*nodeLabel + c - '0';
                        }
                    if(idMapping.get(nodeLabel)==null) // on rencontre ce sommet pour la premiere fois
                        idMapping.put(nodeLabel, nbNode++); // le voila numéroté n
                    nbEdge ++;
            }
            br.close();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        nodeList = new Node[nbNode];
        adjList = new int[nbEdge];

        for(int i=0;i<nbNode;i++){
            nodeList[i]=new Node();
        }
            


	// Passe 2 : relit le fichier et remplit adj et les noms de sommets. J'avoue un vilain copié/collé de la passe 1 :-(
        int posAdj=0 ; // position dans adj
        int x=0,y ; // on lit l'arete x--y
        int lastX=0; // num précédent sommet lu. Erreur si pas croissant.
        try {
            BufferedReader read = new BufferedReader(new FileReader(fileName));
    
            while(true) {
            String line = read.readLine();
            if(line==null) // fin de fichier
                break;
            if(line.length()==0||line.charAt(0) == '#') // commentaire
                continue;
            int a = 0;
            for (int pos = 0; pos < line.length(); pos++)
                {
                boolean chiffres = true; // vaut vrai tant qu'on a lu que des chiffres. Sert à détecter le premier blanc.
                char c = line.charAt(pos);
                if(c==' ' || c == '\t')
                    {
                    if(chiffres) { // on lit le sommet a
                        chiffres = false; 
                        // on assume que a a été mis dans la HashMap a l'étape 1
                        x = (int) idMapping.get(a); // son numero est x
                        if(x!=lastX) // on a changé de sommet ?
                        {								    
                            if(nodeList[x].isDone()) // zut c'est la deuxieme fois qu'on voit x comme origine d'un arc
                            {
                                System.out.println("ERREUR graphe mal trié : liste d'adjacence du sommet "+ a +"non consecutive !");
                                System.exit(1);
                            }
                            else
                            nodeList[x].changeDone();
                            nodeList[x].setLabel(a);
                            nodeList[x].setID(x);
                        }
                        lastX = x;
                        nodeList[x].incOutDegree();
                        if(maxOutDegree < nodeList[x].getOutDegree())
                            maxOutDegree = nodeList[x].getOutDegree();
                        nodeList[x].setLabel(a);
                        if(nodeList[x].getAdjListIndex()==0 && x>0 ) // ce sommet n'a pas encore sont debutAdj
                            nodeList[x].setAdjListIndex(posAdj);
                    }
                        
                    a=0;
                    continue;
                    }
                // on assume que c est entre '0' et '9' car vérifié lors de la apsse précédente
                a = 10*a + c - '0';
                }
            y = (int) idMapping.get(a); // on vient de lire l'extremite de l'arete
            nodeList[y].setLabel(a);
            nodeList[y].incInDegree();
            if(maxInDegree < nodeList[y].getInDegree())
                maxInDegree = nodeList[y].getInDegree();
            adjList[posAdj++] = y; // on l'insere dans la liste d'adj
    
              }
              read.close();
        }  catch (IOException e) {
            System.out.println("ERREUR entree/sortie sur "+fileName);
            System.exit(1);
        }
    }

    public int getNbNode(){
        return this.nbNode;
    }

    public int getNbEdge(){
        return this.nbEdge;
    }

    public int getMaxOutDegree(){
        return this.maxOutDegree;
    }

    public int getMaxInDegree(){
        return this.maxInDegree;
    }

    public int[] getNB_BFS(int label_ori){
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
            for(int i=0;i<nodeList[x].getOutDegree();i++) {
                int y = adjList[(nodeList[x].getAdjListIndex()+i)%this.adjList.length];
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
                    for(int j=0;j<nodeList[x].getOutDegree();j++) {
                        int y = adjList[(nodeList[x].getAdjListIndex()+j)%this.adjList.length];
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

    public void BFS(int v){
        for(Node n:nodeList){
            n.resetVisited();
            n.npcc = 0;
            n.setDist(-1);
        }
        ArrayDeque<Integer> f = new ArrayDeque<Integer>();
        f.add(v);
        nodeList[v].npcc = 1;
        nodeList[v].setVisited();
        nodeList[v].setDist(0);
        while(!f.isEmpty()){
            int x = f.poll();
            for(int i=0;i<nodeList[x].getOutDegree();i++) {
                int y = adjList[(nodeList[x].getAdjListIndex()+i)%this.adjList.length];
                if(!nodeList[y].getVisited()){
                    f.add(y);
                    nodeList[y].setVisited();
                    nodeList[y].setDist(nodeList[x].getDist()+1);
                }
                if(nodeList[y].getDist()==nodeList[x].getDist()+1){
                    nodeList[y].npcc += nodeList[x].npcc;
                }
            }
        }
    }

    // Version using one list of all nodes of degree k-1 at each iteration
    public int computeCardiality(int nodeLabel){
        int k = 1;
        ArrayDeque<Node> f = new ArrayDeque<Node>();

        outerloop:
        while(true){
            // create list of nodes of degree k-1
            for (Node node : nodeList){
                if(!node.isDeleted() && node.getInDegree() == k-1){
                    f.push(node);
                }
            }
            while(!f.isEmpty()){
                Node x = f.poll();
                if(x.getLabel() == nodeLabel) // wanted node found
                    break outerloop;
                x.setDeleted();
                int index = x.getAdjListIndex(); // retrieving x's index in adjList and outDegree to get all y node with (x, y) edge
                int outDegree = x.getOutDegree();
                for(int j = index; j < index + outDegree; j++){
                    Node y = nodeList[adjList[j]]; // this is node y
                    if(y.isDeleted())
                        continue;
                    y.decInDegree();
                    if(y.getInDegree() < k && !f.contains(y))
                        f.push(y);
                }
            }
            k++;
        }
        return k-1;
    }



    // Version using a list for each degree (does not work)
    // public int computeCardiality(int nodeLabel){
    //     Map<Integer, ArrayDeque<Integer>> inDegreeMapping = new HashMap<>();
    //     // Create every list of node of inDegree i
    //     for(int i=0; i <= this.maxInDegree; i++){
    //         if(inDegreeMapping.get(i) == null)
    //             inDegreeMapping.put(i, new ArrayDeque<Integer>());
    //     }

    //     // Populate every inDegree list
    //     for (Node node : nodeList){
    //         int degree = node.getInDegree();
    //         inDegreeMapping.get(degree).add(node.getID());
    //     }
    //     // System.out.println(inDegreeMapping);
    //     int k = 0;
    //     outerloop:
    //     while(true){
    //         // System.out.println("value of k : " + k);
    //         for(int i = 0; i < k; i++){ // Parcourir toutes les listes de degré 0 à k
    //             ArrayDeque<Integer> idNodeK = inDegreeMapping.get(i);
    //             // System.out.println("value of degree examined : " + i);
    //             // System.out.println(idNodeK);
    //             for (Integer id : idNodeK){
    //                 Node node = nodeList[id];
    //                 // if(node.isDeleted())
    //                 //     continue;
    //                 if(node.getLabel() == nodeLabel) // wanted node found
    //                     break outerloop;
    //                 node.setDeleted();
    //                 int index = node.getAdjListIndex(); // retrieving x's index in adjList and outDegree to get all y node with (x, y) edge
    //                 int outDegree = node.getOutDegree();
    //                 // System.out.println("----- x is : " + id);
    //                 for(int j = index; j < index + outDegree; j++){
    //                     Node targetNode = nodeList[adjList[j]]; // this is node y
    //                     // System.out.println(adjList[j] + " has degree : " + targetNode.getInDegree());
    //                     // System.out.println("y is " + adjList[j] + " degree is " + targetNode.getInDegree());
    //                     // System.out.println("Before : " + inDegreeMapping.get(targetNode.getInDegree()));
    //                     inDegreeMapping.get(targetNode.getInDegree()).remove(targetNode.getID()); // removing node id from his previous degree list
    //                     // System.out.println("After : " + inDegreeMapping.get(targetNode.getInDegree()));
    //                     targetNode.decInDegree();
    //                     if(targetNode.getInDegree() >= 0)
    //                         inDegreeMapping.get(targetNode.getInDegree()).add(targetNode.getID()); // add to its new degree list
    //                 }
    //             }
    //         }
    //         k++;
    //     }
    //     return k-1;
    // }

    public double calculateBetweennessCentrality(int v_label) {
        double betweenness = 0.0;
        int v = -1;
        for(Node node:nodeList){
            if(node.getLabel()==v_label){
                v =node.getID();
                break;
            }
        }

        BFS(v);
        int[] d_v = new int[nbNode];
        int[] npcc_v = new int[nbNode];



        for(int i = 0; i<nbNode; i++){
            d_v[i] = nodeList[i].getDist();
            npcc_v[i] = nodeList[i].npcc;
        }


        int[] d_s = new int[nbNode];
        int[] npcc_s = new int[nbNode];

        for(int s = 0; s<nbNode; s++){

            if(s==v) {
                continue;
            }

            BFS(s);

            for(int i = 0; i<nbNode; i++){
                d_s[i] = nodeList[i].getDist();
                npcc_s[i] = nodeList[i].npcc;
            }

            for(int t = 0; t<nbNode; t++){
                if(t==v||t==s){
                    continue;
                }
                if(d_v[t]+d_s[v]>d_s[t]){
                    continue;
                }
                if(npcc_s[t]==0){
                    continue;
                }
                if(d_v[t]+d_s[v]==d_s[t]){
                    betweenness += (double)npcc_s[v] * npcc_v[t] / npcc_s[t]; //svt / st
                }

            }

        }
        return betweenness/((nbNode-1)*(nbNode-2));

    }

}
