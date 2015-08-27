package com.vrp;

import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;

import org.coinor.opents.TabuList;

import com.TabuSearch.MovesType;
import com.TabuSearch.MyMoveManager;
import com.TabuSearch.MyObjectiveFunction;
import com.TabuSearch.MySearchProgram;
import com.TabuSearch.MySolution;
import com.TabuSearch.MyTabuList;
import heneticmethod.*;

public class SVRPTW implements SolutionRoutes {
	MySearchProgram search;
	MySolution initialSol;
	MyObjectiveFunction objFunc;
	MyMoveManager moveManager;
	TabuList tabuList;
	Parameters parameters = new Parameters(); // holds all the parameters passed
												// from the input line
	Instance instance; // holds all the problem data extracted from the input
						// file
	Duration duration = new Duration(); // used to calculate the elapsed time
	PrintStream outPrintSream = null; // used to redirect the output

	public SVRPTW(Matrix matrix, int currentRidersCount) {

		duration.start();
		// get the instance from the file
		instance = new Instance(parameters);
		instance.populateFromFile(matrix, currentRidersCount);
		// Init memory for Tabu Search
		initialSol = new MySolution(instance);
		objFunc = new MyObjectiveFunction(instance, true);
		moveManager = new MyMoveManager(instance);
		moveManager.setMovesType(parameters.getMovesType());
		// Tabu list
		// First is depotNr
		int dimension[] = { 1, instance.getVehiclesNr(), instance.getCustomersNr(), 1, 1 };
		tabuList = new MyTabuList(parameters.getTabuTenure(), dimension, instance);
		// Create Tabu Search object
		search = new MySearchProgram(instance, initialSol, moveManager, objFunc, tabuList, false, outPrintSream, true);
		// Start solving
		search.tabuSearch.setIterationsToGo(parameters.getIterations());
		search.tabuSearch.startSolving();
		duration.stop();
		initialSol.getCost().setC();
		objFunc = new MyObjectiveFunction(instance, false);
		search = new MySearchProgram(instance, search.getSolution(), moveManager, objFunc, tabuList, false,
				outPrintSream, false);
		// Start solving
		search.tabuSearch.setIterationsToGo(parameters.getIterations());
		search.tabuSearch.startSolving();

		// Count routes
		int routesNr = 0;
		ArrayList<Route> list = search.getFeasibleRoutes();
		System.out.println("End.");
		System.out.println();
		for (int i = 0; i < list.size(); ++i)
			if (list.get(i).getCustomersLength() > 0) {
				routesNr++;
				System.out.println(list.get(i).printRoute());
			}
		System.out.println("Count of routes: " + routesNr);
		System.out.printf("Total cost: %.2f", search.getSolution().getCost().totalWeightedCost);
		// Print results
		/*
		 * String outSol = String.format("%s; %5.2f; %d; %4d\r\n",
		 * instance.getParameters().getInputFileName(),
		 * search.feasibleCost.total,
		 * duration.getMinutes()*60+duration.getSeconds(), routesNr);
		 * System.out.println(outSol); /* FileWriter fw = new
		 * FileWriter(parameters.getOutputFileName(),true); fw.write(outSol);
		 * fw.close();
		 */
	}
	
	public ArrayList<? extends ArrayList<Integer>> getRoutes() {
		ArrayList<ArrayList<Integer>> aRoutes = new ArrayList<ArrayList<Integer>>();
		ArrayList<Route> list = search.getFeasibleRoutes();
		for (Route route : list) {
			if (route.getCustomersLength() > 0){
				ArrayList<Integer> aRoute = new ArrayList<Integer>();
				for (Customer customer : route.getCustomers()){
					aRoute.add(customer.getNumber());
				}
				aRoutes.add(aRoute);
			}
		}
		return aRoutes;
	}
}
