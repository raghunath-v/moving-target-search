package Aalgorithm;

import graph.Edge;
import graph.Graph;
import graph.Node;
import main.ExpandCounter;
import main.NoPathFoundException;

import java.util.List;

public interface SearchSolver {

    /**
     * initializes the algorithm. This method has to be called before calling any other method
     * @param graph the graph of the map where the search takes place
     * @param targetStart the start position for the target (in our case the thief)
     * @param searchStart the start position for the searcher (in our case the police)
     * @param counter the counter object that counts the expanded nodes
     */
    void initialize(Graph graph, Node targetStart, Node searchStart, ExpandCounter counter);

    /**
     * gets the shortest path from search start to target
     * @return list of edges that describe the path
     * @throws NoPathFoundException if there is no path to the target
     */
    List<Edge> getPath() throws NoPathFoundException;
}
