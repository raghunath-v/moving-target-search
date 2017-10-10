package MTDalgorithm;

import graph.Edge;
import graph.Graph;
import graph.Node;
import main.ExpandCounter;
import main.NoPathFoundException;

import java.util.*;

/**
 * implementation of the Moving Target D* MTDalgorithm according to this paper:
 * http://idm-lab.org/bib/abstracts/papers/aamas10a.pdf
 */
public class MTDStar implements MovingTargetSearchSolver{

    private static final double INFINITY = Double.MAX_VALUE;

    private Graph graph;
    private Node targetPosition;
    private Node currentPosition;

    private PriorityQueue<Node> openList; //the nodes that are open for expanding
    private Set<Node> closedList; //the nodes that have been expanded

    private double k; //value k_m of the pseudo code

    private ExpandCounter counter;

    public MTDStar() {

    }

    @Override
    public void initialize(Graph graph, Node targetStart, Node searchStart, ExpandCounter counter) {
        this.graph = graph;
        targetPosition = targetStart;
        currentPosition = searchStart;
        this.counter = counter;

        initializeNodes();
        currentPosition.setRhs(0);
        k = 0;
        openList = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1 != null && o2 != null) {
                    return Double.compare(o1.getKey(), o2.getKey());
                } else {
                    return 1;
                }
            }
        });
        closedList = new HashSet<>();
        currentPosition.calculateKey(k);
        openList.add(currentPosition);

        //set heuristics
        for (Node n : graph.getNodes()) {
            n.setH(graph.getHeuristic().get(n).get(targetPosition));
        }
    }

    private void initializeNodes() {
        for (Node node : graph.getNodes()) {
            node.setRhs(INFINITY);
            node.setG(INFINITY);
            node.setParent(null);
            node.setEdgeToParent(null);
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


    /**
     * Computes the shortest path to te current target position and sets
     * the parent pointers accordingly
     */
    private void computeCostMinimalPath() {
        while (openList.peek().getKey() < targetPosition.calculateKey(k) ||
                targetPosition.getRhs() > targetPosition.getG()) {
            Node u = openList.peek();
            double kOld = u.getKey();
            double kNew = u.calculateKey(k);
            if (kOld < kNew) {
                //update key in priority queue
                openList.remove(u);
                openList.add(u);
            } else if (u.getG() > u.getRhs()) {
                //Logger.log("expand node " + u.getId());
                counter.countNodeExpand();
                u.setG(u.getRhs());
                openList.remove(u);
                closedList.add(u);
                for (Edge edge : u.getEdges()) {
                    Node s = edge.getNodeB();
                    if (s != currentPosition && s.getRhs() > u.getG() + edge.getWeight()) {
                        s.setParent(u);
                        s.setEdgeToParent(edge);
                        s.setRhs(u.getG() + edge.getWeight());
                        updateState(s);
                    }
                }
            } else {
                u.setG(INFINITY);
                updateState(u);
                for (Edge edge : u.getEdges()) {
                    Node s = edge.getNodeB();
                    if (!s.equals(currentPosition) && s.getParent().equals(u)) {
                        double min = INFINITY;
                        Node minNode = null;
                        Edge minEdge = null;
                        for (Edge preEdge : s.getEdges()) {
                            if (min < preEdge.getNodeB().getG() + preEdge.getWeight()) {
                                min = preEdge.getNodeB().getG() + preEdge.getWeight();
                                minNode = preEdge.getNodeB();
                                minEdge = edge;
                            }
                        }
                        s.setRhs(min);
                        //TODO: check if this really is the same as in pseudo code
                        if (min == INFINITY) {
                            s.setParent(null);
                            s.setEdgeToParent(null);
                        } else {
                            s.setParent(minNode);
                            s.setEdgeToParent(minEdge);
                        }
                    }
                    updateState(s);
                }
            }
        }
    }

    /**
     * adjusts search tree to new start node
     */
    private void optimizedDeletion() {
        currentPosition.setParent(null);
        currentPosition.setEdgeToParent(null);
        Set<Node> deletedList = new HashSet<>();

        //calculate set of nodes that are in the search tree but not in the
        //subtree rooted at the current start node
        Set<Node> subTreeNodes = new HashSet<>();
        getSubtreeNodes(currentPosition, subTreeNodes);
        Set<Node> toDeleteNodes = new HashSet<>();
        toDeleteNodes.addAll(openList);
        toDeleteNodes.addAll(closedList);
        toDeleteNodes.removeAll(subTreeNodes);

        for (Node s : toDeleteNodes) {
            s.setParent(null);
            s.setEdgeToParent(null);
            s.setRhs(INFINITY);
            s.setG(INFINITY);
            openList.remove(s); //removes s if it is contained
            deletedList.add(s);
        }

        for (Node s : deletedList) {
            for (Edge edge : s.getEdges()) {
                Node pred = edge.getNodeB();
                if (s.getRhs() > pred.getG() + edge.getWeight()) {
                    s.setRhs(pred.getG() + edge.getWeight());
                    s.setParent(pred);
                    s.setEdgeToParent(edge);
                }
            }
            if (s.getRhs() < INFINITY) {
                s.calculateKey(k);
                openList.add(s);
            }
        }
    }

    private void getSubtreeNodes(Node subTreeRoot, Set<Node> subTreeNodes) {
        subTreeNodes.add(subTreeRoot);
        for (Edge edge : subTreeRoot.getEdges()) {
            Node s = edge.getNodeB();
            if (s.getParent() != null && s.getParent().equals(subTreeRoot)) {
                //recursive call
                getSubtreeNodes(s, subTreeNodes);
            }
        }
    }

    @Override
    public List<Edge> moveTarget(Node newTarget, Node startPosition) throws NoPathFoundException {
        Node oldTarget = targetPosition;
        Node oldStart = currentPosition;
        targetPosition = newTarget;
        currentPosition = startPosition;
        k += oldTarget.getH();
        if (!currentPosition.equals(oldStart)) {
            optimizedDeletion();
        }

        //set heuristics
        for (Node n : graph.getNodes()) {
            n.setH(graph.getHeuristic().get(n).get(targetPosition));
        }

        //TODO: implement changing edge weights

        computeCostMinimalPath();
        if (currentPosition.getRhs() == INFINITY) {
            throw new NoPathFoundException();
        }

        return getPath();
    }

    private List<Edge> getPath() {
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
}
