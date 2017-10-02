package algorithm;

import graph.Edge;
import graph.Graph;
import graph.Node;
import org.omg.CORBA.INITIALIZE;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * implementation of the Moving Target D* algorithm according to this paper:
 * http://idm-lab.org/bib/abstracts/papers/aamas10a.pdf
 */
public class MTDStar implements MovingTargetSearchSolver{

    private static final double INFINITY = Double.MAX_VALUE;

    private Graph graph;
    private Node targetPosition;
    private Node currentPosition;

    private PriorityQueue<Node> openList; //the nodes that are open for extension
    private Set<Node> deletedList; //the nodes that were deleted

    private double k; //value k_m of the pseudo code

    public MTDStar() {

    }

    @Override
    public void initialize(Graph graph, Node targetStart, Node searchStart) {
        this.graph = graph;
        targetPosition = targetStart;
        currentPosition = searchStart;

        initializeNodes();
        currentPosition.setRhs(0);
        k = 0;
        openList = new PriorityQueue<>();
        currentPosition.calculateKey(k);
        openList.add(currentPosition);
    }

    private void initializeNodes() {
        for (Node node : graph.getNodes()) {
            node.setRhs(INFINITY);
            node.setG(INFINITY);
            node.setParent(null);
        }
    }

    /**
     * updates a state in the search tree
     * @param node the node that should be updated
     */
    private void updateState(Node node) {
        if (node.getG() != node.getRhs() && openList.contains(node)) {
            //update key in priority queue -> remove -> add
            openList.remove(node);
            node.calculateKey(k);
            openList.add(node);
        } else if (node.getG() != node.getRhs() && !openList.contains(node)) {
            node.calculateKey(k);
            openList.add(node);
        } else if (node.getG() == node.getRhs() && openList.contains(node)) {
            openList.remove(node);
        }
    }

    private void optimizedDeletion() {
        deletedList = new HashSet<>();
        currentPosition.setParent(null);

        //TODO: complete method
    }

    @Override
    public List<Edge> moveTarget(Node newTarget) {
        return null;
    }
}
