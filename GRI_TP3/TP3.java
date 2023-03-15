public class TP3 {    
    public static void main(String[] args) {
        if(args.length != 7){
            System.out.println("ERREUR 7 paramètres en entrée: choix(-b ou -c), file_name, considered_node_label");
            return;
        }

        String fileName = args[1];
        int nodeLabel = Integer.parseInt(args[2]);

        

        String option = args[0];    // "-w" or "-k" for either Watts et Strogatz or Kleinberg
        String output = args[1];    // path to file output
        String arg2 = args[2];      // either n or c
        String arg3 = args[3];      // either k or origine_x
        String arg4 = args[4];      // either k or origine_y
        String arg5 = args[5];      // either origine or cible_x
        String arg6 = args[6];      // either cible or cible_y
        
        if (option.equals("-w")) {

            // System.out.println(graph.calculateBetweennessCentrality(nodeLabel));
        } else if (option.equals("-k")) {
            new KleinbergGraph(output, arg2, arg3, arg4, arg5, arg6);
        } else {
            System.out.println("Invalid option: " + option);
            System.exit(1);
        }
    }

}