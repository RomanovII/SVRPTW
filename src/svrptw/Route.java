package svrptw;

import java.util.ArrayList;
import java.util.List;

/**
 * @author   Ilya
 */
public class Route {
	/**
	 * @uml.property  name="index"
	 */
	private int index; // Number of the route
	/**
	 * @uml.property  name="cost"
	 * @uml.associationEnd  
	 */
	private Cost cost; // cost of the route
	/**
	 * @uml.property  name="assignedVehicle"
	 * @uml.associationEnd  
	 */
	private Vehicle assignedVehicle; // vehicle assigned to the route
	/**
	 * @uml.property  name="depot"
	 * @uml.associationEnd  
	 */
	private Depot depot; // depot the route starts from
	/**
	 * @uml.property  name="customers"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="svrptw.Depot"
	 */
	private ArrayList<Customer> customers; // list of customers served in the
											// route

	/**
	 * Constructor of the route
	 */
	public Route() {
		cost = new Cost();
		customers = new ArrayList<Customer>();
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

	/**
	 * @param  depot
	 * @uml.property  name="depot"
	 */
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

	/**
	 * @return
	 * @uml.property  name="depot"
	 */
	public Depot getDepot() {
		return this.depot;
	}

	public int getLastCustomerNr() {
		return getCustomerNr(customers.size() - 1);
	}

	public int getFirstCustomerNr() {
		return getCustomerNr(1);
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
		this.evaluate();
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

	/**
	 * @param  index
	 * @uml.property  name="index"
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	public void setCapacity(double capacity) {
		this.cost.setCapacity(capacity);
	}

	/**
	 * @param  cost
	 * @uml.property  name="cost"
	 */
	public void setCost(Cost cost) {
		this.cost = cost;
	}

	/**
	 * @param  assignedvehicle
	 * @uml.property  name="assignedVehicle"
	 */
	public void setAssignedVehicle(Vehicle assignedvehicle) {
		this.assignedVehicle = assignedvehicle;
	}

	/**
	 * @return
	 * @uml.property  name="assignedVehicle"
	 */
	public Vehicle getAssignedVehicle() {
		return this.assignedVehicle;
	}

	public double getLoadAdmited() {
		return assignedVehicle.getCapacity();
	}

	public List<Customer> getCustomers() {
		return this.customers;
	}

	/**
	 * @return
	 * @uml.property  name="index"
	 */
	public int getIndex() {
		return index;
	}

	public int getCustomersLength() {
		return this.customers.size();
	}

	/**
	 * @return
	 * @uml.property  name="cost"
	 */
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
		
		this.cost.clear();
		
		for (int i = 1; i < this.customers.size() - 1; ++i) {
			preCust = this.customers.get(i - 1);
			curCust = this.customers.get(i);

			this.cost.addDistance(instance.getDistance(preCust.getNumber(),
					curCust.getNumber()));
			this.cost.addCapacity(curCust.getCapacity());
			shape += instance.getShape()
					* instance
							.getTime(preCust.getNumber(), curCust.getNumber())
					/ scale;
			this.cost.addExpectedTime(instance.getTime(preCust.getNumber(),
					curCust.getNumber()));
			this.cost.addVarianceTime(instance.getTime(preCust.getNumber(),
					curCust.getNumber())
					* scale);

			/* Earliness */
			lowerBound = curCust.getStartTw();
			shiftedLowerBound = lowerBound - this.cost.getTotalServiceTime();
			earlinessTemp = 0;
			if (lowerBound <= this.cost.getTotalServiceTime()) {
				earlinessTemp = 0;
			} else {
				earlinessTemp = shiftedLowerBound
						* instance.getGamma(shape, scale, shiftedLowerBound)
						- shape
						* scale
						* instance
								.getGamma(shape + 1, scale, shiftedLowerBound);
			}
			this.cost.addEarliness(earlinessTemp);

			/* Delay */
			upperBound = curCust.getEndTw();
			shiftedUpperBound = upperBound - this.cost.getTotalServiceTime();
			delayTemp = 0;
			if (upperBound <= this.cost.getTotalServiceTime()) {
				delayTemp = this.cost.getExpectedTime() - shiftedUpperBound;
			} else {
				delayTemp = shape
						* scale
						* (1 - instance.getGamma(shape + 1, scale,
								shiftedUpperBound))
						- shiftedUpperBound
						* (1 - instance.getGamma(shape, scale,
								shiftedUpperBound));
			}
			this.cost.addDelay(delayTemp);

			this.cost.addTotalServiceTime(new Double(curCust.getServiceDuration()).intValue());
		}
		preCust = this.customers.get(this.customers.size() - 2);
		curCust = this.customers.get(this.customers.size() - 1);
		/* Overtime */
		upperBound = curCust.getEndTw();
		shiftedUpperBound = upperBound - this.cost.getTotalServiceTime();
		if (upperBound <= this.cost.getTotalServiceTime()) {
			overtimeTemp = this.cost.getExpectedTime() - shiftedUpperBound;
		} else {
			overtimeTemp = shape
					* scale
					* (1 - instance.getGamma(shape + 1, scale,
							shiftedUpperBound)) - shiftedUpperBound
					* (1 - instance.getGamma(shape, scale, shiftedUpperBound));
		}
		this.cost.setOvertime(overtimeTemp);
	}
}
