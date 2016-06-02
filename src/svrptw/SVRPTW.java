package svrptw;

//import java.io.Console;
//import java.io.FileWriter;
//import java.io.PrintStream;
import java.lang.management.ManagementFactory;
//import java.util.ArrayList;

import org.coinor.opents.SimpleTabuList;

import tabu.MyMoveManager;
import tabu.MyObjectiveFunction;
import tabu.MySearchProgram;
import tabu.MySolution;
import heneticmethod.*;

/**
 * @author   Ilya
 */
public class SVRPTW extends Thread {
	/**
	 * @uml.property  name="listener"
	 * @uml.associationEnd  
	 */
	MethodListener listener;
	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
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
		Duration duration = new Duration(); // used to calculate the elapsed
											// time
		duration.start();
		double[][] initArr = {
				{1.00, 0.00, 0.00},
				{0.00, 1.00, 0.00},
				{0.50, 0.50, 0.00},
				{0.80, 0.00, 0.20},
				{0.00, 0.80, 0.20},
				{0.40, 0.40, 0.20},
				{0.60, 0.00, 0.40},
				{0.00, 0.60, 0.40},
				{0.30, 0.30, 0.40},
				{0.40, 0.00, 0.60},
				{0.00, 0.40, 0.60},
				{0.20, 0.20, 0.60},
				{0.20, 0.00, 0.80},
				{0.00, 0.20, 0.80},
				{0.10, 0.10, 0.80},
				{0.10, 0.00, 0.90},
				{0.00, 0.10, 0.90},
				{0.05, 0.05, 0.90},
				{0.00, 0.00, 1.00}
		};
		MySolution initialSol = new MySolution();
//		for (int i = initArr.length - 1; i >= 0; --i) {
//			initialSol = new MySolution();
//			initialSol.initializeRoutes();
//			initialSol.startSolution(initArr[i][0], initArr[i][1], initArr[i][2], listener);
//		}
		initialSol = new MySolution();
		initialSol.initializeRoutes();
		initialSol.startSolution(initArr[0][0], initArr[0][1], initArr[0][2], listener);
		// Create Tabu Search object
		MySearchProgram search = new MySearchProgram(initialSol,
				new MyMoveManager(), new MyObjectiveFunction(),
				new SimpleTabuList(instance.getTenure()), false, System.out,
				listener);

		// Start solving
		search.tabuSearch.setIterationsToGo(instance.getParameters()
				.getIterations());
		search.tabuSearch.startSolving();
		System.out.println("END");
		duration.stop();

		long nanos = ManagementFactory.getThreadMXBean().getThreadCpuTime(
				Thread.currentThread().getId());
		System.out.println("It's nanosecond:" + nanos);
		// initialSol.getCost().setC();
		// objFunc = new MyObjectiveFunction(instance, false);
		// search = new MySearchProgram(instance, search.getSolution(),
		// moveManager, objFunc, tabuList, false,
		// outPrintSream, false);
		// // Start solving
		// search.tabuSearch.setIterationsToGo(parameters.getIterations());
		// search.tabuSearch.startSolving();
		//
		// // Count routes
		// int routesNr = 0;
		// list = search.getFeasibleRoutes();
		// System.out.println("End.");
		// System.out.println();
		// for (int i = 0; i < list.size(); ++i)
		// if (list.get(i).getCustomersLength() > 0) {
		// routesNr++;
		// System.out.println(list.get(i).printRoute());
		// }
		// System.out.println("Count of routes: " + routesNr);
		// System.out.printf("Total cost: %.2f",
		// search.getSolution().getCost().totalWeightedCost);
		// System.out.printf("\n%.2f",
		// search.getSolution().getCost().travelTime);

	}

	// public static void main(String[] args) {
	// Test test = new Test();
	// test.testS();
	// new SVRPTW(test.m);
	// }
}
