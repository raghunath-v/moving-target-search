import Aalgorithm.AStar;
import Aalgorithm.SearchSolver;
import MTDalgorithm.Logger;
import MTDalgorithm.MTDStar;
import MTDalgorithm.MovingTargetSearchSolver;
import graph.Edge;
import graph.Graph;
import graph.Node;
import parser.InputGraphParser;
import parser.MovingTargetSearchProblem;

import java.util.List;

public class RaceTest {

    public static void main(String[] args) {
        int time = 0;
        int thiefTime = 0;
        int policeTime = 0;
        boolean catched = false;

        MovingTargetSearchProblem problem = InputGraphParser.parseStringFromFile(args[0]);

        if (problem == null) {
            throw new Error("parsing error");
        }

        Graph graph = problem.getGraph();
        Logger.log(graph.toString());
        Node thiefPosition = problem.getTargetStart();
        Logger.log(thiefPosition.toString());
        Node policePosition = problem.getSearchStart();
        Logger.log(policePosition.toString());
        Node thiefTarget = problem.getTargetTarget();
        Node thiefNext = null;
        int thiefPathPosition = 0;

        SearchSolver solver = new AStar();
        MovingTargetSearchSolver policeSolver = new MTDStar();
        solver.initialize(graph, thiefTarget, thiefPosition);
        List<Edge> thiefPath = null;
        try {
            thiefPath = solver.getPath();
        } catch (SearchSolver.NoPathFoundException e) {
            throw new Error("no path");
        }

        policeSolver.initialize(graph, thiefPosition, policePosition);
        Logger.log("start catch");
        while (!catched && thiefNext != thiefTarget) {
            if (thiefTime == 0) {
                thiefPosition = thiefPath.get(thiefPathPosition).getNodeA();
                thiefNext = thiefPath.get(thiefPathPosition).getNodeB();
                thiefTime = (int) thiefPath.get(thiefPathPosition).getWeight();
                thiefPathPosition++;
                Logger.log("thief is between " + thiefPosition +" and " + thiefNext);
            }
            if (policeTime == 0) {
                if (policePosition.equals(thiefNext)) {
                    catched = true;
                    continue;
                }
                try {
                    List<Edge> policePath = policeSolver.moveTarget(thiefNext, policePosition);
                    policePosition = policePath.get(0).getNodeB();
                    policeTime = (int) policePath.get(0).getWeight();
                    Logger.log("police drives to " + policePosition);
                } catch (MovingTargetSearchSolver.NoPathFoundException e) {
                    throw new Error("no path");
                }
            }

            policeTime--;
            thiefTime--;
        }

        if (thiefNext == thiefTarget) {
            Logger.log("thief escaped");
        }
        if (catched) {
            Logger.log("thief got catched");
        }
    }
}
