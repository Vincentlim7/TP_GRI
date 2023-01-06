import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) 
    {
        int id = 0;
        int currentNodeLabel = 0;

        try (BufferedReader br = Files.newBufferedReader(Paths.get("graph_test2.txt"))) {

            // read line by line
            String line;
            while ((line = br.readLine()) != null) {
                String [] nodes = line.split("\\s+");
                System.out.println(line);
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }


        // String str = "1 2";
        // String [] res = str.split("\\s+");
        // System.out.println(res[1]+"\n");
        
    }
}
