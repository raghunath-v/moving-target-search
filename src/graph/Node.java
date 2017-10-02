package graph;

import java.util.Set;

/**
 * basic implementation of a node in a graph with a set of edges
 */
public class Node {
    private Set<Edge> edges;

    public Node() {

    }

    public Node(Set<Edge> edges) {
        this.edges = edges;
    }

    public void addEdge(Edge newEdge) {
        edges.add(newEdge);
    }

    public void removeEdge(Edge edge) {
        edges.remove(edge);
    }

    public Set<Edge> getEdges() {
        return edges;
    }
}
