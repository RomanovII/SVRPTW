package tabu;

import org.coinor.opents.*;

import svrptw.Cost;
import svrptw.Customer;
import svrptw.Instance;
import svrptw.Route;

@SuppressWarnings("serial")
public class MyRelocateMove implements ComplexMove {
	private Instance instance = Instance.getInstance();
	private Customer customer;
	private int deleteDepotNr;
	private int deleteRouteNr;
	private int deletePositionIndex;
	private int insertDepotNr;
	private int insertRouteNr;
	private int insertPositionIndex;

	public MyRelocateMove( Customer customer, int deleteRouteNr, int deletePositionIndex,  int insertRouteNr, int insertPositionIndex)
	{   
		this.customer            = customer;
		this.deleteRouteNr       = deleteRouteNr;
		this.deletePositionIndex = deletePositionIndex;
		this.insertRouteNr       = insertRouteNr;
		this.insertPositionIndex = insertPositionIndex;
	}   // end constructor

	/**
	 * This function make the move on the solution and updates the originalPosition to be able to undo quickly
	 * @param solution
	 */
	public void operateOn( Solution solution )
	{
		MySolution sol = (MySolution)solution;
		Route insertRoute = sol.getRoute(insertRouteNr);
		Route deleteRoute = sol.getRoute(deleteRouteNr);
		Cost initialInsertCost = new Cost(insertRoute.getCost());
		Cost initialDeleteCost = new Cost(deleteRoute.getCost());
		evaluateDeleteRoute(deleteRoute, customer, deletePositionIndex);
		evaluateInsertRoute(insertRoute, customer, insertPositionIndex);
		evaluateTotalCostVariation(sol, this, initialInsertCost, initialDeleteCost);
	}   // end operateOn

	/**
	 * Set the insert position index of the move
	 * (is done in objective function, for performance factor)
	 * @param index
	 */
	public void setInsertPositionIndex(int index) {
		this.insertPositionIndex = index;
	} 

	public int[] attributesDelete() {
		return new int[]{ deleteDepotNr, deleteRouteNr, customer.getNumber(), 0, 0};
	}

	public int[] attributesInsert() {
		return new int[]{ insertDepotNr, insertRouteNr, customer.getNumber(), 0, 0};
	}
	
	private void evaluateTotalCostVariation(MySolution sol, MyRelocateMove myRelocateMove,
			Cost initialInsertCost, Cost initialDeleteCost) 
	{
		Cost insertCost = sol.getRoute(myRelocateMove.getInsertRouteNr()).getCost();
		Cost deleteCost = sol.getRoute(myRelocateMove.getDeleteRouteNr()).getCost();
		double distance = sol.getCost().getDistance() + insertCost.getDistance() + deleteCost.getDistance()
			- initialInsertCost.getDistance() - initialDeleteCost.getDistance();
		double capacity = sol.getCost().getCapacity() + insertCost.getCapacity() + deleteCost.getCapacity()
			- initialInsertCost.getCapacity() - initialDeleteCost.getCapacity();
		double expectedTime = sol.getCost().getExpectedTime() + insertCost.getExpectedTime() + deleteCost.getExpectedTime()
			- initialInsertCost.getExpectedTime() - initialDeleteCost.getExpectedTime();
		double varianceTime = sol.getCost().getVarianceTime() + insertCost.getVarianceTime() + deleteCost.getVarianceTime()
			- initialInsertCost.getVarianceTime() - initialDeleteCost.getVarianceTime();
		int totalServiceTime = sol.getCost().getTotalServiceTime() + insertCost.getTotalServiceTime() + deleteCost.getTotalServiceTime()
			- initialInsertCost.getTotalServiceTime() - initialDeleteCost.getTotalServiceTime();
		double delay = sol.getCost().getDelay() + insertCost.getDelay() + deleteCost.getDelay()
			- initialInsertCost.getDelay() - initialDeleteCost.getDelay();
		double earliness = sol.getCost().getEarliness() + insertCost.getEarliness() + deleteCost.getEarliness()
			- initialInsertCost.getEarliness() - initialDeleteCost.getEarliness();
		double vechile = sol.getCost().getVechile() + insertCost.getVechile() + deleteCost.getVechile()
			- initialInsertCost.getVechile() - initialDeleteCost.getVechile();
		double overtime = sol.getCost().getOvertime() + insertCost.getOvertime() + deleteCost.getOvertime()
			- initialInsertCost.getOvertime() - initialDeleteCost.getOvertime();
		sol.getCost().setDistance(distance);
		sol.getCost().setCapacity(capacity);
		sol.getCost().setExpectedTime(expectedTime);
		sol.getCost().setVarianceTime(varianceTime);
		sol.getCost().setTotalServiceTime(totalServiceTime);
		sol.getCost().setDelay(delay);
		sol.getCost().setEarliness(earliness);
		sol.getCost().setVechile(vechile);
		sol.getCost().setOvertime(overtime);
		sol.getCost().calculateTotalCost();
	}

