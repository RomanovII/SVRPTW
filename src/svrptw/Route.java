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
//		print.append(this.depot.getNumber() + " ");
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
	
	public void addDepot(Depot depot) {
		this.customers.add(depot);
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
		Customer curCust, preCust;
		Instance instance = Instance.getInstance();
		int lowerBound, shiftedLowerBound, upperBound, shiftedUpperBound;
		double earlinessTemp, delayTemp, overtimeTemp;
		double scale = instance.getScale();
		double shape = instance.getShape();
		
		Cost cost = new Cost();
		cost.setVechile(this.cost.getVechile());

		for (int i = 1; i < this.customers.size() - 1; ++i) {
			preCust = this.customers.get(i - 1);
			curCust = this.customers.get(i);

			cost.addDistance(instance.getDistance(preCust.getNumber(),
					curCust.getNumber()));
			cost.addCapacity(curCust.getCapacity());
			shape += instance.getShape()
					* instance
							.getTime(preCust.getNumber(), curCust.getNumber())
					/ scale;
			cost.addExpectedTime(instance.getTime(preCust.getNumber(),
					curCust.getNumber()));
			cost.addVarianceTime(instance.getTime(preCust.getNumber(),
					curCust.getNumber())
					* scale);

			/* Earliness */
			lowerBound = curCust.getStartTw();
			shiftedLowerBound = lowerBound - cost.getTotalServiceTime();
			earlinessTemp = 0;
			if (lowerBound <= cost.getTotalServiceTime()) {
				earlinessTemp = 0;
			} else {
				earlinessTemp = shiftedLowerBound
						* instance.getGamma(shape, scale, shiftedLowerBound)
						- shape
						* scale
						* instance
								.getGamma(shape + 1, scale, shiftedLowerBound);
			}
			// if (earlinessTemp <= new Double(0)) {
			// System.out.println("Error 2: Route evaluate. Earliness < 0 : "
			// + earlinessTemp);
			// earlinessTemp = 0;
			// }
			cost.addEarliness(earlinessTemp);

			/* Delay */
			upperBound = curCust.getEndTw();
			shiftedUpperBound = upperBound - cost.getTotalServiceTime();
			delayTemp = 0;
			if (upperBound <= cost.getTotalServiceTime()) {
				delayTemp = cost.getExpectedTime() - shiftedUpperBound;
			} else {
				delayTemp = shape
						* scale
						* (1 - instance.getGamma(shape + 1, scale,
								shiftedUpperBound))
						- shiftedUpperBound
						* (1 - instance.getGamma(shape, scale,
								shiftedUpperBound));
			}
			// if (delayTemp <= new Double(0)) {
			// System.out.println("Error 3: Route evaluate. Delay < 0 : "
			// + delayTemp);
			// delayTemp = 0;
			// }
			cost.addDelay(delayTemp);

			cost.addTotalServiceTime(new Double(curCust.getServiceDuration()).intValue());
		}
		preCust = this.customers.get(this.customers.size() - 2);
		curCust = this.customers.get(this.customers.size() - 1);
		/* Overtime */
		upperBound = curCust.getEndTw();
		shiftedUpperBound = upperBound - cost.getTotalServiceTime();
		if (upperBound <= cost.getTotalServiceTime()) {
			overtimeTemp = cost.getExpectedTime() - shiftedUpperBound;
		} else {
			overtimeTemp = shape
					* scale
					* (1 - instance.getGamma(shape + 1, scale,
							shiftedUpperBound)) - shiftedUpperBound
					* (1 - instance.getGamma(shape, scale, shiftedUpperBound));
		}
		cost.setOvertime(overtimeTemp);
		
		cost.rangeDelay();
		cost.rangeEarliness();
		cost.rangeOvertime();
		
		if (new Double(cost.getEarliness() * 1000).intValue() < 0) {
			System.out.println("Error 4: Route evaluate. Earliness < 0 : "
					+ cost.getEarliness());
		}
		if (new Double(cost.getDelay() * 1000).intValue() < 0) {
			System.out.println("Error 5: Route evaluate. Delay < 0 : "
					+ cost.getDelay());
		}
		if (new Double(cost.getOvertime() * 1000).intValue() < 0) {
			System.out.println("Error 6: Route evaluate. Overtime < 0 : "
					+ cost.getDelay() + " " +  new Double(cost.getOvertime()).intValue());
		}

		this.cost = cost;
	}
}
