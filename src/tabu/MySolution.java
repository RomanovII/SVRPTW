package tabu;

//import java.util.Random;
import java.util.ArrayList;

import org.coinor.opents.SolutionAdapter;

import svrptw.Cost;
import svrptw.Customer;
import svrptw.Instance;
import svrptw.Route;
import svrptw.Vehicle;

@SuppressWarnings("serial")
public class MySolution extends SolutionAdapter {
	private Instance instance = Instance.getInstance();
	private ArrayList<Route> routes;
	private Cost cost;

	// public MySolution(){} //This is needed otherwise java gives random
	// errors.. YES we love java <3

	public MySolution() {
		cost = new Cost();
		routes = new ArrayList<Route>();
		initializeRoutes();
		buildInitialRoutes();
	}

	public void initializeRoutes() {
		int vehiclesNr = instance.getVehiclesNr();
		routes = new ArrayList<Route>(vehiclesNr);
		for (int j = 0; j < vehiclesNr; ++j) {
			Route r = new Route();
			r.setIndex(j);
			r.setDepot(instance.getDepot());
			Cost cost = new Cost();
			r.setCost(cost);
			Vehicle vehicle = new Vehicle();
			vehicle.setCapacity(instance.getVechileCapacity());
			r.setAssignedVehicle(vehicle);
			routes.add(r);
		}
	}

	// This is needed for tabu search
	public Object clone() { //WHAT?
		MySolution copy = (MySolution) super.clone();
		copy.cost = new Cost(this.cost);
		copy.routes = new ArrayList<Route>(this.routes);
		return copy;
	}
	
	public void updateParameters(double a, double b) {
		// capacity violation test
		if (a == 0) {
			alpha = alpha / (1 + delta);
		} else {
			alpha = alpha * (1 + delta);
			if(alpha > upLimit){
				alpha = resetValue;
			}
		}
		// time window violation test
		if (b == 0) {
			gamma = gamma / (1 + delta);
		} else {
			gamma = gamma * (1 + delta);
			if(gamma > upLimit){
				gamma = resetValue;
			}
		}
	}

	public void buildInitialRoutes() {
		ArrayList<Customer> unroutedCustomers = new ArrayList<Customer>(instance.getSortedCustomers());

		int routeIndex = 0;

		while (unroutedCustomers.size() > 0) {
			Route newRoute = routes.get(routeIndex);

			for (Customer cust : unroutedCustomers) {
				int pos = 0;
				while (pos <= newRoute.getCustomersLength() && !isFeasibleInsert(cust, pos, newRoute)) {
					++pos;
				}
				if (pos <= newRoute.getCustomersLength()) {
					newRoute.addCustomer(instance.getCustomers().get(cust.getNumber()), pos);
					evaluateRoute(newRoute);
				}
			}

			routes.set(routeIndex, newRoute);
			++routeIndex;
			System.out.println(newRoute.printRoute());
			System.out.println(newRoute.printRouteCost());
		}
		trimRoutes(this.routes);
	}

	private boolean isFeasibleInsert(Customer cust, int pos, Route route) { // WHAT?
		if (route.getCost().getCapacity() + cust.getCapacity() > route.getAssignedVehicle().getCapacity()) {
			return false;
		}
		return true;
	}