	/**
	 * This function simulate the insertion of the customer in the given route on the given position.
	 * Computes the new cost and return it.
	 * It is an optimized version of the evaluate route. Calculates only for the customers affected
	 * by the insertion. Starts from the given position and could finish before reaching the end of
	 * the list if there is no modification in the arrive time at the customers.
	 * Does not alter the route or the customer
	 * @param route
	 * @param customer
	 * @param position
	 * @return
	 */
	private void evaluateInsertRoute(Route route, Customer customer, int position) {
		route.addCustomer(customer, position);
		evaluateRoute(route, position);
	} // end method evaluate insert route

	/**
	 * This function delete the customer in the given route on the given position and updates
	 * the cost.
	 * It is an optimized version of the evaluate route. Calculates only for the customers affected
	 * by the deletion. Starts from the given position and could finish before reaching the end of
	 * the list if there is no modification in the arrive time at the customers.
	 * Does alter the route.
	 * @param route
	 * @param position
	 * @return
	 */
	private void evaluateDeleteRoute(Route route, Customer customer, int position) {
		route.removeCustomer(position);
		evaluateRoute(route, position);
	} // end method evaluate delete route


	private void evaluateRoute(Route route, int pos) { // WHAT?
		route.getCost().clear();

		if (route.isEmpty()) {
			return;
		}
		
		Customer prevCust;
		if (pos == 0) {
			prevCust = instance.getDepot();
		}
		else {
			prevCust = route.getCustomer(pos - 1);
		}
		
		for (int i = pos; i < route.getCustomersLength(); ++i){
			Customer cust = route.getCustomer(i);
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
	
	/**
	 * This function returns a string containing the move information in readable format
	 */
	public String toString() {
		StringBuffer print = new StringBuffer();
		print.append("--- Move Customer " + customer.getNumber() + "-------------------------------------");
		print.append("\n" + "| DeleteDepot=" + deleteDepotNr + " DeleteRoute=" + deleteRouteNr + " DeletePosition=" + deletePositionIndex);
		print.append("\n" + "| InsertDepot=" + insertDepotNr + " InsertRoute=" + insertRouteNr + " InsertPosition=" + insertPositionIndex);
		print.append("\n" + "--------------------------------------------------");
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
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the deleteDepotNr
	 */
	public int getDeleteDepotNr() {
		return deleteDepotNr;
	}

	/**
	 * @param deleteDepotNr the deleteDepotNr to set
	 */
	public void setDeleteDepotNr(int deleteDepotNr) {
		this.deleteDepotNr = deleteDepotNr;
	}

	/**
	 * @return the deleteRouteNr
	 */
	public int getDeleteRouteNr() {
		return deleteRouteNr;
	}

	/**
	 * @param deleteRouteNr the deleteRouteNr to set
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
	 * @param deletePositionIndex the deletePositionIndex to set
	 */
	public void setDeletePositionIndex(int deletePositionIndex) {
		this.deletePositionIndex = deletePositionIndex;
	}

	/**
	 * @return the insertDepotNr
	 */
	public int getInsertDepotNr() {
		return insertDepotNr;
	}

	/**
	 * @param insertDepotNr the insertDepotNr to set
	 */
	public void setInsertDepotNr(int insertDepotNr) {
		this.insertDepotNr = insertDepotNr;
	}

	/**
	 * @return the insertRouteNr
	 */
	public int getInsertRouteNr() {
		return insertRouteNr;
	}

	/**
	 * @param insertRouteNr the insertRouteNr to set
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
}   // end class MySwapMove
