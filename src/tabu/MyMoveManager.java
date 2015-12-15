package tabu;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.coinor.opents.Move;
import org.coinor.opents.MoveManager;
import org.coinor.opents.Solution;

import svrptw.Customer;
import svrptw.Instance;
import svrptw.Route;

@SuppressWarnings("serial")
public class MyMoveManager implements MoveManager {
	private static Instance instance;
	private MovesType movesType;

	public MyMoveManager(Instance instance) {
		MyMoveManager.setInstance(instance);
	}

	@Override
	public Move[] getAllMoves(Solution solution) {
		MySolution sol = ((MySolution)solution);
		switch(movesType)
		{
		case RELOCATE:
			return getRelocateMoves(sol);
		case SWAP:
			return getSwapMoves(sol);				
		default:
			return getRelocateMoves(sol);
		}
	}

	public Move[] getSwapMoves(MySolution solution){
		ArrayList<Route> routes = solution.getRoutes();
		Move[] buffer = new Move[ getInstance().getCustomersNr() * getInstance().getVehiclesNr() ];
		int nextBufferPos = 0;
		for (int j = 0; j < routes.size(); ++j) { // for each route 
			for (int k = 0; k < routes.get(j).getCustomersLength(); ++k) { // for each customer of the route
				for(int l = 0; l < routes.size(); ++l){ // for all the other routes 
					// if it's not the same route
					if(j!=l){
						Customer customer = routes.get(j).getCustomer(k);
						buffer[nextBufferPos++] = new MySwapMove(getInstance(), customer, j, k, l);
					}
				}
			}
		}
		// Trim buffer
		Move[] moves = new Move[ nextBufferPos];
		System.arraycopy( buffer, 0, moves, 0, nextBufferPos );
		return moves;
	}

	private Move[] getRelocateMoves(MySolution sol) {
		ArrayList<Route> routes = sol.getRoutes();
		Move[] buffer  = new Move[instance.getCustomersNr()*instance.getVehiclesNr()];
		int nextBufferPos = 0;
		int deletePositionIndex;
		int insertPositionIndex;
		int customerRouteindex;
		int thisRouteIndex;

		for (int i=0 ; i< routes.size(); i++){
			if(!routes.get(i).isEmpty()){
				Customer customer;
				thisRouteIndex= routes.get(i).getIndex(); 				// route you are evaluating 
				ArrayList<Customer> customers = findCustomersForSwap(routes.get(i)); // the list of all the customers near to the route (can include the customer of the route itself)
				for(int z=0; z<customers.size(); z++){
					customer = customers.get(z); // iterate each customer of the list
					customerRouteindex = customer.getRouteIndex(); // index of the route to which the customer found belongs
					if(customerRouteindex != thisRouteIndex){
						insertPositionIndex= insertBestTravel(routes.get(i), customer);
						deletePositionIndex = sol.getRoute(customerRouteindex).getCustomers().indexOf(customer);
						buffer[nextBufferPos++] =  new MyRelocateMove(instance, customer, customerRouteindex,  deletePositionIndex, thisRouteIndex , insertPositionIndex);
					}
				}
			}
		}
		Move[] moves = new Move[ nextBufferPos];
		System.arraycopy( buffer, 0, moves, 0, nextBufferPos );
		return moves;
	}

	public ArrayList<Customer> findCustomersForSwap(Route path){
		Route route = path;		
		ArrayList<Customer> list = new ArrayList<Customer>();
		ArrayList<Customer> cust= (ArrayList<Customer>) route.getCustomers();

		for (int i=0; i<route.getCustomersLength();i++){ // per ogni customer nella rotta
			Customer k = cust.get(i);
			ArrayList<Customer> orderedList= instance.calculateAnglesToCustomer(k);
			list.addAll(orderedList.subList(0, instance.getCustomersNr()/2)); //prendi gli n customer vicini		
		}


		// add elements to al, including duplicates
		HashSet<Customer> hs = new HashSet<Customer>();
		hs.addAll(list);
		list.clear();
		list.addAll(hs);

		return list;

	}

