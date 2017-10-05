package parser;

import graph.Graph;
import graph.Node;

public class MovingTargetSearchProblem {
    private Graph graph;
    private Node searchStart;
    private Node targetStart;
    private Node targetTarget;

    public MovingTargetSearchProblem(Graph graph, Node searchStart, Node targetStart, Node targetTarget) {
        this.graph = graph;
        this.searchStart = searchStart;
        this.targetStart = targetStart;
        this.targetTarget = targetTarget;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Node getSearchStart() {
        return searchStart;
    }

    public void setSearchStart(Node searchStart) {
        this.searchStart = searchStart;
    }

    public Node getTargetStart() {
        return targetStart;
    }

    public void setTargetStart(Node targetStart) {
        this.targetStart = targetStart;
    }

    public Node getTargetTarget() {
        return targetTarget;
    }

    public void setTargetTarget(Node targetTarget) {
        this.targetTarget = targetTarget;
    }
}
