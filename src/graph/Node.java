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

    private String id; //string representation for this node
    private int index; //index of node in graph

    //TODO: change to lat/long (x, y for test purpose)
    private double x;
    private double y;


    public Node(String id, double x, double y, int index) {
        edges = new HashSet<>();
        this.id = id;
        this.x = x;
        this.y = y;
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getId() {
        return id;
    }
    
    public boolean isSame(Node compareNode)
    {
    	boolean flag=true;
    	if (this.getX()!=compareNode.getX()) 
    	{
    		flag=false;
    	}
    	if (this.getX()!=compareNode.getY()) 
    	{
    		flag=false;
    	}
    	return flag;
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
