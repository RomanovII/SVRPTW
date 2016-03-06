package svrptw;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Route {
	private int index; // Number of the route
	private Cost cost; // cost of the route
	private Vehicle assignedVehicle; // vehicle assigned to the route
	private Depot depot; // depot the route starts from
	private ArrayList<Customer> customers; // list of customers served in the
											// route

	/**
	 * Constructor of the route
	 */
	public Route() {
		cost = new Cost();
		customers = new ArrayList<>();
	}

	public Route(Route route) {
		this.index = route.index;
		this.cost = new Cost(route.cost);
		this.assignedVehicle = route.assignedVehicle;
		this.depot = route.depot;
		this.customers = new ArrayList<Customer>(route.getCustomersLength());
		for (Customer cust : route.getCustomers())
			this.customers.add(cust);
	}

	public Customer getCustomer(int index) {
		return this.customers.get(index);
	}

	public void setDepot(Depot depot) {
		this.depot = depot;
	}

	public void removeCustomer(int index) {
		this.customers.remove(index);
		this.evaluate();
	}

	public int getDepotNr() {
		return this.depot.getNumber();
	}

	public Depot getDepot() {
		return this.depot;
	}

	public int getLastCustomerNr() {
		return getCustomerNr(customers.size() - 1);
	}

	public int getFirstCustomerNr() {
		return getCustomerNr(0);
	}

	public boolean isEmpty() {
		if (getCustomersLength() > 0)
			return false;
		else
			return true;
	}

	public int getCustomerNr(int index) {
		return this.customers.get(index).getNumber();
	}

	public String printRoute() {
		StringBuffer print = new StringBuffer();
		// print.append("Route[" + index + ", " + (getCustomersLength()) +
		// "]=");
		print.append(this.depot.getNumber() + " ");
		for (int i = 0; i < this.customers.size(); ++i) {
			print.append(this.customers.get(i).getNumber()/*
														 * + "(" +
														 * this.customers
														 * .get(i).
														 * getRouteIndex() + ")"
														 */+ " ");
		}
		// print.append("\n");
		return print.toString();
	}

	public String printRouteCost() {
		StringBuffer print = new StringBuffer();
		// print.append("\n" + "Route[" + index + "]");
		print.append("--------------------------------------------");
		print.append("\n" + "| Capacity=" + cost.getCapacity());
		print.append("\n");
		return print.toString();
	}

	public void setCustomers(ArrayList<Customer> customers) {
		this.customers = customers;
	}

	public void addCustomer(Customer customer) {
		customer.setRouteIndex(this.getIndex());
		customer.setIsTaken(true);
		this.customers.add(customer);
		this.evaluate();
	}

	public void addCustomer(Customer customer, int index) {
		customer.setRouteIndex(this.getIndex());
		customer.setIsTaken(true);
		this.customers.add(index, customer);
		this.evaluate();
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setCapacity(double capacity) {
		this.cost.setCapacity(capacity);
	}

	public void setCost(Cost cost) {
		this.cost = cost;
	}

	public void setAssignedVehicle(Vehicle assignedvehicle) {
		this.assignedVehicle = assignedvehicle;
	}

	public Vehicle getAssignedVehicle() {
		return this.assignedVehicle;
	}

	public double getLoadAdmited() {
		return assignedVehicle.getCapacity();
	}

	public List<Customer> getCustomers() {
		return this.customers;
	}

	public int getIndex() {
		return index;
	}

	public int getCustomersLength() {
		return this.customers.size();
	}

	public Cost getCost() {
		return this.cost;
	}

	public void initializeTimes() {
		cost.clear();
	}

	public void evaluate() {
		double distance = 0;
		double capacity = 0;
		double expectedTime = 0;
		double varianceTime = 0;
		double delay = 0;
		double earliness = 0;
		double overtime = 0;
		int serviceTime = 0;
		Instance instance = Instance.getInstance();
		double shape = 0;
		double scale = instance.getScale();
		
		for (int i = 1; i < this.customers.size() - 1; ++i) {
			Customer curCust = this.customers.get(i - 1);
			Customer preCust = this.customers.get(i);
			distance += instance.getDistance(preCust.getNumber(),
					curCust.getNumber());
			capacity += curCust.getCapacity();
			shape += instance.getShape()
					* instance
							.getTime(preCust.getNumber(), curCust.getNumber())
					/ scale;
			expectedTime += instance.getTime(preCust.getNumber(),
					curCust.getNumber());
			varianceTime += instance.getTime(preCust.getNumber(),
					curCust.getNumber())
					* scale;

			// earliness
			int lowerBound = curCust.getStartTw();
			int shiftedLowerBound = lowerBound - serviceTime;
			double earlinessTemp = 0;
			if (lowerBound <= serviceTime) {
				earlinessTemp = 0;
			} else {
				earlinessTemp = shiftedLowerBound
						* instance.getGamma(shape, scale, shiftedLowerBound)
						- shape
						* scale
						* instance
								.getGamma(shape + 1, scale, shiftedLowerBound);
			}
//			if (earlinessTemp <= new Double(0)) {
//				System.out.println("Error 2: Route evaluate. Earliness < 0 : " + earlinessTemp);
//				earlinessTemp = 0;
//			}
			earliness += earlinessTemp;

			// delay
			int upperBound = curCust.getEndTw();
			int shiftedUpperBound = upperBound - serviceTime;
			double delayTemp = 0;
			if (upperBound <= serviceTime) {
				delayTemp = expectedTime + serviceTime - upperBound;
			} else {
				delayTemp = shape
						* scale
						* (1 - instance.getGamma(shape + 1, scale,
								shiftedUpperBound))
						- shiftedUpperBound
						* (1 - instance.getGamma(shape, scale,
								shiftedUpperBound));
			}
//			if (delayTemp <= new Double(0)) {
//				System.out.println("Error 3: Route evaluate. Delay < 0 : " + delayTemp);
//				delayTemp = 0;
//			}
			delay += delayTemp;

			serviceTime += curCust.getServiceDuration();
		}
		
		delay = new BigDecimal(delay).setScale(3, RoundingMode.HALF_UP).doubleValue();
		earliness = new BigDecimal(earliness).setScale(3, RoundingMode.HALF_UP).doubleValue();
		
		double zero = 0;
		if (earliness < zero) {
			System.out.println("Error 4: Route evaluate. Earliness < 0 : " + earliness);
		}
		if (delay < zero) {
			System.out.println("Error 5: Route evaluate. Delay < 0 : " + delay);
		}
		
		this.cost.setDistance(distance);
		this.cost.setCapacity(capacity);
		this.cost.setExpectedTime(expectedTime);
		this.cost.setVarianceTime(varianceTime);
		this.cost.setTotalServiceTime(serviceTime);
		this.cost.setDelay(delay);
		this.cost.setEarliness(earliness);
	}
}




















