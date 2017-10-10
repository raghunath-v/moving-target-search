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
import parser.OutputFileWriter;
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

    private static int policeExpandCounter; //counts expanded nodes in police search algorithm
    private static int thiefExpandCounter; //counts expanded nodes in thief search algorithm

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

        //run simulation
        runRace(problem, movingTargetAlgorithm);
    }

    private static void runRace(MovingTargetSearchProblem problem, int movingTargetAlgorithm) {
        //setup output file writer to write in a txt file
        OutputFileWriter output = new OutputFileWriter("output_" + System.currentTimeMillis());

        int thiefTime = 0; // time until the thief is at the next node
        int policeTime = 0; // time until the police is at the next node
        boolean catched = false; // indicates if the police is on the same node as the thief
        Node thiefNext = null; // the node towards which the thief is heading
        Node policeLast = null; // the node where the police comes from
        Node policeTarget = null; //the node towards the police drives right now
        List<Edge> policePath = new ArrayList<>(); // the path for the police
        int policePathPosition = 0; // the index of the node towards which the police is heading in the police path
        int thiefPathPosition = 0; // the index of the node towards which the thief is heading in the thief path
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

        //write start positions in output file
        output.addLine("P " + policePosition.getIndex());
        output.addLine("T " + thiefPosition.getIndex());
        output.addLine("TT " + thiefTarget.getIndex());

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

            //output path
            Logger.log("---- thief path -----");
            output.addLine((thiefPath.size() + 1) + "");
            output.addLine(thiefPosition.getIndex() + "");
            for (Edge e : thiefPath) {
                Logger.log(e.getNodeB().toString());
                output.addLine(e.getNodeB().getIndex() + "");
            }
            Logger.log("---- --------- -----");
        } catch (NoPathFoundException e) {
            throw new Error("no path");
        }

        //determine the moving target search algorithm to use
        MovingTargetSearchSolver policeSolver = null;
        if (movingTargetAlgorithm == MTD_STAR) {
            policeSolver = new MTDStar();
        } else if (movingTargetAlgorithm == MULTI_A_STAR) {
            policeSolver = new MultipleAStar();
        }

        //initialize moving target solver
        policeSolver.initialize(graph, thiefPosition, policePosition, new ExpandCounter() {
            @Override
            public void countNodeExpand() {
                policeExpandCounter++;
            }
        });

        //start the simulation
        Logger.log("------- start catch --------");
        //if thiefPosition == thiefTarget -> thief is at its goal node and thus escaped
        while (!catched && thiefPosition != thiefTarget) {
            if (thiefTime == 0) {
                //thief is at next node -> move to the next node in path and update variables
                if (thiefTarget.equals(thiefNext)) {
                    //thief is at last node
                    thiefPosition = thiefNext;
                } else {
                    //get the next node in the thief path and set the thief position variables
                    thiefPosition = thiefPath.get(thiefPathPosition).getNodeA();
                    output.addLine("t " + thiefPosition.getIndex());
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
                    // thief and police are driving towards each other in the same edge -> terminate loop
                    output.addLine("cb " + thiefPosition.getIndex() + " " + thiefNext.getIndex());
                    catched = true;
                    continue;
                }
            }
            if (policeTime == 0) {
                output.addLine("p " + policePosition.getIndex());
                policeRealPath.add(policePosition);
                //police is at next node -> check if its the node towards thief is heading
                if (policePosition.equals(thiefNext)) {
                    // terminate loop
                    output.addLine("ca " + policePosition.getIndex());
                    catched = true;
                    continue;
                }
                try {
                    //calculate new path for police
                    //only call algorithm if target changed
                    if (!thiefNext.equals(policeTarget)) {
                        policePathPosition = 0;
                        policePath = policeSolver.moveTarget(thiefNext, policePosition);

                        //write new path to output file
                        output.addLine("pp " + (policePath.size() + 1));
                        output.addLine("" + policePosition.getIndex());
                        for (Edge e : policePath) {
                            output.addLine("" + e.getNodeB().getIndex());
                        }

                        policeTarget = thiefNext;
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
            output.addLine("e");
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

        //write all lines in the output writer to a output txt file
        output.writeToFile();
    }
}
