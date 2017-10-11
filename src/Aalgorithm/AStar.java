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

    private Graph graph; //the graph on which the algorithm operates
    private Node targetPosition; //the target node of the search
    private Node startPosition; //the start node of the search

    private PriorityQueue<Node> openList; //the nodes that are open for expanding

    private ExpandCounter counter; //the counter that counts the number of expanded nodes

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
                    //compare nodes according to their f value
                    return Double.compare(o1.getF(), o2.getF());
                } else {
                    return 1;
                }
            }
        });
        openList.add(startPosition); //start position should be open for expanding
        initializeNodes();
        startPosition.setG(0); //the cost from start node to start node is 0
    }

    /**
     * initializes all values of a node according to the A* algorithm
     */
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

        //collect edges in stack according to parent edge pointers
        Stack<Edge> stack = new Stack<>(); //use stack to easily reverse the order
        Node current = targetPosition;
        while (current.getParent() != null) {
            stack.push(current.getEdgeToParent());
            current = current.getParent();
        }

        //reverse the order to get the path from start to target (stack is from target to start)
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
            //Logger.log("expand " + q);
            counter.countNodeExpand();

            //iterate over all successors
            for (Edge edge : q.getEdges()) {
                Node s = edge.getNodeB();
                if (s != startPosition && q.getG() + edge.getWeight() < s.getG()) {
                    //this way to the node s is faster -> take this way -> update variables accordingly
                    s.setParent(q);
                    s.setEdgeToParent(edge);
                    s.setG(q.getG() + edge.getWeight());
                    s.calculateF();

                    //update s in the open list (remove and add is the easiest way to do this as there is no update method)
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
