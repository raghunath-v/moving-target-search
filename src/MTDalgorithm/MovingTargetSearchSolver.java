package MTDalgorithm;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.List;

/**
 * interface for a MTDalgorithm that solves a moving target planning problem
 */
public interface MovingTargetSearchSolver {
    /**
     * initializes the algorithm. This method has to be called before calling any other method
     * @param graph the graph of the map where the search takes place
     * @param targetStart the start position for the target (in our case the thief)
     * @param searchStart the start position for the searcher (in our case the police)
     */
    void initialize(Graph graph, Node targetStart, Node searchStart);

    /**
     * simulates a move by the target and calculates the new best path. Call initialize() before calling this
     * method
     * @param newTarget the new position of the target
     * @param startPosition the current position of the searcher
     * @return the list of edges that form the best path to the target at the new position
     */
    List<Edge> moveTarget(Node newTarget, Node startPosition) throws NoPathFoundException;

    class NoPathFoundException extends Exception {

    }
}
