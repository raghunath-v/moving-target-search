package parser;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.io.*;
import java.util.*;

public class InputGraphParser {

    public static final String NODE_PREFIX = "node";

    /**
     * parses a problem instance from a txt file in the right format
     * @param filePath path to the txt file that should be parsed
     * @return an object that contains all information of the problem instance
     */
    public static MovingTargetSearchProblem parseStringFromFile(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            //parse first line
            String line = br.readLine();
            String[] split = line.split(" ");
            int numNodes = Integer.parseInt(split[0]);
            int numEdges = Integer.parseInt(split[1]);

            //read nodes
            List<Node> nodes = new ArrayList<>();
            for (int i = 0;i<numNodes;i++) {
                line = br.readLine();
                split = line.split(" ");
                double lat = Double.parseDouble(split[0]);
                double lon = Double.parseDouble(split[1]);
                nodes.add(new Node(NODE_PREFIX + (i + 1), lat, lon, i + 1));
            }

            //read edges
            Map<Integer, Set<Integer>> matrix = new HashMap<>();
            for (int i = 0;i<numEdges;i++) {
                line = br.readLine();
                split = line.split(" ");
                int a = Integer.parseInt(split[0]) - 1; //-1 to get a 1 based numbering
                int b = Integer.parseInt(split[1]) - 1; //-1 to get a 1 based numbering
                if (!matrix.containsKey(a) || !matrix.get(a).contains(b)) {
                    Node nodeA = nodes.get(a);
                    Node nodeB = nodes.get(b);
                    double dist = Utils.computeDistance(nodeA, nodeB);
                    nodeA.addEdge(new Edge(nodeA, nodeB, dist));
                    nodeB.addEdge(new Edge(nodeB, nodeA, dist));
                    if (!matrix.containsKey(a)) {
                        matrix.put(a, new HashSet<>());
                    }
                    if (!matrix.containsKey(b)) {
                        matrix.put(b, new HashSet<>());
                    }
                    matrix.get(a).add(b);
                    matrix.get(b).add(a);
                }
            }

            //computing heuristic
            Map<Node, Map<Node, Double>> heuristic = new HashMap<>();
            for (Node a : nodes) {
                heuristic.put(a, new HashMap<>());
                for (Node b : nodes) {
                    heuristic.get(a).put(b, Utils.computeDistance(a, b));
                }
            }

            //build graph
            Set<Node> nodeSet = new HashSet<>(nodes);
            Graph graph = new Graph(nodeSet, heuristic);

            //get special nodes
            line = br.readLine();
            split = line.split(" ");
            int searchStartIndex = Integer.parseInt(split[0]) - 1;
            int targetStartIndex = Integer.parseInt(split[1]) - 1;
            int targetTargetIndex = Integer.parseInt(split[2]) - 1;

            //build problem object
            return new MovingTargetSearchProblem(graph, nodes.get(searchStartIndex), nodes.get(targetStartIndex),
                    nodes.get(targetTargetIndex));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
