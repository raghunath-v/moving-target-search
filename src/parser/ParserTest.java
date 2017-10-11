package parser;

import graph.Graph;

/**
 * class that tests the parser class
 */
public class ParserTest {
    public static void main(String[] args) {
        String filePath = args[0];

        if (filePath == null) {
            throw new Error("invalid path");
        }

        MovingTargetSearchProblem problem = InputGraphParser.parseStringFromFile(filePath);
        Graph graph = problem.getGraph();

        System.out.println("----- Problem: -----");
        System.out.println("Graph:");
        System.out.println(graph);
        System.out.println("Police start: " + problem.getSearchStart());
        System.out.println("Target start: " + problem.getTargetStart());
        System.out.println("Target target: " + problem.getTargetTarget());
    }
}
