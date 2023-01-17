public class TP1 {    
    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("ERREUR 2 paramètres en entrée: file_name, considered_node_id");
            return;
        }

        String fileName = args[0];
        int nodeID = Integer.parseInt(args[1]);

        Graph graph = new Graph(fileName);

        if(nodeID < 0 || nodeID > graph.getNbNode()){
            System.out.println("ERREUR de valeur : nodeID doit être supérieur à 0 et inférieur au nombre de noeud dans le graphe (" + graph.getNbNode() + ")");
            return;
        }
        System.out.println("----------------------------");
        System.out.println(graph.getNbNode());
        System.out.println(graph.getNbEdge());
        System.out.println(graph.getMaxDegree());
        int[] out = graph.getNB_BFS(1);
        System.out.println(out[0]); // nb sommet accessible depuis le noeud
        System.out.println(out[1]); // eccentricité du noeud
        // System.out.println(); // nombre de composantes connexes
    }

}