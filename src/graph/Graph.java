package graph;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * basic graph implementation with sets for nodes and edges
 */
public class Graph {
    private Set<Node> nodes;
    private Set<Edge> edges;
    private Map<Node, Map<Node, Double>> heuristic;

    public Graph() {
        nodes = new HashSet<>();
        edges = new HashSet<>();
    }

    public Graph(Set<Node> nodes, Map<Node, Map<Node, Double>> heuristic) {
        this.nodes = nodes;
        this.heuristic = heuristic;
        edges = new HashSet<>();
    }

    public Graph(Set<Node> nodes, Set<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
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

    public Map<Node, Map<Node, Double>> getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Map<Node, Map<Node, Double>> heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("----- Nodes ------").append("\n");
        for (Node n : getNodes()) {
            //builder.append(n).append(":").append("\n");
            for (Edge e : n.getEdges()) {
                builder.append(e.getNodeA()).append(" -> ").append(e.getNodeB()).append(" [ label = ").append(e.getWeight()).append(" ] ;").append("\n");
            }
        }

        return builder.toString();
    }
}
