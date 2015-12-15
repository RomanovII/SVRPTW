package svrptw;

import org.apache.commons.math3.distribution.GammaDistribution;

/**
 * This class stores information about cost of a route or a group of routes.
 * It has total which is the sum of travel, capacityViol, durationViol, twViol.
 */
public class Cost {
	public double totalTransportCost;
	public double totalWeightedCost;
	public double total;			 // sum of all the costs
	public double travelTime;		 // sum of all distances travel time
	public double load;			     // sum of all quantities
	public double serviceTime;		 // sum of all service time;	 
	public double waitingTime;		 // sum of all waiting times when arrives before start TW
	public double loadViol;		     // violation of the load
	public double twViol;            // violation of the time window
	public double returnToDepotTime; // stores time to return to the depot
	public double depotTwViol;       // stores the time window violation of the depot
	public double totalDelay;
	public double totalEarly; 
	public static double ro = 0.5;
	public double C1;
	public double C2;
	
	// default constructor
	public Cost(){
		totalTransportCost 	= 0;
		totalWeightedCost	= 0;
		
		total             = 0;
		travelTime        = 0;
		load		      = 0;
		serviceTime		  = 0;
		waitingTime		  = 0;

		loadViol          = 0;

		twViol            = 0;

		returnToDepotTime = 0;
		depotTwViol       = 0;
		C1 				  = 1;
		C2				  = 1;
		totalDelay 		  = 0;
		totalEarly		  = 0;

	}

	// constructor which clone the cost passed as parameter
	public Cost(Cost cost) {
		this.total             = new Double(cost.total);
		this.travelTime        = new Double(cost.travelTime);
		this.load              = new Double(cost.load);
		this.serviceTime       = new Double(cost.serviceTime);
		this.waitingTime       = new Double(cost.waitingTime);

		this.loadViol          = new Double(cost.loadViol);
		this.twViol            = new Double(cost.twViol);

		this.returnToDepotTime = new Double(cost.returnToDepotTime);
		this.depotTwViol       = new Double(cost.depotTwViol);	
		this.totalTransportCost = new Double(cost.totalTransportCost);
		this.totalWeightedCost = new Double(cost.totalWeightedCost);
		this.totalEarly 	   = new Double(cost.totalEarly);
		this.totalDelay		 	= new Double(cost.totalDelay);
		this.C1					= new Double(cost.C1);
		this.C2					= new Double(cost.C2);
	}


	public String toString() {
		StringBuffer print = new StringBuffer();
		print.append("--- Cost -------------------------------------");
		print.append("\n" + "| TotalTravelCost=" + travelTime + " TotalCostViol=" + total);
		print.append("\n" + "| LoadViol=" + loadViol  + " TWViol=" + twViol);
		print.append("\n" + "--------------------------------------------------" + "\n");
		return print.toString();
	}

	public void calculateTotalCostViol() {
		total = travelTime + loadViol + twViol ;
		totalTransportCost = travelTime + loadViol + twViol;
		totalWeightedCost = ro*(totalDelay + totalEarly)/C1 + (1 - ro)*(totalTransportCost)/C2;
	}

	public void setDelay(double alpha, double value){
		double lamda =  1;
		double temp = alpha * lamda;// * (1 - new GammaDistribution(alpha + 1, lamda).density(value))  - value * (1 - new GammaDistribution(alpha, lamda).density(value));
		if (temp != temp)
			this.totalDelay = 0;
		this.totalDelay = temp;
	}
	
	public void setC(){
		this.C1 = Math.max(1, this.total);
		this.C2 = Math.max(1, this.totalDelay + this.totalEarly);
	}
	
	public void setEarly(double alpha, double value){
		double lamda =  1;
		double temp =  value * lamda;// * new GammaDistribution(alpha, lamda).density(value) - alpha * lamda * new GammaDistribution(alpha + 1, lamda).density(value);
		if (temp != temp)
			this.totalEarly = 0;
		this.totalEarly = temp;
	}
	public void addDelay(double alpha, double value){
		double lamda =  1;
		double temp = alpha * lamda;// * (1 - new GammaDistribution(alpha + 1, lamda).density(value))  - value * (1 - new GammaDistribution(alpha, lamda).density(value));
		if (temp != temp)
			this.totalDelay = 0;
		this.totalDelay = temp;
	}
	
