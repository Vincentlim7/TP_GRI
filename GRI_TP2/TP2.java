public class TP2 {    
    public static void main(String[] args) {
        if(args.length != 3){
            System.out.println("ERREUR 3 paramètres en entrée: choix(-b ou -c), file_name, considered_node_label");
            return;
        }

        String fileName = args[1];
        int nodeLabel = Integer.parseInt(args[2]);

        Graph graph = new Graph(fileName);

        String option = args[0];
        
        if (option.equals("-b")) {
            System.out.println(graph.calculateBetweennessCentrality(nodeLabel));
        } else if (option.equals("-c")) {
            System.out.println(graph.computeCardiality(nodeLabel));
        } else {
            System.out.println("Invalid option: " + option);
            System.exit(1);
        }
    }

}