public class TP3 {    
    public static void main(String[] args) {
        if(args.length != 7){
            System.out.println("ERREUR 7 paramètres en entrée: choix(-b ou -c), file_name, considered_node_label");
            return;
        }


        

        String option = args[0];    // "-w" or "-k" for either Watts et Strogatz or Kleinberg
        String output = args[1];    // path to file output
        int arg2 = Integer.parseInt(args[2]);      // either n or c
        int arg3 = Integer.parseInt(args[3]);      // either k or origine_x 
        int arg5 = Integer.parseInt(args[5]);      // either origine or cible_x
        int arg6 = Integer.parseInt(args[6]);      // either cible or cible_y
        if(arg3 < 0 || arg3 > arg2 || arg5 > arg2 || arg5 < 0 || arg6 > arg2 || arg6 < 0){
            System.out.println("ERREUR : une ou plusieurs coordonnees non valides ");
        }
        else{
            if (option.equals("-w")) {
                double arg4 = Double.parseDouble(args[4]);      //  p
                if(arg4 > 1 || arg4 < 0){
                    System.out.println("ERREUR : p doit etre entre 0 et 1");
                }
                else{
                    new WattsStrogatzGraph(output, arg2, arg3, arg4, arg5, arg6);
                }
                // System.out.println(graph.calculateBetweennessCentrality(nodeLabel));
            } else if (option.equals("-k")) {
                int arg4 = Integer.parseInt(args[4]);      // origine_y
                if(arg4 > arg2 || arg4 < 0){
                    System.out.println("ERREUR : une ou plusieurs coordonnees non valides ");
                }
                else{
                    new KleinbergGraph(output, arg2, arg3, arg4, arg5, arg6);
                }
            } else {
                System.out.println("Invalid option: " + option);
                System.exit(1);
            }
        }

    }

}