package svrptw;

import java.io.Console;
import java.io.FileWriter;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

import org.coinor.opents.SimpleTabuList;
import org.coinor.opents.TabuList;
import org.coinor.opents.TabuSearch;
import org.coinor.opents.TabuSearchListener;

import tabu.MyMoveManager;
import tabu.MyObjectiveFunction;
import tabu.MySearchProgram;
import tabu.MySolution;
import tabu.MyTabuList;
import heneticmethod.*;

public class SVRPTW extends Thread{
	MethodListener listener;
	Instance instance = Instance.getInstance();
	
	public SVRPTW(Matrix matrix, MethodListener listener) {
		this.listener = listener;
		this.instance.populate(matrix);
	}
	
	public SVRPTW(Matrix matrix) {
		this.listener = null;
		this.instance.populate(matrix);
	}
	
	public void run() {
		Duration duration = new Duration(); // used to calculate the elapsed time
		duration.start();
		
		MySolution initialSol = new MySolution();
		initialSol.initializeRoutes();
		initialSol.startSolution();
		//initialSol.buildInitRoutes();
		
		if (this.listener != null) {
			listener.newBestSolutionFound(initialSol.getRoutes(), "Step 1");
		}
		// Create Tabu Search object
		MySearchProgram search = new MySearchProgram(
				initialSol, 
				new MyMoveManager(), 
				new MyObjectiveFunction(), 
				new SimpleTabuList(instance.getTenure()), 
				false, 
				null, 
				listener
				);
		
		// Start solving
		search.tabuSearch.setIterationsToGo(instance.getParameters().getIterations());
		search.tabuSearch.startSolving();
		System.out.println("END");
		duration.stop();
		
		long nanos = ManagementFactory.getThreadMXBean().getThreadCpuTime(Thread.currentThread().getId());
		System.out.println("It's nanosecond:" + nanos);
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
//		list = search.getFeasibleRoutes();
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
	
//	public static void main(String[] args) {
//    	Test test = new Test();
//    	test.testS();
//    	new SVRPTW(test.m);
//	}
}
