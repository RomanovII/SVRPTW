package svrptw;

import java.util.ArrayList;
import svrptw.Customer;

/**
 * @author   Ilya
 */
public class Depot extends Customer {
	/**
	 * @uml.property  name="assignedCustomers"
	 * @uml.associationEnd  multiplicity="(0 -1)" inverse="assignedDepot:svrptw.Customer"
	 */
	private ArrayList<Customer> assignedCustomers;

	public Depot() {
		super();
		this.assignedCustomers = new ArrayList<Customer>();
	}
	
	public Depot( Depot copy ) {
		super( copy );
		this.assignedCustomers = new ArrayList<Customer>( copy.assignedCustomers );
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
