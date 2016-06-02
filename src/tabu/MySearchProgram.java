package tabu;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.coinor.opents.*;

import svrptw.*;

/**
 * @author   Ilya
 */
@SuppressWarnings("serial")
public class MySearchProgram implements TabuSearchListener {
	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private static int iterationsDone;
	/**
	 * @uml.property  name="tabuSearch"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	public TabuSearch tabuSearch;
	/**
	 * @uml.property  name="sol"
	 * @uml.associationEnd  readOnly="true"
	 */
	private MySolution sol;
	/**
	 * @uml.property  name="feasibleObj"
	 */
	public double[] feasibleObj;
	/**
	 * @uml.property  name="feasibleRoutes"
	 */
	public ArrayList<Route> feasibleRoutes; // stores the routes of the feasible
											// solution if any
	/**
	 * @uml.property  name="feasibleCost"
	 * @uml.associationEnd  readOnly="true"
	 */
	public Cost feasibleCost; // stores the total cost of feasible solution if
								// any, otherwise totalcostviol =
								// Double.Infinity
	/**
	 * @uml.property  name="bestRoutes"
	 */
	public ArrayList<Route> bestRoutes; // stores the routes of with the best
										// travel time
	/**
	 * @uml.property  name="bestCost"
	 * @uml.associationEnd  readOnly="true"
	 */
	public Cost bestCost; // stores the total cost of best travel time solution
	/**
	 * @uml.property  name="currentRoutes"
	 */
	public ArrayList<Route> currentRoutes; // stores the routes of the current
											// solution
	/**
	 * @uml.property  name="currentCost"
	 * @uml.associationEnd  readOnly="true"
	 */
	public Cost currentCost; // stores the total cost of current solution
	/**
	 * @uml.property  name="feasibleIndex"
	 */
	public int feasibleIndex;
	/**
	 * @uml.property  name="bestIndex"
	 */
	public int bestIndex;
	/**
	 * @uml.property  name="numberFeasibleSol"
	 */
	public int numberFeasibleSol;
	/**
	 * @uml.property  name="manager"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	public MyMoveManager manager;
	/**
	 * @uml.property  name="df"
	 */
	public DecimalFormat df = new DecimalFormat("#.##");
	/**
	 * @uml.property  name="counter"
	 */
	public int counter;
	/**
	 * @uml.property  name="externalListener"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	public MethodListener externalListener;

	public MySearchProgram(Solution initialSol, MoveManager moveManager,
			ObjectiveFunction objFunc, TabuList tabuList, boolean minmax,
			PrintStream outPrintStream, MethodListener listener) {
		externalListener = listener;
		tabuSearch = new SingleThreadedTabuSearch(initialSol, moveManager,
				objFunc, tabuList, new BestEverAspirationCriteria(), minmax);
		feasibleIndex = -1;
		feasibleObj = new double[] { Double.MAX_VALUE, Double.MAX_VALUE };
		bestIndex = 0;
		numberFeasibleSol = 0;
		MySearchProgram.setIterationsDone(0);
		tabuSearch.addTabuSearchListener(this);
		this.manager = (MyMoveManager) moveManager;
	}

	public void improvingMoveMade(TabuSearchEvent event) {
	}

	/**
	 * when a new best solution event occur save and print it
	 */
	public void newBestSolutionFound(TabuSearchEvent event) {
		sol = ((MySolution) tabuSearch.getBestSolution());
		externalListener.newBestSolutionFound(sol.getRoutes(),
				"BE " + Double.toString(sol.getCost().getDistance()));
//		bestCost = sol.getCost();
//		bestRoutes = cloneRoutes(sol.getRoutes());
//		bestIndex = tabuSearch.getIterationsCompleted() + 1; // plus the current
//																// one
//		System.out.println("Best Sol: " + sol.getObjectiveValue()[0]);
//		System.out.println("   ---->  " + sol.getObjectiveValue()[1]);
//		// System.out.println("     -->  " + sol.getObjectiveValue()[2]);
//		// System.out.println("      ->  " + sol.getObjectiveValue()[3]);
//		System.out.println("    --->  " + sol.getCost().getDistance());
//		System.out.println("     -->  " + sol.coefNu);
//		System.out.println("      ->  " + sol.capacityViol);
	}

