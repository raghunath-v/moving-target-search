package MTDalgorithm;

import Aalgorithm.AStar;
import graph.Edge;
import graph.Graph;
import graph.Node;
import main.ExpandCounter;
import main.NoPathFoundException;

import java.util.List;

public class MultipleAStar implements MovingTargetSearchSolver {

    private AStar aStar;
    private Graph graph;
    private ExpandCounter counter;

    @Override
    public void initialize(Graph graph, Node targetStart, Node searchStart, ExpandCounter counter) {
        aStar = new AStar();
        aStar.initialize(graph, targetStart, searchStart, counter);
        this.graph = graph;
        this.counter = counter;
    }

    @Override
    public List<Edge> moveTarget(Node newTarget, Node startPosition) throws NoPathFoundException {
        //initialize a new A* algorithm
        aStar.initialize(graph, newTarget, startPosition, counter);
        return aStar.getPath();
    }
}