	private void evaluateRoute(Route route) { // WHAT?
		route.getCost().clear();

		if (route.isEmpty()) {
			return;
		}

		Customer prevCust = instance.getDepot();
		for (Customer cust : route.getCustomers()) {
			int numCust = cust.getNumber();
			int numPrevCust = prevCust.getNumber();

			double distance = prevCust.getCost().getDistance() + instance.getDistance(numPrevCust, numCust);
			double capacity = prevCust.getCost().getCapacity() + cust.getCapacity();

			double shape = instance.getShape() * instance.getTime(numPrevCust, numCust);
			double scale = instance.getScale();

			double expectedTime = prevCust.getCost().getExpectedTime() + shape * scale;
			double varianceTime = prevCust.getCost().getVarianceTime() + shape * scale * scale;

			int totalServiceTime = prevCust.getCost().getTotalServiceTime();
			int lowerBound = cust.getStartTw();
			int upperBound = cust.getEndTw();
			int shiftedLowerBound = lowerBound - totalServiceTime;
			int shiftedUpperBound = upperBound - totalServiceTime;

			double delay = prevCust.getCost().getDelay();
			if (upperBound <= totalServiceTime) {
				delay += expectedTime + totalServiceTime - upperBound;
			} else {
				delay += shape * scale * (1 - instance.getGamma(shape + 1, scale, shiftedUpperBound))
						- shiftedUpperBound * (1 - instance.getGamma(shape, scale, shiftedUpperBound));
			}

			double earliness = prevCust.getCost().getEarliness();
			if (lowerBound <= totalServiceTime) {
				earliness += 0;
			} else {
				earliness += shiftedLowerBound * instance.getGamma(shape, scale, shiftedLowerBound)
						- shape * scale * instance.getGamma(shape + 1, scale, shiftedLowerBound);
			}

			totalServiceTime += cust.getServiceDuration();

			cust.getCost().setDistance(distance);
			cust.getCost().setCapacity(capacity);
			cust.getCost().setExpectedTime(expectedTime);
			cust.getCost().setVarianceTime(varianceTime);
			cust.getCost().setTotalServiceTime(totalServiceTime);
			cust.getCost().setDelay(delay);
			cust.getCost().setEarliness(earliness);
			prevCust = cust;
		}

		Customer cust = instance.getDepot();
		int numCust = cust.getNumber();
		int numPrevCust = prevCust.getNumber();

		double distance = prevCust.getCost().getDistance() + instance.getDistance(numPrevCust, numCust);
		double capacity = prevCust.getCost().getCapacity() + cust.getCapacity();

		double shape = instance.getShape() * instance.getTime(numPrevCust, numCust);
		double scale = instance.getScale();

		double expectedTime = prevCust.getCost().getExpectedTime() + shape * scale;
		double varianceTime = prevCust.getCost().getVarianceTime() + shape * scale * scale;

		int totalServiceTime = prevCust.getCost().getTotalServiceTime();
//		int lowerBound = cust.getStartTw();
		int upperBound = cust.getEndTw();
//		int shiftedLowerBound = lowerBound - totalServiceTime;
		int shiftedUpperBound = upperBound - totalServiceTime;

		double delay = prevCust.getCost().getDelay();

		double overtime;
		if (upperBound <= totalServiceTime) {
			overtime = expectedTime + totalServiceTime - upperBound;
		} else {
			overtime = shape * scale * (1 - instance.getGamma(shape + 1, scale, shiftedUpperBound))
					- shiftedUpperBound * (1 - instance.getGamma(shape, scale, shiftedUpperBound));
		}

		double earliness = prevCust.getCost().getEarliness();
//		if (lowerBound <= totalServiceTime) {
//			earliness += 0;
//		} else {
//			earliness += shiftedLowerBound * instance.getGamma(shape, scale, shiftedLowerBound)
//					- shape * scale * instance.getGamma(shape + 1, scale, shiftedLowerBound);
//		}

		totalServiceTime += cust.getServiceDuration();

		route.getCost().setDistance(distance);
		route.getCost().setCapacity(capacity);
		route.getCost().setExpectedTime(expectedTime);
		route.getCost().setVarianceTime(varianceTime);
		route.getCost().setTotalServiceTime(totalServiceTime);
		route.getCost().setDelay(delay);
		route.getCost().setEarliness(earliness);
		route.getCost().setVechile(1);
		route.getCost().setOvertime(overtime);
		route.getCost().calculateTotalCost();
	}

	private void trimRoutes(ArrayList<Route> routes) {
		Route r;
		for (int i = 0; i < routes.size(); i++) {
			r = routes.get(i);
			if (r.getCustomersLength() == 0) {
				routes.remove(i);
				i--;
			}
		}
		routes.trimToSize();
	}

	public void setCost(Cost cost) {
		this.cost = cost;
	}

	public Cost getCost() {
		return cost;
	}

	public ArrayList<Route> getRoutes() {
		return routes;
	}

	public Route getRoute(int index) {
		return routes.get(index);
	}

	public int getRouteNr() {
		return routes.size();
	}

	public void setRoutes(ArrayList<Route> routes) {
		this.routes = routes;
	}

	public void deleteFromSolution(Route route) {
		this.routes.remove(route);
	}
}