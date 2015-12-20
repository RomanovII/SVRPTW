package svrptw;

import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;

import org.coinor.opents.SimpleTabuList;
import org.coinor.opents.TabuList;

import tabu.MovesType;
import tabu.MyMoveManager;
import tabu.MyObjectiveFunction;
import tabu.MySearchProgram;
import tabu.MySolution;
import tabu.MyTabuList;
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
		initialSol.buildInitialRoutes();
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
    	Matrix m = new Matrix(4);
    	m.ENC = new int[]{0, 1, 3, 2};  //(идентификаторы инкассаций)
    	m.ATM = new String[]{"1", "132964", "155652", "155750"}; //(соотв. идентификаторы банкоматов)
    	m.distanceCoeffs = new int[][]{{0, 17, 39, 19}, {18, 0, 26, 33}, {42, 29, 0, 67}, {20, 33, 66, 0}}; 
    	m.timeCoeffs = new int[][]{{0, 17, 39, 19},{18, 0, 26, 33},{42, 29, 0, 67},{20, 33, 66, 0}}; 
    	m.addTimeWindow(0, 0, 10000, false);
    	m.addTimeWindow(1, 480, 840, false);
    	m.addTimeWindow(2, 600, 1080, false);
    	m.addTimeWindow(3, 480, 840, false);
    	m.addRiderTimeWindow(60, 1380);
    	m.amountOfMoney = new int[]{0, 500000, 500000, 500000};  //(количество денег, которое нужно загрузить в каждый банкомат, может быть 0)
    	m.serviceTime = new int[]{20, 20, 20, 20}; 	 // - вместо этого можно просто int serviceTime (это время обслуживания одного банкомата, сейчас оно одно для всех, но в идеале может различаться для разных типов банкоматов и даже быть уникальным для каждого)
    	m.MaxMoney = 40000000;  // (макс количество денег, которое может перевезти машина)
    	m.amountOfCassettes = new int[] {0, 200, 200, 200};  // (кол-во кассет, которое нужно завезти в банкомат) - это сейчас не используется
    	m.VolumeOneCar = 40000000;  // (макс кол-во кассет, кот. может перевезти машина) - не используется
    	m.FixPrice = 100.0;  // - фикс стоимость подъезда машины к банкомату
    	m.LengthPrice = 20.0;  // - цена за километр пути
    	m.MaxATMInWay = 7; // - макс кол-во банкоматов в маршруте
    	m.MaxTime = 0;  // - макс время которое можно затрачивать на 1 маршрут
    	m.MaxLength = 0;  // макс длина одного маршрута
    	m.depot = "1";  // - идентификатор депо
    	m.maxCars = 5;  // - макс доступное кол-во машин
    	m.AtmPrice = new double[] {100.0, 100.0, 100.0, 100.0};  // - стоимость подъезда к каждому банкомату (сейчас не используется)
    	m.currCode = 810;  // - валюта всех денежных параметров
    	m.windowMode = 0;  // - режим окон для банкоматов, обычный и дефолтный (при котором, каждое окно ставится на максимально возможный промежуток);
    	new SVRPTW(m);
	}
}
