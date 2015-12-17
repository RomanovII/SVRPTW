package svrptw;

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
		this.customers = new ArrayList<>();
		for (int i = 0; i < route.customers.size(); ++i) {
			this.customers.add(/* new Customer */(route.getCustomer(i)));
		}
	}

	public Customer getCustomer(int index) {
		return this.customers.get(index);
	}

	public void setDepot(Depot depot) {
		this.depot = depot;
	}

	public void removeCustomer(int index) {
		this.customers.remove(index);
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
		print.append("Route[" + index + ", " + (getCustomersLength()) + "]=");
		print.append(" " + this.depot.getNumber());
		for (int i = 0; i < this.customers.size(); ++i) {
			print.append(" " + this.customers.get(i)
					.getNumber()/*
								 * + "(" + this.customers.get(i).getRouteIndex()
								 * + ")"
								 */);
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
	}

	public void addCustomer(Customer customer, int index) {
		customer.setRouteIndex(this.getIndex());
		customer.setIsTaken(true);
		this.customers.add(index, customer);
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
}
