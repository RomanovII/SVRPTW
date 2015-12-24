package svrptw;

import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;

import org.coinor.opents.SimpleTabuList;
import org.coinor.opents.TabuList;
import org.coinor.opents.TabuSearch;

import tabu.MyMoveManager;
import tabu.MyObjectiveFunction;
import tabu.MySearchProgram;
import tabu.MySolution;
import tabu.MyTabuList;
import svrptw.Test;
import heneticmethod.*;

public class SVRPTW {
	public SVRPTW(Matrix matrix) {
		MySearchProgram search;
		MySolution initialSol;
		MyObjectiveFunction objFunc;
		MyMoveManager moveManager;
		TabuList tabuList;
		Parameters parameters = new Parameters(); // holds all the parameters
													// passed from the input
													// line
		Instance instance = Instance.getInstance();
		
		Duration duration = new Duration(); // used to calculate the elapsed
											// time
		PrintStream outPrintSream = null; // used to redirect the output

		duration.start();

		// get the instance from the file
		instance.populate(matrix);

		// Init memory for Tabu Search
		initialSol = new MySolution();
		initialSol.initializeRoutes();
		initialSol.buildInitRoutes();
		objFunc = new MyObjectiveFunction();
		moveManager = new MyMoveManager();
//		// Tabu list
//		// First is depotNr
		int dimension[] = { 1, instance.getVehiclesNr(), instance.getCustomersNr(), 1, 1 };
		tabuList = new SimpleTabuList(instance.getParameters().getTenure());
//		// Create Tabu Search object
		search = new MySearchProgram(initialSol, moveManager, objFunc, tabuList, false, outPrintSream);
//		// Start solving
		search.tabuSearch.setIterationsToGo(instance.getParameters().getIterations());
		search.tabuSearch.startSolving();
		duration.stop();
//		initialSol.getCost().setC();
//		objFunc = new MyObjectiveFunction(instance, false);
//		search = new MySearchProgram(instance, search.getSolution(), moveManager, objFunc, tabuList, false,
//				outPrintSream, false);
//		// Start solving
//		search.tabuSearch.setIterationsToGo(parameters.getIterations());
//		search.tabuSearch.startSolving();
//
//		// Count routes
//		int routesNr = 0;
//		ArrayList<Route> list = search.getFeasibleRoutes();
//		System.out.println("End.");
//		System.out.println();
//		for (int i = 0; i < list.size(); ++i)
//			if (list.get(i).getCustomersLength() > 0) {
//				routesNr++;
//				System.out.println(list.get(i).printRoute());
//			}
//		System.out.println("Count of routes: " + routesNr);
//		System.out.printf("Total cost: %.2f", search.getSolution().getCost().totalWeightedCost);
//		System.out.printf("\n%.2f", search.getSolution().getCost().travelTime);

	}
	
	public static void main(String[] args) {
    	Test test = new Test();
    	test.testS();
    	new SVRPTW(test.m);
	}
}
