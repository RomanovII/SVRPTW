package com.TabuSearch;

//import java.util.Random;
import java.util.ArrayList;

import org.coinor.opents.SolutionAdapter;

import com.vrp.Cost;
import com.vrp.Customer;
import com.vrp.Instance;
import com.vrp.Route;
import com.vrp.Vehicle;

@SuppressWarnings("serial")
public class MySolution extends SolutionAdapter{
	public static double CoefMu = 1.0;
	public static double CoefAlpha1 = 0.5;
	public static double CoefAlpha2 = 0.5;
	public static double CoefAlpha3 = 0.5;
	public static double CoefLambda = 1;
	public static double CoefCe = 1;
	public static double CoefCd = 1;
	private static Instance instance;
	private ArrayList<Route> route;
	private Cost cost; //Save the tot cost of the route
	private double alpha;		
	private double beta;		
	private double gamma;		
	private double delta;		
	private double upLimit;
	private double resetValue;

	public MySolution(){} //This is needed otherwise java gives random errors.. YES we love java <3

	public MySolution(Instance instance) {
		MySolution.setInstance(instance);
		cost = new Cost();
		route = new ArrayList<Route>();
		initializeRoutes(instance);
		buildInitialRoutes(instance);
		alpha 	= 1;
		gamma	= 1;
		delta	= 0.005;
		upLimit = 10000000;
		resetValue = 0.1;
	}

	public void initializeRoutes(Instance instance) {
		route = new ArrayList<Route>(instance.getVehiclesNr());
		for (int j = 0; j < instance.getVehiclesNr(); ++j)
		{
			// initialization of routes
			Route r = new Route();
			r.setIndex(j);
			// add the depot as the first node to the route
			r.setDepot(instance.getDepot());
			// set the cost of the route
			Cost cost = new Cost();
			r.setCost(cost);
			// assign vehicle
			Vehicle vehicle = new Vehicle();
			vehicle.setCapacity(instance.getCapacity(0, 0));
			r.setAssignedVehicle(vehicle);
			//add the new route into the arrayList
			route.add(r);
		}
	}

	//This is needed for tabu search
	public Object clone(){
		MySolution copy = (MySolution) super.clone();
		copy.cost = new Cost(this.cost);
		copy.route = new ArrayList<Route>(this.route);
		copy.alpha         = this.alpha;
		copy.beta          = this.beta;
		copy.gamma         = this.gamma;
		copy.delta         = this.delta;
		return copy;
	}

	public Cost getCost(){
		return cost;
	}

	public void updateParameters(double a, double b) {
		// capacity violation test
		if (a == 0) {
			alpha = alpha / (1 + delta);
		} else {
			alpha = alpha * (1 + delta);
			if(alpha > upLimit){
				alpha = resetValue;
			}
		}
		// time window violation test
		if (b == 0) {
			gamma = gamma / (1 + delta);
		} else {
			gamma = gamma * (1 + delta);
			if(gamma > upLimit){
				gamma = resetValue;
			}
		}
	}

	public void buildInitialRoutes(Instance instance) {
		ArrayList<Customer> unroutedCustomers = new ArrayList<Customer>(instance.getSortedCustomers());
		
		int routeIndex = 0;
		
		while (unroutedCustomers.size() > 0)
		{
			Route newRoute = route.get(routeIndex);
			
			Customer cust = instance.getCustomers().get(unroutedCustomers.get(0).getNumber());
			unroutedCustomers.remove(0);
			
			newRoute.addCustomer(cust);
						
			while (unroutedCustomers.size() > 0)
			{
				if (!findAndInsertBestCustomer(newRoute, unroutedCustomers, instance))
					break;
			}
			
			route.set(routeIndex, newRoute);
			++routeIndex;
			System.out.println(newRoute.printRoute());
			System.out.println(newRoute.printRouteCost());
		}
		trimRoutes(this.route);
	}
	
	private boolean findAndInsertBestCustomer(Route route, ArrayList<Customer> unroutedCustomers, Instance instance)
	{
		Customer bestCust = null;
		double bestCustCost = Double.MAX_VALUE;
		int bestCustPos = -1;
		
		for (Customer cust : unroutedCustomers){
			int bestPos = -1;
			double bestCost = Double.MAX_VALUE;
			for (int i = 0; i <= route.getCustomersLength(); ++i){
				if (isFeasibleInsert(cust, i, route, instance)){
					double cost = getInsertCost(cust, i, route, instance);
					if (cost < bestCost){
						bestCost = cost;
						bestPos = i;
					}
				}
			}
			if (bestPos != -1){
				if (bestCost < bestCustCost){
					bestCust = cust;
					bestCustCost = bestCost;
					bestCustPos = bestPos;
				}
			}
		}
		if (bestCust == null){
			return false;
		}
		
		unroutedCustomers.remove(bestCust);
		route.addCustomer(instance.getCustomers().get(bestCust.getNumber()), bestCustPos);
		evaluateRoute(route);
		return true;
	}
	
