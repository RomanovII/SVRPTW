package svrptw;

import heneticmethod.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import org.apache.commons.math3.distribution.GammaDistribution;
/**
 * Instance class holds all the information about the problem, customers, depots, vehicles.
 * It offers functions to grab all the data from a file print it formated and all the function
 * needed for the initial solution.
 */
public class Instance {
	private static Instance instance = new Instance();

	private ArrayList<Customer> sortCustomers 	= new ArrayList<>();
	private ArrayList<Customer> allCustomers 	= new ArrayList<>();
	private Random random = new Random();
	private Parameters parameters;
	private Depot depot;
	private int vehiclesNr;
	private int customersNr;
	private int[][] distance;
	private int[][] time;
	private double vechiclesCapacity;

	private Instance(){
		this.parameters = new Parameters();
	}
	
	public static Instance getInstance() {
		return instance;
	}

	public void populate(Matrix matrix) {
		vehiclesNr = matrix.maxCars;
		vechiclesCapacity = matrix.MaxMoney;
		customersNr = matrix.distanceCoeffs.length - 1;
		
		distance = matrix.distanceCoeffs;
		time = matrix.timeCoeffs;
		
		depot = new Depot();
		depot.setNumber(0);
		depot.setStartTw(matrix.getTimeWindow(0).StartWork);
		depot.setEndTw(matrix.getTimeWindow(0).EndWork);
		depot.setENC(matrix.ENC[0]);
		allCustomers.add(depot);
		
		for (int i = 1; i <= customersNr; ++i) {
			Customer customer = new Customer();
			customer.setNumber(i);
			customer.setENC(matrix.ENC[i]);
			customer.setCapacity(matrix.amountOfMoney[i]);
			customer.setStartTw(matrix.getTimeWindow(i).StartWork);
			customer.setEndTw(matrix.getTimeWindow(i).EndWork);
			customer.setServiceDuration(matrix.serviceTime[i]);
			customer.setDistanceFromDepot(distance[0][i]);
					
			sortCustomers.add(customer);
			allCustomers.add(customer);
		}
		assignCustomersToDepots();
		Collections.sort(sortCustomers, new CompareDistances());
	}
	
	/**
	 * For each customer set the depot and assign to the depot the customers
	 */
	public void assignCustomersToDepots() {
		for (int i = 0; i < customersNr; ++i){
			allCustomers.get(i).setAssignedDepot(depot);
			sortCustomers.get(i).setAssignedDepot(depot);
			depot.addAssignedCustomer(sortCustomers.get(i));
		}
	}
	
	/**
	 * @return the distance necessary to travel from node 1 to node 2
	 */
	public double getDistance(int i, int j) {
		return distance[i][j];
	}
	
	/**
	 * @return the time necessary to travel from node 1 to node 2
	 */
	public double getTime(int i, int j) {
		return time[i][j];
	}
	
	/**
	 * @return the vehiclesNr
	 */
	public int getVehiclesNr() {
		return vehiclesNr;
	}
	
	/**
	 * @return the parameters
	 */
	public Parameters getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	/**
	 * @param random the random to set
	 */
	public void setRandom(Random random) {
		this.random = random;
	}

	/**
	 * @return the precision
	 */
	public double getPrecision(){
		return parameters.getPrecision();
	}
	
	/**
	 * @return the depot
	 */
	public Depot getDepot() {
		return depot;
	}
	
	/**
	 * @return the capacity
	 */
	public double getVechileCapacity() {
		return vechiclesCapacity;
	}
	
	/**
	 * @return the customers number
	 */
	public int getCustomersNr() {
		return allCustomers.size();
	}
	
	/**
	 * @return the all customers
	 */
	public ArrayList<Customer> getCustomers() {
		return allCustomers;
	}
			
	/**
	 * @return the sorted customers
	 */
	public ArrayList<Customer> getSortedCustomers() {
		return sortCustomers;
	}
	
	public double getCoefDelay() {
		return parameters.getCoefDelay();
	}

	public double getCoefEarliness() {
		return parameters.getCoefEarliness();
	}

	public double getCoefDistance() {
		return parameters.getCoefDistance();
	}

	public double getCoefOvertime() {
		return parameters.getCoefOvertime();
	}

	public double getCoefVechile() {
		return parameters.getCoefVechile();
	}
	
	public boolean getFlagTotal() {
		return parameters.getFlagTotal();
	}
	
	public void setFlagTotal(boolean flagTotal) {
		parameters.setFlagTotal(flagTotal);
	}

	public double getCoefService() {
		return parameters.getCoefService();
	}
	
	public void setCoefService(double coefService) {
		parameters.setCoefService(coefService);
	}

	public double getCoefTransportation() {
		return parameters.getCoefTransportation();
	}
	
	public void setCoefTransportation(double coefTransportation) {
		parameters.setCoefTransportation(coefTransportation);
	}

	public double getCoefRho() {
		return parameters.getCoefRho();
	}
	
	public void setCoefRho(double coefRho) {
		parameters.setCoefRho(coefRho);
	}

	public double getCoefPhi() {
		return parameters.getCoefPhi();
	}
	
	public void setCoefPhi(double coefPhi) {
		parameters.setCoefPhi(coefPhi);
	}
	
	public double getShape() {
		return parameters.getShape();
	}
	
	public double getScale() {
		return parameters.getScale();
	}
	
	public int getTenure() {
		return parameters.getTenure();
	}
	
	public double getGamma(double shape, double scale, double value) {
		if (shape == 0 || scale == 0) {
			System.out.println("WTF???");
			return 0;
		}
		GammaDistribution gd = new GammaDistribution(shape, scale);
		return gd.cumulativeProbability(value);
	}
	
	public ArrayList<Customer> calculateTimeToCustomer(Customer c) {
		@SuppressWarnings("unchecked")
		ArrayList<Customer> list = (ArrayList<Customer>) allCustomers.clone();
		list.remove(0);
		list.remove(c);
		Collections.sort(list, new CompareTime(c.getNumber()));
		return list;
	}
	
	/**
	 * @return all the customers as string
	 */
	public String printCustomers() {
		StringBuffer print = new StringBuffer();
		for (int i = 0; i < customersNr; ++i) {
			print.append(sortCustomers.get(i));
		}
		return print.toString();
	}
}
