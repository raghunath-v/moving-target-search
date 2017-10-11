package graph;

import java.util.HashSet;
import java.util.Set;

/**
 * basic implementation of a node in a graph with a set of edges.
 */
public class Node {

    private Set<Edge> edges;

    /* variables needed for the MTDalgorithm */
    private double h; //the heuristic value for this node
    private double g; //an approximation of the cost from the start node to this node
    private double f; //the sum of g and h
    private Node parent; //the parent node in the search tree
    private Edge edgeToParent; //the edge from the parent to this node
    private double rhs; //one step lookahead g value
    private double key; //the priority key for this node

    /* variables needed to identify node */
    private String id; //string representation for this node
    private int index; //index of node in graph

    private double lat; //latitude of this nodes location
    private double longt; //longtitude of this nodes location


    public Node(String id, double lat, double longt, int index) {
        edges = new HashSet<>();
        this.id = id;
        this.lat = lat;
        this.longt = longt;
        this.index = index;
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

    public void calculateF() {
        f = g + h;
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

    public double getKey() {
        return key;
    }

    public double getLat() {
        return lat;
    }

    public double getLongt() {
        return longt;
    }

    public String getId() {
        return id;
    }

    public Edge getEdgeToParent() {
        return edgeToParent;
    }

    public void setEdgeToParent(Edge edgeToParent) {
        this.edgeToParent = edgeToParent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return id;
    }
}
