package Aalgorithm;

import MTDalgorithm.Logger;
import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.*;

/**
 * implementation of the A* search algorithm according to this pseudo code:
 * http://web.mit.edu/eranki/www/tutorials/search/
 */
public class AStar implements SearchSolver {

    private static final double INFINITY = Double.MAX_VALUE;

    private Graph graph;
    private Node targetPosition;
    private Node startPosition;

    private PriorityQueue<Node> openList; //the nodes that are open for expanding

    @Override
    public void initialize(Graph graph, Node targetStart, Node searchStart) {
        this.graph = graph;
        this.targetPosition = targetStart;
        this.startPosition = searchStart;

        openList = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1 != null && o2 != null) {
                    return Double.compare(o1.getF(), o2.getF());
                } else {
                    return 1;
                }
            }
        });
        openList.add(startPosition);
        initializeNodes();
        startPosition.setG(0);
    }

    private void initializeNodes() {
        for (Node node : graph.getNodes()) {
            node.setG(INFINITY);
            node.setParent(null);
            node.setF(INFINITY);
            node.setH(graph.getHeuristic().get(node).get(targetPosition));
        }
    }

    @Override
    public List<Node> getPath() throws NoPathFoundException {
        computeCostMinimalPath();

        Stack<Node> stack = new Stack<>();
        Node current = targetPosition;
        stack.push(current);
        while (current.getParent() != null) {
            current = current.getParent();
            stack.push(current);
        }

        List<Node> list = new ArrayList<>();
        while (!stack.empty()) {
            list.add(stack.pop());
        }
        return list;
    }

    /**
     * Computes the shortest path to te current target position and sets
     * the parent pointers accordingly
     */
    private void computeCostMinimalPath() {
        while (!openList.isEmpty()) {
            Node q = openList.poll();
            Logger.log("expand " + q);
            for (Edge edge : q.getEdges()) {
                Node s = edge.getNodeB();
                if (s != startPosition && q.getG() + edge.getWeight() < s.getG()) {
                    s.setParent(q);
                    s.setG(q.getG() + edge.getWeight());
                    s.calculateF();
                    openList.remove(s);
                    openList.add(s);
                }
                if (s.equals(targetPosition)) {
                    //found the target -> terminate search
                    return;
                }
            }
        }
    }
}
