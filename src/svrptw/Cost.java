package svrptw;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class stores information about cost of a route or a group of routes. It
 * has total which is the sum of travel, capacityViol, durationViol, twViol.
 */
public class Cost {
	private Instance instance = Instance.getInstance();
	private double capacity;
	private double delay;
	private double earliness;
	private double distance;
	private double vechile;
	private double overtime;
	private double servCost;
	private double tranCost;
	private double totalCost;
	private double expectedTime;
	private double varianceTime;
	private int serviceTime;

	public Cost() {
		this.capacity = 0;
		this.delay = 0;
		this.earliness = 0;
		this.distance = 0;
		this.vechile = 0;
		this.overtime = 0;
		this.servCost = 0;
		this.tranCost = 0;
		this.totalCost = 0;
		this.expectedTime = 0;
		this.varianceTime = 0;
		this.serviceTime = 0;
	}

	// constructor which clone the cost passed as parameter
	public Cost(Cost cost) {
		this.capacity = new Double(cost.getCapacity());
		this.delay = new Double(cost.getDelay());
		this.earliness = new Double(cost.getEarliness());
		this.distance = new Double(cost.getDistance());
		this.vechile = new Double(cost.getVechile());
		this.overtime = new Double(cost.getOvertime());
		this.servCost = new Double(cost.getServiceCost());
		this.tranCost = new Double(cost.getTransportationCost());
		this.totalCost = new Double(cost.getTotalCost());
		this.expectedTime = new Double(cost.getExpectedTime());
		this.varianceTime = new Double(cost.getVarianceTime());
		this.serviceTime = new Integer(cost.getTotalServiceTime());
	}

	public void clear() {
		this.capacity = 0;
		this.delay = 0;
		this.earliness = 0;
		this.distance = 0;
		this.vechile = 0;
		this.overtime = 0;
		this.servCost = 0;
		this.tranCost = 0;
		this.totalCost = 0;
		this.expectedTime = 0;
		this.varianceTime = 0;
	}

	public double getCapacity() {
		return this.capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public void addCapacity(double capacity) {
		this.capacity += capacity;
	}
	
	public double getDelay() {
		return this.delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}
	
	public void addDelay(double delay) {
		this.delay += delay;
	}
	
	public void rangeDelay() {
		this.delay = new BigDecimal(this.delay).setScale(3, RoundingMode.HALF_UP)
				.doubleValue();
	}

	public double getEarliness() {
		return this.earliness;
	}

	public void setEarliness(double earliness) {
		this.earliness = earliness;
	}
	
	public void addEarliness(double earliness) {
		this.earliness += earliness;
	}
	
	public void rangeEarliness() {
		this.earliness = new BigDecimal(this.earliness).setScale(3, RoundingMode.HALF_UP)
				.doubleValue();
	}

	public double getDistance() {
		return this.distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public void addDistance(double distance) {
		this.distance += distance;
	}
	public double getVechile() {
		return this.vechile;
	}

	public void setVechile(double vechile) {
		this.vechile = vechile;
	}

	public double getOvertime() {
		return this.overtime;
	}

	public void setOvertime(double overtime) {
		this.overtime = overtime;
	}
	
	public void rangeOvertime() {
		this.overtime = new BigDecimal(this.overtime).setScale(3, RoundingMode.HALF_UP)
				.doubleValue();
	}

	public double getServiceCost() {
		return this.servCost;
	}

	public double getTransportationCost() {
		return this.tranCost;
	}

	public double getTotalCost() {
		return this.totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public void calculateServiceCost() {
		double coefDelay = instance.getCoefDelay();
		double coefEarliness = instance.getCoefEarliness();
		this.servCost = coefDelay * this.delay + coefEarliness * this.earliness;
	}

	public void calculateTransportationCost() {
		double coefDistance = instance.getCoefDistance();
		double coefOvertime = instance.getCoefOvertime();
		double coefVechile = instance.getCoefVechile();
		this.tranCost = coefDistance * this.distance + coefOvertime
				* this.overtime + coefVechile * this.vechile;
	}

	public void calculateTotalCost() {
		boolean flagTotal = instance.getFlagTotal();
		double coefServ = instance.getCoefService();
		double coefTran = instance.getCoefTransportation();
		double coefRho = instance.getCoefRho();
		calculateServiceCost();
		calculateTransportationCost();
		if (flagTotal) {
			this.totalCost = coefRho * coefServ * this.servCost + (1 - coefRho)
					* coefTran * this.tranCost;
		} else {
			this.totalCost = this.tranCost;
		}
	}

	public double getExpectedTime() {
		return this.expectedTime;
	}

	public void setExpectedTime(double expectedTime) {
		this.expectedTime = expectedTime;
	}
	
	public void addExpectedTime(double expectedTime) {
		this.expectedTime += expectedTime;
	}

	public double getVarianceTime() {
		return this.varianceTime;
	}

	public void setVarianceTime(double varianceTime) {
		this.varianceTime = varianceTime;
	}
	
	public void addVarianceTime(double varianceTime) {
		this.varianceTime += varianceTime;
	}

	public int getTotalServiceTime() {
		return this.serviceTime;
	}

	public void setTotalServiceTime(int serviceTime) {
		this.serviceTime = serviceTime;
	}
	
	public void addTotalServiceTime(int serviceTime) {
		this.serviceTime += serviceTime;
	}

	public boolean checkFeasible() {
		return true;
	}

	public double getObjectiveValue(double coefNu) {
		return this.totalCost;/* + coefNu * capacity; */
	}
}