	private boolean isFeasibleInsert(Customer cust, int pos, Route route, Instance instance)
	{
		if (route.getCost().getLoad() + cust.getCapacity() > route.getAssignedVehicle().getCapacity()){
			return false;
		}
		
		Customer prevCust = (pos>0 ? route.getCustomer(pos - 1) : instance.getDepotCust());
		
		double time = prevCust.getArriveTime();
		time = nextArriveTime(cust, prevCust, time, instance);
		if (time > cust.getEndTw()) {
		    return false;	
		}
		Customer lastCust = cust;
		for (int i = pos; i < route.getCustomersLength(); i++) {
			Customer nextCust = route.getCustomer(i);
			time = nextArriveTime(nextCust, lastCust, time, instance);
			if (time > nextCust.getEndTw())
				return false;
			lastCust = nextCust;
		}
		return true;
	}
	
	private double getInsertCost(Customer cust, int pos, Route route, Instance instance){
		Customer prevCust = (pos > 0 ? route.getCustomer(pos - 1) : instance.getDepotCust());
		Customer nextCust = (pos < route.getCustomersLength() ? route.getCustomer(pos) : instance.getDepotCust());
		
		double distanceChange = CriterionC11(prevCust, cust, nextCust, route, instance);
		if (distanceChange < 0)
			System.out.println("Ñ11 = " + distanceChange);
		
		double serviceTimeChange = CriterionC12(prevCust, cust, nextCust, route, instance);
		if (serviceTimeChange < 0)
			System.out.println("Ñ12 = " + serviceTimeChange);
		
		//double waitingChange;
		
		double c1 = CoefAlpha1*distanceChange + CoefAlpha2*serviceTimeChange;
		
		double cost = CoefLambda*cust.getDistanceFromDepot() - c1;
		return cost;
	}
	
	private void trimRoutes(ArrayList<Route> route){
		Route r;
		for(int i=0; i < route.size();i++){
			r = route.get(i);
			if(r.getCustomersLength() == 0){
				route.remove(i);
				i--;
			}
		}
		route.trimToSize();
	}

	public double nextArriveTime(Customer newCustomer, Customer prevCustomer, double prevTime, Instance instance)
	{
		prevTime = Math.max(prevTime, prevCustomer.getStartTw());
		double travelTime = instance.getTravelTime(prevCustomer.getNumber(), newCustomer.getNumber());
		double serviceTime = prevCustomer.getServiceDuration();
		return prevTime + serviceTime + travelTime;
	}

	public double CriterionC11(Customer custI, Customer custU, Customer custJ, Route route, Instance instance)
	{
		int indexI = custI.getNumber();
		int indexU = custU.getNumber();
		int indexJ = custJ.getNumber();
		double distIU = instance.getDistances(indexI, indexU);
		double distUJ = instance.getDistances(indexU, indexJ);
		double distIJ = instance.getDistances(indexI, indexJ);
		return distIU + distUJ - CoefMu*distIJ;
	}

	public double CriterionC12(Customer custI, Customer custU, Customer custJ, Route route, Instance instance)
	{
		double bI = custI.getArriveTime();        
		double bU = nextArriveTime(custU, custI, bI, instance);
		double bJu = nextArriveTime(custJ, custU, bU, instance);
		double bJ = custJ.getArriveTime();
		return bJu - bJ;
	}
/*
	public double CriterionC13(int i, Customer custU, int j, Route route, Instance instance)
	{
		double prevDelay = 0;
		double prevEarly = 0;
		//for (int k = j; k < route.getCustomersLength(); ++k)
		//{
		int k = j;
			prevDelay += route.getCustomer(k).getDelay();
			prevEarly += route.getCustomer(k).getEarly();
		//}
		double Delay = 0;
		double Early = 0;
		Customer custI = route.getCustomer(i);
		Customer custJ = route.getCustomer(j);
		double bI = custI.getArriveTime();
		double bU = nextArriveTime(custU, custI, bI, instance);
		Delay += custU.getDelay(bU, custU.getEndTw());//Math.max(0, bU + custU.getServiceDuration() - custU.getEndTw()));
		Early += custU.getEarly(bU, custU.getStartTw());//Math.max(0, custU.getStartTw() - bU));
		double bK = nextArriveTime(custJ, custU, bU, instance);
		Delay += custJ.getDelay(bK, custJ.getEndTw());//Math.max(0, bK + custJ.getServiceDuration() - custJ.getEndTw()));
		Early += custJ.getEarly(bK, custJ.getStartTw());//Math.max(0, custJ.getStartTw() - bK));
		//for (int k = j + 1; k < route.getCustomersLength(); ++k)
		//{
			Customer newCustomer = route.getCustomer(k);
			Customer prevCustomer = route.getCustomer(k-1);
			bK = nextArriveTime(newCustomer, prevCustomer, bK, instance);
			Delay += newCustomer.getDelay(bK, newCustomer.getEndTw());//Math.max(0, bK + newCustomer.getServiceDuration() - newCustomer.getEndTw()));
			Early += newCustomer.getEarly(bK, newCustomer.getStartTw());//Math.max(0, newCustomer.getStartTw() - bK));
		//}
		return CoefCd*Math.max(0,Delay - prevDelay) + CoefCe*Math.max(0,Early - prevEarly);
	}
*/
/*
	public double CriterionC1(int i, Customer u, int j, Route route, Instance instance)
	{
		return CoefAlpha1*CriterionC11(i, u, j, route, instance) + CoefAlpha2*CriterionC12(i, u, j, route, instance) + CoefAlpha3*CriterionC13(i, u, j, route, instance);
	}
*/

