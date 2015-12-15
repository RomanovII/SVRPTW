package svrptw;

import heneticmethod.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
/**
 * Instance class holds all the information about the problem, customers, depots, vehicles.
 * It offers functions to grab all the data from a file print it formated and all the function
 * needed for the initial solution.
 */
public class Instance {
	private int vehiclesNr;
	private int customersNr;
	private ArrayList<Customer> sortCustomers 	= new ArrayList<>(); 		// vector of customers;
	private ArrayList<Customer> allCustomers 	= new ArrayList<>();
	private Depot depot;
	private double capacity;
	private double[][] distance;
	private double[][] time;
	private double[][] velocity;
	private Random random 					= new Random();
	private Parameters parameters;

	public Instance(Parameters parameters) 
	{
		this.setParameters(parameters);
		// set the random seed if passed as parameter
		if(parameters.getRandomSeed() != -1)
			random.setSeed(parameters.getRandomSeed());
	}

	
	/**
	 * Read from arg the problem data: D and Q, customers data and depots data.
	 * After the variables are populated calculates the distances, assign
	 * customers to depot and calculates angles
	 * 
	 * @param filename
	 */
	public void populate(Matrix matrix) {
		vehiclesNr = matrix.maxCars;
		capacity = matrix.MaxMoney;
		customersNr = matrix.distanceCoeffs.length - 1;
		
		distance = matrix.distanceCoeffs;
		time = matrix.timeCoeffs;
		
		depot = new Depot();
		depot.setNumber(0);
		depot.setStartTW(matrix.getTimeWindow(0).StartWork);
		depot.setEndTW(matrix.getTimeWindow(0).EndWork);
		depot.setENC(matrix.ENC[0]);

		for (int i = 1; i < customersNr; ++i) {
			Customer customer = new Customer();
			customer.setNumber(i);
			customer.setENC(matrix.ENC[i]);
			customer.setCapacity(matrix.amountOfMoney[i]);
			customer.setStartTw(matrix.getTimeWindow(i).StartWork);
			customer.setEndTw(matrix.getTimeWindow(i).EndWork);
			customer.setServiceDuration(matrix.serviceTime[i]);
			customer.setDistanceFromDepot(distance[i][0]);
			
			customer.setArriveTime(distance[i][0]); //WHAT?
			
			sortCustomers.add(customer);
			allCustomers.add(customer);
		}

		calculateVelocity();
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
	 * Calculate the matrix of velocity
	 */
	public void calculateVelocity() 
	{
		for (int i = 0; i  < customersNr; ++i)
		{
			for (int j = 0; j < customersNr; ++j)
			{
				velocity[i][j] = distance[i][j] / time[i][j];
			}
		}
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
	public double getCapacity() {
		return capacity;
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
	
	/**
	 * @return all the customers as string
	 */
	public String printCustomers() {
		StringBuffer print = new StringBuffer();
		for (int i = 0; i < customersNr; ++i) {
			print.append(customers.get(i));
		}
		return print.toString();
	}
}