	/**
	 * when a new current solution is triggered do the following: - update the
	 * parameters alpha, beta, gamma - check to see if a new better feasible
	 * solution is found - if graphics is visible update panel components and
	 * repaint
	 */
	public void newCurrentSolutionFound(TabuSearchEvent event) {
		sol = ((MySolution) tabuSearch.getCurrentSolution());
////		MySolution bestSol = ((MySolution) tabuSearch.getBestSolution());
//		currentCost = sol.getCost();
//		MySearchProgram.iterationsDone += 1;
//		if (sol.isFeasible() && sol.getObjectiveValue()[0] <= feasibleObj[0]
//				&& sol.getObjectiveValue()[1] < feasibleObj[1]) {
//			feasibleObj = sol.getObjectiveValue();
//			feasibleCost = currentCost;
//			feasibleRoutes = cloneRoutes(sol.getRoutes());
//			// set the new best to the current one
//			tabuSearch.setBestSolution(sol);
//
//			externalListener.newBestSolutionFound(sol.getRoutes(),
//					Double.toString(sol.getCost().getDistance()));
			System.out.println("Best: ");
			for (int i = 0; i < sol.getRoutesNr(); ++i) {
				System.out.println(sol.getRoute(i).printRoute());
			}
//			numberFeasibleSol++;
//		}
		externalListener.newBestSolutionFound(sol.getRoutes(),
				"CU " + Double.toString(sol.getCost().getDistance()));
		System.out.println(sol.getObjectiveValue()[0] + " " + sol.capacityViol + " " + sol.vechile + " " + sol.coefNu);
		sol.updateParameters();
		System.out.println(sol.getObjectiveValue()[0] + " " + sol.capacityViol + " " + sol.vechile + " " + sol.coefNu);
	}

	public void noChangeInValueMoveMade(TabuSearchEvent event) {
//		sol = ((MySolution) tabuSearch.getCurrentSolution());
//		sol.setObjectiveValue(new double[] { sol.getObjectiveValue()[0] * 1.2,
//				sol.getObjectiveValue()[1] });
	}

	/**
	 * When tabu search starts initialize best cost and routes and feasible cost
	 * and routes and also if graphics enabled initialize them and print the
	 * initial route
	 */
	public void tabuSearchStarted(TabuSearchEvent event) {
//		sol = ((MySolution) tabuSearch.getCurrentSolution());
//		// initialize the feasible and best cost with the initial solution
//		// objective value
//		bestCost = sol.getCost();
//		feasibleCost = bestCost;
//		feasibleObj = sol.getObjectiveValue();
//		feasibleRoutes = cloneRoutes(sol.getRoutes());
//		bestRoutes = feasibleRoutes;
	}

	public void tabuSearchStopped(TabuSearchEvent event) {
		sol = ((MySolution) tabuSearch.getBestSolution());
		externalListener.newBestSolutionFound(sol.getRoutes(),
				"END " + Double.toString(sol.getCost().getDistance()));
//		sol = ((MySolution) tabuSearch.getBestSolution());
//		sol.setCost(feasibleCost);
//		sol.setRoutes(feasibleRoutes);
//		tabuSearch.setBestSolution(sol);
//		System.out.println("END: ");
//		for (int i = 0; i < sol.getRoutesNr(); ++i) {
//			System.out.println(sol.getRoute(i).printRoute());
//		}
	}

	public void unimprovingMoveMade(TabuSearchEvent event) {
//		counter++;
	}

	// clone the routes passed as parameter
	public ArrayList<Route> cloneRoutes(ArrayList<Route> routes) {
		ArrayList<Route> routescopy = new ArrayList<Route>();
		for (int i = 0; i < routes.size(); ++i)
			routescopy.add(new Route(routes.get(i)));
		return routescopy;
	}

	/**
	 * @return the iterationsDone
	 */
	public static int getIterationsDone() {
		return iterationsDone;
	}

	/**
	 * @param iterationsDone
	 *            the iterationsDone to set
	 */
	public static void setIterationsDone(int iterationsDone) {
		MySearchProgram.iterationsDone = iterationsDone;
	}

	public ArrayList<Route> getFeasibleRoutes() {
		return feasibleRoutes;
	}

	public MySolution getSolution() {
		return sol;
	}

	public void setFeasibleRoutes(ArrayList<Route> feasibleRoutes) {
		this.feasibleRoutes = feasibleRoutes;
	}

	/**
	 * @return
	 * @uml.property  name="numberFeasibleSol"
	 */
	public int getNumberFeasibleSol() {
		return numberFeasibleSol;
	}

	/**
	 * @param  numberFeasibleSol
	 * @uml.property  name="numberFeasibleSol"
	 */
	public void setNumberFeasibleSol(int numberFeasibleSol) {
		this.numberFeasibleSol = numberFeasibleSol;
	}
}
