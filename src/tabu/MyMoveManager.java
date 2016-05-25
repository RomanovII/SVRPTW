package tabu;

import java.util.ArrayList;

import org.coinor.opents.Move;
import org.coinor.opents.MoveManager;
import org.coinor.opents.Solution;

import svrptw.Customer;
import svrptw.Instance;
import svrptw.Route;

@SuppressWarnings("serial")
public class MyMoveManager implements MoveManager {
	private Instance instance = Instance.getInstance();

	public MyMoveManager() {
	}

	@Override
	public Move[] getAllMoves(Solution solution) {
		MySolution sol = ((MySolution) solution);
		return getRelocateMoves(sol);
	}

	private Move[] getMoves(MySolution sol) {
		Move[] buffer = new Move[5 * instance.getCustomersNr()
				* instance.getCustomersNr()];
		int nextBufferPos = 0;
		int inRoute = 0, inPos = 0;
		boolean in = false;
		for (int i = 0; i < sol.getRoutesNr(); ++i) {
			if (sol.getRoute(i).isEmpty()) {
				inRoute = i;
				in = true;
				break;
			}
		}
		for (int i = 1; i < instance.getCustomersNr(); ++i) {
			double[] distances = { Double.MAX_VALUE, Double.MAX_VALUE,
					Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE };
			int[] indexs = { instance.getCustomersNr(),
					instance.getCustomersNr(), instance.getCustomersNr(),
					instance.getCustomersNr(), instance.getCustomersNr() };
			for (int j = 1; j < instance.getCustomersNr(); ++j) {
				if (i == j)
					continue;
				double dist = instance.getDistance(i, j);
				int index = j;
				for (int k = 0; k < 5; ++k) {
					if (distances[k] > dist) {
						double tmp = dist;
						dist = distances[k];
						distances[k] = tmp;
						int t = index;
						index = indexs[k];
						indexs[k] = t;
					}
				}
			}
			Customer customer = instance.getCustomers().get(i);
			int deleteRouteNr = customer.getRouteIndex();
			int deletePositionIndex = 0;
			for (int j = 0; j < sol.getRoute(deleteRouteNr)
					.getCustomersLength(); ++j) {
				if (customer.getNumber() == sol.getRoute(deleteRouteNr)
						.getCustomerNr(j)) {
					deletePositionIndex = j;
				}
			}
			for (int k = 0; k < 5; ++k) {
				if (indexs[k] == instance.getCustomersNr())
					break;
				Customer cust = instance.getCustomers().get(indexs[k]);
				int insertRouteNr = cust.getRouteIndex();
				int insertPositionIndex = 0;
				for (int j = 0; j < sol.getRoute(insertRouteNr)
						.getCustomersLength(); ++j) {
					if (cust.getNumber() == sol.getRoute(insertRouteNr)
							.getCustomerNr(j)) {
						insertPositionIndex = j;
					}
				}
				buffer[nextBufferPos++] = new MyRelocateMove(customer,
						deleteRouteNr, deletePositionIndex, insertRouteNr,
						insertPositionIndex);
				if (!sol.getRoute(insertRouteNr).isEmpty()
						&& sol.getRoute(insertRouteNr).getCustomersLength() > insertPositionIndex + 1)
					buffer[nextBufferPos++] = new MyRelocateMove(customer,
							deleteRouteNr, deletePositionIndex, insertRouteNr,
							insertPositionIndex + 1);
			}
			if (in) {
				buffer[nextBufferPos++] = new MyRelocateMove(customer,
						deleteRouteNr, deletePositionIndex, inRoute, inPos);
			}
		}
		Move[] moves = new Move[nextBufferPos];
		System.arraycopy(buffer, 0, moves, 0, nextBufferPos);
		System.out.println("yes");
		return moves;
	}

	private Move[] getRelocateMoves(MySolution sol) {
		ArrayList<Route> routes = sol.getRoutes();
		Move[] buffer = new Move[instance.getCustomersNr()
				* instance.getVehiclesNr()];
		int nextBufferPos = 0;
		int deletePositionIndex;
		int insertPositionIndex;

		for (Route routeFrom : routes) {
			int size = routeFrom.getCustomersLength();
			for (int i = 1; i < size - 1; ++i) {
				Customer customer = routeFrom.getCustomer(i);
				deletePositionIndex = i;
				routeFrom.removeCustomer(deletePositionIndex);
				for (Route routeTo : routes) {
					insertPositionIndex = insertBestTravel(routeTo, customer);
					if (routeFrom.getIndex() == routeTo.getIndex()
							&& deletePositionIndex == insertPositionIndex) {
						continue;
					}
//					if (java.lang.System.currentTimeMillis() % 4 == 0) {
//						System.out.print(instance.getCustomersNr()
//								* instance.getVehiclesNr());
//						System.out.print(' ');
//						System.out.print(nextBufferPos);
//						System.out.println();
						buffer[nextBufferPos++] = new MyRelocateMove(customer,
								routeFrom.getIndex(), deletePositionIndex,
								routeTo.getIndex(), insertPositionIndex);
//					}
				}
				routeFrom.addCustomer(customer, deletePositionIndex);
			}
		}
		Move[] moves = new Move[nextBufferPos];
		System.arraycopy(buffer, 0, moves, 0, nextBufferPos);
		return moves;
	}

	private int insertBestTravel(Route route, Customer customerChosenPtr) { // WHAT?
		double minCost = Double.MAX_VALUE;
		double tempMinCost = Double.MAX_VALUE;
		int position = 0;

		if (route.isEmpty()) {
			return position;
		}

		// first position
		/*
		 * Check if it's possible to put this customer into first position If
		 * the tw of the new customer is lower than that of the customer in
		 * position zero update the value tempMinCost and assign time to go from
		 * depot to current customer + time to go from current customer to
		 * customer in first position - time to go from depot to customer in
		 * first position
		 */
		tempMinCost = instance.getDistance(route.getDepotNr(),
				customerChosenPtr.getNumber())
				+ instance.getDistance(customerChosenPtr.getNumber(),
						route.getFirstCustomerNr())
				- instance.getDistance(route.getDepotNr(),
						route.getFirstCustomerNr());
		if (minCost > tempMinCost) {
			minCost = tempMinCost;
			position = 0;
		}

		// at the end
		// If the tw from last customer of the routes inserted is lower than the
		// current tw
		// then do the same as above
		tempMinCost = instance.getDistance(route.getLastCustomerNr(),
				customerChosenPtr.getNumber())
				+ instance.getDistance(customerChosenPtr.getNumber(),
						route.getDepotNr())
				- instance.getDistance(route.getLastCustomerNr(),
						route.getDepotNr());
		if (minCost > tempMinCost) {
			minCost = tempMinCost;
			position = route.getCustomersLength();
		}
		// try between each customer
		// check time windows by pair (preceding and next), comparing them with
		// ours
		for (int i = 0; i < route.getCustomersLength() - 1; ++i) {
			tempMinCost = instance.getDistance(route.getCustomerNr(i),
					customerChosenPtr.getNumber())
					+ instance.getDistance(customerChosenPtr.getNumber(),
							route.getCustomerNr(i + 1))
					- instance.getDistance(route.getCustomerNr(i),
							route.getCustomerNr(i + 1));
			if (minCost > tempMinCost) {
				minCost = tempMinCost;
				position = i + 1;
			}
		}
		return position;
	}

}