	public int getPositionInRoute(Customer deletedCustomer, MySolution sol)
	{
		int position = 0;
		int routeIndex = deletedCustomer.getRouteIndex();

		if(routeIndex>0)
		{
			List<Customer> lista = sol.getRoute(routeIndex).getCustomers();

			for(Customer c : lista)
			{
				if(c.getNumber() == deletedCustomer.getNumber())
					break;

				position++;
			}
		}
		return position;
	}
	private int insertBestTravel(Route route, Customer customerChosenPtr) {
		double minCost = Double.MAX_VALUE;
		double tempMinCost = Double.MAX_VALUE;
		int position = 0;

		// first position
		/*
		 * Check if it's possible to put this customer into first position
		 * If the tw of the new customer is lower than that of the customer in position zero
		 * update the value tempMinCost and assign time to go from depot to current customer +
		 * time to go from current customer to customer in first position - time to go from depot
		 * to customer in first position
		 */
		if(customerChosenPtr.getEndTw() <= route.getCustomer(0).getEndTw()) 
		{
			tempMinCost = instance.getTravelTime(route.getDepotNr(), customerChosenPtr.getNumber()) 
					+ instance.getTravelTime(customerChosenPtr.getNumber(), route.getFirstCustomerNr()) 
					- instance.getTravelTime(route.getDepotNr(), route.getFirstCustomerNr());
			if(minCost > tempMinCost) {
				minCost = tempMinCost;
				position = 0;
			}
		}

		// at the end
		//If the tw from last customer of the routes inserted is lower than the current tw
		//then do the same as above
		if(route.getCustomer(route.getCustomersLength() - 1).getEndTw() <= customerChosenPtr.getEndTw()){
			tempMinCost = instance.getTravelTime(route.getLastCustomerNr(), customerChosenPtr.getNumber()) 
					+ instance.getTravelTime(customerChosenPtr.getNumber(), route.getDepotNr()) 
					- instance.getTravelTime(route.getLastCustomerNr(), route.getDepotNr());
			if(minCost > tempMinCost) {
				minCost = tempMinCost;
				position = route.getCustomersLength();
			}
		}
		// try between each customer
		// check time windows by pair (preceding and next), comparing them with ours
		for(int i = 0; i < route.getCustomersLength() - 1; ++i) 
		{
			if(route.getCustomer(i).getEndTw() <= customerChosenPtr.getEndTw() && customerChosenPtr.getEndTw() <= route.getCustomer(i + 1).getEndTw()) {
				tempMinCost = instance.getTravelTime(route.getCustomerNr(i), customerChosenPtr.getNumber()) 
						+ instance.getTravelTime(customerChosenPtr.getNumber(), route.getCustomerNr(i + 1)) 
						- instance.getTravelTime(route.getCustomerNr(i), route.getCustomerNr(i + 1));
				if(minCost > tempMinCost) {
					minCost = tempMinCost;
					position = i + 1;
				}
			}
		}
		return position;
	}

	public Customer insertNewCustomer(Route routeD, Route routeI, Customer customer)
	{
		Random random = new Random();
		List<Customer> customersRouteI = routeI.getCustomers();
		Customer k = new Customer();
		int length, custIndex; 
		int a = 0;

		while(a==0)
		{
			length = customersRouteI.size();
			if(length==1)
				break;
			length = random.nextInt(length);
			custIndex = customersRouteI.get(length).getNumber();

			if(custIndex != customer.getNumber())
			{
				a++;
				k = customersRouteI.get(length);
			}
		}
		return k;
	}

	public static Instance getInstance() {
		return instance;
	}

	public static void setInstance(Instance instance) {
		MyMoveManager.instance = instance;
	}

	public MovesType getMovesType() {
		return movesType;
	}

	public void setMovesType(MovesType movesType) {
		this.movesType = movesType;
	}
}