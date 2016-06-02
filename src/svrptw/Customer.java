package svrptw;

import svrptw.Cost;

/**
 * @author   Ilya
 */
public class Customer {
	/**
	 * @uml.property  name="cost"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Cost cost;
	/**
	 * @uml.property  name="number"
	 */
	private int number;
	/**
	 * @uml.property  name="serviceDuration"
	 */
	private double serviceDuration; 
	/**
	 * @uml.property  name="capacity"
	 */
	private double capacity;
	/**
	 * @uml.property  name="startTw"
	 */
	private int startTw; 
	/**
	 * @uml.property  name="endTw"
	 */
	private int endTw;
	/**
	 * @uml.property  name="assignedDepot"
	 * @uml.associationEnd  inverse="assignedCustomers:svrptw.Depot"
	 */
	private Depot assignedDepot;        // the depot from which the customer will be served
	/**
	 * @uml.property  name="distanceFromDepot"
	 */
	private double distanceFromDepot;
	/**
	 * @uml.property  name="isTaken"
	 */
	private boolean isTaken=false;
	/**
	 * @uml.property  name="routeIndex"
	 */
	private int routeIndex;
	/**
	 * @uml.property  name="eNC"
	 */
	private int ENC;
	
	public Customer() {
		number = 0;
		serviceDuration = 0;
		capacity = 0;
		startTw = 0;
		endTw = 0;
		distanceFromDepot = 0;
		routeIndex =-1;
		ENC = 0;
		cost = new Cost();
	}
	
	public Customer ( Customer copy ) {
		this.cost =				new Cost(copy.cost);
		this.number = 			new Integer(copy.number);
		this.serviceDuration = 	new Double(copy.serviceDuration);
		this.capacity = 		new Double(copy.capacity);
		this.startTw =			new Integer(copy.startTw);
		this.endTw = 			new Integer(copy.endTw);
		this.assignedDepot =	new Depot(copy.assignedDepot);
		this.distanceFromDepot= new Double(copy.distanceFromDepot);
		this.isTaken = 			new Boolean(copy.isTaken);
		this.routeIndex =		new Integer(copy.routeIndex);
		this.ENC = 				new Integer(copy.ENC);
	}
	
	/**
	 * @return
	 * @uml.property  name="number"
	 */
	public int getNumber() {
		return this.number;
	}
	
	/**
	 * @param  customernumber
	 * @uml.property  name="number"
	 */
	public void setNumber(int customernumber) {
		this.number = customernumber;
	}

	/**
	 * @return
	 * @uml.property  name="distanceFromDepot"
	 */
	public double getDistanceFromDepot(){
		return distanceFromDepot;
	}

	/**
	 * @param  distance
	 * @uml.property  name="distanceFromDepot"
	 */
	public void setDistanceFromDepot(double distance){
		this.distanceFromDepot = distance;
	}

	/**
	 * @param  value
	 * @uml.property  name="isTaken"
	 */
	public void setIsTaken(boolean value){
		this.isTaken = value;
	}

	public boolean getIsTaken(){
		return this.isTaken;
	}

	/**
	 * @return
	 * @uml.property  name="serviceDuration"
	 */
	public double getServiceDuration() {
		return serviceDuration;
	}

	/**
	 * @param  serviceduration
	 * @uml.property  name="serviceDuration"
	 */
	public void setServiceDuration(double serviceduration) {
		this.serviceDuration = serviceduration;
	}

	/**
	 * @return
	 * @uml.property  name="capacity"
	 */
	public double getCapacity() {
		return capacity;
	}

	/**
	 * @param  demand
	 * @uml.property  name="capacity"
	 */
	public void setCapacity(double demand) {
		this.capacity = demand;
	}

	/**
	 * @return
	 * @uml.property  name="startTw"
	 */
	public int getStartTw() {
		return startTw;
	}

	/**
	 * @param  startTW
	 * @uml.property  name="startTw"
	 */
	public void setStartTw(int startTW) {
		this.startTw = startTW;
	}

	/**
	 * @return
	 * @uml.property  name="endTw"
	 */
	public int getEndTw() {
		return endTw;
	}

	/**
	 * @param  endTW
	 * @uml.property  name="endTw"
	 */
	public void setEndTw(int endTW) {
		this.endTw = endTW;
	}

	/**
	 * @return
	 * @uml.property  name="assignedDepot"
	 */
	public Depot getAssignedDepot() {
		return assignedDepot;
	}

	/**
	 * @param  assigneddepot
	 * @uml.property  name="assignedDepot"
	 */
	public void setAssignedDepot(Depot assigneddepot) {
		this.assignedDepot = assigneddepot;
	}

	/**
	 * @return
	 * @uml.property  name="routeIndex"
	 */
	public int getRouteIndex() {
		return routeIndex;
	}

	/**
	 * @param  routeIndex
	 * @uml.property  name="routeIndex"
	 */
	public void setRouteIndex(int routeIndex) {
		this.routeIndex = routeIndex;
	}

	/**
	 * @return
	 * @uml.property  name="cost"
	 */
	public Cost getCost() {
		return cost;
	}
	
	/**
	 * @param  cost
	 * @uml.property  name="cost"
	 */
	public void setCost(Cost cost) {
		this.cost = cost;
	}
	
	/**
	 * @return
	 * @uml.property  name="eNC"
	 */
	public int getENC() {
		return ENC;
	}
	
	/**
	 * @param  ENC
	 * @uml.property  name="eNC"
	 */
	public void setENC(int ENC) {
		this.ENC = ENC;
	}
}