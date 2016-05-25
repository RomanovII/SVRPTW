package tabu;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.coinor.opents.*;

import svrptw.*;

@SuppressWarnings("serial")
public class MySearchProgram implements TabuSearchListener {
	private Instance instance = Instance.getInstance();
	private static int iterationsDone;
	public TabuSearch tabuSearch;
	private MySolution sol;
	public double[] feasibleObj;
	public ArrayList<Route> feasibleRoutes; // stores the routes of the feasible
											// solution if any
	public Cost feasibleCost; // stores the total cost of feasible solution if
								// any, otherwise totalcostviol =
								// Double.Infinity
	public ArrayList<Route> bestRoutes; // stores the routes of with the best
										// travel time
	public Cost bestCost; // stores the total cost of best travel time solution
	public ArrayList<Route> currentRoutes; // stores the routes of the current
											// solution
	public Cost currentCost; // stores the total cost of current solution
	public int feasibleIndex;
	public int bestIndex;
	public int numberFeasibleSol;
	public MyMoveManager manager;
	public DecimalFormat df = new DecimalFormat("#.##");
	public int counter;
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
	@Override
	public void newBestSolutionFound(TabuSearchEvent event) {
		sol = ((MySolution) tabuSearch.getBestSolution());
		bestCost = sol.getCost();
		bestRoutes = cloneRoutes(sol.getRoutes());
		bestIndex = tabuSearch.getIterationsCompleted() + 1; // plus the current
																// one
		System.out.println("Best Sol: " + sol.getObjectiveValue()[0]);
		System.out.println("   ---->  " + sol.getObjectiveValue()[1]);
		// System.out.println("     -->  " + sol.getObjectiveValue()[2]);
		// System.out.println("      ->  " + sol.getObjectiveValue()[3]);
		System.out.println("    --->  " + sol.getCost().getDistance());
		System.out.println("     -->  " + sol.coefNu);
		System.out.println("      ->  " + sol.capacityViol);
	}

	/**
	 * when a new current solution is triggered do the following: - update the
	 * parameters alpha, beta, gamma - check to see if a new better feasible
	 * solution is found - if graphics is visible update panel components and
	 * repaint
	 */
	@Override
	public void newCurrentSolutionFound(TabuSearchEvent event) {
		sol = ((MySolution) tabuSearch.getCurrentSolution());
		MySolution bestSol = ((MySolution) tabuSearch.getBestSolution());
		currentCost = sol.getCost();
		MySearchProgram.iterationsDone += 1;
		if (sol.isFeasible() && sol.getObjectiveValue()[0] <= feasibleObj[0]
				&& sol.getObjectiveValue()[1] < feasibleObj[1]) {
			feasibleObj = sol.getObjectiveValue();
			feasibleCost = currentCost;
			feasibleRoutes = cloneRoutes(sol.getRoutes());
			// set the new best to the current one
			tabuSearch.setBestSolution(sol);

			externalListener.newBestSolutionFound(sol.getRoutes(),
					Double.toString(sol.getCost().getDistance()));
			System.out.println("Best: ");
			for (int i = 0; i < sol.getRoutesNr(); ++i) {
				System.out.println(sol.getRoute(i).printRoute());
			}
			numberFeasibleSol++;
		}
		sol.updateParameters();
	}

	@Override
	public void noChangeInValueMoveMade(TabuSearchEvent event) {
		sol = ((MySolution) tabuSearch.getCurrentSolution());
		sol.setObjectiveValue(new double[] {sol.getObjectiveValue()[0] * 1.2, sol.getObjectiveValue()[1]});
	}

	/**
	 * When tabu search starts initialize best cost and routes and feasible cost
	 * and routes and also if graphics enabled initialize them and print the
	 * initial route
	 */
	@Override
	public void tabuSearchStarted(TabuSearchEvent event) {
		sol = ((MySolution) tabuSearch.getCurrentSolution());
		// initialize the feasible and best cost with the initial solution
		// objective value
		bestCost = sol.getCost();
		feasibleCost = bestCost;
		feasibleObj = sol.getObjectiveValue();
		if (!sol.isFeasible()) {
			feasibleCost.setTotalCost(Double.POSITIVE_INFINITY);
		}
		feasibleRoutes = cloneRoutes(sol.getRoutes());
		bestRoutes = feasibleRoutes;
	}

	@Override
	public void tabuSearchStopped(TabuSearchEvent event) {
		sol = ((MySolution) tabuSearch.getBestSolution());
		if (feasibleCost.getTotalCost() != Double.POSITIVE_INFINITY) {
			sol.setCost(feasibleCost);
			sol.setRoutes(feasibleRoutes);
			tabuSearch.setBestSolution(sol);
		}
		System.out.println("END: ");
		for (int i = 0; i < sol.getRoutesNr(); ++i) {
			System.out.println(sol.getRoute(i).printRoute());
		}
	}

	@Override
	public void unimprovingMoveMade(TabuSearchEvent event) {
		counter++;
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

	public int getNumberFeasibleSol() {
		return numberFeasibleSol;
	}

	public void setNumberFeasibleSol(int numberFeasibleSol) {
		this.numberFeasibleSol = numberFeasibleSol;
	}
}
