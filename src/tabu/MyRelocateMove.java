package tabu;

import org.coinor.opents.*;

import svrptw.Customer;
import svrptw.Route;

@SuppressWarnings("serial")
public class MyRelocateMove implements ComplexMove {
	private Customer customer;
	private int deleteRouteNr;
	private int deletePositionIndex;
	private int insertRouteNr;
	private int insertPositionIndex;

	public MyRelocateMove(Customer customer, int deleteRouteNr,
			int deletePositionIndex, int insertRouteNr, int insertPositionIndex) {
		this.customer = customer;
		this.deleteRouteNr = deleteRouteNr;
		this.deletePositionIndex = deletePositionIndex;
		this.insertRouteNr = insertRouteNr;
		this.insertPositionIndex = insertPositionIndex;
	} // end constructor

	/**
	 * This function make the move on the solution and updates the
	 * originalPosition to be able to undo quickly
	 * 
	 * @param solution
	 */
	public void operateOn(Solution solution) {
		MySolution sol = (MySolution) solution;
		Route insertRoute = sol.getRoute(insertRouteNr);
		Route deleteRoute = sol.getRoute(deleteRouteNr);
		sol.evaluateInsertRoute(insertRoute, customer, insertPositionIndex);
		sol.evaluateDeleteRoute(deleteRoute, customer, deletePositionIndex);
	} // end operateOn

	public void undoOperation(Solution solution) {
		MySolution sol = (MySolution) solution;
		Route insertRoute = sol.getRoute(deleteRouteNr);
		Route deleteRoute = sol.getRoute(insertRouteNr);
		sol.evaluateInsertRoute(insertRoute, customer, deletePositionIndex);
		sol.evaluateDeleteRoute(deleteRoute, customer, insertPositionIndex);
	}

	/**
	 * This function returns a string containing the move information in
	 * readable format
	 */
	public String toString() {
		StringBuffer print = new StringBuffer();
		print.append("--- Move Customer " + customer.getNumber()
				+ "-------------------------------------");
		print.append("\n" + "| DeleteRoute=" + deleteRouteNr
				+ " DeletePosition=" + deletePositionIndex);
		print.append("\n" + "| InsertRoute=" + insertRouteNr
				+ " InsertPosition=" + insertPositionIndex);
		print.append("\n"
				+ "--------------------------------------------------");
		return print.toString();
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the customer number
	 */
	public int getCustomerNr() {
		return customer.getNumber();
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the deleteRouteNr
	 */
	public int getDeleteRouteNr() {
		return deleteRouteNr;
	}

	/**
	 * @param deleteRouteNr
	 *            the deleteRouteNr to set
	 */
	public void setDeleteRouteNr(int deleteRouteNr) {
		this.deleteRouteNr = deleteRouteNr;
	}

	/**
	 * @return the deletePositionIndex
	 */
	public int getDeletePositionIndex() {
		return deletePositionIndex;
	}

	/**
	 * @param deletePositionIndex
	 *            the deletePositionIndex to set
	 */
	public void setDeletePositionIndex(int deletePositionIndex) {
		this.deletePositionIndex = deletePositionIndex;
	}

	/**
	 * @return the insertRouteNr
	 */
	public int getInsertRouteNr() {
		return insertRouteNr;
	}

	/**
	 * @param insertRouteNr
	 *            the insertRouteNr to set
	 */
	public void setInsertRouteNr(int insertRouteNr) {
		this.insertRouteNr = insertRouteNr;
	}

	/**
	 * @return the insertPositionIndex
	 */
	public int getInsertPositionIndex() {
		return insertPositionIndex;
	}

	@Override
	public int[] attributes() {
		return new int[] { customer.getNumber(), insertRouteNr,
				insertPositionIndex, deleteRouteNr, deletePositionIndex };
	}
}
