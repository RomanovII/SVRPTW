package svrptw;

import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;

import org.coinor.opents.TabuList;

import tabu.MovesType;
import tabu.MyMoveManager;
import tabu.MyObjectiveFunction;
import tabu.MySearchProgram;
import tabu.MySolution;
import tabu.MyTabuList;

public class SVRPTW {
	public static void main(String[] args) {
		MySearchProgram     search;
		MySolution          initialSol;
		MyObjectiveFunction objFunc;
		MyMoveManager       moveManager;
		TabuList            tabuList;
		Parameters          parameters 		= new Parameters(); 	// holds all the parameters passed from the input line
		Instance            instance; 								// holds all the problem data extracted from the input file
		Duration            duration 		= new Duration(); 		// used to calculate the elapsed time
		PrintStream         outPrintSream 	= null;					// used to redirect the output

		try {			
			// check to see if an input file was specified
			parameters.updateParameters(args);
			if(parameters.getInputFileName() == null){
				System.out.println("You must specify an input file name");
				return;
			}
			duration.start();
			// get the instance from the file			
			instance = new Instance(parameters); 
			instance.populateFromFile(parameters.getInputFileName());
			// Init memory for Tabu Search
			initialSol 		= new MySolution(instance);
			objFunc 		= new MyObjectiveFunction(instance, true);
			moveManager 	= new MyMoveManager(instance);
			moveManager.setMovesType(parameters.getMovesType());
			// Tabu list
			//First is depotNr
			int dimension[] = {1, instance.getVehiclesNr(), instance.getCustomersNr(), 1, 1};
			tabuList 		= new MyTabuList(parameters.getTabuTenure(), dimension, instance);
			// Create Tabu Search object
			search 			= new MySearchProgram(instance, initialSol, moveManager, objFunc, tabuList, false, outPrintSream, true);
			// Start solving        
			search.tabuSearch.setIterationsToGo(parameters.getIterations());
			search.tabuSearch.startSolving();
			duration.stop();
			initialSol.getCost().setC();
			objFunc 		= new MyObjectiveFunction(instance, false);
			search 			= new MySearchProgram(instance, search.getSolution(), moveManager, objFunc, tabuList, false, outPrintSream, false);
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
			System.out.printf("\n%.2f", search.getSolution().getCost().travelTime);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
