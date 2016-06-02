package svrptw;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class stores information about cost of a route or a group of routes. It has total which is the sum of travel, capacityViol, durationViol, twViol.
 */
public class Cost {
	/**
	 * @uml.property  name="capacity"
	 */
	private double capacity;
	/**
	 * @uml.property  name="delay"
	 */
	private double delay;
	/**
	 * @uml.property  name="earliness"
	 */
	private double earliness;
	/**
	 * @uml.property  name="distance"
	 */
	private double distance;
	/**
	 * @uml.property  name="overtime"
	 */
	private double overtime;
	/**
	 * @uml.property  name="expectedTime"
	 */
	private double expectedTime;
	/**
	 * @uml.property  name="varianceTime"
	 */
	private double varianceTime;
	/**
	 * @uml.property  name="serviceTime"
	 */
	private int serviceTime;

	public Cost() {
		this.capacity = 0;
		this.delay = 0;
		this.earliness = 0;
		this.overtime = 0;
		this.distance = 0;
		this.expectedTime = 0;
		this.varianceTime = 0;
		this.serviceTime = 0;
	}

	// constructor which clone the cost passed as parameter
	public Cost(Cost cost) {
		this.capacity = new Double(cost.getCapacity());
		this.delay = new Double(cost.getDelay());
		this.earliness = new Double(cost.getEarliness());
		this.overtime = new Double(cost.getOvertime());
		this.distance = new Double(cost.getDistance());
		this.expectedTime = new Double(cost.getExpectedTime());
		this.varianceTime = new Double(cost.getVarianceTime());
		this.serviceTime = new Integer(cost.getTotalServiceTime());
	}

	public void addCost(Cost cost) {
		this.capacity += cost.getCapacity();
		this.delay += cost.getDelay();
		this.earliness += cost.getEarliness();
		this.overtime += cost.getOvertime();
		this.distance += cost.getDistance();
		this.expectedTime += cost.getExpectedTime();
		this.varianceTime += cost.getVarianceTime();
		this.serviceTime += cost.getTotalServiceTime();
	}

	public void clear() {
		this.capacity = 0;
		this.delay = 0;
		this.earliness = 0;
		this.overtime = 0;
		this.distance = 0;
		this.expectedTime = 0;
		this.varianceTime = 0;
		this.serviceTime = 0;
	}

	private double range(double value) {
		return new BigDecimal(value).setScale(3, RoundingMode.HALF_UP)
				.doubleValue();
	}
	
	/**
	 * @return
	 * @uml.property  name="capacity"
	 */
	public double getCapacity() {
		return this.capacity;
	}

	/**
	 * @param  capacity
	 * @uml.property  name="capacity"
	 */
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public void addCapacity(double capacity) {
		this.capacity += capacity;
	}

	/**
	 * @return
	 * @uml.property  name="delay"
	 */
	public double getDelay() {
		return this.delay;
	}

	/**
	 * @param  delay
	 * @uml.property  name="delay"
	 */
	public void setDelay(double delay) {
		this.delay = range(delay);
	}

	public void addDelay(double delay) {
		this.delay += range(delay);
	}

	/**
	 * @return
	 * @uml.property  name="earliness"
	 */
	public double getEarliness() {
		return this.earliness;
	}

	/**
	 * @param  earliness
	 * @uml.property  name="earliness"
	 */
	public void setEarliness(double earliness) {
		this.earliness = range(earliness);
	}

	public void addEarliness(double earliness) {
		this.earliness += range(earliness);
	}
	
	/**
	 * @return
	 * @uml.property  name="overtime"
	 */
	public double getOvertime() {
		return this.overtime;
	}

	/**
	 * @param  overtime
	 * @uml.property  name="overtime"
	 */
	public void setOvertime(double overtime) {
		this.overtime = range(overtime);
	}

	/**
	 * @return
	 * @uml.property  name="distance"
	 */
	public double getDistance() {
		return this.distance;
	}

	/**
	 * @param  distance
	 * @uml.property  name="distance"
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	public void addDistance(double distance) {
		this.distance += distance;
	}
//
//	public void calculateServiceCost() {
//		double coefDelay = instance.getCoefDelay();
//		double coefEarliness = instance.getCoefEarliness();
//		this.servCost = coefDelay * this.delay + coefEarliness * this.earliness;
//	}
//
//	public void calculateTransportationCost() {
//		double coefDistance = instance.getCoefDistance();
//		double coefOvertime = instance.getCoefOvertime();
//		double coefVechile = instance.getCoefVechile();
//		this.tranCost = coefDistance * this.distance + coefOvertime
//				* this.overtime + coefVechile * this.vechile;
//	}
//
//	public void calculateTotalCost() {
//		boolean flagTotal = instance.getFlagTotal();
//		double coefServ = instance.getCoefService();
//		double coefTran = instance.getCoefTransportation();
//		double coefRho = instance.getCoefRho();
//		calculateServiceCost();
//		calculateTransportationCost();
//		if (flagTotal) {
//			this.totalCost = coefRho * coefServ * this.servCost + (1 - coefRho)
//					* coefTran * this.tranCost;
//		} else {
//			this.totalCost = this.tranCost;
//		}
//	}

	/**
	 * @return
	 * @uml.property  name="expectedTime"
	 */
	public double getExpectedTime() {
		return this.expectedTime;
	}

	/**
	 * @param  expectedTime
	 * @uml.property  name="expectedTime"
	 */
	public void setExpectedTime(double expectedTime) {
		this.expectedTime = expectedTime;
	}

	public void addExpectedTime(double expectedTime) {
		this.expectedTime += expectedTime;
	}

	/**
	 * @return
	 * @uml.property  name="varianceTime"
	 */
	public double getVarianceTime() {
		return this.varianceTime;
	}

	/**
	 * @param  varianceTime
	 * @uml.property  name="varianceTime"
	 */
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
}