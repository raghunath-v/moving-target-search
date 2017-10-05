package MTDalgorithm;

import Aalgorithm.AStar;
import Aalgorithm.SearchSolver;
import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MTDStarTest {
    public static void main(String[] args) {
        testMTD();
    }

    private static void testMTD() {
        //build test graph
        Node policeStart = new Node("policeStart", 0, 0);
        Node thiefStart = new Node("thiefStart", 10, 9);
        Node node3 = new Node("node3", 0, 5);
        Node node4 = new Node("node4", 6, 2);
        Node node5 = new Node("node5", 3, 7);
        Node node6 = new Node("node6", 1 , 11);
        Node node7 = new Node("node7", 10, 9);

        policeStart.addEdge(new Edge(policeStart, node3, calcDistance(policeStart, node3)));
        policeStart.addEdge(new Edge(policeStart, node4, calcDistance(policeStart, node4)));

        node3.addEdge(new Edge(node3, policeStart, calcDistance(policeStart, node3)));
        node3.addEdge(new Edge(node3, node6, calcDistance(node6, node3)));
        node3.addEdge(new Edge(node3, node5, calcDistance(node5, node3)));

        node4.addEdge(new Edge(node4, policeStart, calcDistance(policeStart, node4)));
        node4.addEdge(new Edge(node4, node5, calcDistance(node5, node4)));
        node4.addEdge(new Edge(node4, node7, calcDistance(node7, node4)));

        node5.addEdge(new Edge(node5, node3, calcDistance(node3, node5)));
        node5.addEdge(new Edge(node5, node6, calcDistance(node6, node5)));
        node5.addEdge(new Edge(node5, node4, calcDistance(node4, node5)));

        node6.addEdge(new Edge(node6, node3, calcDistance(node3, node6)));
        node6.addEdge(new Edge(node6, node5, calcDistance(node5, node6)));
        node6.addEdge(new Edge(node6, thiefStart, calcDistance(thiefStart, node6)));

        node7.addEdge(new Edge(node7, node4, calcDistance(node7, node4)));
        node7.addEdge(new Edge(node7, thiefStart, calcDistance(thiefStart, node7)));

        thiefStart.addEdge(new Edge(thiefStart, node6, calcDistance(thiefStart, node6)));
        thiefStart.addEdge(new Edge(thiefStart, node7, calcDistance(thiefStart, node7)));

        Graph graph = new Graph();
        graph.addNode(policeStart);
        graph.addNode(thiefStart);
        graph.addNode(node3);
        graph.addNode(node4);
        graph.addNode(node5);
        graph.addNode(node6);
        graph.addNode(node7);

        Map<Node, Map<Node, Double>> heuristic = new HashMap<>();
        for (Node a : graph.getNodes()) {
            heuristic.put(a, new HashMap<>());
            for (Node b : graph.getNodes()) {
                heuristic.get(a).put(b, calcDistance(a, b));
            }
        }
        graph.setHeuristic(heuristic);

        try {
            MovingTargetSearchSolver mtsolver = new MTDStar();
            mtsolver.initialize(graph, thiefStart, policeStart);
            System.out.println("------ MTD*: -----");
            List<Node> path = mtsolver.moveTarget(thiefStart, policeStart);
            for (Node s : path) {
                System.out.println(s);
            }

            path = mtsolver.moveTarget(node6, node4);
            for (Node s : path) {
                System.out.println(s);
            }

            SearchSolver solver = new AStar();
            solver.initialize(graph, thiefStart, policeStart);
            System.out.println("------ A*: -----");
            path = solver.getPath();
            for (Node s : path) {
                System.out.println(s);
            }

            solver.initialize(graph, node6, node4);
            path = solver.getPath();
            for (Node s : path) {
                System.out.println(s);
            }
        } catch (MovingTargetSearchSolver.NoPathFoundException| SearchSolver.NoPathFoundException e) {
            e.printStackTrace();
        }
    }

    private static double calcDistance(Node a, Node b) {
        double x1 = a.getX();
        double y1 = a.getY();
        double x2 = b.getX();
        double y2 = b.getY();
        return Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2));
    }
}
