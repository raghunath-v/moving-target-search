package main;

import Aalgorithm.AStar;
import Aalgorithm.SearchSolver;
import MTDalgorithm.Logger;
import MTDalgorithm.MTDStar;
import MTDalgorithm.MovingTargetSearchSolver;
import MTDalgorithm.MultipleAStar;
import graph.Edge;
import graph.Graph;
import graph.Node;
import parser.InputGraphParser;
import parser.MovingTargetSearchProblem;
import parser.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that runs a simulated catch between a police car and a thief car. The program needs the graph text file
 * as first argument and the algorithm for the moving target search as second one ('mtdstar' or 'astar')
 */
public class Main {

    private static final int MTD_STAR = 1;
    private static final int MULTI_A_STAR = 2;
    private static final double POLICE_TRAFFIC_MEAN = 0.7;
    private static final double THIEF_TRAFFIC_MEAN = 1.5;
    private static final double STANDARD_DERIVATION = 0.3;

    private static int policeExpandCounter;
    private static int thiefExpandCounter;

    public static void main(String[] args) {
        //parse text file
        MovingTargetSearchProblem problem = InputGraphParser.parseStringFromFile(args[0]);
        if (problem == null) {
            throw new Error("parsing error");
        }

        // selecting the algorithm for the moving target search solver
        int movingTargetAlgorithm = 0;
        if (args[1].equals("mtdstar")) {
            movingTargetAlgorithm = MTD_STAR;
        } else if (args[1].equals("astar")) {
            movingTargetAlgorithm = MULTI_A_STAR;
        }

        runRace(problem, movingTargetAlgorithm);
    }

    private static void runRace(MovingTargetSearchProblem problem, int movingTargetAlgorithm) {
        int thiefTime = 0; // time until the thief is at the next node
        int policeTime = 0; // time until the police is at the next node
        boolean catched = false; // indicates if the police is on the same node as the thief
        Node thiefNext = null; // the node towards which the thief is heading
        Node policeLast = null; // the node where the police comes from
        Node policeTarget = null; //the node towards the police drives right now
        List<Edge> policePath = new ArrayList<>(); // the path for the police
        int policePathPosition = 0;
        int thiefPathPosition = 0; // th index of the node towards which the thief is heading in the thief path
        List<Node> thiefRealPath = new ArrayList<>(); // list to collect the nodes that thief really visits
        List<Node> policeRealPath = new ArrayList<>(); // list to collect the nodes that police really visits

        //get graph
        Graph graph = problem.getGraph();
        Logger.log(graph.toString());

        //get start positions
        Node thiefPosition = problem.getTargetStart();
        Logger.log(thiefPosition.toString());
        Node policePosition = problem.getSearchStart();
        Logger.log(policePosition.toString());
        Node thiefTarget = problem.getTargetTarget();

        // calculate path for thief
        SearchSolver solver = new AStar();
        solver.initialize(graph, thiefTarget, thiefPosition, new ExpandCounter() {
            @Override
            public void countNodeExpand() {
                thiefExpandCounter++;
            }
        });
        List<Edge> thiefPath = null;
        try {
            thiefPath = solver.getPath();
            Logger.log("---- thief path -----");
            for (Edge e : thiefPath) {
                Logger.log(e.getNodeB().toString());
            }
            Logger.log("---- --------- -----");
        } catch (NoPathFoundException e) {
            throw new Error("no path");
        }

        MovingTargetSearchSolver policeSolver = null;
        if (movingTargetAlgorithm == MTD_STAR) {
            policeSolver = new MTDStar();
        } else if (movingTargetAlgorithm == MULTI_A_STAR) {
            policeSolver = new MultipleAStar();
        }
        policeSolver.initialize(graph, thiefPosition, policePosition, new ExpandCounter() {
            @Override
            public void countNodeExpand() {
                policeExpandCounter++;
            }
        });

        //start the simulation
        Logger.log("------- start catch --------");
        while (!catched && thiefPosition != thiefTarget) {
            if (thiefTime == 0) {
                //thief is at next node -> move to the next node in path and update variables
                if (thiefTarget.equals(thiefNext)) {
                    thiefPosition = thiefNext;
                } else {
                    thiefPosition = thiefPath.get(thiefPathPosition).getNodeA();
                    thiefRealPath.add(thiefPosition);
                    thiefNext = thiefPath.get(thiefPathPosition).getNodeB();
                    //simulate traffic
                    double traffic = Utils.normalValue(THIEF_TRAFFIC_MEAN, STANDARD_DERIVATION);
                    Logger.log("thief traffic: " + traffic);
                    thiefTime = (int) (thiefPath.get(thiefPathPosition).getWeight() * traffic);
                    thiefPathPosition++;
                    Logger.log("thief is between " + thiefPosition + " and " + thiefNext + " (" + thiefTime + ")");
                }

                if (thiefNext != null && thiefNext.equals(policeLast) && policePosition.equals(thiefPosition)) {
                    // terminate loop
                    catched = true;
                    continue;
                }
            }
            if (policeTime == 0) {
                policeRealPath.add(policePosition);
                //police is at next node -> check if its the node towards thief is heading
                if (policePosition.equals(thiefNext)) {
                    // terminate loop
                    catched = true;
                    continue;
                }
                try {
                    //calculate new path for police
                    //only call algorithm if target changed
                    if (!thiefNext.equals(policeTarget)) {
                        policePathPosition = 0;
                        policePath = policeSolver.moveTarget(thiefNext, policePosition);
                    } else {
                        policePathPosition++;
                    }
                    policeLast = policePosition;
                    policePosition = policePath.get(policePathPosition).getNodeB();
                    //simulate traffic
                    double traffic = Utils.normalValue(POLICE_TRAFFIC_MEAN, STANDARD_DERIVATION);
                    Logger.log("police traffic: " + traffic);
                    policeTime = (int) (policePath.get(policePathPosition).getWeight() * traffic);
                    Logger.log("police drives to " + policePosition + " (" + policeTime + ")");
                } catch (NoPathFoundException e) {
                    throw new Error("no path");
                }
            }

            //simulate time
            policeTime--;
            thiefTime--;
        }

        if (thiefNext == thiefTarget) {
            Logger.log("thief escaped");
        }
        if (catched) {
            Logger.log("thief got catched");
        }

        Logger.log("---- thief nodes: -----");
        for (Node n : thiefRealPath) {
            Logger.log(n.toString());
        }

        Logger.log("---- police nodes: -----");
        for (Node n : policeRealPath) {
            Logger.log(n.toString());
        }

        Logger.log("thief expanded " + thiefExpandCounter + " nodes");
        Logger.log("police expanded " + policeExpandCounter + " nodes");
    }
}