	public double CriterionC2(Customer u, double c1Value, Route route)
	{
		double d0U = u.getArriveTime();
		return CoefLambda*d0U - c1Value;
	}


	private void evaluateRoute(Route route) {
		double totalTime = 0;
		double waitingTime = 0;
		double twViol = 0;
		Customer customerK;
		route.initializeTimes();
		// do the math only if the route is not empty
		if(!route.isEmpty()){
			// sum distances between each node in the route
			for (int k = 0; k < route.getCustomersLength(); ++k){
				// get the actual customer
				customerK = route.getCustomer(k);
				// add travel time to the route
				if(k == 0){
					route.getCost().travelTime += getInstance().getTravelTime(route.getDepotNr(), customerK.getNumber());
					totalTime += getInstance().getTravelTime(route.getDepotNr(), customerK.getNumber());
				}else{
					route.getCost().travelTime += getInstance().getTravelTime(route.getCustomerNr(k -1), customerK.getNumber());
					totalTime += getInstance().getTravelTime(route.getCustomerNr(k -1), customerK.getNumber());
				} // end if else			
				customerK.setArriveTime(totalTime);
				// add waiting time if any
				waitingTime = Math.max(0, customerK.getStartTw() - totalTime);
				route.getCost().waitingTime += waitingTime;
				// update customer timings information
				customerK.setWaitingTime(waitingTime);
				totalTime = Math.max(customerK.getStartTw(), totalTime);
				// add time window violation if any
				twViol = Math.max(0, totalTime - customerK.getEndTw());
				route.getCost().addTWViol(twViol);
				customerK.setTwViol(twViol);
				// add the service time to the total
				totalTime += customerK.getServiceDuration();
				// add service time to the route
				route.getCost().serviceTime += customerK.getServiceDuration();
				// add capacity to the route
				route.getCost().load += customerK.getCapacity();
			} // end for customers
			// add the distance to return to depot: from last node to depot
			totalTime += getInstance().getTravelTime(route.getLastCustomerNr(), route.getDepotNr());
			route.getCost().travelTime += getInstance().getTravelTime(route.getLastCustomerNr(), route.getDepotNr());
			// add the depot time window violation if any
			twViol = Math.max(0, totalTime - route.getDepot().getEndTw());
			route.getCost().addTWViol(twViol);
			// update route with timings of the depot
			route.setDepotTwViol(twViol);
			route.setReturnToDepotTime(totalTime);
			route.getCost().setLoadViol(Math.max(0, route.getCost().load - route.getLoadAdmited()));
			// update total violation
			route.getCost().calculateTotalCostViol();			
		} // end if route not empty
	} // end method evaluate route


	public double getAlpha() {
		return alpha;
	}

	public double getBeta() {
		return beta;
	}

	public double getGamma() {
		return gamma;
	}
/*
	public double getCoefDelay() {
		return cDelay;
	}

	public double getCoefEarliness() {
		return cEarliness;
	}

	public double getCoefOvertime() {
		return cOvertime;
	}

	public double getCoefDictance() {
		return cDistance;
	}

	public double getCoefVehicle() {
		return cVehicle;
	}

	public double getCoefService() {
		return cService;
	}

	public double getCoefTransportation() {
		return cTransportation;
	}

	public double getRo() {
		return ro;
	}

	public void setCoefService(double coefService) {
		this.cService = coefService;
	}

	public void setCoefTransportation(double coefTransportation) {
		this.cTransportation = coefTransportation;
	}
*/
	public void addTravelTime(double travelTime){
		cost.travelTime += travelTime;
	}

	public void addServiceTime(double serviceTime) {
		cost.serviceTime += serviceTime;	
	}

	public void addWaitingTime(double waitingTime) {
		cost.waitingTime += waitingTime;
	}

	public void setCost(Cost cost) {
		this.cost = cost;	
	}

	public static Instance getInstance(){
		return instance;
	}

	public static void setInstance(Instance instance){
		MySolution.instance = instance;
	}

	public ArrayList<Route> getRoutes() {
		return route;
	}

	public Route getRoute(int index){
		return route.get(index);
	}

	public int getRouteNr(){
		return route.size();
	}

	public void setRoute(ArrayList<Route> route) {
		this.route = route;
	}

	public void deleteFromSolution (Route route){
		this.route.remove(route);
	}
}