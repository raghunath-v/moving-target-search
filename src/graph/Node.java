package graph;

import java.util.Set;

/**
 * basic implementation of a node in a graph with a set of edges
 */
public class Node {
    private Set<Edge> edges;

    /* variables needed for the algorithm */
    private double h; //the heuristic value for this node
    private double g; //an approximation of the cost from the start node to this node
    private double f; //the sum of g and h
    private Node parent; //the parent node in the search tree
    private double rhs; //one step lookahead g value

    private double key; //the priority key for this node


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

    /**
     * calculates the key for this node
     * @return resulting key
     */
    public double calculateKey(double k) {
        key = Math.min(g, rhs) + h + k;
        return key;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public double getRhs() {
        return rhs;
    }

    public void setRhs(double rhs) {
        this.rhs = rhs;
    }
}
