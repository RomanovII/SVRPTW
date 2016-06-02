package svrptw;

/**
 * @author   Ilya
 */
public class Vehicle {

	/**
	 * @uml.property  name="capacity"
	 */
	private double capacity;

	public Vehicle() {}

	/**
	 * @return    the capacity
	 * @uml.property  name="capacity"
	 */
	public double getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity    the capacity to set
	 * @uml.property  name="capacity"
	 */
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
}
