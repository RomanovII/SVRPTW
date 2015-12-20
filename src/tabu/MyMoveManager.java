package tabu;

import java.util.ArrayList;
import java.util.HashSet;

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

	private Move[] getRelocateMoves(MySolution sol) {
		ArrayList<Route> routes = sol.getRoutes();
		Move[] buffer = new Move[instance.getCustomersNr()
				* instance.getVehiclesNr()];
		int nextBufferPos = 0;
		int deletePositionIndex;
		int insertPositionIndex;
		int customerRouteindex;
		int thisRouteIndex;

		for (int i = 0; i < routes.size(); i++) {
			if (!routes.get(i).isEmpty()) {
				Customer customer;
				thisRouteIndex = routes.get(i).getIndex(); // route you are
															// evaluating
				ArrayList<Customer> customers = findCustomersForSwap(routes
						.get(i)); // the list of all the customers near to the
									// route (can include the customer of the
									// route itself)
				for (int z = 0; z < customers.size(); z++) {
					customer = customers.get(z); // iterate each customer of the
													// list
					customerRouteindex = customer.getRouteIndex(); // index of
																	// the route
																	// to which
																	// the
																	// customer
																	// found
																	// belongs
					//if (customerRouteindex != thisRouteIndex) {
						insertPositionIndex = insertBestTravel(routes.get(i),
								customer);
						deletePositionIndex = sol.getRoute(customerRouteindex)
								.getCustomers().indexOf(customer);
						buffer[nextBufferPos++] = new MyRelocateMove(customer,
								customerRouteindex, deletePositionIndex,
								thisRouteIndex, insertPositionIndex);
					//}
				}
			}
		}
		Move[] moves = new Move[nextBufferPos];
		System.arraycopy(buffer, 0, moves, 0, nextBufferPos);
		return moves;
	}

	public ArrayList<Customer> findCustomersForSwap(Route path) {
		Route route = path;
		ArrayList<Customer> list = new ArrayList<Customer>();
		ArrayList<Customer> cust = (ArrayList<Customer>) route.getCustomers();

		for (int i = 0; i < route.getCustomersLength(); i++) {
			Customer k = cust.get(i);
			ArrayList<Customer> orderedList = instance
					.calculateTimeToCustomer(k);
			list.addAll(orderedList.subList(0, instance.getCustomersNr() / 2));
		}

		HashSet<Customer> hs = new HashSet<Customer>();
		hs.addAll(list);
		list.clear();
		list.addAll(hs);

		return list;

	}

	private int insertBestTravel(Route route, Customer customerChosenPtr) { // WHAT?
		double minCost = Double.MAX_VALUE;
		double tempMinCost = Double.MAX_VALUE;
		int position = 0;

		// first position
		/*
		 * Check if it's possible to put this customer into first position If
		 * the tw of the new customer is lower than that of the customer in
		 * position zero update the value tempMinCost and assign time to go from
		 * depot to current customer + time to go from current customer to
		 * customer in first position - time to go from depot to customer in
		 * first position
		 */
		if (customerChosenPtr.getEndTw() <= route.getCustomer(0).getEndTw()) {
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
		}

		// at the end
		// If the tw from last customer of the routes inserted is lower than the
		// current tw
		// then do the same as above
		if (route.getCustomer(route.getCustomersLength() - 1).getEndTw() <= customerChosenPtr
				.getEndTw()) {
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
		}
		// try between each customer
		// check time windows by pair (preceding and next), comparing them with
		// ours
		for (int i = 0; i < route.getCustomersLength() - 1; ++i) {
			if (route.getCustomer(i).getEndTw() <= customerChosenPtr.getEndTw()
					&& customerChosenPtr.getEndTw() <= route.getCustomer(i + 1)
							.getEndTw()) {
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
		}
		return position;
	}

}