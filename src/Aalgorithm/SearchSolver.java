package Aalgorithm;

import graph.Graph;
import graph.Node;

import java.util.List;

public interface SearchSolver {

    /**
     * initializes the algorithm. This method has to be called before calling any other method
     * @param graph the graph of the map where the search takes place
     * @param targetStart the start position for the target (in our case the thief)
     * @param searchStart the start position for the searcher (in our case the police)
     */
    void initialize(Graph graph, Node targetStart, Node searchStart);

    /**
     * gets the shortest path from search start to target
     * @return list of nodes that descibe the path
     * @throws NoPathFoundException if there is no path to the target
     */
    List<Node> getPath() throws NoPathFoundException;

    class NoPathFoundException extends Exception {

    }
}