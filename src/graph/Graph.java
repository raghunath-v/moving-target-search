package graph;

import java.util.Set;

/**
 * basic graph implementation with sets for nodes and edges
 */
public class Graph {
    private Set<Node> nodes;
    private Set<Edge> edges;
    private Node root; //TODO: define other starting nodes

    public Graph() {

    }

    public Graph(Set<Node> nodes, Set<Edge> edges, Node root) {
        this.nodes = nodes;
        this.edges = edges;
        this.root = root;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public Node getRoot() {
        return root;
    }
}
