package Aalgorithm;

import MTDalgorithm.Logger;
import graph.Edge;
import graph.Graph;
import graph.Node;
import main.ExpandCounter;
import main.NoPathFoundException;

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

    private ExpandCounter counter;

    @Override
    public void initialize(Graph graph, Node targetStart, Node searchStart, ExpandCounter counter) {
        this.graph = graph;
        this.targetPosition = targetStart;
        this.startPosition = searchStart;
        this.counter = counter;

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
            node.setEdgeToParent(null);
            node.setF(INFINITY);
            node.setH(graph.getHeuristic().get(node).get(targetPosition));
        }
    }

    @Override
    public List<Edge> getPath() throws NoPathFoundException {
        computeCostMinimalPath();

        Stack<Edge> stack = new Stack<>();
        Node current = targetPosition;
        while (current.getParent() != null) {
            stack.push(current.getEdgeToParent());
            current = current.getParent();
        }

        List<Edge> list = new ArrayList<>();
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
            counter.countNodeExpand();
            for (Edge edge : q.getEdges()) {
                Node s = edge.getNodeB();
                if (s != startPosition && q.getG() + edge.getWeight() < s.getG()) {
                    s.setParent(q);
                    s.setEdgeToParent(edge);
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
