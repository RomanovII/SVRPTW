package svrptw;

import java.util.ArrayList;
import svrptw.Customer;

public class Depot extends Customer {
	private ArrayList<Customer> assignedCustomers;

	public Depot() {
		super();
		this.assignedCustomers = new ArrayList<>();
	}

	public int getAssignedCustomersNr() {
		return assignedCustomers.size();
	}

	public void addAssignedCustomer(Customer customer) {
		assignedCustomers.add(customer);
	}

	public Customer getAssignedCustomer(int index) {
		return assignedCustomers.get(index);
	}

	public ArrayList<Customer> getAssignedcustomers() {
		return assignedCustomers;
	}

	public void setAssignedcustomers(ArrayList<Customer> assignedCustomers) {
		this.assignedCustomers = assignedCustomers;
	}


}
