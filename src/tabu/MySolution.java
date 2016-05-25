package tabu;

//import java.util.Random;
import java.util.ArrayList;

import javax.swing.plaf.SliderUI;
import javax.xml.bind.JAXB;

import org.coinor.opents.SolutionAdapter;

import svrptw.Cost;
import svrptw.Customer;
import svrptw.Instance;
import svrptw.MethodListener;
import svrptw.Route;
import svrptw.Vehicle;

@SuppressWarnings("serial")
public class MySolution extends SolutionAdapter {
	private Instance instance = Instance.getInstance();
	private ArrayList<Route> routes;
	private Cost cost;
	public double coefNu;
	public double capacityViol;

	// private double[] objectiveValue;

	// public MySolution(){} //This is needed otherwise java gives random
	// errors.. YES we love java <3

	public MySolution() {
		cost = new Cost();
		routes = new ArrayList<Route>();
		coefNu = 1;
		capacityViol = 0;
	}

	// This is needed for tabu search
	public Object clone() { // WHAT?
		MySolution copy = (MySolution) super.clone();
		copy.cost = new Cost(this.cost);
		copy.routes = new ArrayList<Route>(this.routes.size());
		for (Route route : this.routes)
			copy.routes.add(new Route(route));
		copy.coefNu = new Double(this.coefNu);
		copy.capacityViol = new Double(this.capacityViol);
		return copy;
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
			r.addDepot(instance.getDepot());
			r.addDepot(instance.getDepot());
			routes.add(r);
		}
	}

	public void startSolution() {
		ArrayList<Customer> unroutedCustomers = instance.getSortedCustomers();

		for (Customer unroutedCustomer : unroutedCustomers) {
			int bestNumRoute = -1;
			int bestNumPosition = -1;
			double bestInsertMeasures = Double.MAX_VALUE;
			for (Route route : routes) {
				Cost preCost = new Cost(route.getCost());
				if (!isFeasibleInsert(unroutedCustomer, 0, route)) {
					continue;
				}
				for (int pos = 1; pos < route.getCustomersLength(); ++pos) {
					route.addCustomer(unroutedCustomer, pos);
					Cost newCost = route.getCost();

					double m11 = newCost.getDistance() - preCost.getDistance();
					double m12 = newCost.getExpectedTime()
							- preCost.getExpectedTime();
					double m13 = instance.getCoefDelay()
							* (newCost.getDelay() - preCost.getDelay())
							+ instance.getCoefEarliness()
							* (newCost.getEarliness() - preCost.getEarliness());
					double newInsertMeasures = m11 + m12 + m13;
					if (bestInsertMeasures > newInsertMeasures) {
						bestInsertMeasures = newInsertMeasures;
						bestNumPosition = pos;
						bestNumRoute = route.getIndex();
					}

					route.removeCustomer(pos);
				}
			}
			if (bestNumPosition == -1 || bestNumRoute == -1) {
				System.out
						.println("Error 1. Step 1 - Initialization algorithm. Insert "
								+ bestNumPosition
								+ " into "
								+ bestNumRoute
								+ " route.");
			} else {
				routes.get(bestNumRoute).addCustomer(unroutedCustomer,
						bestNumPosition);
				System.out.println(unroutedCustomer.getNumber() + ": E "
						+ routes.get(bestNumRoute).getCost().getEarliness()
						+ ", D "
						+ routes.get(bestNumRoute).getCost().getDelay()
						+ ", Cap "
						+ routes.get(bestNumRoute).getCost().getCapacity()
						+ ", Insert " + bestNumPosition + " into "
						+ bestNumRoute + " route.");
			}
		}
		evaluateAbsolutely();
	}

	public void buildInitRoutes() {
		ArrayList<Customer> unroutedCustomers = new ArrayList<Customer>(
				instance.getSortedCustomers());
		int size = unroutedCustomers.size() / instance.getVehiclesNr() + 1;
		for (int routeIndex = 0; routeIndex < instance.getVehiclesNr(); ++routeIndex) {
			Route newRoute = routes.get(routeIndex);
			for (int i = 0; i < size; ++i) {
				if (unroutedCustomers.size() > 0) {
					newRoute.addCustomer(unroutedCustomers.get(0));
					unroutedCustomers.remove(0);
				}
			}
		}
		evaluateAbsolutely();
		int routeIndex = 0;
		ArrayList<Customer> removeList = new ArrayList<>();

		while (unroutedCustomers.size() > 0) {
			if (routeIndex == instance.getVehiclesNr()) {
				System.out.println("limit");
			}
			Route newRoute = routes.get(routeIndex);
			removeList.clear();
			for (Customer cust : unroutedCustomers) {
				int pos = 0;
				while (pos <= newRoute.getCustomersLength()
						&& !isFeasibleInsert(cust, pos, newRoute)) {
					++pos;
				}
				if (pos <= newRoute.getCustomersLength()) {
					newRoute.addCustomer(
							instance.getCustomers().get(cust.getNumber()), pos);
					evaluateInitRoute(newRoute);
					removeList.add(cust);
				}
			}

			for (Customer cust : removeList) {
				unroutedCustomers.remove(cust);
			}

			routes.set(routeIndex, newRoute);
			++routeIndex;
			evaluateObjectiveValue(newRoute.getCost(), new Cost());
			System.out.println(newRoute.printRoute());
			System.out.println(newRoute.printRouteCost());
		}
		// trimRoutes(this.routes);
	}

	public void buildInitialRoutes() {
		ArrayList<Customer> unroutedCustomers = new ArrayList<Customer>(
				instance.getSortedCustomers());

		int routeIndex = 0;

		ArrayList<Customer> removeList = new ArrayList<>();

		while (unroutedCustomers.size() > 0) {
			if (routeIndex == instance.getVehiclesNr()) {
				System.out.println("limit");
			}
			Route newRoute = routes.get(routeIndex);
			removeList.clear();
			for (Customer cust : unroutedCustomers) {
				int pos = 0;
				while (pos <= newRoute.getCustomersLength()
						&& !isFeasibleInsert(cust, pos, newRoute)) {
					++pos;
				}
				if (pos <= newRoute.getCustomersLength()) {
					newRoute.addCustomer(
							instance.getCustomers().get(cust.getNumber()), pos);
					evaluateInitRoute(newRoute);
					removeList.add(cust);
				}
			}

			for (Customer cust : removeList) {
				unroutedCustomers.remove(cust);
			}

			routes.set(routeIndex, newRoute);
			++routeIndex;
			evaluateObjectiveValue(newRoute.getCost(), new Cost());
			System.out.println(newRoute.printRoute());
			System.out.println(newRoute.printRouteCost());
		}
		// trimRoutes(this.routes);
	}

	private boolean isFeasibleInsert(Customer cust, int pos, Route route) { // WHAT?
		if (route.getCost().getCapacity() + cust.getCapacity() > route
				.getAssignedVehicle().getCapacity()) {
			return false;
		}
		return true;
	}

	private void evaluateInitRoute(Route route) { // WHAT?
		route.getCost().clear();

		if (route.isEmpty()) {
			return;
		}

		Customer prevCust = instance.getDepot();
		for (Customer cust : route.getCustomers()) {
			int numCust = cust.getNumber();
			int numPrevCust = prevCust.getNumber();

			double distance = prevCust.getCost().getDistance()
					+ instance.getDistance(numPrevCust, numCust);
			double capacity = prevCust.getCost().getCapacity()
					+ cust.getCapacity();

			double shape = instance.getShape()
					* instance.getTime(numPrevCust, numCust);
			double scale = instance.getScale();

			double expectedTime = prevCust.getCost().getExpectedTime() + shape
					* scale;
			double varianceTime = prevCust.getCost().getVarianceTime() + shape
					* scale * scale;

			int totalServiceTime = prevCust.getCost().getTotalServiceTime();
			int lowerBound = cust.getStartTw();
			int upperBound = cust.getEndTw();
			int shiftedLowerBound = lowerBound - totalServiceTime;
			int shiftedUpperBound = upperBound - totalServiceTime;

			double delay = prevCust.getCost().getDelay();
			if (upperBound <= totalServiceTime) {
				delay += expectedTime + totalServiceTime - upperBound;
			} else {
				delay += shape
						* scale
						* (1 - instance.getGamma(shape + 1, scale,
								shiftedUpperBound))
						- shiftedUpperBound
						* (1 - instance.getGamma(shape, scale,
								shiftedUpperBound));
			}

			double earliness = prevCust.getCost().getEarliness();
			if (lowerBound <= totalServiceTime) {
				earliness += 0;
			} else {
				earliness += shiftedLowerBound
						* instance.getGamma(shape, scale, shiftedLowerBound)
						- shape
						* scale
						* instance
								.getGamma(shape + 1, scale, shiftedLowerBound);
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

		double distance = prevCust.getCost().getDistance()
				+ instance.getDistance(numPrevCust, numCust);
		double capacity = prevCust.getCost().getCapacity() + cust.getCapacity();

		double shape = instance.getShape()
				* instance.getTime(numPrevCust, numCust);
		double scale = instance.getScale();

		double expectedTime = prevCust.getCost().getExpectedTime() + shape
				* scale;
		double varianceTime = prevCust.getCost().getVarianceTime() + shape
				* scale * scale;

		int totalServiceTime = prevCust.getCost().getTotalServiceTime();
		// int lowerBound = cust.getStartTw();
		int upperBound = cust.getEndTw();
		// int shiftedLowerBound = lowerBound - totalServiceTime;
		int shiftedUpperBound = upperBound - totalServiceTime;

		double delay = prevCust.getCost().getDelay();

		double overtime;
		if (upperBound <= totalServiceTime) {
			overtime = expectedTime + totalServiceTime - upperBound;
		} else {
			overtime = shape
					* scale
					* (1 - instance.getGamma(shape + 1, scale,
							shiftedUpperBound)) - shiftedUpperBound
					* (1 - instance.getGamma(shape, scale, shiftedUpperBound));
		}

		double earliness = prevCust.getCost().getEarliness();
		// if (lowerBound <= totalServiceTime) {
		// earliness += 0;
		// } else {
		// earliness += shiftedLowerBound * instance.getGamma(shape, scale,
		// shiftedLowerBound)
		// - shape * scale * instance.getGamma(shape + 1, scale,
		// shiftedLowerBound);
		// }

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

	public int getRoutesNr() {
		return routes.size();
	}

	public void setRoutes(ArrayList<Route> routes) {
		this.routes = routes;
	}

	public void deleteFromSolution(Route route) {
		this.routes.remove(route);
	}

	public void evaluateAbsolutely() {
		this.cost = new Cost();
		for (Route route : this.routes) {
			evaluateAbsRoute(route);
		}
//		System.out.println("Current Sol: " + this.getObjectiveValue()[0] + " " + this.getObjectiveValue()[1]);
	}

	public void evaluateInsertRoute(Route route, Customer customer, int position) {
		Cost prevCost = new Cost(route.getCost());
		route.addCustomer(customer, position);
		evaluateObjectiveValue(route.getCost(), prevCost);
	}

	public void evaluateDeleteRoute(Route route, Customer customer, int position) {
		Cost prevCost = new Cost(route.getCost());
		route.removeCustomer(position);
		evaluateObjectiveValue(route.getCost(), prevCost);
	}

	private void evaluateAbsRoute(Route route) {
		route.evaluate();
		evaluateAbsObjectiveValue(route.getCost());
	}

	private void calcCapacityViol() {
		capacityViol = 0;
		for (Route route : routes) {
			double capacViol = route.getCost().getCapacity()
					- route.getAssignedVehicle().getCapacity();
			capacityViol += capacViol > 0 ? capacViol : 0;
		}
	}

	private void evaluateAbsObjectiveValue(Cost cost) {
		double distance = this.cost.getDistance() + cost.getDistance();
		double capacity = this.cost.getCapacity() + cost.getCapacity();
		double expectedTime = this.cost.getExpectedTime()
				+ cost.getExpectedTime();
		double varianceTime = this.cost.getVarianceTime()
				+ cost.getVarianceTime();
		int totalServiceTime = this.cost.getTotalServiceTime()
				+ cost.getTotalServiceTime();
		double delay = this.cost.getDelay() + cost.getDelay();
		double earliness = this.cost.getEarliness() + cost.getEarliness();
		double vechile = this.cost.getVechile() + cost.getVechile();
		double overtime = this.cost.getOvertime() + cost.getOvertime();
		this.cost.setDistance(distance);
		this.cost.setCapacity(capacity);
		this.cost.setExpectedTime(expectedTime);
		this.cost.setVarianceTime(varianceTime);
		this.cost.setTotalServiceTime(totalServiceTime);
		this.cost.setDelay(delay);
		this.cost.setEarliness(earliness);
		this.cost.setVechile(vechile);
		this.cost.setOvertime(overtime);
		this.cost.calculateTotalCost();
		double obj2 = this.cost.getObjectiveValue(this.coefNu);
		calcCapacityViol();
		setObjectiveValue(new double[] { obj2 + this.coefNu * capacityViol,
				obj2 });
		// this.objectiveValue = new double[] {obj2 + this.coefNu *
		// capacityViol, obj2};
	}

	private void evaluateObjectiveValue(Cost cost, Cost prevCost) {
		double distance = this.cost.getDistance() + cost.getDistance()
				- prevCost.getDistance();
		double capacity = this.cost.getCapacity() + cost.getCapacity()
				- prevCost.getCapacity();
		double expectedTime = this.cost.getExpectedTime()
				+ cost.getExpectedTime() - prevCost.getExpectedTime();
		double varianceTime = this.cost.getVarianceTime()
				+ cost.getVarianceTime() - prevCost.getVarianceTime();
		int totalServiceTime = this.cost.getTotalServiceTime()
				+ cost.getTotalServiceTime() - prevCost.getTotalServiceTime();
		double delay = this.cost.getDelay() + cost.getDelay()
				- prevCost.getDelay();
		double earliness = this.cost.getEarliness() + cost.getEarliness()
				- prevCost.getEarliness();
		double vechile = this.cost.getVechile() + cost.getVechile()
				- prevCost.getVechile();
		double overtime = this.cost.getOvertime() + cost.getOvertime()
				- prevCost.getOvertime();
		this.cost.setDistance(distance);
		this.cost.setCapacity(capacity);
		this.cost.setExpectedTime(expectedTime);
		this.cost.setVarianceTime(varianceTime);
		this.cost.setTotalServiceTime(totalServiceTime);
		this.cost.setDelay(delay);
		this.cost.setEarliness(earliness);
		this.cost.setVechile(vechile);
		this.cost.setOvertime(overtime);
		this.cost.calculateTotalCost();
		double obj2 = this.cost.getObjectiveValue(this.coefNu);
		calcCapacityViol();
		setObjectiveValue(new double[] { obj2 + this.coefNu * capacityViol,
				obj2/*, this.cost.getDistance(), this.coefNu*/ });
		// this.objectiveValue = new double[] {obj2 + this.coefNu *
		// capacityViol, obj2};
	}

	public void updateParameters() {
		if (this.capacityViol == 0) {
			this.coefNu = this.coefNu / (1 + this.instance.getCoefPhi());
		} else {
			this.coefNu = this.coefNu * (1 + this.instance.getCoefPhi());
		}
	}

	public boolean isFeasible() {
		boolean feasible = true;
		for (Route route : routes) {
			if (route.getCost().getCapacity() > route.getAssignedVehicle()
					.getCapacity()) {
				feasible = false;
				break;
			}
		}
		return feasible;
	}
}