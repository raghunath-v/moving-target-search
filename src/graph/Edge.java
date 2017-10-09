package graph;

/**
 * basic implementation of an edge in a graph with two nodes and the cost for using this edge
 */
public class Edge {
    private Node nodeA;
    private Node nodeB;
    private double weight; //TODO: adjust for probability distribution

    public Edge(Node nodeA, Node nodeB, double weight) {
        this.nodeA  = nodeA;
        this.nodeB  = nodeB;
        this.weight  = weight;
    }

    public Node getNodeA() {
        return nodeA;
    }

    public Node getNodeB() {
        return nodeB;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return nodeA + " -> " + nodeB;
    }
}