	public void addEarly(double alpha, double value){
		double lamda =  1;
		double temp =  value * lamda;// * new GammaDistribution(alpha, lamda).density(value) - alpha * lamda * new GammaDistribution(alpha + 1, lamda).density(value);
		if (temp != temp)
			this.totalEarly = 0;
		this.totalEarly = temp;
	}

	/**
	 * Set the total cost based on alpha, beta, gamma
	 * @param alpha
	 * @param beta
	 * @param gamma
	 */
	public void  calculateTotal(double alpha, double beta, double gamma) {
		total = travelTime+ alpha * loadViol + gamma * twViol;
		if (depotTwViol > 0)
			depotTwViol = 0;
		totalTransportCost = travelTime + loadViol + twViol;
		totalWeightedCost = ro*(totalDelay + totalEarly)/C1 + (1 - ro)*(totalTransportCost)/C2;
	}

	
	public void calculatedTotalTransportationCost(double cDistance, double cVehicle, double cOvertime) {
		total = cDistance * travelTime + cVehicle ;
	}

	public void setLoadViol(double capacityviol) {
		this.loadViol = capacityviol;
	}


	public void addLoadViol(double capacityviol) {
		this.loadViol += capacityviol;
	}

	public void addTWViol(double TWviol) {
		this.twViol += TWviol;
	}


	/**
	 * Add cost to the total cost
	 * @param cost
	 */
	public void addTravel(double cost) {
		travelTime +=cost;
	}

	public void setTravelTime(double travelTime) {
		this.travelTime = travelTime;
	}

	/**
	 * @return the totalcost
	 */
	public double getTravel() {
		return travelTime;
	}

	/**
	 * @return the totalcostviol
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @return the capacityviol
	 */

	public double getLoadViol() {
		return loadViol;
	}


	/**
	 * @return the tWviol
	 */

	public double getTwViol() {
		return twViol;
	}

	public void initialize() {
		totalTransportCost 	= 0;
		totalWeightedCost	= 0;
		
		total             	= 0;
		travelTime        	= 0;
		load		      	= 0;
		serviceTime		  	= 0;
		waitingTime			= 0;

		loadViol			= 0;
		twViol       		= 0;

		returnToDepotTime 	= 0;
		depotTwViol       	= 0;
		C1 				  	= 1;
		C2				 	= 1;
		totalDelay 			= 0;
		totalEarly			= 0;
	}


	// check if a cost has violations
	public boolean checkFeasible() {
		if (this.loadViol == 0 && this.twViol == 0) {
			return true;
		} else {
			return false;
		}
	}

	public double getDuration(){
		return this.serviceTime + this.waitingTime;
	}

	/**
	 * @return the load
	 */
	public double getLoad() {
		return load;
	}

	/**
	 * @param load the load to set
	 */
	public void setLoad(double load) {
		this.load = load;
	}

	/**
	 * @return the serviceTime
	 */
	public double getServiceTime() {
		return serviceTime;
	}

	/**
	 * @param serviceTime the serviceTime to set
	 */
	public void setServiceTime(double serviceTime) {
		this.serviceTime = serviceTime;
	}

	/**
	 * @return the waitingTime
	 */
	public double getWaitingTime() {
		return waitingTime;
	}

	/**
	 * @param waitingTime the waitingTime to set
	 */
	public void setWaitingTime(double waitingTime) {
		this.waitingTime = waitingTime;
	}

	/**
	 * @return the returnToDepotTime
	 */
	public double getReturnToDepotTime() {
		return returnToDepotTime;
	}

	/**
	 * @param returnToDepotTime the returnToDepotTime to set
	 */
	public void setReturnToDepotTime(double returnToDepotTime) {
		this.returnToDepotTime = returnToDepotTime;
	}

	/**
	 * @return the depotTwViol
	 */

	public double getDepotTwViol() {
		return depotTwViol;
	}

	/**
	 * @param depotTwViol the depotTwViol to set
	 */

	public void setDepotTwViol(double depotTwViol) {
		this.depotTwViol = depotTwViol;
	}

	/**
	 * @return the travelTime
	 */

	public double getTravelTime() {
		return travelTime;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * @param twViol the twViol to set
	 */

	public void setTwViol(double twViol) {
		this.twViol = twViol;
	}
}
