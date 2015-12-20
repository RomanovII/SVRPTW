package svrptw;

import svrptw.Cost;

public class Customer {
	private Cost cost;
	private int number;
	private double serviceDuration; 
	private double capacity;
	private int startTw; 
	private int endTw;
	private Depot assignedDepot;        // the depot from which the customer will be served
	private double distanceFromDepot;
	private boolean isTaken=false;
	private int routeIndex;
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
	
	public int getNumber() {
		return this.number;
	}
	
	public void setNumber(int customernumber) {
		this.number = customernumber;
	}

	public double getDistanceFromDepot(){
		return distanceFromDepot;
	}

	public void setDistanceFromDepot(double distance){
		this.distanceFromDepot = distance;
	}

	public void setIsTaken(boolean value){
		this.isTaken = value;
	}

	public boolean getIsTaken(){
		return this.isTaken;
	}

	public double getServiceDuration() {
		return serviceDuration;
	}

	public void setServiceDuration(double serviceduration) {
		this.serviceDuration = serviceduration;
	}

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double demand) {
		this.capacity = demand;
	}

	public int getStartTw() {
		return startTw;
	}

	public void setStartTw(int startTW) {
		this.startTw = startTW;
	}

	public int getEndTw() {
		return endTw;
	}

	public void setEndTw(int endTW) {
		this.endTw = endTW;
	}

	public Depot getAssignedDepot() {
		return assignedDepot;
	}

	public void setAssignedDepot(Depot assigneddepot) {
		this.assignedDepot = assigneddepot;
	}

	public int getRouteIndex() {
		return routeIndex;
	}

	public void setRouteIndex(int routeIndex) {
		this.routeIndex = routeIndex;
	}

	public Cost getCost() {
		return cost;
	}
	
	public void setCost(Cost cost) {
		this.cost = cost;
	}
	
	public int getENC() {
		return ENC;
	}
	
	public void setENC(int ENC) {
		this.ENC = ENC;
	}
}